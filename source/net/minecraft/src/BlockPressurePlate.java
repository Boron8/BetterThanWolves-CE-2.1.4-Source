package net.minecraft.src;

import java.util.List;

public class BlockPressurePlate extends BlockBasePressurePlate {
   private EnumMobType triggerMobType;

   protected BlockPressurePlate(int var1, String var2, Material var3, EnumMobType var4) {
      super(var1, var2, var3);
      this.triggerMobType = var4;
   }

   @Override
   protected int getMetaFromWeight(int var1) {
      return var1 > 0 ? 1 : 0;
   }

   @Override
   protected int getPowerSupply(int var1) {
      return var1 == 1 ? 15 : 0;
   }

   @Override
   protected int getPlateState(World var1, int var2, int var3, int var4) {
      List var5 = null;
      if (this.triggerMobType == EnumMobType.everything) {
         var5 = var1.getEntitiesWithinAABBExcludingEntity(null, this.a(var2, var3, var4));
      }

      if (this.triggerMobType == EnumMobType.mobs) {
         var5 = var1.getEntitiesWithinAABB(EntityLiving.class, this.a(var2, var3, var4));
      }

      if (this.triggerMobType == EnumMobType.players) {
         var5 = var1.getEntitiesWithinAABB(EntityPlayer.class, this.a(var2, var3, var4));
      }

      if (!var5.isEmpty()) {
         for (Entity var7 : var5) {
            if (!var7.doesEntityNotTriggerPressurePlate()) {
               return 15;
            }
         }
      }

      return 0;
   }
}
