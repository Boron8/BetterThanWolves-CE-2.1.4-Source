package net.minecraft.src;

public class ItemSlab extends ItemBlock {
   private final boolean isFullBlock;
   private final BlockHalfSlab theHalfSlab;
   private final BlockHalfSlab doubleSlab;

   public ItemSlab(int var1, BlockHalfSlab var2, BlockHalfSlab var3, boolean var4) {
      super(var1);
      this.theHalfSlab = var2;
      this.doubleSlab = var3;
      this.isFullBlock = var4;
      this.e(0);
      this.a(true);
   }

   @Override
   public Icon getIconFromDamage(int var1) {
      return Block.blocksList[this.itemID].getIcon(2, var1);
   }

   @Override
   public int getMetadata(int var1) {
      return var1;
   }

   @Override
   public String getUnlocalizedName(ItemStack var1) {
      return this.theHalfSlab.getFullSlabName(var1.getItemDamage());
   }

   @Override
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      if (this.isFullBlock) {
         return super.onItemUse(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      } else if (var1.stackSize == 0) {
         return false;
      } else if (!var2.canPlayerEdit(var4, var5, var6, var7, var1)) {
         return false;
      } else {
         int var11 = var3.getBlockId(var4, var5, var6);
         int var12 = var3.getBlockMetadata(var4, var5, var6);
         int var13 = var12 & 7;
         boolean var14 = (var12 & 8) != 0;
         if ((var7 == 1 && !var14 || var7 == 0 && var14) && var11 == this.theHalfSlab.blockID && var13 == var1.getItemDamage()) {
            if (var3.checkNoEntityCollision(this.doubleSlab.b(var3, var4, var5, var6)) && var3.setBlock(var4, var5, var6, this.doubleSlab.blockID, var13, 3)) {
               var3.playSoundEffect(
                  var4 + 0.5F,
                  var5 + 0.5F,
                  var6 + 0.5F,
                  this.doubleSlab.stepSound.getPlaceSound(),
                  (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F,
                  this.doubleSlab.stepSound.getPitch() * 0.8F
               );
               var1.stackSize--;
            }

            return true;
         } else {
            return this.func_77888_a(var1, var2, var3, var4, var5, var6, var7)
               ? true
               : super.onItemUse(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
         }
      }
   }

   @Override
   public boolean canPlaceItemBlockOnSide(World var1, int var2, int var3, int var4, int var5, EntityPlayer var6, ItemStack var7) {
      int var8 = var2;
      int var9 = var3;
      int var10 = var4;
      int var11 = var1.getBlockId(var2, var3, var4);
      int var12 = var1.getBlockMetadata(var2, var3, var4);
      int var13 = var12 & 7;
      boolean var14 = (var12 & 8) != 0;
      if ((var5 == 1 && !var14 || var5 == 0 && var14) && var11 == this.theHalfSlab.blockID && var13 == var7.getItemDamage()) {
         return true;
      } else {
         if (var5 == 0) {
            var3--;
         }

         if (var5 == 1) {
            var3++;
         }

         if (var5 == 2) {
            var4--;
         }

         if (var5 == 3) {
            var4++;
         }

         if (var5 == 4) {
            var2--;
         }

         if (var5 == 5) {
            var2++;
         }

         var11 = var1.getBlockId(var2, var3, var4);
         var12 = var1.getBlockMetadata(var2, var3, var4);
         var13 = var12 & 7;
         var14 = (var12 & 8) != 0;
         return var11 == this.theHalfSlab.blockID && var13 == var7.getItemDamage()
            ? true
            : super.canPlaceItemBlockOnSide(var1, var8, var9, var10, var5, var6, var7);
      }
   }

   private boolean func_77888_a(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
      if (var7 == 0) {
         var5--;
      }

      if (var7 == 1) {
         var5++;
      }

      if (var7 == 2) {
         var6--;
      }

      if (var7 == 3) {
         var6++;
      }

      if (var7 == 4) {
         var4--;
      }

      if (var7 == 5) {
         var4++;
      }

      int var8 = var3.getBlockId(var4, var5, var6);
      int var9 = var3.getBlockMetadata(var4, var5, var6);
      int var10 = var9 & 7;
      if (var8 == this.theHalfSlab.blockID && var10 == var1.getItemDamage()) {
         if (var3.checkNoEntityCollision(this.doubleSlab.b(var3, var4, var5, var6)) && var3.setBlock(var4, var5, var6, this.doubleSlab.blockID, var10, 3)) {
            var3.playSoundEffect(
               var4 + 0.5F,
               var5 + 0.5F,
               var6 + 0.5F,
               this.doubleSlab.stepSound.getPlaceSound(),
               (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F,
               this.doubleSlab.stepSound.getPitch() * 0.8F
            );
            var1.stackSize--;
         }

         return true;
      } else {
         return false;
      }
   }
}
