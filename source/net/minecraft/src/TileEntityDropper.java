package net.minecraft.src;

public class TileEntityDropper extends TileEntityDispenser {
   @Override
   public String getInvName() {
      return this.c() ? this.customName : "container.dropper";
   }
}
