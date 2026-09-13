package btw.client.texture;

import java.nio.ByteBuffer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureStitched;

@Environment(EnvType.CLIENT)
public class FireTexture extends TextureStitched {
   ByteBuffer frameBuffer;
   int bufferWidth;
   int bufferHeight;
   int bufferPixelSize;
   int fireAnimationIndex;
   FireAnimation fireAnimation = null;

   public FireTexture(String sName, int iFireAnimationIndex) {
      super(sName);
      this.fireAnimationIndex = iFireAnimationIndex;
   }

   @Override
   public void init(Texture par1Texture, List par2List, int par3, int par4, int par5, int par6, boolean par7) {
      super.init(par1Texture, par2List, par3, par4, par5, par6, par7);
      this.bufferWidth = ((Texture)this.textureList.get(0)).getWidth();
      this.bufferHeight = ((Texture)this.textureList.get(0)).getHeight();
      this.bufferPixelSize = this.bufferWidth * this.bufferHeight;
      this.frameBuffer = ByteBuffer.allocateDirect(this.bufferPixelSize * 4);
      this.fireAnimation = FireAnimation.instanceArray[this.fireAnimationIndex];
      if (this.fireAnimation == null) {
         this.fireAnimation = new FireAnimation(this.fireAnimationIndex, this.bufferWidth, this.bufferHeight);
      }
   }

   @Override
   public void updateAnimation() {
      this.frameCounter = 0;
      if (this.fireAnimation != null) {
         this.fireAnimation.copyRegularFireFrameToByteBuffer(this.frameBuffer, this.bufferPixelSize);
      }

      this.textureSheet.uploadByteBufferToGPU(this.originX, this.originY, this.frameBuffer, this.bufferWidth, this.bufferHeight);
   }

   @Override
   public boolean isProcedurallyAnimated() {
      return true;
   }
}
