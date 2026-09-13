package net.minecraft.src;

public class SaveFormatComparator implements Comparable {
   private final String fileName;
   private final String displayName;
   private final long lastTimePlayed;
   private final long sizeOnDisk;
   private final boolean requiresConversion;
   private final EnumGameType theEnumGameType;
   private final boolean hardcore;
   private final boolean cheatsEnabled;

   public SaveFormatComparator(String var1, String var2, long var3, long var5, EnumGameType var7, boolean var8, boolean var9, boolean var10) {
      this.fileName = var1;
      this.displayName = var2;
      this.lastTimePlayed = var3;
      this.sizeOnDisk = var5;
      this.theEnumGameType = var7;
      this.requiresConversion = var8;
      this.hardcore = var9;
      this.cheatsEnabled = var10;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public boolean requiresConversion() {
      return this.requiresConversion;
   }

   public long getLastTimePlayed() {
      return this.lastTimePlayed;
   }

   public int compareTo(SaveFormatComparator var1) {
      if (this.lastTimePlayed < var1.lastTimePlayed) {
         return 1;
      } else {
         return this.lastTimePlayed > var1.lastTimePlayed ? -1 : this.fileName.compareTo(var1.fileName);
      }
   }

   public EnumGameType getEnumGameType() {
      return this.theEnumGameType;
   }

   public boolean isHardcoreModeEnabled() {
      return this.hardcore;
   }

   public boolean getCheatsEnabled() {
      return this.cheatsEnabled;
   }
}
