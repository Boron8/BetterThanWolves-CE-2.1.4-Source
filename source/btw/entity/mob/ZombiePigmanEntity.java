package btw.entity.mob;

import btw.item.BTWItems;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ZombiePigmanEntity extends EntityPigZombie {
   public ZombiePigmanEntity(World world) {
      super(world);
   }

   @Override
   public boolean attackEntityFrom(DamageSource source, int iDamage) {
      boolean bReturnValue = this.entityMobAttackEntityFrom(source, iDamage);
      if (!this.aq()) {
         Entity attacker = source.getEntity();
         if (attacker instanceof EntityPlayer) {
            this.p(attacker);
            this.angerNearbyPigmen(attacker);
         } else if ((source.isExplosion() || attacker instanceof GhastEntity) && this.worldObj.getDifficulty().shouldGhastsAngerPigmen()) {
            EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 16.0);
            if (closestPlayer != null) {
               this.p(closestPlayer);
               this.angerNearbyPigmen(closestPlayer);
            }
         }
      }

      return bReturnValue;
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.fireProtection.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   protected void dropHead() {
   }

   @Override
   protected void addRandomArmor() {
      if (this.rand.nextFloat() < 0.05F) {
         this.c(0, new ItemStack(Item.swordGold));
         this.equipmentDropChances[0] = 0.99F;
      }
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iFortuneModifier) {
      int iFleshCount = this.rand.nextInt(2 + iFortuneModifier);

      for (int iTempCount = 0; iTempCount < iFleshCount; iTempCount++) {
         this.b(Item.rottenFlesh.itemID, 1);
      }

      if (this.rand.nextFloat() <= 0.1F || this.worldObj.provider.dimensionId == -1) {
         int iNuggetCount = this.rand.nextInt(2 + iFortuneModifier);

         for (int iTempCount = 0; iTempCount < iNuggetCount; iTempCount++) {
            this.b(Item.goldNugget.itemID, 1);
         }
      }
   }

   @Override
   protected void dropRareDrop(int iBonusDrop) {
   }

   public void becomeAngryWhenPigAttacked(Entity attackingEntity) {
      this.p(attackingEntity);
   }

   protected void angerNearbyPigmen(Entity target) {
      for (Entity tempEntity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0, 32.0, 32.0))) {
         if (tempEntity instanceof ZombiePigmanEntity) {
            ZombiePigmanEntity tempPigmen = (ZombiePigmanEntity)tempEntity;
            tempPigmen.p(target);
         }
      }
   }
}
