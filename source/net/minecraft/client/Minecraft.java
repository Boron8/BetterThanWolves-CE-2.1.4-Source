package net.minecraft.client;

import btw.AddonHandler;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.hd.AAHelper;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CallableClientMemoryStats;
import net.minecraft.src.CallableClientProfiler;
import net.minecraft.src.CallableGLInfo;
import net.minecraft.src.CallableLWJGLVersion;
import net.minecraft.src.CallableModded;
import net.minecraft.src.CallableParticleScreenName;
import net.minecraft.src.CallableTexturePack;
import net.minecraft.src.CallableTickingScreenName;
import net.minecraft.src.CallableType2;
import net.minecraft.src.CallableUpdatingScreenName;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.CrashReport;
import net.minecraft.src.CrashReportCategory;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityItemFrame;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOS;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiMemoryErrorScreen;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.HttpUtil;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.IPlayerUsage;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IntegratedServer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.LogAgent;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MemoryConnection;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.ProfilerResult;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ReportedException;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.ServerData;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.StatList;
import net.minecraft.src.StatStringFormatKeyInv;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureManager;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.ThreadClientSleep;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.ThreadShutdown;
import net.minecraft.src.Timer;
import net.minecraft.src.WorldClient;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldRenderer;
import net.minecraft.src.WorldSettings;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

@Environment(EnvType.CLIENT)
public class Minecraft implements Runnable, IPlayerUsage {
   public static final boolean isRunningOnMac = getOs() == EnumOS.MACOS;
   public static byte[] memoryReserve = new byte[10485760];
   private final ILogAgent field_94139_O;
   private static final List macDisplayModes = new ArrayList<>(Arrays.asList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)));
   private ServerData currentServerData;
   private static Minecraft theMinecraft;
   public PlayerControllerMP playerController;
   private boolean fullscreen = false;
   private boolean hasCrashed = false;
   private CrashReport crashReporter;
   public int displayWidth;
   public int displayHeight;
   private Timer timer = new Timer(20.0F);
   private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this);
   public WorldClient theWorld;
   public RenderGlobal renderGlobal;
   public EntityClientPlayerMP thePlayer;
   public EntityLiving renderViewEntity;
   public EntityLiving pointedEntityLiving;
   public EffectRenderer effectRenderer;
   public Session session = null;
   public String minecraftUri;
   public boolean hideQuitButton = false;
   public volatile boolean isGamePaused = false;
   public RenderEngine renderEngine;
   public FontRenderer fontRenderer;
   public FontRenderer standardGalacticFontRenderer;
   public GuiScreen currentScreen = null;
   public LoadingScreenRenderer loadingScreen;
   public EntityRenderer entityRenderer;
   private ThreadDownloadResources downloadResourcesThread;
   private int leftClickCounter = 0;
   private int tempDisplayWidth;
   private int tempDisplayHeight;
   private IntegratedServer theIntegratedServer;
   public GuiAchievement guiAchievement;
   public GuiIngame ingameGUI;
   public boolean skipRenderWorld = false;
   public MovingObjectPosition objectMouseOver = null;
   public GameSettings gameSettings;
   public SoundManager sndManager = new SoundManager();
   public MouseHelper mouseHelper;
   public TexturePackList texturePackList;
   public File mcDataDir;
   private ISaveFormat saveLoader;
   private static int debugFPS;
   private int rightClickDelayTimer = 0;
   private boolean refreshTexturePacksScheduled;
   public StatFileWriter statFileWriter;
   private String serverName;
   private int serverPort;
   boolean isTakingScreenshot = false;
   public boolean inGameHasFocus = false;
   long systemTime = getSystemTime();
   private int joinPlayerCounter = 0;
   private boolean isDemo;
   private INetworkManager myNetworkManager;
   private boolean integratedServerIsRunning;
   public final Profiler mcProfiler = new Profiler();
   private long field_83002_am = -1L;
   private static File minecraftDir = null;
   public volatile boolean running = true;
   public String debug = "";
   long debugUpdateTime = getSystemTime();
   int fpsCounter = 0;
   long prevFrameTime = -1L;
   private String debugProfilerName = "root";

   public Minecraft(int par3, int par4, boolean par5, String gameDir) {
      MCPatcherUtils.setMinecraft((File)null, (File)null, "1.5.2", "5.0.3");
      this.mcDataDir = new File(gameDir);
      minecraftDir = new File(gameDir);
      this.field_94139_O = new LogAgent("Minecraft-Client", " [CLIENT]", new File(getMinecraftDir(), "output-client.log").getAbsolutePath());
      StatList.nopInit();
      AddonHandler.initializeLogger();
      this.fullscreen = par5;
      Packet3Chat.maxChatLength = 32767;
      this.startTimerHackThread();
      this.displayWidth = par3;
      this.displayHeight = par4;
      this.tempDisplayWidth = par3;
      this.tempDisplayHeight = par4;
      this.fullscreen = par5;
      theMinecraft = this;
      TextureManager.init();
      this.guiAchievement = new GuiAchievement(this);
      AddonHandler.initializeMods();
   }

   private void startTimerHackThread() {
      ThreadClientSleep var1 = new ThreadClientSleep(this, "Timer hack thread");
      var1.setDaemon(true);
      var1.start();
   }

   public void crashed(CrashReport par1CrashReport) {
      this.hasCrashed = true;
      this.crashReporter = par1CrashReport;
   }

   public void displayCrashReport(CrashReport par1CrashReport) {
      this.hasCrashed = true;
      File var2 = new File(getMinecraft().mcDataDir, "crash-reports");
      File var3 = new File(var2, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
      System.out.println(par1CrashReport.getCompleteReport());
      if (par1CrashReport.getFile() != null) {
         System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + par1CrashReport.getFile());
         System.exit(-1);
      } else if (par1CrashReport.saveToFile(var3, this.getLogAgent())) {
         System.out.println("#@!@# Game crashed! Crash report saved to: #@!@# " + var3.getAbsolutePath());
         System.exit(-1);
      } else {
         System.out.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
         System.exit(-2);
      }
   }

   public void setServer(String par1Str, int par2) {
      this.serverName = par1Str;
      this.serverPort = par2;
   }

   public void startGame() throws LWJGLException {
      this.mcDataDir = getMinecraftDir();
      this.gameSettings = new GameSettings(this, this.mcDataDir);
      if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
         this.displayWidth = this.gameSettings.overrideWidth;
         this.displayHeight = this.gameSettings.overrideHeight;
      }

      if (this.fullscreen) {
         Display.setFullscreen(true);
         this.displayWidth = Display.getDisplayMode().getWidth();
         this.displayHeight = Display.getDisplayMode().getHeight();
         if (this.displayWidth <= 0) {
            this.displayWidth = 1;
         }

         if (this.displayHeight <= 0) {
            this.displayHeight = 1;
         }
      } else {
         Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
      }

      Display.setResizable(true);
      Display.setTitle("Minecraft 1.5.2");
      this.getLogAgent().logInfo("LWJGL Version: " + Sys.getVersion());
      if (getOs() != EnumOS.MACOS) {
      }

      try {
         Display.create(AAHelper.setupPixelFormat(new PixelFormat().withDepthBits(24)));
      } catch (LWJGLException var5) {
         var5.printStackTrace();

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var4) {
         }

         if (this.fullscreen) {
            this.updateDisplayMode();
         }

         Display.create();
      }

      OpenGlHelper.initializeTextures();
      this.mcDataDir = getMinecraftDir();
      this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
      this.texturePackList = new TexturePackList(this.mcDataDir, this);
      this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
      this.loadScreen();
      this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine, false);
      this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, "/font/alternate.png", this.renderEngine, false);
      if (this.gameSettings.language != null && Minecraft.class.getResource("/lang/" + this.gameSettings.language + ".lang") != null) {
         StringTranslate.getInstance().setLanguage(this.gameSettings.language, false);
         this.fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
         this.fontRenderer.setBidiFlag(StringTranslate.isBidirectional(this.gameSettings.language));
      }

      ColorizerGrass.setGrassBiomeColorizer(this.renderEngine.getTextureContents("/misc/grasscolor.png"));
      ColorizerFoliage.setFoliageBiomeColorizer(this.renderEngine.getTextureContents("/misc/foliagecolor.png"));
      this.entityRenderer = new EntityRenderer(this);
      RenderManager.instance.itemRenderer = new ItemRenderer(this);
      this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
      AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
      this.loadScreen();
      Mouse.create();
      this.mouseHelper = new MouseHelper();
      this.checkGLError("Pre startup");
      GL11.glEnable(3553);
      GL11.glShadeModel(7425);
      GL11.glClearDepth(1.0);
      GL11.glEnable(2929);
      GL11.glDepthFunc(515);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glCullFace(1029);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glMatrixMode(5888);
      this.checkGLError("Startup");
      this.sndManager.loadSoundSettings(this.gameSettings);
      this.renderGlobal = new RenderGlobal(this, this.renderEngine);
      TexturePackChangeHandler.earlyInitialize("com.prupe.mcpatcher.mal.tile.TileLoader", "init");
      TexturePackChangeHandler.earlyInitialize("com.prupe.mcpatcher.ctm.CTMUtils", "reset");
      TexturePackChangeHandler.earlyInitialize("com.prupe.mcpatcher.cit.CITUtils", "init");
      TexturePackChangeHandler.earlyInitialize("com.prupe.mcpatcher.hd.FontUtils", "init");
      TexturePackChangeHandler.earlyInitialize("com.prupe.mcpatcher.mob.MobRandomizer", "init");
      TexturePackChangeHandler.earlyInitialize("com.prupe.mcpatcher.cc.Colorizer", "init");
      TexturePackChangeHandler.beforeChange1();
      this.renderEngine.refreshTextureMaps();
      TexturePackChangeHandler.afterChange1();
      GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
      this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

      try {
         this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
         this.downloadResourcesThread.start();
      } catch (Exception var3) {
      }

      this.checkGLError("Post startup");
      this.ingameGUI = new GuiIngame(this);
      if (this.serverName != null) {
         this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
      } else {
         this.displayGuiScreen(new GuiMainMenu());
      }

      this.loadingScreen = new LoadingScreenRenderer(this);
      if (this.gameSettings.fullScreen && !this.fullscreen) {
         this.toggleFullscreen();
      }
   }

   private ByteBuffer readImage(File par1File) throws IOException {
      BufferedImage var2 = ImageIO.read(par1File);
      int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), (int[])null, 0, var2.getWidth());
      ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);

      for (int var8 : var3) {
         var4.putInt(var8 << 8 | var8 >> 24 & 0xFF);
      }

      ((Buffer)var4).flip();
      return var4;
   }

   private void updateDisplayMode() throws LWJGLException {
      HashSet var1 = new HashSet();
      Collections.addAll(var1, Display.getAvailableDisplayModes());
      DisplayMode var2 = Display.getDesktopDisplayMode();
      if (!var1.contains(var2) && getOs() == EnumOS.MACOS) {
         for (DisplayMode var4 : macDisplayModes) {
            boolean var5 = true;

            for (DisplayMode var7 : var1) {
               if (var7.getBitsPerPixel() == 32 && var7.getWidth() == var4.getWidth() && var7.getHeight() == var4.getHeight()) {
                  var5 = false;
                  break;
               }
            }

            if (!var5) {
               for (DisplayMode var7x : var1) {
                  if (var7x.getBitsPerPixel() == 32 && var7x.getWidth() == var4.getWidth() / 2 && var7x.getHeight() == var4.getHeight() / 2) {
                     var2 = var7x;
                     break;
                  }
               }
            }
         }
      }

      Display.setDisplayMode(var2);
      this.displayWidth = var2.getWidth();
      this.displayHeight = var2.getHeight();
   }

   private void loadScreen() throws LWJGLException {
      ScaledResolution var1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
      GL11.glClear(16640);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0, 1000.0, 3000.0);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
      GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
      GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GL11.glDisable(2896);
      GL11.glEnable(3553);
      GL11.glDisable(2912);
      Tessellator var2 = Tessellator.instance;
      this.renderEngine.bindTexture("/title/mojang.png");
      var2.startDrawingQuads();
      var2.setColorOpaque_I(16777215);
      var2.addVertexWithUV(0.0, this.displayHeight, 0.0, 0.0, 0.0);
      var2.addVertexWithUV(this.displayWidth, this.displayHeight, 0.0, 0.0, 0.0);
      var2.addVertexWithUV(this.displayWidth, 0.0, 0.0, 0.0, 0.0);
      var2.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
      var2.draw();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      var2.setColorOpaque_I(16777215);
      short var3 = 256;
      short var4 = 256;
      this.scaledTessellator((var1.getScaledWidth() - var3) / 2, (var1.getScaledHeight() - var4) / 2, 0, 0, var3, var4);
      GL11.glDisable(2896);
      GL11.glDisable(2912);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.1F);
      Display.swapBuffers();
   }

   public void scaledTessellator(int par1, int par2, int par3, int par4, int par5, int par6) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(par1 + 0, par2 + par6, 0.0, (par3 + 0) * var7, (par4 + par6) * var8);
      var9.addVertexWithUV(par1 + par5, par2 + par6, 0.0, (par3 + par5) * var7, (par4 + par6) * var8);
      var9.addVertexWithUV(par1 + par5, par2 + 0, 0.0, (par3 + par5) * var7, (par4 + 0) * var8);
      var9.addVertexWithUV(par1 + 0, par2 + 0, 0.0, (par3 + 0) * var7, (par4 + 0) * var8);
      var9.draw();
   }

   public static File getMinecraftDir() {
      if (minecraftDir == null) {
         minecraftDir = getAppDir("minecraft");
      }

      return minecraftDir;
   }

   public static File getAppDir(String par0Str) {
      String var1 = System.getProperty("user.home", ".");
      File var2;
      switch (getOs()) {
         case LINUX:
         case SOLARIS:
            var2 = new File(var1, '.' + par0Str + '/');
            break;
         case WINDOWS:
            String var3 = System.getenv("APPDATA");
            if (var3 != null) {
               var2 = new File(var3, "." + par0Str + '/');
            } else {
               var2 = new File(var1, '.' + par0Str + '/');
            }
            break;
         case MACOS:
            var2 = new File(var1, "Library/Application Support/" + par0Str);
            break;
         default:
            var2 = new File(var1, par0Str + '/');
      }

      if (!var2.exists() && !var2.mkdirs()) {
         throw new RuntimeException("The working directory could not be created: " + var2);
      } else {
         return var2;
      }
   }

   public static EnumOS getOs() {
      String var0 = System.getProperty("os.name").toLowerCase();
      return var0.contains("win")
         ? EnumOS.WINDOWS
         : (
            var0.contains("mac")
               ? EnumOS.MACOS
               : (
                  var0.contains("solaris")
                     ? EnumOS.SOLARIS
                     : (
                        var0.contains("sunos")
                           ? EnumOS.SOLARIS
                           : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN))
                     )
               )
         );
   }

   public ISaveFormat getSaveLoader() {
      return this.saveLoader;
   }

   public void displayGuiScreen(GuiScreen par1GuiScreen) {
      if (this.currentScreen != null) {
         this.currentScreen.onGuiClosed();
      }

      this.statFileWriter.syncStats();
      if (par1GuiScreen == null && this.theWorld == null) {
         par1GuiScreen = new GuiMainMenu();
      } else if (par1GuiScreen == null && this.thePlayer.aX() <= 0) {
         par1GuiScreen = new GuiGameOver();
      }

      if (par1GuiScreen instanceof GuiMainMenu) {
         this.gameSettings.showDebugInfo = false;
         this.ingameGUI.getChatGUI().clearChatMessages();
      }

      this.currentScreen = par1GuiScreen;
      if (par1GuiScreen != null) {
         this.setIngameNotInFocus();
         ScaledResolution var2 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
         int var3 = var2.getScaledWidth();
         int var4 = var2.getScaledHeight();
         par1GuiScreen.setWorldAndResolution(this, var3, var4);
         this.skipRenderWorld = false;
      } else {
         this.setIngameFocus();
      }
   }

   private void checkGLError(String par1Str) {
      int var2 = GL11.glGetError();
      if (var2 != 0) {
         String var3 = GLU.gluErrorString(var2);
         this.getLogAgent().logSevere("########## GL ERROR ##########");
         this.getLogAgent().logSevere("@ " + par1Str);
         this.getLogAgent().logSevere(var2 + ": " + var3);
      }
   }

   public void shutdownMinecraftApplet() {
      try {
         this.statFileWriter.syncStats();

         try {
            if (this.downloadResourcesThread != null) {
               this.downloadResourcesThread.closeMinecraft();
            }
         } catch (Exception var9) {
         }

         this.getLogAgent().logInfo("Stopping!");

         try {
            this.loadWorld((WorldClient)null);
         } catch (Throwable var8) {
         }

         try {
            GLAllocation.deleteTexturesAndDisplayLists();
         } catch (Throwable var7) {
         }

         this.sndManager.closeMinecraft();
         Mouse.destroy();
         Keyboard.destroy();
      } finally {
         Display.destroy();
         if (!this.hasCrashed) {
            System.exit(0);
         }
      }

      System.gc();
   }

   @Override
   public void run() {
      this.running = true;

      try {
         this.startGame();
      } catch (Exception var11) {
         var11.printStackTrace();
         this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(new CrashReport("Failed to start game", var11)));
         return;
      }

      try {
         try {
            while (this.running) {
               if (this.hasCrashed && this.crashReporter != null) {
                  this.displayCrashReport(this.crashReporter);
                  return;
               } else {
                  if (this.refreshTexturePacksScheduled) {
                     this.refreshTexturePacksScheduled = false;
                     this.renderEngine.refreshTextures();
                  }

                  try {
                     this.runGameLoop();
                  } catch (OutOfMemoryError var10) {
                     this.freeMemory();
                     this.displayGuiScreen(new GuiMemoryErrorScreen());
                     System.gc();
                  }
               }
            }

            return;
         } catch (MinecraftError var12) {
         } catch (ReportedException var13) {
            this.addGraphicsAndWorldToCrashReport(var13.getCrashReport());
            this.freeMemory();
            var13.printStackTrace();
            this.displayCrashReport(var13.getCrashReport());
         } catch (Throwable var14) {
            CrashReport var2 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var14));
            this.freeMemory();
            var14.printStackTrace();
            this.displayCrashReport(var2);
         }
      } finally {
         this.shutdownMinecraftApplet();
      }
   }

   private void runGameLoop() {
      TexturePackChangeHandler.checkForTexturePackChange();
      AxisAlignedBB.getAABBPool().cleanPool();
      if (this.theWorld != null) {
         this.theWorld.U().clear();
      }

      this.mcProfiler.startSection("root");
      if (Display.isCloseRequested()) {
         this.shutdown();
      }

      if (this.isGamePaused && this.theWorld != null) {
         float var1 = this.timer.renderPartialTicks;
         this.timer.updateTimer();
         this.timer.renderPartialTicks = var1;
      } else {
         this.timer.updateTimer();
      }

      long var6 = System.nanoTime();
      this.mcProfiler.startSection("tick");

      for (int var3 = 0; var3 < this.timer.elapsedTicks; var3++) {
         this.runTick();
      }

      this.mcProfiler.endStartSection("preRenderErrors");
      long var7 = System.nanoTime() - var6;
      this.checkGLError("Pre render");
      RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
      this.mcProfiler.endStartSection("sound");
      this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
      if (!this.isGamePaused) {
         this.sndManager.func_92071_g();
      }

      this.mcProfiler.endSection();
      this.mcProfiler.startSection("render");
      this.mcProfiler.startSection("display");
      GL11.glEnable(3553);
      if (!Keyboard.isKeyDown(65)) {
         Display.update();
      }

      if (this.thePlayer != null && this.thePlayer.S()) {
         this.gameSettings.thirdPersonView = 0;
      }

      this.mcProfiler.endSection();
      if (!this.skipRenderWorld) {
         this.mcProfiler.endStartSection("gameRenderer");
         this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
         this.mcProfiler.endSection();
      }

      GL11.glFlush();
      this.mcProfiler.endSection();
      if (!Display.isActive() && this.fullscreen) {
         this.toggleFullscreen();
      }

      if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
         if (!this.mcProfiler.profilingEnabled) {
            this.mcProfiler.clearProfiling();
         }

         this.mcProfiler.profilingEnabled = true;
         this.displayDebugInfo(var7);
      } else {
         this.mcProfiler.profilingEnabled = false;
         this.prevFrameTime = System.nanoTime();
      }

      this.guiAchievement.updateAchievementWindow();
      this.mcProfiler.startSection("root");
      Thread.yield();
      if (Keyboard.isKeyDown(65)) {
         Display.update();
      }

      this.screenshotListener();
      if (!this.fullscreen && Display.wasResized()) {
         this.displayWidth = Display.getWidth();
         this.displayHeight = Display.getHeight();
         if (this.displayWidth <= 0) {
            this.displayWidth = 1;
         }

         if (this.displayHeight <= 0) {
            this.displayHeight = 1;
         }

         this.resize(this.displayWidth, this.displayHeight);
      }

      this.checkGLError("Post render");
      this.fpsCounter++;
      boolean var5 = this.isGamePaused;
      this.isGamePaused = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic();
      if (this.isIntegratedServerRunning() && this.thePlayer != null && this.thePlayer.sendQueue != null && this.isGamePaused != var5) {
         ((MemoryConnection)this.thePlayer.sendQueue.getNetManager()).setGamePaused(this.isGamePaused);
      }

      while (getSystemTime() >= this.debugUpdateTime + 1000L) {
         debugFPS = this.fpsCounter;
         this.debug = debugFPS + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
         WorldRenderer.chunksUpdated = 0;
         this.debugUpdateTime += 1000L;
         this.fpsCounter = 0;
         this.usageSnooper.addMemoryStatsToSnooper();
         if (!this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.startSnooper();
         }
      }

      this.mcProfiler.endSection();
      if (this.func_90020_K() > 0) {
         Display.sync(EntityRenderer.performanceToFps(this.func_90020_K()));
      }
   }

   private int func_90020_K() {
      return this.currentScreen != null && this.currentScreen instanceof GuiMainMenu ? 2 : this.gameSettings.limitFramerate;
   }

   public void freeMemory() {
      try {
         memoryReserve = new byte[0];
         this.renderGlobal.deleteAllDisplayLists();
      } catch (Throwable var4) {
      }

      try {
         System.gc();
         AxisAlignedBB.getAABBPool().clearPool();
         this.theWorld.U().clearAndFreeCache();
      } catch (Throwable var3) {
      }

      try {
         System.gc();
         this.loadWorld((WorldClient)null);
      } catch (Throwable var2) {
      }

      System.gc();
   }

   private void screenshotListener() {
      if (Keyboard.isKeyDown(60)) {
         if (!this.isTakingScreenshot) {
            this.isTakingScreenshot = true;
            this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
         }
      } else {
         this.isTakingScreenshot = false;
      }
   }

   private void updateDebugProfilerName(int par1) {
      List var2 = this.mcProfiler.getProfilingData(this.debugProfilerName);
      if (var2 != null && !var2.isEmpty()) {
         ProfilerResult var3 = (ProfilerResult)var2.remove(0);
         if (par1 == 0) {
            if (var3.field_76331_c.length() > 0) {
               int var4 = this.debugProfilerName.lastIndexOf(".");
               if (var4 >= 0) {
                  this.debugProfilerName = this.debugProfilerName.substring(0, var4);
               }
            }
         } else {
            par1--;
            if (par1 < var2.size() && !((ProfilerResult)var2.get(par1)).field_76331_c.equals("unspecified")) {
               if (this.debugProfilerName.length() > 0) {
                  this.debugProfilerName = this.debugProfilerName + ".";
               }

               this.debugProfilerName = this.debugProfilerName + ((ProfilerResult)var2.get(par1)).field_76331_c;
            }
         }
      }
   }

   private void displayDebugInfo(long par1) {
      if (this.mcProfiler.profilingEnabled) {
         List var3 = this.mcProfiler.getProfilingData(this.debugProfilerName);
         ProfilerResult var4 = (ProfilerResult)var3.remove(0);
         GL11.glClear(256);
         GL11.glMatrixMode(5889);
         GL11.glEnable(2903);
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
         GL11.glLineWidth(1.0F);
         GL11.glDisable(3553);
         Tessellator var5 = Tessellator.instance;
         short var6 = 160;
         int var7 = this.displayWidth - var6 - 10;
         int var8 = this.displayHeight - var6 * 2;
         GL11.glEnable(3042);
         var5.startDrawingQuads();
         var5.setColorRGBA_I(0, 200);
         var5.addVertex(var7 - var6 * 1.1F, var8 - var6 * 0.6F - 16.0F, 0.0);
         var5.addVertex(var7 - var6 * 1.1F, var8 + var6 * 2, 0.0);
         var5.addVertex(var7 + var6 * 1.1F, var8 + var6 * 2, 0.0);
         var5.addVertex(var7 + var6 * 1.1F, var8 - var6 * 0.6F - 16.0F, 0.0);
         var5.draw();
         GL11.glDisable(3042);
         double var9 = 0.0;

         for (int var11 = 0; var11 < var3.size(); var11++) {
            ProfilerResult var12 = (ProfilerResult)var3.get(var11);
            int var13 = MathHelper.floor_double(var12.field_76332_a / 4.0) + 1;
            var5.startDrawing(6);
            var5.setColorOpaque_I(var12.func_76329_a());
            var5.addVertex(var7, var8, 0.0);

            for (int var14 = var13; var14 >= 0; var14--) {
               float var15 = (float)((var9 + var12.field_76332_a * var14 / var13) * Math.PI * 2.0 / 100.0);
               float var16 = MathHelper.sin(var15) * var6;
               float var17 = MathHelper.cos(var15) * var6 * 0.5F;
               var5.addVertex(var7 + var16, var8 - var17, 0.0);
            }

            var5.draw();
            var5.startDrawing(5);
            var5.setColorOpaque_I((var12.func_76329_a() & 16711422) >> 1);

            for (int var23 = var13; var23 >= 0; var23--) {
               float var15 = (float)((var9 + var12.field_76332_a * var23 / var13) * Math.PI * 2.0 / 100.0);
               float var16 = MathHelper.sin(var15) * var6;
               float var17 = MathHelper.cos(var15) * var6 * 0.5F;
               var5.addVertex(var7 + var16, var8 - var17, 0.0);
               var5.addVertex(var7 + var16, var8 - var17 + 10.0F, 0.0);
            }

            var5.draw();
            var9 += var12.field_76332_a;
         }

         DecimalFormat var19 = new DecimalFormat("##0.00");
         GL11.glEnable(3553);
         String var18 = "";
         if (!var4.field_76331_c.equals("unspecified")) {
            var18 = var18 + "[0] ";
         }

         if (var4.field_76331_c.length() == 0) {
            var18 = var18 + "ROOT ";
         } else {
            var18 = var18 + var4.field_76331_c + " ";
         }

         int var13 = 16777215;
         this.fontRenderer.drawStringWithShadow(var18, var7 - var6, var8 - var6 / 2 - 16, var13);
         this.fontRenderer
            .drawStringWithShadow(
               var18 = var19.format(var4.field_76330_b) + "%", var7 + var6 - this.fontRenderer.getStringWidth(var18), var8 - var6 / 2 - 16, var13
            );

         for (int var21 = 0; var21 < var3.size(); var21++) {
            ProfilerResult var20 = (ProfilerResult)var3.get(var21);
            String var22 = "";
            if (var20.field_76331_c.equals("unspecified")) {
               var22 = var22 + "[?] ";
            } else {
               var22 = var22 + "[" + (var21 + 1) + "] ";
            }

            var22 = var22 + var20.field_76331_c;
            this.fontRenderer.drawStringWithShadow(var22, var7 - var6, var8 + var6 / 2 + var21 * 8 + 20, var20.func_76329_a());
            this.fontRenderer
               .drawStringWithShadow(
                  var22 = var19.format(var20.field_76332_a) + "%",
                  var7 + var6 - 50 - this.fontRenderer.getStringWidth(var22),
                  var8 + var6 / 2 + var21 * 8 + 20,
                  var20.func_76329_a()
               );
            this.fontRenderer
               .drawStringWithShadow(
                  var22 = var19.format(var20.field_76330_b) + "%",
                  var7 + var6 - this.fontRenderer.getStringWidth(var22),
                  var8 + var6 / 2 + var21 * 8 + 20,
                  var20.func_76329_a()
               );
         }
      }
   }

   public void shutdown() {
      this.running = false;
   }

   public void setIngameFocus() {
      if (Display.isActive() && !this.inGameHasFocus) {
         this.inGameHasFocus = true;
         this.mouseHelper.grabMouseCursor();
         this.displayGuiScreen((GuiScreen)null);
         this.leftClickCounter = 10000;
      }
   }

   public void setIngameNotInFocus() {
      if (this.inGameHasFocus) {
         KeyBinding.unPressAllKeys();
         this.inGameHasFocus = false;
         this.mouseHelper.ungrabMouseCursor();
      }
   }

   public void displayInGameMenu() {
      if (this.currentScreen == null) {
         this.displayGuiScreen(new GuiIngameMenu());
         if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
            this.sndManager.pauseAllSounds();
         }
      }
   }

   private void sendClickBlockToController(int par1, boolean par2) {
      if (!par2) {
         this.leftClickCounter = 0;
      }

      if (par1 != 0 || this.leftClickCounter <= 0) {
         if (par2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && par1 == 0 && !this.thePlayer.bX()) {
            int var3 = this.objectMouseOver.blockX;
            int var4 = this.objectMouseOver.blockY;
            int var5 = this.objectMouseOver.blockZ;
            this.playerController.onPlayerDamageBlock(var3, var4, var5, this.objectMouseOver.sideHit);
            if (this.thePlayer.e(var3, var4, var5)) {
               this.effectRenderer.addBlockHitEffects(var3, var4, var5, this.objectMouseOver.sideHit);
               this.thePlayer.swingItem();
            }
         } else {
            this.playerController.resetBlockRemoving();
         }
      }
   }

   private void clickMouse(int par1) {
      if (par1 != 0 || this.leftClickCounter <= 0) {
         if (par1 == 0) {
            this.thePlayer.swingItem();
         }

         if (par1 == 1) {
            this.rightClickDelayTimer = 4;
         }

         boolean var2 = true;
         ItemStack var3 = this.thePlayer.inventory.getCurrentItem();
         if (this.objectMouseOver == null) {
            if (par1 == 0 && this.playerController.isNotCreative()) {
               this.leftClickCounter = 10;
            }
         } else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
            if (par1 == 0) {
               this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
            }

            if (par1 == 1 && this.playerController.func_78768_b(this.thePlayer, this.objectMouseOver.entityHit)) {
               var2 = false;
            }
         } else if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
            int var4 = this.objectMouseOver.blockX;
            int var5 = this.objectMouseOver.blockY;
            int var6 = this.objectMouseOver.blockZ;
            int var7 = this.objectMouseOver.sideHit;
            if (par1 == 0) {
               this.playerController.clickBlock(var4, var5, var6, this.objectMouseOver.sideHit);
            } else {
               int var8 = var3 != null ? var3.stackSize : 0;
               if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, var3, var4, var5, var6, var7, this.objectMouseOver.hitVec)) {
                  var2 = false;
                  this.thePlayer.swingItem();
               }

               if (var3 == null) {
                  return;
               }

               if (var3.stackSize == 0) {
                  this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
               } else if (var3.stackSize != var8 || this.playerController.isInCreativeMode()) {
                  this.entityRenderer.itemRenderer.resetEquippedProgress();
               }
            }
         }

         if (var2 && par1 == 1) {
            ItemStack var9 = this.thePlayer.inventory.getCurrentItem();
            if (var9 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, var9)) {
               this.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
         }
      }
   }

   public void toggleFullscreen() {
      try {
         this.fullscreen = !this.fullscreen;
         if (this.fullscreen) {
            this.updateDisplayMode();
            this.displayWidth = Display.getDisplayMode().getWidth();
            this.displayHeight = Display.getDisplayMode().getHeight();
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         } else {
            Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
            this.displayWidth = this.tempDisplayWidth;
            this.displayHeight = this.tempDisplayHeight;
            if (this.displayWidth <= 0) {
               this.displayWidth = 1;
            }

            if (this.displayHeight <= 0) {
               this.displayHeight = 1;
            }
         }

         if (this.currentScreen != null) {
            this.resize(this.displayWidth, this.displayHeight);
         }

         Display.setFullscreen(this.fullscreen);
         Display.setVSyncEnabled(this.gameSettings.enableVsync);
         Display.update();
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private void resize(int par1, int par2) {
      this.displayWidth = par1 <= 0 ? 1 : par1;
      this.displayHeight = par2 <= 0 ? 1 : par2;
      if (this.currentScreen != null) {
         ScaledResolution var3 = new ScaledResolution(this.gameSettings, par1, par2);
         int var4 = var3.getScaledWidth();
         int var5 = var3.getScaledHeight();
         this.currentScreen.setWorldAndResolution(this, var4, var5);
      }
   }

   public void runTick() {
      if (this.rightClickDelayTimer > 0) {
         this.rightClickDelayTimer--;
      }

      this.mcProfiler.startSection("stats");
      this.statFileWriter.func_77449_e();
      this.mcProfiler.endStartSection("gui");
      if (!this.isGamePaused) {
         this.ingameGUI.updateTick();
      }

      this.mcProfiler.endStartSection("pick");
      this.entityRenderer.getMouseOver(1.0F);
      this.mcProfiler.endStartSection("gameMode");
      if (!this.isGamePaused && this.theWorld != null) {
         this.playerController.updateController();
      }

      this.renderEngine.bindTexture("/terrain.png");
      this.mcProfiler.endStartSection("textures");
      if (!this.isGamePaused) {
         this.renderEngine.updateDynamicTextures();
      }

      if (this.currentScreen == null && this.thePlayer != null) {
         if (this.thePlayer.aX() <= 0) {
            this.displayGuiScreen((GuiScreen)null);
         } else if (this.thePlayer.bz() && this.theWorld != null) {
            this.displayGuiScreen(new GuiSleepMP());
         }
      } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.bz()) {
         this.displayGuiScreen((GuiScreen)null);
      }

      if (this.currentScreen != null) {
         this.leftClickCounter = 10000;
      }

      if (this.currentScreen != null) {
         try {
            this.currentScreen.handleInput();
         } catch (Throwable var9) {
            CrashReport var2 = CrashReport.makeCrashReport(var9, "Updating screen events");
            CrashReportCategory var3 = var2.makeCategory("Affected screen");
            var3.addCrashSectionCallable("Screen name", new CallableUpdatingScreenName(this));
            throw new ReportedException(var2);
         }

         if (this.currentScreen != null) {
            try {
               this.currentScreen.guiParticles.update();
            } catch (Throwable var8) {
               CrashReport var2 = CrashReport.makeCrashReport(var8, "Ticking screen particles");
               CrashReportCategory var3 = var2.makeCategory("Affected screen");
               var3.addCrashSectionCallable("Screen name", new CallableParticleScreenName(this));
               throw new ReportedException(var2);
            }

            try {
               this.currentScreen.updateScreen();
            } catch (Throwable var71) {
               CrashReport var2 = CrashReport.makeCrashReport(var71, "Ticking screen");
               CrashReportCategory var3 = var2.makeCategory("Affected screen");
               var3.addCrashSectionCallable("Screen name", new CallableTickingScreenName(this));
               throw new ReportedException(var2);
            }
         }
      }

      if (this.currentScreen == null || this.currentScreen.allowUserInput) {
         this.mcProfiler.endStartSection("mouse");

         while (Mouse.next()) {
            int var1_1 = Mouse.getEventButton();
            if (isRunningOnMac && var1_1 == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157))) {
               var1_1 = 1;
            }

            KeyBinding.setKeyBindState(var1_1 - 100, Mouse.getEventButtonState());
            if (Mouse.getEventButtonState()) {
               KeyBinding.onTick(var1_1 - 100);
            }

            long var1 = getSystemTime() - this.systemTime;
            if (var1 <= 200L) {
               int var10 = Mouse.getEventDWheel();
               if (var10 != 0) {
                  this.thePlayer.inventory.changeCurrentItem(var10);
                  if (this.gameSettings.noclip) {
                     if (var10 > 0) {
                        var10 = 1;
                     }

                     if (var10 < 0) {
                        var10 = -1;
                     }

                     this.gameSettings.noclipRate += var10 * 0.25F;
                  }
               }

               if (this.currentScreen == null) {
                  if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                     this.setIngameFocus();
                  }
               } else if (this.currentScreen != null) {
                  this.currentScreen.handleMouseInput();
               }
            }
         }

         if (this.leftClickCounter > 0) {
            this.leftClickCounter--;
         }

         this.mcProfiler.endStartSection("keyboard");

         while (Keyboard.next()) {
            KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
            if (Keyboard.getEventKeyState()) {
               KeyBinding.onTick(Keyboard.getEventKey());
            }

            if (this.field_83002_am > 0L) {
               if (getSystemTime() - this.field_83002_am >= 6000L) {
                  throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
               }

               if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                  this.field_83002_am = -1L;
               }
            } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
               this.field_83002_am = getSystemTime();
            }

            if (Keyboard.getEventKeyState()) {
               if (Keyboard.getEventKey() == 87) {
                  this.toggleFullscreen();
               } else {
                  if (this.currentScreen != null) {
                     this.currentScreen.handleKeyboardInput();
                  } else {
                     if (Keyboard.getEventKey() == 1) {
                        this.displayInGameMenu();
                     }

                     if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                        this.forceReload();
                     }

                     if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61)) {
                        this.renderEngine.refreshTextures();
                        this.renderGlobal.loadRenderers();
                     }

                     if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
                        boolean var8 = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                        this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, var8 ? -1 : 1);
                     }

                     if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61)) {
                        this.renderGlobal.loadRenderers();
                     }

                     if (Keyboard.getEventKey() == 35 && Keyboard.isKeyDown(61)) {
                        this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
                        this.gameSettings.saveOptions();
                     }

                     if (Keyboard.getEventKey() == 48 && Keyboard.isKeyDown(61)) {
                        RenderManager.field_85095_o = !RenderManager.field_85095_o;
                     }

                     if (Keyboard.getEventKey() == 25 && Keyboard.isKeyDown(61)) {
                        this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
                        this.gameSettings.saveOptions();
                     }

                     if (Keyboard.getEventKey() == 59) {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                     }

                     if (Keyboard.getEventKey() == 61) {
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                        this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                     }

                     if (Keyboard.getEventKey() == 63) {
                        this.gameSettings.thirdPersonView++;
                        if (this.gameSettings.thirdPersonView > 2) {
                           this.gameSettings.thirdPersonView = 0;
                        }
                     }

                     if (Keyboard.getEventKey() == 66) {
                        this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
                     }
                  }

                  for (int var9 = 0; var9 < 9; var9++) {
                     if (Keyboard.getEventKey() == 2 + var9) {
                        this.thePlayer.inventory.currentItem = var9;
                     }
                  }

                  if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
                     if (Keyboard.getEventKey() == 11) {
                        this.updateDebugProfilerName(0);
                     }

                     for (int var20 = 0; var20 < 9; var20++) {
                        if (Keyboard.getEventKey() == 2 + var20) {
                           this.updateDebugProfilerName(var20 + 1);
                        }
                     }
                  }
               }
            }
         }

         boolean var8 = this.gameSettings.chatVisibility != 2;

         while (this.gameSettings.keyBindInventory.isPressed()) {
            this.displayGuiScreen(new GuiInventory(this.thePlayer));
         }

         while (this.gameSettings.keyBindDrop.isPressed()) {
            this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
         }

         while (this.gameSettings.keyBindChat.isPressed() && var8) {
            this.displayGuiScreen(new GuiChat());
         }

         if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && var8) {
            this.displayGuiScreen(new GuiChat("/"));
         }

         if (this.thePlayer.bX()) {
            if (!this.gameSettings.keyBindUseItem.pressed) {
               this.playerController.onStoppedUsingItem(this.thePlayer);
            }

            while (this.gameSettings.keyBindAttack.isPressed()) {
            }

            while (this.gameSettings.keyBindUseItem.isPressed()) {
            }

            while (this.gameSettings.keyBindPickBlock.isPressed()) {
            }
         } else {
            while (this.gameSettings.keyBindAttack.isPressed()) {
               this.clickMouse(0);
            }

            while (this.gameSettings.keyBindUseItem.isPressed()) {
               this.clickMouse(1);
            }

            while (this.gameSettings.keyBindPickBlock.isPressed()) {
               this.clickMiddleMouseButton();
            }
         }

         if (this.gameSettings.keyBindUseItem.pressed && this.rightClickDelayTimer == 0 && !this.thePlayer.bX()) {
            ItemStack currentStack = this.thePlayer.inventory.getCurrentItem();
            if (currentStack != null && currentStack.getItem().isMultiUsePerClick()) {
               this.clickMouse(1);
            }
         }

         this.sendClickBlockToController(0, this.currentScreen == null && this.gameSettings.keyBindAttack.pressed && this.inGameHasFocus);
      }

      if (this.theWorld != null) {
         if (this.thePlayer != null) {
            this.joinPlayerCounter++;
            if (this.joinPlayerCounter == 30) {
               this.joinPlayerCounter = 0;
               this.theWorld.h(this.thePlayer);
            }
         }

         this.mcProfiler.endStartSection("gameRenderer");
         if (!this.isGamePaused) {
            this.entityRenderer.updateRenderer();
         }

         this.mcProfiler.endStartSection("levelRenderer");
         if (!this.isGamePaused) {
            this.renderGlobal.updateClouds();
         }

         this.mcProfiler.endStartSection("level");
         if (!this.isGamePaused) {
            if (this.theWorld.lastLightningBolt > 0) {
               this.theWorld.lastLightningBolt--;
            }

            this.theWorld.h();
         }

         if (!this.isGamePaused) {
            this.theWorld.a(this.theWorld.difficultySetting > 0, true);

            try {
               this.theWorld.tick();
            } catch (Throwable var10x) {
               CrashReport var2 = CrashReport.makeCrashReport(var10x, "Exception in world tick");
               if (this.theWorld == null) {
                  CrashReportCategory var3 = var2.makeCategory("Affected level");
                  var3.addCrashSection("Problem", "Level is null!");
               } else {
                  this.theWorld.addWorldInfoToCrashReport(var2);
               }

               throw new ReportedException(var2);
            }
         }

         this.mcProfiler.endStartSection("animateTick");
         if (!this.isGamePaused && this.theWorld != null) {
            this.theWorld
               .doVoidFogParticles(
                  MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ)
               );
         }

         this.mcProfiler.endStartSection("particles");
         if (!this.isGamePaused) {
            this.effectRenderer.updateEffects();
         }
      } else if (this.myNetworkManager != null) {
         this.mcProfiler.endStartSection("pendingConnection");
         this.myNetworkManager.processReadPackets();
      }

      this.mcProfiler.endSection();
      this.systemTime = getSystemTime();
   }

   private void forceReload() {
      this.getLogAgent().logInfo("FORCING RELOAD!");
      if (this.sndManager != null) {
         this.sndManager.stopAllSounds();
      }

      this.sndManager = new SoundManager();
      this.sndManager.loadSoundSettings(this.gameSettings);
      this.downloadResourcesThread.reloadResources();
   }

   public void launchIntegratedServer(String par1Str, String par2Str, WorldSettings par3WorldSettings) {
      this.loadWorld((WorldClient)null);
      System.gc();
      ISaveHandler var4 = this.saveLoader.getSaveLoader(par1Str, false);
      WorldInfo var5 = var4.loadWorldInfo();
      if (var5 == null && par3WorldSettings != null) {
         this.statFileWriter.readStat(StatList.createWorldStat, 1);
         var5 = new WorldInfo(par3WorldSettings, par1Str, true);
         var4.saveWorldInfo(var5);
      }

      if (par3WorldSettings == null) {
         par3WorldSettings = new WorldSettings(var5);
      }

      this.statFileWriter.readStat(StatList.startGameStat, 1);
      this.theIntegratedServer = new IntegratedServer(this, par1Str, par2Str, par3WorldSettings);
      this.theIntegratedServer.t();
      this.integratedServerIsRunning = true;
      this.loadingScreen.displayProgressMessage(StatCollector.translateToLocal("menu.loadingLevel"));

      while (!this.theIntegratedServer.af()) {
         String var6 = this.theIntegratedServer.d();
         if (var6 != null) {
            this.loadingScreen.resetProgresAndWorkingMessage(StatCollector.translateToLocal(var6));
         } else {
            this.loadingScreen.resetProgresAndWorkingMessage("");
         }

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var9) {
         }
      }

      this.displayGuiScreen((GuiScreen)null);

      try {
         NetClientHandler var10 = new NetClientHandler(this, this.theIntegratedServer);
         this.myNetworkManager = var10.getNetManager();
      } catch (IOException var81) {
         this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(new CrashReport("Connecting to integrated server", var81)));
      }
   }

   public void loadWorld(WorldClient par1WorldClient) {
      this.loadWorld(par1WorldClient, "");
   }

   public void loadWorld(WorldClient par1WorldClient, String par2Str) {
      this.statFileWriter.syncStats();
      if (par1WorldClient == null) {
         NetClientHandler var3 = this.getNetHandler();
         if (var3 != null) {
            var3.cleanup();
         }

         if (this.myNetworkManager != null) {
            this.myNetworkManager.closeConnections();
         }

         if (this.theIntegratedServer != null) {
            this.theIntegratedServer.initiateShutdown();
         }

         this.theIntegratedServer = null;
      }

      this.renderViewEntity = null;
      this.myNetworkManager = null;
      if (this.loadingScreen != null) {
         this.loadingScreen.resetProgressAndMessage(par2Str);
         this.loadingScreen.resetProgresAndWorkingMessage("");
      }

      if (par1WorldClient == null && this.theWorld != null) {
         if (this.texturePackList.getIsDownloading()) {
            this.texturePackList.onDownloadFinished();
         }

         this.setServerData((ServerData)null);
         this.integratedServerIsRunning = false;
      }

      this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F);
      this.sndManager.stopAllSounds();
      this.theWorld = par1WorldClient;
      if (par1WorldClient != null) {
         if (this.renderGlobal != null) {
            this.renderGlobal.setWorldAndLoadRenderers(par1WorldClient);
         }

         if (this.effectRenderer != null) {
            this.effectRenderer.clearEffects(par1WorldClient);
         }

         if (this.thePlayer == null) {
            this.thePlayer = this.playerController.func_78754_a(par1WorldClient);
            this.playerController.flipPlayer(this.thePlayer);
         }

         this.thePlayer.v();
         par1WorldClient.spawnEntityInWorld(this.thePlayer);
         this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
         this.playerController.setPlayerCapabilities(this.thePlayer);
         this.renderViewEntity = this.thePlayer;
      } else {
         this.saveLoader.flushCache();
         this.thePlayer = null;
      }

      System.gc();
      this.systemTime = 0L;
   }

   public void installResource(String par1Str, File par2File) {
      int var3 = par1Str.indexOf("/");
      String var4 = par1Str.substring(0, var3);
      par1Str = par1Str.substring(var3 + 1);
      if (var4.equalsIgnoreCase("sound3")) {
         this.sndManager.addSound(par1Str, par2File);
      } else if (var4.equalsIgnoreCase("streaming")) {
         this.sndManager.addStreaming(par1Str, par2File);
      } else if (var4.equalsIgnoreCase("music") || var4.equalsIgnoreCase("newmusic")) {
         this.sndManager.addMusic(par1Str, par2File);
      } else if (var4.equalsIgnoreCase("lang")) {
         StringTranslate.getInstance().func_94519_a(par1Str, par2File);
      }
   }

   public String debugInfoRenders() {
      return this.renderGlobal.getDebugInfoRenders();
   }

   public String getEntityDebug() {
      return this.renderGlobal.getDebugInfoEntities();
   }

   public String getWorldProviderName() {
      return this.theWorld.y();
   }

   public String debugInfoEntities() {
      return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.x();
   }

   public void setDimensionAndSpawnPlayer(int par1) {
      this.theWorld.f();
      this.theWorld.removeAllEntities();
      int var2 = 0;
      if (this.thePlayer != null) {
         var2 = this.thePlayer.entityId;
         this.theWorld.removeEntity(this.thePlayer);
      }

      this.renderViewEntity = null;
      this.thePlayer = this.playerController.func_78754_a(this.theWorld);
      this.thePlayer.dimension = par1;
      this.renderViewEntity = this.thePlayer;
      this.thePlayer.v();
      this.theWorld.spawnEntityInWorld(this.thePlayer);
      this.playerController.flipPlayer(this.thePlayer);
      this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
      this.thePlayer.entityId = var2;
      this.playerController.setPlayerCapabilities(this.thePlayer);
      if (this.currentScreen instanceof GuiGameOver) {
         this.displayGuiScreen((GuiScreen)null);
      }
   }

   public void setDemo(boolean par1) {
      this.isDemo = par1;
   }

   public final boolean isDemo() {
      return this.isDemo;
   }

   public NetClientHandler getNetHandler() {
      return this.thePlayer != null ? this.thePlayer.sendQueue : null;
   }

   public static void main(String[] par0ArrayOfStr) {
      HashMap var1 = new HashMap();
      boolean var2 = false;
      boolean var3 = true;
      boolean var4 = false;
      String var5 = "Player" + getSystemTime() % 1000L;
      String var6 = var5;
      String gameDir = ".";
      String var7 = "-";
      if (par0ArrayOfStr.length > 0) {
         if (par0ArrayOfStr.length > 1 && par0ArrayOfStr[0].equals("--username")) {
            var6 = par0ArrayOfStr[1];
            if (par0ArrayOfStr.length > 3 && par0ArrayOfStr[2].equals("--session")) {
               var7 = par0ArrayOfStr[3];
            } else if (par0ArrayOfStr.length > 2) {
               var7 = par0ArrayOfStr[2];
            }
         } else {
            var6 = par0ArrayOfStr[0];
            if (par0ArrayOfStr.length > 1) {
               var7 = par0ArrayOfStr[1];
            }
         }
      }

      ArrayList var8 = new ArrayList();

      for (int var9 = 2; var9 < par0ArrayOfStr.length; var9++) {
         String var10 = par0ArrayOfStr[var9];
         String var11 = var9 == par0ArrayOfStr.length - 1 ? null : par0ArrayOfStr[var9 + 1];
         boolean var12 = false;
         if (var10.equals("-demo") || var10.equals("--demo")) {
            var2 = true;
         } else if (var10.equals("--applet")) {
            var3 = false;
         } else if (var10.equals("--password") && var11 != null) {
            String[] var13 = HttpUtil.loginToMinecraft((ILogAgent)null, var6, var11);
            if (var13 != null) {
               var6 = var13[0];
               var7 = var13[1];
               var8.add("Logged in insecurely as " + var6);
            } else {
               var8.add("Could not log in as " + var6 + " with given password");
            }

            var12 = true;
         } else if (var10.equals("--gameDir") && var11 != null) {
            gameDir = var11;
            var12 = true;
         }

         if (var12) {
            var9++;
         }
      }

      if (var6.contains("@") && var7.length() <= 1 || var6.equals("--assetIndex")) {
         var6 = var5;
      }

      var1.put("demo", "" + var2);
      var1.put("stand-alone", "" + var3);
      var1.put("username", var6);
      var1.put("fullscreen", "" + var4);
      var1.put("sessionid", var7);
      var1.put("gameDir", gameDir);
      Main var18 = new Main();
      var18.init(var1);

      for (String var14 : var8) {
         getMinecraft().getLogAgent().logInfo(var14);
      }

      var18.start();
      Runtime.getRuntime().addShutdownHook(new ThreadShutdown());
   }

   public static boolean isGuiEnabled() {
      return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
   }

   public static boolean isFancyGraphicsEnabled() {
      return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
   }

   public static boolean isAmbientOcclusionEnabled() {
      return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
   }

   public boolean handleClientCommand(String par1Str) {
      return !par1Str.startsWith("/") ? false : false;
   }

   private void clickMiddleMouseButton() {
      if (this.objectMouseOver != null) {
         boolean var1 = this.thePlayer.capabilities.isCreativeMode;
         int var3 = 0;
         boolean var4 = false;
         int var2;
         if (this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
            int var5 = this.objectMouseOver.blockX;
            int var6 = this.objectMouseOver.blockY;
            int var7 = this.objectMouseOver.blockZ;
            Block var8 = Block.blocksList[this.theWorld.a(var5, var6, var7)];
            if (var8 == null) {
               return;
            }

            var2 = var8.idPicked(this.theWorld, var5, var6, var7);
            if (var2 == 0) {
               return;
            }

            var4 = Item.itemsList[var2].getHasSubtypes();
            int var9 = var2 < 256 && !Block.blocksList[var8.blockID].isFlowerPot() ? var2 : var8.blockID;
            var3 = Block.blocksList[var9].getDamageValue(this.theWorld, var5, var6, var7);
         } else {
            if (this.objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !var1) {
               return;
            }

            if (this.objectMouseOver.entityHit instanceof EntityPainting) {
               var2 = Item.painting.itemID;
            } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
               EntityItemFrame var10 = (EntityItemFrame)this.objectMouseOver.entityHit;
               if (var10.getDisplayedItem() == null) {
                  var2 = Item.itemFrame.itemID;
               } else {
                  var2 = var10.getDisplayedItem().itemID;
                  var3 = var10.getDisplayedItem().getItemDamage();
                  var4 = true;
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
               EntityMinecart var11 = (EntityMinecart)this.objectMouseOver.entityHit;
               if (var11.getMinecartType() == 2) {
                  var2 = Item.minecartPowered.itemID;
               } else if (var11.getMinecartType() == 1) {
                  var2 = Item.minecartCrate.itemID;
               } else if (var11.getMinecartType() == 3) {
                  var2 = Item.minecartTnt.itemID;
               } else if (var11.getMinecartType() == 5) {
                  var2 = Item.minecartHopper.itemID;
               } else {
                  var2 = Item.minecartEmpty.itemID;
               }
            } else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
               var2 = Item.boat.itemID;
            } else {
               var2 = Item.monsterPlacer.itemID;
               var3 = EntityList.getEntityID(this.objectMouseOver.entityHit);
               var4 = true;
               if (var3 <= 0 || !EntityList.entityEggs.containsKey(var3)) {
                  return;
               }
            }
         }

         this.thePlayer.inventory.setCurrentItem(var2, var3, var4, var1);
         if (var1) {
            int var5x = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + this.thePlayer.inventory.currentItem;
            this.playerController.sendSlotPacket(this.thePlayer.inventory.getStackInSlot(this.thePlayer.inventory.currentItem), var5x);
         }
      }
   }

   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport par1CrashReport) {
      par1CrashReport.func_85056_g().addCrashSectionCallable("LWJGL", new CallableLWJGLVersion(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("OpenGL", new CallableGLInfo(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Is Modded", new CallableModded(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Type", new CallableType2(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Texture Pack", new CallableTexturePack(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Profiler Position", new CallableClientProfiler(this));
      par1CrashReport.func_85056_g().addCrashSectionCallable("Vec3 Pool Size", new CallableClientMemoryStats(this));
      if (this.theWorld != null) {
         this.theWorld.addWorldInfoToCrashReport(par1CrashReport);
      }

      return par1CrashReport;
   }

   public static Minecraft getMinecraft() {
      return theMinecraft;
   }

   public void scheduleTexturePackRefresh() {
      this.refreshTexturePacksScheduled = true;
   }

   @Override
   public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper) {
      par1PlayerUsageSnooper.addData("fps", debugFPS);
      par1PlayerUsageSnooper.addData("texpack_name", this.texturePackList.getSelectedTexturePack().getTexturePackFileName());
      par1PlayerUsageSnooper.addData("vsync_enabled", this.gameSettings.enableVsync);
      par1PlayerUsageSnooper.addData("display_frequency", Display.getDisplayMode().getFrequency());
      par1PlayerUsageSnooper.addData("display_type", this.fullscreen ? "fullscreen" : "windowed");
      if (this.theIntegratedServer != null && this.theIntegratedServer.aj() != null) {
         par1PlayerUsageSnooper.addData("snooper_partner", this.theIntegratedServer.aj().getUniqueID());
      }
   }

   @Override
   public void addServerTypeToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper) {
      par1PlayerUsageSnooper.addData("opengl_version", GL11.glGetString(7938));
      par1PlayerUsageSnooper.addData("opengl_vendor", GL11.glGetString(7936));
      par1PlayerUsageSnooper.addData("client_brand", ClientBrandRetriever.getClientModName());
      par1PlayerUsageSnooper.addData("applet", this.hideQuitButton);
      ContextCapabilities var2 = GLContext.getCapabilities();
      par1PlayerUsageSnooper.addData("gl_caps[ARB_multitexture]", var2.GL_ARB_multitexture);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_multisample]", var2.GL_ARB_multisample);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_texture_cube_map]", var2.GL_ARB_texture_cube_map);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_blend]", var2.GL_ARB_vertex_blend);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_matrix_palette]", var2.GL_ARB_matrix_palette);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_program]", var2.GL_ARB_vertex_program);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_shader]", var2.GL_ARB_vertex_shader);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_fragment_program]", var2.GL_ARB_fragment_program);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_fragment_shader]", var2.GL_ARB_fragment_shader);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_shader_objects]", var2.GL_ARB_shader_objects);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_buffer_object]", var2.GL_ARB_vertex_buffer_object);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_framebuffer_object]", var2.GL_ARB_framebuffer_object);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_pixel_buffer_object]", var2.GL_ARB_pixel_buffer_object);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_uniform_buffer_object]", var2.GL_ARB_uniform_buffer_object);
      par1PlayerUsageSnooper.addData("gl_caps[ARB_texture_non_power_of_two]", var2.GL_ARB_texture_non_power_of_two);
      par1PlayerUsageSnooper.addData("gl_caps[gl_max_vertex_uniforms]", GL11.glGetInteger(35658));
      par1PlayerUsageSnooper.addData("gl_caps[gl_max_fragment_uniforms]", GL11.glGetInteger(35657));
      par1PlayerUsageSnooper.addData("gl_max_texture_size", getGLMaximumTextureSize());
   }

   public static int getGLMaximumTextureSize() {
      for (int var0 = 16384; var0 > 0; var0 >>= 1) {
         GL11.glTexImage2D(32868, 0, 6408, var0, var0, 0, 6408, 5121, (ByteBuffer)null);
         int var1 = GL11.glGetTexLevelParameteri(32868, 0, 4096);
         if (var1 != 0) {
            return var0;
         }
      }

      return -1;
   }

   @Override
   public boolean isSnooperEnabled() {
      return this.gameSettings.snooperEnabled;
   }

   public void setServerData(ServerData par1ServerData) {
      this.currentServerData = par1ServerData;
   }

   public ServerData getServerData() {
      return this.currentServerData;
   }

   public boolean isIntegratedServerRunning() {
      return this.integratedServerIsRunning;
   }

   public boolean isSingleplayer() {
      return this.integratedServerIsRunning && this.theIntegratedServer != null;
   }

   public IntegratedServer getIntegratedServer() {
      return this.theIntegratedServer;
   }

   public static void stopIntegratedServer() {
      if (theMinecraft != null) {
         IntegratedServer var0 = theMinecraft.getIntegratedServer();
         if (var0 != null) {
            var0.stopServer();
         }
      }
   }

   public PlayerUsageSnooper getPlayerUsageSnooper() {
      return this.usageSnooper;
   }

   public static long getSystemTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public boolean isFullScreen() {
      return this.fullscreen;
   }

   @Override
   public ILogAgent getLogAgent() {
      return this.field_94139_O;
   }

   public Timer getTimer() {
      return this.timer;
   }
}
