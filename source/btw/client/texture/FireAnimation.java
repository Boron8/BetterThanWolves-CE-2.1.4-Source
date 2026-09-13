package btw.client.texture;

import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FireAnimation {
   public static FireAnimation[] instanceArray = new FireAnimation[]{null, null};
   public int width;
   public int height;
   public int textureHeight;
   public int size;
   protected float[] previousIntensities;
   protected float[] currentIntensities;
   private float intensityDecayFactor;
   private float intensityDecayFactorTop;
   private double distanceFromCenterIntensityModifier;
   private int centerRow;
   public static final float COLOR_SHIFT_SEPARATOR_BLUE_TO_WHITE = 0.39F;
   public static final float COLOR_SHIFT_SEPARATOR_WHITE_TO_RED = 0.66F;
   public static final float INVISIBLE_PIXEL_THRESHOLD_TOP = 0.87F;
   public static final float INVISIBLE_PIXEL_THRESHOLD_BOTTOM = 0.001F;

   public FireAnimation(int iInstanceIndex, int iTextureWidth, int iTextureHeight) {
      this.width = iTextureWidth;
      this.height = iTextureHeight * 2;
      this.textureHeight = iTextureHeight;
      this.size = this.width * this.height;
      this.previousIntensities = new float[this.size];
      this.currentIntensities = new float[this.size];

      for (int iTempIndex = 0; iTempIndex < this.size; iTempIndex++) {
         this.previousIntensities[iTempIndex] = 0.0F;
         this.currentIntensities[iTempIndex] = 0.0F;
      }

      this.intensityDecayFactor = 1.0F + 0.08F * (16.0F / this.textureHeight);
      this.intensityDecayFactorTop = 1.0F + 0.07F * (16.0F / this.textureHeight);
      this.distanceFromCenterIntensityModifier = 0.123 * (16.0 / this.width);
      this.centerRow = this.width / 2;
      instanceArray[iInstanceIndex] = this;
   }

   public void update() {
      this.driftFireUpwards();
      this.generateNewBottomRow();
      this.previousIntensities = this.currentIntensities;
   }

   private void generateNewBottomRow() {
      for (int i = 0; i < this.width; i++) {
         double dDistFromCenter = this.centerRow - Math.abs(i - (this.centerRow - 1));
         double dBaseIntensity = Math.random() * Math.random() * Math.random() * 4.0 + Math.random() * 0.1F + 0.2;
         double dDistanceFromCenterModifier = dDistFromCenter * this.distanceFromCenterIntensityModifier;
         dDistanceFromCenterModifier *= dDistanceFromCenterModifier;
         this.currentIntensities[i + (this.height - 1) * this.width] = (float)(dBaseIntensity + dDistanceFromCenterModifier);
      }
   }

   private void driftFireUpwards() {
      for (int i = 0; i < this.width; i++) {
         for (int j = 0; j < this.height - 1; j++) {
            int iTotalWeight = 18;
            float fNewIntensity = this.previousIntensities[i + (j + 1) * this.width] * iTotalWeight;

            for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
               for (int iTempJ = j; iTempJ <= j + 1; iTempJ++) {
                  if (iTempI >= 0 && iTempJ >= 0 && iTempI < this.width && iTempJ < this.height) {
                     fNewIntensity += this.previousIntensities[iTempI + iTempJ * this.width];
                  }

                  iTotalWeight++;
               }
            }

            if (j < this.textureHeight) {
               fNewIntensity /= iTotalWeight * this.intensityDecayFactorTop;
            } else {
               fNewIntensity /= iTotalWeight * this.intensityDecayFactor;
            }

            this.currentIntensities[i + j * this.width] = fNewIntensity;
         }
      }
   }

   public void copyRegularFireFrameToByteBuffer(ByteBuffer m_frameBuffer, int m_iBufferPixelSize) {
      for (int iPixelIndex = 0; iPixelIndex < m_iBufferPixelSize; iPixelIndex++) {
         float iPixelIntensity = this.currentIntensities[iPixelIndex];
         if (iPixelIntensity > 1.0F) {
            iPixelIntensity = 1.0F;
         } else if (iPixelIntensity < 0.0F) {
            iPixelIntensity = 0.0F;
         }

         float fColorMultiplier = 1.0F - iPixelIntensity;
         int iRed = 0;
         int iGreen = 0;
         int iBlue = 0;
         char cAlpha = 255;
         if (fColorMultiplier > 0.87F || fColorMultiplier < 0.001F) {
            cAlpha = 0;
         } else if (fColorMultiplier < 0.39F) {
            float fFactor = fColorMultiplier / 0.39F;
            float fFactorSquared = fFactor * fFactor;
            iRed = (int)(fFactorSquared * 255.0F);
            iGreen = (int)(fFactorSquared * 255.0F);
            iBlue = (int)(fFactor * 100.0F) + 155;
         } else if (fColorMultiplier < 0.66F) {
            iRed = 255;
            iGreen = 255;
            iBlue = 255;
         } else {
            float fDelta = 1.0F - (fColorMultiplier - 0.66F) / 0.33999997F;
            iRed = (int)(fDelta * 120.0F) + 135;
            float fDeltaSquared = fDelta * fDelta;
            iGreen = (int)(fDeltaSquared * 225.0F) + 30;
            float fBlueMultiplier = fDeltaSquared * fDeltaSquared;
            fBlueMultiplier *= fBlueMultiplier;
            iBlue = (int)(fBlueMultiplier * 255.0F);
         }

         m_frameBuffer.put(iPixelIndex * 4 + 0, (byte)iRed);
         m_frameBuffer.put(iPixelIndex * 4 + 1, (byte)iGreen);
         m_frameBuffer.put(iPixelIndex * 4 + 2, (byte)iBlue);
         m_frameBuffer.put(iPixelIndex * 4 + 3, (byte)cAlpha);
      }
   }

   public void copyStokedFireFrameToByteBuffer(ByteBuffer m_frameBuffer, int m_iBufferPixelSize) {
      for (int iPixelIndex = 0; iPixelIndex < m_iBufferPixelSize; iPixelIndex++) {
         float iPixelIntensity = this.currentIntensities[iPixelIndex + this.textureHeight * this.width];
         if (iPixelIntensity > 1.0F) {
            iPixelIntensity = 1.0F;
         } else if (iPixelIntensity < 0.0F) {
            iPixelIntensity = 0.0F;
         }

         float fColorMultiplier = 1.0F - iPixelIntensity;
         int iRed = 0;
         int iGreen = 0;
         int iBlue = 0;
         char cAlpha = 255;
         if (fColorMultiplier > 0.87F || fColorMultiplier < 0.001F) {
            cAlpha = 0;
         } else if (fColorMultiplier < 0.39F) {
            float fFactor = fColorMultiplier / 0.39F;
            float fFactorSquared = fFactor * fFactor;
            iRed = (int)(fFactorSquared * 255.0F);
            iGreen = (int)(fFactorSquared * 255.0F);
            iBlue = (int)(fFactor * 100.0F) + 155;
         } else if (fColorMultiplier < 0.66F) {
            iRed = 255;
            iGreen = 255;
            iBlue = 255;
         } else {
            float fDelta = 1.0F - (fColorMultiplier - 0.66F) / 0.33999997F;
            iRed = (int)(fDelta * 120.0F) + 135;
            float fDeltaSquared = fDelta * fDelta;
            iGreen = (int)(fDeltaSquared * 225.0F) + 30;
            float fBlueMultiplier = fDeltaSquared * fDeltaSquared;
            fBlueMultiplier *= fBlueMultiplier;
            iBlue = (int)(fBlueMultiplier * 255.0F);
         }

         m_frameBuffer.put(iPixelIndex * 4 + 0, (byte)iRed);
         m_frameBuffer.put(iPixelIndex * 4 + 1, (byte)iGreen);
         m_frameBuffer.put(iPixelIndex * 4 + 2, (byte)iBlue);
         m_frameBuffer.put(iPixelIndex * 4 + 3, (byte)cAlpha);
      }
   }

   public static void updateInstances() {
      for (int iTempIndex = 0; iTempIndex < 2; iTempIndex++) {
         if (instanceArray[iTempIndex] != null) {
            instanceArray[iTempIndex].update();
         }
      }
   }
}
