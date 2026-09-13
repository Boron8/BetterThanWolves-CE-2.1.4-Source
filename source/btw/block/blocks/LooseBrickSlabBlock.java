package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LooseBrickSlabBlock extends MortarReceiverSlabBlock {
   public LooseBrickSlabBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockBrickLooseSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.looseBrick.blockID;
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      int iNewMetadata = 4;
      if (this.getIsUpsideDown(world, i, j, k)) {
         iNewMetadata |= 8;
      }

      world.setBlockAndMetadataWithNotify(i, j, k, Block.stoneSingleSlab.blockID, iNewMetadata);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockBrickLoose");
   }
}
