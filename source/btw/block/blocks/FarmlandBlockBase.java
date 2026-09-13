package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class FarmlandBlockBase extends Block {
   @Environment(EnvType.CLIENT)
   protected Icon iconTopWet;
   @Environment(EnvType.CLIENT)
   protected Icon iconTopDry;

   protected FarmlandBlockBase(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.9375, 1.0);
      this.k(255);
      w[iBlockID] = true;
      this.b(true);
      this.a(h);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (this.hasIrrigatingBlocks(world, i, j, k) || world.isRainingAtPos(i, j + 1, k)) {
         this.setFullyHydrated(world, i, j, k);
      } else if (this.isHydrated(world, i, j, k)) {
         this.dryIncrementally(world, i, j, k);
      } else {
         this.checkForSoilReversion(world, i, j, k);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(i, j, k);
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
   public void onFallenUpon(World world, int i, int j, int k, Entity entity, float fFallDist) {
      if (!world.isRemote && world.rand.nextFloat() < fFallDist - 0.75F) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
      }
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 0.8F;
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseDirt.blockID;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.dirtPile.itemID, 6, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public void onVegetationAboveGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      if (animal.getDisruptsEarthOnGraze()) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
         this.notifyNeighborsBlockDisrupted(world, i, j, k);
      }
   }

   @Override
   public boolean canDomesticatedCropsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canWildVegetationGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBlockHydratedForPlantGrowthOn(World world, int i, int j, int k) {
      return this.isHydrated(world, i, j, k);
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && !this.isFertilized(world, i, j, k) && entity.isEntityAlive() && entity instanceof EntityItem) {
         EntityItem entityItem = (EntityItem)entity;
         ItemStack stack = entityItem.getEntityItem();
         if (stack.itemID == Item.dyePowder.itemID && stack.getItemDamage() == 15) {
            stack.stackSize--;
            if (stack.stackSize <= 0) {
               entityItem.w();
            }

            this.setFertilized(world, i, j, k);
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         }
      }
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -0.0625F;
   }

   @Override
   public boolean attemptToApplyFertilizerTo(World world, int i, int j, int k) {
      if (!this.isFertilized(world, i, j, k)) {
         this.setFertilized(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean getCanBlightSpreadToBlock(World world, int i, int j, int k, int iBlightLevel) {
      return iBlightLevel >= 1;
   }

   protected boolean isHydrated(World world, int i, int j, int k) {
      return this.isHydrated(world.getBlockMetadata(i, j, k));
   }

   protected abstract boolean isHydrated(int var1);

   protected void setFullyHydrated(World world, int i, int j, int k) {
      int iMetadata = this.setFullyHydrated(world.getBlockMetadata(i, j, k));
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected abstract int setFullyHydrated(int var1);

   protected abstract void dryIncrementally(World var1, int var2, int var3, int var4);

   protected abstract boolean isFertilized(IBlockAccess var1, int var2, int var3, int var4);

   protected void setFertilized(World world, int i, int j, int k) {
   }

   protected int getHorizontalHydrationRange(World world, int i, int j, int k) {
      return 4;
   }

   protected boolean hasIrrigatingBlocks(World world, int i, int j, int k) {
      int iHorizontalRange = this.getHorizontalHydrationRange(world, i, j, k);

      for (int iTempI = i - iHorizontalRange; iTempI <= i + iHorizontalRange; iTempI++) {
         for (int iTempJ = j; iTempJ <= j + 1; iTempJ++) {
            for (int iTempK = k - iHorizontalRange; iTempK <= k + iHorizontalRange; iTempK++) {
               if (world.getBlockMaterial(iTempI, iTempJ, iTempK) == Material.water) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   protected boolean doesBlockAbovePreventSoilReversion(World world, int i, int j, int k) {
      int iBlockAboveID = world.getBlockId(i, j + 1, k);
      return iBlockAboveID == Block.crops.blockID
         || iBlockAboveID == Block.melonStem.blockID
         || iBlockAboveID == Block.pumpkinStem.blockID
         || iBlockAboveID == Block.potato.blockID
         || iBlockAboveID == Block.carrot.blockID;
   }

   protected void checkForSoilReversion(World world, int i, int j, int k) {
      if (!this.doesBlockAbovePreventSoilReversion(world, i, j, k)) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("dirt");
      this.iconTopWet = register.registerIcon("farmland_wet");
      this.iconTopDry = register.registerIcon("farmland_dry");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide == 1) {
         return this.isHydrated(iMetadata) ? this.iconTopWet : this.iconTopDry;
      } else {
         return this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return BTWBlocks.farmland.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide == 1 || super.shouldSideBeRendered(blockAccess, i, j, k, iSide);
   }
}
