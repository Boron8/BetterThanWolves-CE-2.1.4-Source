package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class AshGroundCoverBlock extends GroundCoverBlock {
   public AshGroundCoverBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.ashMaterial);
      this.b(true);
      this.a(Block.soundSandFootstep);
      this.c("fcBlockAshGroundCover");
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (world.isRainingAtPos(i, j, k)) {
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
   }

   @Override
   public boolean getCanGrassGrowUnderBlock(World world, int i, int j, int k, boolean bGrassOnHalfSlab) {
      return false;
   }

   @Override
   public boolean getCanBlockBeReplacedByFire(World world, int i, int j, int k) {
      return true;
   }

   public static boolean canAshReplaceBlock(World world, int i, int j, int k) {
      Block block = Block.blocksList[world.getBlockId(i, j, k)];
      return block == null || block.isAirBlock() || block.isGroundCover() && block != BTWBlocks.ashCoverBlock;
   }

   public static boolean attemptToPlaceAshAt(World world, int i, int j, int k) {
      if (canAshReplaceBlock(world, i, j, k)) {
         int iBlockBelowID = world.getBlockId(i, j - 1, k);
         Block blockBelow = Block.blocksList[iBlockBelowID];
         if (blockBelow != null && blockBelow.canGroundCoverRestOnBlock(world, i, j - 1, k)) {
            world.setBlockWithNotify(i, j, k, BTWBlocks.ashCoverBlock.blockID);
            return true;
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      super.b(world, i, j, k, rand);
      if (rand.nextInt(10) == 0) {
         double dYParticle = j + 0.25;
         world.spawnParticle("townaura", i + rand.nextDouble(), dYParticle, k + rand.nextDouble(), 0.0, 0.0, 0.0);
      }
   }
}
