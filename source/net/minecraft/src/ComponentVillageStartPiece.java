package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.util.hardcorespawn.HardcoreSpawnUtils;
import java.util.ArrayList;
import java.util.Random;

public class ComponentVillageStartPiece extends ComponentVillageWell {
   public final WorldChunkManager worldChunkMngr;
   public final boolean inDesert;
   private static ArrayList<BiomeGenBase> desertBiomes = new ArrayList<>();
   public final int terrainType;
   public StructureVillagePieceWeight structVillagePieceWeight;
   public ArrayList structureVillageWeightedPieceList;
   public ArrayList field_74932_i = new ArrayList();
   public ArrayList field_74930_j = new ArrayList();
   private int abandonmentLevel;
   private int primaryCropBlockID;
   private int secondaryCropBlockID;
   private boolean modSpecificDataInitialized = false;

   public ComponentVillageStartPiece(
      WorldChunkManager par1WorldChunkManager, int par2, Random par3Random, int par4, int par5, ArrayList par6ArrayList, int par7
   ) {
      super((ComponentVillageStartPiece)null, 0, par3Random, par4, par5);
      this.worldChunkMngr = par1WorldChunkManager;
      this.structureVillageWeightedPieceList = par6ArrayList;
      this.terrainType = par7;
      BiomeGenBase var8 = par1WorldChunkManager.getBiomeGenAt(par4, par5);
      this.inDesert = isDesertBiome(var8);
      this.startPiece = this;
   }

   public WorldChunkManager getWorldChunkManager() {
      return this.worldChunkMngr;
   }

   public static void addDesertBiome(BiomeGenBase biome) {
      desertBiomes.add(biome);
   }

   public static boolean isDesertBiome(BiomeGenBase biome) {
      return desertBiomes.contains(biome);
   }

   public int getAbandonmentLevel(World world) {
      this.checkIfModSpecificDataRequiresInit(world);
      return this.abandonmentLevel;
   }

   public int getPrimaryCropBlockID(World world) {
      this.checkIfModSpecificDataRequiresInit(world);
      return this.primaryCropBlockID;
   }

   public int getSecondaryCropBlockID(World world) {
      this.checkIfModSpecificDataRequiresInit(world);
      return this.secondaryCropBlockID;
   }

   private void checkIfModSpecificDataRequiresInit(World world) {
      if (!this.modSpecificDataInitialized) {
         this.initializeModSpecificData(world);
      }
   }

   private void initializeModSpecificData(World world) {
      this.modSpecificDataInitialized = true;
      this.abandonmentLevel = 0;
      int iSpawnX = world.getWorldInfo().getSpawnX();
      int iSpawnZ = world.getWorldInfo().getSpawnZ();
      int iVillageX = this.boundingBox.getCenterX();
      int iVillageZ = this.boundingBox.getCenterZ();
      double dDeltaX = iSpawnX - iVillageX;
      double dDeltaZ = iSpawnZ - iVillageZ;
      double dDistSqFromSpawn = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
      double dAbandonedRadius = HardcoreSpawnUtils.getAbandonedVillageRadius(world);
      if (dDistSqFromSpawn < dAbandonedRadius * dAbandonedRadius) {
         this.abandonmentLevel = 2;
      } else {
         double dPartiallyAbandonedRadius = HardcoreSpawnUtils.getPartiallyAbandonedVillageRadius(world);
         if (dDistSqFromSpawn < dPartiallyAbandonedRadius * dPartiallyAbandonedRadius) {
            this.abandonmentLevel = 1;
            this.primaryCropBlockID = BTWBlocks.wheatCrop.blockID;
            this.secondaryCropBlockID = BTWBlocks.wheatCrop.blockID;
         } else {
            this.primaryCropBlockID = BTWBlocks.wheatCrop.blockID;
            int iRandomFactor = world.rand.nextInt(4);
            if (iRandomFactor == 3) {
               this.secondaryCropBlockID = Block.potato.blockID;
            } else if (iRandomFactor == 2) {
               this.secondaryCropBlockID = BTWBlocks.carrotCrop.blockID;
            } else {
               this.secondaryCropBlockID = BTWBlocks.wheatCrop.blockID;
            }
         }
      }
   }
}
