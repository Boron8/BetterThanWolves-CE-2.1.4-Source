package net.minecraft.src;

import java.util.Random;

public class BlockCocoa extends BlockDirectional {
   public static final String[] cocoaIcons = new String[]{"cocoa_0", "cocoa_1", "cocoa_2"};
   private Icon[] iconArray;

   public BlockCocoa(int var1) {
      super(var1, Material.plants);
      this.b(true);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return this.iconArray[2];
   }

   public Icon func_94468_i_(int var1) {
      if (var1 < 0 || var1 >= this.iconArray.length) {
         var1 = this.iconArray.length - 1;
      }

      return this.iconArray[var1];
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (!this.canBlockStay(var1, var2, var3, var4)) {
         this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
         var1.setBlockToAir(var2, var3, var4);
      } else if (var1.rand.nextInt(5) == 0) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         int var7 = func_72219_c(var6);
         if (var7 < 2) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, ++var7 << 2 | j(var6), 2);
         }
      }
   }

   @Override
   public boolean canBlockStay(World var1, int var2, int var3, int var4) {
      int var5 = j(var1.getBlockMetadata(var2, var3, var4));
      var2 += Direction.offsetX[var5];
      var4 += Direction.offsetZ[var5];
      int var6 = var1.getBlockId(var2, var3, var4);
      return var6 == Block.wood.blockID && BlockLog.limitToValidMetadata(var1.getBlockMetadata(var2, var3, var4)) == 3;
   }

   @Override
   public int getRenderType() {
      return 28;
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
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.b(var1, var2, var3, var4);
   }

   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.c_(var1, var2, var3, var4);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      int var5 = var1.getBlockMetadata(var2, var3, var4);
      int var6 = j(var5);
      int var7 = func_72219_c(var5);
      int var8 = 4 + var7 * 2;
      int var9 = 5 + var7 * 2;
      float var10 = var8 / 2.0F;
      switch (var6) {
         case 0:
            this.a((8.0F - var10) / 16.0F, (12.0F - var9) / 16.0F, (15.0F - var8) / 16.0F, (8.0F + var10) / 16.0F, 0.75F, 0.9375F);
            break;
         case 1:
            this.a(0.0625F, (12.0F - var9) / 16.0F, (8.0F - var10) / 16.0F, (1.0F + var8) / 16.0F, 0.75F, (8.0F + var10) / 16.0F);
            break;
         case 2:
            this.a((8.0F - var10) / 16.0F, (12.0F - var9) / 16.0F, 0.0625F, (8.0F + var10) / 16.0F, 0.75F, (1.0F + var8) / 16.0F);
            break;
         case 3:
            this.a((15.0F - var8) / 16.0F, (12.0F - var9) / 16.0F, (8.0F - var10) / 16.0F, 0.9375F, 0.75F, (8.0F + var10) / 16.0F);
      }
   }

   @Override
   public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6) {
      int var7 = ((MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 3) + 0) % 4;
      var1.setBlockMetadataWithNotify(var2, var3, var4, var7, 2);
   }

   @Override
   public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      if (var5 == 1 || var5 == 0) {
         var5 = 2;
      }

      return Direction.rotateOpposite[Direction.facingToDirection[var5]];
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (!this.canBlockStay(var1, var2, var3, var4)) {
         this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
         var1.setBlockToAir(var2, var3, var4);
      }
   }

   public static int func_72219_c(int var0) {
      return (var0 & 12) >> 2;
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
      int var8 = func_72219_c(var5);
      byte var9 = 1;
      if (var8 >= 2) {
         var9 = 3;
      }

      for (int var10 = 0; var10 < var9; var10++) {
         this.b(var1, var2, var3, var4, new ItemStack(Item.dyePowder, 1, 3));
      }
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return Item.dyePowder.itemID;
   }

   @Override
   public int getDamageValue(World var1, int var2, int var3, int var4) {
      return 3;
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.iconArray = new Icon[cocoaIcons.length];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = var1.registerIcon(cocoaIcons[var2]);
      }
   }
}
