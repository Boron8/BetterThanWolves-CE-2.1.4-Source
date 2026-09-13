package btw;

import btw.network.packet.handler.CustomPacketHandler;
import btw.world.biome.BiomeDecoratorBase;
import btw.world.util.WorldData;
import btw.world.util.WorldUtils;
import btw.world.util.difficulty.Difficulties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommand;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;

public abstract class BTWAddon {
   protected String addonName;
   protected String versionString;
   protected String prefix;
   protected boolean isRequiredClientAndServer = false;
   protected boolean shouldVersionCheck = false;
   private boolean awaitingLoginAck = false;
   private int ticksSinceAckRequested = 0;
   private static final int MAX_TICKS_FOR_ACK_WAIT = 50;
   public String addonCustomPacketChannelVersionCheck;
   public String addonCustomPacketChannelVersionCheckAck;
   private ArrayList<String> configProperties = new ArrayList<>();
   private Map<String, String> configPropertyDefaults = new HashMap<>();
   private Map<String, String> configPropertyComments = new HashMap<>();
   private Map<BTWAddon, AddonHandler.DependencyType> dependencyList = new HashMap<>();
   public final Set<BTWAddon> dependants = new HashSet<>();
   public boolean hasBeenInitialized = false;

   protected BTWAddon() {
      AddonHandler.addMod(this);
   }

   public BTWAddon(String addonName, String versionString, String prefix) {
      this();
      this.addonName = addonName;
      this.versionString = versionString;
      this.prefix = prefix;
      this.addonCustomPacketChannelVersionCheck = prefix + "|VC";
      this.addonCustomPacketChannelVersionCheckAck = prefix + "|VC_Ack";
      this.shouldVersionCheck = true;
      this.isRequiredClientAndServer = true;
   }

   public void preInitialize() {
   }

   public abstract void initialize();

   public void postInitialize() {
   }

   public void onLanguageLoaded(StringTranslate translator) {
   }

   public void handleConfigProperties(Map<String, String> propertyValues) {
   }

   public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
   }

   @Deprecated
   public boolean serverCustomPacketReceived(NetServerHandler handler, Packet250CustomPayload packet) {
      return false;
   }

   public WorldData createWorldData() {
      return null;
   }

   public void decorateWorld(BiomeDecoratorBase decorator, World world, Random rand, int x, int y, BiomeGenBase biome) {
   }

   protected void registerProperty(String propertyName, String defaultValue, String comment) {
      if (!this.configPropertyDefaults.containsKey(propertyName)) {
         this.configProperties.add(propertyName);
         this.configPropertyDefaults.put(propertyName, defaultValue);
         this.configPropertyComments.put(propertyName, comment);
      } else {
         AddonHandler.logWarning(
            "Cannot add config property \""
               + propertyName
               + "\" for "
               + this.addonName
               + " because a property with that name has already been registered for this addon"
         );
      }
   }

   protected void registerProperty(String propertyName, String defaultValue) {
      this.registerProperty(propertyName, defaultValue, "");
   }

   public void registerPacketHandler(String channel, CustomPacketHandler handler) {
      if (!channel.startsWith(this.prefix + "|")) {
         throw new IllegalArgumentException("Channel must be of the format \"<addon prefix>|<channel string>\"");
      } else {
         AddonHandler.registerPacketHandler(channel, handler);
      }
   }

   public void registerAddonCommand(ICommand command) {
      AddonHandler.registerCommand(command, false);
   }

   public String getName() {
      return this.addonName;
   }

   public String getVersionString() {
      return this.versionString;
   }

   public Map<String, String> loadConfigProperties() {
      if (this.configPropertyDefaults.size() == 0) {
         return null;
      } else {
         String filename = this.prefix + ".properties";
         BufferedReader fileIn = null;

         try {
            fileIn = new BufferedReader(new FileReader("config/" + filename));
         } catch (FileNotFoundException var9) {
            try {
               Files.createDirectories(Paths.get("config"));
               File config = new File("config/" + filename);
               config.createNewFile();
               fileIn = new BufferedReader(new FileReader(config));
            } catch (IOException var8) {
               var8.printStackTrace();
            }
         }

         if (fileIn == null) {
            return null;
         } else {
            Map<String, String> propertyValues = new HashMap<>();

            String line;
            try {
               while ((line = fileIn.readLine()) != null) {
                  if (!line.startsWith("#")) {
                     String[] lineSplit = line.split("=");
                     if (this.configPropertyDefaults.containsKey(lineSplit[0])) {
                        propertyValues.put(lineSplit[0], lineSplit[1]);
                     }
                  }
               }
            } catch (IOException var10) {
               var10.printStackTrace();
            }

            for (String propertyName : this.configProperties) {
               if (!propertyValues.containsKey(propertyName)) {
                  propertyValues.put(propertyName, this.configPropertyDefaults.get(propertyName));
               }
            }

            try {
               fileIn.close();
               return propertyValues;
            } catch (IOException var7) {
               var7.printStackTrace();
               return null;
            }
         }
      }
   }

   public void repopulateConfigFile(Map<String, String> propertyValues) {
      String filename = this.prefix + ".properties";
      File config = new File("config/" + filename);

      try {
         BufferedWriter writer = Files.newBufferedWriter(config.toPath(), StandardOpenOption.TRUNCATE_EXISTING);

         for (String propertyName : this.configProperties) {
            String comment = this.configPropertyComments.get(propertyName);
            if (!comment.equals("")) {
               writer.write("\n# " + comment + "\n");
            }

            String propertyValue;
            if (propertyValues.containsKey(propertyName)) {
               propertyValue = propertyValues.get(propertyName);
            } else {
               propertyValue = this.configPropertyDefaults.get(propertyName);
            }

            writer.write(propertyName + "=" + propertyValue + "\n");
         }

         writer.close();
      } catch (IOException var9) {
         var9.printStackTrace();
      }
   }

   public void sendVersionCheckToClient(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
      if (!MinecraftServer.getServer().isSinglePlayer()) {
         WorldUtils.sendPacketToPlayer(serverHandler, new Packet3Chat("\u00a7f" + this.addonName + " V" + this.versionString));
         if (this.shouldVersionCheck) {
            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(byteArrayOutput);

            try {
               dataOutput.writeUTF(this.versionString);
            } catch (Exception var6) {
               var6.printStackTrace();
            }

            Packet250CustomPayload var4 = new Packet250CustomPayload(this.addonCustomPacketChannelVersionCheck, byteArrayOutput.toByteArray());
            WorldUtils.sendPacketToPlayer(serverHandler, var4);
            this.awaitingLoginAck = true;
         }
      } else {
         WorldUtils.sendPacketToPlayer(serverHandler, new Packet3Chat("\u00a7f" + this.addonName + " V" + this.versionString));
      }
   }

   public void sendDifficultyToClient(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
      WorldUtils.sendPacketToPlayer(serverHandler, Difficulties.createDifficultyPacket(serverHandler.mcServer));
   }

   public boolean serverAckPacketReceived(NetServerHandler serverHandler, Packet250CustomPayload packet) {
      if (this.addonCustomPacketChannelVersionCheckAck.equals(packet.channel)) {
         WorldUtils.sendPacketToPlayer(serverHandler, new Packet3Chat("\u00a7f" + this.addonName + " version check successful."));
         this.awaitingLoginAck = false;
         this.ticksSinceAckRequested = 0;
      }

      return false;
   }

   public boolean getAwaitingLoginAck() {
      return this.awaitingLoginAck;
   }

   public void incrementTicksSinceAckRequested() {
      this.ticksSinceAckRequested++;
   }

   public boolean handleAckCheck() {
      if (this.ticksSinceAckRequested > 50) {
         this.awaitingLoginAck = false;
         this.ticksSinceAckRequested = 0;
         return false;
      } else {
         return true;
      }
   }

   public String getLanguageFilePrefix() {
      return this.prefix;
   }

   @Deprecated
   @Environment(EnvType.CLIENT)
   public boolean clientCustomPacketReceived(Minecraft mcInstance, Packet250CustomPayload packet) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   public boolean interceptCustomClientPacket(Minecraft mc, Packet250CustomPayload packet) {
      return false;
   }

   @Deprecated
   @Environment(EnvType.CLIENT)
   public boolean clientPlayCustomAuxFX(Minecraft mcInstance, World world, EntityPlayer player, int iFXID, int i, int j, int k, int iFXSpecificData) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   public EntityFX spawnCustomParticle(World world, String particleType, double x, double y, double z, double velX, double velY, double velZ) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   public void registerAddonCommandClientOnly(ICommand command) {
      AddonHandler.registerCommand(command, false);
   }

   @Environment(EnvType.CLIENT)
   public boolean versionCheckPacketReceived(Minecraft mc, Packet250CustomPayload packet) {
      try {
         WorldClient world = mc.theWorld;
         DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(packet.data));
         String var33 = dataStream.readUTF();
         if (!var33.equals(this.versionString)) {
            mc.thePlayer
               .b("\u00a74WARNING: " + this.getName() + " version mismatch detected! Local Version: " + this.versionString + " Server Version: " + var33);
         }

         ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
         DataOutputStream dataOutput = new DataOutputStream(byteArrayOutput);

         try {
            dataOutput.writeUTF(this.versionString);
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         mc.thePlayer.b("\u00a7f" + this.addonName + " version check successful.");
         return true;
      } catch (IOException var10) {
         var10.printStackTrace();
         return false;
      }
   }

   @Environment(EnvType.SERVER)
   public void registerAddonCommandServerOnly(ICommand command) {
      if (MinecraftServer.getIsServer()) {
         MinecraftServer.getServer().commandManager.a(command);
      }
   }
}
