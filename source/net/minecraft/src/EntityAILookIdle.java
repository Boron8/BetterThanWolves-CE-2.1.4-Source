package net.minecraft.src;

public class EntityAILookIdle extends EntityAIBase {
   private EntityLiving idleEntity;
   private double lookX;
   private double lookZ;
   private int idleTime = 0;

   public EntityAILookIdle(EntityLiving var1) {
      this.idleEntity = var1;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      return this.idleEntity.getRNG().nextFloat() < 0.02F;
   }

   @Override
   public boolean continueExecuting() {
      return this.idleTime >= 0;
   }

   @Override
   public void startExecuting() {
      double var1 = (Math.PI * 2) * this.idleEntity.getRNG().nextDouble();
      this.lookX = Math.cos(var1);
      this.lookZ = Math.sin(var1);
      this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
   }

   @Override
   public void updateTask() {
      this.idleTime--;
      this.idleEntity
         .getLookHelper()
         .setLookPosition(
            this.idleEntity.posX + this.lookX,
            this.idleEntity.posY + this.idleEntity.getEyeHeight(),
            this.idleEntity.posZ + this.lookZ,
            10.0F,
            this.idleEntity.getVerticalFaceSpeed()
         );
   }
}
