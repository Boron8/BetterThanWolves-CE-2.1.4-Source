package net.minecraft.src;

import btw.entity.mob.ZombieEntity;
import java.util.List;
import java.util.Random;

abstract class ComponentVillage extends StructureComponent {
   private int villagersSpawned;
   protected ComponentVillageStartPiece startPiece;

   protected ComponentVillage(ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2) {
      super(par2);
      this.startPiece = par1ComponentVillageStartPiece;
   }

   protected StructureComponent getNextComponentNN(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, List par2List, Random par3Random, int par4, int par5
   ) {
      switch (this.coordBaseMode) {
         case 0:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.minX - 1,
               this.boundingBox.minY + par4,
               this.boundingBox.minZ + par5,
               1,
               this.c()
            );
         case 1:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.minX + par5,
               this.boundingBox.minY + par4,
               this.boundingBox.minZ - 1,
               2,
               this.c()
            );
         case 2:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.minX - 1,
               this.boundingBox.minY + par4,
               this.boundingBox.minZ + par5,
               1,
               this.c()
            );
         case 3:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.minX + par5,
               this.boundingBox.minY + par4,
               this.boundingBox.minZ - 1,
               2,
               this.c()
            );
         default:
            return null;
      }
   }

   protected StructureComponent getNextComponentPP(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, List par2List, Random par3Random, int par4, int par5
   ) {
      switch (this.coordBaseMode) {
         case 0:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.maxX + 1,
               this.boundingBox.minY + par4,
               this.boundingBox.minZ + par5,
               3,
               this.c()
            );
         case 1:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.minX + par5,
               this.boundingBox.minY + par4,
               this.boundingBox.maxZ + 1,
               0,
               this.c()
            );
         case 2:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.maxX + 1,
               this.boundingBox.minY + par4,
               this.boundingBox.minZ + par5,
               3,
               this.c()
            );
         case 3:
            return StructureVillagePieces.getNextStructureComponent(
               par1ComponentVillageStartPiece,
               par2List,
               par3Random,
               this.boundingBox.minX + par5,
               this.boundingBox.minY + par4,
               this.boundingBox.maxZ + 1,
               0,
               this.c()
            );
         default:
            return null;
      }
   }

   protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox) {
      int var3 = 0;
      int var4 = 0;

      for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; var5++) {
         for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; var6++) {
            if (par2StructureBoundingBox.isVecInside(var6, 64, var5)) {
               var3 += Math.max(par1World.getTopSolidOrLiquidBlock(var6, var5), par1World.provider.getAverageGroundLevel());
               var4++;
            }
         }
      }

      return var4 == 0 ? -1 : var3 / var4;
   }

   protected static boolean canVillageGoDeeper(StructureBoundingBox par0StructureBoundingBox) {
      return par0StructureBoundingBox != null && par0StructureBoundingBox.minY > 10;
   }

   protected void spawnVillagers(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6) {
      int iAbandonmentLevel = this.startPiece.getAbandonmentLevel(par1World);
      boolean bDirtyPeasants = false;
      if (iAbandonmentLevel > 1) {
         if (par1World.rand.nextInt(20) != 0) {
            return;
         }

         if (par6 > 1) {
            par6 = 1;
         }

         bDirtyPeasants = true;
      }

      if (this.villagersSpawned < par6) {
         for (int var7 = this.villagersSpawned; var7 < par6; var7++) {
            int var8 = this.a(par3 + var7, par5);
            int var9 = this.a(par4);
            int var10 = this.b(par3 + var7, par5);
            if (!par2StructureBoundingBox.isVecInside(var8, var9, var10)) {
               break;
            }

            this.villagersSpawned++;
            int iVillagerType = this.getVillagerType(var7);
            if (iVillagerType == 0 || iAbandonmentLevel <= 0 || iAbandonmentLevel <= 1 && (iVillagerType == 3 || iVillagerType == 4)) {
               ZombieEntity zombieVillager = (ZombieEntity)EntityList.createEntityOfType(ZombieEntity.class, par1World);
               zombieVillager.villagerClass = iVillagerType;
               zombieVillager.setPersistent(true);
               zombieVillager.setVillager(true);
               zombieVillager.b(var8 + 0.5, var9, var10 + 0.5, 0.0F, 0.0F);
               par1World.spawnEntityInWorld(zombieVillager);
            }
         }
      }
   }

   protected int getVillagerType(int par1) {
      return 0;
   }

   protected int getBiomeSpecificBlock(int par1, int par2) {
      if (this.startPiece.inDesert) {
         if (par1 == Block.wood.blockID) {
            return Block.sandStone.blockID;
         }

         if (par1 == Block.cobblestone.blockID) {
            return Block.sandStone.blockID;
         }

         if (par1 == Block.planks.blockID) {
            return Block.sandStone.blockID;
         }

         if (par1 == Block.stairsWoodOak.blockID) {
            return Block.stairsSandStone.blockID;
         }

         if (par1 == Block.stairsCobblestone.blockID) {
            return Block.stairsSandStone.blockID;
         }

         if (par1 == Block.gravel.blockID) {
            return Block.sandStone.blockID;
         }
      }

      return par1;
   }

   protected int getBiomeSpecificBlockMetadata(int par1, int par2) {
      if (this.startPiece.inDesert) {
         if (par1 == Block.wood.blockID) {
            return 0;
         }

         if (par1 == Block.cobblestone.blockID) {
            return 0;
         }

         if (par1 == Block.planks.blockID) {
            return 2;
         }
      }

      return par2;
   }

   @Override
   protected void placeBlockAtCurrentPosition(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox) {
      int var8 = this.getBiomeSpecificBlock(par2, par3);
      int var9 = this.getBiomeSpecificBlockMetadata(par2, par3);
      super.placeBlockAtCurrentPosition(par1World, var8, var9, par4, par5, par6, par7StructureBoundingBox);
   }

   @Override
   protected void fillWithBlocks(
      World par1World,
      StructureBoundingBox par2StructureBoundingBox,
      int par3,
      int par4,
      int par5,
      int par6,
      int par7,
      int par8,
      int par9,
      int par10,
      boolean par11
   ) {
      int var12 = this.getBiomeSpecificBlock(par9, 0);
      int var13 = this.getBiomeSpecificBlockMetadata(par9, 0);
      int var14 = this.getBiomeSpecificBlock(par10, 0);
      int var15 = this.getBiomeSpecificBlockMetadata(par10, 0);
      super.fillWithMetadataBlocks(par1World, par2StructureBoundingBox, par3, par4, par5, par6, par7, par8, var12, var13, var14, var15, par11);
   }

   @Override
   protected void fillCurrentPositionBlocksDownwards(
      World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox
   ) {
      int var8 = this.getBiomeSpecificBlock(par2, par3);
      int var9 = this.getBiomeSpecificBlockMetadata(par2, par3);
      super.fillCurrentPositionBlocksDownwards(par1World, var8, var9, par4, par5, par6, par7StructureBoundingBox);
   }
}
