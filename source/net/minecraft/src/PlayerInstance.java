package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class PlayerInstance {
   private final List playersInChunk;
   private final ChunkCoordIntPair chunkLocation;
   private short[] locationOfBlockChange;
   private int numberOfTilesToUpdate;
   private int field_73260_f;

   public PlayerInstance(PlayerManager var1, int var2, int var3) {
      this.thePlayerManager = var1;
      this.playersInChunk = new ArrayList();
      this.locationOfBlockChange = new short[64];
      this.numberOfTilesToUpdate = 0;
      this.chunkLocation = new ChunkCoordIntPair(var2, var3);
      var1.getWorldServer().theChunkProviderServer.loadChunk(var2, var3);
   }

   public void addPlayerToChunkWatchingList(EntityPlayerMP var1) {
      if (this.playersInChunk.contains(var1)) {
         throw new IllegalStateException(
            "Failed to add player. " + var1 + " already is in chunk " + this.chunkLocation.chunkXPos + ", " + this.chunkLocation.chunkZPos
         );
      } else {
         this.playersInChunk.add(var1);
         var1.loadedChunks.add(this.chunkLocation);
      }
   }

   public void sendThisChunkToPlayer(EntityPlayerMP var1) {
      if (this.playersInChunk.contains(var1)) {
         var1.playerNetServerHandler
            .sendPacketToPlayer(
               new Packet51MapChunk(PlayerManager.getWorldServer(this.thePlayerManager).e(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos), true, 0)
            );
         this.playersInChunk.remove(var1);
         var1.loadedChunks.remove(this.chunkLocation);
         if (this.playersInChunk.isEmpty()) {
            long var2 = this.chunkLocation.chunkXPos + 2147483647L | this.chunkLocation.chunkZPos + 2147483647L << 32;
            PlayerManager.getChunkWatchers(this.thePlayerManager).remove(var2);
            if (this.numberOfTilesToUpdate > 0) {
               PlayerManager.getChunkWatchersWithPlayers(this.thePlayerManager).remove(this);
            }

            this.thePlayerManager
               .getWorldServer()
               .theChunkProviderServer
               .unloadChunksIfNotNearSpawn(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos);
         }
      }
   }

   public void flagChunkForUpdate(int var1, int var2, int var3) {
      if (this.numberOfTilesToUpdate == 0) {
         PlayerManager.getChunkWatchersWithPlayers(this.thePlayerManager).add(this);
      }

      this.field_73260_f |= 1 << (var2 >> 4);
      if (this.numberOfTilesToUpdate < 64) {
         short var4 = (short)(var1 << 12 | var3 << 8 | var2);

         for (int var5 = 0; var5 < this.numberOfTilesToUpdate; var5++) {
            if (this.locationOfBlockChange[var5] == var4) {
               return;
            }
         }

         this.locationOfBlockChange[this.numberOfTilesToUpdate++] = var4;
      }
   }

   public void sendToAllPlayersWatchingChunk(Packet var1) {
      for (int var2 = 0; var2 < this.playersInChunk.size(); var2++) {
         EntityPlayerMP var3 = (EntityPlayerMP)this.playersInChunk.get(var2);
         if (!var3.loadedChunks.contains(this.chunkLocation)) {
            var3.playerNetServerHandler.sendPacketToPlayer(var1);
         }
      }
   }

   public void sendChunkUpdate() {
      if (this.numberOfTilesToUpdate != 0) {
         if (this.numberOfTilesToUpdate == 1) {
            int var1 = this.chunkLocation.chunkXPos * 16 + (this.locationOfBlockChange[0] >> 12 & 15);
            int var2 = this.locationOfBlockChange[0] & 255;
            int var3 = this.chunkLocation.chunkZPos * 16 + (this.locationOfBlockChange[0] >> 8 & 15);
            this.sendToAllPlayersWatchingChunk(new Packet53BlockChange(var1, var2, var3, PlayerManager.getWorldServer(this.thePlayerManager)));
            if (PlayerManager.getWorldServer(this.thePlayerManager).d(var1, var2, var3)) {
               this.sendTileToAllPlayersWatchingChunk(PlayerManager.getWorldServer(this.thePlayerManager).r(var1, var2, var3));
            }
         } else if (this.numberOfTilesToUpdate == 64) {
            int var7 = this.chunkLocation.chunkXPos * 16;
            int var9 = this.chunkLocation.chunkZPos * 16;
            this.sendToAllPlayersWatchingChunk(
               new Packet51MapChunk(
                  PlayerManager.getWorldServer(this.thePlayerManager).e(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos), false, this.field_73260_f
               )
            );

            for (int var11 = 0; var11 < 16; var11++) {
               if ((this.field_73260_f & 1 << var11) != 0) {
                  int var4 = var11 << 4;
                  List var5 = PlayerManager.getWorldServer(this.thePlayerManager).getAllTileEntityInBox(var7, var4, var9, var7 + 16, var4 + 16, var9 + 16);

                  for (int var6 = 0; var6 < var5.size(); var6++) {
                     this.sendTileToAllPlayersWatchingChunk((TileEntity)var5.get(var6));
                  }
               }
            }
         } else {
            this.sendToAllPlayersWatchingChunk(
               new Packet52MultiBlockChange(
                  this.chunkLocation.chunkXPos,
                  this.chunkLocation.chunkZPos,
                  this.locationOfBlockChange,
                  this.numberOfTilesToUpdate,
                  PlayerManager.getWorldServer(this.thePlayerManager)
               )
            );

            for (int var8 = 0; var8 < this.numberOfTilesToUpdate; var8++) {
               int var10 = this.chunkLocation.chunkXPos * 16 + (this.locationOfBlockChange[var8] >> 12 & 15);
               int var12 = this.locationOfBlockChange[var8] & 255;
               int var13 = this.chunkLocation.chunkZPos * 16 + (this.locationOfBlockChange[var8] >> 8 & 15);
               if (PlayerManager.getWorldServer(this.thePlayerManager).d(var10, var12, var13)) {
                  this.sendTileToAllPlayersWatchingChunk(PlayerManager.getWorldServer(this.thePlayerManager).r(var10, var12, var13));
               }
            }
         }

         this.numberOfTilesToUpdate = 0;
         this.field_73260_f = 0;
      }
   }

   private void sendTileToAllPlayersWatchingChunk(TileEntity var1) {
      if (var1 != null) {
         Packet var2 = var1.getDescriptionPacket();
         if (var2 != null) {
            this.sendToAllPlayersWatchingChunk(var2);
         }
      }
   }
}
