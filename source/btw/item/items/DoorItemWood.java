package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;

public class DoorItemWood extends DoorItem {
   public DoorItemWood(int iITemID) {
      super(iITemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.b("doorWood");
   }

   @Override
   public Block getDoorBlock() {
      return BTWBlocks.woodenDoor;
   }
}
