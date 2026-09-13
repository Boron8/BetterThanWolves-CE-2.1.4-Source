package net.minecraft.src;

public class MaterialLogic extends Material {
   public MaterialLogic(MapColor var1) {
      super(var1);
      this.p();
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
