package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableTileEntityName implements Callable {
   CallableTileEntityName(TileEntity var1) {
      this.theTileEntity = var1;
   }

   public String callTileEntityName() {
      return (String)TileEntity.getClassToNameMap().get(this.theTileEntity.getClass()) + " // " + this.theTileEntity.getClass().getCanonicalName();
   }
}
