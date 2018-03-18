package xpbox;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof EntityPlayer
                && !e.getObject().hasCapability(XPBox.XP_CAPABILITY, null))
            e.addCapability(new ResourceLocation(XPBox.MODID, "xpCapability"),
                    new XPCapability.Provider());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone e) {
        // may be returning from the End or respawning after death
        e.getEntity().getCapability(XPBox.XP_CAPABILITY, null).value =
                e.getOriginal().getCapability(XPBox.XP_CAPABILITY, null).value;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(XPBox.blockXPBox);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(XPBox.blockXPBox).setRegistryName(XPBox.blockXPBox.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        XPBox.proxy.registerItemRenderer(Item.getItemFromBlock(XPBox.blockXPBox));
    }
}
