package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockSapling extends BlockFlower {
   public static final String[] WOOD_TYPES = new String[]{"oak", "spruce", "birch", "jungle"};
   private static final String[] field_94370_b = new String[]{"sapling", "sapling_spruce", "sapling_birch", "sapling_jungle"};
   private Icon[] saplingIcon;

   protected BlockSapling(int var1) {
      super(var1);
      float var2 = 0.4F;
      this.a(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var2 * 2.0F, 0.5F + var2);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (!var1.isRemote) {
         super.updateTick(var1, var2, var3, var4, var5);
         if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9 && var5.nextInt(7) == 0) {
            this.markOrGrowMarked(var1, var2, var3, var4, var5);
         }
      }
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      var2 &= 3;
      return this.saplingIcon[var2];
   }

   public void markOrGrowMarked(World var1, int var2, int var3, int var4, Random var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      if ((var6 & 8) == 0) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, var6 | 8, 4);
      } else {
         this.growTree(var1, var2, var3, var4, var5);
      }
   }

   public void growTree(World var1, int var2, int var3, int var4, Random var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4) & 3;
      Object var7 = null;
      int var8 = 0;
      int var9 = 0;
      boolean var10 = false;
      if (var6 == 1) {
         var7 = new WorldGenTaiga2(true);
      } else if (var6 == 2) {
         var7 = new WorldGenForest(true);
      } else if (var6 == 3) {
         for (var8 = 0; var8 >= -1; var8--) {
            for (var9 = 0; var9 >= -1; var9--) {
               if (this.isSameSapling(var1, var2 + var8, var3, var4 + var9, 3)
                  && this.isSameSapling(var1, var2 + var8 + 1, var3, var4 + var9, 3)
                  && this.isSameSapling(var1, var2 + var8, var3, var4 + var9 + 1, 3)
                  && this.isSameSapling(var1, var2 + var8 + 1, var3, var4 + var9 + 1, 3)) {
                  var7 = new WorldGenHugeTrees(true, 10 + var5.nextInt(20), 3, 3);
                  var10 = true;
                  break;
               }
            }

            if (var7 != null) {
               break;
            }
         }

         if (var7 == null) {
            var9 = 0;
            var8 = 0;
            var7 = new WorldGenTrees(true, 4 + var5.nextInt(7), 3, 3, false);
         }
      } else {
         var7 = new WorldGenTrees(true);
         if (var5.nextInt(10) == 0) {
            var7 = new WorldGenBigTree(true);
         }
      }

      if (var10) {
         var1.setBlock(var2 + var8, var3, var4 + var9, 0, 0, 4);
         var1.setBlock(var2 + var8 + 1, var3, var4 + var9, 0, 0, 4);
         var1.setBlock(var2 + var8, var3, var4 + var9 + 1, 0, 0, 4);
         var1.setBlock(var2 + var8 + 1, var3, var4 + var9 + 1, 0, 0, 4);
      } else {
         var1.setBlock(var2, var3, var4, 0, 0, 4);
      }

      if (!((WorldGenerator)var7).generate(var1, var5, var2 + var8, var3, var4 + var9)) {
         if (var10) {
            var1.setBlock(var2 + var8, var3, var4 + var9, this.blockID, var6, 4);
            var1.setBlock(var2 + var8 + 1, var3, var4 + var9, this.blockID, var6, 4);
            var1.setBlock(var2 + var8, var3, var4 + var9 + 1, this.blockID, var6, 4);
            var1.setBlock(var2 + var8 + 1, var3, var4 + var9 + 1, this.blockID, var6, 4);
         } else {
            var1.setBlock(var2, var3, var4, this.blockID, var6, 4);
         }
      }
   }

   public boolean isSameSapling(World var1, int var2, int var3, int var4, int var5) {
      return var1.getBlockId(var2, var3, var4) == this.blockID && (var1.getBlockMetadata(var2, var3, var4) & 3) == var5;
   }

   @Override
   public int damageDropped(int var1) {
      return var1 & 3;
   }

   @Override
   public void getSubBlocks(int var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
      var3.add(new ItemStack(var1, 1, 2));
      var3.add(new ItemStack(var1, 1, 3));
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.saplingIcon = new Icon[field_94370_b.length];

      for (int var2 = 0; var2 < this.saplingIcon.length; var2++) {
         this.saplingIcon[var2] = var1.registerIcon(field_94370_b[var2]);
      }
   }
}
