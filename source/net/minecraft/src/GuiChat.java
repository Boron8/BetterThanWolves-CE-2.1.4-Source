package net.minecraft.src;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat extends GuiScreen {
   private String field_73898_b = "";
   private int sentHistoryCursor = -1;
   private boolean field_73897_d = false;
   private boolean field_73905_m = false;
   private int field_73903_n = 0;
   private List field_73904_o = new ArrayList();
   private URI clickedURI = null;
   protected GuiTextField inputField;
   private String defaultInputFieldText = "";

   public GuiChat() {
   }

   public GuiChat(String var1) {
      this.defaultInputFieldText = var1;
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      this.inputField = new GuiTextField(this.fontRenderer, 4, this.height - 12, this.width - 4, 12);
      this.inputField.setMaxStringLength(100);
      this.inputField.setEnableBackgroundDrawing(false);
      this.inputField.setFocused(true);
      this.inputField.setText(this.defaultInputFieldText);
      this.inputField.setCanLoseFocus(false);
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.getChatGUI().resetScroll();
   }

   @Override
   public void updateScreen() {
      this.inputField.updateCursorCounter();
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.field_73905_m = false;
      if (var2 == 15) {
         this.completePlayerName();
      } else {
         this.field_73897_d = false;
      }

      if (var2 == 1) {
         this.mc.displayGuiScreen(null);
      } else if (var2 == 28) {
         String var3 = this.inputField.getText().trim();
         if (var3.length() > 0) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(var3);
            if (!this.mc.handleClientCommand(var3)) {
               this.mc.thePlayer.sendChatMessage(var3);
            }
         }

         this.mc.displayGuiScreen(null);
      } else if (var2 == 200) {
         this.getSentHistory(-1);
      } else if (var2 == 208) {
         this.getSentHistory(1);
      } else if (var2 == 201) {
         this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().func_96127_i() - 1);
      } else if (var2 == 209) {
         this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().func_96127_i() + 1);
      } else {
         this.inputField.textboxKeyTyped(var1, var2);
      }
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      int var1 = Mouse.getEventDWheel();
      if (var1 != 0) {
         if (var1 > 1) {
            var1 = 1;
         }

         if (var1 < -1) {
            var1 = -1;
         }

         if (!p()) {
            var1 *= 7;
         }

         this.mc.ingameGUI.getChatGUI().scroll(var1);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (var3 == 0 && this.mc.gameSettings.chatLinks) {
         ChatClickData var4 = this.mc.ingameGUI.getChatGUI().func_73766_a(Mouse.getX(), Mouse.getY());
         if (var4 != null) {
            URI var5 = var4.getURI();
            if (var5 != null) {
               if (this.mc.gameSettings.chatLinksPrompt) {
                  this.clickedURI = var5;
                  this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var4.getClickedUrl(), 0, false));
               } else {
                  this.func_73896_a(var5);
               }

               return;
            }
         }
      }

      this.inputField.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      if (var2 == 0) {
         if (var1) {
            this.func_73896_a(this.clickedURI);
         }

         this.clickedURI = null;
         this.mc.displayGuiScreen(this);
      }
   }

   private void func_73896_a(URI var1) {
      try {
         Class var2 = Class.forName("java.awt.Desktop");
         Object var3 = var2.getMethod("getDesktop").invoke(null);
         var2.getMethod("browse", URI.class).invoke(var3, var1);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }
   }

   public void completePlayerName() {
      if (this.field_73897_d) {
         this.inputField.deleteFromCursor(this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
         if (this.field_73903_n >= this.field_73904_o.size()) {
            this.field_73903_n = 0;
         }
      } else {
         int var1 = this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false);
         this.field_73904_o.clear();
         this.field_73903_n = 0;
         String var2 = this.inputField.getText().substring(var1).toLowerCase();
         String var3 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
         this.func_73893_a(var3, var2);
         if (this.field_73904_o.isEmpty()) {
            return;
         }

         this.field_73897_d = true;
         this.inputField.deleteFromCursor(var1 - this.inputField.getCursorPosition());
      }

      if (this.field_73904_o.size() > 1) {
         StringBuilder var4 = new StringBuilder();

         for (String var6 : this.field_73904_o) {
            if (var4.length() > 0) {
               var4.append(", ");
            }

            var4.append(var6);
         }

         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(var4.toString(), 1);
      }

      this.inputField.writeText((String)this.field_73904_o.get(this.field_73903_n++));
   }

   private void func_73893_a(String var1, String var2) {
      if (var1.length() >= 1) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new Packet203AutoComplete(var1));
         this.field_73905_m = true;
      }
   }

   public void getSentHistory(int var1) {
      int var2 = this.sentHistoryCursor + var1;
      int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      if (var2 < 0) {
         var2 = 0;
      }

      if (var2 > var3) {
         var2 = var3;
      }

      if (var2 != this.sentHistoryCursor) {
         if (var2 == var3) {
            this.sentHistoryCursor = var3;
            this.inputField.setText(this.field_73898_b);
         } else {
            if (this.sentHistoryCursor == var3) {
               this.field_73898_b = this.inputField.getText();
            }

            this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
            this.sentHistoryCursor = var2;
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      a(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
      this.inputField.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }

   public void func_73894_a(String[] var1) {
      if (this.field_73905_m) {
         this.field_73904_o.clear();

         for (String var5 : var1) {
            if (var5.length() > 0) {
               this.field_73904_o.add(var5);
            }
         }

         if (this.field_73904_o.size() > 0) {
            this.field_73897_d = true;
            this.completePlayerName();
         }
      }
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }
}
