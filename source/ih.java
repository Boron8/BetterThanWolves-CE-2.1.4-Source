import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.src.DedicatedServer;

final class ih extends WindowAdapter {
   final DedicatedServer a;

   ih(DedicatedServer par1DedicatedServer) {
      this.a = par1DedicatedServer;
   }

   @Override
   public void windowClosing(WindowEvent par1WindowEvent) {
      this.a.n();

      while (!this.a.ac()) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

      System.exit(0);
   }
}
