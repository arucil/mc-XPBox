package xpbox;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = XPBox.MODID, name = XPBox.NAME, version = XPBox.VERSION,
      acceptedMinecraftVersions = "[1.12]")
public class XPBox {
   public static final String MODID = "xpbox";
   public static final String NAME = "XPBox";
   public static final String VERSION = "1.0.1.12";

   public static Block blockXPBox;

   @CapabilityInject(XPCapability.class)
   public static final Capability<XPCapability> XP_CAPABILITY = null;

   @SidedProxy(serverSide = "xpbox.CommonProxy",
         clientSide = "xpbox.ClientProxy")
   public static CommonProxy proxy;

   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent e) {
	   
	   //register blockxpbox
	   ForgeRegistries.BLOCKS.register(blockXPBox=new BlockXPBox().setUnlocalizedName("xpbox"));
	   
	   //register itemxpbox
	   ForgeRegistries.ITEMS.register(new ItemBlock(blockXPBox).setRegistryName(blockXPBox.getRegistryName()));
	   
	   proxy.registerItemRenderer(Item.getItemFromBlock(blockXPBox));

      // init network
      PacketHandler.init();
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent e) { 
      // register recipe
      BlockXPBox.registerRecipe();

      // register xp capability
      CapabilityManager.INSTANCE.register(XPCapability.class,
            new XPCapability.Storage(),
            XPCapability::new);

      // register event handler
      MinecraftForge.EVENT_BUS.register(this);
      
   }

   @SubscribeEvent
   public void attachCapability(AttachCapabilitiesEvent<Entity> e) {
      if (e.getObject() instanceof EntityPlayer
            && !e.getObject().hasCapability(XP_CAPABILITY, null))
         e.addCapability(new ResourceLocation(MODID, "xpCapability"),
               new XPCapability.Provider());
   }

   @SubscribeEvent
   public void onPlayerClone(PlayerEvent.Clone e) {
      // may be returning from the End or respawning after death
      e.getEntity().getCapability(XPBox.XP_CAPABILITY, null).value =
            e.getOriginal().getCapability(XPBox.XP_CAPABILITY, null).value;
   }
}
