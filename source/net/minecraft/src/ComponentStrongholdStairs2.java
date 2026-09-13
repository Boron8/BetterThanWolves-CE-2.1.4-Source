package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentStrongholdStairs2 extends ComponentStrongholdStairs {
   public StructureStrongholdPieceWeight strongholdPieceWeight;
   public ComponentStrongholdPortalRoom strongholdPortalRoom;
   public ArrayList field_75026_c = new ArrayList();

   public ComponentStrongholdStairs2(int var1, Random var2, int var3, int var4) {
      super(0, var2, var3, var4);
   }

   @Override
   public ChunkPosition getCenter() {
      return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.a() : super.a();
   }
}
