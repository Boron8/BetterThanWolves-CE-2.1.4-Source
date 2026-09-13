package net.minecraft.src;

class EntityMinecartMobSpawnerLogic extends MobSpawnerBaseLogic {
   EntityMinecartMobSpawnerLogic(EntityMinecartMobSpawner var1) {
      this.spawnerMinecart = var1;
   }

   @Override
   public void func_98267_a(int var1) {
      this.spawnerMinecart.worldObj.setEntityState(this.spawnerMinecart, (byte)var1);
   }

   @Override
   public World getSpawnerWorld() {
      return this.spawnerMinecart.worldObj;
   }

   @Override
   public int getSpawnerX() {
      return MathHelper.floor_double(this.spawnerMinecart.posX);
   }

   @Override
   public int getSpawnerY() {
      return MathHelper.floor_double(this.spawnerMinecart.posY);
   }

   @Override
   public int getSpawnerZ() {
      return MathHelper.floor_double(this.spawnerMinecart.posZ);
   }
}
