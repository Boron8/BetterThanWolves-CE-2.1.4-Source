package net.minecraft.src;

import btw.item.items.ShearsItem;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockLeaves extends BlockLeavesBase {
   public static final String[] LEAF_TYPES = new String[]{"oak", "spruce", "birch", "jungle"};
   public static final String[][] field_94396_b = new String[][]{
      {"leaves", "leaves_spruce", "leaves", "leaves_jungle"}, {"leaves_opaque", "leaves_spruce_opaque", "leaves_opaque", "leaves_jungle_opaque"}
   };
   @Environment(EnvType.CLIENT)
   private int field_94394_cP;
   private Icon[][] iconArray = new Icon[2][];
   public int[] adjacentTreeBlocks;

   protected BlockLeaves(int par1) {
      super(par1, Material.leaves, false);
      this.b(true);
      this.a(CreativeTabs.tabDecorations);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBlockColor() {
      if (ColorizeBlock.colorizeBlock(this)) {
         return ColorizeBlock.blockColor;
      } else {
         double var1 = 0.5;
         double var3 = 1.0;
         return ColorizerFoliage.getFoliageColor(var1, var3);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderColor(int par1) {
      return ColorizeBlock.colorizeBlock(this, par1)
         ? ColorizeBlock.blockColor
         : (
            (par1 & 3) == 1
               ? ColorizerFoliage.getFoliageColorPine()
               : ((par1 & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic())
         );
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      if (ColorizeBlock.colorizeBlock(this, par1IBlockAccess, par2, par3, par4)) {
         return ColorizeBlock.blockColor;
      } else {
         int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
         if ((var5 & 3) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
         } else if ((var5 & 3) == 2) {
            return ColorizerFoliage.getFoliageColorBirch();
         } else {
            int var6 = 0;
            int var7 = 0;
            int var8 = 0;

            for (int var9 = -1; var9 <= 1; var9++) {
               for (int var10 = -1; var10 <= 1; var10++) {
                  int var11 = par1IBlockAccess.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor();
                  var6 += (var11 & 0xFF0000) >> 16;
                  var7 += (var11 & 0xFF00) >> 8;
                  var8 += var11 & 0xFF;
               }
            }

            return (var6 / 9 & 0xFF) << 16 | (var7 / 9 & 0xFF) << 8 | var8 / 9 & 0xFF;
         }
      }
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
      byte var7 = 1;
      int var8 = var7 + 1;
      if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            for (int var10 = -var7; var10 <= var7; var10++) {
               for (int var11 = -var7; var11 <= var7; var11++) {
                  int var12 = par1World.getBlockId(par2 + var9, par3 + var10, par4 + var11);
                  if (var12 == Block.leaves.blockID) {
                     int var13 = par1World.getBlockMetadata(par2 + var9, par3 + var10, par4 + var11);
                     par1World.setBlockMetadataWithNotify(par2 + var9, par3 + var10, par4 + var11, var13 | 8, 4);
                  }
               }
            }
         }
      }
   }

   @Override
   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (!par1World.isRemote) {
         int var6 = par1World.getBlockMetadata(par2, par3, par4);
         if ((var6 & 8) != 0 && (var6 & 4) == 0) {
            byte var7 = 4;
            int var8 = var7 + 1;
            byte var9 = 32;
            int var10 = var9 * var9;
            int var11 = var9 / 2;
            if (this.adjacentTreeBlocks == null) {
               this.adjacentTreeBlocks = new int[var9 * var9 * var9];
            }

            if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
               for (int var12 = -var7; var12 <= var7; var12++) {
                  for (int var13 = -var7; var13 <= var7; var13++) {
                     for (int var14 = -var7; var14 <= var7; var14++) {
                        int var15 = par1World.getBlockId(par2 + var12, par3 + var13, par4 + var14);
                        if (var15 == Block.wood.blockID) {
                           this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
                        } else if (var15 == Block.leaves.blockID) {
                           this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
                        } else {
                           this.adjacentTreeBlocks[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
                        }
                     }
                  }
               }

               for (int var16 = 1; var16 <= 4; var16++) {
                  for (int var13 = -var7; var13 <= var7; var13++) {
                     for (int var14x = -var7; var14x <= var7; var14x++) {
                        for (int var15 = -var7; var15 <= var7; var15++) {
                           if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11) * var9 + var15 + var11] == var16 - 1) {
                              if (this.adjacentTreeBlocks[(var13 + var11 - 1) * var10 + (var14x + var11) * var9 + var15 + var11] == -2) {
                                 this.adjacentTreeBlocks[(var13 + var11 - 1) * var10 + (var14x + var11) * var9 + var15 + var11] = var16;
                              }

                              if (this.adjacentTreeBlocks[(var13 + var11 + 1) * var10 + (var14x + var11) * var9 + var15 + var11] == -2) {
                                 this.adjacentTreeBlocks[(var13 + var11 + 1) * var10 + (var14x + var11) * var9 + var15 + var11] = var16;
                              }

                              if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11 - 1) * var9 + var15 + var11] == -2) {
                                 this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11 - 1) * var9 + var15 + var11] = var16;
                              }

                              if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11 + 1) * var9 + var15 + var11] == -2) {
                                 this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11 + 1) * var9 + var15 + var11] = var16;
                              }

                              if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11) * var9 + (var15 + var11 - 1)] == -2) {
                                 this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11) * var9 + (var15 + var11 - 1)] = var16;
                              }

                              if (this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11) * var9 + var15 + var11 + 1] == -2) {
                                 this.adjacentTreeBlocks[(var13 + var11) * var10 + (var14x + var11) * var9 + var15 + var11 + 1] = var16;
                              }
                           }
                        }
                     }
                  }
               }
            }

            int var12 = this.adjacentTreeBlocks[var11 * var10 + var11 * var9 + var11];
            if (var12 >= 0) {
               par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 & -9, 4);
            } else {
               this.removeLeaves(par1World, par2, par3, par4);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (par1World.canLightningStrikeAt(par2, par3 + 1, par4) && !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && par5Random.nextInt(15) == 1) {
         double var6 = par2 + par5Random.nextFloat();
         double var8 = par3 - 0.05;
         double var10 = par4 + par5Random.nextFloat();
         par1World.spawnParticle("dripWater", var6, var8, var10, 0.0, 0.0, 0.0);
      }
   }

   private void removeLeaves(World par1World, int par2, int par3, int par4) {
      this.c(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
      par1World.setBlockToAir(par2, par3, par4);
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return par1Random.nextInt(20) == 0 ? 1 : 0;
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return Block.sapling.blockID;
   }

   @Override
   public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
      if (!par1World.isRemote) {
         int var8 = 20;
         if ((par5 & 3) == 3) {
            var8 = 40;
         }

         if (par7 > 0) {
            var8 -= 2 << par7;
            if (var8 < 10) {
               var8 = 10;
            }
         }

         if (par1World.rand.nextInt(var8) == 0) {
            int var9 = this.idDropped(par5, par1World.rand, par7);
            this.b(par1World, par2, par3, par4, new ItemStack(var9, 1, this.damageDropped(par5)));
         }

         var8 = 200;
         if (par7 > 0) {
            var8 -= 10 << par7;
            if (var8 < 40) {
               var8 = 40;
            }
         }

         if ((par5 & 3) == 0 && par1World.rand.nextInt(var8) == 0) {
            this.b(par1World, par2, par3, par4, new ItemStack(Item.appleRed, 1, 0));
         }
      }
   }

   @Override
   public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
      if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null && par2EntityPlayer.getCurrentEquippedItem().getItem() instanceof ShearsItem
         )
       {
         par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
         this.b(par1World, par3, par4, par5, new ItemStack(Block.leaves.blockID, 1, par6 & 3));
      } else {
         super.a(par1World, par2EntityPlayer, par3, par4, par5, par6);
      }
   }

   @Override
   public int damageDropped(int par1) {
      return par1 & 3;
   }

   @Override
   public boolean isOpaqueCube() {
      return !this.graphicsLevel;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return (par2 & 3) == 1
         ? this.iconArray[this.field_94394_cP][1]
         : ((par2 & 3) == 3 ? this.iconArray[this.field_94394_cP][3] : this.iconArray[this.field_94394_cP][0]);
   }

   @Environment(EnvType.CLIENT)
   public void setGraphicsLevel(boolean par1) {
      this.graphicsLevel = par1;
      this.field_94394_cP = par1 ? 0 : 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
   }

   @Override
   protected ItemStack createStackedBlock(int par1) {
      return new ItemStack(this.blockID, 1, par1 & 3);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      for (int var2 = 0; var2 < field_94396_b.length; var2++) {
         this.iconArray[var2] = new Icon[field_94396_b[var2].length];

         for (int var3 = 0; var3 < field_94396_b[var2].length; var3++) {
            this.iconArray[var2][var3] = par1IconRegister.registerIcon(field_94396_b[var2][var3]);
         }
      }
   }
}
