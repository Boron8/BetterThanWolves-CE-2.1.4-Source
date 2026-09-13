package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
   private final WorldServer theWorldServer;
   private final List players = new ArrayList();
   private final LongHashMap playerInstances = new LongHashMap();
   private final List chunkWatcherWithPlayers = new ArrayList();
   private final int playerViewRadius;
   private final int[][] xzDirectionsConst = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

   public PlayerManager(WorldServer var1, int var2) {
      if (var2 > 15) {
         throw new IllegalArgumentException("Too big view radius!");
      } else if (var2 < 3) {
         throw new IllegalArgumentException("Too small view radius!");
      } else {
         this.playerViewRadius = var2;
         this.theWorldServer = var1;
      }
   }

   public WorldServer getWorldServer() {
      return this.theWorldServer;
   }

   public void updatePlayerInstances() {
      for (int var1 = 0; var1 < this.chunkWatcherWithPlayers.size(); var1++) {
         ((PlayerInstance)this.chunkWatcherWithPlayers.get(var1)).sendChunkUpdate();
      }

      this.chunkWatcherWithPlayers.clear();
      if (this.players.isEmpty()) {
         WorldProvider var2 = this.theWorldServer.provider;
         if (!var2.canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
         }
      }
   }

   private PlayerInstance getOrCreateChunkWatcher(int var1, int var2, boolean var3) {
      long var4 = var1 + 2147483647L | var2 + 2147483647L << 32;
      PlayerInstance var6 = (PlayerInstance)this.playerInstances.getValueByKey(var4);
      if (var6 == null && var3) {
         var6 = new PlayerInstance(this, var1, var2);
         this.playerInstances.add(var4, var6);
      }

      return var6;
   }

   public void flagChunkForUpdate(int var1, int var2, int var3) {
      int var4 = var1 >> 4;
      int var5 = var3 >> 4;
      PlayerInstance var6 = this.getOrCreateChunkWatcher(var4, var5, false);
      if (var6 != null) {
         var6.flagChunkForUpdate(var1 & 15, var2, var3 & 15);
      }
   }

   public void addPlayer(EntityPlayerMP var1) {
      int var2 = (int)var1.posX >> 4;
      int var3 = (int)var1.posZ >> 4;
      var1.managedPosX = var1.posX;
      var1.managedPosZ = var1.posZ;

      for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; var4++) {
         for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; var5++) {
            this.getOrCreateChunkWatcher(var4, var5, true).addPlayerToChunkWatchingList(var1);
         }
      }

      this.players.add(var1);
      this.filterChunkLoadQueue(var1);
   }

   public void filterChunkLoadQueue(EntityPlayerMP var1) {
      ArrayList var2 = new ArrayList(var1.loadedChunks);
      int var3 = 0;
      int var4 = this.playerViewRadius;
      int var5 = (int)var1.posX >> 4;
      int var6 = (int)var1.posZ >> 4;
      int var7 = 0;
      int var8 = 0;
      ChunkCoordIntPair var9 = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(var5, var6, true));
      var1.loadedChunks.clear();
      if (var2.contains(var9)) {
         var1.loadedChunks.add(var9);
      }

      for (int var10 = 1; var10 <= var4 * 2; var10++) {
         for (int var11 = 0; var11 < 2; var11++) {
            int[] var12 = this.xzDirectionsConst[var3++ % 4];

            for (int var13 = 0; var13 < var10; var13++) {
               var7 += var12[0];
               var8 += var12[1];
               var9 = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true));
               if (var2.contains(var9)) {
                  var1.loadedChunks.add(var9);
               }
            }
         }
      }

      var3 %= 4;

      for (int var17 = 0; var17 < var4 * 2; var17++) {
         var7 += this.xzDirectionsConst[var3][0];
         var8 += this.xzDirectionsConst[var3][1];
         var9 = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true));
         if (var2.contains(var9)) {
            var1.loadedChunks.add(var9);
         }
      }
   }

   public void removePlayer(EntityPlayerMP var1) {
      int var2 = (int)var1.managedPosX >> 4;
      int var3 = (int)var1.managedPosZ >> 4;

      for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; var4++) {
         for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; var5++) {
            PlayerInstance var6 = this.getOrCreateChunkWatcher(var4, var5, false);
            if (var6 != null) {
               var6.sendThisChunkToPlayer(var1);
            }
         }
      }

      this.players.remove(var1);
   }

   private boolean func_72684_a(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 - var3;
      int var7 = var2 - var4;
      return var6 < -var5 || var6 > var5 ? false : var7 >= -var5 && var7 <= var5;
   }

   public void updateMountedMovingPlayer(EntityPlayerMP var1) {
      int var2 = (int)var1.posX >> 4;
      int var3 = (int)var1.posZ >> 4;
      double var4 = var1.managedPosX - var1.posX;
      double var6 = var1.managedPosZ - var1.posZ;
      double var8 = var4 * var4 + var6 * var6;
      if (!(var8 < 64.0)) {
         int var10 = (int)var1.managedPosX >> 4;
         int var11 = (int)var1.managedPosZ >> 4;
         int var12 = this.playerViewRadius;
         int var13 = var2 - var10;
         int var14 = var3 - var11;
         if (var13 != 0 || var14 != 0) {
            for (int var15 = var2 - var12; var15 <= var2 + var12; var15++) {
               for (int var16 = var3 - var12; var16 <= var3 + var12; var16++) {
                  if (!this.func_72684_a(var15, var16, var10, var11, var12)) {
                     this.getOrCreateChunkWatcher(var15, var16, true).addPlayerToChunkWatchingList(var1);
                  }

                  if (!this.func_72684_a(var15 - var13, var16 - var14, var2, var3, var12)) {
                     PlayerInstance var17 = this.getOrCreateChunkWatcher(var15 - var13, var16 - var14, false);
                     if (var17 != null) {
                        var17.sendThisChunkToPlayer(var1);
                     }
                  }
               }
            }

            this.filterChunkLoadQueue(var1);
            var1.managedPosX = var1.posX;
            var1.managedPosZ = var1.posZ;
         }
      }
   }

   public boolean isPlayerWatchingChunk(EntityPlayerMP var1, int var2, int var3) {
      PlayerInstance var4 = this.getOrCreateChunkWatcher(var2, var3, false);
      return var4 == null ? false : PlayerInstance.getPlayersInChunk(var4).contains(var1) && !var1.loadedChunks.contains(PlayerInstance.getChunkLocation(var4));
   }

   public static int getFurthestViewableBlock(int var0) {
      return var0 * 16 - 16;
   }
}
