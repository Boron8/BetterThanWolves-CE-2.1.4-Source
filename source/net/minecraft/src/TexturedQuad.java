package net.minecraft.src;

public class TexturedQuad {
   public PositionTextureVertex[] vertexPositions;
   public int nVertices = 0;
   private boolean invertNormal = false;

   public TexturedQuad(PositionTextureVertex[] var1) {
      this.vertexPositions = var1;
      this.nVertices = var1.length;
   }

   public TexturedQuad(PositionTextureVertex[] var1, int var2, int var3, int var4, int var5, float var6, float var7) {
      this(var1);
      float var8 = 0.0F / var6;
      float var9 = 0.0F / var7;
      var1[0] = var1[0].setTexturePosition(var4 / var6 - var8, var3 / var7 + var9);
      var1[1] = var1[1].setTexturePosition(var2 / var6 + var8, var3 / var7 + var9);
      var1[2] = var1[2].setTexturePosition(var2 / var6 + var8, var5 / var7 - var9);
      var1[3] = var1[3].setTexturePosition(var4 / var6 - var8, var5 / var7 - var9);
   }

   public void flipFace() {
      PositionTextureVertex[] var1 = new PositionTextureVertex[this.vertexPositions.length];

      for (int var2 = 0; var2 < this.vertexPositions.length; var2++) {
         var1[var2] = this.vertexPositions[this.vertexPositions.length - var2 - 1];
      }

      this.vertexPositions = var1;
   }

   public void draw(Tessellator var1, float var2) {
      Vec3 var3 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[0].vector3D);
      Vec3 var4 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[2].vector3D);
      Vec3 var5 = var4.crossProduct(var3).normalize();
      var1.startDrawingQuads();
      if (this.invertNormal) {
         var1.setNormal(-((float)var5.xCoord), -((float)var5.yCoord), -((float)var5.zCoord));
      } else {
         var1.setNormal((float)var5.xCoord, (float)var5.yCoord, (float)var5.zCoord);
      }

      for (int var6 = 0; var6 < 4; var6++) {
         PositionTextureVertex var7 = this.vertexPositions[var6];
         var1.addVertexWithUV(
            (float)var7.vector3D.xCoord * var2,
            (float)var7.vector3D.yCoord * var2,
            (float)var7.vector3D.zCoord * var2,
            var7.texturePositionX,
            var7.texturePositionY
         );
      }

      var1.draw();
   }
}
