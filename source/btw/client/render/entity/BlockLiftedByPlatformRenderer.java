package btw.client.render.entity;

import btw.entity.mechanical.platform.BlockLiftedByPlatformEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRailBase;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.Entity;
import net.minecraft.src.Icon;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BlockLiftedByPlatformRenderer extends Render {
   public BlockLiftedByPlatformRenderer() {
      this.shadowSize = 0.0F;
   }

   @Override
   public void doRender(Entity entity, double x, double y, double z, float fYaw, float renderPartialTicks) {
      y += 0.075F;
      World world = entity.worldObj;
      BlockLiftedByPlatformEntity liftedBlockEntity = (BlockLiftedByPlatformEntity)entity;
      int iBlockID = liftedBlockEntity.getBlockID();
      int iMetaData = liftedBlockEntity.getBlockMetadata();
      Block block = Block.blocksList[iBlockID];
      if (block != null) {
         if (block instanceof BlockRailBase) {
            BlockRailBase blockRail = (BlockRailBase)block;
            this.renderBlockMinecartTrack(world, liftedBlockEntity, blockRail, iMetaData, x, y, z);
         } else if (block instanceof BlockRedstoneWire) {
            BlockRedstoneWire blockRedstoneWire = (BlockRedstoneWire)block;
            this.renderBlockRedstoneWire(world, liftedBlockEntity, blockRedstoneWire, iMetaData, x, y, z);
         }
      }
   }

   public boolean renderBlockRedstoneWire(
      World world, BlockLiftedByPlatformEntity liftedBlockEntity, BlockRedstoneWire blockRedstoneWire, int iMetaData, double x, double y, double z
   ) {
      GL11.glPushMatrix();
      GL11.glDisable(2896);
      this.a("/terrain.png");
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      int iLoc = MathHelper.floor_double(liftedBlockEntity.posX);
      int jLoc = MathHelper.floor_double(liftedBlockEntity.posY);
      int kLoc = MathHelper.floor_double(liftedBlockEntity.posZ);
      float fBrightness = blockRedstoneWire.f(world, iLoc, jLoc, kLoc);
      float fBrightness2 = blockRedstoneWire.f(world, iLoc, jLoc - 1, kLoc);
      if (fBrightness2 > fBrightness) {
         fBrightness = fBrightness2;
      }

      tessellator.setColorOpaque_F(0.4F * fBrightness, 0.0F, 0.0F);
      Icon i1 = blockRedstoneWire.a(0, iMetaData);
      double d = i1.getMinU();
      double d1 = i1.getMaxU();
      double d2 = i1.getMinV();
      double d3 = i1.getMaxV();
      float fVerticalOffset = 0.015625F;
      float f2 = (float)(x + 0.5);
      float f3 = (float)(x + 0.5);
      float f4 = (float)(x - 0.5);
      float f5 = (float)(x - 0.5);
      float f6 = (float)(z - 0.5);
      float f7 = (float)(z + 0.5);
      float f8 = (float)(z + 0.5);
      float f9 = (float)(z - 0.5);
      float f10 = (float)(y - 0.5) + fVerticalOffset;
      float f11 = (float)(y - 0.5) + fVerticalOffset;
      float f12 = (float)(y - 0.5) + fVerticalOffset;
      float f13 = (float)(y - 0.5) + fVerticalOffset;
      tessellator.addVertexWithUV(f2, f10, f6, d1, d2);
      tessellator.addVertexWithUV(f3, f11, f7, d1, d3);
      tessellator.addVertexWithUV(f4, f12, f8, d, d3);
      tessellator.addVertexWithUV(f5, f13, f9, d, d2);
      tessellator.addVertexWithUV(f5, f13, f9, d, d2);
      tessellator.addVertexWithUV(f4, f12, f8, d, d3);
      tessellator.addVertexWithUV(f3, f11, f7, d1, d3);
      tessellator.addVertexWithUV(f2, f10, f6, d1, d2);
      tessellator.draw();
      GL11.glEnable(2896);
      GL11.glPopMatrix();
      return true;
   }

   public boolean renderBlockMinecartTrack(
      World world, BlockLiftedByPlatformEntity liftedBlockEntity, BlockRailBase blockrail, int iMetaData, double x, double y, double z
   ) {
      GL11.glPushMatrix();
      GL11.glDisable(2896);
      this.a("/terrain.png");
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      int iLoc = MathHelper.floor_double(liftedBlockEntity.posX);
      int jLoc = MathHelper.floor_double(liftedBlockEntity.posY);
      int kLoc = MathHelper.floor_double(liftedBlockEntity.posZ);
      float fBrightness = blockrail.f(world, iLoc, jLoc, kLoc);
      float fBrightness2 = blockrail.f(world, iLoc, jLoc - 1, kLoc);
      if (fBrightness2 > fBrightness) {
         fBrightness = fBrightness2;
      }

      tessellator.setColorOpaque_F(fBrightness, fBrightness, fBrightness);
      Icon i1 = blockrail.a(0, iMetaData);
      if (blockrail.isPowered()) {
         iMetaData &= 7;
      }

      double d = i1.getMinU();
      double d1 = i1.getMaxU();
      double d2 = i1.getMinV();
      double d3 = i1.getMaxV();
      float fVerticalOffset = 0.0625F;
      float f2 = (float)(x + 0.5);
      float f3 = (float)(x + 0.5);
      float f4 = (float)(x - 0.5);
      float f5 = (float)(x - 0.5);
      float f6 = (float)(z - 0.5);
      float f7 = (float)(z + 0.5);
      float f8 = (float)(z + 0.5);
      float f9 = (float)(z - 0.5);
      float f10 = (float)(y - 0.5) + fVerticalOffset;
      float f11 = (float)(y - 0.5) + fVerticalOffset;
      float f12 = (float)(y - 0.5) + fVerticalOffset;
      float f13 = (float)(y - 0.5) + fVerticalOffset;
      if (iMetaData == 1 || iMetaData == 2 || iMetaData == 3 || iMetaData == 7) {
         f2 = f5 = (float)(x + 0.5);
         f3 = f4 = (float)(x - 0.5);
         f6 = f7 = (float)(z + 0.5);
         f8 = f9 = (float)(z - 0.5);
      } else if (iMetaData == 8) {
         f2 = f3 = (float)(x - 0.5);
         f4 = f5 = (float)(x + 0.5);
         f6 = f9 = (float)(z + 0.5);
         f7 = f8 = (float)(z - 0.5);
      } else if (iMetaData == 9) {
         f2 = f5 = (float)(x - 0.5);
         f3 = f4 = (float)(x + 0.5);
         f6 = f7 = (float)(z - 0.5);
         f8 = f9 = (float)(z + 0.5);
      }

      if (iMetaData == 2 || iMetaData == 4) {
         f10++;
         f13++;
      } else if (iMetaData == 3 || iMetaData == 5) {
         f11++;
         f12++;
      }

      tessellator.addVertexWithUV(f2, f10, f6, d1, d2);
      tessellator.addVertexWithUV(f3, f11, f7, d1, d3);
      tessellator.addVertexWithUV(f4, f12, f8, d, d3);
      tessellator.addVertexWithUV(f5, f13, f9, d, d2);
      tessellator.addVertexWithUV(f5, f13, f9, d, d2);
      tessellator.addVertexWithUV(f4, f12, f8, d, d3);
      tessellator.addVertexWithUV(f3, f11, f7, d1, d3);
      tessellator.addVertexWithUV(f2, f10, f6, d1, d2);
      tessellator.draw();
      GL11.glEnable(2896);
      GL11.glPopMatrix();
      return true;
   }
}
