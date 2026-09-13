package btw.block.blocks;

import btw.world.util.BlockPos;
import java.util.ArrayList;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public abstract class GourdBlock extends FallingBlock {
   private static final double ARROW_SPEED_SQUARED_TO_EXPLODE = 1.1;
   @Environment(EnvType.CLIENT)
   protected Icon iconTop;

   protected GourdBlock(int iBlockID) {
      super(iBlockID, Material.pumpkin);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.b(true);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      super.updateTick(world, i, j, k, rand);
      if (world.getBlockId(i, j, k) == this.blockID) {
         this.validateConnectionState(world, i, j, k);
      }
   }

   @Override
   public int getMobilityFlag() {
      return 0;
   }

   @Override
   public void onArrowImpact(World world, int i, int j, int k, EntityArrow arrow) {
      if (!world.isRemote) {
         double dArrowSpeedSq = arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ;
         if (dArrowSpeedSq >= 1.1) {
            world.setBlockWithNotify(i, j, k, 0);
            this.explode(world, i + 0.5, j + 0.5, k + 0.5);
         } else {
            world.playAuxSFX(2251, i, j, k, 0);
         }
      }
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      world.playAuxSFX(this.auxFXIDOnExplode(), i, j, k, 0);
   }

   @Override
   public boolean onFinishedFalling(EntityFallingSand entity, float fFallDistance) {
      entity.metadata = 0;
      if (!entity.worldObj.isRemote) {
         int i = MathHelper.floor_double(entity.posX);
         int j = MathHelper.floor_double(entity.posY);
         int k = MathHelper.floor_double(entity.posZ);
         int iFallDistance = MathHelper.ceiling_float_int(entity.fallDistance - 5.0F);
         if (iFallDistance >= 0) {
            this.damageCollidingEntitiesOnFall(entity, fFallDistance);
            if (!Material.water.equals(entity.worldObj.getBlockMaterial(i, j, k)) && entity.rand.nextInt(10) < iFallDistance) {
               this.explode(entity.worldObj, i + 0.5, j + 0.5, k + 0.5);
               return false;
            }
         }

         entity.worldObj.playAuxSFX(2251, i, j, k, 0);
      }

      return true;
   }

   @Override
   public int adjustMetadataForPistonMove(int iMetadata) {
      int var2 = false;
      return 0;
   }

   @Override
   public boolean isBlockAttachedToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return iMetadata >= 2 && iFacing == iMetadata;
   }

   @Override
   public void attachToFacing(World world, int i, int j, int k, int iFacing) {
      if (iFacing >= 2 && iFacing <= 5) {
         world.setBlockMetadataWithClient(i, j, k, iFacing);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      super.onNeighborBlockChange(world, i, j, k, iBlockID);
      this.validateConnectionState(world, i, j, k);
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess access, int i, int j, int k, EntityAnimal animal) {
      return animal.canGrazeOnRoughVegetation();
   }

   protected abstract Item itemToDropOnExplode();

   protected abstract int itemCountToDropOnExplode();

   protected abstract int auxFXIDOnExplode();

   protected abstract DamageSource getFallDamageSource();

   private void explode(World world, double posX, double posY, double posZ) {
      Item itemToDrop = this.itemToDropOnExplode();
      if (itemToDrop != null) {
         for (int iTempCount = 0; iTempCount < this.itemCountToDropOnExplode(); iTempCount++) {
            ItemStack itemStack = new ItemStack(itemToDrop, 1, 0);
            EntityItem entityItem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, world, posX, posY + 0.5, posZ, itemStack);
            entityItem.motionX = (world.rand.nextDouble() - 0.5) * 0.5;
            entityItem.motionY = 0.2 + world.rand.nextDouble() * 0.3;
            entityItem.motionZ = (world.rand.nextDouble() - 0.5) * 0.5;
            entityItem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityItem);
         }
      }

      this.notifyNearbyAnimalsFinishedFalling(world, MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
      world.playAuxSFX(this.auxFXIDOnExplode(), MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ), 0);
   }

   private void damageCollidingEntitiesOnFall(EntityFallingSand entity, float fFallDistance) {
      int var2x = MathHelper.ceiling_float_int(fFallDistance - 1.0F);
      if (var2x > 0) {
         ArrayList collisionList = new ArrayList(entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox));
         DamageSource source = this.getFallDamageSource();

         for (Entity tempEntity : collisionList) {
            tempEntity.attackEntityFrom(source, 1);
         }
      }
   }

   protected void validateConnectionState(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iMetadata > 0) {
         BlockPos targetPos = new BlockPos(i, j, k);
         if (iMetadata >= 2 && iMetadata <= 5) {
            targetPos.addFacingAsOffset(iMetadata);
            int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            if (Block.blocksList[iTargetBlockID] == null
               || !(Block.blocksList[iTargetBlockID] instanceof StemBlock)
               || world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z) != 15) {
               world.setBlockMetadata(i, j, k, 0);
            }
         } else {
            world.setBlockMetadata(i, j, k, 0);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide != 1 && iSide != 0 ? this.blockIcon : this.iconTop;
   }
}
