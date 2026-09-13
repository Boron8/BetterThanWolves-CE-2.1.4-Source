package btw.crafting.util;

public enum FurnaceBurnTime {
   NONE(0),
   DAMP_VEGETATION(10),
   KINDLING(25),
   SMALL_FUEL(50),
   WOOD_TOOLS,
   PLANKS_OAK(400),
   PLANKS_SPRUCE,
   PLANKS_BIRCH,
   PLANKS_JUNGLE,
   PLANKS_BLOOD,
   WOOD_BASED_BLOCK,
   COAL(1600),
   BLAZE_ROD(12800),
   LAVA_BUCKET(20000);

   public static final FurnaceBurnTime WOOL = KINDLING;
   public static final FurnaceBurnTime WOOL_KNIT = SMALL_FUEL;
   public static final FurnaceBurnTime SHAFT = SMALL_FUEL;
   public static final FurnaceBurnTime WICKER_PIECE = SMALL_FUEL;
   public final int burnTime;

   private FurnaceBurnTime(int iBurnTime) {
      this.burnTime = iBurnTime;
   }

   // $VF: Failed to inline enum fields
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   static {
      WOOD_TOOLS = new FurnaceBurnTime(SMALL_FUEL.burnTime * 4);
      PLANKS_SPRUCE = new FurnaceBurnTime(PLANKS_OAK.burnTime * 3 / 4);
      PLANKS_BIRCH = new FurnaceBurnTime(PLANKS_OAK.burnTime * 5 / 4);
      PLANKS_JUNGLE = new FurnaceBurnTime(PLANKS_OAK.burnTime / 2);
      PLANKS_BLOOD = new FurnaceBurnTime(PLANKS_OAK.burnTime / 2);
      WOOD_BASED_BLOCK = new FurnaceBurnTime(PLANKS_OAK.burnTime / 2);
   }
}
