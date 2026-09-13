package net.minecraft.src;

public class GuiProgress extends GuiScreen implements IProgressUpdate {
   private String progressMessage = "";
   private String workingMessage = "";
   private int currentProgress = 0;
   private boolean noMoreProgress;

   @Override
   public void displayProgressMessage(String var1) {
      this.resetProgressAndMessage(var1);
   }

   @Override
   public void resetProgressAndMessage(String var1) {
      this.progressMessage = var1;
      this.resetProgresAndWorkingMessage("Working...");
   }

   @Override
   public void resetProgresAndWorkingMessage(String var1) {
      this.workingMessage = var1;
      this.setLoadingProgress(0);
   }

   @Override
   public void setLoadingProgress(int var1) {
      this.currentProgress = var1;
   }

   @Override
   public void onNoMoreProgress() {
      this.noMoreProgress = true;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      if (this.noMoreProgress) {
         this.mc.displayGuiScreen(null);
      } else {
         this.e();
         this.a(this.fontRenderer, this.progressMessage, this.width / 2, 70, 16777215);
         this.a(this.fontRenderer, this.workingMessage + " " + this.currentProgress + "%", this.width / 2, 90, 16777215);
         super.drawScreen(var1, var2, var3);
      }
   }
}
