package btw.block.blocks;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockHopper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class VanillaHopperBlock extends BlockHopper {
   public VanillaHopperBlock(int iBlockID) {
      super(iBlockID);
      this.c(3.0F);
      this.b(8.0F);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.a(g);
      this.c("hopper");
      this.a(null);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      this.b(world, i, j, k).addToListIfIntersects(intersectingBox, list);
   }

   @Override
   public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
      return par9;
   }

   @Override
   public TileEntity createNewTileEntity(World par1World) {
      return null;
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      return false;
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
   }

   @Override
   public int getRenderType() {
      return 0;
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return false;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon("fcBlockStub");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getItemIconName() {
      return null;
   }
}
