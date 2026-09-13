package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class PropertiesFile {
   private static final MCLogger staticLogger = MCLogger.getLogger("Texture Pack");
   private final MCLogger logger;
   private final FakeResourceLocation resource;
   private final String prefix;
   private final Properties properties;
   private int warningCount;
   private int errorCount;

   public static PropertiesFile get(FakeResourceLocation resource) {
      return get(staticLogger, resource);
   }

   public static PropertiesFile getNonNull(FakeResourceLocation resource) {
      return getNonNull(staticLogger, resource);
   }

   public static PropertiesFile get(MCLogger logger, FakeResourceLocation resource) {
      Properties properties = TexturePackAPI.getProperties(resource);
      return properties == null ? null : new PropertiesFile(logger, resource, properties);
   }

   public static PropertiesFile getNonNull(MCLogger logger, FakeResourceLocation resource) {
      PropertiesFile propertiesFile = get(logger, resource);
      return propertiesFile == null ? new PropertiesFile(logger, resource, new Properties()) : propertiesFile;
   }

   public PropertiesFile(MCLogger logger, FakeResourceLocation resource) {
      this(logger, resource, new Properties());
   }

   public PropertiesFile(MCLogger logger, FakeResourceLocation resource, Properties properties) {
      this.logger = logger;
      this.resource = resource;
      this.prefix = resource == null ? "" : (resource.toString() + ": ").replace("%", "%%");
      this.properties = properties;
   }

   public String getString(String key, String defaultValue) {
      return MCPatcherUtils.getStringProperty(this.properties, key, defaultValue);
   }

   public FakeResourceLocation getResourceLocation(String key, String defaultValue) {
      String value = this.getString(key, defaultValue);
      return TexturePackAPI.parseResourceLocation(this.resource, value);
   }

   public int getInt(String key, int defaultValue) {
      return MCPatcherUtils.getIntProperty(this.properties, key, defaultValue);
   }

   public int getHex(String key, int defaultValue) {
      return MCPatcherUtils.getHexProperty(this.properties, key, defaultValue);
   }

   public float getFloat(String key, float defaultValue) {
      return MCPatcherUtils.getFloatProperty(this.properties, key, defaultValue);
   }

   public double getDouble(String key, double defaultValue) {
      return MCPatcherUtils.getDoubleProperty(this.properties, key, defaultValue);
   }

   public int[] getIntList(String key, int minValue, int maxValue) {
      return this.getIntList(key, minValue, maxValue, null);
   }

   public int[] getIntList(String key, int minValue, int maxValue, String defaultValue) {
      String value = this.getString(key, defaultValue);
      return value == null ? null : MCPatcherUtils.parseIntegerList(value, minValue, maxValue);
   }

   public boolean getBoolean(String key, boolean defaultValue) {
      return MCPatcherUtils.getBooleanProperty(this.properties, key, defaultValue);
   }

   public void setProperty(String key, String value) {
      this.properties.setProperty(key, value);
   }

   public boolean isLoggable(Level level) {
      return this.logger.isLoggable(level);
   }

   public void finest(String format, Object... params) {
      this.logger.finest(this.prefix + format, params);
   }

   public void finer(String format, Object... params) {
      this.logger.finer(this.prefix + format, params);
   }

   public void fine(String format, Object... params) {
      this.logger.fine(this.prefix + format, params);
   }

   public void info(String format, Object... params) {
      this.logger.info(this.prefix + format, params);
   }

   public void config(String format, Object... params) {
      this.logger.config(format, params);
   }

   public void warning(String format, Object... params) {
      this.logger.warning(this.prefix + format, params);
      this.warningCount++;
   }

   public boolean error(String format, Object... params) {
      this.logger.error(this.prefix + format, params);
      this.errorCount++;
      return false;
   }

   public int getWarningCount() {
      return this.warningCount;
   }

   public int getErrorCount() {
      return this.errorCount;
   }

   public boolean valid() {
      return this.getErrorCount() == 0;
   }

   public Set<Entry<String, String>> entrySet() {
      return this.properties.entrySet();
   }

   public FakeResourceLocation getResource() {
      return this.resource;
   }

   @Override
   public String toString() {
      return this.resource.toString();
   }

   @Override
   public int hashCode() {
      return this.resource.hashCode();
   }

   @Override
   public boolean equals(Object that) {
      if (this == that) {
         return true;
      } else {
         return !(that instanceof PropertiesFile) ? false : this.resource.equals(((PropertiesFile)that).resource);
      }
   }
}
