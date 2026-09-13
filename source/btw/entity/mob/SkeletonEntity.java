package btw.entity.mob;

import btw.entity.InfiniteArrowEntity;
import btw.entity.RottenArrowEntity;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.entity.mob.behavior.SkeletonArrowAttackBehavior;
import btw.item.BTWItems;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class SkeletonEntity extends EntitySkeleton {
   private SkeletonArrowAttackBehavior aiRangedAttack;
   private EntityAIAttackOnCollide aiMeleeAttack;
   private static final float ENCHANTMENT_PROBABILITY = 0.0125F;
   private static final float ARMOR_ENCHANTMENT_PROBABILITY = 0.05F;
   private static final float PICK_UP_LOOT_PROBABILITY = 0.15F;

   public SkeletonEntity(World world) {
      super(world);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.tasks.addTask(5, new SimpleWanderBehavior(this, this.moveSpeed));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.aiRangedAttack = new SkeletonArrowAttackBehavior(this, 0.25F, 60, 15.0F);
      this.aiMeleeAttack = new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.31F, false);
   }

   @Override
   public void preInitCreature() {
      if (this.worldObj.provider.dimensionId == -1 && this.aE().nextInt(5) > 0) {
         this.a(1);
      }
   }

   @Override
   public void initCreature() {
      if (this.o() == 1) {
         this.c(0, new ItemStack(Item.swordStone));
      } else {
         this.bH();
         this.func_82162_bC();
      }

      this.setCombatTask();
      this.h(this.rand.nextFloat() < 0.15F);
   }

   @Override
   public void spawnerInitCreature() {
      this.setCombatTask();
      this.h(this.rand.nextFloat() < 0.15F);
      this.func_82162_bC();
   }

   @Override
   public void onLivingUpdate() {
      this.checkForCatchFireInSun();
      if (this.worldObj.isRemote && this.o() == 1) {
         this.a(0.72F, 2.34F);
      }

      this.entityMobOnLivingUpdate();
   }

   @Override
   protected int getDropItemId() {
      return 0;
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (this.o() == 0 && this.worldObj.provider.dimensionId != -1 && this.bG() != null && this.bG().itemID == Item.bow.itemID) {
         int iNumArrows = this.rand.nextInt(3 + iLootingModifier);

         for (int iTempCount = 0; iTempCount < iNumArrows; iTempCount++) {
            this.b(BTWItems.rottenArrow.itemID, 1);
         }
      }

      int iNumBones = this.rand.nextInt(3 + iLootingModifier);

      for (int iTempCount = 0; iTempCount < iNumBones; iTempCount++) {
         this.b(Item.bone.itemID, 1);
      }
   }

   @Override
   protected void dropRareDrop(int iBonusDrop) {
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLiving target, float fDamageModifier) {
      EntityArrow arrow = null;
      if (this.worldObj.provider.dimensionId == -1) {
         arrow = (EntityArrow)EntityList.createEntityOfType(InfiniteArrowEntity.class, this.worldObj, this, target, 1.6F, 12.0F);
      } else {
         arrow = (EntityArrow)EntityList.createEntityOfType(RottenArrowEntity.class, this.worldObj, this, target, 1.6F, 12.0F);
      }

      int iPowerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.bG());
      if (iPowerLevel > 0) {
         arrow.setDamage(arrow.getDamage() + iPowerLevel * 0.5 + 0.5);
      }

      int iPunchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.bG());
      if (iPunchLevel > 0) {
         arrow.setKnockbackStrength(iPunchLevel);
      }

      int iFlameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.bG());
      if (iFlameLevel > 0 || this.o() == 1 || this.ae() && this.rand.nextFloat() < 0.3F) {
         arrow.d(100);
      }

      this.a("random.bow", 1.0F, 1.0F / (this.aE().nextFloat() * 0.4F + 0.8F));
      this.worldObj.spawnEntityInWorld(arrow);
   }

   @Override
   public void setCombatTask() {
      this.tasks.removeTask(this.aiMeleeAttack);
      this.tasks.removeTask(this.aiRangedAttack);
      ItemStack heldStack = this.bG();
      if (heldStack != null && heldStack.itemID == Item.bow.itemID) {
         this.tasks.addTask(4, this.aiRangedAttack);
      } else {
         this.tasks.addTask(4, this.aiMeleeAttack);
      }
   }

   @Override
   protected boolean isValidLightLevel() {
      return this.worldObj.provider.dimensionId == -1 ? true : super.i_();
   }

   @Override
   public void checkForScrollDrop() {
      if (this.o() == 0 && this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = null;
         if (this.worldObj.provider.dimensionId == -1) {
            itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.infinity.effectId);
         } else {
            itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.projectileProtection.effectId);
         }

         this.a(itemstack, 0.0F);
      }
   }

   @Override
   protected void dropHead() {
      if (this.o() == 1) {
         this.a(new ItemStack(Item.skull.itemID, 1, 1), 0.0F);
      } else {
         this.a(new ItemStack(Item.skull.itemID, 1, 0), 0.0F);
      }
   }

   @Override
   protected void func_82162_bC() {
      if (this.bG() != null && this.rand.nextFloat() < 0.0125F) {
         EnchantmentHelper.addRandomEnchantment(this.rand, this.bG(), 7 * this.rand.nextInt(6));
         this.equipmentDropChances[0] = 0.99F;
      }

      for (int var1 = 0; var1 < 4; var1++) {
         ItemStack var2 = this.q(var1);
         if (var2 != null && this.rand.nextFloat() < 0.05F) {
            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 7 * this.rand.nextInt(6));
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource damageSource, int iDamageAmount) {
      return damageSource != DamageSource.cactus ? super.a(damageSource, iDamageAmount) : false;
   }
}
