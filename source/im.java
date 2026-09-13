import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class im implements ActionListener {
   final il a;

   im(il par1GuiStatsComponent) {
      this.a = par1GuiStatsComponent;
   }

   @Override
   public void actionPerformed(ActionEvent par1ActionEvent) {
      il.a(this.a);
   }
}
