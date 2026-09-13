package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl1 implements Callable {
   CallableLvl1(World var1, int var2) {
      this.theWorld = var1;
      this.field_85179_a = var2;
   }

   public String getWorldEntitiesAsString() {
      try {
         return String.format(
            "ID #%d (%s // %s)",
            this.field_85179_a,
            Block.blocksList[this.field_85179_a].getUnlocalizedName(),
            Block.blocksList[this.field_85179_a].getClass().getCanonicalName()
         );
      } catch (Throwable var2) {
         return "ID #" + this.field_85179_a;
      }
   }
}
