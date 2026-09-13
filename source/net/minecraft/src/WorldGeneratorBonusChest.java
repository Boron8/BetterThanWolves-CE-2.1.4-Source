package net.minecraft.src;

import java.util.Random;

public class WorldGeneratorBonusChest extends WorldGenerator {
   private final WeightedRandomChestContent[] theBonusChestGenerator;
   private final int itemsToGenerateInBonusChest;

   public WorldGeneratorBonusChest(WeightedRandomChestContent[] var1, int var2) {
      this.theBonusChestGenerator = var1;
      this.itemsToGenerateInBonusChest = var2;
   }

   @Override
   public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
      int var6 = 0;

      while (((var6 = var1.getBlockId(var3, var4, var5)) == 0 || var6 == Block.leaves.blockID) && var4 > 1) {
         var4--;
      }

      if (var4 < 1) {
         return false;
      } else {
         var4++;

         for (int var7 = 0; var7 < 4; var7++) {
            int var8 = var3 + var2.nextInt(4) - var2.nextInt(4);
            int var9 = var4 + var2.nextInt(3) - var2.nextInt(3);
            int var10 = var5 + var2.nextInt(4) - var2.nextInt(4);
            if (var1.isAirBlock(var8, var9, var10) && var1.doesBlockHaveSolidTopSurface(var8, var9 - 1, var10)) {
               var1.setBlock(var8, var9, var10, Block.chest.blockID, 0, 2);
               TileEntityChest var11 = (TileEntityChest)var1.getBlockTileEntity(var8, var9, var10);
               if (var11 != null && var11 != null) {
                  WeightedRandomChestContent.generateChestContents(var2, this.theBonusChestGenerator, var11, this.itemsToGenerateInBonusChest);
               }

               if (var1.isAirBlock(var8 - 1, var9, var10) && var1.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
                  var1.setBlock(var8 - 1, var9, var10, Block.torchWood.blockID, 0, 2);
               }

               if (var1.isAirBlock(var8 + 1, var9, var10) && var1.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
                  var1.setBlock(var8 + 1, var9, var10, Block.torchWood.blockID, 0, 2);
               }

               if (var1.isAirBlock(var8, var9, var10 - 1) && var1.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
                  var1.setBlock(var8, var9, var10 - 1, Block.torchWood.blockID, 0, 2);
               }

               if (var1.isAirBlock(var8, var9, var10 + 1) && var1.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
                  var1.setBlock(var8, var9, var10 + 1, Block.torchWood.blockID, 0, 2);
               }

               return true;
            }
         }

         return false;
      }
   }
}
