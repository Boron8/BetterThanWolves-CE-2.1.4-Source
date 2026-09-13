package net.minecraft.src;

public class EntitySelectorArmoredMob implements IEntitySelector {
   private final ItemStack field_96567_c;

   public EntitySelectorArmoredMob(ItemStack var1) {
      this.field_96567_c = var1;
   }

   @Override
   public boolean isEntityApplicable(Entity var1) {
      if (!var1.isEntityAlive()) {
         return false;
      } else if (!(var1 instanceof EntityLiving)) {
         return false;
      } else {
         EntityLiving var2 = (EntityLiving)var1;
         return var2.getCurrentItemOrArmor(EntityLiving.getArmorPosition(this.field_96567_c)) != null
            ? false
            : var2.canPickUpLoot() || var2 instanceof EntityPlayer;
      }
   }
}
