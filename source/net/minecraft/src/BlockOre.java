package net.minecraft.src;

import java.util.Random;

public class BlockOre extends Block {
   public BlockOre(int var1) {
      super(var1, Material.rock);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      if (this.blockID == Block.oreCoal.blockID) {
         return Item.coal.itemID;
      } else if (this.blockID == Block.oreDiamond.blockID) {
         return Item.diamond.itemID;
      } else if (this.blockID == Block.oreLapis.blockID) {
         return Item.dyePowder.itemID;
      } else if (this.blockID == Block.oreEmerald.blockID) {
         return Item.emerald.itemID;
      } else {
         return this.blockID == Block.oreNetherQuartz.blockID ? Item.netherQuartz.itemID : this.blockID;
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return this.blockID == Block.oreLapis.blockID ? 4 + var1.nextInt(5) : 1;
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      if (var1 > 0 && this.blockID != this.idDropped(0, var2, var1)) {
         int var3 = var2.nextInt(var1 + 2) - 1;
         if (var3 < 0) {
            var3 = 0;
         }

         return this.quantityDropped(var2) * (var3 + 1);
      } else {
         return this.quantityDropped(var2);
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
      super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
      if (this.idDropped(var5, var1.rand, var7) != this.blockID) {
         int var8 = 0;
         if (this.blockID == Block.oreCoal.blockID) {
            var8 = MathHelper.getRandomIntegerInRange(var1.rand, 0, 2);
         } else if (this.blockID == Block.oreDiamond.blockID) {
            var8 = MathHelper.getRandomIntegerInRange(var1.rand, 3, 7);
         } else if (this.blockID == Block.oreEmerald.blockID) {
            var8 = MathHelper.getRandomIntegerInRange(var1.rand, 3, 7);
         } else if (this.blockID == Block.oreLapis.blockID) {
            var8 = MathHelper.getRandomIntegerInRange(var1.rand, 2, 5);
         } else if (this.blockID == Block.oreNetherQuartz.blockID) {
            var8 = MathHelper.getRandomIntegerInRange(var1.rand, 2, 5);
         }

         this.j(var1, var2, var3, var4, var8);
      }
   }

   @Override
   public int damageDropped(int var1) {
      return this.blockID == Block.oreLapis.blockID ? 4 : 0;
   }
}
