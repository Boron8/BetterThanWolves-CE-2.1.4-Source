package btw.client.texture;

import java.nio.ByteBuffer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureStitched;

@Environment(EnvType.CLIENT)
public class ArcaneVesselXPTexture extends TextureStitched {
   ByteBuffer frameBuffer;
   float colorMultiplierRed;
   float colorMultiplierGreen;
   float colorMultiplierBlue;
   int bufferWidth;
   int bufferHeight;
   int bufferPixelSize;

   public ArcaneVesselXPTexture(String sName) {
      super(sName);
   }

   @Override
   public void init(Texture par1Texture, List par2List, int par3, int par4, int par5, int par6, boolean par7) {
      super.init(par1Texture, par2List, par3, par4, par5, par6, par7);
      this.colorMultiplierRed = 0.0F;
      this.colorMultiplierGreen = 0.0F;
      this.colorMultiplierBlue = 0.0F;
      this.bufferWidth = ((Texture)this.textureList.get(0)).getWidth();
      this.bufferHeight = ((Texture)this.textureList.get(0)).getHeight();
      this.bufferPixelSize = this.bufferWidth * this.bufferHeight;
      this.frameBuffer = ByteBuffer.allocateDirect(this.bufferPixelSize * 4);
   }

   @Override
   public void updateAnimation() {
      this.frameCounter++;
      float fRedAngle = this.frameCounter % 360 * (float) Math.PI / 180.0F;
      this.colorMultiplierRed = (MathHelper.sin(fRedAngle) * 0.5F + 0.5F) * 0.75F + 0.25F;
      this.copyFrameToBufferWithColorMultiplier(this.frameBuffer, this.bufferPixelSize);
      this.textureSheet.uploadByteBufferToGPU(this.originX, this.originY, this.frameBuffer, this.bufferWidth, this.bufferHeight);
   }

   private void copyFrameToBufferWithColorMultiplier(ByteBuffer m_frameBuffer, int m_iBufferPixelSize) {
      ByteBuffer sourceBuffer = ((Texture)this.textureList.get(0)).getTextureData();

      for (int iPixelIndex = 0; iPixelIndex < m_iBufferPixelSize; iPixelIndex++) {
         int iSourceRed = sourceBuffer.get(iPixelIndex * 4 + 0) & 255;
         int iSourceGreen = sourceBuffer.get(iPixelIndex * 4 + 1) & 255;
         int iSourceBlue = sourceBuffer.get(iPixelIndex * 4 + 2) & 255;
         int iPixelRed = (int)(iSourceRed * this.colorMultiplierRed);
         int iPixelGreen = (int)(iSourceGreen * this.colorMultiplierGreen);
         int iPixelBlue = (int)(iSourceBlue * this.colorMultiplierBlue);
         int iPixelAlpha = sourceBuffer.get(iPixelIndex * 4 + 3);
         if (iPixelRed > 255 || iPixelGreen > 255 || iPixelBlue > 255 || iPixelRed < 0 || iPixelGreen < 0 || iPixelBlue < 0) {
            boolean var12 = true;
         }

         m_frameBuffer.put(iPixelIndex * 4 + 0, (byte)iPixelRed);
         m_frameBuffer.put(iPixelIndex * 4 + 1, (byte)iPixelGreen);
         m_frameBuffer.put(iPixelIndex * 4 + 2, (byte)iPixelBlue);
         m_frameBuffer.put(iPixelIndex * 4 + 3, (byte)iPixelAlpha);
      }
   }

   @Override
   public boolean isProcedurallyAnimated() {
      return true;
   }
}
