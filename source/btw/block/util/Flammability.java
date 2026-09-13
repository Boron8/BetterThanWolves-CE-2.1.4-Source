package btw.block.util;

public enum Flammability {
   NONE(0, 0),
   MINIMAL(5, 5),
   MEDIUM(5, 20),
   HIGH(30, 60),
   EXTREME(60, 100),
   QUICKCATCHSLOWBURN(15, 100),
   MEDIUMCATCHQUICKBURN(30, 20);

   public static final Flammability LOGS = MINIMAL;
   public static final Flammability PLANKS = MEDIUM;
   public static final Flammability LEAVES = HIGH;
   public static final Flammability CLOTH = HIGH;
   public static final Flammability CROPS = HIGH;
   public static final Flammability GRASS = EXTREME;
   public static final Flammability WICKER = EXTREME;
   public static final Flammability EXPLOSIVES = QUICKCATCHSLOWBURN;
   public static final Flammability VINES = QUICKCATCHSLOWBURN;
   public static final Flammability BOOKSHELVES = MEDIUMCATCHQUICKBURN;
   public final int chanceToEncourageFire;
   public final int abilityToCatchFire;

   private Flammability(int iChanceToEncourageFire, int iAbilityToCatchFire) {
      this.chanceToEncourageFire = iChanceToEncourageFire;
      this.abilityToCatchFire = iAbilityToCatchFire;
   }
}
