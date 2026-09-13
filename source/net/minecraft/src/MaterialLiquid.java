package net.minecraft.src;

public class MaterialLiquid extends Material {
   public MaterialLiquid(MapColor var1) {
      super(var1);
      this.i();
      this.n();
   }

   @Override
   public boolean isLiquid() {
      return true;
   }

   @Override
   public boolean blocksMovement() {
      return false;
   }

   @Override
   public boolean isSolid() {
      return false;
   }
}
