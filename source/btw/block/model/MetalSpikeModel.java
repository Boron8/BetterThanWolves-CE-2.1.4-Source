package btw.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public class MetalSpikeModel extends BlockModel {
   protected static final double SHAFT_WIDTH = 0.0625;
   protected static final double SHAFT_HALF_WIDTH = 0.03125;
   protected static final double BASE_WIDTH = 0.25;
   protected static final double BASE_HALF_WIDTH = 0.125;
   protected static final double BASE_HEIGHT = 0.125;
   protected static final double BASE_HALF_HEIGHT = 0.0625;
   protected static final double BALL_WIDTH = 0.1875;
   protected static final double BALL_HALF_WIDTH = 0.09375;
   protected static final double BALL_VERTICAL_OFFSET = 0.625;
   protected static final double CANDLE_HOLDER_WIDTH = 0.25;
   protected static final double CANDLE_HOLDER_HALF_WIDTH = 0.125;
   protected static final double CANDLE_HOLDER_HEIGHT = 0.0625;
   protected static final double CANDLE_HOLDER_HALF_HEIGHT = 0.03125;
   protected static final double CANDLE_HOLDER_VERTICAL_OFFSET = 0.9375;
   protected static final double CENTER_BRACE_WIDTH = 0.1875;
   protected static final double CENTER_BRACE_HALF_WIDTH = 0.09375;
   protected static final double CENTER_BRACE_HEIGHT = 0.25;
   protected static final double CENTER_BRACE_HALF_HEIGHT = 0.125;
   protected static final double CENTER_BRACE_VERTICAL_OFFSET = 0.375;
   protected static final double SIDE_SUPPORT_LENGTH = 0.5;
   protected static final double SIDE_SUPPORT_WIDTH = 0.0625;
   protected static final double SIDE_SUPPORT_HALF_WIDTH = 0.03125;
   protected static final double SIDE_SUPPORT_HEIGHT = 0.125;
   protected static final double SIDE_SUPPORT_HALF_HEIGHT = 0.0625;
   protected static final double SIDE_SUPPORT_VERTICAL_OFFSET = 0.4375;
   protected static final double BOUNDING_BOX_WIDTH = 0.25;
   protected static final double BOUNDING_BOX_HALF_WIDTH = 0.125;
   public BlockModel modelBase;
   public BlockModel modelHolder;
   public BlockModel modelBall;
   public BlockModel modelCenterBrace;
   public BlockModel modelSideSupport;
   public BlockModel holderModelLarge;
   public AxisAlignedBB boxCollisionCenter;
   public AxisAlignedBB boxCollisionStrut;

   @Override
   protected void initModel() {
      this.addBox(0.46875, 0.0, 0.46875, 0.53125, 1.0, 0.53125);
      this.modelBase = new BlockModel();
      this.modelBase.addBox(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
      this.modelHolder = new BlockModel();
      this.modelHolder.addBox(0.375, 0.9375, 0.375, 0.625, 1.0, 0.625);
      this.holderModelLarge = new BlockModel();
      this.holderModelLarge.addBox(0.375, 0.875, 0.375, 0.625, 0.9375, 0.625);
      this.holderModelLarge.addBox(0.3125, 0.9375, 0.3125, 0.6875, 1.0, 0.6875);
      this.modelBall = new BlockModel();
      this.modelBall.addBox(0.40625, 0.625, 0.40625, 0.59375, 0.8125, 0.59375);
      this.modelCenterBrace = new BlockModel();
      this.modelCenterBrace.addBox(0.40625, 0.375, 0.40625, 0.59375, 0.625, 0.59375);
      this.modelSideSupport = new BlockModel();
      this.modelSideSupport.addBox(0.46875, 0.4375, 0.0, 0.53125, 0.5625, 0.5);
      this.boxCollisionCenter = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      this.boxCollisionStrut = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 0.625);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderAsItemBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      super.renderAsItemBlock(renderBlocks, block, iItemDamage);
      this.modelBase.renderAsItemBlock(renderBlocks, block, iItemDamage);
      this.modelBall.renderAsItemBlock(renderBlocks, block, iItemDamage);
   }
}
