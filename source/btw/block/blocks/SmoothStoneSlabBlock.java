package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class SmoothStoneSlabBlock extends BlockHalfSlab {
   private Icon[] sideIcon = new Icon[3];
   private Icon[] topIcon = new Icon[3];

   public SmoothStoneSlabBlock(int iBlockID, boolean bDoubleSlab) {
      super(iBlockID, bDoubleSlab, Material.rock);
      this.setPicksEffectiveOn();
      this.c(2.0F);
      this.b(10.0F);
      this.a(j);
      this.c("stoneSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.stoneSlab.blockID;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.getStrata(metadata);
   }

   @Override
   public String getFullSlabName(int iMetadata) {
      return super.a() + ".stone";
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      return new ItemStack(BTWBlocks.stoneSlab.blockID, 2, this.getStrata(iMetadata));
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      int numItems = this.isDoubleSlab ? 2 : 1;
      this.dropItemsIndividually(world, i, j, k, BTWBlocks.looseCobblestoneSlab.blockID, numItems, this.getStrata(iMetadata) << 2, fChanceOfDrop);
      return true;
   }

   @Override
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return BTWBlocks.stoneSlab.blockID;
   }

   @Override
   public int getDamageValue(World par1World, int x, int y, int z) {
      return this.getStrata(par1World, x, y, z);
   }

   @Override
   public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      if (!this.isDoubleSlab && iFacing < 2) {
         boolean bIsUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         return bIsUpsideDown == (iFacing == 1);
      } else {
         return true;
      }
   }

   @Override
   public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      return this.isDoubleSlab || bIsSlabUpsideDown == this.getIsUpsideDown(blockAccess, i, j, k);
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return iMetadata & 3;
   }

   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      if (!this.isDoubleSlab) {
         for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(iBlockID, 1, i));
         }
      }
   }

   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stoneslab_top");
      this.topIcon[0] = this.blockIcon;
      this.topIcon[1] = register.registerIcon("stoneslab_top_1");
      this.topIcon[2] = register.registerIcon("stoneslab_top_2");
      this.sideIcon[0] = register.registerIcon("stoneslab_side");
      this.sideIcon[1] = register.registerIcon("stoneslab_side_1");
      this.sideIcon[2] = register.registerIcon("stoneslab_side_2");
   }

   @Override
   public Icon getIcon(int side, int metadata) {
      if (this.isDoubleSlab && (metadata & 8) != 0) {
         side = 1;
      }

      return side != 1 && side != 0 ? this.sideIcon[this.getStrata(metadata)] : this.topIcon[this.getStrata(metadata)];
   }
}
