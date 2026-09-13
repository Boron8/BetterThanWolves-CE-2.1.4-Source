import java.util.Vector;
import javax.swing.JList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IUpdatePlayerListBox;

public class ik extends JList implements IUpdatePlayerListBox {
   private MinecraftServer a;
   private int b = 0;

   public ik(MinecraftServer par1MinecraftServer) {
      this.a = par1MinecraftServer;
      par1MinecraftServer.a(this);
   }

   @Override
   public void update() {
      if (this.b++ % 20 == 0) {
         Vector var1 = new Vector();

         for (int var2 = 0; var2 < this.a.getConfigurationManager().playerEntityList.size(); var2++) {
            var1.add(((EntityPlayerMP)this.a.getConfigurationManager().playerEntityList.get(var2)).username);
         }

         this.setListData(var1);
      }
   }
}
