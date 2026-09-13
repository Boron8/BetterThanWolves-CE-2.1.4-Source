package net.minecraft.src;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockChest extends BlockContainer {
   private final Random random = new Random();
   public final int isTrapped;

   protected BlockChest(int par1, int par2) {
      super(par1, Material.wood);
      this.isTrapped = par2;
      this.a(CreativeTabs.tabDecorations);
      this.a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
   public int getRenderType() {
      return 22;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      if (par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == this.blockID) {
         this.a(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
      } else if (par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == this.blockID) {
         this.a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
      } else if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == this.blockID) {
         this.a(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      } else if (par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == this.blockID) {
         this.a(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
      } else {
         this.a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      }
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
      this.unifyAdjacentChests(par1World, par2, par3, par4);
      int var5 = par1World.getBlockId(par2, par3, par4 - 1);
      int var6 = par1World.getBlockId(par2, par3, par4 + 1);
      int var7 = par1World.getBlockId(par2 - 1, par3, par4);
      int var8 = par1World.getBlockId(par2 + 1, par3, par4);
      if (var5 == this.blockID) {
         this.unifyAdjacentChests(par1World, par2, par3, par4 - 1);
      }

      if (var6 == this.blockID) {
         this.unifyAdjacentChests(par1World, par2, par3, par4 + 1);
      }

      if (var7 == this.blockID) {
         this.unifyAdjacentChests(par1World, par2 - 1, par3, par4);
      }

      if (var8 == this.blockID) {
         this.unifyAdjacentChests(par1World, par2 + 1, par3, par4);
      }
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
      int var7 = par1World.getBlockId(par2, par3, par4 - 1);
      int var8 = par1World.getBlockId(par2, par3, par4 + 1);
      int var9 = par1World.getBlockId(par2 - 1, par3, par4);
      int var10 = par1World.getBlockId(par2 + 1, par3, par4);
      byte var11 = 0;
      int var12 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      if (var12 == 0) {
         var11 = 2;
      }

      if (var12 == 1) {
         var11 = 5;
      }

      if (var12 == 2) {
         var11 = 3;
      }

      if (var12 == 3) {
         var11 = 4;
      }

      if (var7 != this.blockID && var8 != this.blockID && var9 != this.blockID && var10 != this.blockID) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, var11, 3);
      } else {
         if ((var7 == this.blockID || var8 == this.blockID) && (var11 == 4 || var11 == 5)) {
            if (var7 == this.blockID) {
               par1World.setBlockMetadataWithNotify(par2, par3, par4 - 1, var11, 3);
            } else {
               par1World.setBlockMetadataWithNotify(par2, par3, par4 + 1, var11, 3);
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, var11, 3);
         }

         if ((var9 == this.blockID || var10 == this.blockID) && (var11 == 2 || var11 == 3)) {
            if (var9 == this.blockID) {
               par1World.setBlockMetadataWithNotify(par2 - 1, par3, par4, var11, 3);
            } else {
               par1World.setBlockMetadataWithNotify(par2 + 1, par3, par4, var11, 3);
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, var11, 3);
         }
      }

      if (par6ItemStack.hasDisplayName()) {
         ((TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4)).func_94043_a(par6ItemStack.getDisplayName());
      }
   }

   public void unifyAdjacentChests(World par1World, int par2, int par3, int par4) {
      if (!par1World.isRemote) {
         int var5 = par1World.getBlockId(par2, par3, par4 - 1);
         int var6 = par1World.getBlockId(par2, par3, par4 + 1);
         int var7 = par1World.getBlockId(par2 - 1, par3, par4);
         int var8 = par1World.getBlockId(par2 + 1, par3, par4);
         boolean var9 = true;
         boolean hasNeighborChest = false;
         int metadata;
         if (var5 == this.blockID || var6 == this.blockID) {
            int var10 = par1World.getBlockId(par2 - 1, par3, var5 == this.blockID ? par4 - 1 : par4 + 1);
            int var11 = par1World.getBlockId(par2 + 1, par3, var5 == this.blockID ? par4 - 1 : par4 + 1);
            metadata = 5;
            hasNeighborChest = true;
            int var14;
            if (var5 == this.blockID) {
               var14 = par1World.getBlockMetadata(par2, par3, par4 - 1);
            } else {
               var14 = par1World.getBlockMetadata(par2, par3, par4 + 1);
            }

            if (var14 == 4) {
               metadata = 4;
            }

            if ((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11]) {
               metadata = 5;
            }

            if ((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var10]) {
               metadata = 4;
            }
         } else if (var7 != this.blockID && var8 != this.blockID) {
            metadata = 3;
            if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
               metadata = 3;
            }

            if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
               metadata = 2;
            }

            if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
               metadata = 5;
            }

            if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
               metadata = 4;
            }
         } else {
            int var10x = par1World.getBlockId(var7 == this.blockID ? par2 - 1 : par2 + 1, par3, par4 - 1);
            int var11x = par1World.getBlockId(var7 == this.blockID ? par2 - 1 : par2 + 1, par3, par4 + 1);
            metadata = 3;
            hasNeighborChest = true;
            int var14x;
            if (var7 == this.blockID) {
               var14x = par1World.getBlockMetadata(par2 - 1, par3, par4);
            } else {
               var14x = par1World.getBlockMetadata(par2 + 1, par3, par4);
            }

            if (var14x == 2) {
               metadata = 2;
            }

            if ((Block.opaqueCubeLookup[var5] || Block.opaqueCubeLookup[var10x]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11x]) {
               metadata = 3;
            }

            if ((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11x]) && !Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var10x]) {
               metadata = 2;
            }
         }

         if (hasNeighborChest) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata, 3);
         }
      }
   }

   @Override
   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      int var5 = 0;
      if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID) {
         var5++;
      }

      if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID) {
         var5++;
      }

      if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID) {
         var5++;
      }

      if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID) {
         var5++;
      }

      return var5 > 1
         ? false
         : (
            this.isThereANeighborChest(par1World, par2 - 1, par3, par4)
               ? false
               : (
                  this.isThereANeighborChest(par1World, par2 + 1, par3, par4)
                     ? false
                     : (this.isThereANeighborChest(par1World, par2, par3, par4 - 1) ? false : !this.isThereANeighborChest(par1World, par2, par3, par4 + 1))
               )
         );
   }

   private boolean isThereANeighborChest(World par1World, int par2, int par3, int par4) {
      return par1World.getBlockId(par2, par3, par4) != this.blockID
         ? false
         : (
            par1World.getBlockId(par2 - 1, par3, par4) == this.blockID
               ? true
               : (
                  par1World.getBlockId(par2 + 1, par3, par4) == this.blockID
                     ? true
                     : (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID ? true : par1World.getBlockId(par2, par3, par4 + 1) == this.blockID)
               )
         );
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      super.a(par1World, par2, par3, par4, par5);
      TileEntityChest var6 = (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4);
      if (var6 != null) {
         var6.updateContainingBlockInfo();
      }
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
      TileEntityChest var7 = (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4);
      if (var7 != null) {
         for (int var8 = 0; var8 < var7.getSizeInventory(); var8++) {
            ItemStack var9 = var7.getStackInSlot(var8);
            if (var9 != null) {
               float var10 = this.random.nextFloat() * 0.8F + 0.1F;
               float var11 = this.random.nextFloat() * 0.8F + 0.1F;
               float var12 = this.random.nextFloat() * 0.8F + 0.1F;

               while (var9.stackSize > 0) {
                  int var13 = this.random.nextInt(21) + 10;
                  if (var13 > var9.stackSize) {
                     var13 = var9.stackSize;
                  }

                  var9.stackSize -= var13;
                  EntityItem var14 = (EntityItem)EntityList.createEntityOfType(
                     EntityItem.class,
                     par1World,
                     (double)(par2 + var10),
                     (double)(par3 + var11),
                     (double)(par4 + var12),
                     new ItemStack(var9.itemID, var13, var9.getItemDamage())
                  );
                  float var15 = 0.05F;
                  var14.motionX = (float)this.random.nextGaussian() * var15;
                  var14.motionY = (float)this.random.nextGaussian() * var15 + 0.2F;
                  var14.motionZ = (float)this.random.nextGaussian() * var15;
                  if (var9.hasTagCompound()) {
                     var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                  }

                  par1World.spawnEntityInWorld(var14);
               }
            }
         }

         par1World.func_96440_m(par2, par3, par4, par5);
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if (par1World.isRemote) {
         return true;
      } else {
         IInventory var10 = this.getInventory(par1World, par2, par3, par4);
         if (var10 != null) {
            par5EntityPlayer.displayGUIChest(var10);
         }

         return true;
      }
   }

   public IInventory getInventory(World par1World, int par2, int par3, int par4) {
      Object var5 = (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4);
      if (var5 == null) {
         return null;
      } else if (par1World.isBlockNormalCube(par2, par3 + 1, par4)) {
         return null;
      } else if (isOcelotBlockingChest(par1World, par2, par3, par4)) {
         return null;
      } else if (par1World.getBlockId(par2 - 1, par3, par4) != this.blockID
         || !par1World.isBlockNormalCube(par2 - 1, par3 + 1, par4) && !isOcelotBlockingChest(par1World, par2 - 1, par3, par4)) {
         if (par1World.getBlockId(par2 + 1, par3, par4) != this.blockID
            || !par1World.isBlockNormalCube(par2 + 1, par3 + 1, par4) && !isOcelotBlockingChest(par1World, par2 + 1, par3, par4)) {
            if (par1World.getBlockId(par2, par3, par4 - 1) != this.blockID
               || !par1World.isBlockNormalCube(par2, par3 + 1, par4 - 1) && !isOcelotBlockingChest(par1World, par2, par3, par4 - 1)) {
               if (par1World.getBlockId(par2, par3, par4 + 1) != this.blockID
                  || !par1World.isBlockNormalCube(par2, par3 + 1, par4 + 1) && !isOcelotBlockingChest(par1World, par2, par3, par4 + 1)) {
                  if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID) {
                     var5 = new InventoryLargeChest(
                        "container.chestDouble", (TileEntityChest)par1World.getBlockTileEntity(par2 - 1, par3, par4), (IInventory)var5
                     );
                  }

                  if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID) {
                     var5 = new InventoryLargeChest(
                        "container.chestDouble", (IInventory)var5, (TileEntityChest)par1World.getBlockTileEntity(par2 + 1, par3, par4)
                     );
                  }

                  if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID) {
                     var5 = new InventoryLargeChest(
                        "container.chestDouble", (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4 - 1), (IInventory)var5
                     );
                  }

                  if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID) {
                     var5 = new InventoryLargeChest(
                        "container.chestDouble", (IInventory)var5, (TileEntityChest)par1World.getBlockTileEntity(par2, par3, par4 + 1)
                     );
                  }

                  return (IInventory)var5;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   @Override
   public TileEntity createNewTileEntity(World par1World) {
      return new TileEntityChest();
   }

   @Override
   public boolean canProvidePower() {
      return this.isTrapped == 1;
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      if (!this.canProvidePower()) {
         return 0;
      } else {
         int var6 = ((TileEntityChest)par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).numUsingPlayers;
         return MathHelper.clamp_int(var6, 0, 15);
      }
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return par5 == 1 ? this.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5) : 0;
   }

   private static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3) {
      for (EntityOcelot var5 : par0World.getEntitiesWithinAABB(
         EntityOcelot.class, AxisAlignedBB.getAABBPool().getAABB(par1, par2 + 1, par3, par1 + 1, par2 + 2, par3 + 1)
      )) {
         if (var5.n()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory(this.getInventory(par1World, par2, par3, par4));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon("wood");
   }
}
