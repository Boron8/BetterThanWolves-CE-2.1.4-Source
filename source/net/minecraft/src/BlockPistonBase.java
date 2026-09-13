package net.minecraft.src;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockPistonBase extends Block {
   protected final boolean isSticky;
   @Environment(EnvType.CLIENT)
   protected Icon innerTopIcon;
   @Environment(EnvType.CLIENT)
   protected Icon bottomIcon;
   @Environment(EnvType.CLIENT)
   protected Icon topIcon;

   public BlockPistonBase(int par1, boolean par2) {
      super(par1, Material.piston);
      this.isSticky = par2;
      this.a(j);
      this.c(0.5F);
      this.a(CreativeTabs.tabRedstone);
   }

   @Environment(EnvType.CLIENT)
   public Icon getPistonExtensionTexture() {
      return this.topIcon;
   }

   @Environment(EnvType.CLIENT)
   public void func_96479_b(float par1, float par2, float par3, float par4, float par5, float par6) {
      this.a(par1, par2, par3, par4, par5, par6);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      int var3 = getOrientation(par2);
      return var3 > 5
         ? this.topIcon
         : (
            par1 == var3
               ? (
                  !isExtended(par2) && this.minX <= 0.0 && this.minY <= 0.0 && this.minZ <= 0.0 && this.maxX >= 1.0 && this.maxY >= 1.0 && this.maxZ >= 1.0
                     ? this.topIcon
                     : this.innerTopIcon
               )
               : (par1 == Facing.oppositeSide[var3] ? this.bottomIcon : this.blockIcon)
         );
   }

   @Environment(EnvType.CLIENT)
   public static Icon func_94496_b(String par0Str) {
      return par0Str == "piston_side"
         ? Block.pistonBase.blockIcon
         : (
            par0Str == "piston_top"
               ? Block.pistonBase.topIcon
               : (par0Str == "piston_top_sticky" ? Block.pistonStickyBase.topIcon : (par0Str == "piston_inner_top" ? Block.pistonBase.innerTopIcon : null))
         );
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon("piston_side");
      this.topIcon = par1IconRegister.registerIcon(this.isSticky ? "piston_top_sticky" : "piston_top");
      this.innerTopIcon = par1IconRegister.registerIcon("piston_inner_top");
      this.bottomIcon = par1IconRegister.registerIcon("piston_bottom");
   }

   @Override
   public int getRenderType() {
      return 16;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      return false;
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
      int var7 = determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
      par1World.setBlockMetadataWithNotify(par2, par3, par4, var7, 2);
      if (!par1World.isRemote) {
         this.updatePistonState(par1World, par2, par3, par4);
      }
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      if (!par1World.isRemote) {
         this.updatePistonState(par1World, par2, par3, par4);
      }
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null) {
         this.updatePistonState(par1World, par2, par3, par4);
      }
   }

   protected void updatePistonState(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockMetadata(par2, par3, par4);
      int var6 = getOrientation(var5);
      if (var6 != 7) {
         boolean var7 = this.isIndirectlyPowered(par1World, par2, par3, par4, var6);
         if (var7 && !isExtended(var5)) {
            if (this.canExtend(par1World, par2, par3, par4, var6)) {
               par1World.addBlockEvent(par2, par3, par4, this.blockID, 0, var6);
            }
         } else if (!var7 && isExtended(var5)) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
            par1World.addBlockEvent(par2, par3, par4, this.blockID, 1, var6);
         }
      }
   }

   private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4, int par5) {
      return par5 != 0 && par1World.getIndirectPowerOutput(par2, par3 - 1, par4, 0)
         ? true
         : (
            par5 != 1 && par1World.getIndirectPowerOutput(par2, par3 + 1, par4, 1)
               ? true
               : (
                  par5 != 2 && par1World.getIndirectPowerOutput(par2, par3, par4 - 1, 2)
                     ? true
                     : (
                        par5 != 3 && par1World.getIndirectPowerOutput(par2, par3, par4 + 1, 3)
                           ? true
                           : (
                              par5 != 5 && par1World.getIndirectPowerOutput(par2 + 1, par3, par4, 5)
                                 ? true
                                 : (
                                    par5 != 4 && par1World.getIndirectPowerOutput(par2 - 1, par3, par4, 4)
                                       ? true
                                       : (
                                          par1World.getIndirectPowerOutput(par2, par3, par4, 0)
                                             ? true
                                             : (
                                                par1World.getIndirectPowerOutput(par2, par3 + 2, par4, 1)
                                                   ? true
                                                   : (
                                                      par1World.getIndirectPowerOutput(par2, par3 + 1, par4 - 1, 2)
                                                         ? true
                                                         : (
                                                            par1World.getIndirectPowerOutput(par2, par3 + 1, par4 + 1, 3)
                                                               ? true
                                                               : (
                                                                  par1World.getIndirectPowerOutput(par2 - 1, par3 + 1, par4, 4)
                                                                     ? true
                                                                     : par1World.getIndirectPowerOutput(par2 + 1, par3 + 1, par4, 5)
                                                               )
                                                         )
                                                   )
                                             )
                                       )
                                 )
                           )
                     )
               )
         );
   }

   @Override
   public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
      if (!par1World.isRemote) {
         boolean var7 = this.isIndirectlyPowered(par1World, par2, par3, par4, par6);
         if (var7 && par5 == 1) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 2);
            return false;
         }

         if (!var7 && par5 == 0) {
            return false;
         }
      }

      if (par5 == 0) {
         if (!this.tryExtend(par1World, par2, par3, par4, par6)) {
            return false;
         }

         par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 2);
         par1World.playSoundEffect(par2 + 0.5, par3 + 0.5, par4 + 0.5, "tile.piston.out", 0.5F, par1World.rand.nextFloat() * 0.25F + 0.6F);
      } else if (par5 == 1) {
         TileEntity var16 = par1World.getBlockTileEntity(
            par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]
         );
         if (var16 instanceof TileEntityPiston) {
            ((TileEntityPiston)var16).clearPistonTileEntity();
         }

         par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, par6, 3);
         par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(this.blockID, par6, par6, false, true));
         if (this.isSticky) {
            int var8 = par2 + Facing.offsetsXForSide[par6] * 2;
            int var9 = par3 + Facing.offsetsYForSide[par6] * 2;
            int var10 = par4 + Facing.offsetsZForSide[par6] * 2;
            int var11 = par1World.getBlockId(var8, var9, var10);
            int var12 = par1World.getBlockMetadata(var8, var9, var10);
            boolean var13 = false;
            if (var11 == Block.pistonMoving.blockID) {
               TileEntity var14 = par1World.getBlockTileEntity(var8, var9, var10);
               if (var14 instanceof TileEntityPiston) {
                  TileEntityPiston var15 = (TileEntityPiston)var14;
                  if (var15.getPistonOrientation() == par6 && var15.isExtending()) {
                     var15.clearPistonTileEntity();
                     var11 = var15.getStoredBlockID();
                     var12 = var15.getBlockMetadata();
                     var13 = true;
                  }
               }
            }

            Block targetBlock = Block.blocksList[var11];
            if (!var13 && targetBlock != null && targetBlock.canBlockBePulledByPiston(par1World, var8, var9, var10, Block.getOppositeFacing(par6))) {
               var12 = targetBlock.adjustMetadataForPistonMove(var12);
               par2 += Facing.offsetsXForSide[par6];
               par3 += Facing.offsetsYForSide[par6];
               par4 += Facing.offsetsZForSide[par6];
               NBTTagCompound tileEntityData = getBlockTileEntityData(par1World, var8, var9, var10);
               par1World.removeBlockTileEntity(var8, var9, var10);
               par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, var12, 3);
               par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(var11, var12, par6, false, false));
               ((TileEntityPiston)par1World.getBlockTileEntity(par2, par3, par4)).storeTileEntity(tileEntityData);
               par1World.setBlockToAir(var8, var9, var10);
            } else if (!var13) {
               par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);
            }
         } else {
            par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);
         }

         par1World.playSoundEffect(par2 + 0.5, par3 + 0.5, par4 + 0.5, "tile.piston.in", 0.5F, par1World.rand.nextFloat() * 0.15F + 0.6F);
      }

      return true;
   }

   public static NBTTagCompound getBlockTileEntityData(World worldObj, int x, int y, int z) {
      NBTTagCompound tileEntityData = null;
      TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);
      if (tileEntity != null) {
         tileEntityData = new NBTTagCompound();
         tileEntity.writeToNBT(tileEntityData);
      }

      return tileEntityData;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
      if (isExtended(var5)) {
         switch (getOrientation(var5)) {
            case 0:
               this.a(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 1:
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
               break;
            case 2:
               this.a(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
               break;
            case 3:
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
               break;
            case 4:
               this.a(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 5:
               this.a(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
         }
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   public void setBlockBoundsForItemRender() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
      return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   public static int getOrientation(int par0) {
      return par0 & 7;
   }

   public static boolean isExtended(int par0) {
      return (par0 & 8) != 0;
   }

   public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLiving par4EntityLiving) {
      if (MathHelper.abs((float)par4EntityLiving.posX - par1) < 2.0F && MathHelper.abs((float)par4EntityLiving.posZ - par3) < 2.0F) {
         double var5 = par4EntityLiving.posY + 1.82 - par4EntityLiving.yOffset;
         if (var5 - par2 > 2.0) {
            return 1;
         }

         if (par2 - var5 > 0.0) {
            return 0;
         }
      }

      int var7 = MathHelper.floor_double(par4EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      return var7 == 0 ? 2 : (var7 == 1 ? 5 : (var7 == 2 ? 3 : (var7 == 3 ? 4 : 0)));
   }

   protected boolean canExtend(World world, int i, int j, int k, int iFacing) {
      return false;
   }

   protected boolean tryExtend(World world, int i, int j, int k, int iFacing) {
      return false;
   }
}
