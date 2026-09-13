package net.minecraft.src;

import java.util.Map;
import java.util.Map.Entry;

public class MapGenMineshaft extends MapGenStructure {
   private double field_82673_e = 0.01;

   public MapGenMineshaft() {
   }

   public MapGenMineshaft(Map var1) {
      for (Entry var3 : var1.entrySet()) {
         if (((String)var3.getKey()).equals("chance")) {
            this.field_82673_e = MathHelper.parseDoubleWithDefault((String)var3.getValue(), this.field_82673_e);
         }
      }
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(var1), Math.abs(var2));
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      return new StructureMineshaftStart(this.worldObj, this.rand, var1, var2);
   }
}
