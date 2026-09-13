package net.minecraft.src;

public class GuiLanguage extends GuiScreen {
   protected GuiScreen parentGui;
   private int updateTimer = -1;
   private GuiSlotLanguage languageList;
   private final GameSettings theGameSettings;
   private GuiSmallButton doneButton;

   public GuiLanguage(GuiScreen var1, GameSettings var2) {
      this.parentGui = var1;
      this.theGameSettings = var2;
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.add(this.doneButton = new GuiSmallButton(6, this.width / 2 - 75, this.height - 38, var1.translateKey("gui.done")));
      this.languageList = new GuiSlotLanguage(this);
      this.languageList.a(this.buttonList, 7, 8);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         switch (var1.id) {
            case 5:
               break;
            case 6:
               this.mc.displayGuiScreen(this.parentGui);
               break;
            default:
               this.languageList.a(var1);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.languageList.a(var1, var2, var3);
      if (this.updateTimer <= 0) {
         this.mc.texturePackList.updateAvaliableTexturePacks();
         this.updateTimer += 20;
      }

      StringTranslate var4 = StringTranslate.getInstance();
      this.a(this.fontRenderer, var4.translateKey("options.language"), this.width / 2, 16, 16777215);
      this.a(this.fontRenderer, "(" + var4.translateKey("options.languageWarning") + ")", this.width / 2, this.height - 56, 8421504);
      super.drawScreen(var1, var2, var3);
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.updateTimer--;
   }
}
