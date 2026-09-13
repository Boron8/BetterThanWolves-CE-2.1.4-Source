package btw.entity;

import btw.item.BTWItems;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet23VehicleSpawn;
import net.minecraft.src.World;

public class BroadheadArrowEntity extends EntityArrow implements EntityWithCustomPacket {
   private static final float BROADHEAD_DAMAGE_MULTIPLIER = 1.5F;
   private static final int VEHICLE_SPAWN_PACKET_TYPE = 101;

   public BroadheadArrowEntity(World world) {
      super(world);
   }

   public BroadheadArrowEntity(World world, double d, double d1, double d2) {
      super(world, d, d1, d2);
   }

   public BroadheadArrowEntity(World world, EntityLiving entityLiving, float f) {
      super(world, entityLiving, f);
   }

   @Override
   protected float getDamageMultiplier() {
      return 1.5F;
   }

   @Override
   public Item getCorrespondingItem() {
      return BTWItems.broadheadArrow;
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      return new Packet23VehicleSpawn(this, getVehicleSpawnPacketType(), this.shootingEntity == null ? this.entityId : this.shootingEntity.entityId);
   }

   @Override
   public int getTrackerViewDistance() {
      return 64;
   }

   @Override
   public int getTrackerUpdateFrequency() {
      return 20;
   }

   @Override
   public boolean getTrackMotion() {
      return false;
   }

   @Override
   public boolean shouldServerTreatAsOversized() {
      return false;
   }

   public static int getVehicleSpawnPacketType() {
      return 101;
   }
}
