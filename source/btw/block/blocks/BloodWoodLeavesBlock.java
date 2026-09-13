package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.entity.item.BloodWoodSaplingItemEntity;
import btw.item.items.ShearsItem;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BloodWoodLeavesBlock extends LeavesBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconLowDetail;

   public BloodWoodLeavesBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockBloodWoodLeaves");
      this.c(0.2F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.k(1);
      this.setFireProperties(Flammability.EXTREME);
      this.a(Block.soundGrassFootstep);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      int l = 1;
      int i1 = l + 1;
      if (world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
         for (int j1 = -l; j1 <= l; j1++) {
            for (int k1 = -l; k1 <= l; k1++) {
               for (int l1 = -l; l1 <= l; l1++) {
                  int i2 = world.getBlockId(i + j1, j + k1, k + l1);
                  if (i2 == this.blockID) {
                     int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
                     world.setBlockMetadata(i + j1, j + k1, k + l1, j2 | 8);
                  }
               }
            }
         }
      }

      super.a(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return BTWBlocks.aestheticVegetation.blockID;
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      if (!world.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ShearsItem) {
         this.dropBlockAsItem_do(world, i, j, k, new ItemStack(BTWBlocks.bloodWoodLeaves.blockID, 1, iMetadata & 3));
         player.getCurrentEquippedItem().damageItem(1, player);
      } else {
         super.a(world, player, i, j, k, iMetadata);
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1) {
      if (!world.isRemote) {
         int j1 = this.a(i1, world.rand);

         for (int k1 = 0; k1 < j1; k1++) {
            if (!(world.rand.nextFloat() > f)) {
               int l1 = this.idDropped(l, world.rand, i1);
               if (l1 > 0) {
                  this.dropBlockAsItem_do(world, i, j, k, new ItemStack(l1, 1, this.damageDropped(l)));
               }
            }
         }
      }
   }

   @Override
   public int damageDropped(int iMetaData) {
      return 2;
   }

   @Override
   protected void dropBlockAsItem_do(World world, int i, int j, int k, ItemStack itemStack) {
      if (itemStack.itemID != BTWBlocks.aestheticVegetation.blockID || itemStack.getItemDamage() != 2) {
         super.b(world, i, j, k, itemStack);
      } else if (!world.isRemote) {
         float f = 0.7F;
         double d = world.rand.nextFloat() * f + (1.0F - f) * 0.5;
         double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5;
         double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5;
         EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(BloodWoodSaplingItemEntity.class, world, i + d, j + d1, k + d2, itemStack);
         entityitem.delayBeforeCanPickup = 10;
         world.spawnEntityInWorld(entityitem);
      }
   }

   @Override
   public boolean isOpaqueCube() {
      return !Block.leaves.graphicsLevel;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (!world.isRemote) {
         int l = world.getBlockMetadata(i, j, k);
         if ((l & 8) != 0 && (l & 4) == 0) {
            byte byte0 = 4;
            int i1 = byte0 + 1;
            byte byte1 = 32;
            int j1 = byte1 * byte1;
            int k1 = byte1 / 2;
            if (this.adjacentTreeBlocks == null) {
               this.adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
            }

            if (world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
               for (int l1 = -byte0; l1 <= byte0; l1++) {
                  for (int k2 = -byte0; k2 <= byte0; k2++) {
                     for (int i3 = -byte0; i3 <= byte0; i3++) {
                        int k3 = world.getBlockId(i + l1, j + k2, k + i3);
                        if (k3 == BTWBlocks.bloodWoodLog.blockID) {
                           this.adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + i3 + k1] = 0;
                        } else if (k3 == BTWBlocks.bloodWoodLeaves.blockID) {
                           this.adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + i3 + k1] = -2;
                        } else {
                           this.adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + i3 + k1] = -1;
                        }
                     }
                  }
               }

               for (int i2 = 1; i2 <= 4; i2++) {
                  for (int l2 = -byte0; l2 <= byte0; l2++) {
                     for (int j3 = -byte0; j3 <= byte0; j3++) {
                        for (int l3 = -byte0; l3 <= byte0; l3++) {
                           if (this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1] == i2 - 1) {
                              if (this.adjacentTreeBlocks[(l2 + k1 - 1) * j1 + (j3 + k1) * byte1 + l3 + k1] == -2) {
                                 this.adjacentTreeBlocks[(l2 + k1 - 1) * j1 + (j3 + k1) * byte1 + l3 + k1] = i2;
                              }

                              if (this.adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + l3 + k1] == -2) {
                                 this.adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + l3 + k1] = i2;
                              }

                              if (this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 - 1) * byte1 + l3 + k1] == -2) {
                                 this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 - 1) * byte1 + l3 + k1] = i2;
                              }

                              if (this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + l3 + k1] == -2) {
                                 this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + l3 + k1] = i2;
                              }

                              if (this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 - 1)] == -2) {
                                 this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 - 1)] = i2;
                              }

                              if (this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1 + 1] == -2) {
                                 this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1 + 1] = i2;
                              }
                           }
                        }
                     }
                  }
               }
            }

            int j2 = this.adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
            if (j2 >= 0) {
               world.setBlockMetadata(i, j, k, l & -9);
            } else {
               this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
               world.setBlockWithNotify(i, j, k, 0);
            }
         }
      }
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
      super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
   }

   @Override
   protected void generateAshOnBurn(World world, int i, int j, int k) {
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean isLeafBlock(IBlockAccess blockAccess, int x, int y, int z) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("leaves");
      this.iconLowDetail = register.registerIcon("leaves_opaque");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return !Block.leaves.graphicsLevel ? this.iconLowDetail : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBlockColor() {
      return 14163743;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderColor(int i) {
      return 14163743;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
      return 14163743;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      this.graphicsLevel = Block.leaves.graphicsLevel;
      return super.a(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
