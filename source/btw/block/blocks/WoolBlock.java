package btw.block.blocks;

import btw.block.util.Flammability;
import net.minecraft.src.BlockCloth;

public class WoolBlock extends BlockCloth {
   public WoolBlock() {
      this.c(0.8F);
      this.setBuoyant();
      this.setFireProperties(Flammability.CLOTH);
      this.a(m);
      this.c("cloth");
   }
}
