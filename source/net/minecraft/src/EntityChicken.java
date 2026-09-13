package net.minecraft.src;

public class EntityChicken extends EntityAnimal {
   public boolean field_70885_d = false;
   public float field_70886_e = 0.0F;
   public float destPos = 0.0F;
   public float field_70884_g;
   public float field_70888_h;
   public float field_70889_i = 1.0F;
   public int timeUntilNextEgg;

   public EntityChicken(World par1World) {
      super(par1World);
      this.texture = "/mob/chicken.png";
      this.a(0.3F, 0.7F);
      this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
      float var2 = 0.25F;
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
      this.tasks.addTask(2, new EntityAIMate(this, var2));
      this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.seeds.itemID, false));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 0.28F));
      this.tasks.addTask(5, new EntityAIWander(this, var2));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
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
      super.onLivingUpdate();
      this.field_70888_h = this.field_70886_e;
      this.field_70884_g = this.destPos;
      this.destPos = (float)(this.destPos + (this.onGround ? -1 : 4) * 0.3);
      if (this.destPos < 0.0F) {
         this.destPos = 0.0F;
      }

      if (this.destPos > 1.0F) {
         this.destPos = 1.0F;
      }

      if (!this.onGround && this.field_70889_i < 1.0F) {
         this.field_70889_i = 1.0F;
      }

      this.field_70889_i = (float)(this.field_70889_i * 0.9);
      if (!this.onGround && this.motionY < 0.0) {
         this.motionY *= 0.6;
      }

      this.field_70886_e = this.field_70886_e + this.field_70889_i * 2.0F;
      if (!this.h_() && !this.worldObj.isRemote && --this.timeUntilNextEgg <= 0) {
         this.a("mob.chicken.plop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.b(Item.egg.itemID, 1);
         this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
      }
   }

   @Override
   protected void fall(float par1) {
   }

   @Override
   protected String getLivingSound() {
      return "mob.chicken.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.chicken.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.chicken.hurt";
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.chicken.step", 0.15F, 1.0F);
   }

   @Override
   protected int getDropItemId() {
      return Item.feather.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);

      for (int var4 = 0; var4 < var3; var4++) {
         this.b(Item.feather.itemID, 1);
      }

      if (this.ae()) {
         this.b(Item.chickenCooked.itemID, 1);
      } else {
         this.b(Item.chickenRaw.itemID, 1);
      }
   }

   public EntityChicken spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      return (EntityChicken)EntityList.createEntityOfType(EntityChicken.class, this.worldObj);
   }

   @Override
   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return par1ItemStack != null && par1ItemStack.getItem() instanceof ItemSeeds;
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.spawnBabyAnimal(par1EntityAgeable);
   }
}
