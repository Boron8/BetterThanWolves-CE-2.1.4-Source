package net.minecraft.src;

public class ItemSnow extends ItemBlockWithMetadata {
   public ItemSnow(int var1, Block var2) {
      super(var1, var2);
   }

   @Override
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      if (var1.stackSize == 0) {
         return false;
      } else if (!var2.canPlayerEdit(var4, var5, var6, var7, var1)) {
         return false;
      } else {
         int var11 = var3.getBlockId(var4, var5, var6);
         if (var11 == Block.snow.blockID) {
            Block var12 = Block.blocksList[this.g()];
            int var13 = var3.getBlockMetadata(var4, var5, var6);
            int var14 = var13 & 7;
            if (var14 <= 6
               && var3.checkNoEntityCollision(var12.getCollisionBoundingBoxFromPool(var3, var4, var5, var6))
               && var3.setBlockMetadataWithNotify(var4, var5, var6, var14 + 1 | var13 & -8, 2)) {
               var3.playSoundEffect(
                  var4 + 0.5F,
                  var5 + 0.5F,
                  var6 + 0.5F,
                  var12.stepSound.getPlaceSound(),
                  (var12.stepSound.getVolume() + 1.0F) / 2.0F,
                  var12.stepSound.getPitch() * 0.8F
               );
               var1.stackSize--;
               return true;
            }
         }

         return super.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }
   }
}
