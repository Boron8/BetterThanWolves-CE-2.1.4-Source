package btw.block.model;

import btw.block.util.RayTraceUtils;
import btw.util.PrimitiveGeometric;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class BlockModel extends PrimitiveGeometric {
   private List<PrimitiveGeometric> primitiveList;
   private int assemblyID = -1;
   private int activePrimitiveID = -1;

   public BlockModel() {
      this.primitiveList = new LinkedList<>();
      this.initModel();
   }

   public BlockModel(int iAssemblyID) {
      this();
      this.setAssemblyID(iAssemblyID);
   }

   public BlockModel makeTemporaryCopy() {
      BlockModel newModel = new BlockModel(this.getAssemblyID());
      this.makeTemporaryCopyOfPrimitiveList(newModel);
      return newModel;
   }

   @Override
   public void rotateAroundYToFacing(int iFacing) {
      if (iFacing > 2) {
         Iterator<PrimitiveGeometric> tempIterator = this.primitiveList.iterator();

         while (tempIterator.hasNext()) {
            tempIterator.next().rotateAroundYToFacing(iFacing);
         }
      }
   }

   @Override
   public void tiltToFacingAlongY(int iFacing) {
      if (iFacing != 1) {
         Iterator<PrimitiveGeometric> tempIterator = this.primitiveList.iterator();

         while (tempIterator.hasNext()) {
            tempIterator.next().tiltToFacingAlongY(iFacing);
         }
      }
   }

   @Override
   public void translate(double dDeltaX, double dDeltaY, double dDeltaZ) {
      Iterator<PrimitiveGeometric> tempIterator = this.primitiveList.iterator();

      while (tempIterator.hasNext()) {
         tempIterator.next().translate(dDeltaX, dDeltaY, dDeltaZ);
      }
   }

   @Override
   public void addToRayTrace(RayTraceUtils rayTrace) {
      Iterator<PrimitiveGeometric> tempIterator = this.primitiveList.iterator();

      while (tempIterator.hasNext()) {
         tempIterator.next().addToRayTrace(rayTrace);
      }
   }

   @Override
   public void addIntersectingBoxesToCollisionList(World world, int i, int j, int k, AxisAlignedBB boxToIntersect, List collisionList) {
      Iterator<PrimitiveGeometric> tempIterator = this.primitiveList.iterator();

      while (tempIterator.hasNext()) {
         tempIterator.next().addIntersectingBoxesToCollisionList(world, i, j, k, boxToIntersect, collisionList);
      }
   }

   @Override
   public int getAssemblyID() {
      return this.assemblyID;
   }

   protected void initModel() {
   }

   public void makeTemporaryCopyOfPrimitiveList(BlockModel modelTo) {
      Iterator<PrimitiveGeometric> tempIterator = this.primitiveList.iterator();

      while (tempIterator.hasNext()) {
         modelTo.primitiveList.add(tempIterator.next().makeTemporaryCopy());
      }
   }

   public void addPrimitive(PrimitiveGeometric primitive) {
      this.primitiveList.add(primitive);
   }

   public void addBox(double dMinX, double dMinY, double dMinZ, double dMaxX, double dMaxY, double dMaxZ) {
      this.primitiveList.add(new AxisAlignedBB(dMinX, dMinY, dMinZ, dMaxX, dMaxY, dMaxZ));
   }

   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      this.addToRayTrace(rayTrace);
      return rayTrace.getFirstIntersection();
   }

   public int getActivePrimitiveID() {
      return this.activePrimitiveID;
   }

   public void setAssemblyID(int iAssemblyID) {
      this.assemblyID = iAssemblyID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlock(RenderBlocks renderBlocks, Block block, int i, int j, int k) {
      for (PrimitiveGeometric tempPrimitive : this.primitiveList) {
         this.activePrimitiveID = tempPrimitive.getAssemblyID();
         tempPrimitive.renderAsBlock(renderBlocks, block, i, j, k);
      }

      this.activePrimitiveID = -1;
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlockWithColorMultiplier(RenderBlocks renderBlocks, Block block, int i, int j, int k, float fRed, float fGreen, float fBlue) {
      for (PrimitiveGeometric tempPrimitive : this.primitiveList) {
         this.activePrimitiveID = tempPrimitive.getAssemblyID();
         tempPrimitive.renderAsBlockWithColorMultiplier(renderBlocks, block, i, j, k, fRed, fGreen, fBlue);
      }

      this.activePrimitiveID = -1;
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlockWithTexture(RenderBlocks renderBlocks, Block block, int i, int j, int k, Icon icon) {
      for (PrimitiveGeometric tempPrimitive : this.primitiveList) {
         this.activePrimitiveID = tempPrimitive.getAssemblyID();
         tempPrimitive.renderAsBlockWithTexture(renderBlocks, block, i, j, k, icon);
      }

      this.activePrimitiveID = -1;
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlockFullBrightWithTexture(RenderBlocks renderBlocks, Block block, int i, int j, int k, Icon icon) {
      for (PrimitiveGeometric tempPrimitive : this.primitiveList) {
         this.activePrimitiveID = tempPrimitive.getAssemblyID();
         tempPrimitive.renderAsBlockFullBrightWithTexture(renderBlocks, block, i, j, k, icon);
      }

      this.activePrimitiveID = -1;
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderAsItemBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      for (PrimitiveGeometric tempPrimitive : this.primitiveList) {
         this.activePrimitiveID = tempPrimitive.getAssemblyID();
         tempPrimitive.renderAsItemBlock(renderBlocks, block, iItemDamage);
      }

      this.activePrimitiveID = -1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderAsFallingBlock(RenderBlocks renderBlocks, Block block, int i, int j, int k, int iMetadata) {
      for (PrimitiveGeometric tempPrimitive : this.primitiveList) {
         this.activePrimitiveID = tempPrimitive.getAssemblyID();
         tempPrimitive.renderAsFallingBlock(renderBlocks, block, i, j, k, iMetadata);
      }

      this.activePrimitiveID = -1;
   }
}
