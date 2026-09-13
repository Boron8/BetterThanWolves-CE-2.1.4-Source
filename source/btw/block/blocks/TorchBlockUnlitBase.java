package btw.block.blocks;

import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public abstract class TorchBlockUnlitBase extends TorchBlockBase {
   protected TorchBlockUnlitBase(int iBlockID) {
      super(iBlockID);
      this.a(null);
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return 60;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      world.setBlockAndMetadataWithNotify(i, j, k, this.getLitBlockID(), world.getBlockMetadata(i, j, k));
      world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
      return true;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   protected abstract int getLitBlockID();
}
