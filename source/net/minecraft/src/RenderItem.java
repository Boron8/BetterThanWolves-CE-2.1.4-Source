package net.minecraft.src;

import com.prupe.mcpatcher.cit.CITUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderItem extends Render {
   private RenderBlocks itemRenderBlocks = new RenderBlocks();
   private Random random = new Random();
   public boolean renderWithColor = true;
   public float zLevel = 0.0F;
   public static boolean renderInFrame = false;
   public static boolean forceFancyItemRender = false;

   public RenderItem() {
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
      this.random.setSeed(187L);
      ItemStack var10 = par1EntityItem.getEntityItem();
      if (var10.getItem() != null) {
         GL11.glPushMatrix();
         float var11 = MathHelper.sin((par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
         float var12 = ((par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180.0F / (float)Math.PI);
         byte var13 = 1;
         if (par1EntityItem.getEntityItem().stackSize > 1) {
            var13 = 2;
         }

         if (par1EntityItem.getEntityItem().stackSize > 5) {
            var13 = 3;
         }

         if (par1EntityItem.getEntityItem().stackSize > 20) {
            var13 = 4;
         }

         if (par1EntityItem.getEntityItem().stackSize > 40) {
            var13 = 5;
         }

         GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
         GL11.glEnable(32826);
         if (var10.getItemSpriteNumber() == 0
            && Block.blocksList[var10.itemID] != null
            && Block.blocksList[var10.itemID].doesItemRenderAsBlock(var10.getItemDamage())) {
            Block var21 = Block.blocksList[var10.itemID];
            GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
            if (renderInFrame) {
               GL11.glScalef(1.25F, 1.25F, 1.25F);
               GL11.glTranslatef(0.0F, 0.05F, 0.0F);
               GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            this.a("/terrain.png");
            float var25 = 0.25F;
            int var24 = var21.getRenderType();
            if (var24 == 1 || var24 == 19 || var24 == 12 || var24 == 2) {
               var25 = 0.5F;
            }

            GL11.glScalef(var25, var25, var25);

            for (int var17 = 0; var17 < var13; var17++) {
               GL11.glPushMatrix();
               if (var17 > 0) {
                  float var18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
                  float var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
                  float var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
                  GL11.glTranslatef(var18, var19, var20);
               }

               float var18 = 1.0F;
               this.itemRenderBlocks.renderBlockAsItem(var21, var10.getItemDamage(), var18);
               GL11.glPopMatrix();
            }
         } else if (var10.getItem().requiresMultipleRenderPasses()) {
            if (renderInFrame) {
               GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
               GL11.glTranslatef(0.0F, -0.05F, 0.0F);
            } else {
               GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            this.a("/gui/items.png");

            for (int var14 = 0; var14 <= 1; var14++) {
               this.random.setSeed(187L);
               Icon var15 = CITUtils.getIcon(var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var14), var10, var14);
               float var16 = 1.0F;
               if (this.renderWithColor) {
                  int var17 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var14);
                  float var18 = (var17 >> 16 & 0xFF) / 255.0F;
                  float var19 = (var17 >> 8 & 0xFF) / 255.0F;
                  float var20 = (var17 & 0xFF) / 255.0F;
                  GL11.glColor4f(var18 * var16, var19 * var16, var20 * var16, 1.0F);
                  this.renderDroppedItem(par1EntityItem, var15, var13, par9, var18 * var16, var19 * var16, var20 * var16);
               } else {
                  this.renderDroppedItem(par1EntityItem, var15, var13, par9, 1.0F, 1.0F, 1.0F);
               }
            }
         } else {
            if (renderInFrame) {
               GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
               GL11.glTranslatef(0.0F, -0.05F, 0.0F);
            } else {
               GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            Icon var23 = var10.getIconIndex();
            if (var10.getItemSpriteNumber() == 0) {
               this.a("/terrain.png");
            } else {
               this.a("/gui/items.png");
            }

            if (this.renderWithColor) {
               int var22 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
               float var16 = (var22 >> 16 & 0xFF) / 255.0F;
               float var26 = (var22 >> 8 & 0xFF) / 255.0F;
               float var18 = (var22 & 0xFF) / 255.0F;
               float var19 = 1.0F;
               this.renderDroppedItem(par1EntityItem, var23, var13, par9, var16 * var19, var26 * var19, var18 * var19);
            } else {
               this.renderDroppedItem(par1EntityItem, var23, var13, par9, 1.0F, 1.0F, 1.0F);
            }
         }

         GL11.glDisable(32826);
         GL11.glPopMatrix();
      }
   }

   private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7) {
      Tessellator var8 = Tessellator.instance;
      if (par2Icon == null) {
         par2Icon = this.renderManager.renderEngine.getMissingIcon(par1EntityItem.getEntityItem().getItemSpriteNumber());
      }

      float var9 = par2Icon.getMinU();
      float var10 = par2Icon.getMaxU();
      float var11 = par2Icon.getMinV();
      float var12 = par2Icon.getMaxV();
      float var13 = 1.0F;
      float var14 = 0.5F;
      float var15 = 0.25F;
      if (!this.renderManager.options.fancyGraphics && !forceFancyItemRender) {
         for (int var25 = 0; var25 < par3; var25++) {
            GL11.glPushMatrix();
            if (var25 > 0) {
               float var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
               float var27 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
               float var26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
               GL11.glTranslatef(var17, var27, var26);
            }

            if (!renderInFrame) {
               GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            }

            GL11.glColor4f(par5, par6, par7, 1.0F);
            var8.startDrawingQuads();
            var8.setNormal(0.0F, 1.0F, 0.0F);
            var8.addVertexWithUV(0.0F - var14, 0.0F - var15, 0.0, var9, var12);
            var8.addVertexWithUV(var13 - var14, 0.0F - var15, 0.0, var10, var12);
            var8.addVertexWithUV(var13 - var14, 1.0F - var15, 0.0, var10, var11);
            var8.addVertexWithUV(0.0F - var14, 1.0F - var15, 0.0, var9, var11);
            var8.draw();
            GL11.glPopMatrix();
         }
      } else {
         GL11.glPushMatrix();
         if (renderInFrame) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GL11.glRotatef(((par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
         }

         float var16 = 0.0625F;
         float var17 = 0.021875F;
         ItemStack var18 = par1EntityItem.getEntityItem();
         int var19 = var18.stackSize;
         byte var24;
         if (var19 < 2) {
            var24 = 1;
         } else if (var19 < 16) {
            var24 = 2;
         } else if (var19 < 32) {
            var24 = 3;
         } else {
            var24 = 4;
         }

         GL11.glTranslatef(-var14, -var15, -((var16 + var17) * var24 / 2.0F));

         for (int var20 = 0; var20 < var24; var20++) {
            GL11.glTranslatef(0.0F, 0.0F, var16 + var17);
            if (var18.getItemSpriteNumber() == 0 && Block.blocksList[var18.itemID] != null) {
               this.a("/terrain.png");
            } else {
               this.a("/gui/items.png");
            }

            GL11.glColor4f(par5, par6, par7, 1.0F);
            ItemRenderer.renderItemIn2D(var8, var10, var11, var9, var12, par2Icon.getSheetWidth(), par2Icon.getSheetHeight(), var16);
            if (var18 != null && !CITUtils.renderEnchantmentDropped(var18) && var18.hasEffect()) {
               GL11.glDepthFunc(514);
               GL11.glDisable(2896);
               this.renderManager.renderEngine.bindTexture("%blur%/misc/glint.png");
               GL11.glEnable(3042);
               GL11.glBlendFunc(768, 1);
               float var21 = 0.76F;
               GL11.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
               GL11.glMatrixMode(5890);
               GL11.glPushMatrix();
               float var22 = 0.125F;
               GL11.glScalef(var22, var22, var22);
               float var23 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
               GL11.glTranslatef(var23, 0.0F, 0.0F);
               GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
               ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glScalef(var22, var22, var22);
               var23 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
               GL11.glTranslatef(-var23, 0.0F, 0.0F);
               GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
               ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
               GL11.glPopMatrix();
               GL11.glMatrixMode(5888);
               GL11.glDisable(3042);
               GL11.glEnable(2896);
               GL11.glDepthFunc(515);
            }
         }

         GL11.glPopMatrix();
      }
   }

   public void renderItemIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
      int var6 = par3ItemStack.itemID;
      int var7 = par3ItemStack.getItemDamage();
      Icon var8 = par3ItemStack.getIconIndex();
      if (par3ItemStack.getItemSpriteNumber() == 0 && Block.blocksList[var6].doesItemRenderAsBlock(par3ItemStack.getItemDamage())) {
         par2RenderEngine.bindTexture("/terrain.png");
         Block var15 = Block.blocksList[var6];
         GL11.glPushMatrix();
         GL11.glTranslatef(par4 - 2, par5 + 3, -3.0F + this.zLevel);
         GL11.glScalef(10.0F, 10.0F, 10.0F);
         GL11.glTranslatef(1.0F, 0.5F, 1.0F);
         GL11.glScalef(1.0F, 1.0F, -1.0F);
         GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         int var18 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
         float var16 = (var18 >> 16 & 0xFF) / 255.0F;
         float var12 = (var18 >> 8 & 0xFF) / 255.0F;
         float var13 = (var18 & 0xFF) / 255.0F;
         if (this.renderWithColor) {
            GL11.glColor4f(var16, var12, var13, 1.0F);
         }

         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         this.itemRenderBlocks.useInventoryTint = this.renderWithColor;
         this.itemRenderBlocks.renderBlockAsItem(var15, var7, 1.0F);
         this.itemRenderBlocks.useInventoryTint = true;
         GL11.glPopMatrix();
      } else if (Item.itemsList[var6].requiresMultipleRenderPasses()) {
         GL11.glDisable(2896);
         par2RenderEngine.bindTexture("/gui/items.png");

         for (int var9 = 0; var9 <= 1; var9++) {
            Icon var10 = CITUtils.getIcon(Item.itemsList[var6].getIconFromDamageForRenderPass(var7, var9), par3ItemStack, var9);
            int var11 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, var9);
            float var12 = (var11 >> 16 & 0xFF) / 255.0F;
            float var13 = (var11 >> 8 & 0xFF) / 255.0F;
            float var14 = (var11 & 0xFF) / 255.0F;
            if (this.renderWithColor) {
               GL11.glColor4f(var12, var13, var14, 1.0F);
            }

            this.renderIcon(par4, par5, var10, 16, 16);
         }

         GL11.glEnable(2896);
      } else {
         GL11.glDisable(2896);
         if (par3ItemStack.getItemSpriteNumber() == 0) {
            par2RenderEngine.bindTexture("/terrain.png");
         } else {
            par2RenderEngine.bindTexture("/gui/items.png");
         }

         if (var8 == null) {
            var8 = par2RenderEngine.getMissingIcon(par3ItemStack.getItemSpriteNumber());
         }

         int var9 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
         float var17 = (var9 >> 16 & 0xFF) / 255.0F;
         float var16 = (var9 >> 8 & 0xFF) / 255.0F;
         float var12 = (var9 & 0xFF) / 255.0F;
         if (this.renderWithColor) {
            GL11.glColor4f(var17, var16, var12, 1.0F);
         }

         this.renderIcon(par4, par5, var8, 16, 16);
         GL11.glEnable(2896);
      }

      GL11.glEnable(2884);
   }

   public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
      if (par3ItemStack != null) {
         this.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);
         if (!CITUtils.renderEnchantmentGUI(par3ItemStack, par4, par5, this.zLevel) && par3ItemStack.hasEffect()) {
            GL11.glDepthFunc(516);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            par2RenderEngine.bindTexture("%blur%/misc/glint.png");
            this.zLevel -= 50.0F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(774, 774);
            GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
            this.renderGlint(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            this.zLevel += 50.0F;
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
         }
      }
   }

   private void renderGlint(int par1, int par2, int par3, int par4, int par5) {
      for (int var6 = 0; var6 < 2; var6++) {
         if (var6 == 0) {
            GL11.glBlendFunc(768, 1);
         }

         if (var6 == 1) {
            GL11.glBlendFunc(768, 1);
         }

         float var7 = 0.00390625F;
         float var8 = 0.00390625F;
         float var9 = (float)(Minecraft.getSystemTime() % (3000 + var6 * 1873)) / (3000.0F + var6 * 1873) * 256.0F;
         float var10 = 0.0F;
         Tessellator var11 = Tessellator.instance;
         float var12 = 4.0F;
         if (var6 == 1) {
            var12 = -1.0F;
         }

         var11.startDrawingQuads();
         var11.addVertexWithUV(par2 + 0, par3 + par5, this.zLevel, (var9 + par5 * var12) * var7, (var10 + par5) * var8);
         var11.addVertexWithUV(par2 + par4, par3 + par5, this.zLevel, (var9 + par4 + par5 * var12) * var7, (var10 + par5) * var8);
         var11.addVertexWithUV(par2 + par4, par3 + 0, this.zLevel, (var9 + par4) * var7, (var10 + 0.0F) * var8);
         var11.addVertexWithUV(par2 + 0, par3 + 0, this.zLevel, (var9 + 0.0F) * var7, (var10 + 0.0F) * var8);
         var11.draw();
      }
   }

   public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
      this.renderItemOverlayIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5, (String)null);
   }

   public void renderItemOverlayIntoGUI(
      FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5, String par6Str
   ) {
      if (par3ItemStack != null) {
         if (par3ItemStack.stackSize > 1 || par6Str != null) {
            String var7 = par6Str == null ? String.valueOf(par3ItemStack.stackSize) : par6Str;
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            par1FontRenderer.drawStringWithShadow(var7, par4 + 19 - 2 - par1FontRenderer.getStringWidth(var7), par5 + 6 + 3, 16777215);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
         }

         if (par3ItemStack.isItemDamaged()) {
            int var12 = (int)Math.round(13.0 - par3ItemStack.getItemDamageForDisplay() * 13.0 / par3ItemStack.getMaxDamage());
            int var8 = (int)Math.round(255.0 - par3ItemStack.getItemDamageForDisplay() * 255.0 / par3ItemStack.getMaxDamage());
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            Tessellator var9 = Tessellator.instance;
            int var10 = 255 - var8 << 16 | var8 << 8;
            int var11 = (255 - var8) / 4 << 16 | 16128;
            this.renderQuad(var9, par4 + 2, par5 + 13, 13, 2, 0);
            this.renderQuad(var9, par4 + 2, par5 + 13, 12, 1, var11);
            this.renderQuad(var9, par4 + 2, par5 + 13, var12, 1, var10);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }
   }

   private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
      par1Tessellator.startDrawingQuads();
      par1Tessellator.setColorOpaque_I(par6);
      par1Tessellator.addVertex(par2 + 0, par3 + 0, 0.0);
      par1Tessellator.addVertex(par2 + 0, par3 + par5, 0.0);
      par1Tessellator.addVertex(par2 + par4, par3 + par5, 0.0);
      par1Tessellator.addVertex(par2 + par4, par3 + 0, 0.0);
      par1Tessellator.draw();
   }

   public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5) {
      Tessellator var6 = Tessellator.instance;
      var6.startDrawingQuads();
      var6.addVertexWithUV(par1 + 0, par2 + par5, this.zLevel, par3Icon.getMinU(), par3Icon.getMaxV());
      var6.addVertexWithUV(par1 + par4, par2 + par5, this.zLevel, par3Icon.getMaxU(), par3Icon.getMaxV());
      var6.addVertexWithUV(par1 + par4, par2 + 0, this.zLevel, par3Icon.getMaxU(), par3Icon.getMinV());
      var6.addVertexWithUV(par1 + 0, par2 + 0, this.zLevel, par3Icon.getMinU(), par3Icon.getMinV());
      var6.draw();
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderItem((EntityItem)par1Entity, par2, par4, par6, par8, par9);
   }
}
