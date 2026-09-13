package net.minecraft.src;

import java.util.concurrent.Callable;

final class CallableBlockType implements Callable {
   CallableBlockType(int var1) {
      this.blockID = var1;
   }

   public String callBlockType() {
      try {
         return String.format(
            "ID #%d (%s // %s)",
            this.blockID,
            Block.blocksList[this.blockID].getUnlocalizedName(),
            Block.blocksList[this.blockID].getClass().getCanonicalName()
         );
      } catch (Throwable var2) {
         return "ID #" + this.blockID;
      }
   }
}
