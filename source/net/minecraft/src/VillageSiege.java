package net.minecraft.src;

public class VillageSiege {
   private World worldObj;
   private boolean field_75535_b = false;
   private int field_75536_c = -1;
   private int field_75533_d;
   private int field_75534_e;
   private Village theVillage;
   private int field_75532_g;
   private int field_75538_h;
   private int field_75539_i;

   public VillageSiege(World par1World) {
      this.worldObj = par1World;
   }

   public void tick() {
      boolean var1 = false;
      if (var1) {
         if (this.field_75536_c == 2) {
            this.field_75533_d = 100;
            return;
         }
      } else {
         if (this.worldObj.isDaytime()) {
            this.field_75536_c = 0;
            return;
         }

         if (this.field_75536_c == 2) {
            return;
         }

         if (this.field_75536_c == 0) {
            float var2 = this.worldObj.getCelestialAngle(0.0F);
            if (var2 < 0.5 || var2 > 0.501) {
               return;
            }

            this.field_75536_c = this.worldObj.rand.nextInt(10) == 0 ? 1 : 2;
            this.field_75535_b = false;
            if (this.field_75536_c == 2) {
               return;
            }
         }
      }

      if (!this.field_75535_b) {
         if (!this.func_75529_b()) {
            return;
         }

         this.field_75535_b = true;
      }

      if (this.field_75534_e > 0) {
         this.field_75534_e--;
      } else {
         this.field_75534_e = 2;
         if (this.field_75533_d > 0) {
            this.spawnZombie();
            this.field_75533_d--;
         } else {
            this.field_75536_c = 2;
         }
      }
   }

   private boolean func_75529_b() {
      for (EntityPlayer var3 : this.worldObj.playerEntities) {
         this.theVillage = this.worldObj.villageCollectionObj.findNearestVillage((int)var3.posX, (int)var3.posY, (int)var3.posZ, 1);
         if (this.theVillage != null
            && this.theVillage.getNumVillageDoors() >= 10
            && this.theVillage.getTicksSinceLastDoorAdding() >= 20
            && this.theVillage.getNumVillagers() >= 20) {
            ChunkCoordinates var4 = this.theVillage.getCenter();
            float var5 = this.theVillage.getVillageRadius();
            boolean var6 = false;

            for (int var7 = 0; var7 < 10; var7++) {
               this.field_75532_g = var4.posX + (int)(MathHelper.cos(this.worldObj.rand.nextFloat() * (float) Math.PI * 2.0F) * var5 * 0.9);
               this.field_75538_h = var4.posY;
               this.field_75539_i = var4.posZ + (int)(MathHelper.sin(this.worldObj.rand.nextFloat() * (float) Math.PI * 2.0F) * var5 * 0.9);
               var6 = false;

               for (Village var9 : this.worldObj.villageCollectionObj.getVillageList()) {
                  if (var9 != this.theVillage && var9.isInRange(this.field_75532_g, this.field_75538_h, this.field_75539_i)) {
                     var6 = true;
                     break;
                  }
               }

               if (!var6) {
                  break;
               }
            }

            if (var6) {
               return false;
            }

            Vec3 var10 = this.func_75527_a(this.field_75532_g, this.field_75538_h, this.field_75539_i);
            if (var10 != null) {
               this.field_75534_e = 0;
               this.field_75533_d = 20;
               return true;
            }
         }
      }

      return false;
   }

   private boolean spawnZombie() {
      Vec3 var1 = this.func_75527_a(this.field_75532_g, this.field_75538_h, this.field_75539_i);
      if (var1 == null) {
         return false;
      } else {
         EntityZombie var2;
         try {
            var2 = (EntityZombie)EntityList.createEntityOfType(EntityZombie.class, this.worldObj);
            var2.initCreature();
            var2.setVillager(false);
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }

         var2.b(var1.xCoord, var1.yCoord, var1.zCoord, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
         this.worldObj.spawnEntityInWorld(var2);
         ChunkCoordinates var3 = this.theVillage.getCenter();
         var2.b(var3.posX, var3.posY, var3.posZ, this.theVillage.getVillageRadius());
         return true;
      }
   }

   private Vec3 func_75527_a(int par1, int par2, int par3) {
      for (int var4 = 0; var4 < 10; var4++) {
         int var5 = par1 + this.worldObj.rand.nextInt(16) - 8;
         int var6 = par2 + this.worldObj.rand.nextInt(6) - 3;
         int var7 = par3 + this.worldObj.rand.nextInt(16) - 8;
         if (this.theVillage.isInRange(var5, var6, var7)
            && SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, this.worldObj, var5, var6, var7)) {
            this.worldObj.getWorldVec3Pool().getVecFromPool(var5, var6, var7);
         }
      }

      return null;
   }
}
