package net.minecraft.server;

import btw.AddonHandler;
import btw.network.packet.TimerSpeedPacket;
import btw.world.util.difficulty.Difficulties;
import btw.world.util.difficulty.Difficulty;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.CallableIsServerModded;
import net.minecraft.src.CallableServerMemoryStats;
import net.minecraft.src.CallableServerProfiler;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ConvertingProgressUpdate;
import net.minecraft.src.CrashReport;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.DemoWorldServer;
import net.minecraft.src.DispenserBehaviors;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.IPlayerUsage;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.RConConsoleSource;
import net.minecraft.src.ReportedException;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.StatList;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import net.minecraft.src.ThreadMinecraftServer;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public abstract class MinecraftServer implements ICommandSender, Runnable, IPlayerUsage {
   private static MinecraftServer mcServer = null;
   private final ISaveFormat anvilConverterForAnvilFile;
   private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this);
   private final File anvilFile;
   private final List tickables = new ArrayList();
   public final ServerCommandManager commandManager;
   public final Profiler theProfiler = new Profiler();
   private String hostname;
   private int serverPort = -1;
   public WorldServer[] worldServers;
   private ServerConfigurationManager serverConfigManager;
   private boolean serverRunning = true;
   private boolean serverStopped = false;
   private int tickCounter = 0;
   public String currentTask;
   public int percentDone;
   private boolean onlineMode;
   private boolean canSpawnAnimals;
   private boolean canSpawnNPCs;
   private boolean pvpEnabled;
   private boolean allowFlight;
   private String motd;
   private int buildLimit;
   private long lastSentPacketID;
   private long lastSentPacketSize;
   private long lastReceivedID;
   private long lastReceivedSize;
   public final long[] sentPacketCountArray = new long[100];
   public final long[] sentPacketSizeArray = new long[100];
   public final long[] receivedPacketCountArray = new long[100];
   public final long[] receivedPacketSizeArray = new long[100];
   public final long[] tickTimeArray = new long[100];
   public long[][] timeOfLastDimensionTick;
   private KeyPair serverKeyPair;
   private String serverOwner;
   private String folderName;
   @Environment(EnvType.CLIENT)
   private String worldName;
   private boolean isDemo;
   private boolean enableBonusChest;
   private Difficulty difficultyLevel = Difficulties.STANDARD;
   private boolean worldIsBeingDeleted;
   private String texturePack = "";
   private boolean serverIsRunning = false;
   private long timeOfLastWarning;
   private String userMessage;
   private boolean startProfiling;
   private boolean field_104057_T = false;
   private static boolean isServer = false;
   private boolean sendTimerSpeedImmediately = false;

   public MinecraftServer(File par1File) {
      mcServer = this;
      this.anvilFile = par1File;
      this.commandManager = new ServerCommandManager();
      this.anvilConverterForAnvilFile = new AnvilSaveConverter(par1File);
      this.registerDispenseBehaviors();
   }

   private void registerDispenseBehaviors() {
      DispenserBehaviors.func_96467_a();
   }

   protected abstract boolean startServer() throws IOException;

   protected void convertMapIfNeeded(String par1Str) {
      if (this.getActiveAnvilConverter().isOldMapFormat(par1Str)) {
         this.getLogAgent().logInfo("Converting map!");
         this.setUserMessage("menu.convertingLevel");
         this.getActiveAnvilConverter().convertMapFormat(par1Str, new ConvertingProgressUpdate(this));
      }
   }

   protected synchronized void setUserMessage(String par1Str) {
      this.userMessage = par1Str;
   }

   @Environment(EnvType.CLIENT)
   public synchronized String getUserMessage() {
      return this.userMessage;
   }

   protected void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str) {
      this.convertMapIfNeeded(par1Str);
      this.setUserMessage("menu.loadingLevel");
      this.worldServers = new WorldServer[3];
      this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
      ISaveHandler var7 = this.anvilConverterForAnvilFile.getSaveLoader(par1Str, true);
      WorldInfo var9 = var7.loadWorldInfo();
      WorldSettings var8;
      if (var9 == null) {
         var8 = new WorldSettings(par3, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), par5WorldType, this.difficultyLevel);
         var8.func_82750_a(par6Str);
      } else {
         var8 = new WorldSettings(var9);
      }

      this.difficultyLevel = var8.getDifficulty();
      if (this.enableBonusChest) {
         var8.enableBonusChest();
      }

      for (int var10 = 0; var10 < this.worldServers.length; var10++) {
         byte var11 = 0;
         if (var10 == 1) {
            var11 = -1;
         }

         if (var10 == 2) {
            var11 = 1;
         }

         if (var10 == 0) {
            if (this.isDemo()) {
               this.worldServers[var10] = new DemoWorldServer(this, var7, par2Str, var11, this.theProfiler, this.getLogAgent());
            } else {
               this.worldServers[var10] = new WorldServer(this, var7, par2Str, var11, var8, this.theProfiler, this.getLogAgent());
            }
         } else {
            this.worldServers[var10] = new WorldServerMulti(this, var7, par2Str, var11, var8, this.worldServers[0], this.theProfiler, this.getLogAgent());
         }

         this.worldServers[var10].a(new WorldManager(this, this.worldServers[var10]));
         if (!this.isSinglePlayer()) {
            this.worldServers[var10].M().setGameType(this.getGameType());
         }

         this.serverConfigManager.setPlayerManager(this.worldServers);
      }

      this.setDifficultyForAllWorlds(var8.getDifficulty());
      this.initialWorldChunkLoad();
   }

   protected void initialWorldChunkLoad() {
      int var5 = 0;
      this.setUserMessage("menu.generatingTerrain");
      byte var6 = 0;
      this.getLogAgent().logInfo("Preparing start region for level " + var6);
      WorldServer var7 = this.worldServers[var6];
      ChunkCoordinates var8 = var7.J();
      long var9 = System.currentTimeMillis();

      for (int var11 = -192; var11 <= 192 && this.isServerRunning(); var11 += 16) {
         for (int var12 = -192; var12 <= 192 && this.isServerRunning(); var12 += 16) {
            long var13 = System.currentTimeMillis();
            if (var13 - var9 > 1000L) {
               this.outputPercentRemaining("Preparing spawn area", var5 * 100 / 625);
               var9 = var13;
            }

            var5++;
            var7.theChunkProviderServer.loadChunk(var8.posX + var11 >> 4, var8.posZ + var12 >> 4);
         }
      }

      this.clearCurrentTask();
   }

   public abstract boolean canStructuresSpawn();

   public abstract EnumGameType getGameType();

   public abstract int getDifficulty();

   public abstract boolean isHardcore();

   protected void outputPercentRemaining(String par1Str, int par2) {
      this.currentTask = par1Str;
      this.percentDone = par2;
      this.getLogAgent().logInfo(par1Str + ": " + par2 + "%");
   }

   protected void clearCurrentTask() {
      this.currentTask = null;
      this.percentDone = 0;
   }

   protected void saveAllWorlds(boolean par1) {
      if (!this.worldIsBeingDeleted) {
         for (WorldServer var5 : this.worldServers) {
            if (var5 != null) {
               if (!par1) {
                  this.getLogAgent().logInfo("Saving chunks for level '" + var5.M().getWorldName() + "'/" + var5.provider.getDimensionName());
               }

               try {
                  var5.saveAllChunks(true, (IProgressUpdate)null);
               } catch (MinecraftException var7) {
                  this.getLogAgent().logWarning(var7.getMessage());
               }
            }
         }
      }
   }

   public void stopServer() {
      if (!this.worldIsBeingDeleted) {
         this.getLogAgent().logInfo("Stopping server");
         if (this.getNetworkThread() != null) {
            this.getNetworkThread().stopListening();
         }

         if (this.serverConfigManager != null) {
            this.getLogAgent().logInfo("Saving players");
            this.serverConfigManager.saveAllPlayerData();
            this.serverConfigManager.removeAllPlayers();
         }

         this.getLogAgent().logInfo("Saving worlds");
         this.saveAllWorlds(false);

         for (int var1 = 0; var1 < this.worldServers.length; var1++) {
            WorldServer var2 = this.worldServers[var1];
            var2.flush();
         }

         if (this.usageSnooper != null && this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.stopSnooper();
         }
      }
   }

   public String getServerHostname() {
      return this.hostname;
   }

   public void setHostname(String par1Str) {
      this.hostname = par1Str;
   }

   public boolean isServerRunning() {
      return this.serverRunning;
   }

   public void initiateShutdown() {
      this.serverRunning = false;
   }

   @Override
   public void run() {
      try {
         if (this.startServer()) {
            long prevTimeMillis = System.currentTimeMillis();
            long lastTimerUpdateTime = prevTimeMillis;
            int ticksSinceLastTimerUpdate = 0;

            for (long timeSinceLastTick = 0L; this.serverRunning; this.serverIsRunning = true) {
               long currentTimeMillis = System.currentTimeMillis();
               long timeDiff = currentTimeMillis - prevTimeMillis;
               if (timeDiff > 2000L && prevTimeMillis - this.timeOfLastWarning >= 15000L) {
                  this.getLogAgent().logWarning("Can't keep up! Did the system time change, or is the server overloaded?");
                  timeDiff = 2000L;
                  this.timeOfLastWarning = prevTimeMillis;
               }

               if (timeDiff < 0L) {
                  this.getLogAgent().logWarning("Time ran backwards! Did the system time change?");
                  timeDiff = 0L;
               }

               timeSinceLastTick += timeDiff;
               prevTimeMillis = currentTimeMillis;
               float speedModifier = this.getMinSpeedModifier();
               if (speedModifier > 1.0F) {
                  if ((float)timeSinceLastTick > 50.0F / speedModifier) {
                     timeSinceLastTick -= (long)(50.0F / speedModifier);
                     this.tick();
                     ticksSinceLastTimerUpdate++;
                  }
               } else if (timeSinceLastTick > 50L) {
                  timeSinceLastTick -= 50L;
                  this.tick();
                  ticksSinceLastTimerUpdate++;
               }

               currentTimeMillis = System.currentTimeMillis();
               if (currentTimeMillis - lastTimerUpdateTime > 1000L || this.sendTimerSpeedImmediately) {
                  speedModifier = this.getMinSpeedModifier();
                  this.sendTimerSpeedUpdate(Math.min(50.0F / ((float)(currentTimeMillis - lastTimerUpdateTime) / ticksSinceLastTimerUpdate), speedModifier));
                  lastTimerUpdateTime = System.currentTimeMillis();
                  ticksSinceLastTimerUpdate = 0;
                  this.sendTimerSpeedImmediately = false;
               }

               Thread.sleep(1L);
            }
         } else {
            this.finalTick((CrashReport)null);
         }
      } catch (Throwable var52) {
         var52.printStackTrace();
         this.getLogAgent().logSevereException("Encountered an unexpected exception " + var52.getClass().getSimpleName(), var52);
         CrashReport var2 = null;
         if (var52 instanceof ReportedException) {
            var2 = this.addServerInfoToCrashReport(((ReportedException)var52).getCrashReport());
         } else {
            var2 = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var52));
         }

         File var3 = new File(
            new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt"
         );
         if (var2.saveToFile(var3, this.getLogAgent())) {
            this.getLogAgent().logSevere("This crash report has been saved to: " + var3.getAbsolutePath());
         } else {
            this.getLogAgent().logSevere("We were unable to save this crash report to disk.");
         }

         this.finalTick(var2);
      } finally {
         try {
            this.stopServer();
            this.serverStopped = true;
         } catch (Throwable var50) {
            var50.printStackTrace();
         } finally {
            this.systemExitNow();
         }
      }
   }

   protected File getDataDirectory() {
      return new File(".");
   }

   protected void finalTick(CrashReport par1CrashReport) {
   }

   protected void systemExitNow() {
   }

   public void tick() {
      long var1 = System.nanoTime();
      AxisAlignedBB.getAABBPool().cleanPool();
      this.tickCounter++;
      if (this.startProfiling) {
         this.startProfiling = false;
         this.theProfiler.profilingEnabled = true;
         this.theProfiler.clearProfiling();
      }

      this.theProfiler.startSection("root");
      this.updateTimeLightAndEntities();
      if (this.tickCounter % 900 == 0) {
         this.theProfiler.startSection("save");
         this.serverConfigManager.saveAllPlayerData();
         this.saveAllWorlds(true);
         this.theProfiler.endSection();
      }

      this.theProfiler.startSection("tallying");
      this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - var1;
      this.sentPacketCountArray[this.tickCounter % 100] = Packet.sentID - this.lastSentPacketID;
      this.lastSentPacketID = Packet.sentID;
      this.sentPacketSizeArray[this.tickCounter % 100] = Packet.sentSize - this.lastSentPacketSize;
      this.lastSentPacketSize = Packet.sentSize;
      this.receivedPacketCountArray[this.tickCounter % 100] = Packet.receivedID - this.lastReceivedID;
      this.lastReceivedID = Packet.receivedID;
      this.receivedPacketSizeArray[this.tickCounter % 100] = Packet.receivedSize - this.lastReceivedSize;
      this.lastReceivedSize = Packet.receivedSize;
      this.theProfiler.endSection();
      this.theProfiler.startSection("snooper");
      if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100) {
         this.usageSnooper.startSnooper();
      }

      if (this.tickCounter % 6000 == 0) {
         this.usageSnooper.addMemoryStatsToSnooper();
      }

      this.theProfiler.endSection();
      this.theProfiler.endSection();
   }

   public void updateTimeLightAndEntities() {
      this.theProfiler.startSection("levels");

      for (int var1 = 0; var1 < this.worldServers.length; var1++) {
         long var2 = System.nanoTime();
         if (var1 == 0 || this.getAllowNether()) {
            WorldServer var4 = this.worldServers[var1];
            this.theProfiler.startSection(var4.M().getWorldName());
            this.theProfiler.startSection("pools");
            var4.U().clear();
            this.theProfiler.endSection();
            if (this.tickCounter % 20 == 0) {
               this.theProfiler.startSection("timeSync");
               this.serverConfigManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(var4.H(), var4.I()), var4.provider.dimensionId);
               this.theProfiler.endSection();
            }

            this.theProfiler.startSection("tick");

            try {
               var4.tick();
            } catch (Throwable var8) {
               CrashReport var6 = CrashReport.makeCrashReport(var8, "Exception ticking world");
               var4.a(var6);
               throw new ReportedException(var6);
            }

            try {
               var4.updateEntities();
            } catch (Throwable var7) {
               CrashReport var6 = CrashReport.makeCrashReport(var7, "Exception ticking world entities");
               var4.a(var6);
               throw new ReportedException(var6);
            }

            this.theProfiler.endSection();
            this.theProfiler.startSection("tracker");
            var4.getEntityTracker().updateTrackedEntities();
            this.theProfiler.endSection();
            this.theProfiler.endSection();
         }

         this.timeOfLastDimensionTick[var1][this.tickCounter % 100] = System.nanoTime() - var2;
      }

      this.theProfiler.endStartSection("connection");
      this.getNetworkThread().networkTick();
      this.theProfiler.endStartSection("players");
      this.serverConfigManager.sendPlayerInfoToAllPlayers();
      this.theProfiler.endStartSection("tickables");

      for (int var9 = 0; var9 < this.tickables.size(); var9++) {
         ((IUpdatePlayerListBox)this.tickables.get(var9)).update();
      }

      this.theProfiler.endSection();
   }

   public boolean getAllowNether() {
      return true;
   }

   public void a(IUpdatePlayerListBox par1IUpdatePlayerListBox) {
      this.tickables.add(par1IUpdatePlayerListBox);
   }

   public static void main(String[] par0ArrayOfStr) {
      isServer = true;
      StatList.nopInit();
      AddonHandler.initializeLogger();
      ILogAgent var1 = null;

      try {
         boolean var2 = !GraphicsEnvironment.isHeadless();
         String var3 = null;
         String var4 = ".";
         String var5 = null;
         boolean var6 = false;
         boolean var7 = false;
         int var8 = -1;

         for (int var9 = 0; var9 < par0ArrayOfStr.length; var9++) {
            String var10 = par0ArrayOfStr[var9];
            String var11 = var9 == par0ArrayOfStr.length - 1 ? null : par0ArrayOfStr[var9 + 1];
            boolean var12 = false;
            if (var10.equals("nogui") || var10.equals("--nogui")) {
               var2 = false;
            } else if (var10.equals("--port") && var11 != null) {
               var12 = true;

               try {
                  var8 = Integer.parseInt(var11);
               } catch (NumberFormatException var14) {
               }
            } else if (var10.equals("--singleplayer") && var11 != null) {
               var12 = true;
               var3 = var11;
            } else if (var10.equals("--universe") && var11 != null) {
               var12 = true;
               var4 = var11;
            } else if (var10.equals("--world") && var11 != null) {
               var12 = true;
               var5 = var11;
            } else if (var10.equals("--demo")) {
               var6 = true;
            } else if (var10.equals("--bonusChest")) {
               var7 = true;
            }

            if (var12) {
               var9++;
            }
         }

         DedicatedServer var16 = new DedicatedServer(new File(var4));
         var1 = var16.getLogAgent();
         if (var3 != null) {
            var16.k(var3);
         }

         if (var5 != null) {
            var16.l(var5);
         }

         if (var8 >= 0) {
            var16.b(var8);
         }

         if (var6) {
            var16.b(true);
         }

         if (var7) {
            var16.c(true);
         }

         if (var2) {
            var16.ap();
         }

         var16.t();
         Runtime.getRuntime().addShutdownHook(new go(var16));
      } catch (Exception var151) {
         if (var1 != null) {
            var1.logSevereException("Failed to start the minecraft server", var151);
         } else {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Failed to start the minecraft server", (Throwable)var151);
         }
      }
   }

   public void startServerThread() {
      new ThreadMinecraftServer(this, "Server thread").start();
   }

   public File getFile(String par1Str) {
      return new File(this.getDataDirectory(), par1Str);
   }

   public void logInfo(String par1Str) {
      this.getLogAgent().logInfo(par1Str);
   }

   public void logWarning(String par1Str) {
      this.getLogAgent().logWarning(par1Str);
   }

   public WorldServer worldServerForDimension(int par1) {
      return par1 == -1 ? this.worldServers[1] : (par1 == 1 ? this.worldServers[2] : this.worldServers[0]);
   }

   public String getHostname() {
      return this.hostname;
   }

   public int getPort() {
      return this.serverPort;
   }

   public String getServerMOTD() {
      return this.motd;
   }

   public String getMinecraftVersion() {
      return "1.5.2";
   }

   public int getCurrentPlayerCount() {
      return this.serverConfigManager.getCurrentPlayerCount();
   }

   public int getMaxPlayers() {
      return this.serverConfigManager.getMaxPlayers();
   }

   public String[] getAllUsernames() {
      return this.serverConfigManager.getAllUsernames();
   }

   public String getPlugins() {
      return "";
   }

   public String executeCommand(String par1Str) {
      RConConsoleSource.consoleBuffer.resetLog();
      this.commandManager.a(RConConsoleSource.consoleBuffer, par1Str);
      return RConConsoleSource.consoleBuffer.getChatBuffer();
   }

   public boolean isDebuggingEnabled() {
      return false;
   }

   public void logSevere(String par1Str) {
      this.getLogAgent().logSevere(par1Str);
   }

   public void logDebug(String par1Str) {
      if (this.isDebuggingEnabled()) {
         this.getLogAgent().logInfo(par1Str);
      }
   }

   public String getServerModName() {
      return "vanilla";
   }

   public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport) {
      par1CrashReport.func_85056_g().addCrashSectionCallable("Profiler Position", new CallableIsServerModded(this));
      if (this.worldServers != null && this.worldServers.length > 0 && this.worldServers[0] != null) {
         par1CrashReport.func_85056_g().addCrashSectionCallable("Vec3 Pool Size", new CallableServerProfiler(this));
      }

      if (this.serverConfigManager != null) {
         par1CrashReport.func_85056_g().addCrashSectionCallable("Player Count", new CallableServerMemoryStats(this));
      }

      return par1CrashReport;
   }

   public List getPossibleCompletions(ICommandSender par1ICommandSender, String par2Str) {
      ArrayList var3 = new ArrayList();
      if (par2Str.startsWith("/")) {
         par2Str = par2Str.substring(1);
         boolean var10 = !par2Str.contains(" ");
         List var11 = this.commandManager.b(par1ICommandSender, par2Str);
         if (var11 != null) {
            for (String var13 : var11) {
               if (var10) {
                  var3.add("/" + var13);
               } else {
                  var3.add(var13);
               }
            }
         }

         return var3;
      } else {
         String[] var4 = par2Str.split(" ", -1);
         String var5 = var4[var4.length - 1];

         for (String var9 : this.serverConfigManager.getAllUsernames()) {
            if (CommandBase.doesStringStartWith(var5, var9)) {
               var3.add(var9);
            }
         }

         return var3;
      }
   }

   public static MinecraftServer getServer() {
      return mcServer;
   }

   @Override
   public String getCommandSenderName() {
      return "Server";
   }

   @Override
   public void sendChatToPlayer(String par1Str) {
      this.getLogAgent().logInfo(StringUtils.stripControlCodes(par1Str));
   }

   @Override
   public boolean canCommandSenderUseCommand(int par1, String par2Str) {
      return true;
   }

   @Override
   public String translateString(String par1Str, Object... par2ArrayOfObj) {
      return StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj);
   }

   public ICommandManager getCommandManager() {
      return this.commandManager;
   }

   public KeyPair getKeyPair() {
      return this.serverKeyPair;
   }

   public int getServerPort() {
      return this.serverPort;
   }

   public void setServerPort(int par1) {
      this.serverPort = par1;
   }

   public String getServerOwner() {
      return this.serverOwner;
   }

   public void setServerOwner(String par1Str) {
      this.serverOwner = par1Str;
   }

   public boolean isSinglePlayer() {
      return this.serverOwner != null;
   }

   public String getFolderName() {
      return this.folderName;
   }

   public void setFolderName(String par1Str) {
      this.folderName = par1Str;
   }

   @Environment(EnvType.CLIENT)
   public void setWorldName(String par1Str) {
      this.worldName = par1Str;
   }

   @Environment(EnvType.CLIENT)
   public String getWorldName() {
      return this.worldName;
   }

   public void setKeyPair(KeyPair par1KeyPair) {
      this.serverKeyPair = par1KeyPair;
   }

   public void setDifficultyForAllWorlds(Difficulty difficulty) {
      for (int var2 = 0; var2 < this.worldServers.length; var2++) {
         WorldServer var3 = this.worldServers[var2];
         if (var3 != null) {
            var3.worldInfo.setDifficulty(difficulty);
         }
      }
   }

   protected boolean allowSpawnMonsters() {
      return true;
   }

   public boolean isDemo() {
      return this.isDemo;
   }

   public void setDemo(boolean par1) {
      this.isDemo = par1;
   }

   public void canCreateBonusChest(boolean par1) {
      this.enableBonusChest = par1;
   }

   public ISaveFormat getActiveAnvilConverter() {
      return this.anvilConverterForAnvilFile;
   }

   public void deleteWorldAndStopServer() {
      this.worldIsBeingDeleted = true;
      this.getActiveAnvilConverter().flushCache();

      for (int var1 = 0; var1 < this.worldServers.length; var1++) {
         WorldServer var2 = this.worldServers[var1];
         if (var2 != null) {
            var2.flush();
         }
      }

      this.getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].L().getWorldDirectoryName());
      this.initiateShutdown();
   }

   public String getTexturePack() {
      return this.texturePack;
   }

   public void setTexturePack(String par1Str) {
      this.texturePack = par1Str;
   }

   @Override
   public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper) {
      par1PlayerUsageSnooper.addData("whitelist_enabled", false);
      par1PlayerUsageSnooper.addData("whitelist_count", 0);
      par1PlayerUsageSnooper.addData("players_current", this.getCurrentPlayerCount());
      par1PlayerUsageSnooper.addData("players_max", this.getMaxPlayers());
      par1PlayerUsageSnooper.addData("players_seen", this.serverConfigManager.getAvailablePlayerDat().length);
      par1PlayerUsageSnooper.addData("uses_auth", this.onlineMode);
      par1PlayerUsageSnooper.addData("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
      par1PlayerUsageSnooper.addData("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6));
      par1PlayerUsageSnooper.addData("avg_sent_packet_count", (int)MathHelper.average(this.sentPacketCountArray));
      par1PlayerUsageSnooper.addData("avg_sent_packet_size", (int)MathHelper.average(this.sentPacketSizeArray));
      par1PlayerUsageSnooper.addData("avg_rec_packet_count", (int)MathHelper.average(this.receivedPacketCountArray));
      par1PlayerUsageSnooper.addData("avg_rec_packet_size", (int)MathHelper.average(this.receivedPacketSizeArray));
      int var2 = 0;

      for (int var3 = 0; var3 < this.worldServers.length; var3++) {
         if (this.worldServers[var3] != null) {
            WorldServer var4 = this.worldServers[var3];
            WorldInfo var5 = var4.M();
            par1PlayerUsageSnooper.addData("world[" + var2 + "][dimension]", var4.provider.dimensionId);
            par1PlayerUsageSnooper.addData("world[" + var2 + "][mode]", var5.getGameType());
            par1PlayerUsageSnooper.addData("world[" + var2 + "][difficulty]", var4.difficultySetting);
            par1PlayerUsageSnooper.addData("world[" + var2 + "][hardcore]", var5.isHardcoreModeEnabled());
            par1PlayerUsageSnooper.addData("world[" + var2 + "][generator_name]", var5.getTerrainType().getWorldTypeName());
            par1PlayerUsageSnooper.addData("world[" + var2 + "][generator_version]", var5.getTerrainType().getGeneratorVersion());
            par1PlayerUsageSnooper.addData("world[" + var2 + "][height]", this.buildLimit);
            par1PlayerUsageSnooper.addData("world[" + var2 + "][chunks_loaded]", var4.K().getLoadedChunkCount());
            var2++;
         }
      }

      par1PlayerUsageSnooper.addData("worlds", var2);
   }

   @Override
   public void addServerTypeToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper) {
      par1PlayerUsageSnooper.addData("singleplayer", this.isSinglePlayer());
      par1PlayerUsageSnooper.addData("server_brand", this.getServerModName());
      par1PlayerUsageSnooper.addData("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
      par1PlayerUsageSnooper.addData("dedicated", this.isDedicatedServer());
   }

   @Override
   public boolean isSnooperEnabled() {
      return true;
   }

   public int textureSize() {
      return 16;
   }

   public abstract boolean isDedicatedServer();

   public boolean isServerInOnlineMode() {
      return this.onlineMode;
   }

   public void setOnlineMode(boolean par1) {
      this.onlineMode = par1;
   }

   public boolean getCanSpawnAnimals() {
      return this.canSpawnAnimals;
   }

   public void setCanSpawnAnimals(boolean par1) {
      this.canSpawnAnimals = par1;
   }

   public boolean getCanSpawnNPCs() {
      return this.canSpawnNPCs;
   }

   public void setCanSpawnNPCs(boolean par1) {
      this.canSpawnNPCs = par1;
   }

   public boolean isPVPEnabled() {
      return this.pvpEnabled;
   }

   public void setAllowPvp(boolean par1) {
      this.pvpEnabled = par1;
   }

   public boolean isFlightAllowed() {
      return this.allowFlight;
   }

   public void setAllowFlight(boolean par1) {
      this.allowFlight = par1;
   }

   public abstract boolean isCommandBlockEnabled();

   public String getMOTD() {
      return this.motd;
   }

   public void setMOTD(String par1Str) {
      this.motd = par1Str;
   }

   public int getBuildLimit() {
      return this.buildLimit;
   }

   public void setBuildLimit(int par1) {
      this.buildLimit = par1;
   }

   public boolean isServerStopped() {
      return this.serverStopped;
   }

   public ServerConfigurationManager getConfigurationManager() {
      return this.serverConfigManager;
   }

   public void setConfigurationManager(ServerConfigurationManager par1ServerConfigurationManager) {
      this.serverConfigManager = par1ServerConfigurationManager;
   }

   public void setGameType(EnumGameType par1EnumGameType) {
      for (int var2 = 0; var2 < this.worldServers.length; var2++) {
         getServer().worldServers[var2].M().setGameType(par1EnumGameType);
      }
   }

   public abstract NetworkListenThread getNetworkThread();

   @Environment(EnvType.CLIENT)
   public boolean serverIsInRunLoop() {
      return this.serverIsRunning;
   }

   public boolean getGuiEnabled() {
      return false;
   }

   public abstract String shareToLAN(EnumGameType var1, boolean var2);

   public int getTickCounter() {
      return this.tickCounter;
   }

   public void enableProfiling() {
      this.startProfiling = true;
   }

   @Environment(EnvType.CLIENT)
   public PlayerUsageSnooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   @Override
   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(0, 0, 0);
   }

   public int getSpawnProtectionSize() {
      return 16;
   }

   public boolean func_96290_a(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
      return false;
   }

   @Override
   public abstract ILogAgent getLogAgent();

   public void func_104055_i(boolean par1) {
      this.field_104057_T = par1;
   }

   public boolean func_104056_am() {
      return this.field_104057_T;
   }

   public static ServerConfigurationManager getServerConfigurationManager(MinecraftServer par0MinecraftServer) {
      return par0MinecraftServer.serverConfigManager;
   }

   public static boolean getIsServer() {
      return isServer;
   }

   private float getMinSpeedModifier() {
      float minSpeedModifier = Float.MAX_VALUE;

      for (WorldServer world : this.worldServers) {
         if (world != null && !world.playerEntities.isEmpty()) {
            float speedModifier = world.getMinSpeedModifier();
            if (speedModifier < minSpeedModifier) {
               minSpeedModifier = speedModifier;
            }
         }
      }

      return minSpeedModifier == Float.MAX_VALUE ? 1.0F : minSpeedModifier;
   }

   private void sendTimerSpeedUpdate(float speedModifier) {
      speedModifier = Math.max(speedModifier, 1.0F);
      this.getConfigurationManager().sendPacketToAllPlayers(new TimerSpeedPacket(speedModifier));
   }

   public void sendTimerSpeedImmediately() {
      this.sendTimerSpeedImmediately = true;
   }

   public void setDifficultyFromName(String challengeLevel) {
      this.setDifficulty(Difficulties.getDifficultyFromName(challengeLevel));
   }

   public void setDifficulty(Difficulty difficultyLevel) {
      this.difficultyLevel = difficultyLevel;
   }
}
