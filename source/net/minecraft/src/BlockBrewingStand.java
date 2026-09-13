package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockBrewingStand extends BlockContainer {
   private Random rand = new Random();
   @Environment(EnvType.CLIENT)
   private Icon theIcon;

   public BlockBrewingStand(int par1) {
      super(par1, Material.iron);
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public int getRenderType() {
      return 25;
   }

   @Override
   public TileEntity createNewTileEntity(World par1World) {
      return new TileEntityBrewingStand();
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
      this.a(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      this.setBlockBoundsForItemRender();
      super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
   }

   @Override
   public void setBlockBoundsForItemRender() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if (par1World.isRemote) {
         return true;
      } else {
         TileEntityBrewingStand var10 = (TileEntityBrewingStand)par1World.getBlockTileEntity(par2, par3, par4);
         if (var10 != null) {
            par5EntityPlayer.displayGUIBrewingStand(var10);
         }

         return true;
      }
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
      if (par6ItemStack.hasDisplayName()) {
         ((TileEntityBrewingStand)par1World.getBlockTileEntity(par2, par3, par4)).func_94131_a(par6ItemStack.getDisplayName());
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      double var6 = par2 + 0.4F + par5Random.nextFloat() * 0.2F;
      double var8 = par3 + 0.7F + par5Random.nextFloat() * 0.3F;
      double var10 = par4 + 0.4F + par5Random.nextFloat() * 0.2F;
      par1World.spawnParticle("smoke", var6, var8, var10, 0.0, 0.0, 0.0);
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
      TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);
      if (var7 instanceof TileEntityBrewingStand) {
         TileEntityBrewingStand var8 = (TileEntityBrewingStand)var7;

         for (int var9 = 0; var9 < var8.getSizeInventory(); var9++) {
            ItemStack var10 = var8.getStackInSlot(var9);
            if (var10 != null) {
               float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
               float var12 = this.rand.nextFloat() * 0.8F + 0.1F;
               float var13 = this.rand.nextFloat() * 0.8F + 0.1F;

               while (var10.stackSize > 0) {
                  int var14 = this.rand.nextInt(21) + 10;
                  if (var14 > var10.stackSize) {
                     var14 = var10.stackSize;
                  }

                  var10.stackSize -= var14;
                  EntityItem var15 = (EntityItem)EntityList.createEntityOfType(
                     EntityItem.class,
                     par1World,
                     (double)(par2 + var11),
                     (double)(par3 + var12),
                     (double)(par4 + var13),
                     new ItemStack(var10.itemID, var14, var10.getItemDamage())
                  );
                  float var16 = 0.05F;
                  var15.motionX = (float)this.rand.nextGaussian() * var16;
                  var15.motionY = (float)this.rand.nextGaussian() * var16 + 0.2F;
                  var15.motionZ = (float)this.rand.nextGaussian() * var16;
                  par1World.spawnEntityInWorld(var15);
               }
            }
         }
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return Item.brewingStand.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return Item.brewingStand.itemID;
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
   public void registerIcons(IconRegister par1IconRegister) {
      super.a(par1IconRegister);
      this.theIcon = par1IconRegister.registerIcon("brewingStand_base");
   }

   @Environment(EnvType.CLIENT)
   public Icon getBrewingStandIcon() {
      return this.theIcon;
   }
}
