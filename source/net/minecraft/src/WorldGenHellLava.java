package net.minecraft.src;

import java.util.Random;

public class WorldGenHellLava extends WorldGenerator {
   private int hellLavaID;
   private boolean field_94524_b = false;

   public WorldGenHellLava(int var1, boolean var2) {
      this.hellLavaID = var1;
      this.field_94524_b = var2;
   }

   @Override
   public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
      if (var1.getBlockId(var3, var4 + 1, var5) != Block.netherrack.blockID) {
         return false;
      } else if (var1.getBlockId(var3, var4, var5) != 0 && var1.getBlockId(var3, var4, var5) != Block.netherrack.blockID) {
         return false;
      } else {
         int var6 = 0;
         if (var1.getBlockId(var3 - 1, var4, var5) == Block.netherrack.blockID) {
            var6++;
         }

         if (var1.getBlockId(var3 + 1, var4, var5) == Block.netherrack.blockID) {
            var6++;
         }

         if (var1.getBlockId(var3, var4, var5 - 1) == Block.netherrack.blockID) {
            var6++;
         }

         if (var1.getBlockId(var3, var4, var5 + 1) == Block.netherrack.blockID) {
            var6++;
         }

         if (var1.getBlockId(var3, var4 - 1, var5) == Block.netherrack.blockID) {
            var6++;
         }

         int var7 = 0;
         if (var1.isAirBlock(var3 - 1, var4, var5)) {
            var7++;
         }

         if (var1.isAirBlock(var3 + 1, var4, var5)) {
            var7++;
         }

         if (var1.isAirBlock(var3, var4, var5 - 1)) {
            var7++;
         }

         if (var1.isAirBlock(var3, var4, var5 + 1)) {
            var7++;
         }

         if (var1.isAirBlock(var3, var4 - 1, var5)) {
            var7++;
         }

         if (!this.field_94524_b && var6 == 4 && var7 == 1 || var6 == 5) {
            var1.setBlock(var3, var4, var5, this.hellLavaID, 0, 2);
            var1.scheduledUpdatesAreImmediate = true;
            Block.blocksList[this.hellLavaID].updateTick(var1, var3, var4, var5, var2);
            var1.scheduledUpdatesAreImmediate = false;
         }

         return true;
      }
   }
}
