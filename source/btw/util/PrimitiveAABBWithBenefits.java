package btw.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public class PrimitiveAABBWithBenefits extends AxisAlignedBB {
   private int assemblyID = -1;
   private boolean forceRenderWithColorMultiplier = false;

   protected PrimitiveAABBWithBenefits(double dXMin, double dYMin, double dZMin, double dXMax, double dYMax, double dZMax) {
      super(dXMin, dYMin, dZMin, dXMax, dYMax, dZMax);
   }

   public PrimitiveAABBWithBenefits(double dXMin, double dYMin, double dZMin, double dXMax, double dYMax, double dZMax, int iAssemblyID) {
      super(dXMin, dYMin, dZMin, dXMax, dYMax, dZMax);
      this.assemblyID = iAssemblyID;
   }

   public PrimitiveAABBWithBenefits makeTemporaryCopy() {
      PrimitiveAABBWithBenefits tempCopy = new PrimitiveAABBWithBenefits(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, this.assemblyID);
      tempCopy.forceRenderWithColorMultiplier = this.forceRenderWithColorMultiplier;
      return tempCopy;
   }

   @Override
   public int getAssemblyID() {
      return this.assemblyID;
   }

   public void setForceRenderWithColorMultiplier(boolean bForce) {
      this.forceRenderWithColorMultiplier = bForce;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlock(RenderBlocks renderBlocks, Block block, int i, int j, int k) {
      return !this.forceRenderWithColorMultiplier
         ? super.renderAsBlock(renderBlocks, block, i, j, k)
         : this.renderAsBlockWithColorMultiplier(renderBlocks, block, i, j, k);
   }
}
