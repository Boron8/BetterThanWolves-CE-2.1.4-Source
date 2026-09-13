package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet1Login extends Packet {
   public int clientEntityId = 0;
   public WorldType terrainType;
   public boolean hardcoreMode;
   public EnumGameType gameType;
   public int dimension;
   public byte difficultySetting;
   public byte worldHeight;
   public byte maxPlayers;

   public Packet1Login() {
   }

   public Packet1Login(int var1, WorldType var2, EnumGameType var3, boolean var4, int var5, int var6, int var7, int var8) {
      this.clientEntityId = var1;
      this.terrainType = var2;
      this.dimension = var5;
      this.difficultySetting = (byte)var6;
      this.gameType = var3;
      this.worldHeight = (byte)var7;
      this.maxPlayers = (byte)var8;
      this.hardcoreMode = var4;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.clientEntityId = var1.readInt();
      String var2 = a(var1, 16);
      this.terrainType = WorldType.parseWorldType(var2);
      if (this.terrainType == null) {
         this.terrainType = WorldType.DEFAULT;
      }

      int var3 = var1.readByte();
      this.hardcoreMode = (var3 & 8) == 8;
      var3 &= -9;
      this.gameType = EnumGameType.getByID(var3);
      this.dimension = var1.readByte();
      this.difficultySetting = var1.readByte();
      this.worldHeight = var1.readByte();
      this.maxPlayers = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.clientEntityId);
      a(this.terrainType == null ? "" : this.terrainType.getWorldTypeName(), var1);
      int var2 = this.gameType.getID();
      if (this.hardcoreMode) {
         var2 |= 8;
      }

      var1.writeByte(var2);
      var1.writeByte(this.dimension);
      var1.writeByte(this.difficultySetting);
      var1.writeByte(this.worldHeight);
      var1.writeByte(this.maxPlayers);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleLogin(this);
   }

   @Override
   public int getPacketSize() {
      int var1 = 0;
      if (this.terrainType != null) {
         var1 = this.terrainType.getWorldTypeName().length();
      }

      return 6 + 2 * var1 + 4 + 4 + 1 + 1 + 1;
   }
}
