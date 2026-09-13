package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ConvertingProgressUpdate implements IProgressUpdate {
   private long field_96245_b;

   public ConvertingProgressUpdate(MinecraftServer var1) {
      this.mcServer = var1;
      this.field_96245_b = System.currentTimeMillis();
   }

   @Override
   public void displayProgressMessage(String var1) {
   }

   @Override
   public void resetProgressAndMessage(String var1) {
   }

   @Override
   public void setLoadingProgress(int var1) {
      if (System.currentTimeMillis() - this.field_96245_b >= 1000L) {
         this.field_96245_b = System.currentTimeMillis();
         this.mcServer.getLogAgent().logInfo("Converting... " + var1 + "%");
      }
   }

   @Override
   public void onNoMoreProgress() {
   }

   @Override
   public void resetProgresAndWorkingMessage(String var1) {
   }
}
