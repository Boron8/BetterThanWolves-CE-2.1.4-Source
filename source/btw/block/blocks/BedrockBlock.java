package btw.block.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BedrockBlock extends FullBlock {
   public BedrockBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.r();
      this.b(6000000.0F);
      this.a(Block.soundStoneFootstep);
      this.c("bedrock");
      this.D();
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getMobilityFlag() {
      return 2;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }
}
