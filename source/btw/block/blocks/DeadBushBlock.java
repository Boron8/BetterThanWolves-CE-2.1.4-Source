package btw.block.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.BlockDeadBush;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class DeadBushBlock extends BlockDeadBush {
   protected static final double WIDTH = 0.8;
   protected static final double HALF_WIDTH = 0.4;

   public DeadBushBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.0F);
      this.setBuoyant();
      this.initBlockBounds(0.09999999999999998, 0.0, 0.09999999999999998, 0.9, 0.8, 0.9);
      this.a(Block.soundGrassFootstep);
      this.c("deadbush");
   }

   @Override
   public boolean canSpitWebReplaceBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isReplaceableVegetation(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return animal.canGrazeOnRoughVegetation();
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      return world.getBlockId(i, j, k) == Block.sand.blockID;
   }
}
