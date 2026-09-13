package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class KilnBlock extends Block {
   private static final int MIN_FIRE_FACTOR_BASE_TICK_RATE = 40;
   private static final int MAX_FIRE_FACTOR_BASE_TICK_RATE = 160;
   @Environment(EnvType.CLIENT)
   private Icon[] cookIcons;

   public KilnBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(2.0F);
      this.b(10.0F);
      this.a(j);
      this.b(true);
      this.c("fcBlockKiln");
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      if (this.canBlockBeCooked(world, i, j + 1, k)) {
         this.scheduleUpdateBasedOnCookState(world, i, j, k);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      int iOldCookCounter = this.getCookCounter(world, i, j, k);
      int iNewCookCounter = 0;
      if (this.canBlockBeCooked(world, i, j + 1, k)) {
         if (this.checkKiLnIntegrity(world, i, j, k)) {
            if (iOldCookCounter >= 15) {
               this.cookBlock(world, i, j + 1, k);
            } else {
               iNewCookCounter = iOldCookCounter + 1;
               this.scheduleUpdateBasedOnCookState(world, i, j, k);
            }
         } else {
            this.scheduleUpdateBasedOnCookState(world, i, j, k);
         }
      }

      if (iOldCookCounter != iNewCookCounter) {
         this.setCookCounter(world, i, j, k, iNewCookCounter);
      }
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return Block.brick.blockID;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (world.getBlockId(i, j - 1, k) != BTWBlocks.stokedFire.blockID) {
         world.setBlockWithNotify(i, j, k, Block.brick.blockID);
      } else if (this.canBlockBeCooked(world, i, j + 1, k)) {
         if (!world.isUpdateScheduledForBlock(i, j, k, this.blockID) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
            this.scheduleUpdateBasedOnCookState(world, i, j, k);
         }
      } else if (this.getCookCounter(world, i, j, k) > 0) {
         this.setCookCounterNoNotify(world, i, j, k, 0);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!world.isUpdateScheduledForBlock(i, j, k, this.blockID) && this.canBlockBeCooked(world, i, j + 1, k)) {
         this.scheduleUpdateBasedOnCookState(world, i, j, k);
      }
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   public int getCookCounter(int iMetadata) {
      return iMetadata;
   }

   public int getCookCounter(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getCookCounter(blockAccess.getBlockMetadata(i, j, k));
   }

   public int setCookCounter(int iMetadata, int iCounter) {
      return iCounter;
   }

   public void setCookCounter(World world, int i, int j, int k, int iCounter) {
      int iMetadata = this.setCookCounter(world.getBlockMetadata(i, j, k), iCounter);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public void setCookCounterNoNotify(World world, int i, int j, int k, int iCounter) {
      int iMetadata = this.setCookCounter(world.getBlockMetadata(i, j, k), iCounter);
      world.setBlockMetadataWithClient(i, j, k, iMetadata);
   }

   protected void scheduleUpdateBasedOnCookState(World world, int i, int j, int k) {
      int iTickRate = this.computeTickRateBasedOnFireFactor(world, i, j, k);
      iTickRate *= this.getBlockCookTimeMultiplier(world, i, j + 1, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, iTickRate);
   }

   private boolean canBlockBeCooked(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.getCanBeCookedByKiLn(blockAccess, i, j, k) : false;
   }

   private void cookBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      if (block != null && block.getCanBeCookedByKiLn(world, i, j, k)) {
         block.onCookedByKiLn(world, i, j, k);
      }
   }

   private boolean checkKiLnIntegrity(IBlockAccess blockAccess, int i, int j, int k) {
      int iBrickCount = 0;

      for (int iTempFacing = 1; iTempFacing <= 5; iTempFacing++) {
         BlockPos tempPos = new BlockPos(i, j + 1, k);
         tempPos.addFacingAsOffset(iTempFacing);
         int iTempBlockID = blockAccess.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (iTempBlockID == Block.brick.blockID || iTempBlockID == BTWBlocks.kiln.blockID) {
            if (++iBrickCount >= 3) {
               return true;
            }
         }
      }

      return false;
   }

   private int computeTickRateBasedOnFireFactor(IBlockAccess blockAccess, int i, int j, int k) {
      int iSecondaryFireFactor = 0;

      for (int iOffset = -1; iOffset <= 1; iOffset++) {
         for (int kOffset = -1; kOffset <= 1; kOffset++) {
            if ((iOffset != 0 || kOffset != 0) && blockAccess.getBlockId(i + iOffset, j - 1, k + kOffset) == BTWBlocks.stokedFire.blockID) {
               iSecondaryFireFactor++;
            }
         }
      }

      return 120 * (8 - iSecondaryFireFactor) / 8 + 40;
   }

   private int getBlockCookTimeMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.getCookTimeMultiplierInKiLn(blockAccess, i, j, k) : 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("brick");
      this.cookIcons = new Icon[7];

      for (int iTempIndex = 0; iTempIndex < 7; iTempIndex++) {
         this.cookIcons[iTempIndex] = register.registerIcon("fcOverlayCook_" + (iTempIndex + 1));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   public Icon getCookTextureForCurrentState(IBlockAccess blockAccess, int i, int j, int k) {
      int iTextureIndex = this.getCookCounter(blockAccess, i, j, k) / 2 - 1;
      return iTextureIndex >= 0 && iTextureIndex <= 6 ? this.cookIcons[iTextureIndex] : null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
      if (blockAbove != null && blockAbove.getCanBeCookedByKiLn(world, i, j + 1, k) && this.checkKiLnIntegrity(world, i, j, k)) {
         if (!blockAbove.renderAsNormalBlock()) {
            for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
               double xPos = i + rand.nextDouble();
               double yPos = j + 1.0 + rand.nextDouble() * 0.75;
               double zPos = k + rand.nextDouble();
               world.spawnParticle("fcwhitesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
            }
         } else {
            for (int iTempFacing = 2; iTempFacing < 6; iTempFacing++) {
               double xPos = i + 0.5;
               double yPos = j + 1.0 + rand.nextDouble() * 0.75;
               double zPos = k + 0.5;
               double dFacingOffset = 0.75;
               double dHorizontalOffset = -0.75 + rand.nextDouble() * 1.5;
               if (iTempFacing == 2) {
                  xPos += dHorizontalOffset;
                  zPos -= dFacingOffset;
               } else if (iTempFacing == 3) {
                  xPos += dHorizontalOffset;
                  zPos += dFacingOffset;
               } else if (iTempFacing == 4) {
                  xPos -= dFacingOffset;
                  zPos += dHorizontalOffset;
               } else if (iTempFacing == 5) {
                  xPos += dFacingOffset;
                  zPos += dHorizontalOffset;
               }

               world.spawnParticle("fcwhitesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
            }
         }
      }
   }
}
