package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.tileentity.ChestTileEntity;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockChest;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ChestBlock extends BlockChest {
   public ChestBlock(int iBlockID) {
      super(iBlockID, 0);
      this.setBlockMaterial(BTWBlocks.plankMaterial);
      this.c(1.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.WOOD_BASED_BLOCK);
      this.initBlockBounds(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
      this.a(g);
      this.c("chest");
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new ChestTileEntity();
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      if (blockAccess.getBlockId(i, j, k - 1) == this.blockID) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0, 0.9375, 0.875, 0.9375);
      } else if (blockAccess.getBlockId(i, j, k + 1) == this.blockID) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 1.0);
      } else if (blockAccess.getBlockId(i - 1, j, k) == this.blockID) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
      } else {
         return blockAccess.getBlockId(i + 1, j, k) == this.blockID
            ? AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 1.0, 0.875, 0.9375)
            : AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
      }
   }

   @Override
   protected boolean canSilkHarvest(int iMetadata) {
      return true;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 6, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i - 1, j, k) != this.blockID
         && blockAccess.getBlockId(i + 1, j, k) != this.blockID
         && blockAccess.getBlockId(i, j, k - 1) != this.blockID
         && blockAccess.getBlockId(i, j, k + 1) != this.blockID;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      return Block.rotateFacingAroundY(iMetadata, bReverse);
   }

   @Override
   public boolean canSupportFallingBlocks(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }
}
