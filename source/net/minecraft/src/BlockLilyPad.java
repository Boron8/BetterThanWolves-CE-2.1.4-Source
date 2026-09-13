package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeBlock;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockLilyPad extends BlockFlower {
   protected BlockLilyPad(int par1) {
      super(par1);
      float var2 = 0.5F;
      float var3 = 0.015625F;
      this.a(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var3, 0.5F + var2);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public int getRenderType() {
      return 23;
   }

   @Override
   public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
      if (par7Entity == null || !(par7Entity instanceof EntityBoat)) {
         super.a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return AxisAlignedBB.getAABBPool().getAABB(par2 + this.minX, par3 + this.minY, par4 + this.minZ, par2 + this.maxX, par3 + this.maxY, par4 + this.maxZ);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBlockColor() {
      return ColorizeBlock.colorizeBlock(this) ? ColorizeBlock.blockColor : 2129968;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderColor(int par1) {
      return ColorizeBlock.colorizeBlock(this, par1) ? ColorizeBlock.blockColor : 2129968;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return ColorizeBlock.colorizeBlock(this, par1IBlockAccess, par2, par3, par4) ? ColorizeBlock.blockColor : 2129968;
   }

   protected boolean canThisPlantGrowOnThisBlockID(int par1) {
      return par1 == Block.waterStill.blockID;
   }

   @Override
   public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
      return par3 >= 0 && par3 < 256
         ? par1World.getBlockMaterial(par2, par3 - 1, par4) == Material.water && par1World.getBlockMetadata(par2, par3 - 1, par4) == 0
         : false;
   }
}
