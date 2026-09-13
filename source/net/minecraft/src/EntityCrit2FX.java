package net.minecraft.src;

public class EntityCrit2FX extends EntityFX {
   private Entity theEntity;
   private int currentLife = 0;
   private int maximumLife = 0;
   private String particleName;

   public EntityCrit2FX(World var1, Entity var2) {
      this(var1, var2, "crit");
   }

   public EntityCrit2FX(World var1, Entity var2, String var3) {
      super(var1, var2.posX, var2.boundingBox.minY + var2.height / 2.0F, var2.posZ, var2.motionX, var2.motionY, var2.motionZ);
      this.theEntity = var2;
      this.maximumLife = 3;
      this.particleName = var3;
      this.onUpdate();
   }

   @Override
   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
   }

   @Override
   public void onUpdate() {
      for (int var1 = 0; var1 < 16; var1++) {
         double var2 = this.rand.nextFloat() * 2.0F - 1.0F;
         double var4 = this.rand.nextFloat() * 2.0F - 1.0F;
         double var6 = this.rand.nextFloat() * 2.0F - 1.0F;
         if (!(var2 * var2 + var4 * var4 + var6 * var6 > 1.0)) {
            double var8 = this.theEntity.posX + var2 * this.theEntity.width / 4.0;
            double var10 = this.theEntity.boundingBox.minY + this.theEntity.height / 2.0F + var4 * this.theEntity.height / 4.0;
            double var12 = this.theEntity.posZ + var6 * this.theEntity.width / 4.0;
            this.worldObj.spawnParticle(this.particleName, var8, var10, var12, var2, var4 + 0.2, var6);
         }
      }

      this.currentLife++;
      if (this.currentLife >= this.maximumLife) {
         this.w();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }
}
