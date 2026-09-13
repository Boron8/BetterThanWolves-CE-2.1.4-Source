package btw.item.items;

public class FoulFoodItem extends FoodItem {
   private static final int HEALTH_HEALED = 1;
   private static final float SATURATION_MODIFIER = 0.0F;

   public FoulFoodItem(int iItemID) {
      super(iItemID, 1, 0.0F, false, "fcItemFoulFood");
      this.setIncreasedFoodPoisoningEffect();
   }
}
