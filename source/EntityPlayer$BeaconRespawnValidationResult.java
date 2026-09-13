import net.minecraft.src.ChunkCoordinates;

public class EntityPlayer$BeaconRespawnValidationResult {
   public EntityPlayer$BeaconRespawnValidationResult.BeaconStatus beaconStatus;
   public ChunkCoordinates coords;

   public void setCoords(ChunkCoordinates coords) {
      this.coords = coords;
   }

   public static enum BeaconStatus {
      VALID(0),
      MISSING(2),
      OUT_OF_RANGE(3),
      OBSTRUCTED(4);

      public final int id;

      private BeaconStatus(int id) {
         this.id = id;
      }
   }
}
