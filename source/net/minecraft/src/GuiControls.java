package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GuiControls extends GuiScreen {
   private GuiScreen parentScreen;
   protected String screenTitle = "Controls";
   private GameSettings options;
   private int buttonId = -1;

   public GuiControls(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
      this.parentScreen = par1GuiScreen;
      this.options = par2GameSettings;
   }

   private int getLeftBorder() {
      return this.width / 2 - 155;
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      int var2 = this.getLeftBorder();
      int totalButtons = this.options.keyBindings.length;
      int verticalButtons = Math.floorDiv(totalButtons + totalButtons % 2, 2);

      for (int var3 = 0; var3 < this.options.keyBindings.length; var3++) {
         this.buttonList
            .add(
               new GuiSmallButton(
                  var3,
                  var2 + var3 % 2 * 160,
                  this.height / verticalButtons + 168 / verticalButtons * (var3 >> 1),
                  70,
                  144 / verticalButtons,
                  this.options.getOptionDisplayString(var3)
               )
            );
      }

      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / verticalButtons + 168, var1.translateKey("gui.done")));
      this.screenTitle = var1.translateKey("controls.title");
   }

   @Override
   protected void actionPerformed(GuiButton par1GuiButton) {
      for (int var2 = 0; var2 < this.options.keyBindings.length; var2++) {
         ((GuiButton)this.buttonList.get(var2)).displayString = this.options.getOptionDisplayString(var2);
      }

      if (par1GuiButton.id == 200) {
         this.mc.displayGuiScreen(this.parentScreen);
      } else {
         this.buttonId = par1GuiButton.id;
         par1GuiButton.displayString = "> " + this.options.getOptionDisplayString(par1GuiButton.id) + " <";
      }
   }

   @Override
   protected void mouseClicked(int par1, int par2, int par3) {
      if (this.buttonId >= 0) {
         this.options.setKeyBinding(this.buttonId, -100 + par3);
         ((GuiButton)this.buttonList.get(this.buttonId)).displayString = this.options.getOptionDisplayString(this.buttonId);
         this.buttonId = -1;
         KeyBinding.resetKeyBindingArrayAndHash();
      } else {
         super.mouseClicked(par1, par2, par3);
      }
   }

   @Override
   protected void keyTyped(char par1, int par2) {
      if (this.buttonId >= 0) {
         this.options.setKeyBinding(this.buttonId, par2);
         ((GuiButton)this.buttonList.get(this.buttonId)).displayString = this.options.getOptionDisplayString(this.buttonId);
         this.buttonId = -1;
         KeyBinding.resetKeyBindingArrayAndHash();
      } else {
         super.keyTyped(par1, par2);
      }
   }

   @Override
   public void drawScreen(int par1, int par2, float par3) {
      this.e();
      this.a(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
      int var4 = this.getLeftBorder();
      int var5 = 0;
      int totalButtons = this.options.keyBindings.length;

      for (int verticalButtons = Math.floorDiv(totalButtons + totalButtons % 2, 2); var5 < this.options.keyBindings.length; var5++) {
         boolean var6 = false;

         for (int var7 = 0; var7 < this.options.keyBindings.length; var7++) {
            if (var7 != var5 && this.options.keyBindings[var5].keyCode == this.options.keyBindings[var7].keyCode) {
               var6 = true;
               break;
            }
         }

         if (this.buttonId == var5) {
            ((GuiButton)this.buttonList.get(var5)).displayString = ""
               + EnumChatFormatting.WHITE
               + "> "
               + EnumChatFormatting.YELLOW
               + "??? "
               + EnumChatFormatting.WHITE
               + "<";
         } else if (var6) {
            ((GuiButton)this.buttonList.get(var5)).displayString = EnumChatFormatting.RED + this.options.getOptionDisplayString(var5);
         } else {
            ((GuiButton)this.buttonList.get(var5)).displayString = this.options.getOptionDisplayString(var5);
         }

         this.b(
            this.fontRenderer,
            this.options.getKeyBindingDescription(var5),
            var4 + var5 % 2 * 160 + 70 + 6,
            this.height / verticalButtons + 168 / verticalButtons * (var5 >> 1) + 7,
            -1
         );
      }

      super.drawScreen(par1, par2, par3);
   }
}
