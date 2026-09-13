package net.minecraft.src;

public class EntityFireworkOverlayFX extends EntityFX {
   protected EntityFireworkOverlayFX(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
      this.particleMaxAge = 4;
   }

   @Override
   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = 0.25F;
      float var9 = var8 + 0.25F;
      float var10 = 0.125F;
      float var11 = var10 + 0.25F;
      float var12 = 7.1F * MathHelper.sin((this.particleAge + var2 - 1.0F) * 0.25F * (float) Math.PI);
      this.particleAlpha = 0.6F - (this.particleAge + var2 - 1.0F) * 0.25F * 0.5F;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * var2 - ay);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * var2 - az);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * var2 - aA);
      var1.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
      var1.addVertexWithUV(var13 - var3 * var12 - var6 * var12, var14 - var4 * var12, var15 - var5 * var12 - var7 * var12, var9, var11);
      var1.addVertexWithUV(var13 - var3 * var12 + var6 * var12, var14 + var4 * var12, var15 - var5 * var12 + var7 * var12, var9, var10);
      var1.addVertexWithUV(var13 + var3 * var12 + var6 * var12, var14 + var4 * var12, var15 + var5 * var12 + var7 * var12, var8, var10);
      var1.addVertexWithUV(var13 + var3 * var12 - var6 * var12, var14 - var4 * var12, var15 + var5 * var12 - var7 * var12, var8, var11);
   }
}
