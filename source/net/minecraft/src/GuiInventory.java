package net.minecraft.src;

import btw.client.render.util.RenderUtils;
import btw.inventory.container.PlayerContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class GuiInventory extends InventoryEffectRenderer {
   private float xSize_lo;
   private float ySize_lo;

   public GuiInventory(EntityPlayer par1EntityPlayer) {
      super(par1EntityPlayer.inventoryContainer);
      this.allowUserInput = true;
      par1EntityPlayer.addStat(AchievementList.openInventory, 1);
   }

   @Override
   public void updateScreen() {
      if (this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
      }
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      if (this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
      } else {
         super.initGui();
      }
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 86, 16, 4210752);
      this.drawSecondaryOutputIndicator();
   }

   @Override
   public void drawScreen(int par1, int par2, float par3) {
      super.drawScreen(par1, par2, par3);
      this.xSize_lo = par1;
      this.ySize_lo = par2;
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/inventory.png");
      int var4 = this.guiLeft;
      int var5 = this.guiTop;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
      drawPlayerOnGui(this.mc, var4 + 51, var5 + 75, 30, var4 + 51 - this.xSize_lo, var5 + 75 - 50 - this.ySize_lo);
   }

   public static void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5) {
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glTranslatef(par1, par2, 50.0F);
      GL11.glScalef(-par3, par3, par3);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      float var6 = par0Minecraft.thePlayer.renderYawOffset;
      float var7 = par0Minecraft.thePlayer.rotationYaw;
      float var8 = par0Minecraft.thePlayer.rotationPitch;
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-((float)Math.atan(par5 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
      par0Minecraft.thePlayer.renderYawOffset = (float)Math.atan(par4 / 40.0F) * 20.0F;
      par0Minecraft.thePlayer.rotationYaw = (float)Math.atan(par4 / 40.0F) * 40.0F;
      par0Minecraft.thePlayer.rotationPitch = -((float)Math.atan(par5 / 40.0F)) * 20.0F;
      par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
      GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
      RenderManager.instance.playerViewY = 180.0F;
      RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0, 0.0, 0.0, 0.0F, 1.0F);
      par0Minecraft.thePlayer.renderYawOffset = var6;
      par0Minecraft.thePlayer.rotationYaw = var7;
      par0Minecraft.thePlayer.rotationPitch = var8;
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   @Override
   protected void actionPerformed(GuiButton par1GuiButton) {
      if (par1GuiButton.id == 0) {
         this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
      }

      if (par1GuiButton.id == 1) {
         this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
      }
   }

   private void drawSecondaryOutputIndicator() {
      if (!this.mc.playerController.isInCreativeMode()) {
         PlayerContainer container = (PlayerContainer)this.mc.thePlayer.inventoryContainer;
         if (container != null) {
            IRecipe recipe = CraftingManager.getInstance().findMatchingIRecipe(container.craftMatrix, this.mc.theWorld);
            if (recipe != null && recipe.hasSecondaryOutput()) {
               Slot outputSlot = (Slot)container.inventorySlots.get(0);
               int iDisplayX = outputSlot.xDisplayPosition + 20;
               int iDisplayY = outputSlot.yDisplayPosition + 5;
               RenderUtils.drawSecondaryCraftingOutputIndicator(this.mc, iDisplayX, iDisplayY);
            }
         }
      }
   }
}
