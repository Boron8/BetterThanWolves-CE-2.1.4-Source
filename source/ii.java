import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

class ii implements ActionListener {
   final JTextField a;
   final ig b;

   ii(ig par1ServerGUI, JTextField par2JTextField) {
      this.b = par1ServerGUI;
      this.a = par2JTextField;
   }

   @Override
   public void actionPerformed(ActionEvent par1ActionEvent) {
      String var2 = this.a.getText().trim();
      if (var2.length() > 0) {
         ig.a(this.b).addPendingCommand(var2, MinecraftServer.getServer());
      }

      this.a.setText("");
   }
}
