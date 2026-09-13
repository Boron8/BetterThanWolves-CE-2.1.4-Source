package net.minecraft.src;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntitySheep extends EntityAnimal {
   private final InventoryCrafting field_90016_e = new InventoryCrafting(new ContainerSheep(this), 2, 1);
   public static final float[][] fleeceColorTable = new float[][]{
      {1.0F, 1.0F, 1.0F},
      {0.85F, 0.5F, 0.2F},
      {0.7F, 0.3F, 0.85F},
      {0.4F, 0.6F, 0.85F},
      {0.9F, 0.9F, 0.2F},
      {0.5F, 0.8F, 0.1F},
      {0.95F, 0.5F, 0.65F},
      {0.3F, 0.3F, 0.3F},
      {0.6F, 0.6F, 0.6F},
      {0.3F, 0.5F, 0.6F},
      {0.5F, 0.25F, 0.7F},
      {0.2F, 0.3F, 0.7F},
      {0.4F, 0.3F, 0.2F},
      {0.4F, 0.5F, 0.2F},
      {0.6F, 0.2F, 0.2F},
      {0.1F, 0.1F, 0.1F}
   };
   private int sheepTimer;
   private EntityAIEatGrass aiEatGrass = new EntityAIEatGrass(this);

   public EntitySheep(World par1World) {
      super(par1World);
      this.texture = "/mob/sheep.png";
      this.a(0.9F, 1.3F);
      float var2 = 0.23F;
      this.aC().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
      this.tasks.addTask(2, new EntityAIMate(this, var2));
      this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.itemID, false));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 0.25F));
      this.tasks.addTask(5, this.aiEatGrass);
      this.tasks.addTask(6, new EntityAIWander(this, var2));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.field_90016_e.setInventorySlotContents(0, new ItemStack(Item.dyePowder, 1, 0));
      this.field_90016_e.setInventorySlotContents(1, new ItemStack(Item.dyePowder, 1, 0));
   }

   @Override
   protected boolean isAIEnabled() {
      return true;
   }

   @Override
   protected void updateAITasks() {
      this.sheepTimer = this.aiEatGrass.getEatGrassTick();
      super.bo();
   }

   @Override
   public void onLivingUpdate() {
      if (this.worldObj.isRemote) {
         this.sheepTimer = Math.max(0, this.sheepTimer - 1);
      }

      super.onLivingUpdate();
   }

   @Override
   public int getMaxHealth() {
      return 8;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      if (!this.getSheared()) {
         this.a(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 0.0F);
      }
   }

   @Override
   protected int getDropItemId() {
      return Block.cloth.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 10) {
         this.sheepTimer = 40;
      } else {
         super.handleHealthUpdate(par1);
      }
   }

   @Environment(EnvType.CLIENT)
   public float func_70894_j(float par1) {
      return this.sheepTimer <= 0
         ? 0.0F
         : (
            this.sheepTimer >= 4 && this.sheepTimer <= 36
               ? 1.0F
               : (this.sheepTimer < 4 ? (this.sheepTimer - par1) / 4.0F : -(this.sheepTimer - 40 - par1) / 4.0F)
         );
   }

   @Environment(EnvType.CLIENT)
   public float func_70890_k(float par1) {
      if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
         float var2 = (this.sheepTimer - 4 - par1) / 32.0F;
         return (float) (Math.PI / 5) + 0.2199115F * MathHelper.sin(var2 * 28.7F);
      } else {
         return this.sheepTimer > 0 ? (float) (Math.PI / 5) : this.rotationPitch / (180.0F / (float)Math.PI);
      }
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
      if (var2 != null && var2.itemID == Item.shears.itemID && !this.getSheared() && !this.h_()) {
         if (!this.worldObj.isRemote) {
            this.setSheared(true);
            int var3 = 1 + this.rand.nextInt(3);

            for (int var4 = 0; var4 < var3; var4++) {
               EntityItem var5 = this.a(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
               var5.motionY = var5.motionY + this.rand.nextFloat() * 0.05F;
               var5.motionX = var5.motionX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
               var5.motionZ = var5.motionZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            }
         }

         var2.damageItem(1, par1EntityPlayer);
         this.a("mob.sheep.shear", 1.0F, 1.0F);
      }

      return super.interact(par1EntityPlayer);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("Sheared", this.getSheared());
      par1NBTTagCompound.setByte("Color", (byte)this.getFleeceColor());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setSheared(par1NBTTagCompound.getBoolean("Sheared"));
      this.setFleeceColor(par1NBTTagCompound.getByte("Color"));
   }

   @Override
   protected String getLivingSound() {
      return "mob.sheep.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.sheep.say";
   }

   @Override
   protected String getDeathSound() {
      return "mob.sheep.say";
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.sheep.step", 0.15F, 1.0F);
   }

   public int getFleeceColor() {
      return this.dataWatcher.getWatchableObjectByte(16) & 15;
   }

   public void setFleeceColor(int par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      this.dataWatcher.updateObject(16, (byte)(var2 & 240 | par1 & 15));
   }

   public boolean getSheared() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
   }

   public void setSheared(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 16));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -17));
      }
   }

   public static int getRandomFleeceColor(Random par0Random) {
      int var1 = par0Random.nextInt(100);
      return var1 < 5 ? 15 : (var1 < 10 ? 7 : (var1 < 15 ? 8 : (var1 < 18 ? 12 : (par0Random.nextInt(500) == 0 ? 6 : 0))));
   }

   public EntitySheep func_90015_b(EntityAgeable par1EntityAgeable) {
      EntitySheep var2 = (EntitySheep)par1EntityAgeable;
      EntitySheep var3 = (EntitySheep)EntityList.createEntityOfType(EntitySheep.class, this.worldObj);
      int var4 = this.func_90014_a(this, var2);
      var3.setFleeceColor(15 - var4);
      return var3;
   }

   @Override
   public void eatGrassBonus() {
      this.setSheared(false);
      if (this.h_()) {
         int var1 = this.b() + 1200;
         if (var1 > 0) {
            var1 = 0;
         }

         this.a(var1);
      }
   }

   @Override
   public void initCreature() {
      this.setFleeceColor(getRandomFleeceColor(this.worldObj.rand));
   }

   private int func_90014_a(EntityAnimal par1EntityAnimal, EntityAnimal par2EntityAnimal) {
      int var3 = this.func_90013_b(par1EntityAnimal);
      int var4 = this.func_90013_b(par2EntityAnimal);
      this.field_90016_e.getStackInSlot(0).setItemDamage(var3);
      this.field_90016_e.getStackInSlot(1).setItemDamage(var4);
      ItemStack var5 = CraftingManager.getInstance().findMatchingRecipe(this.field_90016_e, ((EntitySheep)par1EntityAnimal).worldObj);
      int var6;
      if (var5 != null && var5.getItem().itemID == Item.dyePowder.itemID) {
         var6 = var5.getItemDamage();
      } else {
         var6 = this.worldObj.rand.nextBoolean() ? var3 : var4;
      }

      return var6;
   }

   private int func_90013_b(EntityAnimal par1EntityAnimal) {
      return 15 - ((EntitySheep)par1EntityAnimal).getFleeceColor();
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.func_90015_b(par1EntityAgeable);
   }
}
