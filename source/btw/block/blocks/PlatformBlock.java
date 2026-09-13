package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.client.render.util.RenderUtils;
import btw.entity.mechanical.platform.BlockLiftedByPlatformEntity;
import btw.entity.mechanical.platform.MovingAnchorEntity;
import btw.entity.mechanical.platform.MovingPlatformEntity;
import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class PlatformBlock extends Block {
   private boolean[][][] platformAlreadyConsideredForEntityConversion;
   private boolean[][][] platformAlreadyConsideredForConnectedTest;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];

   public PlatformBlock(int iBlockID) {
      super(iBlockID, Material.wood);
      this.c(2.0F);
      this.setAxesEffectiveOn();
      this.setBuoyancy(1.0F);
      this.setFireProperties(Flammability.WICKER);
      this.a(g);
      this.c("fcBlockPlatform");
      this.platformAlreadyConsideredForEntityConversion = new boolean[5][5][5];
      this.platformAlreadyConsideredForConnectedTest = new boolean[5][5][5];
      this.resetPlatformConsideredForEntityConversionArray();
      this.resetPlatformConsideredForConnectedTestArray();
      this.a(CreativeTabs.tabTransport);
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return true;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing <= 1;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isNormalCube(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   private void convertToEntity(World world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity) {
      MovingPlatformEntity entityPlatform = (MovingPlatformEntity)EntityList.createEntityOfType(
         MovingPlatformEntity.class, world, i + 0.5F, j + 0.5F, k + 0.5F, associatedAnchorEntity
      );
      world.spawnEntityInWorld(entityPlatform);
      this.attemptToLiftBlockWithPlatform(world, i, j + 1, k);
      world.setBlockWithNotify(i, j, k, 0);
   }

   private void attemptToLiftBlockWithPlatform(World world, int i, int j, int k) {
      if (BlockLiftedByPlatformEntity.canBlockBeConvertedToEntity(world, i, j, k)) {
         EntityList.createEntityOfType(BlockLiftedByPlatformEntity.class, world, i, j, k);
      }
   }

   private int getDistToClosestConnectedAnchorPoint(World world, int i, int j, int k) {
      int iClosestDist = -1;

      for (int tempI = i - 2; tempI <= i + 2; tempI++) {
         for (int tempJ = j - 2; tempJ <= j + 2; tempJ++) {
            for (int tempK = k - 2; tempK <= k + 2; tempK++) {
               int iTempBlockID = world.getBlockId(tempI, tempJ, tempK);
               if (iTempBlockID == this.blockID) {
                  int iUpwardsBlockID = world.getBlockId(tempI, tempJ + 1, tempK);
                  if (iUpwardsBlockID == BTWBlocks.anchor.blockID
                     && ((AnchorBlock)BTWBlocks.anchor).getFacing(world, tempI, tempJ + 1, tempK) == 1
                     && this.isPlatformConnectedToAnchorPoint(world, i, j, k, tempI, tempJ, tempK)) {
                     int iTempDist = Math.abs(tempI - i) + Math.abs(tempJ - j) + Math.abs(tempK - k);
                     if (iClosestDist == -1 || iTempDist < iClosestDist) {
                        iClosestDist = iTempDist;
                     }
                  }
               }
            }
         }
      }

      return iClosestDist;
   }

   private boolean isPlatformConnectedToAnchorPoint(
      World world, int platformI, int platformJ, int platformK, int anchorPointI, int anchorPointJ, int anchorPointK
   ) {
      this.resetPlatformConsideredForConnectedTestArray();
      return platformI == anchorPointI && platformJ == anchorPointJ && platformK == anchorPointK
         ? true
         : this.propogateTestForConnected(
            world, anchorPointI, anchorPointJ, anchorPointK, anchorPointI, anchorPointJ, anchorPointK, platformI, platformJ, platformK
         );
   }

   private boolean propogateTestForConnected(World world, int i, int j, int k, int sourceI, int sourceJ, int sourceK, int targetI, int targetJ, int targetK) {
      int iDeltaI = i - sourceI;
      int iDeltaJ = j - sourceJ;
      int iDeltaK = k - sourceK;
      if (this.platformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2]) {
         return false;
      } else {
         this.platformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;

         for (int iFacing = 0; iFacing < 6; iFacing++) {
            BlockPos tempPos = new BlockPos(i, j, k);
            tempPos.addFacingAsOffset(iFacing);
            if (tempPos.x == targetI && tempPos.y == targetJ && tempPos.z == targetK) {
               return true;
            }

            int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
            if (iTempBlockID == this.blockID) {
               int tempDistI = Math.abs(sourceI - tempPos.x);
               int tempDistJ = Math.abs(sourceJ - tempPos.y);
               int tempDistK = Math.abs(sourceK - tempPos.z);
               if (tempDistI <= 2
                  && tempDistJ <= 2
                  && tempDistK <= 2
                  && this.propogateTestForConnected(world, tempPos.x, tempPos.y, tempPos.z, sourceI, sourceJ, sourceK, targetI, targetJ, targetK)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   void resetPlatformConsideredForEntityConversionArray() {
      for (int tempI = 0; tempI < 5; tempI++) {
         for (int tempJ = 0; tempJ < 5; tempJ++) {
            for (int tempK = 0; tempK < 5; tempK++) {
               this.platformAlreadyConsideredForEntityConversion[tempI][tempJ][tempK] = false;
            }
         }
      }
   }

   void resetPlatformConsideredForConnectedTestArray() {
      for (int tempI = 0; tempI < 5; tempI++) {
         for (int tempJ = 0; tempJ < 5; tempJ++) {
            for (int tempK = 0; tempK < 5; tempK++) {
               this.platformAlreadyConsideredForConnectedTest[tempI][tempJ][tempK] = false;
            }
         }
      }
   }

   public void covertToEntitiesFromThisPlatform(World world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity) {
      this.resetPlatformConsideredForEntityConversionArray();
      this.propagateCovertToEntity(world, i, j, k, associatedAnchorEntity, i, j, k);
   }

   private void propagateCovertToEntity(World world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity, int sourceI, int sourceJ, int sourceK) {
      int iDeltaI = i - sourceI;
      int iDeltaJ = j - sourceJ;
      int iDeltaK = k - sourceK;
      if (!this.platformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2]) {
         this.platformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
         int distToSource = Math.abs(iDeltaI) + Math.abs(iDeltaJ) + Math.abs(iDeltaK);
         int closestAnchorDist = this.getDistToClosestConnectedAnchorPoint(world, i, j, k);
         if (closestAnchorDist == -1 || distToSource <= closestAnchorDist) {
            this.convertToEntity(world, i, j, k, associatedAnchorEntity);

            for (int iFacing = 0; iFacing < 6; iFacing++) {
               BlockPos tempPos = new BlockPos(i, j, k);
               tempPos.addFacingAsOffset(iFacing);
               int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
               if (iTempBlockID == this.blockID) {
                  int tempDistI = Math.abs(sourceI - tempPos.x);
                  int tempDistJ = Math.abs(sourceJ - tempPos.y);
                  int tempDistK = Math.abs(sourceK - tempPos.z);
                  if (tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2) {
                     this.propagateCovertToEntity(world, tempPos.x, tempPos.y, tempPos.z, associatedAnchorEntity, sourceI, sourceJ, sourceK);
                  }
               }
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon topIcon = register.registerIcon("fcBlockPlatform_top");
      this.blockIcon = topIcon;
      this.iconBySideArray[0] = register.registerIcon("fcBlockPlatform_bottom");
      this.iconBySideArray[1] = topIcon;
      Icon sideIcon = register.registerIcon("fcBlockPlatform_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      if (blockAccess.getBlockId(i - 1, j, k) != this.blockID) {
         renderBlocks.setRenderBounds(1.0E-4F, 0.0625, 1.0E-4F, 0.0625, 0.9375, 0.9999F);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      if (blockAccess.getBlockId(i, j, k + 1) != this.blockID) {
         renderBlocks.setRenderBounds(0.0, 0.0625, 0.9375, 1.0, 0.9375, 1.0);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      if (blockAccess.getBlockId(i + 1, j, k) != this.blockID) {
         renderBlocks.setRenderBounds(0.9375, 0.0625, 1.0E-4F, 0.9999F, 0.9375, 0.9999F);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      if (blockAccess.getBlockId(i, j, k - 1) != this.blockID) {
         renderBlocks.setRenderBounds(0.0, 0.0625, 0.0, 1.0, 0.9375, 0.0625);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
      renderBlocks.renderStandardBlock(this, i, j, k);
      renderBlocks.setRenderBounds(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
      renderBlocks.renderStandardBlock(this, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      Icon sideTexture = this.iconBySideArray[2];
      renderBlocks.setRenderBounds(1.0E-5F, 1.0E-5F, 1.0E-5F, 0.0625, 0.99999F, 0.99999F);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, sideTexture);
      renderBlocks.setRenderBounds(0.0, 0.0, 0.9375, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, sideTexture);
      renderBlocks.setRenderBounds(0.9375, 1.0E-5F, 1.0E-5F, 0.99999F, 0.99999F, 0.99999F);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, sideTexture);
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, sideTexture);
      renderBlocks.setRenderBounds(1.0E-4F, 0.001F, 1.0E-4F, 0.9999F, 0.0625, 0.9999F);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, this.iconBySideArray[0]);
      renderBlocks.setRenderBounds(1.0E-4F, 0.9375, 1.0E-4F, 0.9999F, 0.999F, 0.9999F);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, this.iconBySideArray[1]);
   }
}
