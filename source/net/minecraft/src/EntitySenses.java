package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class EntitySenses {
   EntityLiving entityObj;
   List seenEntities = new ArrayList();
   List unseenEntities = new ArrayList();

   public EntitySenses(EntityLiving var1) {
      this.entityObj = var1;
   }

   public void clearSensingCache() {
      this.seenEntities.clear();
      this.unseenEntities.clear();
   }

   public boolean canSee(Entity var1) {
      if (this.seenEntities.contains(var1)) {
         return true;
      } else if (this.unseenEntities.contains(var1)) {
         return false;
      } else {
         this.entityObj.worldObj.theProfiler.startSection("canSee");
         boolean var2 = this.entityObj.canEntityBeSeen(var1);
         this.entityObj.worldObj.theProfiler.endSection();
         if (var2) {
            this.seenEntities.add(var1);
         } else {
            this.unseenEntities.add(var1);
         }

         return var2;
      }
   }
}
