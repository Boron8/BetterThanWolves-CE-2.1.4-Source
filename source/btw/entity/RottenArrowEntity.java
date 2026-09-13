package btw.entity;

import btw.item.BTWItems;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet23VehicleSpawn;
import net.minecraft.src.World;

public class RottenArrowEntity extends EntityArrow implements EntityWithCustomPacket {
   private static final float DAMAGE_MULTIPLIER = 1.0F;
   private static final int VEHICLE_SPAWN_PACKET_TYPE = 104;

   public RottenArrowEntity(World world) {
      super(world);
   }

   public RottenArrowEntity(World world, double d, double d1, double d2) {
      super(world, d, d1, d2);
   }

   public RottenArrowEntity(World world, EntityLiving entityLiving, float f) {
      super(world, entityLiving, f);
      this.canBePickedUp = 2;
   }

   public RottenArrowEntity(World world, EntityLiving firingEntity, EntityLiving targetEntity, float par4, float par5) {
      super(world, firingEntity, targetEntity, par4, par5);
   }

   @Override
   protected float getDamageMultiplier() {
      return 1.0F;
   }

   @Override
   protected boolean addArrowToPlayerInv(EntityPlayer player) {
      return false;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.isDead && this.inGround) {
         for (int i = 0; i < 32; i++) {
            this.worldObj
               .spawnParticle(
                  "iconcrack_333",
                  this.posX,
                  this.posY,
                  this.posZ,
                  (float)(Math.random() * 2.0 - 1.0) * 0.4F,
                  (float)(Math.random() * 2.0 - 1.0) * 0.4F,
                  (float)(Math.random() * 2.0 - 1.0) * 0.4F
               );
         }

         this.w();
      }
   }

   @Override
   public Item getCorrespondingItem() {
      return BTWItems.rottenArrow;
   }

   @Override
   public boolean canHopperCollect() {
      return false;
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
      return 104;
   }
}
