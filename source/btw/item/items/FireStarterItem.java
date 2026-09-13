package btw.item.items;

import btw.world.util.BlockPos;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class FireStarterItem extends Item {
   private final float exhaustionPerUse;

   public FireStarterItem(int iItemID, int iMaxUses, float fExhaustionPerUse) {
      super(iItemID);
      this.maxStackSize = 1;
      this.e(iMaxUses);
      this.exhaustionPerUse = fExhaustionPerUse;
      this.a(CreativeTabs.tabTools);
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (player.canPlayerEdit(i, j, k, iFacing, stack)) {
         this.performUseEffects(player);
         if (!world.isRemote) {
            this.notifyNearbyAnimalsOfAttempt(player);
            if (this.checkChanceOfStart(stack, world.rand)) {
               this.attemptToLightBlock(stack, world, i, j, k, iFacing);
            }
         }

         player.addExhaustion(this.exhaustionPerUse * world.getDifficulty().getHungerIntensiveActionCostMultiplier());
         stack.damageItem(1, player);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean getCanItemStartFireOnUse(int iItemDamage) {
      return true;
   }

   protected abstract boolean checkChanceOfStart(ItemStack var1, Random var2);

   protected void performUseEffects(EntityPlayer player) {
   }

   protected boolean attemptToLightBlock(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      return targetBlock != null && targetBlock.getCanBeSetOnFireDirectlyByItem(world, i, j, k) && targetBlock.setOnFireDirectly(world, i, j, k);
   }

   protected boolean attemptToLightBlockRegardlessOfFlamability(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      BlockPos targetPos = new BlockPos(i, j, k);
      if (targetBlock == null || !targetBlock.getCanBeSetOnFireDirectlyByItem(world, targetPos.x, targetPos.y, targetPos.z)) {
         targetPos.addFacingAsOffset(iFacing);
         targetBlock = Block.blocksList[world.getBlockId(targetPos.x, targetPos.y, targetPos.z)];
         iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      }

      if (targetBlock != null && targetBlock.getCanBeSetOnFireDirectlyByItem(world, targetPos.x, targetPos.y, targetPos.z)) {
         if (targetBlock.setOnFireDirectly(world, targetPos.x, targetPos.y, targetPos.z)) {
            return true;
         }
      } else if (world.isAirBlock(targetPos.x, targetPos.y, targetPos.z)) {
         world.setBlockWithNotify(targetPos.x, targetPos.y, targetPos.z, Block.fire.blockID);
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
         return true;
      }

      return false;
   }

   public void notifyNearbyAnimalsOfAttempt(EntityPlayer player) {
      for (EntityAnimal tempAnimal : player.worldObj.getEntitiesWithinAABB(EntityAnimal.class, player.boundingBox.expand(6.0, 6.0, 6.0))) {
         if (!tempAnimal.isDead) {
            tempAnimal.onNearbyFireStartAttempt(player);
         }
      }
   }
}
