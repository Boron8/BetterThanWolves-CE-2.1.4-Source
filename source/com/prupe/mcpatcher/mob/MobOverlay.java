package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Tessellator;

@Environment(EnvType.CLIENT)
public class MobOverlay {
   private static final FakeResourceLocation MOOSHROOM_OVERLAY = TexturePackAPI.newMCPatcherResourceLocation("/mob/redcow_overlay.png");
   private static final FakeResourceLocation SNOWMAN_OVERLAY = TexturePackAPI.newMCPatcherResourceLocation("/mob/snowman_overlay.png");
   private static final double MOO_X0 = -0.45;
   private static final double MOO_X1 = 0.45;
   private static final double MOO_Y0 = -0.5;
   private static final double MOO_Y1 = 0.5;
   private static final double MOO_Z0 = -0.45;
   private static final double MOO_Z1 = 0.45;
   private static final double SNOW_X0 = -0.5;
   private static final double SNOW_X1 = 0.5;
   private static final double SNOW_Y0 = -0.5;
   private static final double SNOW_Y1 = 0.5;
   private static final double SNOW_Z0 = -0.5;
   private static final double SNOW_Z1 = 0.5;
   private static boolean overlayActive;
   private static int overlayCounter;
   private static boolean haveMooshroom;
   private static boolean haveSnowman;

   static void reset() {
      haveMooshroom = TexturePackAPI.hasResource(MOOSHROOM_OVERLAY);
      haveSnowman = TexturePackAPI.hasResource(SNOWMAN_OVERLAY);
   }

   public static FakeResourceLocation setupMooshroom(EntityLiving entity, FakeResourceLocation defaultTexture) {
      overlayCounter = 0;
      if (haveMooshroom) {
         overlayActive = true;
         return MobRandomizer.randomTexture(entity, MOOSHROOM_OVERLAY);
      } else {
         overlayActive = false;
         return defaultTexture;
      }
   }

   public static boolean renderMooshroomOverlay(double offset) {
      if (overlayActive && overlayCounter < 3) {
         float tileX0 = overlayCounter / 3.0F;
         float tileX1 = ++overlayCounter / 3.0F;
         Tessellator tessellator = TessellatorAPI.getTessellator();
         TessellatorAPI.startDrawingQuads(tessellator);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, 0.5 + offset, -0.45 - offset, tileX0, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, -0.5 + offset, -0.45 - offset, tileX0, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, -0.5 + offset, 0.45 - offset, tileX1, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, 0.5 + offset, 0.45 - offset, tileX1, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, 0.5 + offset, 0.45 - offset, tileX0, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, -0.5 + offset, 0.45 - offset, tileX0, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, -0.5 + offset, -0.45 - offset, tileX1, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, 0.5 + offset, -0.45 - offset, tileX1, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, 0.5 + offset, 0.45 - offset, tileX0, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, -0.5 + offset, 0.45 - offset, tileX0, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, -0.5 + offset, -0.45 - offset, tileX1, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, 0.5 + offset, -0.45 - offset, tileX1, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, 0.5 + offset, -0.45 - offset, tileX0, 0.0);
         TessellatorAPI.addVertexWithUV(tessellator, 0.45 + offset, -0.5 + offset, -0.45 - offset, tileX0, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, -0.5 + offset, 0.45 - offset, tileX1, 1.0);
         TessellatorAPI.addVertexWithUV(tessellator, -0.45 + offset, 0.5 + offset, 0.45 - offset, tileX1, 0.0);
         TessellatorAPI.draw(tessellator);
      }

      return overlayActive;
   }

   public static void finishMooshroom() {
      overlayCounter = 0;
      overlayActive = false;
   }

   public static boolean renderSnowmanOverlay(EntityLiving entity) {
      if (!haveSnowman) {
         return false;
      } else {
         TexturePackAPI.bindTexture(MobRandomizer.randomTexture(entity, SNOWMAN_OVERLAY));
         Tessellator tessellator = TessellatorAPI.getTessellator();
         TessellatorAPI.startDrawingQuads(tessellator);
         double[] c = new double[4];
         getTileCoordinates(0, c);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, -0.5, -0.5, c[0], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, -0.5, 0.5, c[1], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, -0.5, 0.5, c[1], c[3]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, -0.5, -0.5, c[0], c[3]);
         getTileCoordinates(1, c);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, 0.5, -0.5, c[0], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, 0.5, 0.5, c[1], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, 0.5, 0.5, c[1], c[3]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, 0.5, -0.5, c[0], c[3]);
         getTileCoordinates(2, c);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, 0.5, 0.5, c[0], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, 0.5, -0.5, c[1], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, -0.5, -0.5, c[1], c[3]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, -0.5, 0.5, c[0], c[3]);
         getTileCoordinates(3, c);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, 0.5, -0.5, c[0], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, 0.5, -0.5, c[1], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, -0.5, -0.5, c[1], c[3]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, -0.5, -0.5, c[0], c[3]);
         getTileCoordinates(4, c);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, 0.5, -0.5, c[0], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, 0.5, 0.5, c[1], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, -0.5, 0.5, c[1], c[3]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, -0.5, -0.5, c[0], c[3]);
         getTileCoordinates(5, c);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, 0.5, 0.5, c[0], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, 0.5, 0.5, c[1], c[2]);
         TessellatorAPI.addVertexWithUV(tessellator, -0.5, -0.5, 0.5, c[1], c[3]);
         TessellatorAPI.addVertexWithUV(tessellator, 0.5, -0.5, 0.5, c[0], c[3]);
         TessellatorAPI.draw(tessellator);
         return true;
      }
   }

   private static void getTileCoordinates(int tileNum, double[] c) {
      c[0] = tileNum % 3 / 3.0;
      c[1] = c[0] + 0.3333333333333333;
      c[2] = tileNum / 3 / 2.0;
      c[3] = c[2] + 0.5;
   }
}
