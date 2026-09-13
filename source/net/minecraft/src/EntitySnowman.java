package net.minecraft.src;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
   public EntitySnowman(World par1World) {
      super(par1World);
      this.texture = "/mob/snowman.png";
      this.a(0.4F, 1.8F);
      this.aC().setAvoidsWater(true);
      this.tasks.addTask(1, new EntityAIArrowAttack(this, 0.25F, 20, 10.0F));
      this.tasks.addTask(2, new EntityAIWander(this, 0.2F));
      this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(4, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 16.0F, 0, true, false, IMob.mobSelector));
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public int getMaxHealth() {
      return 4;
   }

   @Override
   public void onLivingUpdate() {
      super.c();
      if (this.F()) {
         this.a(DamageSource.drown, 1);
      }

      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getBiomeGenForCoords(var1, var2).getFloatTemperature() > 1.0F) {
         this.a(DamageSource.onFire, 1);
      }

      for (int var5 = 0; var5 < 4; var5++) {
         var2 = MathHelper.floor_double(this.posX + (var5 % 2 * 2 - 1) * 0.25F);
         int var3 = MathHelper.floor_double(this.posY);
         int var4 = MathHelper.floor_double(this.posZ + (var5 / 2 % 2 * 2 - 1) * 0.25F);
         if (this.worldObj.getBlockId(var2, var3, var4) == 0
            && this.worldObj.getBiomeGenForCoords(var2, var4).getFloatTemperature() < 0.8F
            && Block.snow.canPlaceBlockAt(this.worldObj, var2, var3, var4)) {
            this.worldObj.setBlock(var2, var3, var4, Block.snow.blockID);
         }
      }
   }

   @Override
   protected int getDropItemId() {
      return Item.snowball.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.rand.nextInt(16);

      for (int var4 = 0; var4 < var3; var4++) {
         this.b(Item.snowball.itemID, 1);
      }
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
      EntitySnowball var3 = (EntitySnowball)EntityList.createEntityOfType(EntitySnowball.class, this.worldObj, this);
      double var4 = par1EntityLiving.posX - this.posX;
      double var6 = par1EntityLiving.posY + par1EntityLiving.getEyeHeight() - 1.1F - var3.posY;
      double var8 = par1EntityLiving.posZ - this.posZ;
      float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8) * 0.2F;
      var3.c(var4, var6 + var10, var8, 1.6F, 12.0F);
      this.a("random.bow", 1.0F, 1.0F / (this.aE().nextFloat() * 0.4F + 0.8F));
      this.worldObj.spawnEntityInWorld(var3);
   }
}
