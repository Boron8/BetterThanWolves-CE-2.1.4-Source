package btw.world.chunk;

import java.util.LinkedList;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.LongHashMap;
import net.minecraft.src.MathHelper;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldServer;

public class ChunkTracker {
   public final WorldServer worldServer;
   private final LinkedList<EntityPlayerMP> playersTracked = new LinkedList<>();
   public final LongHashMap trackerEntriesMap = new LongHashMap();
   private final LinkedList<ChunkTrackerEntry> entriesRequiringClientUpdate = new LinkedList<>();
   private final int chunkViewDistance;
   private final int[][] offsetsXZ = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

   public ChunkTracker(WorldServer world, int iChunkViewDistance) {
      if (iChunkViewDistance > 15) {
         throw new IllegalArgumentException("Too big view radius!");
      } else if (iChunkViewDistance < 3) {
         throw new IllegalArgumentException("Too small view radius!");
      } else {
         this.chunkViewDistance = iChunkViewDistance;
         this.worldServer = world;
      }
   }

   public void update() {
      if (!this.entriesRequiringClientUpdate.isEmpty()) {
         for (ChunkTrackerEntry tempEntry : this.entriesRequiringClientUpdate) {
            tempEntry.sendUpdatesToWatchingPlayers();
         }

         this.entriesRequiringClientUpdate.clear();
      }

      if (this.playersTracked.isEmpty()) {
         WorldProvider provider = this.worldServer.provider;
         if (!provider.canRespawnHere()) {
            this.worldServer.theChunkProviderServer.unloadAllChunks();
         }
      }
   }

   public void flagBlockForClientUpdate(int i, int j, int k) {
      int iChunkX = i >> 4;
      int iChunkZ = k >> 4;
      ChunkTrackerEntry entry = this.getTrackerEntry(iChunkX, iChunkZ);
      if (entry != null) {
         if (!entry.requiresClientUpdate()) {
            this.entriesRequiringClientUpdate.add(entry);
         }

         entry.flagBlockForUpdate(i & 15, j, k & 15);
      }
   }

   public void addPlayer(EntityPlayerMP player) {
      int iPlayerChunkX = MathHelper.floor_double(player.posX / 16.0);
      int iPlayerChunkZ = MathHelper.floor_double(player.posZ / 16.0);
      player.managedPosX = player.posX;
      player.managedPosZ = player.posZ;

      for (int iTempChunkX = iPlayerChunkX - this.chunkViewDistance; iTempChunkX <= iPlayerChunkX + this.chunkViewDistance; iTempChunkX++) {
         for (int iTempChunkZ = iPlayerChunkZ - this.chunkViewDistance; iTempChunkZ <= iPlayerChunkZ + this.chunkViewDistance; iTempChunkZ++) {
            this.getOrCreateTrackerEntry(iTempChunkX, iTempChunkZ).addPlayerWatching(player);
         }
      }

      this.playersTracked.add(player);
      this.filterChunksToBeSentToClient(player);
   }

   public void updateMovingPlayer(EntityPlayerMP player) {
      double dDeltaManagedX = player.posX - player.managedPosX;
      double dDeltaManagedZ = player.posZ - player.managedPosZ;
      double dDistManagedSq = dDeltaManagedX * dDeltaManagedX + dDeltaManagedZ * dDeltaManagedZ;
      if (dDistManagedSq >= 64.0) {
         int iPlayerChunkX = MathHelper.floor_double(player.posX / 16.0);
         int iPlayerChunkZ = MathHelper.floor_double(player.posZ / 16.0);
         int iManagedChunkX = MathHelper.floor_double(player.managedPosX / 16.0);
         int iManagedChunkZ = MathHelper.floor_double(player.managedPosZ / 16.0);
         if (iManagedChunkX != iPlayerChunkX || iManagedChunkZ != iPlayerChunkZ) {
            for (int iTempChunkX = iPlayerChunkX - this.chunkViewDistance; iTempChunkX <= iPlayerChunkX + this.chunkViewDistance; iTempChunkX++) {
               for (int iTempChunkZ = iPlayerChunkZ - this.chunkViewDistance; iTempChunkZ <= iPlayerChunkZ + this.chunkViewDistance; iTempChunkZ++) {
                  if (!this.areWithinAxisDistance(iTempChunkX, iTempChunkZ, iManagedChunkX, iManagedChunkZ, this.chunkViewDistance)) {
                     ChunkTrackerEntry tempEntry = this.getOrCreateTrackerEntry(iTempChunkX, iTempChunkZ);
                     tempEntry.addPlayerWatching(player);
                  }
               }
            }

            for (int iTempChunkX = iManagedChunkX - this.chunkViewDistance; iTempChunkX <= iManagedChunkX + this.chunkViewDistance; iTempChunkX++) {
               for (int iTempChunkZx = iManagedChunkZ - this.chunkViewDistance; iTempChunkZx <= iManagedChunkZ + this.chunkViewDistance; iTempChunkZx++) {
                  if (!this.areWithinAxisDistance(iTempChunkX, iTempChunkZx, iPlayerChunkX, iPlayerChunkZ, this.chunkViewDistance)) {
                     ChunkTrackerEntry tempEntry = this.getTrackerEntry(iTempChunkX, iTempChunkZx);
                     if (tempEntry != null) {
                        tempEntry.removePlayerWatching(player);
                     }
                  }
               }
            }

            this.filterChunksToBeSentToClient(player);
            player.managedPosX = player.posX;
            player.managedPosZ = player.posZ;
         }
      }
   }

   public void removeChunkFromTracker(ChunkTrackerEntry entry) {
      ChunkCoordIntPair coord = entry.coord;
      long lKey = this.computeTrackerEntryKey(coord.chunkXPos, coord.chunkZPos);
      this.trackerEntriesMap.remove(lKey);
      if (entry.requiresClientUpdate()) {
         this.entriesRequiringClientUpdate.remove(this);
      }

      this.worldServer.theChunkProviderServer.unloadChunksIfNotNearSpawn(coord.chunkXPos, coord.chunkZPos);
   }

   public void filterChunksToBeSentToClient(EntityPlayerMP player) {
      LinkedList<ChunkCoordIntPair> oldChunksList = player.chunksToBeSentToClient;
      player.chunksToBeSentToClient = new LinkedList<>();
      int iPlayerChunkX = MathHelper.floor_double(player.posX / 16.0);
      int iPlayerChunkZ = MathHelper.floor_double(player.posZ / 16.0);
      ChunkTrackerEntry playerChunkEntry = this.getOrCreateTrackerEntry(iPlayerChunkX, iPlayerChunkZ);
      if (oldChunksList.contains(playerChunkEntry.getChunkLocation())) {
         player.chunksToBeSentToClient.add(playerChunkEntry.getChunkLocation());
      }

      for (int iTempDist = 1; iTempDist <= this.chunkViewDistance; iTempDist++) {
         int iRunningChunkX = iPlayerChunkX - iTempDist;
         int iRunningChunkZ = iPlayerChunkZ - iTempDist;

         for (int iRunningSide = 0; iRunningSide < 4; iRunningSide++) {
            int iSideWidth = iTempDist * 2 + 1;
            int iSideOffsetX = this.offsetsXZ[iRunningSide][0];
            int iSideOffsetZ = this.offsetsXZ[iRunningSide][1];

            for (int iTempCount = 0; iTempCount < iSideWidth - 1; iTempCount++) {
               ChunkTrackerEntry tempEntry = this.getOrCreateTrackerEntry(iRunningChunkX, iRunningChunkZ);
               if (oldChunksList.contains(tempEntry.getChunkLocation())) {
                  player.chunksToBeSentToClient.add(tempEntry.getChunkLocation());
               }

               iRunningChunkX += iSideOffsetX;
               iRunningChunkZ += iSideOffsetZ;
            }
         }
      }
   }

   public void removePlayer(EntityPlayerMP player) {
      int iPlayerChunkX = MathHelper.floor_double(player.managedPosX / 16.0);
      int iPlayerChunkZ = MathHelper.floor_double(player.managedPosZ / 16.0);

      for (int iTempChunkX = iPlayerChunkX - this.chunkViewDistance; iTempChunkX <= iPlayerChunkX + this.chunkViewDistance; iTempChunkX++) {
         for (int iTempChunkZ = iPlayerChunkZ - this.chunkViewDistance; iTempChunkZ <= iPlayerChunkZ + this.chunkViewDistance; iTempChunkZ++) {
            ChunkTrackerEntry tempEntry = this.getTrackerEntry(iTempChunkX, iTempChunkZ);
            if (tempEntry != null) {
               tempEntry.removePlayerWatching(player);
            }
         }
      }

      this.playersTracked.remove(player);
   }

   public boolean isChunkWatchedByPlayerAndSentToClient(EntityPlayerMP player, int iChunkX, int iChunkZ) {
      ChunkTrackerEntry entry = this.getTrackerEntry(iChunkX, iChunkZ);
      return entry != null && entry.getPlayersInChunk().contains(player) && !player.chunksToBeSentToClient.contains(entry.getChunkLocation());
   }

   private boolean areWithinAxisDistance(int iX1, int iZ1, int iX2, int iZ2, int iAxisDist) {
      int iDeltaX = iX1 - iX2;
      int iDeltaZ = iZ1 - iZ2;
      return iDeltaX >= -iAxisDist && iDeltaX <= iAxisDist ? iDeltaZ >= -iAxisDist && iDeltaZ <= iAxisDist : false;
   }

   public static int getFurthestViewableBlock(int iChunkViewDistance) {
      return iChunkViewDistance * 16 - 16;
   }

   public boolean isChunkBeingWatched(int iChunkX, int iChunkZ) {
      return this.getTrackerEntry(iChunkX, iChunkZ) != null;
   }

   private long computeTrackerEntryKey(int iChunkX, int iChunkZ) {
      return iChunkX + 2147483647L | iChunkZ + 2147483647L << 32;
   }

   private ChunkTrackerEntry getTrackerEntry(int iChunkX, int iChunkZ) {
      long lKey = this.computeTrackerEntryKey(iChunkX, iChunkZ);
      return (ChunkTrackerEntry)this.trackerEntriesMap.getValueByKey(lKey);
   }

   private ChunkTrackerEntry getOrCreateTrackerEntry(int iChunkX, int iChunkZ) {
      ChunkTrackerEntry entry = this.getTrackerEntry(iChunkX, iChunkZ);
      if (entry == null) {
         entry = new ChunkTrackerEntry(this, iChunkX, iChunkZ);
         this.trackerEntriesMap.add(this.computeTrackerEntryKey(iChunkX, iChunkZ), entry);
      }

      return entry;
   }
}
