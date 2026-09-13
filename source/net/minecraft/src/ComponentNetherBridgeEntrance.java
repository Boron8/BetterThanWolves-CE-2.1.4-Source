package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.world.util.WorldUtils;
import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeEntrance extends ComponentNetherBridgePiece {
   public ComponentNetherBridgeEntrance(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4) {
      super(par1);
      this.coordBaseMode = par4;
      this.boundingBox = par3StructureBoundingBox;
   }

   @Override
   public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
      this.a((ComponentNetherBridgeStartPiece)par1StructureComponent, par2List, par3Random, 5, 3, true);
   }

   public static ComponentNetherBridgeEntrance createValidComponent(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -5, -3, 0, 13, 14, 13, par5);
      return a(var7) && StructureComponent.findIntersecting(par0List, var7) == null ? new ComponentNetherBridgeEntrance(par6, par1Random, var7, par5) : null;
   }

   @Override
   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      this.a(par1World, par3StructureBoundingBox, 0, 3, 0, 12, 4, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 5, 0, 12, 13, 12, 0, 0, false);
      this.a(par1World, par3StructureBoundingBox, 0, 5, 0, 1, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 11, 5, 0, 12, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 2, 5, 11, 4, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 8, 5, 11, 10, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 5, 9, 11, 7, 12, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 2, 5, 0, 4, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 8, 5, 0, 10, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 5, 9, 0, 7, 12, 1, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 2, 11, 2, 10, 12, 10, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 5, 8, 0, 7, 8, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);

      for (int var4 = 1; var4 <= 11; var4 += 2) {
         this.a(par1World, par3StructureBoundingBox, var4, 10, 0, var4, 11, 0, Block.netherFence.blockID, Block.netherFence.blockID, false);
         this.a(par1World, par3StructureBoundingBox, var4, 10, 12, var4, 11, 12, Block.netherFence.blockID, Block.netherFence.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 0, 10, var4, 0, 11, var4, Block.netherFence.blockID, Block.netherFence.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 12, 10, var4, 12, 11, var4, Block.netherFence.blockID, Block.netherFence.blockID, false);
         this.a(par1World, Block.netherBrick.blockID, 0, var4, 13, 0, par3StructureBoundingBox);
         this.a(par1World, Block.netherBrick.blockID, 0, var4, 13, 12, par3StructureBoundingBox);
         this.a(par1World, Block.netherBrick.blockID, 0, 0, 13, var4, par3StructureBoundingBox);
         this.a(par1World, Block.netherBrick.blockID, 0, 12, 13, var4, par3StructureBoundingBox);
         this.a(par1World, Block.netherFence.blockID, 0, var4 + 1, 13, 0, par3StructureBoundingBox);
         this.a(par1World, Block.netherFence.blockID, 0, var4 + 1, 13, 12, par3StructureBoundingBox);
         this.a(par1World, Block.netherFence.blockID, 0, 0, 13, var4 + 1, par3StructureBoundingBox);
         this.a(par1World, Block.netherFence.blockID, 0, 12, 13, var4 + 1, par3StructureBoundingBox);
      }

      this.a(par1World, Block.netherFence.blockID, 0, 0, 13, 0, par3StructureBoundingBox);
      this.a(par1World, Block.netherFence.blockID, 0, 0, 13, 12, par3StructureBoundingBox);
      this.a(par1World, Block.netherFence.blockID, 0, 0, 13, 0, par3StructureBoundingBox);
      this.a(par1World, Block.netherFence.blockID, 0, 12, 13, 0, par3StructureBoundingBox);

      for (int var7 = 3; var7 <= 9; var7 += 2) {
         this.a(par1World, par3StructureBoundingBox, 1, 7, var7, 1, 8, var7, Block.netherFence.blockID, Block.netherFence.blockID, false);
         this.a(par1World, par3StructureBoundingBox, 11, 7, var7, 11, 8, var7, Block.netherFence.blockID, Block.netherFence.blockID, false);
      }

      this.a(par1World, par3StructureBoundingBox, 4, 2, 0, 8, 2, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 2, 4, 12, 2, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 4, 0, 0, 8, 1, 3, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 4, 0, 9, 8, 1, 12, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 0, 0, 4, 3, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 9, 0, 4, 12, 1, 8, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

      for (int var8 = 4; var8 <= 8; var8++) {
         for (int var5 = 0; var5 <= 2; var5++) {
            this.b(par1World, Block.netherBrick.blockID, 0, var8, -1, var5, par3StructureBoundingBox);
            this.b(par1World, Block.netherBrick.blockID, 0, var8, -1, 12 - var5, par3StructureBoundingBox);
         }
      }

      for (int var9 = 0; var9 <= 2; var9++) {
         for (int var5 = 4; var5 <= 8; var5++) {
            this.b(par1World, Block.netherBrick.blockID, 0, var9, -1, var5, par3StructureBoundingBox);
            this.b(par1World, Block.netherBrick.blockID, 0, 12 - var9, -1, var5, par3StructureBoundingBox);
         }
      }

      this.a(par1World, par3StructureBoundingBox, 5, 5, 5, 7, 5, 7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
      this.a(par1World, par3StructureBoundingBox, 6, 1, 6, 6, 4, 6, 0, 0, false);
      this.a(par1World, Block.netherBrick.blockID, 0, 6, 0, 6, par3StructureBoundingBox);
      this.a(par1World, Block.lavaMoving.blockID, 0, 6, 5, 6, par3StructureBoundingBox);
      int var10 = this.a(6, 6);
      int var5 = this.a(5);
      int var6 = this.b(6, 6);
      if (par3StructureBoundingBox.isVecInside(var10, var5, var6)) {
         par1World.scheduledUpdatesAreImmediate = true;
         Block.blocksList[Block.lavaMoving.blockID].updateTick(par1World, var10, var5, var6, par2Random);
         par1World.scheduledUpdatesAreImmediate = false;
      }

      this.a(par1World, BTWBlocks.dormandSoulforge.blockID, WorldUtils.rotateFacingForCoordBaseMode(4, this.coordBaseMode), 5, 6, 6, par3StructureBoundingBox);
      return true;
   }
}
