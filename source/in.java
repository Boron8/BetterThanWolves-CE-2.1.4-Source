import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;

public class in extends Handler {
   private int[] b = new int[1024];
   private int c = 0;
   Formatter a = new io(this);
   private JTextArea d;

   public in(JTextArea par1JTextArea) {
      this.setFormatter(this.a);
      this.d = par1JTextArea;
   }

   @Override
   public void close() {
   }

   @Override
   public void flush() {
   }

   @Override
   public void publish(LogRecord par1LogRecord) {
      int var2 = this.d.getDocument().getLength();
      this.d.append(this.a.format(par1LogRecord));
      this.d.setCaretPosition(this.d.getDocument().getLength());
      int var3 = this.d.getDocument().getLength() - var2;
      if (this.b[this.c] != 0) {
         this.d.replaceRange("", 0, this.b[this.c]);
      }

      this.b[this.c] = var3;
      this.c = (this.c + 1) % 1024;
   }
}
