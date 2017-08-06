package xpbox;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiXPBox extends GuiScreen {
   private static final ResourceLocation GUI_TEXTURE =
         new ResourceLocation(XPBox.MODID, "textures/gui/xpbox.png");

   private static final int[] TextureX = { 2, 13, 24, 35, 46, 57, 68, 79, 90, 101, 112 };

   private static final int GUI_WIDTH = 176, GUI_HEIGHT = 166;
   private static final int BUTTON_WIDTH = 20, BUTTON_HEIGHT = 20;

   private static final int DIGITS = 7;
   public static final int MAX = 9999999;

   private static final int BTN_0 = 0,
         BTN_1 = 1,
         BTN_2 = 2,
         BTN_3 = 3,
         BTN_4 = 4,
         BTN_5 = 5,
         BTN_6 = 6,
         BTN_7 = 7,
         BTN_8 = 8,
         BTN_9 = 9,
         BTN_DEL = 10,
         BTN_ALLDEP = 11,
         BTN_ALLWD = 12,
         BTN_DEP = 13,
         BTN_WD = 14,
         BTN_SETLVL = 15;

   public static GuiXPBox instance;
   public int xp, playerXP;
   private int inputXP;
   private int[] xpDigits, playerXPDigits, inputXPDigits;

   public GuiXPBox(int xp, int playerXP) {
      this.xp = xp;
      this.playerXP = playerXP;
      xpDigits = new int[DIGITS];
      playerXPDigits = new int[DIGITS];
      inputXPDigits = new int[DIGITS];

      setInputXPDigits();

      instance = this;
   }

   public void setXPAndPlayerXP(int xp, int playerXP) {
      this.xp = xp;
      this.playerXP = playerXP;
      updateScreen();
   }

   @Override
   public void onGuiClosed() {
      instance = null;
   }

   private void setXPAndPlayerXPDigits() {
      int i = DIGITS;
      int t = xp;

      do {
         xpDigits[--i] = t % 10;
      } while ((t /= 10) != 0);
      while (--i >= 0)
         xpDigits[i] = 10;

      i = DIGITS;
      t = playerXP;
      if (t > MAX)
         t = MAX;
      do {
         playerXPDigits[--i] = t % 10;
      } while ((t /= 10) != 0);
      while (--i >= 0)
         playerXPDigits[i] = 10;
   }

   private void setInputXPDigits() {
      int i = DIGITS;
      int t = inputXP;

      do {
         inputXPDigits[--i] = t % 10;
      } while ((t /= 10) != 0);
      while (--i >= 0)
         inputXPDigits[i] = 10;
   }

   @Override
   public void initGui() {
      super.initGui();
      int x = width - GUI_WIDTH >> 1;
      int y = height - GUI_HEIGHT >> 1;

      buttonList.add(new GuiButton(BTN_1, x + 11, y + 62, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_1")));
      buttonList.add(new GuiButton(BTN_2, x + 11 + 25, y + 62, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_2")));
      buttonList.add(new GuiButton(BTN_3, x + 11 + 50, y + 62, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_3")));
      buttonList.add(new GuiButton(BTN_4, x + 11, y + 62 + 25, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_4")));
      buttonList.add(new GuiButton(BTN_5, x + 11 + 25, y + 62 + 25, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_5")));
      buttonList.add(new GuiButton(BTN_6, x + 11 + 50, y + 62 + 25, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_6")));
      buttonList.add(new GuiButton(BTN_7, x + 11, y + 62 + 50, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_7")));
      buttonList.add(new GuiButton(BTN_8, x + 11 + 25, y + 62 + 50, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_8")));
      buttonList.add(new GuiButton(BTN_9, x + 11 + 50, y + 62 + 50, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_9")));
      buttonList.add(new GuiButton(BTN_0, x + 11, y + 62 + 75, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_0")));
      buttonList.add(new GuiButton(BTN_DEL, x + 11 + 25, y + 62 + 75, BUTTON_WIDTH * 2 + 5, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_del")));
      buttonList.add(new GuiButton(BTN_ALLDEP, x + 11 + 80, y + 62, 75, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_alldep")));
      buttonList.add(new GuiButton(BTN_ALLWD, x + 11 + 80, y + 62 + 25, 75, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_allwd")));
      buttonList.add(new GuiButton(BTN_DEP, x + 11 + 80, y + 62 + 75, 35, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_dep")));
      buttonList.add(new GuiButton(BTN_WD, x + 11 + 120, y + 62 + 75, 35, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_wd")));
      buttonList.add(new GuiButton(BTN_SETLVL, x + 11 + 80, y + 62 + 50, 75, BUTTON_HEIGHT, I18n.translateToLocal("gui.button_setlv")));
   }

   private void addInput(int d) {
      inputXP = inputXP * 10 + d;
      if (inputXP > MAX)
         inputXP = MAX;
      setInputXPDigits();
   }

   @Override
   protected void keyTyped(char typedChar, int code) throws IOException {
      super.keyTyped(typedChar, code);
      switch (typedChar) {
      case 13: onButtonClicked(BTN_SETLVL); break;
      case 8:case 127: onButtonClicked(BTN_DEL); break;
      default:
         if (typedChar >= '0' && typedChar <= '9')
            addInput(typedChar - '0');
      }
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
      mc.getTextureManager().bindTexture(GUI_TEXTURE);
      int x = width - GUI_WIDTH >> 1;
      int y = height - GUI_HEIGHT >> 1;
      drawTexturedModalRect(x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT);

      drawTexturedModalRect(x + 116, y + 14, 123, 175, 15, 12);

      drawTexturedModalRect(x + 116, y + 28, 123, 190, 15, 12);

      drawTexturedModalRect(x + 59 + 0, y + 42, TextureX[inputXPDigits[0]], 205, 8, 12);
      drawTexturedModalRect(x + 59 + 8, y + 42, TextureX[inputXPDigits[1]], 205, 8, 12);
      drawTexturedModalRect(x + 59 + 16, y + 42, TextureX[inputXPDigits[2]], 205, 8, 12);
      drawTexturedModalRect(x + 59 + 24, y + 42, TextureX[inputXPDigits[3]], 205, 8, 12);
      drawTexturedModalRect(x + 59 + 32, y + 42, TextureX[inputXPDigits[4]], 205, 8, 12);
      drawTexturedModalRect(x + 59 + 40, y + 42, TextureX[inputXPDigits[5]], 205, 8, 12);
      drawTexturedModalRect(x + 59 + 48, y + 42, TextureX[inputXPDigits[6]], 205, 8, 12);
      drawTexturedModalRect(x + 116, y + 42, 123, 205, 15, 12);

      updateScreen();

      fontRenderer.drawString(I18n.translateToLocal("gui.title"), x + 8, y + 4, 0x404040);

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   @Override
   public void updateScreen() {
      setXPAndPlayerXPDigits();
      int x = width - GUI_WIDTH >> 1;
      int y = height - GUI_HEIGHT >> 1;

      drawTexturedModalRect(x + 59 + 0, y + 14, TextureX[xpDigits[0]], 175, 8, 12);
      drawTexturedModalRect(x + 59 + 8, y + 14, TextureX[xpDigits[1]], 175, 8, 12);
      drawTexturedModalRect(x + 59 + 16, y + 14, TextureX[xpDigits[2]], 175, 8, 12);
      drawTexturedModalRect(x + 59 + 24, y + 14, TextureX[xpDigits[3]], 175, 8, 12);
      drawTexturedModalRect(x + 59 + 32, y + 14, TextureX[xpDigits[4]], 175, 8, 12);
      drawTexturedModalRect(x + 59 + 40, y + 14, TextureX[xpDigits[5]], 175, 8, 12);
      drawTexturedModalRect(x + 59 + 48, y + 14, TextureX[xpDigits[6]], 175, 8, 12);

      drawTexturedModalRect(x + 59 + 0, y + 28, TextureX[playerXPDigits[0]], 190, 8, 12);
      drawTexturedModalRect(x + 59 + 8, y + 28, TextureX[playerXPDigits[1]], 190, 8, 12);
      drawTexturedModalRect(x + 59 + 16, y + 28, TextureX[playerXPDigits[2]], 190, 8, 12);
      drawTexturedModalRect(x + 59 + 24, y + 28, TextureX[playerXPDigits[3]], 190, 8, 12);
      drawTexturedModalRect(x + 59 + 32, y + 28, TextureX[playerXPDigits[4]], 190, 8, 12);
      drawTexturedModalRect(x + 59 + 40, y + 28, TextureX[playerXPDigits[5]], 190, 8, 12);
      drawTexturedModalRect(x + 59 + 48, y + 28, TextureX[playerXPDigits[6]], 190, 8, 12);
   }

   @Override
   protected void actionPerformed(GuiButton btn) throws IOException {
      onButtonClicked(btn.id);
   }

   private void onButtonClicked(int id) {
      int exp;
      switch (id) {
      case BTN_DEL: inputXP = 0; setInputXPDigits(); break;
      case BTN_ALLDEP:
         if (0 == playerXP)
            break;
         inputXP = 0;
         PacketHandler.wrapper.sendToServer(new MessageOp(MessageOp.DEPOSIT, playerXP));
         break;
      case BTN_ALLWD:
         if (0 == xp)
            break;
         inputXP = 0;
         PacketHandler.wrapper.sendToServer(new MessageOp(MessageOp.WITHDRAW, xp));
         break;
      case BTN_DEP:
         exp = inputXP;
         if (exp > playerXP)
            exp = playerXP;
         if (exp + xp > MAX)
            exp = MAX - xp;
         if (0 == exp)
            break;
         inputXP = 0;
         PacketHandler.wrapper.sendToServer(new MessageOp(MessageOp.DEPOSIT, exp));
         break;
      case BTN_WD:
         exp = inputXP;
         if (exp > xp)
            exp = xp;
         if (0 == exp)
            break;
         inputXP = 0;
         PacketHandler.wrapper.sendToServer(new MessageOp(MessageOp.WITHDRAW, exp));
         break;
      case BTN_SETLVL:
         PacketHandler.wrapper.sendToServer(new MessageOp(MessageOp.SETLEVEL, inputXP));
         inputXP = 0;
         break;
      default:
         addInput(id); break;
      }
      setInputXPDigits();
   }
}
