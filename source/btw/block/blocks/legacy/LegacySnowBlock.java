package btw.block.blocks.legacy;

import net.minecraft.src.BlockSnowBlock;
import net.minecraft.src.World;

public class LegacySnowBlock extends BlockSnowBlock {
   public LegacySnowBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.2F);
      this.setShovelsEffectiveOn();
      this.setBuoyant();
      this.a(o);
      this.c("snow");
      this.a(null);
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }
}
