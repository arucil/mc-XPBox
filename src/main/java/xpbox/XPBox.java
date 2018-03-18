package xpbox;

import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XPBox.MODID, name = XPBox.NAME, version = XPBox.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        useMetadata = true)
public class XPBox {
    public static final String MODID = "xpbox";
    public static final String NAME = "XPBox";
    public static final String VERSION = "1.0";

    public static Block blockXPBox = new BlockXPBox();

    @CapabilityInject(XPCapability.class)
    public static final Capability<XPCapability> XP_CAPABILITY = null;

    @Mod.Instance(MODID)
    public static XPBox instance;

    @SidedProxy(serverSide = "xpbox.CommonProxy",
            clientSide = "xpbox.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        // init network
        PacketHandler.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        // register xp capability
        CapabilityManager.INSTANCE.register(XPCapability.class,
                new XPCapability.Storage(),
                XPCapability::new);

    }

}
