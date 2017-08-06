package xpbox;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOp implements IMessage {
   public int op;
   public int xp;

   public static final int DEPOSIT = 1;
   public static final int WITHDRAW = 2;
   public static final int SETLEVEL = 3;

   public MessageOp() {}

   public MessageOp(int op, int xp) {
      this.op = op;
      this.xp = xp;
   }

   @Override
   public void fromBytes(ByteBuf buf) {
      op = buf.readInt();
      xp = buf.readInt();
   }

   @Override
   public void toBytes(ByteBuf buf) {
      buf.writeInt(op);
      buf.writeInt(xp);
   }

   public static class Handler implements IMessageHandler<MessageOp, IMessage> {
      @Override
      public IMessage onMessage(MessageOp message, MessageContext ctx) {
         FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(
               () -> BlockXPBox.updateXP(ctx.getServerHandler().player,
                     message.op, message.xp));
         return null;
      }
   }
}
