package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockHopper extends BlockContainer {
   private final Random field_94457_a = new Random();
   @Environment(EnvType.CLIENT)
   private Icon hopperIcon;
   @Environment(EnvType.CLIENT)
   private Icon hopperTopIcon;
   @Environment(EnvType.CLIENT)
   private Icon hopperInsideIcon;

   public BlockHopper(int par1) {
      super(par1, Material.iron);
      this.a(CreativeTabs.tabRedstone);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      float var8 = 0.125F;
      this.a(0.0F, 0.0F, 0.0F, var8, 1.0F, 1.0F);
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var8);
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      this.a(1.0F - var8, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      this.a(0.0F, 0.0F, 1.0F - var8, 1.0F, 1.0F, 1.0F);
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
      int var10 = Facing.oppositeSide[par5];
      if (var10 == 1) {
         var10 = 0;
      }

      return var10;
   }

   @Override
   public TileEntity createNewTileEntity(World par1World) {
      return new TileEntityHopper();
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
      super.a(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);
      if (par6ItemStack.hasDisplayName()) {
         TileEntityHopper var7 = getHopperTile(par1World, par2, par3, par4);
         var7.setInventoryName(par6ItemStack.getDisplayName());
      }
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
      this.updateMetadata(par1World, par2, par3, par4);
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if (par1World.isRemote) {
         return true;
      } else {
         TileEntityHopper var10 = getHopperTile(par1World, par2, par3, par4);
         if (var10 != null) {
            par5EntityPlayer.displayGUIHopper(var10);
         }

         return true;
      }
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      this.updateMetadata(par1World, par2, par3, par4);
   }

   private void updateMetadata(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockMetadata(par2, par3, par4);
      int var6 = getDirectionFromMetadata(var5);
      boolean var7 = !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
      boolean var8 = getIsBlockNotPoweredFromMetadata(var5);
      if (var7 != var8) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | (var7 ? 0 : 8), 4);
      }
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
      TileEntityHopper var7 = (TileEntityHopper)par1World.getBlockTileEntity(par2, par3, par4);
      if (var7 != null) {
         for (int var8 = 0; var8 < var7.getSizeInventory(); var8++) {
            ItemStack var9 = var7.getStackInSlot(var8);
            if (var9 != null) {
               float var10 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;
               float var11 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;
               float var12 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;

               while (var9.stackSize > 0) {
                  int var13 = this.field_94457_a.nextInt(21) + 10;
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
                  var14.motionX = (float)this.field_94457_a.nextGaussian() * var15;
                  var14.motionY = (float)this.field_94457_a.nextGaussian() * var15 + 0.2F;
                  var14.motionZ = (float)this.field_94457_a.nextGaussian() * var15;
                  par1World.spawnEntityInWorld(var14);
               }
            }
         }

         par1World.func_96440_m(par2, par3, par4, par5);
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   @Override
   public int getRenderType() {
      return 38;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return par1 == 1 ? this.hopperTopIcon : this.hopperIcon;
   }

   public static int getDirectionFromMetadata(int par0) {
      return par0 & 7;
   }

   public static boolean getIsBlockNotPoweredFromMetadata(int par0) {
      return (par0 & 8) != 8;
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory(getHopperTile(par1World, par2, par3, par4));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.hopperIcon = par1IconRegister.registerIcon("hopper");
      this.hopperTopIcon = par1IconRegister.registerIcon("hopper_top");
      this.hopperInsideIcon = par1IconRegister.registerIcon("hopper_inside");
   }

   @Environment(EnvType.CLIENT)
   public static Icon getHopperIcon(String par0Str) {
      return par0Str == "hopper" ? Block.hopperBlock.hopperIcon : (par0Str == "hopper_inside" ? Block.hopperBlock.hopperInsideIcon : null);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getItemIconName() {
      return "hopper";
   }

   public static TileEntityHopper getHopperTile(IBlockAccess par0IBlockAccess, int par1, int par2, int par3) {
      return (TileEntityHopper)par0IBlockAccess.getBlockTileEntity(par1, par2, par3);
   }
}
