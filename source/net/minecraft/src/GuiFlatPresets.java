package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends GuiScreen {
   private static RenderItem presetIconRenderer = new RenderItem();
   private static final List presets = new ArrayList();
   private final GuiCreateFlatWorld createFlatWorldGui;
   private String field_82300_d;
   private String field_82308_m;
   private String field_82306_n;
   private GuiFlatPresetsListSlot theFlatPresetsListSlot;
   private GuiButton theButton;
   private GuiTextField theTextField;

   public GuiFlatPresets(GuiCreateFlatWorld var1) {
      this.createFlatWorldGui = var1;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.field_82300_d = StatCollector.translateToLocal("createWorld.customize.presets.title");
      this.field_82308_m = StatCollector.translateToLocal("createWorld.customize.presets.share");
      this.field_82306_n = StatCollector.translateToLocal("createWorld.customize.presets.list");
      this.theTextField = new GuiTextField(this.fontRenderer, 50, 40, this.width - 100, 20);
      this.theFlatPresetsListSlot = new GuiFlatPresetsListSlot(this);
      this.theTextField.setMaxStringLength(1230);
      this.theTextField.setText(this.createFlatWorldGui.getFlatGeneratorInfo());
      this.buttonList
         .add(
            this.theButton = new GuiButton(
               0, this.width / 2 - 155, this.height - 28, 150, 20, StatCollector.translateToLocal("createWorld.customize.presets.select")
            )
         );
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
      this.func_82296_g();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      this.theTextField.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (!this.theTextField.textboxKeyTyped(var1, var2)) {
         super.keyTyped(var1, var2);
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 0 && this.func_82293_j()) {
         this.createFlatWorldGui.setFlatGeneratorInfo(this.theTextField.getText());
         this.mc.displayGuiScreen(this.createFlatWorldGui);
      } else if (var1.id == 1) {
         this.mc.displayGuiScreen(this.createFlatWorldGui);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.theFlatPresetsListSlot.a(var1, var2, var3);
      this.a(this.fontRenderer, this.field_82300_d, this.width / 2, 8, 16777215);
      this.b(this.fontRenderer, this.field_82308_m, 50, 30, 10526880);
      this.b(this.fontRenderer, this.field_82306_n, 50, 70, 10526880);
      this.theTextField.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }

   @Override
   public void updateScreen() {
      this.theTextField.updateCursorCounter();
      super.updateScreen();
   }

   public void func_82296_g() {
      boolean var1 = this.func_82293_j();
      this.theButton.enabled = var1;
   }

   private boolean func_82293_j() {
      return this.theFlatPresetsListSlot.field_82459_a > -1 && this.theFlatPresetsListSlot.field_82459_a < presets.size()
         || this.theTextField.getText().length() > 1;
   }

   private static void addPresetNoFeatures(String var0, int var1, BiomeGenBase var2, FlatLayerInfo... var3) {
      addPreset(var0, var1, var2, null, var3);
   }

   private static void addPreset(String var0, int var1, BiomeGenBase var2, List var3, FlatLayerInfo... var4) {
      FlatGeneratorInfo var5 = new FlatGeneratorInfo();

      for (int var6 = var4.length - 1; var6 >= 0; var6--) {
         var5.getFlatLayers().add(var4[var6]);
      }

      var5.setBiome(var2.biomeID);
      var5.func_82645_d();
      if (var3 != null) {
         for (String var7 : var3) {
            var5.getWorldFeatures().put(var7, new HashMap());
         }
      }

      presets.add(new GuiFlatPresetsItem(var1, var0, var5.toString()));
   }

   static {
      addPreset(
         "Classic Flat",
         Block.grass.blockID,
         BiomeGenBase.plains,
         Arrays.asList("village"),
         new FlatLayerInfo(1, Block.grass.blockID),
         new FlatLayerInfo(2, Block.dirt.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
      addPreset(
         "Tunnelers' Dream",
         Block.stone.blockID,
         BiomeGenBase.extremeHills,
         Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"),
         new FlatLayerInfo(1, Block.grass.blockID),
         new FlatLayerInfo(5, Block.dirt.blockID),
         new FlatLayerInfo(230, Block.stone.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
      addPreset(
         "Water World",
         Block.waterMoving.blockID,
         BiomeGenBase.plains,
         Arrays.asList("village", "biome_1"),
         new FlatLayerInfo(90, Block.waterStill.blockID),
         new FlatLayerInfo(5, Block.sand.blockID),
         new FlatLayerInfo(5, Block.dirt.blockID),
         new FlatLayerInfo(5, Block.stone.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
      addPreset(
         "Overworld",
         Block.tallGrass.blockID,
         BiomeGenBase.plains,
         Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"),
         new FlatLayerInfo(1, Block.grass.blockID),
         new FlatLayerInfo(3, Block.dirt.blockID),
         new FlatLayerInfo(59, Block.stone.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
      addPreset(
         "Snowy Kingdom",
         Block.snow.blockID,
         BiomeGenBase.icePlains,
         Arrays.asList("village", "biome_1"),
         new FlatLayerInfo(1, Block.snow.blockID),
         new FlatLayerInfo(1, Block.grass.blockID),
         new FlatLayerInfo(3, Block.dirt.blockID),
         new FlatLayerInfo(59, Block.stone.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
      addPreset(
         "Bottomless Pit",
         Item.feather.itemID,
         BiomeGenBase.plains,
         Arrays.asList("village", "biome_1"),
         new FlatLayerInfo(1, Block.grass.blockID),
         new FlatLayerInfo(3, Block.dirt.blockID),
         new FlatLayerInfo(2, Block.cobblestone.blockID)
      );
      addPreset(
         "Desert",
         Block.sand.blockID,
         BiomeGenBase.desert,
         Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"),
         new FlatLayerInfo(8, Block.sand.blockID),
         new FlatLayerInfo(52, Block.sandStone.blockID),
         new FlatLayerInfo(3, Block.stone.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
      addPresetNoFeatures(
         "Redstone Ready",
         Item.redstone.itemID,
         BiomeGenBase.desert,
         new FlatLayerInfo(52, Block.sandStone.blockID),
         new FlatLayerInfo(3, Block.stone.blockID),
         new FlatLayerInfo(1, Block.bedrock.blockID)
      );
   }
}
