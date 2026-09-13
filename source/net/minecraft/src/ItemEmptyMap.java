package net.minecraft.src;

public class ItemEmptyMap extends ItemMapBase {
   protected ItemEmptyMap(int var1) {
      super(var1);
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      ItemStack var4 = new ItemStack(Item.map, 1, var2.getUniqueDataId("map"));
      String var5 = "map_" + var4.getItemDamage();
      MapData var6 = new MapData(var5);
      var2.setItemData(var5, var6);
      var6.scale = 0;
      int var7 = 128 * (1 << var6.scale);
      var6.xCenter = (int)(Math.round(var3.posX / var7) * var7);
      var6.zCenter = (int)(Math.round(var3.posZ / var7) * var7);
      var6.dimension = (byte)var2.provider.dimensionId;
      var6.c();
      var1.stackSize--;
      if (var1.stackSize <= 0) {
         return var4;
      } else {
         if (!var3.inventory.addItemStackToInventory(var4.copy())) {
            var3.dropPlayerItem(var4);
         }

         return var1;
      }
   }
}
