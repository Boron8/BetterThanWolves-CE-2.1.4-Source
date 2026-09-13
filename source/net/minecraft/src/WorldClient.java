package net.minecraft.src;

import btw.world.util.difficulty.Difficulty;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class WorldClient extends World {
   private NetClientHandler sendQueue;
   private ChunkProviderClient clientChunkProvider;
   private IntHashMap entityHashSet = new IntHashMap();
   private Set entityList = new HashSet();
   private Set entitySpawnQueue = new HashSet();
   private final Minecraft mc = Minecraft.getMinecraft();
   protected LinkedList<ChunkCoordIntPair> prevActiveChunksCoordsList = new LinkedList<>();

   public WorldClient(
      NetClientHandler par1NetClientHandler, WorldSettings par2WorldSettings, int par3, int par4, Profiler par5Profiler, ILogAgent par6ILogAgent
   ) {
      super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(par3), par2WorldSettings, par5Profiler, par6ILogAgent);
      this.sendQueue = par1NetClientHandler;
      this.difficultySetting = par4;
      this.E(8, 64, 8);
      this.mapStorage = par1NetClientHandler.mapStorage;
   }

   @Override
   public void tick() {
      super.tick();
      this.a(this.H() + 1L);
      this.b(this.I() + 1L);
      this.theProfiler.startSection("reEntryProcessing");

      for (int var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); var1++) {
         Entity var2 = (Entity)this.entitySpawnQueue.iterator().next();
         this.entitySpawnQueue.remove(var2);
         if (!this.loadedEntityList.contains(var2)) {
            this.spawnEntityInWorld(var2);
         }
      }

      this.theProfiler.endStartSection("connection");
      this.sendQueue.processReadPackets();
      this.theProfiler.endStartSection("chunkCache");
      this.clientChunkProvider.unloadQueuedChunks();
      this.theProfiler.endStartSection("tiles");
      this.tickBlocksAndAmbiance();
      this.theProfiler.endSection();
      int i = this.a(1.0F);
      if (i != this.skylightSubtracted) {
         this.skylightSubtracted = i;
      }
   }

   public void invalidateBlockReceiveRegion(int par1, int par2, int par3, int par4, int par5, int par6) {
   }

   @Override
   protected IChunkProvider createChunkProvider() {
      this.clientChunkProvider = new ChunkProviderClient(this);
      return this.clientChunkProvider;
   }

   @Override
   protected void tickBlocksAndAmbiance() {
      super.tickBlocksAndAmbiance();
      this.prevActiveChunksCoordsList.retainAll(this.activeChunksCoordsList);
      if (this.prevActiveChunksCoordsList.size() == this.activeChunksCoordsList.size()) {
         this.prevActiveChunksCoordsList.clear();
      }

      int var1 = 0;

      for (ChunkCoordIntPair var3 : this.activeChunksCoordsList) {
         if (!this.prevActiveChunksCoordsList.contains(var3)) {
            int var4 = var3.chunkXPos * 16;
            int var5 = var3.chunkZPos * 16;
            this.theProfiler.startSection("getChunk");
            Chunk var6 = this.e(var3.chunkXPos, var3.chunkZPos);
            this.a(var4, var5, var6);
            this.theProfiler.endSection();
            this.prevActiveChunksCoordsList.add(var3);
            if (++var1 >= 10) {
               return;
            }
         }
      }
   }

   public void doPreChunk(int par1, int par2, boolean par3) {
      if (par3) {
         this.clientChunkProvider.loadChunk(par1, par2);
      } else {
         this.clientChunkProvider.unloadChunk(par1, par2);
      }

      if (!par3) {
         this.g(par1 * 16, 0, par2 * 16, par1 * 16 + 15, 256, par2 * 16 + 15);
      }
   }

   @Override
   public boolean spawnEntityInWorld(Entity par1Entity) {
      boolean var2 = super.spawnEntityInWorld(par1Entity);
      this.entityList.add(par1Entity);
      if (!var2) {
         this.entitySpawnQueue.add(par1Entity);
      }

      return var2;
   }

   @Override
   public void removeEntity(Entity par1Entity) {
      super.removeEntity(par1Entity);
      this.entityList.remove(par1Entity);
   }

   @Override
   protected void obtainEntitySkin(Entity par1Entity) {
      super.obtainEntitySkin(par1Entity);
      if (this.entitySpawnQueue.contains(par1Entity)) {
         this.entitySpawnQueue.remove(par1Entity);
      }
   }

   @Override
   protected void releaseEntitySkin(Entity par1Entity) {
      super.releaseEntitySkin(par1Entity);
      if (this.entityList.contains(par1Entity)) {
         if (par1Entity.isEntityAlive()) {
            this.entitySpawnQueue.add(par1Entity);
         } else {
            this.entityList.remove(par1Entity);
         }
      }
   }

   public void addEntityToWorld(int par1, Entity par2Entity) {
      Entity var3 = this.getEntityByID(par1);
      if (var3 != null) {
         this.removeEntity(var3);
      }

      this.entityList.add(par2Entity);
      par2Entity.entityId = par1;
      if (!this.spawnEntityInWorld(par2Entity)) {
         this.entitySpawnQueue.add(par2Entity);
      }

      this.entityHashSet.addKey(par1, par2Entity);
   }

   @Override
   public Entity getEntityByID(int par1) {
      return (Entity)(par1 == this.mc.thePlayer.entityId ? this.mc.thePlayer : (Entity)this.entityHashSet.lookup(par1));
   }

   public Entity removeEntityFromWorld(int par1) {
      Entity var2 = (Entity)this.entityHashSet.removeObject(par1);
      if (var2 != null) {
         this.entityList.remove(var2);
         this.removeEntity(var2);
      }

      return var2;
   }

   public boolean setBlockAndMetadataAndInvalidate(int par1, int par2, int par3, int par4, int par5) {
      this.invalidateBlockReceiveRegion(par1, par2, par3, par1, par2, par3);
      int iOldBlockID = this.a(par1, par2, par3);
      if (iOldBlockID == par4) {
         Block block = Block.blocksList[iOldBlockID];
         if (block != null) {
            int iOldBlockMetadata = this.h(par1, par2, par3);
            block.clientNotificationOfMetadataChange(this, par1, par2, par3, iOldBlockMetadata, par5);
         }
      }

      return super.setBlock(par1, par2, par3, par4, par5, 3);
   }

   @Override
   public void sendQuittingDisconnectingPacket() {
      this.sendQueue.quitWithPacket(new Packet255KickDisconnect("Quitting"));
   }

   @Override
   public IUpdatePlayerListBox func_82735_a(EntityMinecart par1EntityMinecart) {
      return new SoundUpdaterMinecart(this.mc.sndManager, par1EntityMinecart, this.mc.thePlayer);
   }

   @Override
   protected void updateWeather() {
      if (!this.provider.hasNoSky) {
         this.prevRainingStrength = this.rainingStrength;
         if (this.worldInfo.isRaining()) {
            this.rainingStrength = (float)(this.rainingStrength + 0.01);
         } else {
            this.rainingStrength = (float)(this.rainingStrength - 0.01);
         }

         if (this.rainingStrength < 0.0F) {
            this.rainingStrength = 0.0F;
         }

         if (this.rainingStrength > 1.0F) {
            this.rainingStrength = 1.0F;
         }

         this.prevThunderingStrength = this.thunderingStrength;
         if (this.worldInfo.isThundering()) {
            this.thunderingStrength = (float)(this.thunderingStrength + 0.01);
         } else {
            this.thunderingStrength = (float)(this.thunderingStrength - 0.01);
         }

         if (this.thunderingStrength < 0.0F) {
            this.thunderingStrength = 0.0F;
         }

         if (this.thunderingStrength > 1.0F) {
            this.thunderingStrength = 1.0F;
         }
      }
   }

   public void doVoidFogParticles(int par1, int par2, int par3) {
      byte var4 = 16;
      Random var5 = new Random();

      for (int var6 = 0; var6 < 1000; var6++) {
         int var7 = par1 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
         int var8 = par2 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
         int var9 = par3 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
         int var10 = this.a(var7, var8, var9);
         if (var10 == 0 && this.rand.nextInt(8) > var8 && this.provider.getWorldHasVoidParticles()) {
            this.a("depthsuspend", var7 + this.rand.nextFloat(), var8 + this.rand.nextFloat(), var9 + this.rand.nextFloat(), 0.0, 0.0, 0.0);
         } else if (var10 > 0) {
            Block.blocksList[var10].randomDisplayTick(this, var7, var8, var9, var5);
         }
      }
   }

   public void removeAllEntities() {
      this.loadedEntityList.removeAll(this.unloadedEntityList);

      for (int var1 = 0; var1 < this.unloadedEntityList.size(); var1++) {
         Entity var2 = (Entity)this.unloadedEntityList.get(var1);
         int var3 = var2.chunkCoordX;
         int var4 = var2.chunkCoordZ;
         if (var2.addedToChunk && this.c(var3, var4)) {
            this.e(var3, var4).removeEntity(var2);
         }
      }

      for (int var5 = 0; var5 < this.unloadedEntityList.size(); var5++) {
         this.releaseEntitySkin((Entity)this.unloadedEntityList.get(var5));
      }

      this.unloadedEntityList.clear();

      for (int var6 = 0; var6 < this.loadedEntityList.size(); var6++) {
         Entity var2 = (Entity)this.loadedEntityList.get(var6);
         if (var2.ridingEntity != null) {
            if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
               continue;
            }

            var2.ridingEntity.riddenByEntity = null;
            var2.ridingEntity = null;
         }

         if (var2.isDead) {
            int var3 = var2.chunkCoordX;
            int var4 = var2.chunkCoordZ;
            if (var2.addedToChunk && this.c(var3, var4)) {
               this.e(var3, var4).removeEntity(var2);
            }

            this.loadedEntityList.remove(var6--);
            this.releaseEntitySkin(var2);
         }
      }
   }

   @Override
   public CrashReportCategory addWorldInfoToCrashReport(CrashReport par1CrashReport) {
      CrashReportCategory var2 = super.addWorldInfoToCrashReport(par1CrashReport);
      var2.addCrashSectionCallable("Forced entities", new CallableMPL1(this));
      var2.addCrashSectionCallable("Retry entities", new CallableMPL2(this));
      return var2;
   }

   @Override
   public void playSound(double par1, double par3, double par5, String par7Str, float par8, float par9, boolean par10) {
      float var11 = 16.0F;
      if (par8 > 1.0F) {
         var11 *= par8;
      }

      double var12 = this.mc.renderViewEntity.e(par1, par3, par5);
      if (var12 < var11 * var11) {
         if (par10 && var12 > 100.0) {
            double var14 = Math.sqrt(var12) / 40.0;
            this.mc.sndManager.func_92070_a(par7Str, (float)par1, (float)par3, (float)par5, par8, par9, (int)Math.round(var14 * 20.0));
         } else {
            this.mc.sndManager.playSound(par7Str, (float)par1, (float)par3, (float)par5, par8, par9);
         }
      }
   }

   @Override
   public void func_92088_a(double par1, double par3, double par5, double par7, double par9, double par11, NBTTagCompound par13NBTTagCompound) {
      if (par13NBTTagCompound != null) {
         this.mc
            .effectRenderer
            .addEffect(
               (EntityFX)EntityList.createEntityOfType(
                  EntityFireworkStarterFX.class, this, par1, par3, par5, par7, par9, par11, this.mc.effectRenderer, par13NBTTagCompound
               )
            );
      } else {
         this.mc
            .effectRenderer
            .addEffect(
               (EntityFX)EntityList.createEntityOfType(EntityFireworkStarterFX.class, this, par1, par3, par5, par7, par9, par11, this.mc.effectRenderer)
            );
      }
   }

   public void func_96443_a(Scoreboard par1Scoreboard) {
      this.worldScoreboard = par1Scoreboard;
   }

   static Set getEntityList(WorldClient par0WorldClient) {
      return par0WorldClient.entityList;
   }

   static Set getEntitySpawnQueue(WorldClient par0WorldClient) {
      return par0WorldClient.entitySpawnQueue;
   }

   @Override
   protected void updateActiveChunkMap() {
      this.clearActiveChunkMap();
      if (this.mc.thePlayer != null && this.mc.thePlayer.worldObj == this) {
         this.addEntityToActiveChunkMap(this.mc.thePlayer);
      }
   }

   @Override
   public void unloadEntities(List entityList) {
      this.loadedEntityList.removeAll(entityList);
      LinkedList<Entity> removeList = new LinkedList<>();
      removeList.addAll(entityList);

      for (Entity tempEntity : removeList) {
         int iChunkX = tempEntity.chunkCoordX;
         int iChunkZ = tempEntity.chunkCoordZ;
         if (tempEntity.addedToChunk && this.c(iChunkX, iChunkZ)) {
            this.e(iChunkX, iChunkZ).removeEntity(tempEntity);
         }

         this.releaseEntitySkin(tempEntity);
      }
   }

   @Override
   public void markTileEntityForDespawn(TileEntity tileEntity) {
      this.loadedTileEntityList.remove(tileEntity);
   }

   @Override
   public Difficulty getDifficulty() {
      return super.getDifficulty();
   }
}
