package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import btw.item.items.ShearsItem;
import btw.item.util.ItemUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class HempCropBlock extends BlockCrops {
   private static final double COLLISION_BOX_WIDTH = 0.4F;
   private static final double COLLISION_BOX_HALF_WIDTH = 0.2F;
   private static final float BASE_GROWTH_CHANCE = 0.1F;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBottomByGrowthArray = new Icon[8];
   @Environment(EnvType.CLIENT)
   private Icon iconTop;

   public HempCropBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.2F);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.setFireProperties(Flammability.CROPS);
      this.initBlockBounds(0.29999999701976776, 0.0, 0.29999999701976776, 0.7000000029802322, 1.0, 0.7000000029802322);
      this.c("fcBlockHemp");
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return iMetadata >= 7 ? this.getCropItem() : 0;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iGrowthLevel = blockAccess.getBlockMetadata(i, j, k);
      iGrowthLevel = MathHelper.clamp_int(iGrowthLevel, 0, 7);
      double dBoundsHeight = (1 + iGrowthLevel) / 8.0;
      double dHalfWidth = 0.2F;
      int iWeedsGrowthLevel = this.getWeedsGrowthLevel(blockAccess, i, j, k);
      if (iWeedsGrowthLevel > 0) {
         dBoundsHeight = Math.max(dBoundsHeight, WeedsBlock.getWeedsBoundsHeight(iWeedsGrowthLevel));
         dHalfWidth = 0.375;
      }

      return AxisAlignedBB.getAABBPool().getAABB(0.5 - dHalfWidth, 0.0, 0.5 - dHalfWidth, 0.5 + dHalfWidth, dBoundsHeight, 0.5 + dHalfWidth);
   }

   @Override
   public boolean doesBlockDropAsItemOnSaw(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canBlockStay(World world, int i, int j, int k) {
      return super.f(world, i, j, k) || world.getBlockId(i, j - 1, k) == this.blockID && !this.getIsTopBlock(world, i, j - 1, k);
   }

   @Override
   protected void attemptToGrow(World world, int i, int j, int k, Random rand) {
      if (!this.getIsTopBlock(world, i, j, k)
         && this.getWeedsGrowthLevel(world, i, j, k) == 0
         && (
            world.getBlockLightValue(i, j, k) >= 15
               || world.getBlockId(i, j + 1, k) == BTWBlocks.lightBlockOn.blockID
               || world.getBlockId(i, j + 2, k) == BTWBlocks.lightBlockOn.blockID
         )) {
         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null && blockBelow.isBlockHydratedForPlantGrowthOn(world, i, j - 1, k)) {
            int iMetadata = world.getBlockMetadata(i, j, k);
            if (this.getGrowthLevel(world, i, j, k) < 7) {
               float fChanceOfGrowth = this.getBaseGrowthChance(world, i, j, k) * blockBelow.getPlantGrowthOnMultiplier(world, i, j - 1, k, this);
               if (rand.nextFloat() <= fChanceOfGrowth) {
                  this.incrementGrowthLevel(world, i, j, k);
               }
            } else if (world.isAirBlock(i, j + 1, k)) {
               float fChanceOfGrowth = this.getBaseGrowthChance(world, i, j, k) / 4.0F * blockBelow.getPlantGrowthOnMultiplier(world, i, j - 1, k, this);
               if (rand.nextFloat() <= fChanceOfGrowth) {
                  int iNewMetadata = this.setIsTopBlock(0, true);
                  world.setBlockAndMetadataWithNotify(i, j + 1, k, this.blockID, iNewMetadata);
                  blockBelow.notifyOfFullStagePlantGrowthOn(world, i, j - 1, k, this);
               }
            }
         }
      }
   }

   @Override
   public void dropSeeds(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (this.getIsTopBlock(iMetadata) && world.rand.nextInt(100) < 50) {
         ItemUtils.dropStackAsIfBlockHarvested(world, i, j, k, new ItemStack(this.getSeedItem(), 1, 0));
      }
   }

   @Override
   protected int getSeedItem() {
      return BTWItems.hempSeeds.itemID;
   }

   @Override
   protected int getCropItem() {
      return BTWItems.hemp.itemID;
   }

   @Override
   public float getBaseGrowthChance(World world, int i, int j, int k) {
      return 0.1F;
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      super.a(world, player, i, j, k, iMetadata);
      if (!world.isRemote
         && (player.getCurrentEquippedItem() == null || !(player.getCurrentEquippedItem().getItem() instanceof ShearsItem))
         && world.getBlockId(i, j - 1, k) == this.blockID) {
         this.c(world, i, j - 1, k, world.getBlockMetadata(i, j - 1, k), 0);
         world.setBlockToAir(i, j - 1, k);
      }
   }

   protected boolean getIsTopBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsTopBlock(blockAccess.getBlockMetadata(i, j, k));
   }

   protected boolean getIsTopBlock(int iMetadata) {
      return (iMetadata & 8) != 0;
   }

   protected void setIsTopBlock(World world, int i, int j, int k, boolean bTop) {
      int iMetadata = this.setIsTopBlock(world.getBlockMetadata(i, j, k), bTop);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected int setIsTopBlock(int iMetadata, boolean bTop) {
      if (bTop) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconTop = register.registerIcon("fcBlockHemp_top");
      this.blockIcon = this.iconTop;
      this.iconBottomByGrowthArray[0] = register.registerIcon("fcBlockHemp_bottom_00");
      this.iconBottomByGrowthArray[1] = register.registerIcon("fcBlockHemp_bottom_01");
      this.iconBottomByGrowthArray[2] = register.registerIcon("fcBlockHemp_bottom_02");
      this.iconBottomByGrowthArray[3] = register.registerIcon("fcBlockHemp_bottom_03");
      this.iconBottomByGrowthArray[4] = register.registerIcon("fcBlockHemp_bottom_04");
      this.iconBottomByGrowthArray[5] = register.registerIcon("fcBlockHemp_bottom_05");
      this.iconBottomByGrowthArray[6] = register.registerIcon("fcBlockHemp_bottom_06");
      this.iconBottomByGrowthArray[7] = register.registerIcon("fcBlockHemp_bottom_07");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.getIsTopBlock(iMetadata) ? this.iconTop : this.iconBottomByGrowthArray[iMetadata];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.renderCrossedSquares(this, i, j, k);
      if (!this.getIsTopBlock(renderer.blockAccess, i, j, k)) {
         BTWBlocks.weeds.renderWeeds(this, renderer, i, j, k);
      }

      return true;
   }
}
