package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockLeavesBase extends Block {
   public boolean graphicsLevel;

   public BlockLeavesBase(int par1, Material par2Material, boolean par3) {
      super(par1, par2Material);
      this.graphicsLevel = par3;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
      return !this.graphicsLevel && var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
   }
}
