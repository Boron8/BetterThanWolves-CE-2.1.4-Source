package net.minecraft.src;

public class ScreenChatOptions extends GuiScreen {
   private static final EnumOptions[] allScreenChatOptions = new EnumOptions[]{
      EnumOptions.CHAT_VISIBILITY,
      EnumOptions.CHAT_COLOR,
      EnumOptions.CHAT_LINKS,
      EnumOptions.CHAT_OPACITY,
      EnumOptions.CHAT_LINKS_PROMPT,
      EnumOptions.CHAT_SCALE,
      EnumOptions.CHAT_HEIGHT_FOCUSED,
      EnumOptions.CHAT_HEIGHT_UNFOCUSED,
      EnumOptions.CHAT_WIDTH
   };
   private static final EnumOptions[] allMultiplayerOptions = new EnumOptions[]{EnumOptions.SHOW_CAPE};
   private final GuiScreen theGuiScreen;
   private final GameSettings theSettings;
   private String theChatOptions;
   private String field_82268_n;
   private int field_82269_o = 0;

   public ScreenChatOptions(GuiScreen var1, GameSettings var2) {
      this.theGuiScreen = var1;
      this.theSettings = var2;
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      int var2 = 0;
      this.theChatOptions = var1.translateKey("options.chat.title");
      this.field_82268_n = var1.translateKey("options.multiplayer.title");

      for (EnumOptions var6 : allScreenChatOptions) {
         if (var6.getEnumFloat()) {
            this.buttonList
               .add(
                  new GuiSlider(
                     var6.returnEnumOrdinal(),
                     this.width / 2 - 155 + var2 % 2 * 160,
                     this.height / 6 + 24 * (var2 >> 1),
                     var6,
                     this.theSettings.getKeyBinding(var6),
                     this.theSettings.getOptionFloatValue(var6)
                  )
               );
         } else {
            this.buttonList
               .add(
                  new GuiSmallButton(
                     var6.returnEnumOrdinal(),
                     this.width / 2 - 155 + var2 % 2 * 160,
                     this.height / 6 + 24 * (var2 >> 1),
                     var6,
                     this.theSettings.getKeyBinding(var6)
                  )
               );
         }

         var2++;
      }

      if (var2 % 2 == 1) {
         var2++;
      }

      this.field_82269_o = this.height / 6 + 24 * (var2 >> 1);
      var2 += 2;

      for (EnumOptions var11 : allMultiplayerOptions) {
         if (var11.getEnumFloat()) {
            this.buttonList
               .add(
                  new GuiSlider(
                     var11.returnEnumOrdinal(),
                     this.width / 2 - 155 + var2 % 2 * 160,
                     this.height / 6 + 24 * (var2 >> 1),
                     var11,
                     this.theSettings.getKeyBinding(var11),
                     this.theSettings.getOptionFloatValue(var11)
                  )
               );
         } else {
            this.buttonList
               .add(
                  new GuiSmallButton(
                     var11.returnEnumOrdinal(),
                     this.width / 2 - 155 + var2 % 2 * 160,
                     this.height / 6 + 24 * (var2 >> 1),
                     var11,
                     this.theSettings.getKeyBinding(var11)
                  )
               );
         }

         var2++;
      }

      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id < 100 && var1 instanceof GuiSmallButton) {
            this.theSettings.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
            var1.displayString = this.theSettings.getKeyBinding(EnumOptions.getEnumOptions(var1.id));
         }

         if (var1.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.theGuiScreen);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.a(this.fontRenderer, this.theChatOptions, this.width / 2, 20, 16777215);
      this.a(this.fontRenderer, this.field_82268_n, this.width / 2, this.field_82269_o + 7, 16777215);
      super.drawScreen(var1, var2, var3);
   }
}
