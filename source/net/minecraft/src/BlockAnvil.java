package net.minecraft.src;

import java.util.List;

public class BlockAnvil extends BlockSand {
   public static final String[] statuses = new String[]{"intact", "slightlyDamaged", "veryDamaged"};
   private static final String[] anvilIconNames = new String[]{"anvil_top", "anvil_top_damaged_1", "anvil_top_damaged_2"};
   public int field_82521_b = 0;
   private Icon[] iconArray;

   protected BlockAnvil(int var1) {
      super(var1, Material.anvil);
      this.k(0);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      if (this.field_82521_b == 3 && var1 == 1) {
         int var3 = (var2 >> 2) % this.iconArray.length;
         return this.iconArray[var3];
      } else {
         return this.blockIcon;
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon("anvil_base");
      this.iconArray = new Icon[anvilIconNames.length];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = var1.registerIcon(anvilIconNames[var2]);
      }
   }

   @Override
   public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6) {
      int var7 = MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      int var8 = var1.getBlockMetadata(var2, var3, var4) >> 2;
      var7 = ++var7 % 4;
      if (var7 == 0) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, 2 | var8 << 2, 2);
      }

      if (var7 == 1) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, 3 | var8 << 2, 2);
      }

      if (var7 == 2) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, 0 | var8 << 2, 2);
      }

      if (var7 == 3) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, 1 | var8 << 2, 2);
      }
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      if (var1.isRemote) {
         return true;
      } else {
         var5.displayGUIAnvil(var2, var3, var4);
         return true;
      }
   }

   @Override
   public int getRenderType() {
      return 35;
   }

   @Override
   public int damageDropped(int var1) {
      return var1 >> 2;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      int var5 = var1.getBlockMetadata(var2, var3, var4) & 3;
      if (var5 != 3 && var5 != 1) {
         this.a(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
      } else {
         this.a(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
      }
   }

   @Override
   public void getSubBlocks(int var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
      var3.add(new ItemStack(var1, 1, 2));
   }

   @Override
   protected void onStartFalling(EntityFallingSand var1) {
      var1.setIsAnvil(true);
   }

   @Override
   public void onFinishFalling(World var1, int var2, int var3, int var4, int var5) {
      var1.playAuxSFX(1022, var2, var3, var4, 0);
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return true;
   }
}
