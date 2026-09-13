package btw.block.blocks;

import net.minecraft.src.BlockGlowStone;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;

public class GlowStoneBlock extends BlockGlowStone {
   public GlowStoneBlock(int iBlockID) {
      super(iBlockID, Material.glass);
      this.c(0.6F);
      this.b(0.5F);
      this.setPicksEffectiveOn();
      this.a(1.0F);
      this.a(l);
      this.c("lightgem");
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return bIgnoreTransparency;
   }
}
