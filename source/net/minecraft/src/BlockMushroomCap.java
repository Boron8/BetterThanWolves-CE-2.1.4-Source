package net.minecraft.src;

import java.util.Random;

public class BlockMushroomCap extends Block {
   private static final String[] field_94429_a = new String[]{"mushroom_skin_brown", "mushroom_skin_red"};
   private final int mushroomType;
   private Icon[] iconArray;
   private Icon field_94426_cO;
   private Icon field_94427_cP;

   public BlockMushroomCap(int var1, Material var2, int var3) {
      super(var1, var2);
      this.mushroomType = var3;
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      if (var2 == 10 && var1 > 1) {
         return this.field_94426_cO;
      } else if (var2 >= 1 && var2 <= 9 && var1 == 1) {
         return this.iconArray[this.mushroomType];
      } else if (var2 >= 1 && var2 <= 3 && var1 == 2) {
         return this.iconArray[this.mushroomType];
      } else if (var2 >= 7 && var2 <= 9 && var1 == 3) {
         return this.iconArray[this.mushroomType];
      } else if ((var2 == 1 || var2 == 4 || var2 == 7) && var1 == 4) {
         return this.iconArray[this.mushroomType];
      } else if ((var2 == 3 || var2 == 6 || var2 == 9) && var1 == 5) {
         return this.iconArray[this.mushroomType];
      } else if (var2 == 14) {
         return this.iconArray[this.mushroomType];
      } else {
         return var2 == 15 ? this.field_94426_cO : this.field_94427_cP;
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      int var2 = var1.nextInt(10) - 7;
      if (var2 < 0) {
         var2 = 0;
      }

      return var2;
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Block.mushroomBrown.blockID + this.mushroomType;
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return Block.mushroomBrown.blockID + this.mushroomType;
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.iconArray = new Icon[field_94429_a.length];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = var1.registerIcon(field_94429_a[var2]);
      }

      this.field_94427_cP = var1.registerIcon("mushroom_inside");
      this.field_94426_cO = var1.registerIcon("mushroom_skin_stem");
   }
}
