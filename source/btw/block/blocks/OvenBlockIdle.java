package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IBlockAccess;

public class OvenBlockIdle extends OvenBlock {
   public OvenBlockIdle(int iBlockID) {
      super(iBlockID, false);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
      return this.isRenderingInterior ? 10066329 : super.c(blockAccess, x, y, z);
   }
}
