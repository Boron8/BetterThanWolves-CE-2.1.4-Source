package net.minecraft.src;

public class EntityAIOcelotSit extends EntityAIBase {
   private final EntityOcelot theOcelot;
   private final float field_75404_b;
   private int currentTick = 0;
   private int field_75402_d = 0;
   private int maxSittingTicks = 0;
   private int sitableBlockX = 0;
   private int sitableBlockY = 0;
   private int sitableBlockZ = 0;

   public EntityAIOcelotSit(EntityOcelot var1, float var2) {
      this.theOcelot = var1;
      this.field_75404_b = var2;
      this.a(5);
   }

   @Override
   public boolean shouldExecute() {
      return this.theOcelot.m() && !this.theOcelot.n() && this.theOcelot.aE().nextDouble() <= 0.0065F && this.getNearbySitableBlockDistance();
   }

   @Override
   public boolean continueExecuting() {
      return this.currentTick <= this.maxSittingTicks
         && this.field_75402_d <= 60
         && this.isSittableBlock(this.theOcelot.worldObj, this.sitableBlockX, this.sitableBlockY, this.sitableBlockZ);
   }

   @Override
   public void startExecuting() {
      this.theOcelot.aC().tryMoveToXYZ(this.sitableBlockX + 0.5, (double)(this.sitableBlockY + 1), this.sitableBlockZ + 0.5, this.field_75404_b);
      this.currentTick = 0;
      this.field_75402_d = 0;
      this.maxSittingTicks = this.theOcelot.aE().nextInt(this.theOcelot.aE().nextInt(1200) + 1200) + 1200;
      this.theOcelot.q().setSitting(false);
   }

   @Override
   public void resetTask() {
      this.theOcelot.k(false);
   }

   @Override
   public void updateTask() {
      this.currentTick++;
      this.theOcelot.q().setSitting(false);
      if (this.theOcelot.e(this.sitableBlockX, this.sitableBlockY + 1, this.sitableBlockZ) > 1.0) {
         this.theOcelot.k(false);
         this.theOcelot.aC().tryMoveToXYZ(this.sitableBlockX + 0.5, (double)(this.sitableBlockY + 1), this.sitableBlockZ + 0.5, this.field_75404_b);
         this.field_75402_d++;
      } else if (!this.theOcelot.n()) {
         this.theOcelot.k(true);
      } else {
         this.field_75402_d--;
      }
   }

   private boolean getNearbySitableBlockDistance() {
      int var1 = (int)this.theOcelot.posY;
      double var2 = 2.147483647E9;

      for (int var4 = (int)this.theOcelot.posX - 8; var4 < this.theOcelot.posX + 8.0; var4++) {
         for (int var5 = (int)this.theOcelot.posZ - 8; var5 < this.theOcelot.posZ + 8.0; var5++) {
            if (this.isSittableBlock(this.theOcelot.worldObj, var4, var1, var5) && this.theOcelot.worldObj.isAirBlock(var4, var1 + 1, var5)) {
               double var6 = this.theOcelot.e(var4, var1, var5);
               if (var6 < var2) {
                  this.sitableBlockX = var4;
                  this.sitableBlockY = var1;
                  this.sitableBlockZ = var5;
                  var2 = var6;
               }
            }
         }
      }

      return var2 < 2.147483647E9;
   }

   private boolean isSittableBlock(World var1, int var2, int var3, int var4) {
      int var5 = var1.getBlockId(var2, var3, var4);
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      if (var5 == Block.chest.blockID) {
         TileEntityChest var7 = (TileEntityChest)var1.getBlockTileEntity(var2, var3, var4);
         if (var7.numUsingPlayers < 1) {
            return true;
         }
      } else {
         if (var5 == Block.furnaceBurning.blockID) {
            return true;
         }

         if (var5 == Block.bed.blockID && !BlockBed.isBlockHeadOfBed(var6)) {
            return true;
         }
      }

      return false;
   }
}
