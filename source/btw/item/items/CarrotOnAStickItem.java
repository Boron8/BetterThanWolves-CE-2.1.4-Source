package btw.item.items;

import net.minecraft.src.ItemCarrotOnAStick;

public class CarrotOnAStickItem extends ItemCarrotOnAStick {
   public CarrotOnAStickItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setFilterableProperties(4);
      this.setAsBasicPigFood();
      this.b("carrotOnAStick");
   }
}
