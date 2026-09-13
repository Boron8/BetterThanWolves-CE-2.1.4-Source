package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.block.model.LogSpikeModel;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class LogSpikeBlock extends Block {
   public static final float HARDNESS = 2.0F;
   private BlockModel modelBlock = new LogSpikeModel();
   protected String sideTexture;
   protected String topTexture;
   @Environment(EnvType.CLIENT)
   private Icon iconSide;

   public LogSpikeBlock(int blockID, String sideTexture, String topTexture) {
      super(blockID, BTWBlocks.logMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn();
      this.setChiselsEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(5, 5);
      Block.useNeighborBrightness[blockID] = true;
      this.k(4);
      this.a(g);
      this.sideTexture = sideTexture;
      this.topTexture = topTexture;
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
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockModel transformedModel = this.modelBlock.makeTemporaryCopy();
      transformedModel.tiltToFacingAlongY(iFacing);
      return transformedModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == Block.getOppositeFacing(iFacing);
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.b(world, i, j, k, new ItemStack(BTWItems.sawDust));
   }

   @Override
   public boolean canDropFromExplosion(Explosion explosion) {
      return false;
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      float fChanceOfPileDrop = 1.0F;
      if (explosion != null) {
         fChanceOfPileDrop = 1.0F / explosion.explosionSize;
      }

      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 1, 0, fChanceOfPileDrop);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 1000;
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & 7;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -8;
      return iMetadata | iFacing;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon(this.sideTexture);
      this.iconSide = register.registerIcon(this.topTexture);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iFacing = this.getFacing(iMetadata);
      return iSide != iFacing && iSide != Block.getOppositeFacing(iFacing) ? this.iconSide : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int x, int y, int z) {
      int facing = this.getFacing(renderBlocks.blockAccess, x, y, z);
      BlockModel model = this.modelBlock.makeTemporaryCopy();
      model.tiltToFacingAlongY(facing);
      int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
      if (metadata == 4 || metadata == 5 || metadata == 6 || metadata == 7) {
         renderBlocks.setUVRotateTop(1);
         renderBlocks.setUVRotateBottom(1);
         renderBlocks.setUVRotateWest(1);
         renderBlocks.setUVRotateEast(1);
      } else if (metadata == 8 || metadata == 9 || metadata == 10 || metadata == 11) {
         renderBlocks.setUVRotateNorth(1);
         renderBlocks.setUVRotateSouth(1);
      }

      model.renderAsBlock(renderBlocks, this, x, y, z);
      renderBlocks.clearUVRotation();
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.modelBlock.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      AxisAlignedBB tempSelectionBox = LogSpikeModel.boxSelection.makeTemporaryCopy();
      tempSelectionBox.tiltToFacingAlongY(iFacing);
      return tempSelectionBox.offset(i, j, k);
   }
}
