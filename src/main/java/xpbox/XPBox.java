package xpbox;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = XPBox.MODID, name = XPBox.NAME, version = XPBox.VERSION,
      acceptedMinecraftVersions = "[1.10.2]")
public class XPBox {
   public static final String MODID = "xpbox";
   public static final String NAME = "XPBox";
   public static final String VERSION = "1.0";

   public static Block blockXPBox;

   @CapabilityInject(XPCapability.class)
   public static final Capability<XPCapability> XP_CAPABILITY = null;

   @SidedProxy(serverSide = "xpbox.CommonProxy",
         clientSide = "xpbox.ClientProxy")
   public static CommonProxy proxy;

   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent e) {
      // register block
      GameRegistry.register(blockXPBox = new BlockXPBox());
      ItemBlock item = new ItemBlock(blockXPBox);
      GameRegistry.register(item, blockXPBox.getRegistryName());

      proxy.registerItemRenderer(item);

      // init network
      PacketHandler.init();
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent e) {
      // register recipe
      GameRegistry.addShapedRecipe(new ItemStack(blockXPBox),
            "OGO",
            "ICI",
            "ERE",
            'O', Blocks.OBSIDIAN,
            'E', Items.ENDER_PEARL,
            'I', Items.IRON_INGOT,
            'R', Items.REDSTONE,
            'C', Blocks.CHEST,
            'G', Blocks.GLASS_PANE);

      // register xp capability
      CapabilityManager.INSTANCE.register(XPCapability.class,
            new XPCapability.Storage(),
            XPCapability::new);

      // register event handler
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void attachCapability(AttachCapabilitiesEvent.Entity e) {
      if (e.getEntity() instanceof EntityPlayer
            && !e.getEntity().hasCapability(XP_CAPABILITY, null))
         e.addCapability(new ResourceLocation(MODID, "xpCapability"),
               new XPCapability.Provider());
   }

   @SubscribeEvent
   public void onPlayClone(PlayerEvent.Clone e) {
      // may be returning from the End or respawning after death
      e.getEntity().getCapability(XPBox.XP_CAPABILITY, null).value =
            e.getOriginal().getCapability(XPBox.XP_CAPABILITY, null).value;
   }
}
