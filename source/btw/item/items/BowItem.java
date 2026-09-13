package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.entity.InfiniteArrowEntity;
import btw.entity.RottenArrowEntity;
import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBow;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BowItem extends ItemBow {
   public BowItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setfurnaceburntime(3 * FurnaceBurnTime.SHAFT.burnTime);
      this.setIncineratedInCrucible();
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(3);
      this.b("bow");
   }

   @Override
   public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int iTicksInUseRemaining) {
      ItemStack arrowStack = this.getFirstArrowStackInHotbar(player);
      boolean bInfiniteArrows = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStack) > 0;
      if (arrowStack != null || bInfiniteArrows) {
         float fPullStrength = this.getCurrentPullStrength(player, itemStack, iTicksInUseRemaining);
         if (fPullStrength < 0.1F) {
            return;
         }

         EntityArrow entityArrow;
         if (arrowStack != null) {
            entityArrow = this.createArrowEntityForItem(world, player, arrowStack.itemID, fPullStrength);
            player.inventory.consumeInventoryItem(arrowStack.itemID);
         } else {
            entityArrow = (EntityArrow)EntityList.createEntityOfType(
               InfiniteArrowEntity.class, world, player, fPullStrength * this.getPullStrengthToArrowVelocityMultiplier()
            );
         }

         if (entityArrow != null) {
            if (fPullStrength == 1.0F) {
               entityArrow.setIsCritical(true);
            }

            this.applyBowEnchantmentsToArrow(itemStack, entityArrow);
            if (!world.isRemote) {
               world.spawnEntityInWorld(entityArrow);
            }
         }

         itemStack.damageItem(1, player);
         this.playerBowSound(world, player, fPullStrength);
         if (itemStack.stackSize == 0) {
            player.inventory.mainInventory[player.inventory.currentItem] = null;
         }
      }
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (player.capabilities.isCreativeMode
         || this.getFirstArrowStackInHotbar(player) != null
         || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0) {
         player.setItemInUse(stack, this.c_(stack));
      }

      return stack;
   }

   @Override
   public int getItemEnchantability() {
      return 0;
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment enchantment) {
      return enchantment.type == EnumEnchantmentType.bow ? true : super.isEnchantmentApplicable(enchantment);
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      if (outputStack.itemID == Item.stick.itemID && player.timesCraftedThisTick == 0) {
         player.playSound("random.bow", 0.25F, player.worldObj.rand.nextFloat() * 0.25F + 1.5F);
      }
   }

   protected float getCurrentPullStrength(EntityPlayer player, ItemStack itemStack, int iTicksInUseRemaining) {
      int iTicksInUse = this.c_(itemStack) - iTicksInUseRemaining;
      float fPullStrength = iTicksInUse / 20.0F;
      fPullStrength = (fPullStrength * fPullStrength + fPullStrength * 2.0F) / 3.0F;
      if (fPullStrength > 1.0F) {
         fPullStrength = 1.0F;
      }

      return fPullStrength * player.getBowPullStrengthModifier();
   }

   public ItemStack getFirstArrowStackInHotbar(EntityPlayer player) {
      for (int iTempSlot = 0; iTempSlot < 9; iTempSlot++) {
         ItemStack tempStack = player.inventory.getStackInSlot(iTempSlot);
         if (tempStack != null && this.canItemBeFiredAsArrow(tempStack.itemID)) {
            return tempStack;
         }
      }

      return null;
   }

   public boolean canItemBeFiredAsArrow(int iItemID) {
      return iItemID == Item.arrow.itemID || iItemID == BTWItems.rottenArrow.itemID;
   }

   public float getPullStrengthToArrowVelocityMultiplier() {
      return 2.0F;
   }

   protected EntityArrow createArrowEntityForItem(World world, EntityPlayer player, int iItemID, float fPullStrength) {
      EntityArrow entityArrow = null;
      if (iItemID == BTWItems.rottenArrow.itemID) {
         entityArrow = (EntityArrow)EntityList.createEntityOfType(
            RottenArrowEntity.class, world, player, fPullStrength * 0.55F * this.getPullStrengthToArrowVelocityMultiplier()
         );
      } else if (iItemID == Item.arrow.itemID) {
         entityArrow = (EntityArrow)EntityList.createEntityOfType(
            EntityArrow.class, world, player, fPullStrength * this.getPullStrengthToArrowVelocityMultiplier()
         );
      }

      return entityArrow;
   }

   protected void applyBowEnchantmentsToArrow(ItemStack bowStack, EntityArrow entityArrow) {
      int iPowerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bowStack);
      if (iPowerLevel > 0) {
         entityArrow.setDamage(entityArrow.getDamage() + iPowerLevel * 0.5 + 0.5);
      }

      int iPunchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, bowStack);
      if (iPunchLevel > 0) {
         entityArrow.setKnockbackStrength(iPunchLevel);
      }

      if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bowStack) > 0) {
         entityArrow.d(100);
      }
   }

   protected void playerBowSound(World world, EntityPlayer player, float fPullStrength) {
      world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (e.nextFloat() * 0.4F + 1.2F) + fPullStrength * 0.5F);
   }

   public Icon getDrawIcon(int itemInUseDuration) {
      if (itemInUseDuration >= 18) {
         return this.c(2);
      } else if (itemInUseDuration > 12) {
         return this.c(1);
      } else {
         return itemInUseDuration > 0 ? this.c(0) : this.itemIcon;
      }
   }

   @Override
   public Icon getAnimationIcon(EntityPlayer player) {
      ItemStack itemInUse = player.getItemInUse();
      if (itemInUse != null && itemInUse.itemID == this.itemID) {
         int timeInUse = itemInUse.getMaxItemUseDuration() - player.getItemInUseCount();
         return this.getDrawIcon(timeInUse);
      } else {
         return this.itemIcon;
      }
   }
}
