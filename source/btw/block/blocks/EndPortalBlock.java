package btw.block.blocks;

import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockEndPortal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class EndPortalBlock extends BlockEndPortal {
   public EndPortalBlock(int iBlockID, Material material) {
      super(iBlockID, material);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
      this.b(true);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      WorldUtils.gameProgressSetEndDimensionHasBeenAccessedServerOnly();
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      super.a(world, i, j, k, rand);
      WorldUtils.gameProgressSetEndDimensionHasBeenAccessedServerOnly();
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }
}
