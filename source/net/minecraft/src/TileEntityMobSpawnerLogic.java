package net.minecraft.src;

class TileEntityMobSpawnerLogic extends MobSpawnerBaseLogic {
   TileEntityMobSpawnerLogic(TileEntityMobSpawner var1) {
      this.mobSpawnerEntity = var1;
   }

   @Override
   public void func_98267_a(int var1) {
      this.mobSpawnerEntity
         .worldObj
         .addBlockEvent(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord, Block.mobSpawner.blockID, var1, 0);
   }

   @Override
   public World getSpawnerWorld() {
      return this.mobSpawnerEntity.worldObj;
   }

   @Override
   public int getSpawnerX() {
      return this.mobSpawnerEntity.xCoord;
   }

   @Override
   public int getSpawnerY() {
      return this.mobSpawnerEntity.yCoord;
   }

   @Override
   public int getSpawnerZ() {
      return this.mobSpawnerEntity.zCoord;
   }

   @Override
   public void setRandomMinecart(WeightedRandomMinecart var1) {
      super.setRandomMinecart(var1);
      if (this.getSpawnerWorld() != null) {
         this.getSpawnerWorld().markBlockForUpdate(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord);
      }
   }
}
