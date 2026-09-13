package net.minecraft.src;

import btw.world.util.difficulty.Difficulties;
import btw.world.util.difficulty.Difficulty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class WorldSettings {
   private final long seed;
   private final EnumGameType theGameType;
   private final Difficulty difficulty;
   private final boolean mapFeaturesEnabled;
   private final boolean hardcoreEnabled;
   private final WorldType terrainType;
   private boolean commandsAllowed;
   private boolean bonusChestEnabled;
   private String field_82751_h = "";

   @Deprecated
   public WorldSettings(long par1, EnumGameType par3EnumGameType, boolean par4, boolean par5, WorldType par6WorldType) {
      this(par1, par3EnumGameType, par4, par5, par6WorldType, Difficulties.STANDARD);
   }

   public WorldSettings(long par1, EnumGameType par3EnumGameType, boolean par4, boolean par5, WorldType par6WorldType, Difficulty difficulty) {
      this.seed = par1;
      this.theGameType = par3EnumGameType;
      this.mapFeaturesEnabled = par4;
      this.hardcoreEnabled = par5;
      this.terrainType = par6WorldType;
      this.difficulty = difficulty;
   }

   public WorldSettings(WorldInfo par1WorldInfo) {
      this(
         par1WorldInfo.getSeed(),
         par1WorldInfo.getGameType(),
         par1WorldInfo.isMapFeaturesEnabled(),
         par1WorldInfo.isHardcoreModeEnabled(),
         par1WorldInfo.getTerrainType(),
         par1WorldInfo.getDifficulty()
      );
   }

   public WorldSettings enableBonusChest() {
      this.bonusChestEnabled = true;
      return this;
   }

   @Environment(EnvType.CLIENT)
   public WorldSettings enableCommands() {
      this.commandsAllowed = true;
      return this;
   }

   public WorldSettings func_82750_a(String par1Str) {
      this.field_82751_h = par1Str;
      return this;
   }

   public boolean isBonusChestEnabled() {
      return this.bonusChestEnabled;
   }

   public long getSeed() {
      return this.seed;
   }

   public EnumGameType getGameType() {
      return this.theGameType;
   }

   public boolean getHardcoreEnabled() {
      return this.hardcoreEnabled;
   }

   public boolean isMapFeaturesEnabled() {
      return this.mapFeaturesEnabled;
   }

   public WorldType getTerrainType() {
      return this.terrainType;
   }

   public boolean areCommandsAllowed() {
      return this.commandsAllowed;
   }

   public static EnumGameType getGameTypeById(int par0) {
      return EnumGameType.getByID(par0);
   }

   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   public String func_82749_j() {
      return this.field_82751_h;
   }
}
