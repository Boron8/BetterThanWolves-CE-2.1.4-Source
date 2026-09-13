package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LooseDirtSlabBlock extends FallingSlabBlock {
   public LooseDirtSlabBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.5F);
      this.setShovelsEffectiveOn(true);
      this.a(Block.soundGravelFootstep);
      this.c("fcBlockDirtLooseSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.dirtPile.itemID, 3, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean getCanGrassSpreadToBlock(World world, int i, int j, int k) {
      Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
      return blockAbove == null || blockAbove.getCanGrassGrowUnderBlock(world, i, j + 1, k, true);
   }

   @Override
   public boolean spreadGrassToBlock(World world, int x, int y, int z) {
      world.setBlockWithNotify(x, y, z, BTWBlocks.grassSlab.blockID);
      BTWBlocks.grassSlab.setSparse(world, x, y, z);
      return true;
   }

   @Override
   public boolean getCanMyceliumSpreadToBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean spreadMyceliumToBlock(World world, int x, int y, int z) {
      world.setBlockWithNotify(x, y, z, BTWBlocks.myceliumSlab.blockID);
      BTWBlocks.myceliumSlab.setSparse(world, x, y, z);
      return true;
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.looseDirt.blockID;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public void onVegetationAboveGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      if (animal.getDisruptsEarthOnGraze()) {
         this.notifyNeighborsBlockDisrupted(world, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockDirtLoose");
   }
}
