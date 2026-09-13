package btw.block.blocks;

import net.minecraft.src.EnumMobType;
import net.minecraft.src.Material;

public class PressurePlateBlockStone extends PressurePlateBlock {
   public PressurePlateBlockStone(int iBlockID) {
      super(iBlockID, "stone", Material.rock, EnumMobType.mobs);
      this.c(1.5F);
      this.a(j);
      this.c("pressurePlate");
   }
}
