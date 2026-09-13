package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.cit.CITUtils;
import com.prupe.mcpatcher.mal.block.BlockAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ItemRenderer {
   private Minecraft mc;
   private ItemStack itemToRender = null;
   private float equippedProgress = 0.0F;
   private float prevEquippedProgress = 0.0F;
   private RenderBlocks renderBlocksInstance = new RenderBlocks();
   public final MapItemRenderer mapItemRenderer;
   private int equippedItemSlot = -1;

   public ItemRenderer(Minecraft par1Minecraft) {
      this.mc = par1Minecraft;
      this.mapItemRenderer = new MapItemRenderer(par1Minecraft.fontRenderer, par1Minecraft.gameSettings, par1Minecraft.renderEngine);
   }

   public void renderItem(EntityLiving par1EntityLiving, ItemStack par2ItemStack, int par3) {
      GL11.glPushMatrix();
      if (par2ItemStack.getItemSpriteNumber() == 0
         && Block.blocksList[par2ItemStack.itemID] != null
         && Block.blocksList[par2ItemStack.itemID].doesItemRenderAsBlock(par2ItemStack.getItemDamage())) {
         this.mc.renderEngine.bindTexture("/terrain.png");
         this.renderBlocksInstance.renderBlockAsItem(Block.blocksList[par2ItemStack.itemID], par2ItemStack.getItemDamage(), 1.0F);
      } else {
         Icon var4 = CITUtils.getIcon(par1EntityLiving.getItemIcon(par2ItemStack, par3), par2ItemStack, par3);
         if (var4 == null) {
            GL11.glPopMatrix();
            return;
         }

         if (par2ItemStack.getItemSpriteNumber() == 0) {
            this.mc.renderEngine.bindTexture("/terrain.png");
         } else {
            this.mc.renderEngine.bindTexture("/gui/items.png");
         }

         Tessellator var5 = Tessellator.instance;
         float var6 = var4.getMinU();
         float var7 = var4.getMaxU();
         float var8 = var4.getMinV();
         float var9 = var4.getMaxV();
         float var10 = 0.0F;
         float var11 = 0.3F;
         GL11.glEnable(32826);
         GL11.glTranslatef(-var10, -var11, 0.0F);
         float var12 = 1.5F;
         GL11.glScalef(var12, var12, var12);
         GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
         ColorizeBlock.colorizeWaterBlockGL(BlockAPI.getBlockById(par2ItemStack.itemID));
         renderItemIn2D(var5, var7, var8, var6, var9, var4.getSheetWidth(), var4.getSheetHeight(), 0.0625F);
         if (par2ItemStack != null && !CITUtils.renderEnchantmentHeld(par2ItemStack, par3) && par2ItemStack.hasEffect() && par3 == 0) {
            GL11.glDepthFunc(514);
            GL11.glDisable(2896);
            this.mc.renderEngine.bindTexture("%blur%/misc/glint.png");
            GL11.glEnable(3042);
            GL11.glBlendFunc(768, 1);
            float var13 = 0.76F;
            GL11.glColor4f(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            float var14 = 0.125F;
            GL11.glScalef(var14, var14, var14);
            float var15 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(var15, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(var14, var14, var14);
            var15 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-var15, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
         }

         GL11.glDisable(32826);
      }

      GL11.glPopMatrix();
   }

   public static void renderItemIn2D(Tessellator par0Tessellator, float par1, float par2, float par3, float par4, int par5, int par6, float par7) {
      par0Tessellator.startDrawingQuads();
      par0Tessellator.setNormal(0.0F, 0.0F, 1.0F);
      par0Tessellator.addVertexWithUV(0.0, 0.0, 0.0, par1, par4);
      par0Tessellator.addVertexWithUV(1.0, 0.0, 0.0, par3, par4);
      par0Tessellator.addVertexWithUV(1.0, 1.0, 0.0, par3, par2);
      par0Tessellator.addVertexWithUV(0.0, 1.0, 0.0, par1, par2);
      par0Tessellator.draw();
      par0Tessellator.startDrawingQuads();
      par0Tessellator.setNormal(0.0F, 0.0F, -1.0F);
      par0Tessellator.addVertexWithUV(0.0, 1.0, 0.0F - par7, par1, par2);
      par0Tessellator.addVertexWithUV(1.0, 1.0, 0.0F - par7, par3, par2);
      par0Tessellator.addVertexWithUV(1.0, 0.0, 0.0F - par7, par3, par4);
      par0Tessellator.addVertexWithUV(0.0, 0.0, 0.0F - par7, par1, par4);
      par0Tessellator.draw();
      float var8 = par5 * (par1 - par3);
      float var9 = par6 * (par4 - par2);
      par0Tessellator.startDrawingQuads();
      par0Tessellator.setNormal(-1.0F, 0.0F, 0.0F);

      for (int var10 = 0; var10 < var8; var10++) {
         float var11 = var10 / var8;
         float var12 = par1 + (par3 - par1) * var11 - 0.5F / par5;
         par0Tessellator.addVertexWithUV(var11, 0.0, 0.0F - par7, var12, par4);
         par0Tessellator.addVertexWithUV(var11, 0.0, 0.0, var12, par4);
         par0Tessellator.addVertexWithUV(var11, 1.0, 0.0, var12, par2);
         par0Tessellator.addVertexWithUV(var11, 1.0, 0.0F - par7, var12, par2);
      }

      par0Tessellator.draw();
      par0Tessellator.startDrawingQuads();
      par0Tessellator.setNormal(1.0F, 0.0F, 0.0F);

      for (int var14 = 0; var14 < var8; var14++) {
         float var11 = var14 / var8;
         float var12 = par1 + (par3 - par1) * var11 - 0.5F / par5;
         float var13 = var11 + 1.0F / var8;
         par0Tessellator.addVertexWithUV(var13, 1.0, 0.0F - par7, var12, par2);
         par0Tessellator.addVertexWithUV(var13, 1.0, 0.0, var12, par2);
         par0Tessellator.addVertexWithUV(var13, 0.0, 0.0, var12, par4);
         par0Tessellator.addVertexWithUV(var13, 0.0, 0.0F - par7, var12, par4);
      }

      par0Tessellator.draw();
      par0Tessellator.startDrawingQuads();
      par0Tessellator.setNormal(0.0F, 1.0F, 0.0F);

      for (int var15 = 0; var15 < var9; var15++) {
         float var11 = var15 / var9;
         float var12 = par4 + (par2 - par4) * var11 - 0.5F / par6;
         float var13 = var11 + 1.0F / var9;
         par0Tessellator.addVertexWithUV(0.0, var13, 0.0, par1, var12);
         par0Tessellator.addVertexWithUV(1.0, var13, 0.0, par3, var12);
         par0Tessellator.addVertexWithUV(1.0, var13, 0.0F - par7, par3, var12);
         par0Tessellator.addVertexWithUV(0.0, var13, 0.0F - par7, par1, var12);
      }

      par0Tessellator.draw();
      par0Tessellator.startDrawingQuads();
      par0Tessellator.setNormal(0.0F, -1.0F, 0.0F);

      for (int var16 = 0; var16 < var9; var16++) {
         float var11 = var16 / var9;
         float var12 = par4 + (par2 - par4) * var11 - 0.5F / par6;
         par0Tessellator.addVertexWithUV(1.0, var11, 0.0, par3, var12);
         par0Tessellator.addVertexWithUV(0.0, var11, 0.0, par1, var12);
         par0Tessellator.addVertexWithUV(0.0, var11, 0.0F - par7, par1, var12);
         par0Tessellator.addVertexWithUV(1.0, var11, 0.0F - par7, par3, var12);
      }

      par0Tessellator.draw();
   }

   public void renderItemInFirstPerson(float par1) {
      float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * par1;
      EntityClientPlayerMP var3 = this.mc.thePlayer;
      float var4 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * par1;
      GL11.glPushMatrix();
      GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GL11.glPopMatrix();
      if (var3 instanceof EntityPlayerSP) {
         float var6 = var3.prevRenderArmPitch + (var3.renderArmPitch - var3.prevRenderArmPitch) * par1;
         float var7 = var3.prevRenderArmYaw + (var3.renderArmYaw - var3.prevRenderArmYaw) * par1;
         GL11.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
      }

      ItemStack var17 = this.itemToRender;
      float var6 = this.mc.theWorld.q(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
      var6 = 1.0F;
      int var18 = this.mc.theWorld.h(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ), 0);
      int var8 = var18 % 65536;
      int var9 = var18 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var8 / 1.0F, var9 / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (var17 != null) {
         var18 = Item.itemsList[var17.itemID].getColorFromItemStack(var17, 0);
         float var20 = (var18 >> 16 & 0xFF) / 255.0F;
         float var21 = (var18 >> 8 & 0xFF) / 255.0F;
         float var10 = (var18 & 0xFF) / 255.0F;
         GL11.glColor4f(var6 * var20, var6 * var21, var6 * var10, 1.0F);
      } else {
         GL11.glColor4f(var6, var6, var6, 1.0F);
      }

      if (var17 != null && var17.itemID == Item.map.itemID) {
         GL11.glPushMatrix();
         float var7 = 0.8F;
         float var20 = var3.g(par1);
         float var21 = MathHelper.sin(var20 * (float) Math.PI);
         float var10 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float) Math.PI);
         GL11.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var20) * (float) Math.PI * 2.0F) * 0.2F, -var21 * 0.2F);
         var20 = 1.0F - var4 / 45.0F + 0.1F;
         if (var20 < 0.0F) {
            var20 = 0.0F;
         }

         if (var20 > 1.0F) {
            var20 = 1.0F;
         }

         var20 = -MathHelper.cos(var20 * (float) Math.PI) * 0.5F + 0.5F;
         GL11.glTranslatef(0.0F, 0.0F * var7 - (1.0F - var2) * 1.2F - var20 * 0.5F + 0.04F, -0.9F * var7);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(var20 * -85.0F, 0.0F, 0.0F, 1.0F);
         GL11.glEnable(32826);
         GL11.glBindTexture(3553, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.N()));
         this.mc.renderEngine.resetBoundTexture();

         for (int var29 = 0; var29 < 2; var29++) {
            int var22 = var29 * 2 - 1;
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.0F, -0.6F, 1.1F * var22);
            GL11.glRotatef(-45 * var22, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-65 * var22, 0.0F, 1.0F, 0.0F);
            Render var24 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
            RenderPlayer var26 = (RenderPlayer)var24;
            float var13 = 1.0F;
            GL11.glScalef(var13, var13, var13);
            var26.renderFirstPersonArm(this.mc.thePlayer);
            GL11.glPopMatrix();
         }

         var21 = var3.g(par1);
         var10 = MathHelper.sin(var21 * var21 * (float) Math.PI);
         float var11 = MathHelper.sin(MathHelper.sqrt_float(var21) * (float) Math.PI);
         GL11.glRotatef(-var10 * 20.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-var11 * 20.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(-var11 * 80.0F, 1.0F, 0.0F, 0.0F);
         float var12 = 0.38F;
         GL11.glScalef(var12, var12, var12);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
         float var13 = 0.015625F;
         GL11.glScalef(var13, var13, var13);
         this.mc.renderEngine.bindTexture("/misc/mapbg.png");
         Tessellator var28 = Tessellator.instance;
         GL11.glNormal3f(0.0F, 0.0F, -1.0F);
         var28.startDrawingQuads();
         byte var27 = 7;
         var28.addVertexWithUV(0 - var27, 128 + var27, 0.0, 0.0, 1.0);
         var28.addVertexWithUV(128 + var27, 128 + var27, 0.0, 1.0, 1.0);
         var28.addVertexWithUV(128 + var27, 0 - var27, 0.0, 1.0, 0.0);
         var28.addVertexWithUV(0 - var27, 0 - var27, 0.0, 0.0, 0.0);
         var28.draw();
         MapData var16 = Item.map.getMapData(var17, this.mc.theWorld);
         if (var16 != null) {
            this.mapItemRenderer.renderMap(this.mc.thePlayer, this.mc.renderEngine, var16);
         }

         GL11.glPopMatrix();
      } else if (var17 != null) {
         GL11.glPushMatrix();
         float var7x = 0.8F;
         if (var3.bW() > 0) {
            EnumAction var19 = var17.getItemUseAction();
            if (var19 == EnumAction.eat || var19 == EnumAction.drink) {
               float var21x = var3.bW() - par1 + 1.0F;
               float var10x = 1.0F - var21x / var17.getMaxItemUseDuration();
               float var11 = 1.0F - var10x;
               var11 = var11 * var11 * var11;
               var11 = var11 * var11 * var11;
               var11 = var11 * var11 * var11;
               float var12 = 1.0F - var11;
               GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(var21x / 4.0F * (float) Math.PI) * 0.1F) * (var10x > 0.2 ? 1 : 0), 0.0F);
               GL11.glTranslatef(var12 * 0.6F, -var12 * 0.5F, 0.0F);
               GL11.glRotatef(var12 * 90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(var12 * 10.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(var12 * 30.0F, 0.0F, 0.0F, 1.0F);
            } else if (var19 == EnumAction.miscUse) {
               float var21x = var3.bW() - par1 + 1.0F;
               float var11 = var21x / var17.getMaxItemUseDuration();
               var11 = var11 * var11 * var11;
               var11 = var11 * var11 * var11;
               var11 = var11 * var11 * var11;
               float var12 = 1.0F - var11;
               GL11.glTranslatef(
                  0.0F,
                  MathHelper.abs(MathHelper.cos(var21x / 4.0F * (float) Math.PI) * 0.1F)
                     * (var17.getMaxItemUseDuration() - var3.bW() >= var17.getItem().getItemUseWarmupDuration() ? 1.0F : 0.0F),
                  0.0F
               );
               int iItemInUseCount = MathHelper.clamp_int(32 - (var17.getMaxItemUseDuration() - var3.bW()), 0, 32);
               var21x = iItemInUseCount - par1 + 1.0F;
               var11 = var21x / 32.0F;
               var11 = var11 * var11 * var11;
               var11 = var11 * var11 * var11;
               var11 = var11 * var11 * var11;
               var12 = 1.0F - var11;
               GL11.glTranslatef(var12 * 0.6F, -var12 * 0.5F, 0.0F);
               GL11.glRotatef(var12 * 90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(var12 * 10.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(var12 * 30.0F, 0.0F, 0.0F, 1.0F);
            }
         } else {
            float var20x = var3.g(par1);
            float var21x = MathHelper.sin(var20x * (float) Math.PI);
            float var10x = MathHelper.sin(MathHelper.sqrt_float(var20x) * (float) Math.PI);
            GL11.glTranslatef(-var10x * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var20x) * (float) Math.PI * 2.0F) * 0.2F, -var21x * 0.2F);
         }

         GL11.glTranslatef(0.7F * var7x, -0.65F * var7x - (1.0F - var2) * 0.6F, -0.9F * var7x);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glEnable(32826);
         float var20x = var3.g(par1);
         float var21x = MathHelper.sin(var20x * var20x * (float) Math.PI);
         float var10x = MathHelper.sin(MathHelper.sqrt_float(var20x) * (float) Math.PI);
         GL11.glRotatef(-var21x * 20.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-var10x * 20.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(-var10x * 80.0F, 1.0F, 0.0F, 0.0F);
         float var11 = 0.4F;
         GL11.glScalef(var11, var11, var11);
         if (var3.bW() > 0) {
            EnumAction var23 = var17.getItemUseAction();
            if (var23 == EnumAction.block) {
               GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
               GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
            } else if (var23 == EnumAction.bow) {
               GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
               GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
               GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
               float var13 = var17.getMaxItemUseDuration() - (var3.bW() - par1 + 1.0F);
               float var14 = var13 / 20.0F;
               var14 = (var14 * var14 + var14 * 2.0F) / 3.0F;
               if (var14 > 1.0F) {
                  var14 = 1.0F;
               }

               if (var14 > 0.1F) {
                  GL11.glTranslatef(0.0F, MathHelper.sin((var13 - 0.1F) * 1.3F) * 0.01F * (var14 - 0.1F), 0.0F);
               }

               GL11.glTranslatef(0.0F, 0.0F, var14 * 0.1F);
               GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
               GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(0.0F, 0.5F, 0.0F);
               float var15 = 1.0F + var14 * 0.2F;
               GL11.glScalef(1.0F, 1.0F, var15);
               GL11.glTranslatef(0.0F, -0.5F, 0.0F);
               GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            }
         }

         if (var17.getItem().shouldRotateAroundWhenRendering()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         }

         int iItemID = var17.getItem().itemID;
         if (iItemID == Item.compass.itemID) {
            TextureCompass.compassTexture.updateActive();
         }

         if (var17.getItem().requiresMultipleRenderPasses()) {
            this.renderItem(var3, var17, 0);
            int var25 = Item.itemsList[var17.itemID].getColorFromItemStack(var17, 1);
            float var13x = (var25 >> 16 & 0xFF) / 255.0F;
            float var14x = (var25 >> 8 & 0xFF) / 255.0F;
            float var15 = (var25 & 0xFF) / 255.0F;
            GL11.glColor4f(var6 * var13x, var6 * var14x, var6 * var15, 1.0F);
            this.renderItem(var3, var17, 1);
         } else {
            this.renderItem(var3, var17, 0);
         }

         if (iItemID == Item.compass.itemID) {
            TextureCompass.compassTexture.updateInert();
         }

         GL11.glPopMatrix();
      } else if (!var3.ai()) {
         GL11.glPushMatrix();
         float var7xx = 0.8F;
         float var20xx = var3.g(par1);
         float var21xx = MathHelper.sin(var20xx * (float) Math.PI);
         float var10xx = MathHelper.sin(MathHelper.sqrt_float(var20xx) * (float) Math.PI);
         GL11.glTranslatef(-var10xx * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var20xx) * (float) Math.PI * 2.0F) * 0.4F, -var21xx * 0.4F);
         GL11.glTranslatef(0.8F * var7xx, -0.75F * var7xx - (1.0F - var2) * 0.6F, -0.9F * var7xx);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glEnable(32826);
         var20xx = var3.g(par1);
         var21xx = MathHelper.sin(var20xx * var20xx * (float) Math.PI);
         var10xx = MathHelper.sin(MathHelper.sqrt_float(var20xx) * (float) Math.PI);
         GL11.glRotatef(var10xx * 70.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-var21xx * 20.0F, 0.0F, 0.0F, 1.0F);
         GL11.glBindTexture(3553, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.N()));
         this.mc.renderEngine.resetBoundTexture();
         GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
         GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glTranslatef(5.6F, 0.0F, 0.0F);
         Render var24 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
         RenderPlayer var26 = (RenderPlayer)var24;
         float var13x = 1.0F;
         GL11.glScalef(var13x, var13x, var13x);
         var26.renderFirstPersonArm(this.mc.thePlayer);
         GL11.glPopMatrix();
      }

      GL11.glDisable(32826);
      RenderHelper.disableStandardItemLighting();
   }

   public void renderOverlays(float par1) {
      GL11.glDisable(3008);
      if (this.mc.thePlayer.ae()) {
         this.mc.renderEngine.bindTexture("/terrain.png");
         this.renderFireInFirstPerson(par1);
      }

      if (this.mc.thePlayer.S()) {
         int var2 = MathHelper.floor_double(this.mc.thePlayer.posX);
         int var3 = MathHelper.floor_double(this.mc.thePlayer.posY);
         int var4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
         this.mc.renderEngine.bindTexture("/terrain.png");
         int var5 = this.mc.theWorld.a(var2, var3, var4);
         if (this.mc.theWorld.u(var2, var3, var4)) {
            this.renderInsideOfBlock(par1, Block.blocksList[var5].getBlockTextureFromSide(2));
         } else {
            for (int var6 = 0; var6 < 8; var6++) {
               float var7 = ((var6 >> 0) % 2 - 0.5F) * this.mc.thePlayer.width * 0.9F;
               float var8 = ((var6 >> 1) % 2 - 0.5F) * this.mc.thePlayer.height * 0.2F;
               float var9 = ((var6 >> 2) % 2 - 0.5F) * this.mc.thePlayer.width * 0.9F;
               int var10 = MathHelper.floor_float(var2 + var7);
               int var11 = MathHelper.floor_float(var3 + var8);
               int var12 = MathHelper.floor_float(var4 + var9);
               if (this.mc.theWorld.u(var10, var11, var12)) {
                  var5 = this.mc.theWorld.a(var10, var11, var12);
               }
            }
         }

         if (Block.blocksList[var5] != null) {
            this.renderInsideOfBlock(par1, Block.blocksList[var5].getBlockTextureFromSide(2));
         }
      }

      if (this.mc.thePlayer.a(Material.water)) {
         this.mc.renderEngine.bindTexture("/misc/water.png");
         this.renderWarpedTextureOverlay(par1);
      }

      GL11.glEnable(3008);
   }

   private void renderInsideOfBlock(float par1, Icon par2Icon) {
      Tessellator var3 = Tessellator.instance;
      float var4 = 0.1F;
      GL11.glColor4f(var4, var4, var4, 0.5F);
      GL11.glPushMatrix();
      float var5 = -1.0F;
      float var6 = 1.0F;
      float var7 = -1.0F;
      float var8 = 1.0F;
      float var9 = -0.5F;
      float var10 = par2Icon.getMinU();
      float var11 = par2Icon.getMaxU();
      float var12 = par2Icon.getMinV();
      float var13 = par2Icon.getMaxV();
      var3.startDrawingQuads();
      var3.addVertexWithUV(var5, var7, var9, var11, var13);
      var3.addVertexWithUV(var6, var7, var9, var10, var13);
      var3.addVertexWithUV(var6, var8, var9, var10, var12);
      var3.addVertexWithUV(var5, var8, var9, var11, var12);
      var3.draw();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderWarpedTextureOverlay(float par1) {
      Tessellator var2 = Tessellator.instance;
      float var3 = this.mc.thePlayer.c(par1);
      GL11.glColor4f(var3, var3, var3, 0.5F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glPushMatrix();
      float var4 = 4.0F;
      float var5 = -1.0F;
      float var6 = 1.0F;
      float var7 = -1.0F;
      float var8 = 1.0F;
      float var9 = -0.5F;
      float var10 = -this.mc.thePlayer.rotationYaw / 64.0F;
      float var11 = this.mc.thePlayer.rotationPitch / 64.0F;
      var2.startDrawingQuads();
      var2.addVertexWithUV(var5, var7, var9, var4 + var10, var4 + var11);
      var2.addVertexWithUV(var6, var7, var9, 0.0F + var10, var4 + var11);
      var2.addVertexWithUV(var6, var8, var9, 0.0F + var10, 0.0F + var11);
      var2.addVertexWithUV(var5, var8, var9, var4 + var10, 0.0F + var11);
      var2.draw();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
   }

   private void renderFireInFirstPerson(float par1) {
      Tessellator var2 = Tessellator.instance;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      float var3 = 1.0F;

      for (int var4 = 0; var4 < 2; var4++) {
         GL11.glPushMatrix();
         Icon var5 = Block.fire.func_94438_c(1);
         float var6 = var5.getMinU();
         float var7 = var5.getMaxU();
         float var8 = var5.getMinV();
         float var9 = var5.getMaxV();
         float var10 = (0.0F - var3) / 2.0F;
         float var11 = var10 + var3;
         float var12 = 0.0F - var3 / 2.0F;
         float var13 = var12 + var3;
         float var14 = -0.5F;
         GL11.glTranslatef(-(var4 * 2 - 1) * 0.24F, -0.3F, 0.0F);
         GL11.glRotatef((var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
         var2.startDrawingQuads();
         var2.addVertexWithUV(var10, var12, var14, var7, var9);
         var2.addVertexWithUV(var11, var12, var14, var6, var9);
         var2.addVertexWithUV(var11, var13, var14, var6, var8);
         var2.addVertexWithUV(var10, var13, var14, var7, var8);
         var2.draw();
         GL11.glPopMatrix();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
   }

   public void updateEquippedItem() {
      this.prevEquippedProgress = this.equippedProgress;
      EntityClientPlayerMP var1 = this.mc.thePlayer;
      ItemStack var2 = var1.inventory.getCurrentItem();
      boolean var3 = this.equippedItemSlot == var1.inventory.currentItem && var2 == this.itemToRender;
      if (this.itemToRender == null && var2 == null) {
         var3 = true;
      }

      if (var2 != null
         && this.itemToRender != null
         && var2 != this.itemToRender
         && var2.itemID == this.itemToRender.itemID
         && (var2.getItemDamage() == this.itemToRender.getItemDamage() || this.itemToRender.getItem().ignoreDamageWhenComparingDuringUse())) {
         this.itemToRender = var2;
         var3 = true;
      }

      float var4 = 0.4F;
      float var5 = var3 ? 1.0F : 0.0F;
      float var6 = var5 - this.equippedProgress;
      if (var6 < -var4) {
         var6 = -var4;
      }

      if (var6 > var4) {
         var6 = var4;
      }

      this.equippedProgress += var6;
      if (this.equippedProgress < 0.1F) {
         this.itemToRender = var2;
         this.equippedItemSlot = var1.inventory.currentItem;
      }
   }

   public void resetEquippedProgress() {
      this.equippedProgress = 0.0F;
   }

   public void resetEquippedProgress2() {
      this.equippedProgress = 0.0F;
   }
}
