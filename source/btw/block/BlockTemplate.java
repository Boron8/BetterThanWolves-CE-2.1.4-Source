package btw.block;

import btw.block.util.Flammability;
import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.Block;
import net.minecraft.src.Material;

public class BlockTemplate extends Block {
   protected BlockTemplate(int blockID, Material material) {
      super(blockID, Material.rock);
      this.c(1.0F);
      this.b(10.0F);
      this.setShovelsEffectiveOn(false);
      this.setPicksEffectiveOn(false);
      this.setAxesEffectiveOn(false);
      this.setChiselsEffectiveOn(false);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.setNonBuoyant();
      this.setFireProperties(Flammability.NONE);
      this.setFurnaceBurnTime(FurnaceBurnTime.NONE);
      this.setFilterableProperties(1);
      this.k(255);
      Block.useNeighborBrightness[blockID] = false;
      this.a(j);
      this.c("fcBlockTemplate");
   }
}
