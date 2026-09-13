package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentStrongholdRoomCrossing extends ComponentStronghold {
   private static final WeightedRandomChestContent[] strongholdRoomCrossingChestContents = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10),
      new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5),
      new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5),
      new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10),
      new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15),
      new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1)
   };
   protected final EnumDoor doorType;
   protected final int roomType;

   public ComponentStrongholdRoomCrossing(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4) {
      super(par1);
      this.coordBaseMode = par4;
      this.doorType = this.a(par2Random);
      this.boundingBox = par3StructureBoundingBox;
      this.roomType = par2Random.nextInt(5);
   }

   @Override
   public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
      this.a((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 4, 1);
      this.b((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 1, 4);
      this.c((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 1, 4);
   }

   public static ComponentStrongholdRoomCrossing findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -4, -1, 0, 11, 7, 11, par5);
      return a(var7) && StructureComponent.findIntersecting(par0List, var7) == null ? new ComponentStrongholdRoomCrossing(par6, par1Random, var7, par5) : null;
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (this.a(par1World, par3StructureBoundingBox)) {
         return false;
      } else {
         this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 10, 6, 10, true, par2Random, StructureStrongholdPieces.getStrongholdStones());
         this.a(par1World, par2Random, par3StructureBoundingBox, this.doorType, 4, 1, 0);
         this.a(par1World, par3StructureBoundingBox, 4, 1, 10, 6, 3, 10, 0, 0, false);
         this.a(par1World, par3StructureBoundingBox, 0, 1, 4, 0, 3, 6, 0, 0, false);
         this.a(par1World, par3StructureBoundingBox, 10, 1, 4, 10, 3, 6, 0, 0, false);
         switch (this.roomType) {
            case 0:
               this.a(par1World, Block.stoneBrick.blockID, 0, 5, 1, 5, par3StructureBoundingBox);
               this.a(par1World, Block.stoneBrick.blockID, 0, 5, 2, 5, par3StructureBoundingBox);
               this.a(par1World, Block.stoneBrick.blockID, 0, 5, 3, 5, par3StructureBoundingBox);
               this.a(par1World, Block.torchWood.blockID, 0, 4, 3, 5, par3StructureBoundingBox);
               this.a(par1World, Block.torchWood.blockID, 0, 6, 3, 5, par3StructureBoundingBox);
               this.a(par1World, Block.torchWood.blockID, 0, 5, 3, 4, par3StructureBoundingBox);
               this.a(par1World, Block.torchWood.blockID, 0, 5, 3, 6, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 4, 1, 4, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 4, 1, 5, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 4, 1, 6, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 6, 1, 4, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 6, 1, 5, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 6, 1, 6, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 5, 1, 4, par3StructureBoundingBox);
               this.a(par1World, Block.stoneSingleSlab.blockID, 0, 5, 1, 6, par3StructureBoundingBox);
               break;
            case 1:
               for (int var4 = 0; var4 < 5; var4++) {
                  this.a(par1World, Block.stoneBrick.blockID, 0, 3, 1, 3 + var4, par3StructureBoundingBox);
                  this.a(par1World, Block.stoneBrick.blockID, 0, 7, 1, 3 + var4, par3StructureBoundingBox);
                  this.a(par1World, Block.stoneBrick.blockID, 0, 3 + var4, 1, 3, par3StructureBoundingBox);
                  this.a(par1World, Block.stoneBrick.blockID, 0, 3 + var4, 1, 7, par3StructureBoundingBox);
               }

               this.a(par1World, Block.stoneBrick.blockID, 0, 5, 1, 5, par3StructureBoundingBox);
               this.a(par1World, Block.stoneBrick.blockID, 0, 5, 2, 5, par3StructureBoundingBox);
               this.a(par1World, Block.stoneBrick.blockID, 0, 5, 3, 5, par3StructureBoundingBox);
               this.a(par1World, Block.waterMoving.blockID, 0, 5, 4, 5, par3StructureBoundingBox);
               break;
            case 2:
               for (int var4 = 1; var4 <= 9; var4++) {
                  this.a(par1World, Block.cobblestone.blockID, 0, 1, 3, var4, par3StructureBoundingBox);
                  this.a(par1World, Block.cobblestone.blockID, 0, 9, 3, var4, par3StructureBoundingBox);
               }

               for (int var7 = 1; var7 <= 9; var7++) {
                  this.a(par1World, Block.cobblestone.blockID, 0, var7, 3, 1, par3StructureBoundingBox);
                  this.a(par1World, Block.cobblestone.blockID, 0, var7, 3, 9, par3StructureBoundingBox);
               }

               this.a(par1World, Block.cobblestone.blockID, 0, 5, 1, 4, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 5, 1, 6, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 5, 3, 4, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 5, 3, 6, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 4, 1, 5, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 6, 1, 5, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 4, 3, 5, par3StructureBoundingBox);
               this.a(par1World, Block.cobblestone.blockID, 0, 6, 3, 5, par3StructureBoundingBox);

               for (int var8 = 1; var8 <= 3; var8++) {
                  this.a(par1World, Block.cobblestone.blockID, 0, 4, var8, 4, par3StructureBoundingBox);
                  this.a(par1World, Block.cobblestone.blockID, 0, 6, var8, 4, par3StructureBoundingBox);
                  this.a(par1World, Block.cobblestone.blockID, 0, 4, var8, 6, par3StructureBoundingBox);
                  this.a(par1World, Block.cobblestone.blockID, 0, 6, var8, 6, par3StructureBoundingBox);
               }

               this.a(par1World, Block.torchWood.blockID, 0, 5, 3, 5, par3StructureBoundingBox);

               for (int var9 = 2; var9 <= 8; var9++) {
                  this.a(par1World, Block.planks.blockID, 0, 2, 3, var9, par3StructureBoundingBox);
                  this.a(par1World, Block.planks.blockID, 0, 3, 3, var9, par3StructureBoundingBox);
                  if (var9 <= 3 || var9 >= 7) {
                     this.a(par1World, Block.planks.blockID, 0, 4, 3, var9, par3StructureBoundingBox);
                     this.a(par1World, Block.planks.blockID, 0, 5, 3, var9, par3StructureBoundingBox);
                     this.a(par1World, Block.planks.blockID, 0, 6, 3, var9, par3StructureBoundingBox);
                  }

                  this.a(par1World, Block.planks.blockID, 0, 7, 3, var9, par3StructureBoundingBox);
                  this.a(par1World, Block.planks.blockID, 0, 8, 3, var9, par3StructureBoundingBox);
               }

               int iFacing = this.c(Block.ladder.blockID, 4);
               int iMetadata = BTWBlocks.ladder.setFacing(0, iFacing);
               this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 9, 1, 3, par3StructureBoundingBox);
               this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 9, 2, 3, par3StructureBoundingBox);
               this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 9, 3, 3, par3StructureBoundingBox);
               this.a(
                  par1World,
                  par3StructureBoundingBox,
                  par2Random,
                  3,
                  4,
                  8,
                  WeightedRandomChestContent.func_92080_a(strongholdRoomCrossingChestContents, Item.enchantedBook.func_92114_b(par2Random)),
                  1 + par2Random.nextInt(4)
               );
         }

         return true;
      }
   }
}
