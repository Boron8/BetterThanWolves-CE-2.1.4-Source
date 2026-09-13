package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.items.HoeItem;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class DirtBlock extends FullBlock {
   public DirtBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.5F);
      this.setShovelsEffectiveOn();
      this.setHoesEffectiveOn();
      this.a(Block.soundGravelFootstep);
      this.c("dirt");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseDirt.blockID;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.dirtPile.itemID, 6, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      super.onBlockDestroyedWithImproperTool(world, player, i, j, k, iMetadata);
      this.onDirtDugWithImproperTool(world, i, j, k);
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      super.a(world, i, j, k, explosion);
      this.onDirtDugWithImproperTool(world, i, j, k);
   }

   @Override
   protected void onNeighborDirtDugWithImproperTool(World world, int i, int j, int k, int iToFacing) {
      world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
   }

   @Override
   public boolean getCanGrassSpreadToBlock(World world, int i, int j, int k) {
      Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
      return blockAbove == null || blockAbove.getCanGrassGrowUnderBlock(world, i, j + 1, k, false);
   }

   @Override
   public boolean spreadGrassToBlock(World world, int x, int y, int z) {
      world.setBlockWithNotify(x, y, z, Block.grass.blockID);
      ((GrassBlock)Block.grass).setSparse(world, x, y, z);
      return true;
   }

   @Override
   public boolean getCanMyceliumSpreadToBlock(World world, int i, int j, int k) {
      return !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j + 1, k, 0);
   }

   @Override
   public boolean spreadMyceliumToBlock(World world, int x, int y, int z) {
      world.setBlockWithNotify(x, y, z, Block.mycelium.blockID);
      ((MyceliumBlock)Block.mycelium).setSparse(world, x, y, z);
      return true;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public void onVegetationAboveGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      if (animal.getDisruptsEarthOnGraze()) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
         this.notifyNeighborsBlockDisrupted(world, i, j, k);
      }
   }

   @Override
   public boolean canReedsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canSaplingsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canWildVegetationGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlightSpreadToBlock(World world, int i, int j, int k, int iBlightLevel) {
      return true;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return stack != null && stack.getItem() instanceof HoeItem;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
      if (!world.isRemote) {
         world.playAuxSFX(2001, i, j, k, this.blockID);
      }

      return true;
   }
}
