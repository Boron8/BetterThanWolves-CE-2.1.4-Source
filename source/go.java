import net.minecraft.src.DedicatedServer;

public final class go extends Thread {
   final DedicatedServer a;

   public go(DedicatedServer par1) {
      this.a = par1;
   }

   @Override
   public void run() {
      this.a.k();
   }
}
