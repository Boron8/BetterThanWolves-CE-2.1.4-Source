package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class PlanterBlockBase extends Block {
   protected static final double PLANTER_WIDTH = 0.75;
   protected static final double PLANTER_HALF_WIDTH = 0.375;
   protected static final double PLANTER_BAND_HEIGHT = 0.3125;
   protected static final double PLANTER_BAND_HALF_HEIGHT = 0.15625;
   @Environment(EnvType.CLIENT)
   protected Icon iconTopSoilWet;
   @Environment(EnvType.CLIENT)
   protected Icon iconTopSoilWetFertilized;

   protected PlanterBlockBase(int iBlockID) {
      super(iBlockID, Material.glass);
      this.c(0.6F);
      this.setPicksEffectiveOn(true);
      this.b(true);
      this.a(l);
      this.c("fcBlockPlanterSoil");
      this.a(CreativeTabs.tabDecorations);
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
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && entity.isEntityAlive() && entity instanceof EntityItem) {
         EntityItem entityItem = (EntityItem)entity;
         ItemStack stack = entityItem.getEntityItem();
         if (stack.getItem().itemID == Item.dyePowder.itemID && stack.getItemDamage() == 15 && this.attemptToApplyFertilizerTo(world, i, j, k)) {
            stack.stackSize--;
            if (stack.stackSize <= 0) {
               entityItem.w();
            }

            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         }
      }
   }

   @Override
   public float getPlantGrowthOnMultiplier(World world, int i, int j, int k, Block plantBlock) {
      return this.getIsFertilizedForPlantGrowth(world, i, j, k) ? 2.0F : 1.0F;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.125, 0.0, 0.125, 0.875, 0.6875, 0.875);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.6875, 0.0, 1.0, 1.0, 1.0);
      return rayTrace.getFirstIntersection();
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 1.0F;
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 0 || super.hasCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockPlanter");
      this.iconTopSoilWet = register.registerIcon("fcBlockPlanter_top_wet");
      this.iconTopSoilWetFertilized = register.registerIcon("fcBlockPlanter_top_wet_fertilized");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   protected void renderFilledPlanterInvBlock(RenderBlocks renderer, Block block, int iItemDamage) {
      renderer.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.6875, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderer, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderer.setRenderBounds(0.0, 0.6875, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderer, block, -0.5F, -0.5F, -0.5F, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   protected boolean renderFilledPlanterBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.6875, 0.875);
      renderer.renderStandardBlock(this, i, j, k);
      renderer.setRenderBounds(0.0, 0.6875, 0.0, 1.0, 1.0, 1.0);
      renderer.renderStandardBlock(this, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.6875, 0.0, 1.0, 1.0, 1.0).offset(i, j, k);
   }
}
