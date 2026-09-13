package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public abstract class TexturePackImplementation implements ITexturePack {
   private final String texturePackID;
   private final String texturePackFileName;
   public final File texturePackFile;
   protected String firstDescriptionLine;
   protected String secondDescriptionLine;
   private final ITexturePack field_98141_g;
   protected BufferedImage thumbnailImage;
   private int thumbnailTextureName = -1;

   protected TexturePackImplementation(String par1, File par2File, String par3Str, ITexturePack par4ITexturePack) {
      this.texturePackID = par1;
      this.texturePackFileName = par3Str;
      this.texturePackFile = par2File;
      this.field_98141_g = par4ITexturePack;
      this.loadThumbnailImage();
      this.loadDescription();
   }

   private static String trimStringToGUIWidth(String par0Str) {
      if (par0Str != null && par0Str.length() > 34) {
         par0Str = par0Str.substring(0, 34);
      }

      return par0Str;
   }

   private void loadThumbnailImage() {
      InputStream var1 = null;

      try {
         var1 = this.func_98137_a("/pack.png", false);
         this.thumbnailImage = ImageIO.read(var1);
      } catch (IOException var11) {
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var10) {
         }
      }
   }

   protected void loadDescription() {
      InputStream var1 = null;
      BufferedReader var2 = null;

      try {
         var1 = this.func_98139_b("/pack.txt");
         var2 = new BufferedReader(new InputStreamReader(var1));
         this.firstDescriptionLine = trimStringToGUIWidth(var2.readLine());
         this.secondDescriptionLine = trimStringToGUIWidth(var2.readLine());
      } catch (IOException var12) {
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }

            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var11) {
         }
      }
   }

   @Override
   public InputStream func_98137_a(String par1Str, boolean par2) throws IOException {
      try {
         return this.func_98139_b(par1Str);
      } catch (IOException var4) {
         if (this.field_98141_g != null && par2) {
            return this.field_98141_g.func_98137_a(par1Str, true);
         } else {
            throw var4;
         }
      }
   }

   @Override
   public InputStream getResourceAsStream(String par1Str) throws IOException {
      return this.func_98137_a(par1Str, true);
   }

   protected abstract InputStream func_98139_b(String var1) throws IOException;

   @Override
   public void deleteTexturePack(RenderEngine par1RenderEngine) {
      if (this.thumbnailImage != null && this.thumbnailTextureName != -1) {
         par1RenderEngine.deleteTexture(this.thumbnailTextureName);
      }
   }

   @Override
   public void bindThumbnailTexture(RenderEngine par1RenderEngine) {
      if (this.thumbnailImage != null) {
         if (this.thumbnailTextureName == -1) {
            this.thumbnailTextureName = par1RenderEngine.allocateAndSetupTexture(this.thumbnailImage);
         }

         GL11.glBindTexture(3553, this.thumbnailTextureName);
         par1RenderEngine.resetBoundTexture();
      } else {
         par1RenderEngine.bindTexture("/gui/unknown_pack.png");
      }
   }

   @Override
   public boolean func_98138_b(String par1Str, boolean par2) {
      boolean var3 = this.func_98140_c(par1Str);
      return !var3 && par2 && this.field_98141_g != null ? this.field_98141_g.func_98138_b(par1Str, par2) : var3;
   }

   public abstract boolean func_98140_c(String var1);

   @Override
   public String getTexturePackID() {
      return this.texturePackID;
   }

   @Override
   public String getTexturePackFileName() {
      return this.texturePackFileName;
   }

   @Override
   public String getFirstDescriptionLine() {
      return this.firstDescriptionLine;
   }

   @Override
   public String getSecondDescriptionLine() {
      return this.secondDescriptionLine;
   }
}
