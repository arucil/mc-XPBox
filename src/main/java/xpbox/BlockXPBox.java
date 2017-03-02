package xpbox;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockXPBox extends Block {
   public static final String NAME = "xpbox";

   public BlockXPBox() {
      super(Material.ROCK);
      setRegistryName(NAME);
      setUnlocalizedName(NAME);
      setHardness(.5f);
      setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!worldIn.isRemote) {
         sendXPMessage(playerIn);
      }
      return true;
   }

   private static void sendXPMessage(EntityPlayer player) {
      PacketHandler.wrapper.sendTo(
            new MessageXP(player.getCapability(XPBox.XP_CAPABILITY, null).value,
                  getTotalXP(player)),
            (EntityPlayerMP) player);
   }

   public static int getTotalXP(EntityPlayer player) {
      int xp = 0;
      for (int lv = 0; lv < player.experienceLevel; ++lv) {
         xp += xpBarCap(lv);
      }
      return xp + Math.round(player.experience * player.xpBarCap());
   }

   private static int xpBarCap(int lv) {
      return lv >= 30 ? 112 + (lv - 30) * 9 : (lv >= 15 ? 37 + (lv - 15) * 5 : 7 + lv * 2);
   }

   public static void updateXP(EntityPlayer player, int op, int xp) {
      int exp;
      XPCapability cap = player.getCapability(XPBox.XP_CAPABILITY, null);

      switch (op) {
      case MessageOp.WITHDRAW:
         player.addExperience(xp);
         cap.value -= xp;
         break;
      case MessageOp.DEPOSIT:
         exp = getTotalXP(player);
         player.experience = 0;
         player.experienceLevel = 0;
         player.experienceTotal = 0;
         player.addExperience(exp - xp);
         cap.value += xp;
         break;
      case MessageOp.SETLEVEL:
         exp = getTotalXP(player) + cap.value;
         player.experience = 0;
         player.experienceLevel = 0;
         player.experienceTotal = 0;
         for (int lv = 0; lv < xp; ++lv) {
            int t = xpBarCap(lv);
            if (exp >= t) {
               exp -= t;
               player.addExperience(t);
            } else {
               player.addExperience(exp);
               exp = 0;
               break;
            }
         }
         if (exp > GuiXPBox.MAX)
            exp = GuiXPBox.MAX;
         cap.value = exp;
         break;
      }
      sendXPMessage(player);
   }
}
