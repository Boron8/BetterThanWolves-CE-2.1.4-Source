package btw.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class StokedFireTexture extends FireTexture {
   public StokedFireTexture(String sName, int iFireAnimationIndex) {
      super(sName, iFireAnimationIndex);
      this.fireAnimationIndex = iFireAnimationIndex;
   }

   @Override
   public void updateAnimation() {
      this.frameCounter = 0;
      if (this.fireAnimation != null) {
         this.fireAnimation.copyStokedFireFrameToByteBuffer(this.frameBuffer, this.bufferPixelSize);
      }

      this.textureSheet.uploadByteBufferToGPU(this.originX, this.originY, this.frameBuffer, this.bufferWidth, this.bufferHeight);
   }

   @Override
   public boolean isProcedurallyAnimated() {
      return true;
   }
}
