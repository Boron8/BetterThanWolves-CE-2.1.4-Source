package btw.entity.mob.behavior;

import btw.entity.mob.villager.VillagerEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class VillagerBreedBehavior extends EntityAIBase {
   static final double DISTANCE_TO_CHECK_FOR_MATE = 8.0;
   private VillagerEntity villager;
   private VillagerEntity mate;
   private World world;
   private int spawnBabyDelay = 0;
   private int thrustDelay = 0;

   public VillagerBreedBehavior(VillagerEntity villager) {
      this.villager = villager;
      this.world = villager.worldObj;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.villager.getInLove() <= 0) {
         return false;
      } else {
         this.mate = this.getNearbyMate();
         return this.mate != null;
      }
   }

   @Override
   public void resetTask() {
      this.mate = null;
      this.spawnBabyDelay = 0;
   }

   @Override
   public boolean continueExecuting() {
      return this.mate != null && this.mate.R() && this.mate.getInLove() > 0 && this.spawnBabyDelay < 100;
   }

   @Override
   public void updateTask() {
      this.villager.az().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if (this.villager.e(this.mate) > 4.0) {
         this.villager.aC().tryMoveToEntityLiving(this.mate, 0.25F);
         this.spawnBabyDelay = 0;
         this.thrustDelay = this.villager.rand.nextInt(5) + 15;
      } else {
         this.spawnBabyDelay++;
         if (this.spawnBabyDelay == 100) {
            this.giveBirth();
         } else {
            this.thrustDelay--;
            if (this.thrustDelay <= 0) {
               this.world
                  .playSoundAtEntity(this.villager, "random.classic_hurt", 1.0F + this.villager.rand.nextFloat() * 0.25F, this.villager.getSoundPitch() * 2.0F);
               this.thrustDelay = this.villager.rand.nextInt(5) + 15;
               if (this.villager.onGround) {
                  this.villager.bl();
               }
            }
         }
      }
   }

   private void giveBirth() {
      int babyProfession = this.villager.getProfessionFromClass();
      if (this.villager.rand.nextInt(2) == 0) {
         babyProfession = this.mate.getProfessionFromClass();
      }

      if (this.villager.rand.nextInt(3) == 0) {
         int casteID = VillagerEntity.getCasteFromProfession(babyProfession);
         ArrayList<Integer> caste = VillagerEntity.casteMap.get(casteID);
         ArrayList<Integer> casteCopy = new ArrayList<>();

         for (int i : caste) {
            casteCopy.add(i);
         }

         casteCopy.remove(Integer.valueOf(babyProfession));
         if (casteCopy.size() > 0) {
            int index = this.villager.rand.nextInt(casteCopy.size());
            babyProfession = casteCopy.get(index);
         }
      }

      VillagerEntity babyVillager = this.villager.spawnBabyVillagerWithProfession(this.mate, babyProfession);
      this.mate.a(6000);
      this.villager.a(6000);
      this.mate.setInLove(0);
      this.villager.setInLove(0);
      babyVillager.a(-babyVillager.getTicksForChildToGrow());
      babyVillager.b(this.villager.posX, this.villager.posY, this.villager.posZ, 0.0F, 0.0F);
      this.world.spawnEntityInWorld(babyVillager);
      this.world.setEntityState(babyVillager, (byte)12);
      this.world
         .playAuxSFX(
            2222, MathHelper.floor_double(babyVillager.posX), MathHelper.floor_double(babyVillager.posY), MathHelper.floor_double(babyVillager.posZ), 0
         );
   }

   private VillagerEntity getNearbyMate() {
      List potentialMateList = this.world.getEntitiesWithinAABB(VillagerEntity.class, this.villager.boundingBox.expand(8.0, 8.0, 8.0));
      Iterator mateIterator = potentialMateList.iterator();
      VillagerEntity foundMate = null;

      while (mateIterator.hasNext()) {
         VillagerEntity tempVillager = (VillagerEntity)mateIterator.next();
         if (this.canMateWith(tempVillager)) {
            return tempVillager;
         }
      }

      return null;
   }

   private boolean canMateWith(VillagerEntity targetVillager) {
      return targetVillager != this.villager && targetVillager.getInLove() > 0 && !targetVillager.isLivingDead;
   }
}
