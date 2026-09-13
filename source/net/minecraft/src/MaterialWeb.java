package net.minecraft.src;

final class MaterialWeb extends Material {
   MaterialWeb(MapColor var1) {
      super(var1);
   }

   @Override
   public boolean blocksMovement() {
      return false;
   }
}
