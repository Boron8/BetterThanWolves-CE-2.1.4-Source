package btw.block.blocks;

import net.minecraft.src.BlockObsidian;

public class ObsidianBlock extends BlockObsidian {
   public ObsidianBlock(int iBlockID) {
      super(iBlockID);
      this.c(50.0F);
      this.b(2000.0F);
      this.a(j);
      this.c("obsidian");
   }

   @Override
   public int getMobilityFlag() {
      return 2;
   }
}
