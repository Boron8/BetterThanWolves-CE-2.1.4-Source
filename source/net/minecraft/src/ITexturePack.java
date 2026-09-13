package net.minecraft.src;

import java.io.InputStream;

public interface ITexturePack {
   void deleteTexturePack(RenderEngine var1);

   void bindThumbnailTexture(RenderEngine var1);

   InputStream func_98137_a(String var1, boolean var2);

   InputStream getResourceAsStream(String var1);

   String getTexturePackID();

   String getTexturePackFileName();

   String getFirstDescriptionLine();

   String getSecondDescriptionLine();

   boolean func_98138_b(String var1, boolean var2);

   boolean isCompatible();
}
