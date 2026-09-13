package btw.block.blocks;

import btw.crafting.util.FurnaceBurnTime;
import btw.util.RandomSelector;
import btw.world.feature.trees.grower.AbstractTreeGrower;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.ToDoubleFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class SaplingBlock extends DailyGrowthCropsBlock {
   private static final double WIDTH = 0.8;
   private static final double HALF_WIDTH = 0.4;
   private String textureBase;
   private static final int NOT_2X2 = 0;
   private static final int VALID_2X2_NOT_MATURE = 1;
   private static final int VALID_2X2 = 2;
   private final Map<AbstractTreeGrower, Integer> treeGrowers = new HashMap<>();
   private final Map<AbstractTreeGrower, Integer> treeGrowers2x2 = new HashMap<>();
   @Environment(EnvType.CLIENT)
   private Icon[] icons = new Icon[4];
   @Environment(EnvType.CLIENT)
   private final int[] iconIndexByGrowthStage = new int[]{0, 0, 0, 1, 1, 2, 2, 3};

   public SaplingBlock(int id, String name, String textureBase) {
      super(id);
      this.setFurnaceBurnTime(FurnaceBurnTime.KINDLING);
      this.setFilterableProperties(0);
      this.initBlockBounds(0.09999999999999998, 0.0, 0.09999999999999998, 0.9, 0.8, 0.9);
      this.c(name);
      this.textureBase = textureBase;
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (this.updateIfBlockStays(world, x, y, z) && world.provider.dimensionId != 1) {
         this.attemptToGrow(world, x, y, z, rand);
      }
   }

   @Override
   protected void attemptToGrow(World world, int x, int y, int z, Random rand) {
      int timeOfDay = (int)(world.worldInfo.getWorldTime() % 24000L);
      if (timeOfDay > 14000 && timeOfDay < 22000) {
         if (this.getHasGrownToday(world, x, y, z)) {
            this.setHasGrownToday(world, x, y, z, false);
         }
      } else if (!this.getHasGrownToday(world, x, y, z) && this.canGrowAtCurrentLightLevel(world, x, y, z)) {
         Block blockBelow = Block.blocksList[world.getBlockId(x, y - 1, z)];
         if (blockBelow != null) {
            float growthChance = this.getBaseGrowthChance(world, x, y, z);
            if (blockBelow.getIsFertilizedForPlantGrowth(world, x, y - 1, z)) {
               growthChance *= 2.0F;
            }

            if (rand.nextFloat() <= growthChance) {
               this.incrementGrowthLevel(world, x, y, z);
               if (world.getBlockId(x, y, z) == this.blockID) {
                  this.updateFlagForGrownToday(world, x, y, z);
               }
            }
         }
      }
   }

   @Override
   protected void incrementGrowthLevel(World world, int x, int y, int z) {
      if (this.isFullyGrown(world, x, y, z)) {
         this.attemptToGrowTree(world, x, y, z);
      } else {
         int newGrowthLevel = this.getGrowthLevel(world, x, y, z) + 1;
         this.setGrowthLevel(world, x, y, z, newGrowthLevel);
         if (this.isFullyGrown(world, x, y, z)) {
            Block blockBelow = Block.blocksList[world.getBlockId(x, y - 1, z)];
            if (blockBelow != null) {
               blockBelow.notifyOfFullStagePlantGrowthOn(world, x, y - 1, z, this);
            }
         }
      }
   }

   @Override
   protected boolean requiresNaturalLight() {
      return false;
   }

   @Override
   public boolean canWeedsGrowInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int x, int y, int z) {
      Block block = Block.blocksList[world.getBlockId(x, y, z)];
      return block != null && block.canSaplingsGrowOnBlock(world, x, y, z);
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
      return this.getFixedBlockBoundsFromPool();
   }

   @Override
   public int idDropped(int metadata, Random rand, int fortuneModifier) {
      return this.blockID;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.isFullyGrown(metadata) ? this.getGrowthLevel(metadata) : 0;
   }

   @Override
   protected int getCropItemID() {
      return 0;
   }

   @Override
   protected int getSeedItemID() {
      return 0;
   }

   @Override
   protected boolean onlyDropWhenFullyGrown() {
      return false;
   }

   protected void attemptToGrowTree(World world, int x, int y, int z) {
      if (!this.checkFor2x2Tree(world, x, y, z)) {
         AbstractTreeGrower treeGrower = this.getTreeGrower(world.rand);
         int metadata = world.getBlockMetadata(x, y, z);
         world.setBlock(x, y, z, 0);
         if (!treeGrower.growTree(world, world.rand, x, y, z, false)) {
            world.setBlockAndMetadata(x, y, z, this.blockID, metadata);
         }
      }
   }

   protected boolean checkFor2x2Tree(World world, int x, int y, int z) {
      if (this.treeGrowers2x2.isEmpty()) {
         return false;
      } else {
         boolean has2x2 = false;
         if (this.canGrow2x2Tree(world, x, y, z) != 0) {
            has2x2 = true;
            if (this.canGrow2x2Tree(world, x, y, z) == 2) {
               this.attemptToGrow2x2Tree(world, x, y, z);
            }
         }

         if (this.canGrow2x2Tree(world, x - 1, y, z) != 0) {
            has2x2 = true;
            if (this.canGrow2x2Tree(world, x - 1, y, z) == 2) {
               this.attemptToGrow2x2Tree(world, x - 1, y, z);
            }
         }

         if (this.canGrow2x2Tree(world, x, y, z - 1) != 0) {
            has2x2 = true;
            if (this.canGrow2x2Tree(world, x, y, z - 1) == 2) {
               this.attemptToGrow2x2Tree(world, x, y, z - 1);
            }
         }

         if (this.canGrow2x2Tree(world, x - 1, y, z - 1) != 0) {
            has2x2 = true;
            if (this.canGrow2x2Tree(world, x - 1, y, z - 1) == 2) {
               this.attemptToGrow2x2Tree(world, x - 1, y, z - 1);
            }
         }

         return has2x2;
      }
   }

   protected void attemptToGrow2x2Tree(World world, int x, int y, int z) {
      AbstractTreeGrower treeGrower = this.getTreeGrower2x2(world.rand);
      int metadata = world.getBlockMetadata(x, y, z);
      world.setBlock(x, y, z, 0);
      world.setBlock(x + 1, y, z, 0);
      world.setBlock(x, y, z + 1, 0);
      world.setBlock(x + 1, y, z + 1, 0);
      if (!treeGrower.growTree(world, world.rand, x, y, z, false)) {
         world.setBlockAndMetadata(x, y, z, this.blockID, metadata);
         world.setBlockAndMetadata(x + 1, y, z, this.blockID, metadata);
         world.setBlockAndMetadata(x, y, z + 1, this.blockID, metadata);
         world.setBlockAndMetadata(x + 1, y, z + 1, this.blockID, metadata);
      }
   }

   protected int canGrow2x2Tree(World world, int x, int y, int z) {
      if (world.getBlockId(x + 1, y, z) != this.blockID || world.getBlockId(x, y, z + 1) != this.blockID || world.getBlockId(x + 1, y, z + 1) != this.blockID) {
         return 0;
      } else {
         return this.isFullyGrown(world, x, y, z)
               && this.isFullyGrown(world, x + 1, y, z)
               && this.isFullyGrown(world, x, y, z + 1)
               && this.isFullyGrown(world, x + 1, y, z + 1)
            ? 2
            : 1;
      }
   }

   public AbstractTreeGrower getTreeGrower(Random rand) {
      ToDoubleFunction<AbstractTreeGrower> weighter = this.treeGrowers::get;
      RandomSelector<AbstractTreeGrower> selector = RandomSelector.weighted(this.treeGrowers.keySet(), weighter);
      return selector.next(rand);
   }

   public AbstractTreeGrower getTreeGrower2x2(Random rand) {
      ToDoubleFunction<AbstractTreeGrower> weighter = this.treeGrowers2x2::get;
      RandomSelector<AbstractTreeGrower> selector = RandomSelector.weighted(this.treeGrowers2x2.keySet(), weighter);
      return selector.next(rand);
   }

   public SaplingBlock addTreeGrower(AbstractTreeGrower grower, int weight) {
      this.treeGrowers.put(grower, weight);
      return this;
   }

   public SaplingBlock add2x2TreeGrower(AbstractTreeGrower grower, int weight) {
      this.treeGrowers2x2.put(grower, weight);
      return this;
   }

   public boolean removeTreeGrower(String name) {
      for (AbstractTreeGrower treeGrower : this.treeGrowers.keySet()) {
         if (treeGrower.name.equals(name)) {
            this.treeGrowers.remove(treeGrower);
            return true;
         }
      }

      for (AbstractTreeGrower treeGrowerx : this.treeGrowers2x2.keySet()) {
         if (treeGrowerx.name.equals(name)) {
            this.treeGrowers2x2.remove(treeGrowerx);
            return true;
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      for (int i = 0; i < 4; i++) {
         this.icons[i] = register.registerIcon(this.textureBase + i);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int side, int metadata) {
      return this.icons[this.iconIndexByGrowthStage[this.getGrowthLevel(metadata)]];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int metadata, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(this.blockID, 1, 0));
      list.add(new ItemStack(this.blockID, 1, 7));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, x, y, z));
      renderer.renderCrossedSquares(this, x, y, z);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderType() {
      return 1;
   }
}
