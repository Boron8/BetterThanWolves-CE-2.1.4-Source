package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class Vec3Pool {
   private final int truncateArrayResetThreshold;
   private final int minimumSize;
   private final List vec3Cache = new ArrayList();
   private int nextFreeSpace = 0;
   private int maximumSizeSinceLastTruncation = 0;
   private int resetCount = 0;

   public Vec3Pool(int var1, int var2) {
      this.truncateArrayResetThreshold = var1;
      this.minimumSize = var2;
   }

   public Vec3 getVecFromPool(double var1, double var3, double var5) {
      if (this.func_82589_e()) {
         return new Vec3(this, var1, var3, var5);
      } else {
         Vec3 var7;
         if (this.nextFreeSpace >= this.vec3Cache.size()) {
            var7 = new Vec3(this, var1, var3, var5);
            this.vec3Cache.add(var7);
         } else {
            var7 = (Vec3)this.vec3Cache.get(this.nextFreeSpace);
            var7.setComponents(var1, var3, var5);
         }

         this.nextFreeSpace++;
         return var7;
      }
   }

   public void clear() {
      if (!this.func_82589_e()) {
         if (this.nextFreeSpace > this.maximumSizeSinceLastTruncation) {
            this.maximumSizeSinceLastTruncation = this.nextFreeSpace;
         }

         if (this.resetCount++ == this.truncateArrayResetThreshold) {
            int var1 = Math.max(this.maximumSizeSinceLastTruncation, this.vec3Cache.size() - this.minimumSize);

            while (this.vec3Cache.size() > var1) {
               this.vec3Cache.remove(var1);
            }

            this.maximumSizeSinceLastTruncation = 0;
            this.resetCount = 0;
         }

         this.nextFreeSpace = 0;
      }
   }

   public void clearAndFreeCache() {
      if (!this.func_82589_e()) {
         this.nextFreeSpace = 0;
         this.vec3Cache.clear();
      }
   }

   public int getPoolSize() {
      return this.vec3Cache.size();
   }

   public int func_82590_d() {
      return this.nextFreeSpace;
   }

   private boolean func_82589_e() {
      return this.minimumSize < 0 || this.truncateArrayResetThreshold < 0;
   }
}
