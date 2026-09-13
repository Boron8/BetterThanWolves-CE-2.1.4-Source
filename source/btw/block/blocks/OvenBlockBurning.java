package btw.block.blocks;

public class OvenBlockBurning extends OvenBlock {
   public OvenBlockBurning(int iBlockID) {
      super(iBlockID, true);
      this.a(0.5F);
      this.k(8);
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }
}
