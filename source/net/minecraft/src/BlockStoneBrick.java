package net.minecraft.src;

import java.util.List;

public class BlockStoneBrick extends Block {
   public static final String[] STONE_BRICK_TYPES = new String[]{"default", "mossy", "cracked", "chiseled"};
   public static final String[] field_94407_b = new String[]{
      "stonebricksmooth", "stonebricksmooth_mossy", "stonebricksmooth_cracked", "stonebricksmooth_carved"
   };
   private Icon[] field_94408_c;

   public BlockStoneBrick(int var1) {
      super(var1, Material.rock);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      if (var2 < 0 || var2 >= field_94407_b.length) {
         var2 = 0;
      }

      return this.field_94408_c[var2];
   }

   @Override
   public int damageDropped(int var1) {
      return var1;
   }

   @Override
   public void getSubBlocks(int var1, CreativeTabs var2, List var3) {
      for (int var4 = 0; var4 < 4; var4++) {
         var3.add(new ItemStack(var1, 1, var4));
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.field_94408_c = new Icon[field_94407_b.length];

      for (int var2 = 0; var2 < this.field_94408_c.length; var2++) {
         this.field_94408_c[var2] = var1.registerIcon(field_94407_b[var2]);
      }
   }
}
