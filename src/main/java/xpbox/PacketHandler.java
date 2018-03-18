package xpbox;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static SimpleNetworkWrapper wrapper;

    public static void init() {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(XPBox.MODID);

        wrapper.registerMessage(MessageXP.Handler.class, MessageXP.class, 1, Side.CLIENT);
        wrapper.registerMessage(MessageOp.Handler.class, MessageOp.class, 2, Side.SERVER);
    }
}
