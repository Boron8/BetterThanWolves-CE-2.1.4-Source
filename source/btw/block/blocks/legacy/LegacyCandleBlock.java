package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LegacyCandleBlock extends Block {
   private static final double STICK_HEIGHT = 0.375;
   private static final double STICK_WIDTH = 0.125;
   private static final double STICK_HALF_WIDTH = 0.0625;
   private static final double WICK_HEIGHT = 0.0625;
   private static final double WICK_WIDTH = 0.03125;
   private static final double WICK_HALF_WIDTH = 0.015625;
   @Environment(EnvType.CLIENT)
   private boolean renderingWick = false;
   @Environment(EnvType.CLIENT)
   private Icon[] iconByColor = new Icon[16];
   @Environment(EnvType.CLIENT)
   private Icon iconWick;

   public LegacyCandleBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.legacyCandleMaterial);
      this.c(0.0F);
      this.setPicksEffectiveOn(true);
      this.setAxesEffectiveOn(true);
      this.a(1.0F);
      this.initBlockBounds(0.4375, 0.0, 0.4375, 0.5625, 0.375, 0.5625);
      this.a(j);
      this.c("fcBlockCandle");
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      int iBlockBelowMetadata = world.getBlockMetadata(i, j - 1, k);
      return iBlockBelowID == BTWBlocks.aestheticNonOpaque.blockID && iBlockBelowMetadata == 12
         ? true
         : WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(world, i, j - 1, k, 1, true);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return BTWItems.candle.itemID;
   }

   @Override
   public int damageDropped(int iMetadata) {
      return iMetadata;
   }

   @Override
   public int quantityDropped(Random rand) {
      return 4;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (!this.canPlaceBlockAt(world, i, j, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public void onNeighborDisrupted(World world, int i, int j, int k, int iToFacing) {
      if (iToFacing == 0) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconByColor[0] = register.registerIcon("fcBlockCandle_c00");
      this.iconByColor[1] = register.registerIcon("fcBlockCandle_c01");
      this.iconByColor[2] = register.registerIcon("fcBlockCandle_c02");
      this.iconByColor[3] = register.registerIcon("fcBlockCandle_c03");
      this.iconByColor[4] = register.registerIcon("fcBlockCandle_c04");
      this.iconByColor[5] = register.registerIcon("fcBlockCandle_c05");
      this.iconByColor[6] = register.registerIcon("fcBlockCandle_c06");
      this.iconByColor[7] = register.registerIcon("fcBlockCandle_c07");
      this.iconByColor[8] = register.registerIcon("fcBlockCandle_c08");
      this.iconByColor[9] = register.registerIcon("fcBlockCandle_c09");
      this.iconByColor[10] = register.registerIcon("fcBlockCandle_c10");
      this.iconByColor[11] = register.registerIcon("fcBlockCandle_c11");
      this.iconByColor[12] = register.registerIcon("fcBlockCandle_c12");
      this.iconByColor[13] = register.registerIcon("fcBlockCandle_c13");
      this.iconByColor[14] = register.registerIcon("fcBlockCandle_c14");
      this.iconByColor[15] = register.registerIcon("fcBlockCandle_c15");
      this.iconWick = register.registerIcon("fcBlockCandleWick");
      this.blockIcon = this.iconByColor[0];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByColor[iMetadata];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (iSide == 0) {
         return this.renderingWick ? false : RenderUtils.shouldRenderNeighborFullFaceSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      } else {
         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      double xPos = i + 0.5;
      double yPos = j + 0.1 + 0.375;
      double zPos = k + 0.5;
      world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderStandardBlock(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         this.renderingWick = true;
         renderBlocks.setRenderBounds(0.484375, 0.375, 0.484375, 0.515625, 0.4375, 0.515625);
         RenderUtils.renderBlockFullBrightWithTexture(renderBlocks, renderBlocks.blockAccess, i, j, k, this.iconWick);
         this.renderingWick = false;
      }
   }
}
