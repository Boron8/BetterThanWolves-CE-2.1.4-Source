package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityPickupFX extends EntityFX {
   private Entity entityToPickUp;
   private Entity entityPickingUp;
   private int age = 0;
   private int maxAge = 0;
   private float yOffs;

   public EntityPickupFX(World var1, Entity var2, Entity var3, float var4) {
      super(var1, var2.posX, var2.posY, var2.posZ, var2.motionX, var2.motionY, var2.motionZ);
      this.entityToPickUp = var2;
      this.entityPickingUp = var3;
      this.maxAge = 3;
      this.yOffs = var4;
   }

   @Override
   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = (this.age + var2) / this.maxAge;
      var8 *= var8;
      double var9 = this.entityToPickUp.posX;
      double var11 = this.entityToPickUp.posY;
      double var13 = this.entityToPickUp.posZ;
      double var15 = this.entityPickingUp.lastTickPosX + (this.entityPickingUp.posX - this.entityPickingUp.lastTickPosX) * var2;
      double var17 = this.entityPickingUp.lastTickPosY + (this.entityPickingUp.posY - this.entityPickingUp.lastTickPosY) * var2 + this.yOffs;
      double var19 = this.entityPickingUp.lastTickPosZ + (this.entityPickingUp.posZ - this.entityPickingUp.lastTickPosZ) * var2;
      double var21 = var9 + (var15 - var9) * var8;
      double var23 = var11 + (var17 - var11) * var8;
      double var25 = var13 + (var19 - var13) * var8;
      int var27 = MathHelper.floor_double(var21);
      int var28 = MathHelper.floor_double(var23 + this.yOffset / 2.0F);
      int var29 = MathHelper.floor_double(var25);
      int var30 = this.b(var2);
      int var31 = var30 % 65536;
      int var32 = var30 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var31 / 1.0F, var32 / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      var21 -= ay;
      var23 -= az;
      var25 -= aA;
      RenderManager.instance.renderEntityWithPosYaw(this.entityToPickUp, (float)var21, (float)var23, (float)var25, this.entityToPickUp.rotationYaw, var2);
   }

   @Override
   public void onUpdate() {
      this.age++;
      if (this.age == this.maxAge) {
         this.w();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }
}
