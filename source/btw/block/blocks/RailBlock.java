package btw.block.blocks;

import net.minecraft.src.BlockRail;

public class RailBlock extends BlockRail {
   public RailBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.7F);
      this.setPicksEffectiveOn();
      this.a(k);
      this.c("rail");
   }
}
