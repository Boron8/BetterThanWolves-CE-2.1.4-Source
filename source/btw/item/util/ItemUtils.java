package btw.item.util;

import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class ItemUtils {
   public static void ejectStackWithRandomOffset(World world, int i, int j, int k, ItemStack stack) {
      float xOffset = world.rand.nextFloat() * 0.7F + 0.15F;
      float yOffset = world.rand.nextFloat() * 0.2F + 0.1F;
      float zOffset = world.rand.nextFloat() * 0.7F + 0.15F;
      ejectStackWithRandomVelocity(world, i + xOffset, j + yOffset, k + zOffset, stack);
   }

   public static void ejectSingleItemWithRandomOffset(World world, int i, int j, int k, int iShiftedItemIndex, int iDamage) {
      ItemStack itemStack = new ItemStack(iShiftedItemIndex, 1, iDamage);
      ejectStackWithRandomOffset(world, i, j, k, itemStack);
   }

   public static void ejectStackWithRandomVelocity(World world, double xPos, double yPos, double zPos, ItemStack stack) {
      EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, world, xPos, yPos, zPos, stack);
      float velocityFactor = 0.05F;
      entityitem.motionX = (float)world.rand.nextGaussian() * velocityFactor;
      entityitem.motionY = (float)world.rand.nextGaussian() * velocityFactor + 0.2F;
      entityitem.motionZ = (float)world.rand.nextGaussian() * velocityFactor;
      entityitem.delayBeforeCanPickup = 10;
      world.spawnEntityInWorld(entityitem);
   }

   public static void ejectSingleItemWithRandomVelocity(World world, float xPos, float yPos, float zPos, int iShiftedItemIndex, int iDamage) {
      ItemStack itemStack = new ItemStack(iShiftedItemIndex, 1, iDamage);
      ejectStackWithRandomVelocity(world, xPos, yPos, zPos, itemStack);
   }

   public static void dropStackAsIfBlockHarvested(World world, int i, int j, int k, ItemStack stack) {
      float f1 = 0.7F;
      double d = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5;
      double d1 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5;
      double d2 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5;
      EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, world, i + d, j + d1, k + d2, stack);
      entityitem.delayBeforeCanPickup = 10;
      world.spawnEntityInWorld(entityitem);
   }

   public static void dropSingleItemAsIfBlockHarvested(World world, int i, int j, int k, int iShiftedItemIndex, int iDamage) {
      ItemStack itemStack = new ItemStack(iShiftedItemIndex, 1, iDamage);
      dropStackAsIfBlockHarvested(world, i, j, k, itemStack);
   }

   public static void ejectStackAroundBlock(World world, int i, int j, int k, ItemStack stack) {
      int iTempFacing = world.rand.nextInt(6);
      BlockPos targetPos = new BlockPos(i, j, k);
      BlockPos tempPos = new BlockPos();

      for (int iTempFacingCount = 0; iTempFacingCount < 6; iTempFacingCount++) {
         tempPos.set(i, j, k);
         tempPos.addFacingAsOffset(iTempFacing);
         if (WorldUtils.isReplaceableBlock(world, tempPos.x, tempPos.y, tempPos.z)) {
            targetPos.set(tempPos);
            break;
         }

         if (++iTempFacing >= 6) {
            iTempFacing = 0;
         }
      }

      dropStackAsIfBlockHarvested(world, targetPos.x, targetPos.y, targetPos.z, stack);
   }

   public static void ejectStackFromBlockTowardsFacing(World world, int i, int j, int k, ItemStack stack, int iFacing) {
      Vec3 ejectPos = Vec3.createVectorHelper(world.rand.nextDouble() * 0.7 + 0.15, 1.2 + world.rand.nextDouble() * 0.1, world.rand.nextDouble() * 0.7 + 0.15);
      ejectPos.tiltAsBlockPosToFacingAlongJ(iFacing);
      EntityItem entity = (EntityItem)EntityList.createEntityOfType(
         EntityItem.class, world, i + ejectPos.xCoord, j + ejectPos.yCoord, k + ejectPos.zCoord, stack
      );
      if (iFacing < 2) {
         entity.motionX = world.rand.nextDouble() * 0.1 - 0.05;
         entity.motionZ = world.rand.nextDouble() * 0.1 - 0.05;
         if (iFacing == 0) {
            entity.motionY = 0.0;
         } else {
            entity.motionY = 0.2;
         }
      } else {
         Vec3 ejectVel = Vec3.createVectorHelper(world.rand.nextDouble() * 0.1 - 0.05, 0.2, world.rand.nextDouble() * -0.05 - 0.05);
         ejectVel.rotateAsVectorAroundJToFacing(iFacing);
         entity.motionX = ejectVel.xCoord;
         entity.motionY = ejectVel.yCoord;
         entity.motionZ = ejectVel.zCoord;
      }

      entity.delayBeforeCanPickup = 10;
      world.spawnEntityInWorld(entity);
   }

   public static void givePlayerStackOrEjectFromTowardsFacing(EntityPlayer player, ItemStack stack, int i, int j, int k, int iFacing) {
      if (player.inventory.addItemStackToInventory(stack)) {
         player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } else if (!player.worldObj.isRemote) {
         ejectStackFromBlockTowardsFacing(player.worldObj, i, j, k, stack, iFacing);
      }
   }

   public static void givePlayerStackOrEjectFavorEmptyHand(EntityPlayer player, ItemStack stack, int i, int j, int k) {
      if (player.addStackToCurrentHeldStackIfEmpty(stack)) {
         player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } else {
         givePlayerStackOrEject(player, stack, i, j, k);
      }
   }

   public static void givePlayerStackOrEject(EntityPlayer player, ItemStack stack, int i, int j, int k) {
      if (player.inventory.addItemStackToInventory(stack)) {
         player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } else if (!player.worldObj.isRemote) {
         ejectStackWithRandomOffset(player.worldObj, i, j, k, stack);
      }
   }

   public static void givePlayerStackOrEject(EntityPlayer player, ItemStack stack) {
      if (player.inventory.addItemStackToInventory(stack)) {
         player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } else if (!player.worldObj.isRemote) {
         ejectStackWithRandomVelocity(player.worldObj, player.posX, player.posY, player.posZ, stack);
      }
   }
}
