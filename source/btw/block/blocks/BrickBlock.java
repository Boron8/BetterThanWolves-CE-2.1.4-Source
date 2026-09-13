package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BrickBlock extends Block {
   public BrickBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.setPicksEffectiveOn();
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseBrick.blockID;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }
}
