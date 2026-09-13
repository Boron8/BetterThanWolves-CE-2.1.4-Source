package net.minecraft.src;

import com.prupe.mcpatcher.hd.CustomAnimation;
import com.prupe.mcpatcher.hd.Wrapper15;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderEngine {
   private HashMap textureMap = new HashMap();
   private HashMap textureContentsMap = new HashMap();
   private IntHashMap textureNameToImageMap = new IntHashMap();
   private IntBuffer imageData = GLAllocation.createDirectIntBuffer(4194304);
   private Map urlToImageDataMap = new HashMap();
   private GameSettings options;
   private TexturePackList texturePack;
   private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
   private final TextureMap textureMapBlocks;
   private final TextureMap textureMapItems;
   private int boundTexture;

   public RenderEngine(TexturePackList par1TexturePackList, GameSettings par2GameSettings) {
      this.texturePack = par1TexturePackList;
      this.options = par2GameSettings;
      Graphics var3 = this.missingTextureImage.getGraphics();
      var3.setColor(Color.WHITE);
      var3.fillRect(0, 0, 64, 64);
      var3.setColor(Color.BLACK);
      int var4 = 10;
      int var5 = 0;

      while (var4 < 64) {
         String var6 = var5++ % 2 == 0 ? "missing" : "texture";
         var3.drawString(var6, 1, var4);
         var4 += var3.getFont().getSize();
         if (var5 % 2 == 0) {
            var4 += 5;
         }
      }

      var3.dispose();
      this.textureMapBlocks = new TextureMap(0, "terrain", "textures/blocks/", this.missingTextureImage);
      this.textureMapItems = new TextureMap(1, "items", "textures/items/", this.missingTextureImage);
   }

   public int[] getTextureContents(String par1Str) {
      ITexturePack var2 = this.texturePack.getSelectedTexturePack();
      int[] var3 = (int[])this.textureContentsMap.get(par1Str);
      if (var3 != null) {
         return var3;
      } else {
         try {
            InputStream var7 = var2.getResourceAsStream(par1Str);
            int[] var4;
            if (var7 == null) {
               var4 = this.getImageContentsAndAllocate(this.missingTextureImage);
            } else {
               var4 = this.getImageContentsAndAllocate(this.readTextureImage(var7));
            }

            this.textureContentsMap.put(par1Str, var4);
            return var4;
         } catch (IOException var61) {
            var61.printStackTrace();
            int[] var5 = this.getImageContentsAndAllocate(this.missingTextureImage);
            this.textureContentsMap.put(par1Str, var5);
            return var5;
         }
      }
   }

   private int[] getImageContentsAndAllocate(BufferedImage par1BufferedImage) {
      return this.getImageContents(par1BufferedImage, new int[par1BufferedImage.getWidth() * par1BufferedImage.getHeight()]);
   }

   private int[] getImageContents(BufferedImage par1BufferedImage, int[] par2ArrayOfInteger) {
      if (par1BufferedImage == null) {
         return par2ArrayOfInteger;
      } else {
         int var3 = par1BufferedImage.getWidth();
         int var4 = par1BufferedImage.getHeight();
         par1BufferedImage.getRGB(0, 0, var3, var4, par2ArrayOfInteger, 0, var3);
         return par2ArrayOfInteger;
      }
   }

   public void bindTexture(String par1Str) {
      this.bindTexture(this.getTexture(par1Str));
   }

   public void bindTexture(int par1) {
      if (par1 != this.boundTexture) {
         GL11.glBindTexture(3553, par1);
         this.boundTexture = par1;
      }
   }

   public void resetBoundTexture() {
      this.boundTexture = -1;
   }

   public int getTexture(String par1Str) {
      if (par1Str.equals("/terrain.png")) {
         this.textureMapBlocks.getTexture().bindTexture(0);
         return this.textureMapBlocks.getTexture().getGlTextureId();
      } else if (par1Str.equals("/gui/items.png")) {
         this.textureMapItems.getTexture().bindTexture(0);
         return this.textureMapItems.getTexture().getGlTextureId();
      } else {
         Integer var2 = (Integer)this.textureMap.get(par1Str);
         if (var2 != null) {
            return var2;
         } else {
            String var8 = par1Str;

            try {
               int var3 = GLAllocation.generateTextureNames();
               boolean var9 = par1Str.startsWith("%blur%");
               if (var9) {
                  par1Str = par1Str.substring(6);
               }

               boolean var5 = par1Str.startsWith("%clamp%");
               if (var5) {
                  par1Str = par1Str.substring(7);
               }

               InputStream var6 = this.texturePack.getSelectedTexturePack().getResourceAsStream(par1Str);
               if (var6 == null) {
                  Wrapper15.setupTexture(this, this.missingTextureImage, var3, var9, var5, FakeResourceLocation.wrap(par1Str));
               } else {
                  Wrapper15.setupTexture(this, this.readTextureImage(var6), var3, var9, var5, FakeResourceLocation.wrap(par1Str));
               }

               this.textureMap.put(var8, var3);
               return var3;
            } catch (Exception var8x) {
               var8x.printStackTrace();
               int var4 = GLAllocation.generateTextureNames();
               this.setupTexture(this.missingTextureImage, var4);
               this.textureMap.put(par1Str, var4);
               return var4;
            }
         }
      }
   }

   public int allocateAndSetupTexture(BufferedImage par1BufferedImage) {
      int var2 = GLAllocation.generateTextureNames();
      this.setupTexture(par1BufferedImage, var2);
      this.textureNameToImageMap.addKey(var2, par1BufferedImage);
      return var2;
   }

   public void setupTexture(BufferedImage par1BufferedImage, int par2) {
      this.setupTextureExt(par1BufferedImage, par2, false, false);
   }

   public void setupTextureExt(BufferedImage par1BufferedImage, int par2, boolean par3, boolean par4) {
      if (par1BufferedImage != null) {
         this.bindTexture(par2);
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
         if (par3) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
         }

         if (par4) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
         } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
         }

         int var5 = par1BufferedImage.getWidth();
         int var6 = par1BufferedImage.getHeight();
         int[] var7 = new int[var5 * var6];
         par1BufferedImage.getRGB(0, 0, var5, var6, var7, 0, var5);
         if (this.options != null && this.options.anaglyph) {
            var7 = this.colorToAnaglyph(var7);
         }

         this.imageData = FakeResourceLocation.getIntBuffer(this.imageData, var7);
         GL11.glTexImage2D(3553, 0, 6408, var5, var6, 0, 32993, 33639, this.imageData);
      }
   }

   private int[] colorToAnaglyph(int[] par1ArrayOfInteger) {
      int[] var2 = new int[par1ArrayOfInteger.length];

      for (int var3 = 0; var3 < par1ArrayOfInteger.length; var3++) {
         int var4 = par1ArrayOfInteger[var3] >> 24 & 0xFF;
         int var5 = par1ArrayOfInteger[var3] >> 16 & 0xFF;
         int var6 = par1ArrayOfInteger[var3] >> 8 & 0xFF;
         int var7 = par1ArrayOfInteger[var3] & 0xFF;
         int var8 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
         int var9 = (var5 * 30 + var6 * 70) / 100;
         int var10 = (var5 * 30 + var7 * 70) / 100;
         var2[var3] = var4 << 24 | var8 << 16 | var9 << 8 | var10;
      }

      return var2;
   }

   public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
      this.bindTexture(par4);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10242, 10497);
      GL11.glTexParameteri(3553, 10243, 10497);
      if (this.options != null && this.options.anaglyph) {
         par1ArrayOfInteger = this.colorToAnaglyph(par1ArrayOfInteger);
      }

      this.imageData = FakeResourceLocation.getIntBuffer(this.imageData, par1ArrayOfInteger);
      GL11.glTexSubImage2D(3553, 0, 0, 0, par2, par3, 32993, 33639, this.imageData);
   }

   public void deleteTexture(int par1) {
      this.textureNameToImageMap.removeObject(par1);
      GL11.glDeleteTextures(par1);
   }

   public int getTextureForDownloadableImage(String par1Str, String par2Str) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
      if (var3 != null && var3.image != null && !var3.textureSetupComplete) {
         if (var3.textureName < 0) {
            var3.textureName = this.allocateAndSetupTexture(var3.image);
         } else {
            this.setupTexture(var3.image, var3.textureName);
         }

         var3.textureSetupComplete = true;
      }

      return var3 != null && var3.textureName >= 0 ? var3.textureName : (par2Str == null ? -1 : this.getTexture(par2Str));
   }

   public boolean hasImageData(String par1Str) {
      return this.urlToImageDataMap.containsKey(par1Str);
   }

   public ThreadDownloadImageData obtainImageData(String par1Str, IImageBuffer par2IImageBuffer) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
      if (var3 == null) {
         this.urlToImageDataMap.put(par1Str, new ThreadDownloadImageData(par1Str, par2IImageBuffer));
      } else {
         var3.referenceCount++;
      }

      return var3;
   }

   public void releaseImageData(String par1Str) {
      ThreadDownloadImageData var2 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
      if (var2 != null) {
         var2.referenceCount--;
         if (var2.referenceCount == 0) {
            if (var2.textureName >= 0) {
               this.deleteTexture(var2.textureName);
            }

            this.urlToImageDataMap.remove(par1Str);
         }
      }
   }

   public void updateDynamicTextures() {
      this.textureMapBlocks.updateAnimations();
      this.textureMapItems.updateAnimations();
      CustomAnimation.updateAll();
   }

   public void refreshTextures() {
      TexturePackChangeHandler.beforeChange1();
      ITexturePack var1 = this.texturePack.getSelectedTexturePack();
      this.refreshTextureMaps();

      for (int var3 : this.textureNameToImageMap.getKeySet()) {
         BufferedImage var4 = (BufferedImage)this.textureNameToImageMap.lookup(var3);
         this.setupTexture(var4, var3);
      }

      for (ThreadDownloadImageData var10 : this.urlToImageDataMap.values()) {
         var10.textureSetupComplete = false;
      }

      for (String var11 : this.textureMap.keySet()) {
         try {
            int var12 = (Integer)this.textureMap.get(var11);
            boolean var6 = var11.startsWith("%blur%");
            if (var6) {
               var11 = var11.substring(6);
            }

            boolean var7 = var11.startsWith("%clamp%");
            if (var7) {
               var11 = var11.substring(7);
            }

            BufferedImage var5 = this.readTextureImage(var1.getResourceAsStream(var11));
            Wrapper15.setupTexture(this, var5, var12, var6, var7, FakeResourceLocation.wrap(var11));
         } catch (IOException var11x) {
            var11x.printStackTrace();
         }
      }

      for (String var11 : this.textureContentsMap.keySet()) {
         try {
            BufferedImage var4 = this.readTextureImage(var1.getResourceAsStream(var11));
            this.getImageContents(var4, (int[])this.textureContentsMap.get(var11));
         } catch (IOException var101) {
            var101.printStackTrace();
         }
      }

      Minecraft.getMinecraft().fontRenderer.readFontData();
      Minecraft.getMinecraft().standardGalacticFontRenderer.readFontData();
      TexturePackChangeHandler.afterChange1();
   }

   private BufferedImage readTextureImage(InputStream par1InputStream) throws IOException {
      if (par1InputStream == null) {
         return null;
      } else {
         BufferedImage var2 = ImageIO.read(par1InputStream);
         par1InputStream.close();
         return var2;
      }
   }

   public void refreshTextureMaps() {
      this.textureMapBlocks.refreshTextures();
      this.textureMapItems.refreshTextures();
   }

   public Icon getMissingIcon(int par1) {
      switch (par1) {
         case 0:
            return this.textureMapBlocks.getMissingIcon();
         case 1:
         default:
            return this.textureMapItems.getMissingIcon();
      }
   }
}
