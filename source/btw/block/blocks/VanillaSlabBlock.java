package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import java.util.List;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockStep;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class VanillaSlabBlock extends BlockStep {
   private static final int STEP_TYPE_STONE = 0;
   private static final int STEP_TYPE_SANDSTONE = 1;
   private static final int STEP_TYPE_WOOD = 2;
   private static final int STEP_TYPE_COBBLE = 3;
   private static final int STEP_TYPE_BRICK = 4;
   private static final int STEP_TYPE_STONE_BRICK = 5;
   private static final int STEP_TYPE_NETHER_BRICK = 6;
   private static final int STEP_TYPE_NETHER_QUARTZ = 7;

   public VanillaSlabBlock(int iBlockID, boolean bIsDoubleSlab) {
      super(iBlockID, bIsDoubleSlab);
   }

   @Override
   public int idDropped(int metadata, Random random, int fortune) {
      if (metadata == 0) {
         return BTWBlocks.stoneSlab.blockID;
      } else if (metadata == 3) {
         return BTWBlocks.cobblestoneSlab.blockID;
      } else {
         return metadata == 5 ? BTWBlocks.stoneBrickSlab.blockID : super.idDropped(metadata, random, fortune);
      }
   }

   @Override
   public int damageDropped(int metadata) {
      return metadata != 0 && metadata != 3 && metadata != 5 ? super.a(metadata) : 0;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      int iType = this.getBlockType(blockAccess, i, j, k);
      if (iType == 1) {
         return 3;
      } else {
         return iType != 3 && iType != 4 && iType != 5 && iType != 6 ? super.getHarvestToolLevel(blockAccess, i, j, k) : 1000;
      }
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      int iType = this.getBlockType(iMetadata);
      if (iType == 1) {
         if (this.isDoubleSlab) {
            this.dropItemsIndividually(world, i, j, k, BTWItems.sandPile.itemID, 16, 0, fChanceOfDrop);
         } else {
            this.dropItemsIndividually(world, i, j, k, BTWItems.sandPile.itemID, 8, 0, fChanceOfDrop);
         }

         return true;
      } else if (iType == 3) {
         if (this.isDoubleSlab) {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseCobblestone.blockID, 1, 0, fChanceOfDrop);
         } else {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseCobblestoneSlab.blockID, 1, 0, fChanceOfDrop);
         }

         return true;
      } else if (iType == 4) {
         if (this.isDoubleSlab) {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseBrick.blockID, 1, 0, fChanceOfDrop);
         } else {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseBrickSlab.blockID, 1, 0, fChanceOfDrop);
         }

         return true;
      } else if (iType == 5) {
         if (this.isDoubleSlab) {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseStoneBrick.blockID, 1, 0, fChanceOfDrop);
         } else {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseStoneBrickSlab.blockID, 1, 0, fChanceOfDrop);
         }

         return true;
      } else if (iType == 6) {
         if (this.isDoubleSlab) {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseNetherBrick.blockID, 1, 0, fChanceOfDrop);
         } else {
            this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseNetherBrickSlab.blockID, 1, 0, fChanceOfDrop);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      if (!this.isDoubleSlab && iFacing < 2) {
         boolean bIsUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         return bIsUpsideDown == (iFacing == 1);
      } else {
         return true;
      }
   }

   @Override
   public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      return this.isDoubleSlab || bIsSlabUpsideDown == this.getIsUpsideDown(blockAccess, i, j, k);
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      int iType = this.getBlockType(blockAccess, i, j, k);
      return iType == 3 || iType == 4 || iType == 5 || iType == 6;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      int iType = this.getBlockType(world, i, j, k);
      if (iType == 2) {
         return false;
      } else {
         return iType == 6 ? true : super.canMobsSpawnOn(world, i, j, k);
      }
   }

   public int getBlockType(int iMetadata) {
      return iMetadata & 7;
   }

   public int getBlockType(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getBlockType(blockAccess.getBlockMetadata(i, j, k));
   }

   @Override
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      if (par1 != Block.stoneDoubleSlab.blockID) {
         par3List.add(new ItemStack(par1, 1, 1));
         par3List.add(new ItemStack(par1, 1, 4));
         par3List.add(new ItemStack(par1, 1, 6));
         par3List.add(new ItemStack(par1, 1, 7));
      }
   }
}
