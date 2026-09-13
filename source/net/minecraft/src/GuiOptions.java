package net.minecraft.src;

import btw.world.util.difficulty.Difficulties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

@Environment(EnvType.CLIENT)
public class GuiOptions extends GuiScreen {
   private static final EnumOptions[] relevantOptions = new EnumOptions[]{
      EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.FOV, EnumOptions.DIFFICULTY, EnumOptions.TOUCHSCREEN
   };
   private static final EnumOptions[] relevantOptionsMainMenu = new EnumOptions[]{
      EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.FOV, EnumOptions.TOUCHSCREEN
   };
   private final GuiScreen parentScreen;
   private final GameSettings options;
   protected String screenTitle = "Options";
   private final boolean hideDifficulty;

   public GuiOptions(GuiScreen par1GuiScreen, GameSettings par2GameSettings, boolean hideDifficulty) {
      this.parentScreen = par1GuiScreen;
      this.options = par2GameSettings;
      this.hideDifficulty = hideDifficulty;
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      int var2 = 0;
      this.screenTitle = var1.translateKey("options.title");
      EnumOptions[] var3;
      if (this.hideDifficulty) {
         var3 = relevantOptionsMainMenu;
      } else {
         var3 = relevantOptions;
      }

      for (EnumOptions var6 : var3) {
         if (var6.getEnumFloat()) {
            this.buttonList
               .add(
                  new GuiSlider(
                     var6.returnEnumOrdinal(),
                     this.width / 2 - 155 + var2 % 2 * 160,
                     this.height / 6 - 12 + 24 * (var2 >> 1),
                     var6,
                     this.options.getKeyBinding(var6),
                     this.options.getOptionFloatValue(var6)
                  )
               );
         } else {
            GuiSmallButton var7 = new GuiSmallButton(
               var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 - 12 + 24 * (var2 >> 1), var6, this.options.getKeyBinding(var6)
            );
            if (var6 == EnumOptions.DIFFICULTY) {
               var7.displayString = StringTranslate.getInstance().translateKey("selectWorld.difficulty")
                  + ": "
                  + MinecraftServer.getServer().worldServers[0].worldInfo.getDifficulty().getLocalizedName();
            }

            this.buttonList.add(var7);
         }

         var2++;
      }

      this.buttonList.add(new GuiButton(101, this.width / 2 - 152, this.height / 6 + 96 - 6, 150, 20, var1.translateKey("options.video")));
      this.buttonList.add(new GuiButton(100, this.width / 2 + 2, this.height / 6 + 96 - 6, 150, 20, var1.translateKey("options.controls")));
      this.buttonList.add(new GuiButton(102, this.width / 2 - 152, this.height / 6 + 120 - 6, 150, 20, var1.translateKey("options.language")));
      this.buttonList.add(new GuiButton(103, this.width / 2 + 2, this.height / 6 + 120 - 6, 150, 20, var1.translateKey("options.multiplayer.title")));
      this.buttonList.add(new GuiButton(105, this.width / 2 - 152, this.height / 6 + 144 - 6, 150, 20, var1.translateKey("options.texture.pack")));
      this.buttonList.add(new GuiButton(104, this.width / 2 + 2, this.height / 6 + 144 - 6, 150, 20, var1.translateKey("options.snooper.view")));
      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
   }

   @Override
   protected void actionPerformed(GuiButton par1GuiButton) {
      if (par1GuiButton.enabled) {
         if (par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton) {
            if (EnumOptions.getEnumOptions(par1GuiButton.id) == EnumOptions.DIFFICULTY) {
               int newDifficultyID = MinecraftServer.getServer().worldServers[0].worldInfo.getDifficulty().ID + 1;
               if (newDifficultyID >= Difficulties.DIFFICULTY_LIST.size()) {
                  newDifficultyID = 0;
               }

               new CommandDifficulty().processCommand(MinecraftServer.getServer(), new String[]{Difficulties.DIFFICULTY_LIST.get(newDifficultyID).NAME});
               par1GuiButton.displayString = StringTranslate.getInstance().translateKey("selectWorld.difficulty")
                  + ": "
                  + MinecraftServer.getServer().worldServers[0].worldInfo.getDifficulty().getLocalizedName();
            } else {
               this.options.setOptionValue(((GuiSmallButton)par1GuiButton).returnEnumOptions(), 1);
               par1GuiButton.displayString = this.options.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
            }
         }

         if (par1GuiButton.id == 101) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiVideoSettings(this, this.options));
         }

         if (par1GuiButton.id == 100) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiControls(this, this.options));
         }

         if (par1GuiButton.id == 102) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiLanguage(this, this.options));
         }

         if (par1GuiButton.id == 103) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new ScreenChatOptions(this, this.options));
         }

         if (par1GuiButton.id == 104) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiSnooper(this, this.options));
         }

         if (par1GuiButton.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         }

         if (par1GuiButton.id == 105) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiTexturePacks(this, this.options));
         }
      }
   }

   @Override
   public void drawScreen(int par1, int par2, float par3) {
      this.e();
      this.a(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
      super.drawScreen(par1, par2, par3);
   }
}
