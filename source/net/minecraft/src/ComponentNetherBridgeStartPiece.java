package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeStartPiece extends ComponentNetherBridgeCrossing3 {
   public StructureNetherBridgePieceWeight theNetherBridgePieceWeight;
   public List primaryWeights;
   public List secondaryWeights;
   public ArrayList field_74967_d = new ArrayList();

   public ComponentNetherBridgeStartPiece(Random var1, int var2, int var3) {
      super(var1, var2, var3);
      this.primaryWeights = new ArrayList();

      for (StructureNetherBridgePieceWeight var7 : StructureNetherBridgePieces.getPrimaryComponents()) {
         var7.field_78827_c = 0;
         this.primaryWeights.add(var7);
      }

      this.secondaryWeights = new ArrayList();

      for (StructureNetherBridgePieceWeight var11 : StructureNetherBridgePieces.getSecondaryComponents()) {
         var11.field_78827_c = 0;
         this.secondaryWeights.add(var11);
      }
   }
}
