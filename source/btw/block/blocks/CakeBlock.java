package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockCake;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class CakeBlock extends BlockCake {
   static final float BORDER_WIDTH = 0.0625F;
   static final float HEIGHT = 0.5F;

   public CakeBlock(int iBlockID) {
      super(iBlockID);
      this.setBuoyant();
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iEatState = this.getEatState(blockAccess, i, j, k);
      float fWidth = (1 + iEatState * 2) / 16.0F;
      return AxisAlignedBB.getAABBPool().getAABB(fWidth, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      this.eatCakeSliceLocal(world, i, j, k, player);
      return true;
   }

   @Override
   public void onBlockClicked(World world, int i, int j, int k, EntityPlayer player) {
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeigborBlockID) {
      if (!this.f(world, i, j, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      } else {
         boolean bOn = this.isRedstoneOn(world, i, j, k);
         boolean bReceivingRedstone = world.isBlockGettingPowered(i, j, k);
         if (bOn != bReceivingRedstone) {
            this.setRedstoneOn(world, i, j, k, bReceivingRedstone);
            if (bReceivingRedstone) {
               world.playAuxSFX(2225, i, j, k, 0);
            }
         }
      }
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.a(world, i, j, k);
      boolean bReceivingRedstone = world.isBlockGettingPowered(i, j, k);
      if (bReceivingRedstone) {
         this.setRedstoneOn(world, i, j, k, true);
         world.playAuxSFX(2225, i, j, k, 0);
      }
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return (iMetadata & -9) == 0 ? new ItemStack(Item.cake.itemID, 1, 0) : null;
   }

   private void eatCakeSliceLocal(World world, int i, int j, int k, EntityPlayer player) {
      if (player.canEat(true)) {
         player.getFoodStats().addStats(4, 4.0F);
         int iEatState = this.getEatState(world, i, j, k) + 1;
         if (iEatState >= 6) {
            world.setBlockWithNotify(i, j, k, 0);
         } else {
            this.setEatState(world, i, j, k, iEatState);
         }
      } else {
         player.onCantConsume();
      }
   }

   public boolean isRedstoneOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setRedstoneOn(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -9;
      if (bOn) {
         iMetaData |= 8;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   public int getEatState(IBlockAccess iBlockAccess, int i, int j, int k) {
      return iBlockAccess.getBlockMetadata(i, j, k) & 7;
   }

   public void setEatState(World world, int i, int j, int k, int state) {
      int iMetaData = world.getBlockMetadata(i, j, k) & 8;
      iMetaData |= state;
      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return super.getIcon(iSide, iMetadata & 7);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.isRedstoneOn(world, i, j, k)) {
         double d = i + 0.5 + (random.nextFloat() - 0.5) * 0.666;
         double d1 = j + 0.65;
         double d2 = k + 0.5 + (random.nextFloat() - 0.5) * 0.666;
         float f = 0.06666667F;
         float f1 = f * 0.6F + 0.4F;
         float f2 = f * f * 0.7F - 0.5F;
         float f3 = f * f * 0.6F - 0.7F;
         if (f2 < 0.0F) {
            f2 = 0.0F;
         }

         if (f3 < 0.0F) {
            f3 = 0.0F;
         }

         world.spawnParticle("reddust", d, d1, d2, f1, f2, f3);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide == 0 ? !blockAccess.isBlockOpaqueCube(i, j, k) : true;
   }
}
