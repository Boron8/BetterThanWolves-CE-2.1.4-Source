package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class NetherBrickBlock extends Block {
   public NetherBrickBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.netherRockMaterial);
      this.c(2.0F);
      this.b(10.0F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.c("netherBrick");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseNetherBrick.blockID;
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
