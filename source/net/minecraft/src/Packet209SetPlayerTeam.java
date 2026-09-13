package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class Packet209SetPlayerTeam extends Packet {
   public String teamName = "";
   public String teamDisplayName = "";
   public String teamPrefix = "";
   public String teamSuffix = "";
   public Collection playerNames = new ArrayList();
   public int mode = 0;
   public int friendlyFire;

   public Packet209SetPlayerTeam() {
   }

   public Packet209SetPlayerTeam(ScorePlayerTeam var1, int var2) {
      this.teamName = var1.func_96661_b();
      this.mode = var2;
      if (var2 == 0 || var2 == 2) {
         this.teamDisplayName = var1.func_96669_c();
         this.teamPrefix = var1.func_96668_e();
         this.teamSuffix = var1.func_96663_f();
         this.friendlyFire = var1.func_98299_i();
      }

      if (var2 == 0) {
         this.playerNames.addAll(var1.getMembershipCollection());
      }
   }

   public Packet209SetPlayerTeam(ScorePlayerTeam var1, Collection var2, int var3) {
      if (var3 != 3 && var3 != 4) {
         throw new IllegalArgumentException("Method must be join or leave for player constructor");
      } else if (var2 != null && !var2.isEmpty()) {
         this.mode = var3;
         this.teamName = var1.func_96661_b();
         this.playerNames.addAll(var2);
      } else {
         throw new IllegalArgumentException("Players cannot be null/empty");
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.teamName = a(var1, 16);
      this.mode = var1.readByte();
      if (this.mode == 0 || this.mode == 2) {
         this.teamDisplayName = a(var1, 32);
         this.teamPrefix = a(var1, 16);
         this.teamSuffix = a(var1, 16);
         this.friendlyFire = var1.readByte();
      }

      if (this.mode == 0 || this.mode == 3 || this.mode == 4) {
         short var2 = var1.readShort();

         for (int var3 = 0; var3 < var2; var3++) {
            this.playerNames.add(a(var1, 16));
         }
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.teamName, var1);
      var1.writeByte(this.mode);
      if (this.mode == 0 || this.mode == 2) {
         a(this.teamDisplayName, var1);
         a(this.teamPrefix, var1);
         a(this.teamSuffix, var1);
         var1.writeByte(this.friendlyFire);
      }

      if (this.mode == 0 || this.mode == 3 || this.mode == 4) {
         var1.writeShort(this.playerNames.size());

         for (String var3 : this.playerNames) {
            a(var3, var1);
         }
      }
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleSetPlayerTeam(this);
   }

   @Override
   public int getPacketSize() {
      return 3 + this.teamName.length();
   }
}
