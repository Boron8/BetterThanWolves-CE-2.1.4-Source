package net.minecraft.src;

import java.io.File;
import java.io.FilenameFilter;

class AnvilSaveConverterFileFilter implements FilenameFilter {
   AnvilSaveConverterFileFilter(AnvilSaveConverter var1) {
      this.parent = var1;
   }

   @Override
   public boolean accept(File var1, String var2) {
      return var2.endsWith(".mcr");
   }
}
