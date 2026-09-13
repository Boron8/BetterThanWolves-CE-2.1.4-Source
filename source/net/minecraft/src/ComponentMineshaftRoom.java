package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ComponentMineshaftRoom extends StructureComponent {
   private List roomsLinkedToTheRoom = new LinkedList();

   public ComponentMineshaftRoom(int var1, Random var2, int var3, int var4) {
      super(var1);
      this.boundingBox = new StructureBoundingBox(var3, 50, var4, var3 + 7 + var2.nextInt(6), 54 + var2.nextInt(6), var4 + 7 + var2.nextInt(6));
   }

   @Override
   public void buildComponent(StructureComponent var1, List var2, Random var3) {
      int var4 = this.c();
      int var6 = this.boundingBox.getYSize() - 3 - 1;
      if (var6 <= 0) {
         var6 = 1;
      }

      int var5 = 0;

      while (var5 < this.boundingBox.getXSize()) {
         var5 += var3.nextInt(this.boundingBox.getXSize());
         if (var5 + 3 > this.boundingBox.getXSize()) {
            break;
         }

         StructureComponent var7 = StructureMineshaftPieces.getNextComponent(
            var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var3.nextInt(var6) + 1, this.boundingBox.minZ - 1, 2, var4
         );
         if (var7 != null) {
            StructureBoundingBox var8 = var7.getBoundingBox();
            this.roomsLinkedToTheRoom
               .add(new StructureBoundingBox(var8.minX, var8.minY, this.boundingBox.minZ, var8.maxX, var8.maxY, this.boundingBox.minZ + 1));
         }

         var5 += 4;
      }

      var5 = 0;

      while (var5 < this.boundingBox.getXSize()) {
         var5 += var3.nextInt(this.boundingBox.getXSize());
         if (var5 + 3 > this.boundingBox.getXSize()) {
            break;
         }

         StructureComponent var16 = StructureMineshaftPieces.getNextComponent(
            var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var3.nextInt(var6) + 1, this.boundingBox.maxZ + 1, 0, var4
         );
         if (var16 != null) {
            StructureBoundingBox var19 = var16.getBoundingBox();
            this.roomsLinkedToTheRoom
               .add(new StructureBoundingBox(var19.minX, var19.minY, this.boundingBox.maxZ - 1, var19.maxX, var19.maxY, this.boundingBox.maxZ));
         }

         var5 += 4;
      }

      var5 = 0;

      while (var5 < this.boundingBox.getZSize()) {
         var5 += var3.nextInt(this.boundingBox.getZSize());
         if (var5 + 3 > this.boundingBox.getZSize()) {
            break;
         }

         StructureComponent var17 = StructureMineshaftPieces.getNextComponent(
            var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY + var3.nextInt(var6) + 1, this.boundingBox.minZ + var5, 1, var4
         );
         if (var17 != null) {
            StructureBoundingBox var20 = var17.getBoundingBox();
            this.roomsLinkedToTheRoom
               .add(new StructureBoundingBox(this.boundingBox.minX, var20.minY, var20.minZ, this.boundingBox.minX + 1, var20.maxY, var20.maxZ));
         }

         var5 += 4;
      }

      var5 = 0;

      while (var5 < this.boundingBox.getZSize()) {
         var5 += var3.nextInt(this.boundingBox.getZSize());
         if (var5 + 3 > this.boundingBox.getZSize()) {
            break;
         }

         StructureComponent var18 = StructureMineshaftPieces.getNextComponent(
            var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY + var3.nextInt(var6) + 1, this.boundingBox.minZ + var5, 3, var4
         );
         if (var18 != null) {
            StructureBoundingBox var21 = var18.getBoundingBox();
            this.roomsLinkedToTheRoom
               .add(new StructureBoundingBox(this.boundingBox.maxX - 1, var21.minY, var21.minZ, this.boundingBox.maxX, var21.maxY, var21.maxZ));
         }

         var5 += 4;
      }
   }

   @Override
   public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
      if (this.a(var1, var3)) {
         return false;
      } else {
         this.a(
            var1,
            var3,
            this.boundingBox.minX,
            this.boundingBox.minY,
            this.boundingBox.minZ,
            this.boundingBox.maxX,
            this.boundingBox.minY,
            this.boundingBox.maxZ,
            Block.dirt.blockID,
            0,
            true
         );
         this.a(
            var1,
            var3,
            this.boundingBox.minX,
            this.boundingBox.minY + 1,
            this.boundingBox.minZ,
            this.boundingBox.maxX,
            Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY),
            this.boundingBox.maxZ,
            0,
            0,
            false
         );

         for (StructureBoundingBox var5 : this.roomsLinkedToTheRoom) {
            this.a(var1, var3, var5.minX, var5.maxY - 2, var5.minZ, var5.maxX, var5.maxY, var5.maxZ, 0, 0, false);
         }

         this.a(
            var1,
            var3,
            this.boundingBox.minX,
            this.boundingBox.minY + 4,
            this.boundingBox.minZ,
            this.boundingBox.maxX,
            this.boundingBox.maxY,
            this.boundingBox.maxZ,
            0,
            false
         );
         return true;
      }
   }
}
