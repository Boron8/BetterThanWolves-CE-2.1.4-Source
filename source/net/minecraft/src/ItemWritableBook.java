package net.minecraft.src;

public class ItemWritableBook extends Item {
   public ItemWritableBook(int var1) {
      super(var1);
      this.d(1);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      var3.displayGUIBook(var1);
      return var1;
   }

   @Override
   public boolean getShareTag() {
      return true;
   }

   public static boolean validBookTagPages(NBTTagCompound var0) {
      if (var0 == null) {
         return false;
      } else if (!var0.hasKey("pages")) {
         return false;
      } else {
         NBTTagList var1 = (NBTTagList)var0.getTag("pages");

         for (int var2 = 0; var2 < var1.tagCount(); var2++) {
            NBTTagString var3 = (NBTTagString)var1.tagAt(var2);
            if (var3.data == null) {
               return false;
            }

            if (var3.data.length() > 256) {
               return false;
            }
         }

         return true;
      }
   }
}
