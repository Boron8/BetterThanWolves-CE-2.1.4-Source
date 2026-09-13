package net.minecraft.src;

import btw.AddonHandler;
import btw.BTWMod;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;

public class DedicatedServer extends MinecraftServer implements IServer {
   private final List pendingCommandList = Collections.synchronizedList(new ArrayList());
   private final ILogAgent field_98131_l;
   private RConThreadQuery theRConThreadQuery;
   private RConThreadMain theRConThreadMain;
   private PropertyManager settings;
   private boolean canSpawnStructures;
   private EnumGameType gameType;
   private NetworkListenThread networkThread;
   private boolean guiIsEnabled = false;

   public DedicatedServer(File par1File) {
      super(par1File);
      this.field_98131_l = new LogAgent("Minecraft-Server", (String)null, new File(par1File, "server.log").getAbsolutePath());
      new DedicatedServerSleepThread(this);
   }

   @Override
   protected boolean startServer() throws IOException {
      DedicatedServerCommandThread var1 = new DedicatedServerCommandThread(this);
      var1.setDaemon(true);
      var1.start();
      this.getLogAgent().logInfo("Starting minecraft server version 1.5.2");
      AddonHandler.initializeMods();
      if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
         this.getLogAgent().logWarning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
      }

      this.getLogAgent().logInfo("Loading properties");
      this.settings = new PropertyManager(new File("server.properties"), this.getLogAgent());
      if (this.I()) {
         this.d("127.0.0.1");
      } else {
         this.d(this.settings.getBooleanProperty("online-mode", true));
         this.d(this.settings.getProperty("server-ip", ""));
      }

      this.e(this.settings.getBooleanProperty("spawn-animals", true));
      this.f(this.settings.getBooleanProperty("spawn-npcs", true));
      this.g(this.settings.getBooleanProperty("pvp", true));
      this.h(this.settings.getBooleanProperty("allow-flight", false));
      this.n(this.settings.getProperty("texture-pack", ""));
      this.o(this.settings.getProperty("motd", "A Minecraft Server"));
      this.i(this.settings.getBooleanProperty("force-gamemode", false));
      this.setDifficultyFromName(this.settings.getProperty("difficulty", "standard"));
      this.canSpawnStructures = this.settings.getBooleanProperty("generate-structures", true);
      int var2 = this.settings.getIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
      this.gameType = WorldSettings.getGameTypeById(var2);
      this.getLogAgent().logInfo("Default game type: " + this.gameType);
      InetAddress var3 = null;
      if (this.l().length() > 0) {
         var3 = InetAddress.getByName(this.l());
      }

      if (this.G() < 0) {
         this.b(this.settings.getIntProperty("server-port", 25565));
      }

      this.getLogAgent().logInfo("Generating keypair");
      this.a(CryptManager.createNewKeyPair());
      this.getLogAgent().logInfo("Starting Minecraft server on " + (this.l().length() == 0 ? "*" : this.l()) + ":" + this.G());

      try {
         this.networkThread = new DedicatedServerListenThread(this, var3, this.G());
      } catch (IOException var16) {
         this.getLogAgent().logWarning("**** FAILED TO BIND TO PORT!");
         this.getLogAgent().logWarningFormatted("The exception was: {0}", var16.toString());
         this.getLogAgent().logWarning("Perhaps a server is already running on that port?");
         return false;
      }

      if (!this.U()) {
         this.getLogAgent().logWarning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
         this.getLogAgent().logWarning("The server will make no attempt to authenticate usernames. Beware.");
         this.getLogAgent()
            .logWarning(
               "While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose."
            );
         this.getLogAgent().logWarning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
      }

      this.a(new DedicatedPlayerList(this));
      long var4 = System.nanoTime();
      if (this.J() == null) {
         this.l(this.settings.getProperty("level-name", "world"));
      }

      String var6 = this.settings.getProperty("level-seed", "");
      String var7 = this.settings.getProperty("level-type", "DEFAULT");
      String var8 = this.settings.getProperty("generator-settings", "");
      long var9 = new Random().nextLong();
      if (var6.length() > 0) {
         try {
            long var11 = Long.parseLong(var6);
            if (var11 != 0L) {
               var9 = var11;
            }
         } catch (NumberFormatException var15) {
            var9 = var6.hashCode();
         }
      }

      WorldType var17 = WorldType.parseWorldType(var7);
      if (var17 == null) {
         var17 = WorldType.DEFAULT;
      }

      this.d(this.settings.getIntProperty("max-build-height", 256));
      this.d((this.ab() + 8) / 16 * 16);
      this.d(MathHelper.clamp_int(this.ab(), 64, 256));
      this.settings.setProperty("max-build-height", this.ab());
      this.getLogAgent().logInfo("Preparing level \"" + this.J() + "\"");
      this.a(this.J(), this.J(), var9, var17, var8);
      long var12 = System.nanoTime() - var4;
      String var14 = String.format("%.3fs", var12 / 1.0E9);
      this.getLogAgent().logInfo("Done (" + var14 + ")! For help, type \"help\" or \"?\"");
      if (this.settings.getBooleanProperty("enable-query", false)) {
         this.getLogAgent().logInfo("Starting GS4 status listener");
         this.theRConThreadQuery = new RConThreadQuery(this);
         this.theRConThreadQuery.startThread();
      }

      if (this.settings.getBooleanProperty("enable-rcon", false)) {
         this.getLogAgent().logInfo("Starting remote control listener");
         this.theRConThreadMain = new RConThreadMain(this);
         this.theRConThreadMain.startThread();
      }

      return true;
   }

   @Override
   public boolean canStructuresSpawn() {
      return this.canSpawnStructures;
   }

   @Override
   public EnumGameType getGameType() {
      return this.gameType;
   }

   @Override
   public int getDifficulty() {
      return 2;
   }

   @Override
   public boolean isHardcore() {
      return this.settings.getBooleanProperty("hardcore", false) && BTWMod.allowHardcore;
   }

   @Override
   protected void finalTick(CrashReport par1CrashReport) {
      while (this.m()) {
         this.executePendingCommands();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }
   }

   @Override
   public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport) {
      par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
      par1CrashReport.func_85056_g().addCrashSectionCallable("Is Modded", new CallableType(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Type", new CallableServerType(this));
      return par1CrashReport;
   }

   @Override
   protected void systemExitNow() {
      System.exit(0);
   }

   @Override
   public void updateTimeLightAndEntities() {
      super.updateTimeLightAndEntities();
      this.executePendingCommands();
   }

   @Override
   public boolean getAllowNether() {
      return this.settings.getBooleanProperty("allow-nether", true);
   }

   @Override
   public boolean allowSpawnMonsters() {
      return this.settings.getBooleanProperty("spawn-monsters", true);
   }

   @Override
   public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper) {
      par1PlayerUsageSnooper.addData("whitelist_enabled", this.getDedicatedPlayerList().n());
      par1PlayerUsageSnooper.addData("whitelist_count", this.getDedicatedPlayerList().h().size());
      super.addServerStatsToSnooper(par1PlayerUsageSnooper);
   }

   @Override
   public boolean isSnooperEnabled() {
      return this.settings.getBooleanProperty("snooper-enabled", true);
   }

   public void addPendingCommand(String par1Str, ICommandSender par2ICommandSender) {
      this.pendingCommandList.add(new ServerCommand(par1Str, par2ICommandSender));
   }

   public void executePendingCommands() {
      while (!this.pendingCommandList.isEmpty()) {
         ServerCommand var1 = (ServerCommand)this.pendingCommandList.remove(0);
         this.E().executeCommand(var1.sender, var1.command);
      }
   }

   @Override
   public boolean isDedicatedServer() {
      return true;
   }

   public DedicatedPlayerList getDedicatedPlayerList() {
      return (DedicatedPlayerList)super.getConfigurationManager();
   }

   @Override
   public NetworkListenThread getNetworkThread() {
      return this.networkThread;
   }

   @Override
   public int getIntProperty(String par1Str, int par2) {
      return this.settings.getIntProperty(par1Str, par2);
   }

   @Override
   public String getStringProperty(String par1Str, String par2Str) {
      return this.settings.getProperty(par1Str, par2Str);
   }

   public boolean getBooleanProperty(String par1Str, boolean par2) {
      return this.settings.getBooleanProperty(par1Str, par2);
   }

   @Override
   public void setProperty(String par1Str, Object par2Obj) {
      this.settings.setProperty(par1Str, par2Obj);
   }

   @Override
   public void saveProperties() {
      this.settings.saveProperties();
   }

   @Override
   public String getSettingsFilename() {
      File var1 = this.settings.getPropertiesFile();
      return var1 != null ? var1.getAbsolutePath() : "No settings file";
   }

   public void ap() {
      ig.a(this);
      this.guiIsEnabled = true;
   }

   @Override
   public boolean getGuiEnabled() {
      return this.guiIsEnabled;
   }

   @Override
   public String shareToLAN(EnumGameType par1EnumGameType, boolean par2) {
      return "";
   }

   @Override
   public boolean isCommandBlockEnabled() {
      return this.settings.getBooleanProperty("enable-command-block", false);
   }

   @Override
   public int getSpawnProtectionSize() {
      return this.settings.getIntProperty("spawn-protection", super.getSpawnProtectionSize());
   }

   @Override
   public boolean func_96290_a(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
      if (par1World.provider.dimensionId != 0) {
         return false;
      } else if (this.getDedicatedPlayerList().i().isEmpty()) {
         return false;
      } else if (this.getDedicatedPlayerList().e(par5EntityPlayer.username)) {
         return false;
      } else if (this.getSpawnProtectionSize() <= 0) {
         return false;
      } else {
         ChunkCoordinates var6 = par1World.getSpawnPoint();
         int var7 = MathHelper.abs_int(par2 - var6.posX);
         int var8 = MathHelper.abs_int(par4 - var6.posZ);
         int var9 = Math.max(var7, var8);
         return var9 <= this.getSpawnProtectionSize();
      }
   }

   @Override
   public ILogAgent getLogAgent() {
      return this.field_98131_l;
   }

   @Override
   public ServerConfigurationManager getConfigurationManager() {
      return this.getDedicatedPlayerList();
   }
}
