package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.block.tileentity.WickerBasketTileEntity;
import btw.entity.mob.WitchEntity;
import btw.item.BTWItems;
import btw.item.util.RandomItemStack;
import java.util.Random;

public class ComponentScatteredFeatureSwampHut extends ComponentScatteredFeature {
   private boolean hasWitch;
   private boolean hasLootBasket = false;
   private static RandomItemStack[] lootBasketContents = null;

   public ComponentScatteredFeatureSwampHut(Random par1Random, int par2, int par3) {
      super(par1Random, par2, 64, par3, 7, 5, 9);
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (!this.a(par1World, par3StructureBoundingBox, 0)) {
         return false;
      } else {
         this.a(par1World, par3StructureBoundingBox, 1, 1, 1, 5, 1, 7, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 1, 4, 2, 5, 4, 7, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 2, 1, 0, 4, 1, 0, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 2, 2, 2, 3, 3, 2, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 1, 2, 3, 1, 3, 6, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 5, 2, 3, 5, 3, 6, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 2, 2, 7, 4, 3, 7, Block.planks.blockID, 1, Block.planks.blockID, 1, false);
         this.a(par1World, par3StructureBoundingBox, 1, 0, 2, 1, 3, 2, Block.wood.blockID, Block.wood.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 5, 0, 2, 5, 3, 2, Block.wood.blockID, Block.wood.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 1, 0, 7, 1, 3, 7, Block.wood.blockID, Block.wood.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 5, 0, 7, 5, 3, 7, Block.wood.blockID, Block.wood.blockID, false);
         this.a(par1World, Block.fence.blockID, 0, 2, 3, 2, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 3, 3, 7, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 1, 3, 4, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 5, 3, 4, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 5, 3, 5, par3StructureBoundingBox);
         this.a(par1World, Block.flowerPot.blockID, 7, 1, 3, 5, par3StructureBoundingBox);
         this.a(par1World, Block.planks.blockID, 1, 2, 2, 6, par3StructureBoundingBox);
         this.a(par1World, Block.brewingStand.blockID, 0, 2, 3, 6, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 1, 2, 1, par3StructureBoundingBox);
         this.a(par1World, Block.fence.blockID, 0, 5, 2, 1, par3StructureBoundingBox);
         int var4 = this.c(Block.stairsWoodOak.blockID, 3);
         int var5 = this.c(Block.stairsWoodOak.blockID, 1);
         int var6 = this.c(Block.stairsWoodOak.blockID, 0);
         int var7 = this.c(Block.stairsWoodOak.blockID, 2);
         this.a(par1World, par3StructureBoundingBox, 0, 4, 1, 6, 4, 1, Block.stairsWoodSpruce.blockID, var4, Block.stairsWoodSpruce.blockID, var4, false);
         this.a(par1World, par3StructureBoundingBox, 0, 4, 2, 0, 4, 7, Block.stairsWoodSpruce.blockID, var6, Block.stairsWoodSpruce.blockID, var6, false);
         this.a(par1World, par3StructureBoundingBox, 6, 4, 2, 6, 4, 7, Block.stairsWoodSpruce.blockID, var5, Block.stairsWoodSpruce.blockID, var5, false);
         this.a(par1World, par3StructureBoundingBox, 0, 4, 8, 6, 4, 8, Block.stairsWoodSpruce.blockID, var7, Block.stairsWoodSpruce.blockID, var7, false);

         for (int var8 = 2; var8 <= 7; var8 += 5) {
            for (int var9 = 1; var9 <= 5; var9 += 4) {
               this.b(par1World, Block.wood.blockID, 0, var9, -1, var8, par3StructureBoundingBox);
            }
         }

         if (!this.hasLootBasket) {
            this.addLootBasket(par1World, par3StructureBoundingBox, 3, 2, 6);
         }

         if (!this.hasWitch) {
            int var12 = this.a(2, 5);
            int var9 = this.a(2);
            int var10 = this.b(2, 5);
            if (par3StructureBoundingBox.isVecInside(var12, var9, var10)) {
               this.hasWitch = true;
               WitchEntity var11 = (WitchEntity)EntityList.createEntityOfType(WitchEntity.class, par1World);
               var11.preInitCreature();
               var11.b(var12 + 0.5, var9, var10 + 0.5, 0.0F, 0.0F);
               var11.bJ();
               var11.setPersistent(true);
               par1World.spawnEntityInWorld(var11);
            }

            this.spawnAdditionalWitches(par1World);
         }

         return true;
      }
   }

   private void initContentsArray() {
      lootBasketContents = new RandomItemStack[]{
         new RandomItemStack(BTWItems.hempSeeds.itemID, 0, 1, 4, 5),
         new RandomItemStack(Item.glassBottle.itemID, 0, 2, 8, 10),
         new RandomItemStack(BTWItems.redMushroom.itemID, 0, 5, 16, 5)
      };
   }

   private void spawnAdditionalWitches(World world) {
      int iNumWitches = 2;
      if (!this.hasWitch) {
         iNumWitches++;
      }

      int iMinSpawnX = this.boundingBox.minX >> 4 << 4;
      int iMinSpawnZ = this.boundingBox.minZ >> 4 << 4;
      int iSpawnZoneWidth = 16;

      for (int iTempCount = 0; iTempCount < iNumWitches; iTempCount++) {
         for (int iTempTries = 0; iTempTries < 20; iTempTries++) {
            int x = iMinSpawnX + world.rand.nextInt(iSpawnZoneWidth);
            int z = iMinSpawnZ + world.rand.nextInt(iSpawnZoneWidth);
            int y = world.getTopSolidOrLiquidBlock(x, z);
            if (SpawnerAnimals.canEntitySpawnDuringWorldGen(WitchEntity.class, world, x, y, z)) {
               this.hasWitch = true;
               WitchEntity witch = (WitchEntity)EntityList.createEntityOfType(WitchEntity.class, world);
               witch.preInitCreature();
               witch.b(x + 0.5, y, z + 0.5, 0.0F, 0.0F);
               witch.bJ();
               witch.setPersistent(true);
               world.spawnEntityInWorld(witch);
               break;
            }
         }
      }
   }

   private void addLootBasket(World world, StructureBoundingBox boundingBox, int iRelX, int iRelY, int iRelZ) {
      if (lootBasketContents == null) {
         this.initContentsArray();
      }

      int i = this.a(iRelX, iRelZ);
      int j = this.a(iRelY);
      int k = this.b(iRelX, iRelZ);
      if (boundingBox.isVecInside(i, j, k) && world.getBlockId(i, j, k) != BTWBlocks.wickerBasket.blockID) {
         this.hasLootBasket = true;
         world.setBlock(i, j, k, BTWBlocks.wickerBasket.blockID, world.rand.nextInt(4) | 4, 2);
         WickerBasketTileEntity tileEntity = (WickerBasketTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity != null) {
            tileEntity.setStorageStack(RandomItemStack.getRandomStack(world.rand, lootBasketContents));
         }
      }
   }
}
