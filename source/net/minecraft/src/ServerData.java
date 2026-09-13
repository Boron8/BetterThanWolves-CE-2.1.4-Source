package net.minecraft.src;

public class ServerData {
   public String serverName;
   public String serverIP;
   public String populationInfo;
   public String serverMOTD;
   public long pingToServer;
   public int field_82821_f = 61;
   public String gameVersion = "1.5.2";
   public boolean field_78841_f = false;
   private boolean field_78842_g = true;
   private boolean acceptsTextures = false;
   private boolean hideAddress = false;

   public ServerData(String var1, String var2) {
      this.serverName = var1;
      this.serverIP = var2;
   }

   public NBTTagCompound getNBTCompound() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.setString("name", this.serverName);
      var1.setString("ip", this.serverIP);
      var1.setBoolean("hideAddress", this.hideAddress);
      if (!this.field_78842_g) {
         var1.setBoolean("acceptTextures", this.acceptsTextures);
      }

      return var1;
   }

   public boolean getAcceptsTextures() {
      return this.acceptsTextures;
   }

   public boolean func_78840_c() {
      return this.field_78842_g;
   }

   public void setAcceptsTextures(boolean var1) {
      this.acceptsTextures = var1;
      this.field_78842_g = false;
   }

   public boolean isHidingAddress() {
      return this.hideAddress;
   }

   public void setHideAddress(boolean var1) {
      this.hideAddress = var1;
   }

   public static ServerData getServerDataFromNBTCompound(NBTTagCompound var0) {
      ServerData var1 = new ServerData(var0.getString("name"), var0.getString("ip"));
      var1.hideAddress = var0.getBoolean("hideAddress");
      if (var0.hasKey("acceptTextures")) {
         var1.setAcceptsTextures(var0.getBoolean("acceptTextures"));
      }

      return var1;
   }
}
