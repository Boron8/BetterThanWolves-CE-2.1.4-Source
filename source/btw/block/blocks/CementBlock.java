package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.tileentity.CementTileEntity;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class CementBlock extends BlockContainer {
   private final int cementTexture;
   private final int cementPartiallyDryTexture;
   public static final int MAX_CEMENT_SPREAD_DIST = 16;
   public static final int CEMENT_TICKS_TO_DRY = 12;
   public static final int CEMENT_TICKS_TO_PARTIALLY_DRY = 8;
   boolean[] tempSpreadToSideFlags;
   int[] tempClosestDownslopeToSideDist;
   @Environment(EnvType.CLIENT)
   private Icon iconDrying;

   public CementBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.cementMaterial);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.c(100.0F);
      this.k(255);
      this.c("fcBlockCement");
      this.a(Block.soundSandFootstep);
      this.cementTexture = 15;
      this.cementPartiallyDryTexture = 16;
      this.tempSpreadToSideFlags = new boolean[4];
      this.tempClosestDownslopeToSideDist = new int[4];
      Block.useNeighborBrightness[iBlockID] = true;
      this.b(true);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new CementTileEntity();
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean canCollideCheck(int i, boolean flag) {
      return flag && i == 0;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return world.getBlockId(i, j + 1, k) != this.blockID
         ? AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1, j + 0.5F, k + 1)
         : AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1, j + 1, k + 1);
   }

   @Override
   public int idDropped(int i, Random random, int iFortuneModifier) {
      return 0;
   }

   @Override
   public int quantityDropped(Random random) {
      return 0;
   }

   @Override
   public int tickRate(World world) {
      return 20;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      if (world.getBlockId(i, j, k) == this.blockID) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      int cementDist = this.getCementSpreadDist(world, i, j, k);
      if (cementDist > 0) {
         int newCementDist = -100;
         newCementDist = this.checkForLesserSpreadDist(world, i - 1, j, k, newCementDist);
         newCementDist = this.checkForLesserSpreadDist(world, i + 1, j, k, newCementDist);
         newCementDist = this.checkForLesserSpreadDist(world, i, j, k - 1, newCementDist);
         newCementDist = this.checkForLesserSpreadDist(world, i, j, k + 1, newCementDist);
         newCementDist = this.checkForLesserSpreadDist(world, i, j + 1, k, newCementDist);
         if (newCementDist < 0) {
            newCementDist = -1;
         } else {
            newCementDist++;
         }

         int cementDistUp = this.getCementSpreadDist(world, i, j + 1, k);
         if (cementDistUp >= 0 && cementDistUp < newCementDist) {
            newCementDist = cementDistUp + 1;
         }

         if (newCementDist > 0 && newCementDist < cementDist) {
            cementDist = newCementDist;
            this.setCementSpreadDist(world, i, j, k, newCementDist);
            this.setCementDryTime(world, i, j, k, 0);
         }
      }

      int iDryTime = this.getCementDryTime(world, i, j, k);
      iDryTime++;
      int minDryTime = this.checkNeighboursCloserToSourceForMinDryTime(world, i, j, k);
      if (minDryTime <= iDryTime) {
         if (minDryTime <= 0) {
            iDryTime = 0;
         } else {
            iDryTime = minDryTime - 1;
         }
      }

      if (iDryTime > 12) {
         world.setBlockWithNotify(i, j, k, Block.stone.blockID);
      } else {
         this.setCementDryTime(world, i, j, k, iDryTime);
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         if (this.isBlockOpenToSpread(world, i, j - 1, k)) {
            int targetCementDist = cementDist + 1;
            if (targetCementDist <= 16) {
               world.setBlockWithNotify(i, j - 1, k, this.blockID);
               this.setCementSpreadDist(world, i, j - 1, k, targetCementDist);
            }
         } else if (cementDist >= 0 && (cementDist == 0 || this.blockBlocksFlow(world, i, j - 1, k))) {
            boolean[] spreadToSideFlags = this.checkSideBlocksForPotentialSpread(world, i, j, k);
            int spreadDist = cementDist + 1;
            if (spreadDist <= 16) {
               if (spreadToSideFlags[0]) {
                  this.attemptToSpreadToBlock(world, i - 1, j, k, spreadDist);
               }

               if (spreadToSideFlags[1]) {
                  this.attemptToSpreadToBlock(world, i + 1, j, k, spreadDist);
               }

               if (spreadToSideFlags[2]) {
                  this.attemptToSpreadToBlock(world, i, j, k - 1, spreadDist);
               }

               if (spreadToSideFlags[3]) {
                  this.attemptToSpreadToBlock(world, i, j, k + 1, spreadDist);
               }
            }
         }
      }
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   private boolean isPowered(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetaData = blockAccess.getBlockMetadata(i, j, k);
      return (iMetaData & 1) > 0;
   }

   public float getRenderHeight(IBlockAccess blockAccess, int i, int j, int k) {
      float fRenderHeight = 1.0F;
      if (blockAccess.getBlockMaterial(i, j, k) == this.blockMaterial) {
         int dist = this.getCementSpreadDist(blockAccess, i, j, k);
         fRenderHeight = (dist + 1) / 18.0F;
         if (this.isCementPartiallyDry(blockAccess, i, j, k)) {
            fRenderHeight *= 0.1F;
         } else {
            fRenderHeight *= 0.5F;
         }
      }

      return fRenderHeight;
   }

   public int getCementSpreadDist(IBlockAccess blockAccess, int i, int j, int k) {
      if (blockAccess.getBlockMaterial(i, j, k) != this.blockMaterial) {
         return -1;
      } else {
         CementTileEntity tileEntity = (CementTileEntity)blockAccess.getBlockTileEntity(i, j, k);
         return tileEntity.getSpreadDist();
      }
   }

   public void setCementSpreadDist(World world, int i, int j, int k, int iSpreadDist) {
      CementTileEntity tileEntity = (CementTileEntity)world.getBlockTileEntity(i, j, k);
      tileEntity.setSpreadDist(iSpreadDist);
      world.notifyBlocksOfNeighborChange(i, j, k, this.blockID);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
   }

   public boolean isCementSourceBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getCementSpreadDist(blockAccess, i, j, k) == 0;
   }

   public int getCementDryTime(IBlockAccess blockAccess, int i, int j, int k) {
      if (blockAccess.getBlockMaterial(i, j, k) == this.blockMaterial) {
         TileEntity tileEntity = blockAccess.getBlockTileEntity(i, j, k);
         if (tileEntity instanceof CementTileEntity) {
            CementTileEntity cementTileEntity = (CementTileEntity)blockAccess.getBlockTileEntity(i, j, k);
            return cementTileEntity.getDryTime();
         }
      }

      return 0;
   }

   public void setCementDryTime(World world, int i, int j, int k, int iDryTime) {
      CementTileEntity tileEntity = (CementTileEntity)world.getBlockTileEntity(i, j, k);
      tileEntity.setDryTime(iDryTime);
      world.notifyBlocksOfNeighborChange(i, j, k, this.blockID);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
   }

   public boolean isCementPartiallyDry(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getCementDryTime(blockAccess, i, j, k) >= 8;
   }

   private int checkNeighboursCloserToSourceForMinDryTime(World world, int i, int j, int k) {
      int minDryTime = 1000;
      int distToSource = this.getCementSpreadDist(world, i, j, k);
      minDryTime = this.getLesserDryTimeIfCloserToSource(world, i, j + 1, k, distToSource, minDryTime);
      minDryTime = this.getLesserDryTimeIfCloserToSource(world, i + 1, j, k, distToSource, minDryTime);
      minDryTime = this.getLesserDryTimeIfCloserToSource(world, i - 1, j, k, distToSource, minDryTime);
      minDryTime = this.getLesserDryTimeIfCloserToSource(world, i, j, k + 1, distToSource, minDryTime);
      return this.getLesserDryTimeIfCloserToSource(world, i, j, k - 1, distToSource, minDryTime);
   }

   private int getLesserDryTimeIfCloserToSource(World world, int i, int j, int k, int distToSource, int dryTime) {
      Material material = world.getBlockMaterial(i, j, k);
      if (material == this.blockMaterial) {
         int targetDistToSource = this.getCementSpreadDist(world, i, j, k);
         if (targetDistToSource < distToSource) {
            int targetDryTime = this.getCementDryTime(world, i, j, k);
            if (targetDryTime < dryTime) {
               return targetDryTime;
            }
         }
      }

      return dryTime;
   }

   private void attemptToSpreadToBlock(World world, int i, int j, int k, int newSpreadDist) {
      if (this.isBlockOpenToSpread(world, i, j, k)) {
         int i1 = world.getBlockId(i, j, k);
         if (i1 > 0) {
            Block.blocksList[i1].dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         }

         world.setBlockWithNotify(i, j, k, this.blockID);
         this.setCementSpreadDist(world, i, j, k, newSpreadDist);
      }
   }

   private boolean[] checkSideBlocksForPotentialSpread(World world, int i, int j, int k) {
      for (int sideNum = 0; sideNum < 4; sideNum++) {
         int iSide = i;
         int kSide = k;
         switch (sideNum) {
            case 0:
               iSide = i - 1;
               break;
            case 1:
               iSide = i + 1;
               break;
            case 2:
               kSide = k - 1;
               break;
            default:
               kSide = k + 1;
         }

         if (!this.blockBlocksFlow(world, iSide, j, kSide)
            && (world.getBlockMaterial(iSide, j, kSide) != this.blockMaterial || !this.isCementSourceBlock(world, iSide, j, kSide))) {
            this.tempSpreadToSideFlags[sideNum] = true;
         } else {
            this.tempSpreadToSideFlags[sideNum] = false;
         }
      }

      return this.tempSpreadToSideFlags;
   }

   private boolean[] checkSideBlocksForDownslope(World world, int i, int j, int k) {
      for (int sideNum = 0; sideNum < 4; sideNum++) {
         this.tempClosestDownslopeToSideDist[sideNum] = 1000;
         int iSide = i;
         int kSide = k;
         if (sideNum == 0) {
            iSide = i - 1;
         } else if (sideNum == 1) {
            iSide = i + 1;
         } else if (sideNum == 2) {
            kSide = k - 1;
         } else if (sideNum == 3) {
            kSide = k + 1;
         }

         if (!this.blockBlocksFlow(world, iSide, j, kSide)
            && (world.getBlockMaterial(iSide, j, kSide) != this.blockMaterial || !this.isCementSourceBlock(world, iSide, j, kSide))) {
            if (!this.blockBlocksFlow(world, iSide, j - 1, kSide)) {
               this.tempClosestDownslopeToSideDist[sideNum] = 0;
            } else {
               this.tempClosestDownslopeToSideDist[sideNum] = this.recursivelyCheckSideBlocksForDownSlope(world, iSide, j, kSide, 1, sideNum);
            }
         }
      }

      int minDistanceToDownslope = this.tempClosestDownslopeToSideDist[0];

      for (int tempSide = 1; tempSide < 4; tempSide++) {
         if (this.tempClosestDownslopeToSideDist[tempSide] < minDistanceToDownslope) {
            minDistanceToDownslope = this.tempClosestDownslopeToSideDist[tempSide];
         }
      }

      for (int tempSidex = 0; tempSidex < 4; tempSidex++) {
         this.tempSpreadToSideFlags[tempSidex] = this.tempClosestDownslopeToSideDist[tempSidex] == minDistanceToDownslope;
      }

      return this.tempSpreadToSideFlags;
   }

   private int recursivelyCheckSideBlocksForDownSlope(World world, int i, int j, int k, int recursionCount, int originSideNum) {
      int closestDownslope = 1000;

      for (int tempSideNum = 0; tempSideNum < 4; tempSideNum++) {
         if ((tempSideNum != 0 || originSideNum != 1)
            && (tempSideNum != 1 || originSideNum != 0)
            && (tempSideNum != 2 || originSideNum != 3)
            && (tempSideNum != 3 || originSideNum != 2)) {
            int tempi = i;
            int tempk = k;
            if (tempSideNum == 0) {
               tempi = i - 1;
            } else if (tempSideNum == 1) {
               tempi = i + 1;
            } else if (tempSideNum == 2) {
               tempk = k - 1;
            } else if (tempSideNum == 3) {
               tempk = k + 1;
            }

            if (!this.blockBlocksFlow(world, tempi, j, tempk) && this.getCementSpreadDist(world, tempi, j, tempk) != 0) {
               if (!this.blockBlocksFlow(world, tempi, j - 1, tempk)) {
                  return recursionCount;
               }

               if (recursionCount < 4) {
                  int tempSideClosestDownslope = this.recursivelyCheckSideBlocksForDownSlope(world, tempi, j, tempk, recursionCount + 1, tempSideNum);
                  if (tempSideClosestDownslope < closestDownslope) {
                     closestDownslope = tempSideClosestDownslope;
                  }
               }
            }
         }
      }

      return closestDownslope;
   }

   private boolean blockBlocksFlow(World world, int i, int j, int k) {
      Block block = r[world.getBlockId(i, j, k)];
      return block != null && block.blockMaterial != this.blockMaterial && block.getPreventsFluidFlow(world, i, j, k, this);
   }

   protected int checkForLesserSpreadDist(World world, int i, int j, int k, int sourceSpreadDist) {
      int targetSpreadDist = this.getCementSpreadDist(world, i, j, k);
      if (targetSpreadDist < 0) {
         return sourceSpreadDist;
      } else {
         return sourceSpreadDist >= 0 && targetSpreadDist >= sourceSpreadDist ? sourceSpreadDist : targetSpreadDist;
      }
   }

   private boolean isBlockOpenToSpread(World world, int i, int j, int k) {
      if (j < 0) {
         return false;
      } else {
         Material material = world.getBlockMaterial(i, j, k);
         return material == this.blockMaterial ? false : !this.blockBlocksFlow(world, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return this.isCementPartiallyDry(blockAccess, i, j, k) ? this.iconDrying : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockCement");
      this.iconDrying = register.registerIcon("fcBlockCement_drying");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return this.renderCement(renderBlocks, renderBlocks.blockAccess, i, j, k, this);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      Material material = blockAccess.getBlockMaterial(iNeighborI, iNeighborJ, iNeighborK);
      if (material == this.blockMaterial) {
         return false;
      } else if (material == Material.ice) {
         return false;
      } else {
         return iSide == 1 ? true : super.a(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getBlockBrightness(IBlockAccess iblockaccess, int i, int j, int k) {
      float f = iblockaccess.getLightBrightness(i, j, k);
      float f1 = iblockaccess.getLightBrightness(i, j + 1, k);
      return f <= f1 ? f1 : f;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (!this.isCementPartiallyDry(world, i, j, k) && random.nextInt(250) == 0) {
         world.playSound(i + 0.5F, j + 0.5F, k + 0.5F, "mob.ghast.moan", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      }
   }

   @Environment(EnvType.CLIENT)
   public boolean renderCement(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block) {
      boolean bRenderAllFaces = false;
      Tessellator tessellator = Tessellator.instance;
      tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      boolean bTopFaceFlag = block.shouldSideBeRendered(iblockaccess, i, j + 1, k, 1);
      boolean bBottomFaceFlag = block.shouldSideBeRendered(iblockaccess, i, j - 1, k, 0);
      boolean[] bSideFaceFlags = new boolean[]{
         block.shouldSideBeRendered(iblockaccess, i, j, k - 1, 2),
         block.shouldSideBeRendered(iblockaccess, i, j, k + 1, 3),
         block.shouldSideBeRendered(iblockaccess, i - 1, j, k, 4),
         block.shouldSideBeRendered(iblockaccess, i + 1, j, k, 5)
      };
      if (!bTopFaceFlag && !bBottomFaceFlag && !bSideFaceFlags[0] && !bSideFaceFlags[1] && !bSideFaceFlags[2] && !bSideFaceFlags[3]) {
         return false;
      } else {
         boolean hasRendered = false;
         float f = 0.5F;
         float f1 = 1.0F;
         float f2 = 0.8F;
         float f3 = 0.6F;
         float cornerHeight1 = this.renderCementGetCornerHeightFromNeighbours(iblockaccess, i, j, k);
         float cornerHeight2 = this.renderCementGetCornerHeightFromNeighbours(iblockaccess, i, j, k + 1);
         float cornerHeight3 = this.renderCementGetCornerHeightFromNeighbours(iblockaccess, i + 1, j, k + 1);
         float cornerHeight4 = this.renderCementGetCornerHeightFromNeighbours(iblockaccess, i + 1, j, k);
         if (bRenderAllFaces || bTopFaceFlag) {
            hasRendered = true;
            Icon i1 = block.getBlockTexture(iblockaccess, i, j, k, 1);
            double x1 = i1.getMinU();
            double x2 = i1.getMaxU();
            double y1 = i1.getMinV();
            double y2 = i1.getMaxV();
            tessellator.setBrightness(block.getMixedBrightnessForBlock(iblockaccess, i, j + 1, k));
            tessellator.addVertexWithUV(i + 0, j + cornerHeight1, k + 0, x1, y1);
            tessellator.addVertexWithUV(i + 0, j + cornerHeight2, k + 1, x1, y2);
            tessellator.addVertexWithUV(i + 1, j + cornerHeight3, k + 1, x2, y2);
            tessellator.addVertexWithUV(i + 1, j + cornerHeight4, k + 0, x2, y1);
         }

         if (bRenderAllFaces || bBottomFaceFlag) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(iblockaccess, i, j - 1, k));
            renderblocks.renderFaceYNeg(block, i, j, k, block.getBlockTexture(iblockaccess, i, j, k, 0));
            hasRendered = true;
         }

         for (int iSide = 0; iSide < 4; iSide++) {
            int k1 = i;
            int k2 = k;
            if (iSide == 0) {
               k2 = k - 1;
            } else if (iSide == 1) {
               k2 = k + 1;
            } else if (iSide == 2) {
               k1 = i - 1;
            } else if (iSide == 3) {
               k1 = i + 1;
            }

            if (bRenderAllFaces || bSideFaceFlags[iSide]) {
               float f10;
               float f14;
               float f17;
               float f18;
               float f12;
               float f16;
               if (iSide == 0) {
                  f10 = cornerHeight1;
                  f12 = cornerHeight4;
                  f14 = i;
                  f17 = i + 1;
                  f16 = k;
                  f18 = k;
               } else if (iSide == 1) {
                  f10 = cornerHeight3;
                  f12 = cornerHeight2;
                  f14 = i + 1;
                  f17 = i;
                  f16 = k + 1;
                  f18 = k + 1;
               } else if (iSide == 2) {
                  f10 = cornerHeight2;
                  f12 = cornerHeight1;
                  f14 = i;
                  f17 = i;
                  f16 = k + 1;
                  f18 = k;
               } else {
                  f10 = cornerHeight4;
                  f12 = cornerHeight3;
                  f14 = i + 1;
                  f17 = i + 1;
                  f16 = k;
                  f18 = k + 1;
               }

               hasRendered = true;
               Icon l2 = block.getBlockTexture(iblockaccess, i, j, k, iSide + 2);
               double d4 = l2.getMinU();
               double d5 = l2.getMaxU();
               double d6 = l2.getInterpolatedV((1.0 - f10) * 16.0);
               double d7 = l2.getInterpolatedV((1.0F - f12) * 16.0);
               double d8 = l2.getMaxV();
               tessellator.setBrightness(block.getMixedBrightnessForBlock(iblockaccess, k1, j, k2));
               tessellator.addVertexWithUV(f14, j + f10, f16, d4, d6);
               tessellator.addVertexWithUV(f17, j + f12, f18, d5, d7);
               tessellator.addVertexWithUV(f17, j + 0, f18, d5, d8);
               tessellator.addVertexWithUV(f14, j + 0, f16, d4, d8);
            }
         }

         return hasRendered;
      }
   }

   @Environment(EnvType.CLIENT)
   public float renderCementGetCornerHeightFromNeighbours(IBlockAccess iblockaccess, int i, int j, int k) {
      int numFactorsCount = 0;
      float f = 0.0F;

      for (int tempSide = 0; tempSide < 4; tempSide++) {
         int tempi = i - (tempSide & 1);
         int tempk = k - (tempSide >> 1 & 1);
         if (iblockaccess.getBlockMaterial(tempi, j + 1, tempk) == BTWBlocks.cementMaterial) {
            return 1.0F;
         }

         Material material1 = iblockaccess.getBlockMaterial(tempi, j, tempk);
         if (material1 == BTWBlocks.cementMaterial) {
            if (iblockaccess.isBlockOpaqueCube(tempi, j + 1, tempk)) {
               return 1.0F;
            }

            if (this.isCementSourceBlock(iblockaccess, tempi, j, tempk)) {
               f += this.getRenderHeight(iblockaccess, tempi, j, tempk) * 10.0F;
               numFactorsCount += 10;
            }

            f += this.getRenderHeight(iblockaccess, tempi, j, tempk);
            numFactorsCount++;
         } else if (!material1.isSolid()) {
            f += 0.6F;
            numFactorsCount++;
         }
      }

      return numFactorsCount > 0 ? 1.0F - f / numFactorsCount : 1.0F;
   }
}
