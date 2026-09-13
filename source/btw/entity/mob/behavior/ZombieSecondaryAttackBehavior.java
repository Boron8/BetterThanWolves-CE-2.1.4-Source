package btw.entity.mob.behavior;

import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityZombie;

public class ZombieSecondaryAttackBehavior extends EntityAIAttackOnCollide {
   private EntityZombie zombie;

   public ZombieSecondaryAttackBehavior(EntityZombie zombie) {
      super(zombie, EntityCreature.class, zombie.moveSpeed, true);
      this.zombie = zombie;
   }

   @Override
   public boolean continueExecuting() {
      EntityLiving var1 = this.attacker.getAttackTarget();
      return var1 != null && var1.isValidZombieSecondaryTarget(this.zombie) ? super.continueExecuting() : false;
   }
}
