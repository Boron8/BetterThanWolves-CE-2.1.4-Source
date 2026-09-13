package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentMineshaftCorridor extends StructureComponent {
   private final boolean hasRails;
   private final boolean hasSpiders;
   private boolean spawnerPlaced;
   private int sectionCount;

   public ComponentMineshaftCorridor(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4) {
      super(par1);
      this.coordBaseMode = par4;
      this.boundingBox = par3StructureBoundingBox;
      this.hasRails = par2Random.nextInt(3) == 0;
      this.hasSpiders = !this.hasRails && par2Random.nextInt(23) == 0;
      if (this.coordBaseMode != 2 && this.coordBaseMode != 0) {
         this.sectionCount = par3StructureBoundingBox.getXSize() / 5;
      } else {
         this.sectionCount = par3StructureBoundingBox.getZSize() / 5;
      }
   }

   public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5) {
      StructureBoundingBox var6 = new StructureBoundingBox(par2, par3, par4, par2, par3 + 2, par4);

      int var7;
      for (var7 = par1Random.nextInt(3) + 2; var7 > 0; var7--) {
         int var8 = var7 * 5;
         switch (par5) {
            case 0:
               var6.maxX = par2 + 2;
               var6.maxZ = par4 + (var8 - 1);
               break;
            case 1:
               var6.minX = par2 - (var8 - 1);
               var6.maxZ = par4 + 2;
               break;
            case 2:
               var6.maxX = par2 + 2;
               var6.minZ = par4 - (var8 - 1);
               break;
            case 3:
               var6.maxX = par2 + (var8 - 1);
               var6.maxZ = par4 + 2;
         }

         if (StructureComponent.findIntersecting(par0List, var6) == null) {
            break;
         }
      }

      return var7 > 0 ? var6 : null;
   }

   @Override
   public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
      int var4 = this.c();
      int var5 = par3Random.nextInt(4);
      switch (this.coordBaseMode) {
         case 0:
            if (var5 <= 1) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.maxZ + 1,
                  this.coordBaseMode,
                  var4
               );
            } else if (var5 == 2) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX - 1,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.maxZ - 3,
                  1,
                  var4
               );
            } else {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.maxX + 1,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.maxZ - 3,
                  3,
                  var4
               );
            }
            break;
         case 1:
            if (var5 <= 1) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX - 1,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ,
                  this.coordBaseMode,
                  var4
               );
            } else if (var5 == 2) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ - 1,
                  2,
                  var4
               );
            } else {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.maxZ + 1,
                  0,
                  var4
               );
            }
            break;
         case 2:
            if (var5 <= 1) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ - 1,
                  this.coordBaseMode,
                  var4
               );
            } else if (var5 == 2) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.minX - 1,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ,
                  1,
                  var4
               );
            } else {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.maxX + 1,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ,
                  3,
                  var4
               );
            }
            break;
         case 3:
            if (var5 <= 1) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.maxX + 1,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ,
                  this.coordBaseMode,
                  var4
               );
            } else if (var5 == 2) {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.maxX - 3,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.minZ - 1,
                  2,
                  var4
               );
            } else {
               StructureMineshaftPieces.getNextComponent(
                  par1StructureComponent,
                  par2List,
                  par3Random,
                  this.boundingBox.maxX - 3,
                  this.boundingBox.minY - 1 + par3Random.nextInt(3),
                  this.boundingBox.maxZ + 1,
                  0,
                  var4
               );
            }
      }

      if (var4 < 8) {
         if (this.coordBaseMode != 2 && this.coordBaseMode != 0) {
            for (int var6 = this.boundingBox.minX + 3; var6 + 3 <= this.boundingBox.maxX; var6 += 5) {
               int var7 = par3Random.nextInt(5);
               if (var7 == 0) {
                  StructureMineshaftPieces.getNextComponent(
                     par1StructureComponent, par2List, par3Random, var6, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4 + 1
                  );
               } else if (var7 == 1) {
                  StructureMineshaftPieces.getNextComponent(
                     par1StructureComponent, par2List, par3Random, var6, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4 + 1
                  );
               }
            }
         } else {
            for (int var6x = this.boundingBox.minZ + 3; var6x + 3 <= this.boundingBox.maxZ; var6x += 5) {
               int var7 = par3Random.nextInt(5);
               if (var7 == 0) {
                  StructureMineshaftPieces.getNextComponent(
                     par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, var6x, 1, var4 + 1
                  );
               } else if (var7 == 1) {
                  StructureMineshaftPieces.getNextComponent(
                     par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, var6x, 3, var4 + 1
                  );
               }
            }
         }
      }
   }

   @Override
   protected boolean generateStructureChestContents(
      World par1World,
      StructureBoundingBox par2StructureBoundingBox,
      Random par3Random,
      int par4,
      int par5,
      int par6,
      WeightedRandomChestContent[] par7ArrayOfWeightedRandomChestContent,
      int par8
   ) {
      int var9 = this.a(par4, par6);
      int var10 = this.a(par5);
      int var11 = this.b(par4, par6);
      if (par2StructureBoundingBox.isVecInside(var9, var10, var11) && par1World.getBlockId(var9, var10, var11) == 0) {
         par1World.setBlock(var9, var10, var11, Block.rail.blockID, this.getMetadataWithOffset(Block.rail.blockID, par3Random.nextBoolean() ? 1 : 0), 2);
         EntityMinecartChest var12 = (EntityMinecartChest)EntityList.createEntityOfType(
            EntityMinecartChest.class, par1World, (double)(var9 + 0.5F), (double)(var10 + 0.5F), (double)(var11 + 0.5F)
         );
         WeightedRandomChestContent.generateChestContents(par3Random, par7ArrayOfWeightedRandomChestContent, var12, par8);
         this.filterChestMinecartContents(var12);
         par1World.spawnEntityInWorld(var12);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (this.a(par1World, par3StructureBoundingBox)) {
         return false;
      } else {
         int var8 = this.sectionCount * 5 - 1;
         this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 1, var8, 0, 0, false);
         this.a(par1World, par3StructureBoundingBox, par2Random, 0.8F, 0, 2, 0, 2, 2, var8, 0, 0, false);
         if (this.hasSpiders) {
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.6F, 0, 0, 0, 2, 1, var8, BTWBlocks.web.blockID, 0, false);
         }

         for (int var9 = 0; var9 < this.sectionCount; var9++) {
            int var10 = 2 + var9 * 5;
            this.a(par1World, par3StructureBoundingBox, 0, 0, var10, 0, 1, var10, Block.wood.blockID, 0, 0, 0, false);
            this.a(par1World, par3StructureBoundingBox, 2, 0, var10, 2, 1, var10, Block.wood.blockID, 0, 0, 0, false);
            int iHorizontalLogMetadata = this.getMetadataWithOffset(Block.wood.blockID, 4);
            if (par2Random.nextInt(4) == 0) {
               this.a(par1World, par3StructureBoundingBox, 0, 2, var10, 0, 2, var10, Block.wood.blockID, iHorizontalLogMetadata, 0, 0, false);
               this.a(par1World, par3StructureBoundingBox, 2, 2, var10, 2, 2, var10, Block.wood.blockID, iHorizontalLogMetadata, 0, 0, false);
            } else {
               this.a(par1World, par3StructureBoundingBox, 0, 2, var10, 2, 2, var10, Block.wood.blockID, iHorizontalLogMetadata, 0, 0, false);
            }

            this.a(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, var10 - 1, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, var10 - 1, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, var10 + 1, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, var10 + 1, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, var10 - 2, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, var10 - 2, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, var10 + 2, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, var10 + 2, BTWBlocks.web.blockID, 0);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, var10 - 1, BTWBlocks.finiteUnlitTorch.blockID, 8);
            this.a(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, var10 + 1, BTWBlocks.finiteUnlitTorch.blockID, 8);
            if (par2Random.nextInt(100) == 0) {
               this.generateStructureChestContents(
                  par1World,
                  par3StructureBoundingBox,
                  par2Random,
                  2,
                  0,
                  var10 - 1,
                  WeightedRandomChestContent.func_92080_a(StructureMineshaftPieces.func_78816_a(), Item.enchantedBook.func_92114_b(par2Random)),
                  3 + par2Random.nextInt(4)
               );
            }

            if (par2Random.nextInt(100) == 0) {
               this.generateStructureChestContents(
                  par1World,
                  par3StructureBoundingBox,
                  par2Random,
                  0,
                  0,
                  var10 + 1,
                  WeightedRandomChestContent.func_92080_a(StructureMineshaftPieces.func_78816_a(), Item.enchantedBook.func_92114_b(par2Random)),
                  3 + par2Random.nextInt(4)
               );
            }

            if (this.hasSpiders && !this.spawnerPlaced) {
               int var11 = this.a(0);
               int var12 = var10 - 1 + par2Random.nextInt(3);
               int var13 = this.a(1, var12);
               var12 = this.b(1, var12);
               if (par3StructureBoundingBox.isVecInside(var13, var11, var12)) {
                  this.spawnerPlaced = true;
                  par1World.setBlock(var13, var11, var12, Block.mobSpawner.blockID, 0, 2);
                  TileEntityMobSpawner var14 = (TileEntityMobSpawner)par1World.getBlockTileEntity(var13, var11, var12);
                  if (var14 != null) {
                     var14.func_98049_a().setMobID("CaveSpider");
                  }
               }
            }
         }

         for (int var121 = 0; var121 <= 2; var121++) {
            for (int var10x = 0; var10x <= var8; var10x++) {
               int var11 = this.a(par1World, var121, -1, var10x, par3StructureBoundingBox);
               if (var11 == 0) {
                  this.a(par1World, Block.planks.blockID, 0, var121, -1, var10x, par3StructureBoundingBox);
               }
            }
         }

         if (this.hasRails) {
            for (int var131 = 0; var131 <= var8; var131++) {
               int var10xx = this.a(par1World, 1, -1, var131, par3StructureBoundingBox);
               if (var10xx > 0 && Block.opaqueCubeLookup[var10xx]) {
                  this.a(
                     par1World, par3StructureBoundingBox, par2Random, 0.7F, 1, 0, var131, Block.rail.blockID, this.getMetadataWithOffset(Block.rail.blockID, 0)
                  );
               }
            }
         }

         return true;
      }
   }

   private void filterChestMinecartContents(EntityMinecartChest minecart) {
      for (int iSlot = 0; iSlot < minecart.getSizeInventory(); iSlot++) {
         ItemStack tempStack = minecart.a(iSlot);
         if (tempStack != null) {
            int iItemID = tempStack.itemID;
            if (iItemID == Item.ingotIron.itemID) {
               if (minecart.posY > 36.0) {
                  minecart.a(iSlot, null);
               } else {
                  tempStack.stackSize = 1;
               }
            } else if (iItemID == Item.diamond.itemID) {
               if (minecart.posY > 24.0) {
                  minecart.a(iSlot, null);
               } else {
                  tempStack.stackSize = 1;
               }
            } else if (iItemID == Item.pickaxeIron.itemID) {
               minecart.a(iSlot, null);
            } else if (iItemID == Item.redstone.itemID) {
               if (minecart.posY > 24.0) {
                  minecart.a(iSlot, null);
               }
            } else if (iItemID == Item.pumpkinSeeds.itemID) {
               minecart.a(iSlot, null);
            }
         }
      }
   }

   @Override
   protected int getMetadataWithOffset(int iBlockID, int iMetadata) {
      if (iBlockID != Block.wood.blockID) {
         return super.getMetadataWithOffset(iBlockID, iMetadata);
      } else {
         if (this.coordBaseMode == 1 || this.coordBaseMode == 3) {
            int iLogType = iMetadata & 3;
            int iDirection = iMetadata & 12;
            if (iDirection == 4) {
               iDirection = 8;
            } else if (iDirection == 8) {
               iDirection = 4;
            }

            iMetadata = iLogType | iDirection;
         }

         return iMetadata;
      }
   }
}
