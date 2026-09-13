package net.minecraft.src;

import btw.item.items.ShearsItem;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockVine extends Block {
   public BlockVine(int par1) {
      super(par1, Material.vine);
      this.b(true);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void setBlockBoundsForItemRender() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public int getRenderType() {
      return 20;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
      float var7 = 1.0F;
      float var8 = 1.0F;
      float var9 = 1.0F;
      float var10 = 0.0F;
      float var11 = 0.0F;
      float var12 = 0.0F;
      boolean var13 = var6 > 0;
      if ((var6 & 2) != 0) {
         var10 = Math.max(var10, 0.0625F);
         var7 = 0.0F;
         var8 = 0.0F;
         var11 = 1.0F;
         var9 = 0.0F;
         var12 = 1.0F;
         var13 = true;
      }

      if ((var6 & 8) != 0) {
         var7 = Math.min(var7, 0.9375F);
         var10 = 1.0F;
         var8 = 0.0F;
         var11 = 1.0F;
         var9 = 0.0F;
         var12 = 1.0F;
         var13 = true;
      }

      if ((var6 & 4) != 0) {
         var12 = Math.max(var12, 0.0625F);
         var9 = 0.0F;
         var7 = 0.0F;
         var10 = 1.0F;
         var8 = 0.0F;
         var11 = 1.0F;
         var13 = true;
      }

      if ((var6 & 1) != 0) {
         var9 = Math.min(var9, 0.9375F);
         var12 = 1.0F;
         var7 = 0.0F;
         var10 = 1.0F;
         var8 = 0.0F;
         var11 = 1.0F;
         var13 = true;
      }

      if (!var13 && this.canBePlacedOn(par1IBlockAccess.getBlockId(par2, par3 + 1, par4))) {
         var8 = Math.min(var8, 0.9375F);
         var11 = 1.0F;
         var7 = 0.0F;
         var10 = 1.0F;
         var9 = 0.0F;
         var12 = 1.0F;
      }

      this.a(var7, var8, var9, var10, var11, var12);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   @Override
   public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
      switch (par5) {
         case 1:
            return this.canBePlacedOn(par1World.getBlockId(par2, par3 + 1, par4));
         case 2:
            return this.canBePlacedOn(par1World.getBlockId(par2, par3, par4 + 1));
         case 3:
            return this.canBePlacedOn(par1World.getBlockId(par2, par3, par4 - 1));
         case 4:
            return this.canBePlacedOn(par1World.getBlockId(par2 + 1, par3, par4));
         case 5:
            return this.canBePlacedOn(par1World.getBlockId(par2 - 1, par3, par4));
         default:
            return false;
      }
   }

   private boolean canBePlacedOn(int par1) {
      if (par1 == 0) {
         return false;
      } else {
         Block var2 = Block.blocksList[par1];
         return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
      }
   }

   private boolean canVineStay(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockMetadata(par2, par3, par4);
      int var6 = var5;
      if (var5 > 0) {
         for (int var7 = 0; var7 <= 3; var7++) {
            int var8 = 1 << var7;
            if ((var5 & var8) != 0
               && !this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var7], par3, par4 + Direction.offsetZ[var7]))
               && (par1World.getBlockId(par2, par3 + 1, par4) != this.blockID || (par1World.getBlockMetadata(par2, par3 + 1, par4) & var8) == 0)) {
               var6 &= ~var8;
            }
         }
      }

      if (var6 == 0 && !this.canBePlacedOn(par1World.getBlockId(par2, par3 + 1, par4))) {
         return false;
      } else {
         if (var6 != var5) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
         }

         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBlockColor() {
      return ColorizeBlock.colorizeBlock(this) ? ColorizeBlock.blockColor : ColorizerFoliage.getFoliageColorBasic();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderColor(int par1) {
      return ColorizeBlock.colorizeBlock(this, par1) ? ColorizeBlock.blockColor : ColorizerFoliage.getFoliageColorBasic();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return ColorizeBlock.colorizeBlock(this, par1IBlockAccess, par2, par3, par4)
         ? ColorizeBlock.blockColor
         : par1IBlockAccess.getBiomeGenForCoords(par2, par4).getBiomeFoliageColor();
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      if (!par1World.isRemote && !this.canVineStay(par1World, par2, par3, par4)) {
         this.c(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
      }
   }

   @Override
   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (!par1World.isRemote && par1World.rand.nextInt(4) == 0) {
         byte var6 = 4;
         int var7 = 5;
         boolean var8 = false;

         label137:
         for (int var9 = par2 - var6; var9 <= par2 + var6; var9++) {
            for (int var10 = par4 - var6; var10 <= par4 + var6; var10++) {
               for (int var11 = par3 - 1; var11 <= par3 + 1; var11++) {
                  if (par1World.getBlockId(var9, var11, var10) == this.blockID) {
                     if (--var7 <= 0) {
                        var8 = true;
                        break label137;
                     }
                  }
               }
            }
         }

         int var15 = par1World.getBlockMetadata(par2, par3, par4);
         int var10 = par1World.rand.nextInt(6);
         int var11x = Direction.facingToDirection[var10];
         if (var10 == 1 && par3 < 255 && par1World.isAirBlock(par2, par3 + 1, par4)) {
            if (var8) {
               return;
            }

            int var12 = par1World.rand.nextInt(16) & var15;
            if (var12 > 0) {
               for (int var13 = 0; var13 <= 3; var13++) {
                  if (!this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var13], par3 + 1, par4 + Direction.offsetZ[var13]))) {
                     var12 &= ~(1 << var13);
                  }
               }

               if (var12 > 0) {
                  par1World.setBlock(par2, par3 + 1, par4, this.blockID, var12, 2);
               }
            }
         } else if (var10 >= 2 && var10 <= 5 && (var15 & 1 << var11x) == 0) {
            if (var8) {
               return;
            }

            int var12 = par1World.getBlockId(par2 + Direction.offsetX[var11x], par3, par4 + Direction.offsetZ[var11x]);
            if (var12 == 0 || Block.blocksList[var12] == null) {
               int var13x = var11x + 1 & 3;
               int var14 = var11x + 3 & 3;
               if ((var15 & 1 << var13x) != 0
                  && this.canBePlacedOn(
                     par1World.getBlockId(
                        par2 + Direction.offsetX[var11x] + Direction.offsetX[var13x], par3, par4 + Direction.offsetZ[var11x] + Direction.offsetZ[var13x]
                     )
                  )) {
                  par1World.setBlock(par2 + Direction.offsetX[var11x], par3, par4 + Direction.offsetZ[var11x], this.blockID, 1 << var13x, 2);
               } else if ((var15 & 1 << var14) != 0
                  && this.canBePlacedOn(
                     par1World.getBlockId(
                        par2 + Direction.offsetX[var11x] + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var11x] + Direction.offsetZ[var14]
                     )
                  )) {
                  par1World.setBlock(par2 + Direction.offsetX[var11x], par3, par4 + Direction.offsetZ[var11x], this.blockID, 1 << var14, 2);
               } else if ((var15 & 1 << var13x) != 0
                  && par1World.isAirBlock(
                     par2 + Direction.offsetX[var11x] + Direction.offsetX[var13x], par3, par4 + Direction.offsetZ[var11x] + Direction.offsetZ[var13x]
                  )
                  && this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var13x], par3, par4 + Direction.offsetZ[var13x]))) {
                  par1World.setBlock(
                     par2 + Direction.offsetX[var11x] + Direction.offsetX[var13x],
                     par3,
                     par4 + Direction.offsetZ[var11x] + Direction.offsetZ[var13x],
                     this.blockID,
                     1 << (var11x + 2 & 3),
                     2
                  );
               } else if ((var15 & 1 << var14) != 0
                  && par1World.isAirBlock(
                     par2 + Direction.offsetX[var11x] + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var11x] + Direction.offsetZ[var14]
                  )
                  && this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var14]))) {
                  par1World.setBlock(
                     par2 + Direction.offsetX[var11x] + Direction.offsetX[var14],
                     par3,
                     par4 + Direction.offsetZ[var11x] + Direction.offsetZ[var14],
                     this.blockID,
                     1 << (var11x + 2 & 3),
                     2
                  );
               } else if (this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var11x], par3 + 1, par4 + Direction.offsetZ[var11x]))) {
                  par1World.setBlock(par2 + Direction.offsetX[var11x], par3, par4 + Direction.offsetZ[var11x], this.blockID, 0, 2);
               }
            } else if (Block.blocksList[var12].blockMaterial.isOpaque() && Block.blocksList[var12].renderAsNormalBlock()) {
               par1World.setBlockMetadataWithNotify(par2, par3, par4, var15 | 1 << var11x, 2);
            }
         } else if (par3 > 1) {
            int var12 = par1World.getBlockId(par2, par3 - 1, par4);
            if (var12 == 0) {
               int var13x = par1World.rand.nextInt(16) & var15;
               if (var13x > 0) {
                  par1World.setBlock(par2, par3 - 1, par4, this.blockID, var13x, 2);
               }
            } else if (var12 == this.blockID) {
               int var13x = par1World.rand.nextInt(16) & var15;
               int var14 = par1World.getBlockMetadata(par2, par3 - 1, par4);
               if (var14 != (var14 | var13x)) {
                  par1World.setBlockMetadataWithNotify(par2, par3 - 1, par4, var14 | var13x, 2);
               }
            }
         }
      }
   }

   @Override
   public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
      byte var10 = 0;
      switch (par5) {
         case 2:
            var10 = 1;
            break;
         case 3:
            var10 = 4;
            break;
         case 4:
            var10 = 8;
            break;
         case 5:
            var10 = 2;
      }

      return var10 != 0 ? var10 : par9;
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return 0;
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return 0;
   }

   @Override
   public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
      if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null && par2EntityPlayer.getCurrentEquippedItem().getItem() instanceof ShearsItem
         )
       {
         par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
         this.b(par1World, par3, par4, par5, new ItemStack(Block.vine, 1, 0));
      } else {
         super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
      }
   }
}
