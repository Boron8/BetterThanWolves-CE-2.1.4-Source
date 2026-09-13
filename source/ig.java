import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.minecraft.src.DedicatedServer;

public class ig extends JComponent {
   private static boolean a = false;
   private DedicatedServer b;

   public static void a(DedicatedServer par0DedicatedServer) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var3) {
      }

      ig var1 = new ig(par0DedicatedServer);
      a = true;
      JFrame var2 = new JFrame("Minecraft server");
      var2.add(var1);
      var2.pack();
      var2.setLocationRelativeTo((Component)null);
      var2.setVisible(true);
      var2.addWindowListener(new ih(par0DedicatedServer));
   }

   public ig(DedicatedServer par1DedicatedServer) {
      this.b = par1DedicatedServer;
      this.setPreferredSize(new Dimension(854, 480));
      this.setLayout(new BorderLayout());

      try {
         this.add(this.d(), "Center");
         this.add(this.b(), "West");
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private JComponent b() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.add(new il(this.b), "North");
      var1.add(this.c(), "Center");
      var1.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
      return var1;
   }

   private JComponent c() {
      ik var1 = new ik(this.b);
      JScrollPane var2 = new JScrollPane(var1, 22, 30);
      var2.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
      return var2;
   }

   private JComponent d() {
      JPanel var1 = new JPanel(new BorderLayout());
      JTextArea var2 = new JTextArea();
      this.b.getLogAgent().a().addHandler(new in(var2));
      JScrollPane var3 = new JScrollPane(var2, 22, 30);
      var2.setEditable(false);
      JTextField var4 = new JTextField();
      var4.addActionListener(new ii(this, var4));
      var2.addFocusListener(new ij(this));
      var1.add(var3, "Center");
      var1.add(var4, "South");
      var1.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
      return var1;
   }

   static DedicatedServer a(ig par0ServerGUI) {
      return par0ServerGUI.b;
   }
}
