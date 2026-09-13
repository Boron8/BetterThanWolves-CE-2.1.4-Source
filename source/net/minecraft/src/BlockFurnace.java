package net.minecraft.src;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockFurnace extends BlockContainer {
   private final Random furnaceRand = new Random();
   protected final boolean isActive;
   protected static boolean keepFurnaceInventory = false;
   @Environment(EnvType.CLIENT)
   protected Icon furnaceIconTop;
   @Environment(EnvType.CLIENT)
   protected Icon furnaceIconFront;

   protected BlockFurnace(int par1, boolean par2) {
      super(par1, Material.rock);
      this.isActive = par2;
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return Block.furnaceIdle.blockID;
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
   }

   private void setDefaultDirection(World par1World, int par2, int par3, int par4) {
      if (!par1World.isRemote) {
         int var5 = par1World.getBlockId(par2, par3, par4 - 1);
         int var6 = par1World.getBlockId(par2, par3, par4 + 1);
         int var7 = par1World.getBlockId(par2 - 1, par3, par4);
         int var8 = par1World.getBlockId(par2 + 1, par3, par4);
         byte var9 = 3;
         if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
            var9 = 3;
         }

         if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
            var9 = 2;
         }

         if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
            var9 = 5;
         }

         if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
            var9 = 4;
         }

         par1World.setBlockMetadataWithNotify(par2, par3, par4, var9, 2);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return par1 == 1 ? this.furnaceIconTop : (par1 == 0 ? this.furnaceIconTop : (par1 != par2 ? this.blockIcon : this.furnaceIconFront));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon("furnace_side");
      this.furnaceIconFront = par1IconRegister.registerIcon(this.isActive ? "furnace_front_lit" : "furnace_front");
      this.furnaceIconTop = par1IconRegister.registerIcon("furnace_top");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (this.isActive) {
         int var6 = par1World.getBlockMetadata(par2, par3, par4);
         var6 &= 7;
         float var7 = par2 + 0.5F;
         float var8 = par3 + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
         float var9 = par4 + 0.5F;
         float var10 = 0.52F;
         float var11 = par5Random.nextFloat() * 0.6F - 0.3F;
         if (var6 == 4) {
            par1World.spawnParticle("smoke", var7 - var10, var8, var9 + var11, 0.0, 0.0, 0.0);
            par1World.spawnParticle("flame", var7 - var10, var8, var9 + var11, 0.0, 0.0, 0.0);
         } else if (var6 == 5) {
            par1World.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0, 0.0, 0.0);
            par1World.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0, 0.0, 0.0);
         } else if (var6 == 2) {
            par1World.spawnParticle("smoke", var7 + var11, var8, var9 - var10, 0.0, 0.0, 0.0);
            par1World.spawnParticle("flame", var7 + var11, var8, var9 - var10, 0.0, 0.0, 0.0);
         } else if (var6 == 3) {
            par1World.spawnParticle("smoke", var7 + var11, var8, var9 + var10, 0.0, 0.0, 0.0);
            par1World.spawnParticle("flame", var7 + var11, var8, var9 + var10, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if (par1World.isRemote) {
         return true;
      } else {
         TileEntityFurnace var10 = (TileEntityFurnace)par1World.getBlockTileEntity(par2, par3, par4);
         if (var10 != null) {
            par5EntityPlayer.displayGUIFurnace(var10);
         }

         return true;
      }
   }

   public void updateFurnaceBlockState(boolean par0, World par1World, int par2, int par3, int par4, boolean bHasContents) {
      int var5x = par1World.getBlockMetadata(par2, par3, par4);
      TileEntity var6x = par1World.getBlockTileEntity(par2, par3, par4);
      keepFurnaceInventory = true;
      if (par0) {
         par1World.setBlock(par2, par3, par4, Block.furnaceBurning.blockID);
      } else {
         par1World.setBlock(par2, par3, par4, Block.furnaceIdle.blockID);
      }

      keepFurnaceInventory = false;
      if (!bHasContents) {
         var5x &= 7;
      } else {
         var5x |= 8;
      }

      par1World.setBlockMetadataWithNotify(par2, par3, par4, var5x, 2);
      if (var6x != null) {
         var6x.validate();
         par1World.setBlockTileEntity(par2, par3, par4, var6x);
      }
   }

   @Override
   public TileEntity createNewTileEntity(World par1World) {
      return new TileEntityFurnace();
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
      int var7 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      if (var7 == 0) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
      }

      if (var7 == 1) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
      }

      if (var7 == 2) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
      }

      if (var7 == 3) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
      }

      if (par6ItemStack.hasDisplayName()) {
         ((TileEntityFurnace)par1World.getBlockTileEntity(par2, par3, par4)).func_94129_a(par6ItemStack.getDisplayName());
      }
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
      if (!keepFurnaceInventory) {
         TileEntityFurnace var7 = (TileEntityFurnace)par1World.getBlockTileEntity(par2, par3, par4);
         if (var7 != null) {
            for (int var8 = 0; var8 < var7.getSizeInventory(); var8++) {
               ItemStack var9 = var7.getStackInSlot(var8);
               if (var9 != null) {
                  float var10 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                  float var11 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                  float var12 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                  while (var9.stackSize > 0) {
                     int var13 = this.furnaceRand.nextInt(21) + 10;
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
                     if (var9.hasTagCompound()) {
                        var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                     }

                     float var15 = 0.05F;
                     var14.motionX = (float)this.furnaceRand.nextGaussian() * var15;
                     var14.motionY = (float)this.furnaceRand.nextGaussian() * var15 + 0.2F;
                     var14.motionZ = (float)this.furnaceRand.nextGaussian() * var15;
                     par1World.spawnEntityInWorld(var14);
                  }
               }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
         }
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return Block.furnaceIdle.blockID;
   }
}
