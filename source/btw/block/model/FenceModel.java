package btw.block.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public class FenceModel extends BlockModel {
   protected static final double POST_WIDTH = 0.25;
   protected static final double POST_HALF_WIDTH = 0.125;
   protected static final double ITEM_POSTS_WIDTH = 0.25;
   protected static final double ITEM_POSTS_HALF_WIDTH = 0.125;
   protected static final double ITEM_POSTS_BORDER_GAP = 0.125;
   protected static final double STRUT_WIDTH = 0.125;
   protected static final double STRUT_HALF_WIDTH = 0.0625;
   protected static final double STRUT_HEIGHT = 0.1875;
   protected static final double STRUT_BOTTOM_VERTICAL_OFFSET = 0.375;
   protected static final double STRUT_TOP_VERTICAL_OFFSET = 0.75;
   public BlockModel modelStruts;
   public BlockModel modelItem;
   public AxisAlignedBB boxCollisionCenter;
   public AxisAlignedBB boxCollisionStruts;
   public AxisAlignedBB boxBoundsCenter;

   @Override
   protected void initModel() {
      this.addBox(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      this.modelStruts = new BlockModel();
      this.modelStruts.addBox(0.4375, 0.375, 0.0, 0.5625, 0.5625, 0.5);
      this.modelStruts.addBox(0.4375, 0.75, 0.0, 0.5625, 0.9375, 0.5);
      this.modelItem = new BlockModel();
      this.modelItem.addBox(0.375, 0.0, 0.0, 0.625, 1.0, 0.25);
      this.modelItem.addBox(0.375, 0.0, 0.75, 0.625, 1.0, 1.0);
      this.modelItem.addBox(0.4375, 0.375, -0.125, 0.5625, 0.5625, 1.125);
      this.modelItem.addBox(0.4375, 0.75, -0.125, 0.5625, 0.9375, 1.125);
      this.boxCollisionCenter = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.5, 0.625);
      this.boxCollisionStruts = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.5, 0.5);
      this.boxBoundsCenter = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderAsItemBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      this.modelItem.renderAsItemBlock(renderBlocks, block, iItemDamage);
   }
}
