package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

@Environment(EnvType.CLIENT)
public class IntegratedServer extends MinecraftServer {
   private final Minecraft mc;
   private final WorldSettings theWorldSettings;
   private final ILogAgent serverLogAgent = new LogAgent(
      "Minecraft-Server", " [SERVER]", new File(Minecraft.getMinecraftDir(), "output-server.log").getAbsolutePath()
   );
   private IntegratedServerListenThread theServerListeningThread;
   private boolean isGamePaused = false;
   private boolean isPublic;
   private ThreadLanServerPing lanServerPing;

   public IntegratedServer(Minecraft par1Minecraft, String par2Str, String par3Str, WorldSettings par4WorldSettings) {
      super(new File(Minecraft.getMinecraftDir(), "saves"));
      this.k(par1Minecraft.session.username);
      this.l(par2Str);
      this.m(par3Str);
      this.b(par1Minecraft.isDemo());
      this.c(par4WorldSettings.isBonusChestEnabled());
      this.d(256);
      this.a(new IntegratedPlayerList(this));
      this.mc = par1Minecraft;
      this.theWorldSettings = par4WorldSettings;
      this.setDifficulty(this.theWorldSettings.getDifficulty());

      try {
         this.theServerListeningThread = new IntegratedServerListenThread(this);
      } catch (IOException var6) {
         throw new Error();
      }
   }

   @Override
   protected void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str) {
      this.b(par1Str);
      this.worldServers = new WorldServer[3];
      this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
      ISaveHandler var7 = this.N().getSaveLoader(par1Str, true);

      for (int var8 = 0; var8 < this.worldServers.length; var8++) {
         byte var9 = 0;
         if (var8 == 1) {
            var9 = -1;
         }

         if (var8 == 2) {
            var9 = 1;
         }

         if (var8 == 0) {
            if (this.M()) {
               this.worldServers[var8] = new DemoWorldServer(this, var7, par2Str, var9, this.theProfiler, this.getLogAgent());
            } else {
               this.worldServers[var8] = new WorldServer(this, var7, par2Str, var9, this.theWorldSettings, this.theProfiler, this.getLogAgent());
            }
         } else {
            this.worldServers[var8] = new WorldServerMulti(
               this, var7, par2Str, var9, this.theWorldSettings, this.worldServers[0], this.theProfiler, this.getLogAgent()
            );
         }

         this.worldServers[var8].a(new WorldManager(this, this.worldServers[var8]));
         this.ad().setPlayerManager(this.worldServers);
      }

      this.setDifficultyForAllWorlds(this.theWorldSettings.getDifficulty());
      this.e();
   }

   @Override
   protected boolean startServer() throws IOException {
      this.serverLogAgent.logInfo("Starting integrated minecraft server version 1.5.2");
      this.d(false);
      this.e(true);
      this.f(true);
      this.g(true);
      this.h(true);
      this.serverLogAgent.logInfo("Generating keypair");
      this.a(CryptManager.createNewKeyPair());
      this.loadAllWorlds(this.J(), this.K(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.func_82749_j());
      this.o(this.H() + " - " + this.worldServers[0].M().getWorldName());
      return true;
   }

   @Override
   public void tick() {
      boolean var1 = this.isGamePaused;
      this.isGamePaused = this.theServerListeningThread.isGamePaused();
      if (!var1 && this.isGamePaused) {
         this.serverLogAgent.logInfo("Saving and pausing game...");
         this.ad().saveAllPlayerData();
         this.a(false);
      }

      if (!this.isGamePaused) {
         super.tick();
      } else {
         for (int iTempWorldIndex = 0; iTempWorldIndex < this.worldServers.length; iTempWorldIndex++) {
            if (iTempWorldIndex == 0 || this.s()) {
               WorldServer tempWorldServer = this.worldServers[iTempWorldIndex];
               if (!tempWorldServer.playerEntities.isEmpty()) {
                  for (EntityPlayer tempPlayer : tempWorldServer.playerEntities) {
                     if (tempPlayer instanceof EntityPlayerMP) {
                        EntityPlayerMP tempPlayerMP = (EntityPlayerMP)tempPlayer;
                        tempPlayerMP.sendChunksToClient();
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean canStructuresSpawn() {
      return false;
   }

   @Override
   public EnumGameType getGameType() {
      return this.theWorldSettings.getGameType();
   }

   @Override
   public int getDifficulty() {
      return 2;
   }

   @Override
   public boolean isHardcore() {
      return this.theWorldSettings.getHardcoreEnabled();
   }

   @Override
   protected File getDataDirectory() {
      return this.mc.mcDataDir;
   }

   @Override
   public boolean isDedicatedServer() {
      return false;
   }

   public IntegratedServerListenThread getServerListeningThread() {
      return this.theServerListeningThread;
   }

   @Override
   protected void finalTick(CrashReport par1CrashReport) {
      this.mc.crashed(par1CrashReport);
   }

   @Override
   public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport) {
      par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
      par1CrashReport.func_85056_g().addCrashSectionCallable("Type", new CallableType3(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Is Modded", new CallableIsModded(this));
      return par1CrashReport;
   }

   @Override
   public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper) {
      super.addServerStatsToSnooper(par1PlayerUsageSnooper);
      par1PlayerUsageSnooper.addData("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
   }

   @Override
   public boolean isSnooperEnabled() {
      return Minecraft.getMinecraft().isSnooperEnabled();
   }

   @Override
   public String shareToLAN(EnumGameType par1EnumGameType, boolean par2) {
      try {
         String var3 = this.theServerListeningThread.func_71755_c();
         this.getLogAgent().logInfo("Started on " + var3);
         this.isPublic = true;
         this.lanServerPing = new ThreadLanServerPing(this.aa(), var3);
         this.lanServerPing.start();
         this.ad().setGameType(par1EnumGameType);
         this.ad().setCommandsAllowedForAll(par2);
         return var3;
      } catch (IOException var41) {
         return null;
      }
   }

   @Override
   public ILogAgent getLogAgent() {
      return this.serverLogAgent;
   }

   @Override
   public void stopServer() {
      super.stopServer();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }
   }

   @Override
   public void initiateShutdown() {
      super.initiateShutdown();
      if (this.lanServerPing != null) {
         this.lanServerPing.interrupt();
         this.lanServerPing = null;
      }
   }

   public boolean getPublic() {
      return this.isPublic;
   }

   @Override
   public void setGameType(EnumGameType par1EnumGameType) {
      this.ad().setGameType(par1EnumGameType);
   }

   @Override
   public boolean isCommandBlockEnabled() {
      return true;
   }

   @Override
   public NetworkListenThread getNetworkThread() {
      return this.getServerListeningThread();
   }
}
