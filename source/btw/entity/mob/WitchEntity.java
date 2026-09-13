package btw.entity.mob;

import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityWitch;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WitchEntity extends EntityWitch {
   private static final int[] itemDrops = new int[]{BTWItems.witchWart.itemID, Item.glassBottle.itemID, Item.stick.itemID};

   public WitchEntity(World world) {
      super(world);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.tasks.addTask(2, new SimpleWanderBehavior(this, this.moveSpeed));
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      int iNumDrops = this.rand.nextInt(3) + 1;

      for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
         int iItemID = itemDrops[this.rand.nextInt(itemDrops.length)];
         int iNumItems = this.rand.nextInt(3);
         if (iLootingModifier > 0) {
            iNumItems += this.rand.nextInt(iLootingModifier + 1);
         }

         while (iNumItems > 0) {
            this.b(iItemID, 1);
            iNumItems--;
         }
      }
   }

   @Override
   public boolean getCanSpawnHere() {
      return (int)this.posY >= this.worldObj.provider.getAverageGroundLevel() - 5 ? super.bv() : false;
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.aquaAffinity.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   public void playLivingSound() {
      String var1 = "mob.ghast.affectionate scream";
      if (var1 != null) {
         this.a(var1, this.ba() * 0.25F, 0.5F + this.rand.nextFloat() * 0.25F);
      }
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLiving target, float fDamageModifier) {
      if (!this.isConsumingPotion()) {
         super.attackEntityWithRangedAttack(target, fDamageModifier);
         this.worldObj.playSoundAtEntity(this, "mob.wither.shoot", 0.5F, 0.4F / (this.rand.nextFloat() * 0.4F + 0.8F));
      }
   }

   @Override
   public void setAggressive(boolean bIsConsumingPotion) {
      super.setAggressive(bIsConsumingPotion);
      if (bIsConsumingPotion) {
         this.worldObj.playSoundAtEntity(this, "mob.wither.shoot", 0.5F, 0.4F / (this.rand.nextFloat() * 0.4F + 0.8F));
      }
   }

   public boolean isConsumingPotion() {
      return this.m();
   }
}
