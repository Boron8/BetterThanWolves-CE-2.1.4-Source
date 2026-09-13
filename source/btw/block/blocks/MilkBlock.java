package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class MilkBlock extends FallingBlock {
   public static final double HEIGHT = 0.0625;
   public static final int DECAY_TICK_RATE = 10;

   public MilkBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.milkMaterial);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
      this.c(0.0F);
      this.b(0.0F);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockMilk");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!this.checkForFall(world, i, j, k)) {
         int iDecayLevel = this.getDecayLevel(world, i, j, k);
         if (iDecayLevel < 1) {
            this.setDecayLevel(world, i, j, k, ++iDecayLevel);
            world.scheduleBlockUpdate(i, j, k, this.blockID, 10);
         } else {
            world.setBlockToAir(i, j, k);
         }
      }
   }

   @Override
   protected void a(EntityFallingSand entity) {
      entity.metadata = this.setDecayLevel(entity.metadata, 0);
   }

   public int getDecayLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getDecayLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getDecayLevel(int iMetadata) {
      return iMetadata & 1;
   }

   public void setDecayLevel(World world, int i, int j, int k, int iLevel) {
      int iMetadata = this.setDecayLevel(world.getBlockMetadata(i, j, k), iLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setDecayLevel(int iMetadata, int iLevel) {
      iMetadata &= -2;
      return iMetadata | iLevel;
   }
}
