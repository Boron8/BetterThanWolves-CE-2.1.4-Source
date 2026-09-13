package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;

public class ComponentStrongholdLibrary extends ComponentStronghold {
   private static final WeightedRandomChestContent[] strongholdLibraryChestContents = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.book.itemID, 0, 1, 3, 20),
      new WeightedRandomChestContent(Item.paper.itemID, 0, 2, 7, 20),
      new WeightedRandomChestContent(Item.emptyMap.itemID, 0, 1, 1, 1),
      new WeightedRandomChestContent(Item.compass.itemID, 0, 1, 1, 1)
   };
   protected final EnumDoor doorType;
   private final boolean isLargeRoom;

   public ComponentStrongholdLibrary(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4) {
      super(par1);
      this.coordBaseMode = par4;
      this.doorType = this.a(par2Random);
      this.boundingBox = par3StructureBoundingBox;
      this.isLargeRoom = par3StructureBoundingBox.getYSize() > 6;
   }

   public static ComponentStrongholdLibrary findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -4, -1, 0, 14, 11, 15, par5);
      if (!a(var7) || StructureComponent.findIntersecting(par0List, var7) != null) {
         var7 = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -4, -1, 0, 14, 6, 15, par5);
         if (!a(var7) || StructureComponent.findIntersecting(par0List, var7) != null) {
            return null;
         }
      }

      return new ComponentStrongholdLibrary(par6, par1Random, var7, par5);
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if (this.a(par1World, par3StructureBoundingBox)) {
         return false;
      } else {
         byte var4 = 11;
         if (!this.isLargeRoom) {
            var4 = 6;
         }

         this.a(par1World, par3StructureBoundingBox, 0, 0, 0, 13, var4 - 1, 14, true, par2Random, StructureStrongholdPieces.getStrongholdStones());
         this.a(par1World, par2Random, par3StructureBoundingBox, this.doorType, 4, 1, 0);
         this.a(par1World, par3StructureBoundingBox, par2Random, 0.07F, 2, 1, 1, 11, 4, 13, Block.web.blockID, Block.web.blockID, false);

         for (int var7 = 1; var7 <= 13; var7++) {
            if ((var7 - 1) % 4 == 0) {
               this.a(par1World, par3StructureBoundingBox, 1, 1, var7, 1, 4, var7, Block.planks.blockID, Block.planks.blockID, false);
               this.a(par1World, par3StructureBoundingBox, 12, 1, var7, 12, 4, var7, Block.planks.blockID, Block.planks.blockID, false);
               this.a(par1World, Block.torchWood.blockID, 0, 2, 3, var7, par3StructureBoundingBox);
               this.a(par1World, Block.torchWood.blockID, 0, 11, 3, var7, par3StructureBoundingBox);
               if (this.isLargeRoom) {
                  this.a(par1World, par3StructureBoundingBox, 1, 6, var7, 1, 9, var7, Block.planks.blockID, Block.planks.blockID, false);
                  this.a(par1World, par3StructureBoundingBox, 12, 6, var7, 12, 9, var7, Block.planks.blockID, Block.planks.blockID, false);
               }
            } else {
               this.a(par1World, par3StructureBoundingBox, 1, 1, var7, 1, 4, var7, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
               this.a(par1World, par3StructureBoundingBox, 12, 1, var7, 12, 4, var7, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
               if (this.isLargeRoom) {
                  this.a(par1World, par3StructureBoundingBox, 1, 6, var7, 1, 9, var7, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
                  this.a(par1World, par3StructureBoundingBox, 12, 6, var7, 12, 9, var7, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
               }
            }
         }

         for (int var10 = 3; var10 < 12; var10 += 2) {
            this.a(par1World, par3StructureBoundingBox, 3, 1, var10, 4, 3, var10, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 6, 1, var10, 7, 3, var10, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 9, 1, var10, 10, 3, var10, Block.bookShelf.blockID, Block.bookShelf.blockID, false);
         }

         if (this.isLargeRoom) {
            this.a(par1World, par3StructureBoundingBox, 1, 5, 1, 3, 5, 13, Block.planks.blockID, Block.planks.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 10, 5, 1, 12, 5, 13, Block.planks.blockID, Block.planks.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 4, 5, 1, 9, 5, 2, Block.planks.blockID, Block.planks.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 4, 5, 12, 9, 5, 13, Block.planks.blockID, Block.planks.blockID, false);
            this.a(par1World, Block.planks.blockID, 0, 9, 5, 11, par3StructureBoundingBox);
            this.a(par1World, Block.planks.blockID, 0, 8, 5, 11, par3StructureBoundingBox);
            this.a(par1World, Block.planks.blockID, 0, 9, 5, 10, par3StructureBoundingBox);
            this.a(par1World, par3StructureBoundingBox, 3, 6, 2, 3, 6, 12, Block.fence.blockID, Block.fence.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 10, 6, 2, 10, 6, 10, Block.fence.blockID, Block.fence.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 4, 6, 2, 9, 6, 2, Block.fence.blockID, Block.fence.blockID, false);
            this.a(par1World, par3StructureBoundingBox, 4, 6, 12, 8, 6, 12, Block.fence.blockID, Block.fence.blockID, false);
            this.a(par1World, Block.fence.blockID, 0, 9, 6, 11, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, 8, 6, 11, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, 9, 6, 10, par3StructureBoundingBox);
            int iFacing = this.c(Block.ladder.blockID, 3);
            int iMetadata = BTWBlocks.ladder.setFacing(0, iFacing);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 1, 13, par3StructureBoundingBox);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 2, 13, par3StructureBoundingBox);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 3, 13, par3StructureBoundingBox);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 4, 13, par3StructureBoundingBox);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 5, 13, par3StructureBoundingBox);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 6, 13, par3StructureBoundingBox);
            this.a(par1World, BTWBlocks.ladder.blockID, iMetadata, 10, 7, 13, par3StructureBoundingBox);
            byte var8 = 7;
            byte var9 = 7;
            this.a(par1World, Block.fence.blockID, 0, var8 - 1, 9, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8, 9, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8 - 1, 8, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8, 8, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8 - 1, 7, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8, 7, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8 - 2, 7, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8 + 1, 7, var9, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8 - 1, 7, var9 - 1, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8 - 1, 7, var9 + 1, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8, 7, var9 - 1, par3StructureBoundingBox);
            this.a(par1World, Block.fence.blockID, 0, var8, 7, var9 + 1, par3StructureBoundingBox);
            this.a(par1World, Block.torchWood.blockID, 0, var8 - 2, 8, var9, par3StructureBoundingBox);
            this.a(par1World, Block.torchWood.blockID, 0, var8 + 1, 8, var9, par3StructureBoundingBox);
            this.a(par1World, Block.torchWood.blockID, 0, var8 - 1, 8, var9 - 1, par3StructureBoundingBox);
            this.a(par1World, Block.torchWood.blockID, 0, var8 - 1, 8, var9 + 1, par3StructureBoundingBox);
            this.a(par1World, Block.torchWood.blockID, 0, var8, 8, var9 - 1, par3StructureBoundingBox);
            this.a(par1World, Block.torchWood.blockID, 0, var8, 8, var9 + 1, par3StructureBoundingBox);
         }

         this.a(
            par1World,
            par3StructureBoundingBox,
            par2Random,
            3,
            3,
            5,
            WeightedRandomChestContent.func_92080_a(strongholdLibraryChestContents, Item.enchantedBook.func_92112_a(par2Random, 1, 5, 2)),
            1 + par2Random.nextInt(4)
         );
         if (this.isLargeRoom) {
            this.a(par1World, 0, 0, 12, 9, 1, par3StructureBoundingBox);
            this.a(
               par1World,
               par3StructureBoundingBox,
               par2Random,
               12,
               8,
               1,
               WeightedRandomChestContent.func_92080_a(strongholdLibraryChestContents, Item.enchantedBook.func_92112_a(par2Random, 1, 5, 2)),
               1 + par2Random.nextInt(4)
            );
         }

         return true;
      }
   }
}
