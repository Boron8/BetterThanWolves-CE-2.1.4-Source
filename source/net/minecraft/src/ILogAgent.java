package net.minecraft.src;

import java.util.logging.Logger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ILogAgent {
   Logger a();

   void logInfo(String var1);

   void logWarning(String var1);

   void logWarningFormatted(String var1, Object... var2);

   void logWarningException(String var1, Throwable var2);

   void logSevere(String var1);

   void logSevereException(String var1, Throwable var2);

   @Environment(EnvType.CLIENT)
   void logFine(String var1);
}
