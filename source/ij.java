import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ij extends FocusAdapter {
   final ig a;

   ij(ig par1ServerGUI) {
      this.a = par1ServerGUI;
   }

   @Override
   public void focusGained(FocusEvent par1FocusEvent) {
   }
}
