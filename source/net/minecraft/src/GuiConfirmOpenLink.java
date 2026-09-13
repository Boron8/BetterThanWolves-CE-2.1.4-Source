package net.minecraft.src;

public class GuiConfirmOpenLink extends GuiYesNo {
   private String openLinkWarning;
   private String copyLinkButtonText;
   private String field_92028_p;
   private boolean field_92027_q = true;

   public GuiConfirmOpenLink(GuiScreen var1, String var2, int var3, boolean var4) {
      super(var1, StringTranslate.getInstance().translateKey(var4 ? "chat.link.confirmTrusted" : "chat.link.confirm"), var2, var3);
      StringTranslate var5 = StringTranslate.getInstance();
      this.buttonText1 = var5.translateKey(var4 ? "chat.link.open" : "gui.yes");
      this.buttonText2 = var5.translateKey(var4 ? "gui.cancel" : "gui.no");
      this.copyLinkButtonText = var5.translateKey("chat.copy");
      this.openLinkWarning = var5.translateKey("chat.link.warning");
      this.field_92028_p = var2;
   }

   @Override
   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 3 - 83 + 0, this.height / 6 + 96, 100, 20, this.buttonText1));
      this.buttonList.add(new GuiButton(2, this.width / 3 - 83 + 105, this.height / 6 + 96, 100, 20, this.copyLinkButtonText));
      this.buttonList.add(new GuiButton(1, this.width / 3 - 83 + 210, this.height / 6 + 96, 100, 20, this.buttonText2));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 2) {
         this.copyLinkToClipboard();
      }

      this.parentScreen.confirmClicked(var1.id == 0, this.worldNumber);
   }

   public void copyLinkToClipboard() {
      d(this.field_92028_p);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      if (this.field_92027_q) {
         this.a(this.fontRenderer, this.openLinkWarning, this.width / 2, 110, 16764108);
      }
   }

   public void func_92026_h() {
      this.field_92027_q = false;
   }
}
