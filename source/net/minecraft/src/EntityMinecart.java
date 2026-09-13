package net.minecraft.src;

import btw.BTWMod;
import btw.block.blocks.BlockDispenserBlock;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.inventory.util.InventoryUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

public abstract class EntityMinecart extends Entity {
   private boolean isInReverse = false;
   private final IUpdatePlayerListBox field_82344_g;
   private String entityName;
   private static final int[][][] matrix = new int[][][]{
      {{0, 0, -1}, {0, 0, 1}},
      {{-1, 0, 0}, {1, 0, 0}},
      {{-1, -1, 0}, {1, 0, 0}},
      {{-1, 0, 0}, {1, -1, 0}},
      {{0, 0, -1}, {0, -1, 1}},
      {{0, -1, -1}, {0, 0, 1}},
      {{0, 0, 1}, {1, 0, 0}},
      {{0, 0, 1}, {-1, 0, 0}},
      {{0, 0, -1}, {-1, 0, 0}},
      {{0, 0, -1}, {1, 0, 0}}
   };
   private int turnProgress;
   private double minecartX;
   private double minecartY;
   private double minecartZ;
   private double minecartYaw;
   private double minecartPitch;
   @Environment(EnvType.CLIENT)
   private double velocityX;
   @Environment(EnvType.CLIENT)
   private double velocityY;
   @Environment(EnvType.CLIENT)
   private double velocityZ;

   public EntityMinecart(World par1World) {
      super(par1World);
      this.preventEntitySpawning = true;
      this.a(0.98F, 0.7F);
      this.yOffset = this.height / 2.0F;
      this.field_82344_g = par1World != null ? par1World.func_82735_a(this) : null;
   }

   public static EntityMinecart createMinecart(World par0World, double par1, double par3, double par5, int par7) {
      switch (par7) {
         case 1:
            return (EntityMinecart)EntityList.createEntityOfType(EntityMinecartChest.class, par0World, par1, par3, par5);
         case 2:
            return (EntityMinecart)EntityList.createEntityOfType(EntityMinecartFurnace.class, par0World, par1, par3, par5);
         case 3:
            return (EntityMinecart)EntityList.createEntityOfType(EntityMinecartTNT.class, par0World, par1, par3, par5);
         case 4:
            return (EntityMinecart)EntityList.createEntityOfType(EntityMinecartMobSpawner.class, par0World, par1, par3, par5);
         case 5:
            return (EntityMinecart)EntityList.createEntityOfType(EntityMinecartHopper.class, par0World, par1, par3, par5);
         default:
            return (EntityMinecart)EntityList.createEntityOfType(EntityMinecartEmpty.class, par0World, par1, par3, par5);
      }
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(1));
      this.dataWatcher.addObject(19, new Integer(0));
      this.dataWatcher.addObject(20, new Integer(0));
      this.dataWatcher.addObject(21, new Integer(6));
      this.dataWatcher.addObject(22, (byte)0);
   }

   @Override
   public AxisAlignedBB getCollisionBox(Entity par1Entity) {
      return par1Entity.canBePushed() ? par1Entity.boundingBox : null;
   }

   @Override
   public AxisAlignedBB getBoundingBox() {
      return null;
   }

   @Override
   public boolean canBePushed() {
      return true;
   }

   public EntityMinecart(World par1World, double par2, double par4, double par6) {
      this(par1World);
      this.b(par2, par4 + this.yOffset, par6);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.prevPosX = par2;
      this.prevPosY = par4;
      this.prevPosZ = par6;
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.0 - 0.3F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.worldObj.isRemote || this.isDead) {
         return true;
      } else if (this.aq()) {
         return false;
      } else {
         this.setRollingDirection(-this.getRollingDirection());
         this.setRollingAmplitude(10);
         this.J();
         this.setDamage(this.getDamage() + par2 * 10);
         boolean var3 = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;
         if (var3 || this.getDamage() > 40) {
            if (this.riddenByEntity != null) {
               this.riddenByEntity.mountEntity(this);
            }

            if (var3 && !this.isInvNameLocalized()) {
               this.setDead();
            } else {
               this.killMinecart(par1DamageSource);
            }
         }

         return true;
      }
   }

   public void killMinecart(DamageSource par1DamageSource) {
      this.setDead();
      ItemStack var2 = new ItemStack(Item.minecartEmpty, 1);
      if (this.entityName != null) {
         var2.setItemName(this.entityName);
      }

      this.a(var2, 0.0F);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void performHurtAnimation() {
      this.setRollingDirection(-this.getRollingDirection());
      this.setRollingAmplitude(10);
      this.setDamage(this.getDamage() + this.getDamage() * 10);
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void setDead() {
      super.setDead();
      if (this.field_82344_g != null) {
         this.field_82344_g.update();
      }
   }

   @Override
   public void onUpdate() {
      if (this.field_82344_g != null) {
         this.field_82344_g.update();
      }

      if (this.getRollingAmplitude() > 0) {
         this.setRollingAmplitude(this.getRollingAmplitude() - 1);
      }

      if (this.getDamage() > 0) {
         this.setDamage(this.getDamage() - 1);
      }

      if (this.posY < -64.0) {
         this.B();
      }

      if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
         this.worldObj.theProfiler.startSection("portal");
         MinecraftServer var1 = ((WorldServer)this.worldObj).getMinecraftServer();
         int var2 = this.y();
         if (this.inPortal) {
            if (var1.getAllowNether()) {
               if (this.ridingEntity == null && this.field_82153_h++ >= var2) {
                  this.field_82153_h = var2;
                  this.timeUntilPortal = this.aa();
                  byte var3;
                  if (this.worldObj.provider.dimensionId == -1) {
                     var3 = 0;
                  } else {
                     var3 = -1;
                  }

                  this.c(var3);
               }

               this.inPortal = false;
            }
         } else {
            if (this.field_82153_h > 0) {
               this.field_82153_h -= 4;
            }

            if (this.field_82153_h < 0) {
               this.field_82153_h = 0;
            }
         }

         if (this.timeUntilPortal > 0) {
            this.timeUntilPortal--;
         }

         this.worldObj.theProfiler.endSection();
      }

      if (this.worldObj.isRemote) {
         if (this.turnProgress > 0) {
            double var19 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
            double var21 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
            double var5 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
            double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
            this.rotationYaw = (float)(this.rotationYaw + var7 / this.turnProgress);
            this.rotationPitch = (float)(this.rotationPitch + (this.minecartPitch - this.rotationPitch) / this.turnProgress);
            this.turnProgress--;
            this.b(var19, var21, var5);
            this.b(this.rotationYaw, this.rotationPitch);
         } else {
            this.b(this.posX, this.posY, this.posZ);
            this.b(this.rotationYaw, this.rotationPitch);
         }
      } else {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.motionY -= 0.04F;
         int var18 = MathHelper.floor_double(this.posX);
         int var2x = MathHelper.floor_double(this.posY);
         int var20 = MathHelper.floor_double(this.posZ);
         if (BlockRailBase.isRailBlockAt(this.worldObj, var18, var2x - 1, var20)) {
            var2x--;
         }

         double var4 = 0.4;
         double var6 = 0.0078125;
         int var8 = this.worldObj.getBlockId(var18, var2x, var20);
         if (BlockRailBase.isRailBlock(var8)) {
            int var9 = this.worldObj.getBlockMetadata(var18, var2x, var20);
            this.updateOnTrack(var18, var2x, var20, var4, var6, var8, var9);
            if (var8 == Block.railActivator.blockID) {
               this.onActivatorRailPass(var18, var2x, var20, (var9 & 8) != 0);
            }
         } else {
            this.func_94088_b(var4);
         }

         this.C();
         this.rotationPitch = 0.0F;
         double var22 = this.prevPosX - this.posX;
         double var11 = this.prevPosZ - this.posZ;
         if (var22 * var22 + var11 * var11 > 0.001) {
            this.rotationYaw = (float)(Math.atan2(var11, var22) * 180.0 / Math.PI);
            if (this.isInReverse) {
               this.rotationYaw += 180.0F;
            }
         }

         double var13 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
         if (var13 < -170.0 || var13 >= 170.0) {
            this.rotationYaw += 180.0F;
            this.isInReverse = !this.isInReverse;
         }

         this.b(this.rotationYaw, this.rotationPitch);
         List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2F, 0.0, 0.2F));
         if (var15 != null && !var15.isEmpty()) {
            for (int var16 = 0; var16 < var15.size(); var16++) {
               Entity var17 = (Entity)var15.get(var16);
               if (var17 != this.riddenByEntity && var17.canBePushed() && var17 instanceof EntityMinecart) {
                  var17.applyEntityCollision(this);
               }
            }
         }

         if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
            if (this.riddenByEntity.ridingEntity == this) {
               this.riddenByEntity.ridingEntity = null;
            }

            this.riddenByEntity = null;
         }
      }
   }

   public void onActivatorRailPass(int par1, int par2, int par3, boolean par4) {
   }

   protected void func_94088_b(double par1) {
      if (this.motionX < -par1) {
         this.motionX = -par1;
      }

      if (this.motionX > par1) {
         this.motionX = par1;
      }

      if (this.motionZ < -par1) {
         this.motionZ = -par1;
      }

      if (this.motionZ > par1) {
         this.motionZ = par1;
      }

      if (this.onGround) {
         this.motionX *= 0.5;
         this.motionY *= 0.5;
         this.motionZ *= 0.5;
      }

      this.d(this.motionX, this.motionY, this.motionZ);
      if (!this.onGround) {
         this.motionX *= 0.95F;
         this.motionY *= 0.95F;
         this.motionZ *= 0.95F;
      }
   }

   protected void updateOnTrack(int par1, int par2, int par3, double par4, double par6, int par8, int par9) {
      this.fallDistance = 0.0F;
      Vec3 var10 = this.func_70489_a(this.posX, this.posY, this.posZ);
      this.posY = par2;
      boolean var11 = false;
      boolean var12 = false;
      if (par8 == Block.railPowered.blockID) {
         var11 = (par9 & 8) != 0;
         var12 = !var11;
      }

      if (((BlockRailBase)Block.blocksList[par8]).isPowered()) {
         par9 &= 7;
      }

      if (par9 >= 2 && par9 <= 5) {
         this.posY = par2 + 1;
      }

      if (par9 == 2) {
         this.motionX -= par6;
      }

      if (par9 == 3) {
         this.motionX += par6;
      }

      if (par9 == 4) {
         this.motionZ += par6;
      }

      if (par9 == 5) {
         this.motionZ -= par6;
      }

      int[][] var13 = matrix[par9];
      double var14 = var13[1][0] - var13[0][0];
      double var16 = var13[1][2] - var13[0][2];
      double var18 = Math.sqrt(var14 * var14 + var16 * var16);
      double var20 = this.motionX * var14 + this.motionZ * var16;
      if (var20 < 0.0) {
         var14 = -var14;
         var16 = -var16;
      }

      double var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      if (var22 > 2.0) {
         var22 = 2.0;
      }

      this.motionX = var22 * var14 / var18;
      this.motionZ = var22 * var16 / var18;
      if (this.riddenByEntity != null) {
         double var24 = this.riddenByEntity.motionX * this.riddenByEntity.motionX + this.riddenByEntity.motionZ * this.riddenByEntity.motionZ;
         double var26 = this.motionX * this.motionX + this.motionZ * this.motionZ;
         if (var24 > 1.0E-4 && var26 < 0.01) {
            this.motionX = this.motionX + this.riddenByEntity.motionX * 0.1;
            this.motionZ = this.motionZ + this.riddenByEntity.motionZ * 0.1;
            var12 = false;
         }
      }

      if (var12) {
         double var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var24 < 0.03) {
            this.motionX *= 0.0;
            this.motionY *= 0.0;
            this.motionZ *= 0.0;
         } else {
            this.motionX *= 0.5;
            this.motionY *= 0.0;
            this.motionZ *= 0.5;
         }
      }

      double var24 = 0.0;
      double var26 = par1 + 0.5 + var13[0][0] * 0.5;
      double var28 = par3 + 0.5 + var13[0][2] * 0.5;
      double var30 = par1 + 0.5 + var13[1][0] * 0.5;
      double var32 = par3 + 0.5 + var13[1][2] * 0.5;
      var14 = var30 - var26;
      var16 = var32 - var28;
      if (var14 == 0.0) {
         this.posX = par1 + 0.5;
         var24 = this.posZ - par3;
      } else if (var16 == 0.0) {
         this.posZ = par3 + 0.5;
         var24 = this.posX - par1;
      } else {
         double var34 = this.posX - var26;
         double var36 = this.posZ - var28;
         var24 = (var34 * var14 + var36 * var16) * 2.0;
      }

      this.posX = var26 + var14 * var24;
      this.posZ = var28 + var16 * var24;
      this.b(this.posX, this.posY + this.yOffset, this.posZ);
      double var34 = this.motionX;
      double var36 = this.motionZ;
      if (this.riddenByEntity != null) {
         var34 *= 0.75;
         var36 *= 0.75;
      }

      if (var34 < -par4) {
         var34 = -par4;
      }

      if (var34 > par4) {
         var34 = par4;
      }

      if (var36 < -par4) {
         var36 = -par4;
      }

      if (var36 > par4) {
         var36 = par4;
      }

      this.d(var34, 0.0, var36);
      if (var13[0][1] != 0 && MathHelper.floor_double(this.posX) - par1 == var13[0][0] && MathHelper.floor_double(this.posZ) - par3 == var13[0][2]) {
         this.b(this.posX, this.posY + var13[0][1], this.posZ);
      } else if (var13[1][1] != 0 && MathHelper.floor_double(this.posX) - par1 == var13[1][0] && MathHelper.floor_double(this.posZ) - par3 == var13[1][2]) {
         this.b(this.posX, this.posY + var13[1][1], this.posZ);
      }

      this.applyDrag();
      Vec3 var38 = this.func_70489_a(this.posX, this.posY, this.posZ);
      if (var38 != null && var10 != null) {
         double var39 = (var10.yCoord - var38.yCoord) * 0.05;
         var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var22 > 0.0) {
            this.motionX = this.motionX / var22 * (var22 + var39);
            this.motionZ = this.motionZ / var22 * (var22 + var39);
         }

         this.b(this.posX, var38.yCoord, this.posZ);
      }

      int var45 = MathHelper.floor_double(this.posX);
      int var40 = MathHelper.floor_double(this.posZ);
      if (var45 != par1 || var40 != par3) {
         var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.motionX = var22 * (var45 - par1);
         this.motionZ = var22 * (var40 - par3);
      }

      if (var11) {
         double var41 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var41 > 0.01) {
            double var43 = 0.06;
            this.motionX = this.motionX + this.motionX / var41 * var43;
            this.motionZ = this.motionZ + this.motionZ / var41 * var43;
         } else if (par9 == 1) {
            if (this.worldObj.isBlockNormalCube(par1 - 1, par2, par3)) {
               this.motionX = 0.02;
            } else if (this.worldObj.isBlockNormalCube(par1 + 1, par2, par3)) {
               this.motionX = -0.02;
            }
         } else if (par9 == 0) {
            if (this.worldObj.isBlockNormalCube(par1, par2, par3 - 1)) {
               this.motionZ = 0.02;
            } else if (this.worldObj.isBlockNormalCube(par1, par2, par3 + 1)) {
               this.motionZ = -0.02;
            }
         }
      }
   }

   protected void applyDrag() {
      if (this.riddenByEntity != null) {
         this.motionX *= 0.997F;
         this.motionY *= 0.0;
         this.motionZ *= 0.997F;
      } else if (BTWMod.disableMinecartChanges) {
         this.motionX *= 0.96;
         this.motionY *= 0.0;
         this.motionZ *= 0.96;
      } else {
         this.motionX *= 0.985;
         this.motionY *= 0.0;
         this.motionZ *= 0.985;
      }
   }

   @Environment(EnvType.CLIENT)
   public Vec3 func_70495_a(double par1, double par3, double par5, double par7) {
      int var9 = MathHelper.floor_double(par1);
      int var10 = MathHelper.floor_double(par3);
      int var11 = MathHelper.floor_double(par5);
      if (BlockRailBase.isRailBlockAt(this.worldObj, var9, var10 - 1, var11)) {
         var10--;
      }

      int var12 = this.worldObj.getBlockId(var9, var10, var11);
      if (!BlockRailBase.isRailBlock(var12)) {
         return null;
      } else {
         int var13 = this.worldObj.getBlockMetadata(var9, var10, var11);
         if (((BlockRailBase)Block.blocksList[var12]).isPowered()) {
            var13 &= 7;
         }

         par3 = var10;
         if (var13 >= 2 && var13 <= 5) {
            par3 = var10 + 1;
         }

         int[][] var14 = matrix[var13];
         double var15 = var14[1][0] - var14[0][0];
         double var17 = var14[1][2] - var14[0][2];
         double var19 = Math.sqrt(var15 * var15 + var17 * var17);
         var15 /= var19;
         var17 /= var19;
         par1 += var15 * par7;
         par5 += var17 * par7;
         if (var14[0][1] != 0 && MathHelper.floor_double(par1) - var9 == var14[0][0] && MathHelper.floor_double(par5) - var11 == var14[0][2]) {
            par3 += var14[0][1];
         } else if (var14[1][1] != 0 && MathHelper.floor_double(par1) - var9 == var14[1][0] && MathHelper.floor_double(par5) - var11 == var14[1][2]) {
            par3 += var14[1][1];
         }

         return this.func_70489_a(par1, par3, par5);
      }
   }

   public Vec3 func_70489_a(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      if (BlockRailBase.isRailBlockAt(this.worldObj, var7, var8 - 1, var9)) {
         var8--;
      }

      int var10 = this.worldObj.getBlockId(var7, var8, var9);
      if (BlockRailBase.isRailBlock(var10)) {
         int var11 = this.worldObj.getBlockMetadata(var7, var8, var9);
         par3 = var8;
         if (((BlockRailBase)Block.blocksList[var10]).isPowered()) {
            var11 &= 7;
         }

         if (var11 >= 2 && var11 <= 5) {
            par3 = var8 + 1;
         }

         int[][] var12 = matrix[var11];
         double var13 = 0.0;
         double var15 = var7 + 0.5 + var12[0][0] * 0.5;
         double var17 = var8 + 0.5 + var12[0][1] * 0.5;
         double var19 = var9 + 0.5 + var12[0][2] * 0.5;
         double var21 = var7 + 0.5 + var12[1][0] * 0.5;
         double var23 = var8 + 0.5 + var12[1][1] * 0.5;
         double var25 = var9 + 0.5 + var12[1][2] * 0.5;
         double var27 = var21 - var15;
         double var29 = (var23 - var17) * 2.0;
         double var31 = var25 - var19;
         if (var27 == 0.0) {
            par1 = var7 + 0.5;
            var13 = par5 - var9;
         } else if (var31 == 0.0) {
            par5 = var9 + 0.5;
            var13 = par1 - var7;
         } else {
            double var33 = par1 - var15;
            double var35 = par5 - var19;
            var13 = (var33 * var27 + var35 * var31) * 2.0;
         }

         par1 = var15 + var27 * var13;
         par3 = var17 + var29 * var13;
         par5 = var19 + var31 * var13;
         if (var29 < 0.0) {
            par3++;
         }

         if (var29 > 0.0) {
            par3 += 0.5;
         }

         return this.worldObj.getWorldVec3Pool().getVecFromPool(par1, par3, par5);
      } else {
         return null;
      }
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      if (par1NBTTagCompound.getBoolean("CustomDisplayTile")) {
         this.setDisplayTile(par1NBTTagCompound.getInteger("DisplayTile"));
         this.setDisplayTileData(par1NBTTagCompound.getInteger("DisplayData"));
         this.setDisplayTileOffset(par1NBTTagCompound.getInteger("DisplayOffset"));
      }

      if (par1NBTTagCompound.hasKey("CustomName") && par1NBTTagCompound.getString("CustomName").length() > 0) {
         this.entityName = par1NBTTagCompound.getString("CustomName");
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      if (this.hasDisplayTile()) {
         par1NBTTagCompound.setBoolean("CustomDisplayTile", true);
         par1NBTTagCompound.setInteger("DisplayTile", this.getDisplayTile() == null ? 0 : this.getDisplayTile().blockID);
         par1NBTTagCompound.setInteger("DisplayData", this.getDisplayTileData());
         par1NBTTagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
      }

      if (this.entityName != null && this.entityName.length() > 0) {
         par1NBTTagCompound.setString("CustomName", this.entityName);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Override
   public void applyEntityCollision(Entity par1Entity) {
      if (!this.worldObj.isRemote && par1Entity != this.riddenByEntity) {
         if (par1Entity instanceof EntityLiving
            && !(par1Entity instanceof EntityPlayer)
            && !(par1Entity instanceof EntityIronGolem)
            && this.getMinecartType() == 0
            && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01
            && this.riddenByEntity == null
            && par1Entity.ridingEntity == null) {
            par1Entity.mountEntity(this);
         }

         double var2 = par1Entity.posX - this.posX;
         double var4 = par1Entity.posZ - this.posZ;
         double var6 = var2 * var2 + var4 * var4;
         if (var6 >= 1.0E-4F) {
            var6 = MathHelper.sqrt_double(var6);
            var2 /= var6;
            var4 /= var6;
            double var8 = 1.0 / var6;
            if (var8 > 1.0) {
               var8 = 1.0;
            }

            var2 *= var8;
            var4 *= var8;
            var2 *= 0.1F;
            var4 *= 0.1F;
            var2 *= 1.0F - this.entityCollisionReduction;
            var4 *= 1.0F - this.entityCollisionReduction;
            var2 *= 0.5;
            var4 *= 0.5;
            if (par1Entity instanceof EntityMinecart) {
               double var10 = par1Entity.posX - this.posX;
               double var12 = par1Entity.posZ - this.posZ;
               Vec3 var14 = this.worldObj.getWorldVec3Pool().getVecFromPool(var10, 0.0, var12).normalize();
               Vec3 var15 = this.worldObj
                  .getWorldVec3Pool()
                  .getVecFromPool(MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F), 0.0, MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F))
                  .normalize();
               double var16 = Math.abs(var14.dotProduct(var15));
               if (var16 < 0.8F) {
                  return;
               }

               double var18 = par1Entity.motionX + this.motionX;
               double var20 = par1Entity.motionZ + this.motionZ;
               if (((EntityMinecart)par1Entity).getMinecartType() == 2 && this.getMinecartType() != 2) {
                  this.motionX *= 0.2F;
                  this.motionZ *= 0.2F;
                  this.g(par1Entity.motionX - var2, 0.0, par1Entity.motionZ - var4);
                  par1Entity.motionX *= 0.95F;
                  par1Entity.motionZ *= 0.95F;
               } else if (((EntityMinecart)par1Entity).getMinecartType() != 2 && this.getMinecartType() == 2) {
                  par1Entity.motionX *= 0.2F;
                  par1Entity.motionZ *= 0.2F;
                  par1Entity.addVelocity(this.motionX + var2, 0.0, this.motionZ + var4);
                  this.motionX *= 0.95F;
                  this.motionZ *= 0.95F;
               } else {
                  var18 /= 2.0;
                  var20 /= 2.0;
                  this.motionX *= 0.2F;
                  this.motionZ *= 0.2F;
                  this.g(var18 - var2, 0.0, var20 - var4);
                  par1Entity.motionX *= 0.2F;
                  par1Entity.motionZ *= 0.2F;
                  par1Entity.addVelocity(var18 + var2, 0.0, var20 + var4);
               }
            } else {
               this.g(-var2, 0.0, -var4);
               par1Entity.addVelocity(var2 / 4.0, 0.0, var4 / 4.0);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.minecartX = par1;
      this.minecartY = par3;
      this.minecartZ = par5;
      this.minecartYaw = par7;
      this.minecartPitch = par8;
      this.turnProgress = par9 + 2;
      this.motionX = this.velocityX;
      this.motionY = this.velocityY;
      this.motionZ = this.velocityZ;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setVelocity(double par1, double par3, double par5) {
      this.velocityX = this.motionX = par1;
      this.velocityY = this.motionY = par3;
      this.velocityZ = this.motionZ = par5;
   }

   public void setDamage(int par1) {
      this.dataWatcher.updateObject(19, par1);
   }

   public int getDamage() {
      return this.dataWatcher.getWatchableObjectInt(19);
   }

   public void setRollingAmplitude(int par1) {
      this.dataWatcher.updateObject(17, par1);
   }

   public int getRollingAmplitude() {
      return this.dataWatcher.getWatchableObjectInt(17);
   }

   public void setRollingDirection(int par1) {
      this.dataWatcher.updateObject(18, par1);
   }

   public int getRollingDirection() {
      return this.dataWatcher.getWatchableObjectInt(18);
   }

   public abstract int getMinecartType();

   public Block getDisplayTile() {
      if (!this.hasDisplayTile()) {
         return this.getDefaultDisplayTile();
      } else {
         int var1 = this.u().getWatchableObjectInt(20) & 65535;
         return var1 > 0 && var1 < Block.blocksList.length ? Block.blocksList[var1] : null;
      }
   }

   public Block getDefaultDisplayTile() {
      return null;
   }

   public int getDisplayTileData() {
      return !this.hasDisplayTile() ? this.getDefaultDisplayTileData() : this.u().getWatchableObjectInt(20) >> 16;
   }

   public int getDefaultDisplayTileData() {
      return 0;
   }

   public int getDisplayTileOffset() {
      return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.u().getWatchableObjectInt(21);
   }

   public int getDefaultDisplayTileOffset() {
      return 6;
   }

   public void setDisplayTile(int par1) {
      this.u().updateObject(20, par1 & 65535 | this.getDisplayTileData() << 16);
      this.setHasDisplayTile(true);
   }

   public void setDisplayTileData(int par1) {
      Block var2 = this.getDisplayTile();
      int var3 = var2 == null ? 0 : var2.blockID;
      this.u().updateObject(20, var3 & 65535 | par1 << 16);
      this.setHasDisplayTile(true);
   }

   public void setDisplayTileOffset(int par1) {
      this.u().updateObject(21, par1);
      this.setHasDisplayTile(true);
   }

   public boolean hasDisplayTile() {
      return this.u().getWatchableObjectByte(22) == 1;
   }

   public void setHasDisplayTile(boolean par1) {
      this.u().updateObject(22, (byte)(par1 ? 1 : 0));
   }

   public void func_96094_a(String par1Str) {
      this.entityName = par1Str;
   }

   @Override
   public String getEntityName() {
      return this.entityName != null ? this.entityName : super.getEntityName();
   }

   public boolean isInvNameLocalized() {
      return this.entityName != null;
   }

   public String func_95999_t() {
      return this.entityName;
   }

   @Override
   public boolean canCollideWithEntity(Entity entity) {
      return !entity.isItemEntity();
   }

   @Override
   public boolean onBlockDispenserConsume(BlockDispenserBlock blockDispenser, BlockDispenserTileEntity tileEentityDispenser) {
      int minecartType = this.getMinecartType();
      if (this.riddenByEntity != null) {
         this.riddenByEntity.mountEntity(this);
      }

      this.setDead();
      switch (minecartType) {
         case 1:
            InventoryUtils.addSingleItemToInventory(tileEentityDispenser, Item.minecartCrate.itemID, 0);
            break;
         case 2:
            InventoryUtils.addSingleItemToInventory(tileEentityDispenser, Item.minecartPowered.itemID, 0);
            break;
         default:
            InventoryUtils.addSingleItemToInventory(tileEentityDispenser, Item.minecartEmpty.itemID, 0);
      }

      this.worldObj.playAuxSFX(1001, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      return true;
   }
}
