package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.block.model.SoulforgeModel;
import btw.block.tileentity.AnvilTileEntity;
import btw.inventory.BTWContainers;
import btw.inventory.container.SoulforgeContainer;
import btw.util.MiscUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class SoulforgeBlock extends BlockContainer {
   private final BlockModel blockModel = new SoulforgeModel();

   public SoulforgeBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.soulforgedSteelMaterial);
      this.c(3.5F);
      this.a(k);
      this.c("fcBlockAnvil");
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
   public TileEntity createNewTileEntity(World world) {
      return new AnvilTileEntity();
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing < 2) {
         iFacing = 2;
      } else {
         iFacing = Block.getOppositeFacing(iFacing);
      }

      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityLiving);
      this.setFacing(world, i, j, k, iFacing);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote && player instanceof EntityPlayerMP) {
         SoulforgeContainer container = new SoulforgeContainer(player.inventory, world, i, j, k);
         BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.soulforgeContainerID);
      }

      return true;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing != 2 && iFacing != 3
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.25, 1.0, 1.0, 0.75)
         : AxisAlignedBB.getAABBPool().getAABB(0.25, 0.0, 0.0, 0.75, 1.0, 1.0);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      AnvilTileEntity tileEntityAnvil = (AnvilTileEntity)world.getBlockTileEntity(i, j, k);
      if (tileEntityAnvil != null) {
         tileEntityAnvil.ejectMoulds();
      }

      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockModel transformedModel = this.blockModel.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      return transformedModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iFacing;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      this.rotateAroundJAxis(world, i, j, k, bReverse);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      int iFacing = this.getFacing(renderBlocks.blockAccess, i, j, k);
      BlockModel transformedModel = this.blockModel.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      return transformedModel.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.blockModel.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }
}
