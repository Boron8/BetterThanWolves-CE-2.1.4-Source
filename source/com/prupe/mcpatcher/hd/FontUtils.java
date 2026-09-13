package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.FontRenderer;

@Environment(EnvType.CLIENT)
public class FontUtils {
   private static final MCLogger logger = MCLogger.getLogger("HD Font");
   private static final boolean enable = Config.getBoolean("Extended HD", "hdFont", true);
   private static final boolean enableNonHD = Config.getBoolean("Extended HD", "nonHDFontWidth", false);
   private static final int ROWS = 16;
   private static final int COLS = 16;
   public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
   public static final int[] SPACERS = new int[]{33721342, 41975936, 234881023};
   private static final boolean showLines = false;
   private static final Set<FontRenderer> allRenderers = new HashSet<>();

   static void init() {
   }

   public static FakeResourceLocation getFontName(FontRenderer fontRenderer, FakeResourceLocation font, float hdFontAdj) {
      if (fontRenderer.getDefaultFont() == null) {
         fontRenderer.setDefaultFont(font);
      }

      FakeResourceLocation defaultFont = fontRenderer.getDefaultFont();
      if (fontRenderer.getHDFont() == null) {
         String namespace = defaultFont.getNamespace();
         String name = defaultFont.getPath().replaceAll(".*/", "");
         fontRenderer.setHDFont(new FakeResourceLocation(namespace, "/font/" + name));
      }

      FakeResourceLocation hdFont = fontRenderer.getHDFont();
      FakeResourceLocation newFont;
      if (enable && TexturePackAPI.hasResource(hdFont)) {
         if (!hdFont.equals(defaultFont)) {
            logger.fine("using %s instead of %s", hdFont, defaultFont);
         }

         fontRenderer.isHD = true;
         newFont = hdFont;
      } else {
         logger.fine("using default %s", defaultFont);
         fontRenderer.isHD = enable && enableNonHD;
         newFont = defaultFont;
      }

      fontRenderer.fontAdj = fontRenderer.isHD ? hdFontAdj : 1.0F;
      return newFont;
   }

   public static float[] computeCharWidthsf(FontRenderer fontRenderer, FakeResourceLocation filename, BufferedImage image, int[] rgb, int[] charWidth) {
      float[] charWidthf = new float[charWidth.length];
      if (!fontRenderer.isHD) {
         for (int i = 0; i < charWidth.length; i++) {
            charWidthf[i] = charWidth[i];
         }

         charWidthf[32] = 4.0F;
         return charWidthf;
      } else {
         allRenderers.add(fontRenderer);
         int width = image.getWidth();
         int height = image.getHeight();
         int colWidth = width / 16;
         int rowHeight = height / 16;

         label87:
         for (int ch = 0; ch < charWidth.length; ch++) {
            int row = ch / 16;
            int col = ch % 16;

            for (int colIdx = colWidth - 1; colIdx >= 0; colIdx--) {
               int x = col * colWidth + colIdx;

               for (int rowIdx = 0; rowIdx < rowHeight; rowIdx++) {
                  int y = row * rowHeight + rowIdx;
                  int pixel = rgb[x + y * width];
                  if (isOpaque(pixel)) {
                     if (printThis(ch)) {
                        logger.finer("%d '%c' pixel (%d, %d) = %08x, colIdx = %d", ch, (char)ch, x, y, pixel, colIdx);
                     }

                     charWidthf[ch] = 128.0F * (colIdx + 1) / width + 1.0F;
                     continue label87;
                  }
               }
            }
         }

         for (int ch = 0; ch < charWidthf.length; ch++) {
            if (charWidthf[ch] <= 0.0F) {
               charWidthf[ch] = 2.0F;
            } else if (charWidthf[ch] >= 7.99F) {
               charWidthf[ch] = 7.99F;
            }
         }

         boolean[] isOverride = new boolean[charWidth.length];

         try {
            getCharWidthOverrides(filename, charWidthf, isOverride);
         } catch (Throwable var18) {
            var18.printStackTrace();
         }

         if (!isOverride[32]) {
            charWidthf[32] = defaultSpaceWidth(charWidthf);
         }

         for (int chx = 0; chx < charWidth.length; chx++) {
            charWidth[chx] = Math.round(charWidthf[chx]);
            if (printThis(chx)) {
               logger.finer("charWidth[%d '%c'] = %f", chx, (char)chx, charWidthf[chx]);
            }
         }

         return charWidthf;
      }
   }

   private static float getCharWidthf(FontRenderer fontRenderer, char ch) {
      float width = fontRenderer.getCharWidth(ch);
      return !(width < 0.0F) && fontRenderer.charWidthf != null && ch < fontRenderer.charWidthf.length ? fontRenderer.charWidthf[ch] : width;
   }

   public static float getCharWidthf(FontRenderer fontRenderer, int[] charWidth, int ch) {
      return fontRenderer.isHD ? fontRenderer.charWidthf[ch] * fontRenderer.FONT_HEIGHT / 8.0F : charWidth[ch];
   }

   public static float getStringWidthf(FontRenderer fontRenderer, String s) {
      float totalWidth = 0.0F;
      if (s != null) {
         boolean isLink = false;

         for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            float cWidth = getCharWidthf(fontRenderer, c);
            if (cWidth < 0.0F && i < s.length() - 1) {
               c = s.charAt(++i);
               if (c == 'l' || c == 'L') {
                  isLink = true;
               } else if (c == 'r' || c == 'R') {
                  isLink = false;
               }

               cWidth = 0.0F;
            }

            totalWidth += cWidth;
            if (isLink) {
               totalWidth++;
            }
         }
      }

      return totalWidth;
   }

   public static FakeResourceLocation getUnicodePage(FakeResourceLocation resource) {
      if (enable && resource != null) {
         FakeResourceLocation newResource = new FakeResourceLocation(resource.getNamespace(), resource.getPath().replaceFirst("^textures/", "mcpatcher/"));
         if (!newResource.equals(resource) && TexturePackAPI.hasResource(newResource)) {
            logger.fine("using %s instead of %s", newResource, resource);
            return newResource;
         }
      }

      return resource;
   }

   private static boolean isOpaque(int pixel) {
      for (int i : SPACERS) {
         if (pixel == i) {
            return false;
         }
      }

      return (pixel >> 24 & 240) > 0;
   }

   private static boolean printThis(int ch) {
      return "ABCDEF abcdef0123456789".indexOf(ch) >= 0;
   }

   private static float defaultSpaceWidth(float[] charWidthf) {
      if (TexturePackAPI.isDefaultTexturePack()) {
         return 4.0F;
      } else {
         float sum = 0.0F;
         int n = 0;

         for (char ch : AVERAGE_CHARS) {
            if (charWidthf[ch] > 0.0F) {
               sum += charWidthf[ch];
               n++;
            }
         }

         return n > 0 ? sum / n * 7.0F / 12.0F : 4.0F;
      }
   }

   private static void getCharWidthOverrides(FakeResourceLocation font, float[] charWidthf, boolean[] isOverride) {
      FakeResourceLocation textFile = TexturePackAPI.transformResourceLocation(font, ".png", ".properties");
      PropertiesFile props = PropertiesFile.get(logger, textFile);
      if (props != null) {
         logger.fine("reading character widths from %s", textFile);

         for (Entry<String, String> entry : props.entrySet()) {
            String key = entry.getKey().trim();
            String value = entry.getValue().trim();
            if (key.matches("^width\\.\\d+$") && !value.equals("")) {
               try {
                  int ch = Integer.parseInt(key.substring(6));
                  float width = Float.parseFloat(value);
                  if (ch >= 0 && ch < charWidthf.length) {
                     logger.finer("setting charWidthf[%d '%c'] to %f", ch, (char)ch, width);
                     charWidthf[ch] = width;
                     isOverride[ch] = true;
                  }
               } catch (NumberFormatException var11) {
               }
            }
         }
      }
   }

   static {
      TexturePackChangeHandler.register(new TexturePackChangeHandler("HD Font", 1) {
         @Override
         public void initialize() {
         }

         @Override
         public void beforeChange() {
         }

         @Override
         public void afterChange() {
            for (FontRenderer renderer : FontUtils.allRenderers) {
               renderer.readFontData();
            }
         }
      });
   }
}
