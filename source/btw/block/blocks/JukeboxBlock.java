package btw.block.blocks;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.BlockJukeBox;

public class JukeboxBlock extends BlockJukeBox {
   public JukeboxBlock(int iBlockID) {
      super(iBlockID);
      this.c(1.5F);
      this.b(10.0F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.WOOD_BASED_BLOCK);
      this.a(j);
      this.c("jukebox");
   }
}
