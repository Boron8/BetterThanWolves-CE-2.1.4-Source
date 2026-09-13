package btw.block.material;

import net.minecraft.src.MapColor;
import net.minecraft.src.Material;

public class NetherGrothMaterial extends Material {
   public NetherGrothMaterial(MapColor mapColor) {
      super(mapColor);
      this.n();
   }

   @Override
   public boolean blocksMovement() {
      return false;
   }
}
