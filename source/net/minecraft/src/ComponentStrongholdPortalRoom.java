package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdPortalRoom extends ComponentStronghold {
   private boolean hasSpawner;

   public ComponentStrongholdPortalRoom(int var1, Random var2, StructureBoundingBox var3, int var4) {
      super(var1);
      this.coordBaseMode = var4;
      this.boundingBox = var3;
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      if (var1 != null) {
         ((ComponentStrongholdStairs2)var1).strongholdPortalRoom = this;
      }
   }

   public static ComponentStrongholdPortalRoom findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5, int var6) {
      StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -1, 0, 11, 8, 16, var5);
      return a(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new ComponentStrongholdPortalRoom(var6, var1, var7, var5) : null;
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      this.a(var1, var3, 0, 0, 0, 10, 7, 15, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var2, var3, EnumDoor.GRATES, 4, 1, 0);
      int var4 = 6;
      this.a(var1, var3, 1, var4, 1, 1, var4, 14, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 9, var4, 1, 9, var4, 14, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 2, var4, 1, 8, var4, 2, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 2, var4, 14, 8, var4, 14, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 1, 1, 1, 2, 1, 4, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 8, 1, 1, 9, 1, 4, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 1, 1, 1, 1, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
      this.a(var1, var3, 9, 1, 1, 9, 1, 3, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);
      this.a(var1, var3, 3, 1, 8, 7, 1, 12, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 4, 1, 9, 6, 1, 11, Block.lavaMoving.blockID, Block.lavaMoving.blockID, false);

      for (byte var5 = 3; var5 < 14; var5 += 2) {
         this.a(var1, var3, 0, 3, var5, 0, 4, var5, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
         this.a(var1, var3, 10, 3, var5, 10, 4, var5, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
      }

      for (byte var14 = 2; var14 < 9; var14 += 2) {
         this.a(var1, var3, var14, 3, 15, var14, 4, 15, Block.fenceIron.blockID, Block.fenceIron.blockID, false);
      }

      int var15 = this.c(Block.stairsStoneBrick.blockID, 3);
      this.a(var1, var3, 4, 1, 5, 6, 1, 7, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 4, 2, 6, 6, 2, 7, false, var2, StructureStrongholdPieces.getStrongholdStones());
      this.a(var1, var3, 4, 3, 7, 6, 3, 7, false, var2, StructureStrongholdPieces.getStrongholdStones());

      for (int var6 = 4; var6 <= 6; var6++) {
         this.a(var1, Block.stairsStoneBrick.blockID, var15, var6, 1, 4, var3);
         this.a(var1, Block.stairsStoneBrick.blockID, var15, var6, 2, 5, var3);
         this.a(var1, Block.stairsStoneBrick.blockID, var15, var6, 3, 6, var3);
      }

      byte var16 = 2;
      byte var7 = 0;
      byte var8 = 3;
      byte var9 = 1;
      switch (this.coordBaseMode) {
         case 0:
            var16 = 0;
            var7 = 2;
            break;
         case 1:
            var16 = 1;
            var7 = 3;
            var8 = 0;
            var9 = 2;
         case 2:
         default:
            break;
         case 3:
            var16 = 3;
            var7 = 1;
            var8 = 0;
            var9 = 2;
      }

      this.a(var1, Block.endPortalFrame.blockID, var16 + (var2.nextFloat() > 0.9F ? 4 : 0), 4, 3, 8, var3);
      this.a(var1, Block.endPortalFrame.blockID, var16 + (var2.nextFloat() > 0.9F ? 4 : 0), 5, 3, 8, var3);
      this.a(var1, Block.endPortalFrame.blockID, var16 + (var2.nextFloat() > 0.9F ? 4 : 0), 6, 3, 8, var3);
      this.a(var1, Block.endPortalFrame.blockID, var7 + (var2.nextFloat() > 0.9F ? 4 : 0), 4, 3, 12, var3);
      this.a(var1, Block.endPortalFrame.blockID, var7 + (var2.nextFloat() > 0.9F ? 4 : 0), 5, 3, 12, var3);
      this.a(var1, Block.endPortalFrame.blockID, var7 + (var2.nextFloat() > 0.9F ? 4 : 0), 6, 3, 12, var3);
      this.a(var1, Block.endPortalFrame.blockID, var8 + (var2.nextFloat() > 0.9F ? 4 : 0), 3, 3, 9, var3);
      this.a(var1, Block.endPortalFrame.blockID, var8 + (var2.nextFloat() > 0.9F ? 4 : 0), 3, 3, 10, var3);
      this.a(var1, Block.endPortalFrame.blockID, var8 + (var2.nextFloat() > 0.9F ? 4 : 0), 3, 3, 11, var3);
      this.a(var1, Block.endPortalFrame.blockID, var9 + (var2.nextFloat() > 0.9F ? 4 : 0), 7, 3, 9, var3);
      this.a(var1, Block.endPortalFrame.blockID, var9 + (var2.nextFloat() > 0.9F ? 4 : 0), 7, 3, 10, var3);
      this.a(var1, Block.endPortalFrame.blockID, var9 + (var2.nextFloat() > 0.9F ? 4 : 0), 7, 3, 11, var3);
      if (!this.hasSpawner) {
         var4 = this.a(3);
         int var10 = this.a(5, 6);
         int var11 = this.b(5, 6);
         if (var3.isVecInside(var10, var4, var11)) {
            this.hasSpawner = true;
            var1.setBlock(var10, var4, var11, Block.mobSpawner.blockID, 0, 2);
            TileEntityMobSpawner var12 = (TileEntityMobSpawner)var1.getBlockTileEntity(var10, var4, var11);
            if (var12 != null) {
               var12.func_98049_a().setMobID("Silverfish");
            }
         }
      }

      return true;
   }
}
