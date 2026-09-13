package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.lwjgl.opengl.GL11;

public class GuiBeacon extends GuiContainer {
   private TileEntityBeacon beacon;
   private GuiBeaconButtonConfirm beaconConfirmButton;
   private boolean buttonsNotDrawn;

   public GuiBeacon(InventoryPlayer var1, TileEntityBeacon var2) {
      super(new ContainerBeacon(var1, var2));
      this.beacon = var2;
      this.xSize = 230;
      this.ySize = 219;
   }

   @Override
   public void initGui() {
      super.initGui();
      this.buttonList.add(this.beaconConfirmButton = new GuiBeaconButtonConfirm(this, -1, this.guiLeft + 164, this.guiTop + 107));
      this.buttonList.add(new GuiBeaconButtonCancel(this, -2, this.guiLeft + 190, this.guiTop + 107));
      this.buttonsNotDrawn = true;
      this.beaconConfirmButton.enabled = false;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      if (this.buttonsNotDrawn && this.beacon.getLevels() >= 0) {
         this.buttonsNotDrawn = false;

         for (int var1 = 0; var1 <= 2; var1++) {
            int var2 = TileEntityBeacon.effectsList[var1].length;
            int var3 = var2 * 22 + (var2 - 1) * 2;

            for (int var4 = 0; var4 < var2; var4++) {
               int var5 = TileEntityBeacon.effectsList[var1][var4].id;
               GuiBeaconButtonPower var6 = new GuiBeaconButtonPower(
                  this, var1 << 8 | var5, this.guiLeft + 76 + var4 * 24 - var3 / 2, this.guiTop + 22 + var1 * 25, var5, var1
               );
               this.buttonList.add(var6);
               if (var1 >= this.beacon.getLevels()) {
                  var6.enabled = false;
               } else if (var5 == this.beacon.getPrimaryEffect()) {
                  var6.b(true);
               }
            }
         }

         byte var7 = 3;
         int var8 = TileEntityBeacon.effectsList[var7].length + 1;
         int var9 = var8 * 22 + (var8 - 1) * 2;

         for (int var10 = 0; var10 < var8 - 1; var10++) {
            int var12 = TileEntityBeacon.effectsList[var7][var10].id;
            GuiBeaconButtonPower var13 = new GuiBeaconButtonPower(
               this, var7 << 8 | var12, this.guiLeft + 167 + var10 * 24 - var9 / 2, this.guiTop + 47, var12, var7
            );
            this.buttonList.add(var13);
            if (var7 >= this.beacon.getLevels()) {
               var13.enabled = false;
            } else if (var12 == this.beacon.getSecondaryEffect()) {
               var13.b(true);
            }
         }

         if (this.beacon.getPrimaryEffect() > 0) {
            GuiBeaconButtonPower var11 = new GuiBeaconButtonPower(
               this,
               var7 << 8 | this.beacon.getPrimaryEffect(),
               this.guiLeft + 167 + (var8 - 1) * 24 - var9 / 2,
               this.guiTop + 47,
               this.beacon.getPrimaryEffect(),
               var7
            );
            this.buttonList.add(var11);
            if (var7 >= this.beacon.getLevels()) {
               var11.enabled = false;
            } else if (this.beacon.getPrimaryEffect() == this.beacon.getSecondaryEffect()) {
               var11.b(true);
            }
         }
      }

      this.beaconConfirmButton.enabled = this.beacon.getStackInSlot(0) != null && this.beacon.getPrimaryEffect() > 0;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == -2) {
         this.mc.displayGuiScreen(null);
      } else if (var1.id == -1) {
         String var2 = "MC|Beacon";
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         DataOutputStream var4 = new DataOutputStream(var3);

         try {
            var4.writeInt(this.beacon.getPrimaryEffect());
            var4.writeInt(this.beacon.getSecondaryEffect());
            this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         this.mc.displayGuiScreen(null);
      } else if (var1 instanceof GuiBeaconButtonPower) {
         if (((GuiBeaconButtonPower)var1).b()) {
            return;
         }

         int var7 = var1.id;
         int var8 = var7 & 0xFF;
         int var9 = var7 >> 8;
         if (var9 < 3) {
            this.beacon.setPrimaryEffect(var8);
         } else {
            this.beacon.setSecondaryEffect(var8);
         }

         this.buttonList.clear();
         this.initGui();
         this.updateScreen();
      }
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      RenderHelper.disableStandardItemLighting();
      this.a(this.fontRenderer, StatCollector.translateToLocal("tile.beacon.primary"), 62, 10, 14737632);
      this.a(this.fontRenderer, StatCollector.translateToLocal("tile.beacon.secondary"), 169, 10, 14737632);

      for (GuiButton var4 : this.buttonList) {
         if (var4.func_82252_a()) {
            var4.func_82251_b(var1 - this.guiLeft, var2 - this.guiTop);
            break;
         }
      }

      RenderHelper.enableGUIStandardItemLighting();
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/beacon.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
      selectedButton.zLevel = 100.0F;
      selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.emerald), var4 + 42, var5 + 109);
      selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.diamond), var4 + 42 + 22, var5 + 109);
      selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.ingotGold), var4 + 42 + 44, var5 + 109);
      selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.ingotIron), var4 + 42 + 66, var5 + 109);
      selectedButton.zLevel = 0.0F;
   }
}
