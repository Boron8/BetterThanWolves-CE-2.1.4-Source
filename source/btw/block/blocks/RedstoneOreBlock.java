package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class RedstoneOreBlock extends OreBlockStaged {
   private boolean isGlowing;

   public RedstoneOreBlock(int iBlockID, boolean bGlowing) {
      super(iBlockID);
      if (bGlowing) {
         this.b(true);
      }

      this.isGlowing = bGlowing;
   }

   @Override
   public int tickRate(World par1World) {
      return 30;
   }

   @Override
   public void onBlockClicked(World world, int i, int j, int k, EntityPlayer player) {
      this.setGlowing(world, i, j, k);
      super.a(world, i, j, k, player);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      this.setGlowing(world, i, j, k);
      return super.a(world, i, j, k, player, iFacing, fXClick, fYClick, fZClick);
   }

   @Override
   public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
      this.setGlowing(world, i, j, k);
      super.b(world, i, j, k, entity);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (this.isGlowing) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         world.setBlock(i, j, k, Block.oreRedstone.blockID, iMetadata, 3);
      }
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      return new ItemStack(Block.oreRedstone, 1, iMetadata);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return Item.redstone.itemID;
   }

   @Override
   public int quantityDroppedWithBonus(int iFortuneModifier, Random rand) {
      return this.quantityDropped(rand) + rand.nextInt(iFortuneModifier + 1);
   }

   @Override
   public int quantityDropped(Random rand) {
      return 4 + rand.nextInt(2);
   }

   @Override
   public int idDroppedOnConversion(int iMetadata) {
      return Item.redstone.itemID;
   }

   @Override
   public int quantityDroppedOnConversion(Random rand) {
      return 4 + rand.nextInt(2);
   }

   @Override
   public int getRequiredToolLevelForOre(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   protected void setGlowing(World world, int i, int j, int k) {
      this.emitParticles(world, i, j, k);
      if (!this.isGlowing) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         world.setBlock(i, j, k, Block.oreRedstoneGlowing.blockID, iMetadata, 3);
      }
   }

   protected void emitParticles(World world, int i, int j, int k) {
      Random rand = world.rand;
      double dOffstet = 0.0625;

      for (int iTempDirection = 0; iTempDirection < 6; iTempDirection++) {
         double xLoc = i + rand.nextFloat();
         double yLoc = j + rand.nextFloat();
         double zLoc = k + rand.nextFloat();
         if (iTempDirection == 0 && !world.isBlockOpaqueCube(i, j + 1, k)) {
            yLoc = j + 1 + dOffstet;
         }

         if (iTempDirection == 1 && !world.isBlockOpaqueCube(i, j - 1, k)) {
            yLoc = j + 0 - dOffstet;
         }

         if (iTempDirection == 2 && !world.isBlockOpaqueCube(i, j, k + 1)) {
            zLoc = k + 1 + dOffstet;
         }

         if (iTempDirection == 3 && !world.isBlockOpaqueCube(i, j, k - 1)) {
            zLoc = k + 0 - dOffstet;
         }

         if (iTempDirection == 4 && !world.isBlockOpaqueCube(i + 1, j, k)) {
            xLoc = i + 1 + dOffstet;
         }

         if (iTempDirection == 5 && !world.isBlockOpaqueCube(i - 1, j, k)) {
            xLoc = i + 0 - dOffstet;
         }

         if (xLoc < i || xLoc > i + 1 || yLoc < 0.0 || yLoc > j + 1 || zLoc < k || zLoc > k + 1) {
            world.spawnParticle("reddust", xLoc, yLoc, zLoc, 0.0, 0.0, 0.0);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (this.isGlowing) {
         this.emitParticles(world, i, j, k);
      }
   }
}
