package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AABBPool {
   private final int maxNumCleans;
   private final int numEntriesToRemove;
   private final List listAABB = new ArrayList();
   private int nextPoolIndex = 0;
   private int maxPoolIndex = 0;
   private int numCleans = 0;

   public AABBPool(int var1, int var2) {
      this.maxNumCleans = var1;
      this.numEntriesToRemove = var2;
   }

   public AxisAlignedBB getAABB(double var1, double var3, double var5, double var7, double var9, double var11) {
      AxisAlignedBB var13;
      if (this.nextPoolIndex >= this.listAABB.size()) {
         var13 = new AxisAlignedBB(var1, var3, var5, var7, var9, var11);
         this.listAABB.add(var13);
      } else {
         var13 = (AxisAlignedBB)this.listAABB.get(this.nextPoolIndex);
         var13.setBounds(var1, var3, var5, var7, var9, var11);
      }

      this.nextPoolIndex++;
      return var13;
   }

   public void cleanPool() {
      if (this.nextPoolIndex > this.maxPoolIndex) {
         this.maxPoolIndex = this.nextPoolIndex;
      }

      if (this.numCleans++ == this.maxNumCleans) {
         int var1 = Math.max(this.maxPoolIndex, this.listAABB.size() - this.numEntriesToRemove);

         while (this.listAABB.size() > var1) {
            this.listAABB.remove(var1);
         }

         this.maxPoolIndex = 0;
         this.numCleans = 0;
      }

      this.nextPoolIndex = 0;
   }

   public void clearPool() {
      this.nextPoolIndex = 0;
      this.listAABB.clear();
   }

   public int getlistAABBsize() {
      return this.listAABB.size();
   }

   public int getnextPoolIndex() {
      return this.nextPoolIndex;
   }
}
