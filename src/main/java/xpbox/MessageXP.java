package xpbox;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageXP implements IMessage {
   public int xp;
   public int playerXP;

   public MessageXP(int xp, int playerXP) {
      this.xp = xp;
      this.playerXP = playerXP;
   }

   public MessageXP() {}

   @Override
   public void fromBytes(ByteBuf buf) {
      xp = buf.readInt();
      playerXP = buf.readInt();
   }

   @Override
   public void toBytes(ByteBuf buf) {
      buf.writeInt(xp);
      buf.writeInt(playerXP);
   }

   public static class Handler implements IMessageHandler<MessageXP, IMessage> {
      @Override
      public IMessage onMessage(MessageXP message, MessageContext ctx) {
         FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(
               () -> {
                  if (null == GuiXPBox.instance)
                     Minecraft.getMinecraft().displayGuiScreen(
                           new GuiXPBox(message.xp, message.playerXP));
                  else
                     GuiXPBox.instance.setXPAndPlayerXP(message.xp, message.playerXP);
               });
         return null;
      }
   }
}
