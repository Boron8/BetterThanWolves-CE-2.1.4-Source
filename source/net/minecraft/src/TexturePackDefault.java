package net.minecraft.src;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class TexturePackDefault extends TexturePackImplementation {
   public TexturePackDefault() {
      super("default", null, "Default", null);
   }

   @Override
   protected void loadDescription() {
      this.firstDescriptionLine = "The default look of Minecraft";
   }

   @Override
   public boolean func_98140_c(String var1) {
      return TexturePackDefault.class.getResourceAsStream(var1) != null;
   }

   @Override
   public boolean isCompatible() {
      return true;
   }

   @Override
   protected InputStream func_98139_b(String var1) {
      InputStream var2 = TexturePackDefault.class.getResourceAsStream(var1);
      if (var2 == null) {
         throw new FileNotFoundException(var1);
      } else {
         return var2;
      }
   }
}
