package net.minecraft.src;

import java.util.Calendar;
import org.lwjgl.opengl.GL11;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer {
   private ModelChest chestModel = new ModelChest();
   private ModelChest largeChestModel = new ModelLargeChest();
   private boolean isChristmas;

   public TileEntityChestRenderer() {
      Calendar var1 = Calendar.getInstance();
      if (var1.get(2) + 1 == 12 && var1.get(5) >= 24 && var1.get(5) <= 26) {
         this.isChristmas = true;
      }
   }

   public void renderTileEntityChestAt(TileEntityChest var1, double var2, double var4, double var6, float var8) {
      int var9;
      if (!var1.o()) {
         var9 = 0;
      } else {
         Block var10 = var1.q();
         var9 = var1.p();
         if (var10 instanceof BlockChest && var9 == 0) {
            ((BlockChest)var10).unifyAdjacentChests(var1.az(), var1.xCoord, var1.yCoord, var1.zCoord);
            var9 = var1.p();
         }

         var1.checkForAdjacentChests();
      }

      if (var1.adjacentChestZNeg == null && var1.adjacentChestXNeg == null) {
         ModelChest var14;
         if (var1.adjacentChestXPos == null && var1.adjacentChestZPosition == null) {
            var14 = this.chestModel;
            if (var1.func_98041_l() == 1) {
               this.a("/item/chests/trap_small.png");
            } else if (this.isChristmas) {
               this.a("/item/xmaschest.png");
            } else {
               this.a("/item/chest.png");
            }
         } else {
            var14 = this.largeChestModel;
            if (var1.func_98041_l() == 1) {
               this.a("/item/chests/trap_large.png");
            } else if (this.isChristmas) {
               this.a("/item/largexmaschest.png");
            } else {
               this.a("/item/largechest.png");
            }
         }

         GL11.glPushMatrix();
         GL11.glEnable(32826);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glTranslatef((float)var2, (float)var4 + 1.0F, (float)var6 + 1.0F);
         GL11.glScalef(1.0F, -1.0F, -1.0F);
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         short var11 = 0;
         if (var9 == 2) {
            var11 = 180;
         }

         if (var9 == 3) {
            var11 = 0;
         }

         if (var9 == 4) {
            var11 = 90;
         }

         if (var9 == 5) {
            var11 = -90;
         }

         if (var9 == 2 && var1.adjacentChestXPos != null) {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
         }

         if (var9 == 5 && var1.adjacentChestZPosition != null) {
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
         }

         GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         float var12 = var1.prevLidAngle + (var1.lidAngle - var1.prevLidAngle) * var8;
         if (var1.adjacentChestZNeg != null) {
            float var13 = var1.adjacentChestZNeg.prevLidAngle + (var1.adjacentChestZNeg.lidAngle - var1.adjacentChestZNeg.prevLidAngle) * var8;
            if (var13 > var12) {
               var12 = var13;
            }
         }

         if (var1.adjacentChestXNeg != null) {
            float var17 = var1.adjacentChestXNeg.prevLidAngle + (var1.adjacentChestXNeg.lidAngle - var1.adjacentChestXNeg.prevLidAngle) * var8;
            if (var17 > var12) {
               var12 = var17;
            }
         }

         var12 = 1.0F - var12;
         var12 = 1.0F - var12 * var12 * var12;
         var14.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 2.0F);
         var14.renderAll();
         GL11.glDisable(32826);
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }
}
