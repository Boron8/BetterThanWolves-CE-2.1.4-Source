package net.minecraft.client.main;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Session;

@Environment(EnvType.CLIENT)
public class Main {
   private Minecraft mc;
   private Thread mcThread = null;
   private Map arguments;

   public void init(Map par1Map) {
      this.arguments = par1Map;
      boolean var1x = "true".equalsIgnoreCase(this.getParameter("fullscreen"));
      this.mc = new Minecraft(854, 480, var1x, this.getParameter("gameDir"));
      if (this.getParameter("username") != null && this.getParameter("sessionid") != null) {
         this.mc.session = new Session(this.getParameter("username"), this.getParameter("sessionid"));
         this.mc.getLogAgent().logInfo("Setting user: " + this.mc.session.username);
         System.out.println("(Session ID is " + this.mc.session.sessionId + ")");
      } else {
         this.mc.session = new Session("Player", "");
      }

      this.mc.setDemo("true".equals(this.getParameter("demo")));
      if (this.getParameter("server") != null && this.getParameter("port") != null) {
         this.mc.setServer(this.getParameter("server"), Integer.parseInt(this.getParameter("port")));
      }

      this.mc.hideQuitButton = !"true".equals(this.getParameter("stand-alone"));
      this.startMainThread();
   }

   public void startMainThread() {
      if (this.mcThread == null) {
         this.mcThread = new Thread(this.mc, "Minecraft main thread");
         this.mcThread.start();
      }
   }

   public void start() {
      if (this.mc != null) {
         this.mc.isGamePaused = false;
      }
   }

   public void stop() {
      if (this.mc != null) {
         this.mc.isGamePaused = true;
      }
   }

   public void destroy() {
      this.shutdown();
   }

   public void shutdown() {
      if (this.mcThread != null) {
         this.mc.shutdown();

         try {
            this.mcThread.join(10000L);
         } catch (InterruptedException var4) {
            try {
               this.mc.shutdownMinecraftApplet();
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }

         this.mcThread = null;
      }
   }

   public String getParameter(String par1Str) {
      if (this.arguments.containsKey(par1Str)) {
         return (String)this.arguments.get(par1Str);
      } else {
         System.err.println("Client asked for parameter: " + par1Str);
         return null;
      }
   }

   public static void main(String[] args) {
      Minecraft.main(args);
   }
}
