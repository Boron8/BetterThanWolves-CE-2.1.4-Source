package net.minecraft.src;

public class EntityMinecartTNT extends EntityMinecart {
   private int minecartTNTFuse = -1;

   public EntityMinecartTNT(World var1) {
      super(var1);
   }

   public EntityMinecartTNT(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   public int getMinecartType() {
      return 3;
   }

   @Override
   public Block getDefaultDisplayTile() {
      return Block.tnt;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.minecartTNTFuse > 0) {
         this.minecartTNTFuse--;
         this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
      } else if (this.minecartTNTFuse == 0) {
         this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
      }

      if (this.isCollidedHorizontally) {
         double var1 = this.motionX * this.motionX + this.motionZ * this.motionZ;
         if (var1 >= 0.01F) {
            this.explodeCart(var1);
         }
      }
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(var1);
      double var2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
      if (!var1.isExplosion()) {
         this.a(new ItemStack(Block.tnt, 1), 0.0F);
      }

      if (var1.isFireDamage() || var1.isExplosion() || var2 >= 0.01F) {
         this.explodeCart(var2);
      }
   }

   protected void explodeCart(double var1) {
      if (!this.worldObj.isRemote) {
         double var3 = Math.sqrt(var1);
         if (var3 > 5.0) {
            var3 = 5.0;
         }

         this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0 + this.rand.nextDouble() * 1.5 * var3), true);
         this.w();
      }
   }

   @Override
   protected void fall(float var1) {
      if (var1 >= 3.0F) {
         float var2 = var1 / 10.0F;
         this.explodeCart(var2 * var2);
      }

      super.a(var1);
   }

   @Override
   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      if (var4 && this.minecartTNTFuse < 0) {
         this.ignite();
      }
   }

   @Override
   public void handleHealthUpdate(byte var1) {
      if (var1 == 10) {
         this.ignite();
      } else {
         super.a(var1);
      }
   }

   public void ignite() {
      this.minecartTNTFuse = 80;
      if (!this.worldObj.isRemote) {
         this.worldObj.setEntityState(this, (byte)10);
         this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 1.0F);
      }
   }

   public int func_94104_d() {
      return this.minecartTNTFuse;
   }

   public boolean isIgnited() {
      return this.minecartTNTFuse > -1;
   }

   @Override
   public float func_82146_a(Explosion var1, World var2, int var3, int var4, int var5, Block var6) {
      return !this.isIgnited() || !BlockRailBase.isRailBlock(var6.blockID) && !BlockRailBase.isRailBlockAt(var2, var3, var4 + 1, var5)
         ? super.a(var1, var2, var3, var4, var5, var6)
         : 0.0F;
   }

   @Override
   public boolean func_96091_a(Explosion var1, World var2, int var3, int var4, int var5, int var6, float var7) {
      return !this.isIgnited() || !BlockRailBase.isRailBlock(var6) && !BlockRailBase.isRailBlockAt(var2, var3, var4 + 1, var5)
         ? super.a(var1, var2, var3, var4, var5, var6, var7)
         : false;
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      if (var1.hasKey("TNTFuse")) {
         this.minecartTNTFuse = var1.getInteger("TNTFuse");
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("TNTFuse", this.minecartTNTFuse);
   }
}
