import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
import javax.swing.JComponent;
import javax.swing.Timer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.TcpConnection;

public class il extends JComponent {
   private static final DecimalFormat a = new DecimalFormat("########0.000");
   private int[] b = new int[256];
   private int c = 0;
   private String[] d = new String[11];
   private final MinecraftServer e;

   public il(MinecraftServer par1MinecraftServer) {
      this.e = par1MinecraftServer;
      this.setPreferredSize(new Dimension(456, 246));
      this.setMinimumSize(new Dimension(456, 246));
      this.setMaximumSize(new Dimension(456, 246));
      new Timer(500, new im(this)).start();
      this.setBackground(Color.BLACK);
   }

   private void a() {
      long var1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      System.gc();
      this.d[0] = "Memory use: " + var1 / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
      this.d[1] = "Threads: " + TcpConnection.field_74471_a.get() + " + " + TcpConnection.field_74469_b.get();
      this.d[2] = "Avg tick: " + a.format(this.a(this.e.tickTimeArray) * 1.0E-6) + " ms";
      this.d[3] = "Avg sent: " + (int)this.a(this.e.sentPacketCountArray) + ", Avg size: " + (int)this.a(this.e.sentPacketSizeArray);
      this.d[4] = "Avg rec: " + (int)this.a(this.e.receivedPacketCountArray) + ", Avg size: " + (int)this.a(this.e.receivedPacketSizeArray);
      if (this.e.worldServers != null) {
         for (int var3 = 0; var3 < this.e.worldServers.length; var3++) {
            this.d[5 + var3] = "Lvl " + var3 + " tick: " + a.format(this.a(this.e.timeOfLastDimensionTick[var3]) * 1.0E-6) + " ms";
            if (this.e.worldServers[var3] != null && this.e.worldServers[var3].theChunkProviderServer != null) {
               this.d[5 + var3] = this.d[5 + var3] + ", " + this.e.worldServers[var3].theChunkProviderServer.makeString();
               this.d[5 + var3] = this.d[5 + var3]
                  + ", Vec3: "
                  + this.e.worldServers[var3].U().func_82590_d()
                  + " / "
                  + this.e.worldServers[var3].U().getPoolSize();
            }
         }
      }

      this.b[this.c++ & 0xFF] = (int)(this.a(this.e.sentPacketSizeArray) * 100.0 / 12500.0);
      this.repaint();
   }

   private double a(long[] par1ArrayOfLong) {
      long var2 = 0L;

      for (int var4 = 0; var4 < par1ArrayOfLong.length; var4++) {
         var2 += par1ArrayOfLong[var4];
      }

      return (double)var2 / par1ArrayOfLong.length;
   }

   @Override
   public void paint(Graphics par1Graphics) {
      par1Graphics.setColor(new Color(16777215));
      par1Graphics.fillRect(0, 0, 456, 246);

      for (int var2 = 0; var2 < 256; var2++) {
         int var3 = this.b[var2 + this.c & 0xFF];
         par1Graphics.setColor(new Color(var3 + 28 << 16));
         par1Graphics.fillRect(var2, 100 - var3, 1, var3);
      }

      par1Graphics.setColor(Color.BLACK);

      for (int var41 = 0; var41 < this.d.length; var41++) {
         String var4x = this.d[var41];
         if (var4x != null) {
            par1Graphics.drawString(var4x, 32, 116 + var41 * 16);
         }
      }
   }

   static void a(il par0GuiStatsComponent) {
      par0GuiStatsComponent.a();
   }
}
