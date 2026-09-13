package net.minecraft.src;

public class MaterialTransparent extends Material {
   public MaterialTransparent(MapColor var1) {
      super(var1);
      this.i();
   }

   @Override
   public boolean isSolid() {
      return false;
   }

   @Override
   public boolean getCanBlockGrass() {
      return false;
   }

   @Override
   public boolean blocksMovement() {
      return false;
   }
}
