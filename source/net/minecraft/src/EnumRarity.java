package net.minecraft.src;

public enum EnumRarity {
   common(15, "Common"),
   uncommon(14, "Uncommon"),
   rare(11, "Rare"),
   epic(13, "Epic");

   public final int rarityColor;
   public final String rarityName;

   private EnumRarity(int var3, String var4) {
      this.rarityColor = var3;
      this.rarityName = var4;
   }
}
