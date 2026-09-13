package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends GuiScreen {
   private GuiTextField commandTextField;
   private final TileEntityCommandBlock commandBlock;
   private GuiButton doneBtn;
   private GuiButton cancelBtn;

   public GuiCommandBlock(TileEntityCommandBlock var1) {
      this.commandBlock = var1;
   }

   @Override
   public void updateScreen() {
      this.commandTextField.updateCursorCounter();
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("gui.done")));
      this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
      this.commandTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 150, 60, 300, 20);
      this.commandTextField.setMaxStringLength(32767);
      this.commandTextField.setFocused(true);
      this.commandTextField.setText(this.commandBlock.getCommand());
      this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.mc.displayGuiScreen(null);
         } else if (var1.id == 0) {
            String var2 = "MC|AdvCdm";
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            DataOutputStream var4 = new DataOutputStream(var3);

            try {
               var4.writeInt(this.commandBlock.xCoord);
               var4.writeInt(this.commandBlock.yCoord);
               var4.writeInt(this.commandBlock.zCoord);
               Packet.writeString(this.commandTextField.getText(), var4);
               this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
            } catch (Exception var6) {
               var6.printStackTrace();
            }

            this.mc.displayGuiScreen(null);
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.commandTextField.textboxKeyTyped(var1, var2);
      this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
      if (var2 == 28 || var1 == '\r') {
         this.actionPerformed(this.doneBtn);
      } else if (var2 == 1) {
         this.actionPerformed(this.cancelBtn);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.commandTextField.mouseClicked(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("advMode.setCommand"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
      this.b(this.fontRenderer, var4.translateKey("advMode.command"), this.width / 2 - 150, 47, 10526880);
      this.b(this.fontRenderer, var4.translateKey("advMode.nearestPlayer"), this.width / 2 - 150, 97, 10526880);
      this.b(this.fontRenderer, var4.translateKey("advMode.randomPlayer"), this.width / 2 - 150, 108, 10526880);
      this.b(this.fontRenderer, var4.translateKey("advMode.allPlayers"), this.width / 2 - 150, 119, 10526880);
      this.commandTextField.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }
}
