package net.minecraft.src;

import java.util.List;

public class BlockQuartz extends Block {
   public static final String[] quartzBlockTypes = new String[]{"default", "chiseled", "lines"};
   private static final String[] quartzBlockTextureTypes = new String[]{"quartzblock_side", "quartzblock_chiseled", "quartzblock_lines", null, null};
   private Icon[] quartzblockIcons;
   private Icon quartzblock_chiseled_top;
   private Icon quartzblock_lines_top;
   private Icon quartzblock_top;
   private Icon quartzblock_bottom;

   public BlockQuartz(int var1) {
      super(var1, Material.rock);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      if (var2 != 2 && var2 != 3 && var2 != 4) {
         if (var1 != 1 && (var1 != 0 || var2 != 1)) {
            if (var1 == 0) {
               return this.quartzblock_bottom;
            } else {
               if (var2 < 0 || var2 >= this.quartzblockIcons.length) {
                  var2 = 0;
               }

               return this.quartzblockIcons[var2];
            }
         } else {
            return var2 == 1 ? this.quartzblock_chiseled_top : this.quartzblock_top;
         }
      } else if (var2 != 2 || var1 != 1 && var1 != 0) {
         if (var2 != 3 || var1 != 5 && var1 != 4) {
            return var2 != 4 || var1 != 2 && var1 != 3 ? this.quartzblockIcons[var2] : this.quartzblock_lines_top;
         } else {
            return this.quartzblock_lines_top;
         }
      } else {
         return this.quartzblock_lines_top;
      }
   }

   @Override
   public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      if (var9 == 2) {
         switch (var5) {
            case 0:
            case 1:
               var9 = 2;
               break;
            case 2:
            case 3:
               var9 = 4;
               break;
            case 4:
            case 5:
               var9 = 3;
         }
      }

      return var9;
   }

   @Override
   public int damageDropped(int var1) {
      return var1 != 3 && var1 != 4 ? var1 : 2;
   }

   @Override
   protected ItemStack createStackedBlock(int var1) {
      return var1 != 3 && var1 != 4 ? super.createStackedBlock(var1) : new ItemStack(this.blockID, 1, 2);
   }

   @Override
   public int getRenderType() {
      return 39;
   }

   @Override
   public void getSubBlocks(int var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
      var3.add(new ItemStack(var1, 1, 2));
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.quartzblockIcons = new Icon[quartzBlockTextureTypes.length];

      for (int var2 = 0; var2 < this.quartzblockIcons.length; var2++) {
         if (quartzBlockTextureTypes[var2] == null) {
            this.quartzblockIcons[var2] = this.quartzblockIcons[var2 - 1];
         } else {
            this.quartzblockIcons[var2] = var1.registerIcon(quartzBlockTextureTypes[var2]);
         }
      }

      this.quartzblock_top = var1.registerIcon("quartzblock_top");
      this.quartzblock_chiseled_top = var1.registerIcon("quartzblock_chiseled_top");
      this.quartzblock_lines_top = var1.registerIcon("quartzblock_lines_top");
      this.quartzblock_bottom = var1.registerIcon("quartzblock_bottom");
   }
}
