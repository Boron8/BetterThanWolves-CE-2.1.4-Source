package btw.block.blocks;

import btw.world.util.BlockPos;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LavaPillowBlock extends FullBlock {
   public LavaPillowBlock(int blockID) {
      super(blockID, Material.rock);
      this.c(0.8F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(l);
      this.c("fcBlockLavaPillow");
   }

   @Override
   public int idDropped(int metadata, Random random, int fortuneModifier) {
      return 0;
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
      super.a(world, player, x, y, z, metadata);
      this.setBlockToLava(world, x, y, z);
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int x, int y, int z, int metadata) {
      super.onBlockDestroyedWithImproperTool(world, player, x, y, z, metadata);
      this.setBlockToLava(world, x, y, z);
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public void postBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
      super.postBlockDestroyedByExplosion(world, x, y, z, explosion);
      this.setBlockToLava(world, x, y, z);
   }

   @Override
   public boolean isBlockDestroyedByBlockDispenser(int iMetadata) {
      return true;
   }

   @Override
   public void onRemovedByBlockDispenser(World world, int x, int y, int z) {
      super.onRemovedByBlockDispenser(world, x, y, z);
      this.setBlockToLava(world, x, y, z);
   }

   public void setBlockToLava(World world, int x, int y, int z) {
      if (world.isAirBlock(x, y, z)) {
         world.playAuxSFX(2227, x, y, z, 0);
         if (!this.hasWaterToSidesOrTop(world, x, y, z)) {
            int decayLevel = 1;
            world.setBlockAndMetadataWithNotify(x, y, z, Block.lavaMoving.blockID, decayLevel);
            decayLevel++;

            for (int facing = 2; facing <= 5; facing++) {
               BlockPos pos = new BlockPos(x, y, z, facing);
               if (world.isAirBlock(pos.x, pos.y, pos.z)) {
                  world.setBlockAndMetadataWithNotify(pos.x, pos.y, pos.z, Block.lavaMoving.blockID, decayLevel);
               }
            }
         }
      }
   }
}
