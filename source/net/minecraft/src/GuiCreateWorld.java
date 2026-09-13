package net.minecraft.src;

import btw.BTWMod;
import btw.world.util.difficulty.Difficulties;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.input.Keyboard;

@Environment(EnvType.CLIENT)
public class GuiCreateWorld extends GuiScreen {
   private GuiScreen parentGuiScreen;
   private GuiTextField textboxWorldName;
   private GuiTextField textboxSeed;
   private String folderName;
   private String gameMode = "survival";
   private int difficultyID = 0;
   private boolean generateStructures = true;
   private boolean commandsAllowed = false;
   private boolean commandsToggled = false;
   private boolean bonusItems = false;
   private boolean isHardcore = false;
   private boolean createClicked;
   private boolean moreOptions;
   private GuiButton buttonGameMode;
   private GuiButton buttonDifficultyLevel;
   private GuiButton moreWorldOptions;
   private GuiButton buttonGenerateStructures;
   private GuiButton buttonBonusItems;
   private GuiButton buttonWorldType;
   private GuiButton buttonAllowCommands;
   private GuiButton buttonCustomize;
   private String gameModeDescriptionLine1;
   private String gameModeDescriptionLine2;
   private String seed;
   private String localizedNewWorldText;
   private int worldTypeId = 0;
   public String generatorOptionsToUse = "";
   private static final String[] ILLEGAL_WORLD_NAMES = new String[]{
      "CON",
      "COM",
      "PRN",
      "AUX",
      "CLOCK$",
      "NUL",
      "COM1",
      "COM2",
      "COM3",
      "COM4",
      "COM5",
      "COM6",
      "COM7",
      "COM8",
      "COM9",
      "LPT1",
      "LPT2",
      "LPT3",
      "LPT4",
      "LPT5",
      "LPT6",
      "LPT7",
      "LPT8",
      "LPT9"
   };

   public GuiCreateWorld(GuiScreen par1GuiScreen) {
      this.parentGuiScreen = par1GuiScreen;
      this.seed = "";
      this.localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
   }

   @Override
   public void updateScreen() {
      this.textboxWorldName.updateCursorCounter();
      this.textboxSeed.updateCursorCounter();
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, var1.translateKey("selectWorld.create")));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, var1.translateKey("gui.cancel")));
      this.buttonList.add(this.buttonGameMode = new GuiButton(2, this.width / 2 - 75, 100, 150, 20, var1.translateKey("selectWorld.gameMode")));
      this.buttonList.add(this.buttonDifficultyLevel = new GuiButton(9, this.width / 2 - 75, 125, 150, 20, var1.translateKey("selectWorld.difficulty")));
      this.buttonList.add(this.moreWorldOptions = new GuiButton(3, this.width / 2 - 75, 187, 150, 20, var1.translateKey("selectWorld.moreWorldOptions")));
      this.buttonList.add(this.buttonGenerateStructures = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, var1.translateKey("selectWorld.mapFeatures")));
      this.buttonGenerateStructures.drawButton = false;
      this.buttonList.add(this.buttonBonusItems = new GuiButton(7, this.width / 2 + 5, 151, 150, 20, var1.translateKey("selectWorld.bonusItems")));
      this.buttonBonusItems.drawButton = false;
      this.buttonList.add(this.buttonWorldType = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, var1.translateKey("selectWorld.mapType")));
      this.buttonWorldType.drawButton = false;
      this.buttonList.add(this.buttonAllowCommands = new GuiButton(6, this.width / 2 - 155, 151, 150, 20, var1.translateKey("selectWorld.allowCommands")));
      this.buttonAllowCommands.drawButton = false;
      this.buttonList.add(this.buttonCustomize = new GuiButton(8, this.width / 2 + 5, 120, 150, 20, var1.translateKey("selectWorld.customizeType")));
      this.buttonCustomize.drawButton = false;
      this.textboxWorldName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
      this.textboxWorldName.setFocused(true);
      this.textboxWorldName.setText(this.localizedNewWorldText);
      this.textboxSeed = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
      this.textboxSeed.setText(this.seed);
      this.func_82288_a(this.moreOptions);
      this.makeUseableName();
      this.updateButtonText();
   }

   private void makeUseableName() {
      this.folderName = this.textboxWorldName.getText().trim();

      for (char var4 : ChatAllowedCharacters.allowedCharactersArray) {
         this.folderName = this.folderName.replace(var4, '_');
      }

      if (MathHelper.stringNullOrLengthZero(this.folderName)) {
         this.folderName = "World";
      }

      this.folderName = func_73913_a(this.mc.getSaveLoader(), this.folderName);
   }

   private void updateButtonText() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonGameMode.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
      this.gameModeDescriptionLine1 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line1");
      this.gameModeDescriptionLine2 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line2");
      this.buttonGenerateStructures.displayString = var1.translateKey("selectWorld.mapFeatures") + " ";
      if (this.generateStructures) {
         this.buttonGenerateStructures.displayString = this.buttonGenerateStructures.displayString + var1.translateKey("options.on");
      } else {
         this.buttonGenerateStructures.displayString = this.buttonGenerateStructures.displayString + var1.translateKey("options.off");
      }

      this.buttonBonusItems.displayString = var1.translateKey("selectWorld.bonusItems") + " ";
      if (this.bonusItems && !this.isHardcore) {
         this.buttonBonusItems.displayString = this.buttonBonusItems.displayString + var1.translateKey("options.on");
      } else {
         this.buttonBonusItems.displayString = this.buttonBonusItems.displayString + var1.translateKey("options.off");
      }

      this.buttonWorldType.displayString = var1.translateKey("selectWorld.mapType")
         + " "
         + var1.translateKey(WorldType.worldTypes[this.worldTypeId].getTranslateName());
      this.buttonAllowCommands.displayString = var1.translateKey("selectWorld.allowCommands") + " ";
      if (this.commandsAllowed && !this.isHardcore) {
         this.buttonAllowCommands.displayString = this.buttonAllowCommands.displayString + var1.translateKey("options.on");
      } else {
         this.buttonAllowCommands.displayString = this.buttonAllowCommands.displayString + var1.translateKey("options.off");
      }

      this.buttonDifficultyLevel.displayString = var1.translateKey("selectWorld.difficulty")
         + ": "
         + Difficulties.DIFFICULTY_LIST.get(this.difficultyID).getLocalizedName();
   }

   public static String func_73913_a(ISaveFormat par0ISaveFormat, String par1Str) {
      par1Str = par1Str.replaceAll("[\\./\"]", "_");

      for (String var5 : ILLEGAL_WORLD_NAMES) {
         if (par1Str.equalsIgnoreCase(var5)) {
            par1Str = "_" + par1Str + "_";
         }
      }

      while (par0ISaveFormat.getWorldInfo(par1Str) != null) {
         par1Str = par1Str + "-";
      }

      return par1Str;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton par1GuiButton) {
      if (par1GuiButton.enabled) {
         if (par1GuiButton.id == 1) {
            this.mc.displayGuiScreen(this.parentGuiScreen);
         } else if (par1GuiButton.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)null);
            if (this.createClicked) {
               return;
            }

            this.createClicked = true;
            long var2 = new Random().nextLong();
            String var4 = this.textboxSeed.getText();
            if (!MathHelper.stringNullOrLengthZero(var4)) {
               try {
                  long var5 = Long.parseLong(var4);
                  if (var5 != 0L) {
                     var2 = var5;
                  }
               } catch (NumberFormatException var7) {
                  var2 = var4.hashCode();
               }
            }

            EnumGameType var8 = EnumGameType.getByName(this.gameMode);
            WorldSettings var6 = new WorldSettings(
               var2,
               var8,
               this.generateStructures,
               this.isHardcore,
               WorldType.worldTypes[this.worldTypeId],
               Difficulties.DIFFICULTY_LIST.get(this.difficultyID)
            );
            var6.func_82750_a(this.generatorOptionsToUse);
            if (this.bonusItems && !this.isHardcore) {
               var6.enableBonusChest();
            }

            if (this.commandsAllowed && !this.isHardcore) {
               var6.enableCommands();
            }

            this.mc.launchIntegratedServer(this.folderName, this.textboxWorldName.getText().trim(), var6);
         } else if (par1GuiButton.id == 3) {
            this.func_82287_i();
         } else if (par1GuiButton.id == 2) {
            if (this.gameMode.equals("survival") && BTWMod.allowHardcore) {
               if (!this.commandsToggled) {
                  this.commandsAllowed = false;
               }

               this.isHardcore = false;
               this.gameMode = "hardcore";
               this.isHardcore = true;
               this.buttonAllowCommands.enabled = false;
               this.buttonBonusItems.enabled = false;
               this.updateButtonText();
            } else if (!this.gameMode.equals("hardcore") && !this.gameMode.equals("survival")) {
               if (!this.commandsToggled) {
                  this.commandsAllowed = false;
               }

               this.gameMode = "survival";
               this.updateButtonText();
               this.buttonAllowCommands.enabled = true;
               this.buttonBonusItems.enabled = true;
               this.isHardcore = false;
            } else {
               if (!this.commandsToggled) {
                  this.commandsAllowed = true;
               }

               this.isHardcore = false;
               this.gameMode = "creative";
               this.updateButtonText();
               this.isHardcore = false;
               this.buttonAllowCommands.enabled = true;
               this.buttonBonusItems.enabled = true;
            }

            this.updateButtonText();
         } else if (par1GuiButton.id == 4) {
            this.generateStructures = !this.generateStructures;
            this.updateButtonText();
         } else if (par1GuiButton.id == 7) {
            this.bonusItems = !this.bonusItems;
            this.updateButtonText();
         } else if (par1GuiButton.id == 5) {
            this.worldTypeId++;
            if (this.worldTypeId >= WorldType.worldTypes.length) {
               this.worldTypeId = 0;
            }

            while (WorldType.worldTypes[this.worldTypeId] == null || !WorldType.worldTypes[this.worldTypeId].getCanBeCreated()) {
               this.worldTypeId++;
               if (this.worldTypeId >= WorldType.worldTypes.length) {
                  this.worldTypeId = 0;
               }
            }

            this.generatorOptionsToUse = "";
            this.updateButtonText();
            this.func_82288_a(this.moreOptions);
         } else if (par1GuiButton.id == 6) {
            this.commandsToggled = true;
            this.commandsAllowed = !this.commandsAllowed;
            this.updateButtonText();
         } else if (par1GuiButton.id == 8) {
            this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.generatorOptionsToUse));
         } else if (par1GuiButton.id == this.buttonDifficultyLevel.id) {
            this.difficultyID++;
            if (this.difficultyID >= Difficulties.DIFFICULTY_LIST.size()) {
               this.difficultyID = 0;
            }

            this.updateButtonText();
         }
      }
   }

   private void func_82287_i() {
      this.func_82288_a(!this.moreOptions);
   }

   private void func_82288_a(boolean par1) {
      this.moreOptions = par1;
      this.buttonGameMode.drawButton = !this.moreOptions;
      this.buttonGenerateStructures.drawButton = this.moreOptions;
      this.buttonBonusItems.drawButton = this.moreOptions;
      this.buttonWorldType.drawButton = this.moreOptions;
      this.buttonAllowCommands.drawButton = this.moreOptions;
      this.buttonCustomize.drawButton = this.moreOptions && WorldType.worldTypes[this.worldTypeId] == WorldType.FLAT;
      if (this.moreOptions) {
         StringTranslate var2 = StringTranslate.getInstance();
         this.moreWorldOptions.displayString = var2.translateKey("gui.done");
      } else {
         StringTranslate var2 = StringTranslate.getInstance();
         this.moreWorldOptions.displayString = var2.translateKey("selectWorld.moreWorldOptions");
      }

      this.buttonDifficultyLevel.drawButton = !this.moreOptions;
   }

   @Override
   protected void keyTyped(char par1, int par2) {
      if (this.textboxWorldName.isFocused() && !this.moreOptions) {
         this.textboxWorldName.textboxKeyTyped(par1, par2);
         this.localizedNewWorldText = this.textboxWorldName.getText();
      } else if (this.textboxSeed.isFocused() && this.moreOptions) {
         this.textboxSeed.textboxKeyTyped(par1, par2);
         this.seed = this.textboxSeed.getText();
      }

      if (par1 == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.textboxWorldName.getText().length() > 0;
      this.makeUseableName();
   }

   @Override
   protected void mouseClicked(int par1, int par2, int par3) {
      super.mouseClicked(par1, par2, par3);
      if (this.moreOptions) {
         this.textboxSeed.mouseClicked(par1, par2, par3);
      } else {
         this.textboxWorldName.mouseClicked(par1, par2, par3);
      }
   }

   @Override
   public void drawScreen(int par1, int par2, float par3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("selectWorld.create"), this.width / 2, 20, 16777215);
      if (this.moreOptions) {
         this.b(this.fontRenderer, var4.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 47, 10526880);
         this.b(this.fontRenderer, var4.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 85, 10526880);
         this.b(this.fontRenderer, var4.translateKey("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
         this.b(this.fontRenderer, var4.translateKey("selectWorld.allowCommands.info"), this.width / 2 - 150, 172, 10526880);
         this.textboxSeed.drawTextBox();
      } else {
         this.b(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
         this.b(this.fontRenderer, var4.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 10526880);
         this.textboxWorldName.drawTextBox();
         this.b(
            this.fontRenderer,
            var4.translateKey("difficulty." + Difficulties.DIFFICULTY_LIST.get(this.difficultyID).NAME + ".description"),
            this.width / 2 - 90,
            147,
            10526880
         );
         this.b(
            this.fontRenderer,
            var4.translateKey("difficulty." + Difficulties.DIFFICULTY_LIST.get(this.difficultyID).NAME + ".description2"),
            this.width / 2 - 90,
            159,
            10526880
         );
         this.b(
            this.fontRenderer,
            var4.translateKey("difficulty." + Difficulties.DIFFICULTY_LIST.get(this.difficultyID).NAME + ".description3"),
            this.width / 2 - 90,
            171,
            10526880
         );
      }

      super.drawScreen(par1, par2, par3);
   }

   public void func_82286_a(WorldInfo par1WorldInfo) {
      this.localizedNewWorldText = StatCollector.translateToLocalFormatted("selectWorld.newWorld.copyOf", par1WorldInfo.getWorldName());
      this.seed = par1WorldInfo.getSeed() + "";
      this.worldTypeId = par1WorldInfo.getTerrainType().getWorldTypeID();
      this.generatorOptionsToUse = par1WorldInfo.getGeneratorOptions();
      this.generateStructures = par1WorldInfo.isMapFeaturesEnabled();
      this.commandsAllowed = par1WorldInfo.areCommandsAllowed();
      if (par1WorldInfo.isHardcoreModeEnabled()) {
         this.gameMode = "hardcore";
      } else if (par1WorldInfo.getGameType().isSurvivalOrAdventure()) {
         this.gameMode = "survival";
      } else if (par1WorldInfo.getGameType().isCreative()) {
         this.gameMode = "creative";
      }
   }
}
