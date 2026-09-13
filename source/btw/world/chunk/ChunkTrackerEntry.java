package btw.world.chunk;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet51MapChunk;
import net.minecraft.src.Packet52MultiBlockChange;
import net.minecraft.src.Packet53BlockChange;
import net.minecraft.src.TileEntity;

class ChunkTrackerEntry {
   private final ChunkTracker chunkTracker;
   public final ChunkCoordIntPair coord;
   private final List<EntityPlayerMP> playersWatching = new ArrayList<>();
   private short[] locationsRequiringClientUpdate = new short[64];
   private int numLocationsRequiringClientUpdate = 0;
   private int verticalChunksToUpdatePlayersBitfield;

   public ChunkTrackerEntry(ChunkTracker chunkTracker, int iChunkX, int iChunkZ) {
      this.chunkTracker = chunkTracker;
      this.coord = new ChunkCoordIntPair(iChunkX, iChunkZ);
      chunkTracker.worldServer.theChunkProviderServer.loadChunk(iChunkX, iChunkZ);
   }

   public void addPlayerWatching(EntityPlayerMP player) {
      if (this.playersWatching.contains(player)) {
         throw new IllegalStateException("Failed to add player. " + player + " already is in chunk " + this.coord.chunkXPos + ", " + this.coord.chunkZPos);
      } else {
         this.playersWatching.add(player);
         player.chunksToBeSentToClient.add(this.coord);
      }
   }

   public boolean requiresClientUpdate() {
      return this.numLocationsRequiringClientUpdate > 0;
   }

   public void removePlayerWatching(EntityPlayerMP player) {
      if (this.playersWatching.contains(player)) {
         player.playerNetServerHandler.sendPacket(new Packet51MapChunk(this.chunkTracker.worldServer.e(this.coord.chunkXPos, this.coord.chunkZPos), true, 0));
         this.playersWatching.remove(player);
         player.chunksToBeSentToClient.remove(this.coord);
         if (this.playersWatching.isEmpty()) {
            this.chunkTracker.removeChunkFromTracker(this);
         }
      }
   }

   private short getBitEncodingForLocalPos(int iLocalX, int iLocalY, int iLocalZ) {
      return (short)(iLocalX << 12 | iLocalZ << 8 | iLocalY);
   }

   public void flagBlockForUpdate(int iLocalX, int iLocalY, int iLocalZ) {
      this.verticalChunksToUpdatePlayersBitfield |= 1 << (iLocalY >> 4);
      if (this.numLocationsRequiringClientUpdate < 64) {
         short sBitCode = this.getBitEncodingForLocalPos(iLocalX, iLocalY, iLocalZ);

         for (int iTempIndex = 0; iTempIndex < this.numLocationsRequiringClientUpdate; iTempIndex++) {
            if (this.locationsRequiringClientUpdate[iTempIndex] == sBitCode) {
               return;
            }
         }

         this.locationsRequiringClientUpdate[this.numLocationsRequiringClientUpdate++] = sBitCode;
      }
   }

   private void sendToPlayersWatchingNotWaitingFullChunk(Packet packet) {
      for (int iTempCount = 0; iTempCount < this.playersWatching.size(); iTempCount++) {
         EntityPlayerMP tempPlayer = this.playersWatching.get(iTempCount);
         if (!tempPlayer.chunksToBeSentToClient.contains(this.coord)) {
            tempPlayer.playerNetServerHandler.sendPacket(packet);
         }
      }
   }

   public void sendUpdatesToWatchingPlayers() {
      if (this.numLocationsRequiringClientUpdate != 0) {
         int iOffsetX = this.coord.chunkXPos * 16;
         int iOffsetZ = this.coord.chunkZPos * 16;
         if (this.numLocationsRequiringClientUpdate == 1) {
            int iBlockX = iOffsetX + (this.locationsRequiringClientUpdate[0] >> 12 & 15);
            int iBlockY = this.locationsRequiringClientUpdate[0] & 255;
            int iBlockZ = iOffsetZ + (this.locationsRequiringClientUpdate[0] >> 8 & 15);
            this.sendToPlayersWatchingNotWaitingFullChunk(new Packet53BlockChange(iBlockX, iBlockY, iBlockZ, this.chunkTracker.worldServer));
            if (this.chunkTracker.worldServer.d(iBlockX, iBlockY, iBlockZ)) {
               this.sendTileEntityToPlayersWatchingChunk(this.chunkTracker.worldServer.r(iBlockX, iBlockY, iBlockZ));
            }
         } else if (this.numLocationsRequiringClientUpdate != 64) {
            this.sendToPlayersWatchingNotWaitingFullChunk(
               new Packet52MultiBlockChange(
                  this.coord.chunkXPos,
                  this.coord.chunkZPos,
                  this.locationsRequiringClientUpdate,
                  this.numLocationsRequiringClientUpdate,
                  this.chunkTracker.worldServer
               )
            );

            for (int iTempIndex = 0; iTempIndex < this.numLocationsRequiringClientUpdate; iTempIndex++) {
               int iBlockX = iOffsetX + (this.locationsRequiringClientUpdate[iTempIndex] >> 12 & 15);
               int iBlockY = this.locationsRequiringClientUpdate[iTempIndex] & 255;
               int iBlockZ = iOffsetZ + (this.locationsRequiringClientUpdate[iTempIndex] >> 8 & 15);
               if (this.chunkTracker.worldServer.d(iBlockX, iBlockY, iBlockZ)) {
                  this.sendTileEntityToPlayersWatchingChunk(this.chunkTracker.worldServer.r(iBlockX, iBlockY, iBlockZ));
               }
            }
         } else {
            this.sendToPlayersWatchingNotWaitingFullChunk(
               new Packet51MapChunk(
                  this.chunkTracker.worldServer.e(this.coord.chunkXPos, this.coord.chunkZPos), false, this.verticalChunksToUpdatePlayersBitfield
               )
            );

            for (int iTempVerticalChunk = 0; iTempVerticalChunk < 16; iTempVerticalChunk++) {
               if ((this.verticalChunksToUpdatePlayersBitfield & 1 << iTempVerticalChunk) != 0) {
                  int iTempY = iTempVerticalChunk << 4;
                  List<TileEntity> tempTileEntities = this.chunkTracker
                     .worldServer
                     .getAllTileEntityInBox(iOffsetX, iTempY, iOffsetZ, iOffsetX + 16, iTempY + 16, iOffsetZ + 16);

                  for (int iTempCount = 0; iTempCount < tempTileEntities.size(); iTempCount++) {
                     this.sendTileEntityToPlayersWatchingChunk(tempTileEntities.get(iTempCount));
                  }
               }
            }
         }

         this.numLocationsRequiringClientUpdate = 0;
         this.verticalChunksToUpdatePlayersBitfield = 0;
      }
   }

   private void sendTileEntityToPlayersWatchingChunk(TileEntity tileEntity) {
      Packet packet = tileEntity.getDescriptionPacket();
      if (packet != null) {
         this.sendToPlayersWatchingNotWaitingFullChunk(packet);
      }
   }

   public ChunkCoordIntPair getChunkLocation() {
      return this.coord;
   }

   public List getPlayersInChunk() {
      return this.playersWatching;
   }
}
