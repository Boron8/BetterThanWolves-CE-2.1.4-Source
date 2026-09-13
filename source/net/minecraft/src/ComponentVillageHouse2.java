package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.inventory.util.InventoryUtils;
import java.util.List;
import java.util.Random;

public class ComponentVillageHouse2 extends ComponentVillage {
   private static final WeightedRandomChestContent[] villageBlacksmithChestContents = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3),
      new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
      new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5),
      new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.swordIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.plateIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.helmetIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.legsIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.bootsIron.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Block.obsidian.blockID, 0, 3, 7, 5),
      new WeightedRandomChestContent(BTWBlocks.oakSapling.blockID, 0, 3, 7, 5)
   };
   private int averageGroundLevel = -1;
   private boolean hasMadeChest;

   public ComponentVillageHouse2(
      ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5
   ) {
      super(par1ComponentVillageStartPiece, par2);
      this.coordBaseMode = par5;
      this.boundingBox = par4StructureBoundingBox;
   }

   public static ComponentVillageHouse2 func_74915_a(
      ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7
   ) {
      StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 10, 6, 7, par6);
      return a(var8) && StructureComponent.findIntersecting(par1List, var8) == null
         ? new ComponentVillageHouse2(par0ComponentVillageStartPiece, par7, par2Random, var8, par6)
         : null;
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (this.averageGroundLevel < 0) {
         this.averageGroundLevel = this.b(par1World, par3StructureBoundingBox);
         if (this.averageGroundLevel < 0) {
            return true;
         }

         this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 6 - 1, 0);
      }

      this.a(par1World, par3StructureBoundingBox, 0, 1, 0, 9, 4, 6, 0, 0, false);
      this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 9, 0, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 4, 0, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 5, 0, 9, 5, 6, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 5, 1, 8, 5, 5, 0, 0, false);
      this.a(par1World, par3StructureBoundingBox, 1, 1, 0, 2, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 1, 0, 0, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 3, 1, 0, 3, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 1, 6, 0, 4, 6, Block.wood.blockID, Block.wood.blockID, false);
      this.a(par1World, Block.planks.blockID, 0, 3, 3, 1, par3StructureBoundingBox);
      this.a(par1World, par3StructureBoundingBox, 3, 1, 2, 3, 3, 2, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 4, 1, 3, 5, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 1, 1, 6, 5, 3, 6, Block.planks.blockID, Block.planks.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 5, 1, 0, 5, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 9, 1, 0, 9, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 6, 1, 4, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
      this.a(par1World, Block.waterMoving.blockID, 0, 7, 1, 5, par3StructureBoundingBox);
      this.a(par1World, Block.waterMoving.blockID, 0, 8, 1, 5, par3StructureBoundingBox);
      this.a(par1World, Block.fenceIron.blockID, 0, 9, 2, 5, par3StructureBoundingBox);
      this.a(par1World, Block.fenceIron.blockID, 0, 9, 2, 4, par3StructureBoundingBox);
      this.a(par1World, par3StructureBoundingBox, 7, 2, 4, 8, 2, 5, 0, 0, false);
      this.a(par1World, Block.cobblestone.blockID, 0, 6, 1, 3, par3StructureBoundingBox);
      int iAbandonmentLevel = this.startPiece.getAbandonmentLevel(par1World);
      if (iAbandonmentLevel == 0) {
         this.a(par1World, BTWBlocks.idleOven.blockID, this.c(BTWBlocks.idleOven.blockID, 5), 6, 2, 3, par3StructureBoundingBox);
         this.a(par1World, BTWBlocks.idleOven.blockID, this.c(BTWBlocks.idleOven.blockID, 5), 6, 3, 3, par3StructureBoundingBox);
      }

      if (iAbandonmentLevel == 0) {
         this.a(par1World, Block.anvil.blockID, par1World.rand.nextInt(4), 8, 1, 1, par3StructureBoundingBox);
      }

      if (iAbandonmentLevel == 0) {
         this.a(par1World, Block.thinGlass.blockID, 0, 0, 2, 2, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 2, 2, 6, par3StructureBoundingBox);
         this.a(par1World, Block.thinGlass.blockID, 0, 4, 2, 6, par3StructureBoundingBox);
      } else {
         this.a(par1World, 0, 0, 0, 2, 2, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 0, 2, 4, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 2, 2, 6, par3StructureBoundingBox);
         this.a(par1World, 0, 0, 4, 2, 6, par3StructureBoundingBox);
      }

      this.a(par1World, BTWBlocks.oakWoodMouldingAndDecorative.blockID, 15, 2, 1, 4, par3StructureBoundingBox);
      this.a(par1World, Block.planks.blockID, 0, 1, 1, 5, par3StructureBoundingBox);
      this.a(par1World, Block.stairsWoodOak.blockID, this.c(Block.stairsWoodOak.blockID, 3), 2, 1, 5, par3StructureBoundingBox);
      this.a(par1World, Block.stairsWoodOak.blockID, this.c(Block.stairsWoodOak.blockID, 1), 1, 1, 4, par3StructureBoundingBox);
      if (!this.hasMadeChest) {
         int var4 = this.a(1);
         int var5 = this.a(5, 5);
         int var6 = this.b(5, 5);
         if (par3StructureBoundingBox.isVecInside(var5, var4, var6)) {
            this.hasMadeChest = true;
            this.a(par1World, par3StructureBoundingBox, par2Random, 5, 1, 5, villageBlacksmithChestContents, 3 + par2Random.nextInt(6));
            if (this.startPiece.getAbandonmentLevel(par1World) > 0) {
               int iChestI = this.a(5, 5);
               int iChestJ = this.a(1);
               int iChestK = this.b(5, 5);
               TileEntityChest chestEnt = (TileEntityChest)par1World.getBlockTileEntity(iChestI, iChestJ, iChestK);
               if (chestEnt != null) {
                  InventoryUtils.clearInventoryContents(chestEnt);
               }

               this.a(par1World, 0, 0, 5, 1, 5, par3StructureBoundingBox);
            }
         }
      }

      for (int var4 = 6; var4 <= 8; var4++) {
         if (this.a(par1World, var4, 0, -1, par3StructureBoundingBox) == 0 && this.a(par1World, var4, -1, -1, par3StructureBoundingBox) != 0) {
            this.a(par1World, Block.stairsCobblestone.blockID, this.c(Block.stairsCobblestone.blockID, 3), var4, 0, -1, par3StructureBoundingBox);
         }
      }

      for (int var13 = 0; var13 < 7; var13++) {
         for (int var5 = 0; var5 < 10; var5++) {
            this.b(par1World, var5, 6, var13, par3StructureBoundingBox);
            this.b(par1World, Block.cobblestone.blockID, 0, var5, -1, var13, par3StructureBoundingBox);
         }
      }

      this.a(par1World, par3StructureBoundingBox, 7, 1, 1, 1);
      return true;
   }

   @Override
   protected int getVillagerType(int par1) {
      return 3;
   }
}
