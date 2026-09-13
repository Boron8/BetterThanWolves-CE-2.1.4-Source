package btw.block.blocks;

import btw.block.tileentity.VaseTileEntity;
import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class VaseBlock extends BlockContainer {
   public static final float VASE_BASE_WIDTH = 0.5F;
   public static final float VASE_BASE_HALF_WIDTH = 0.25F;
   public static final float VASE_BASE_HEIGHT = 0.0625F;
   public static final float VASE_BODY_WIDTH = 0.625F;
   public static final float VASE_BODY_HALF_WIDTH = 0.3125F;
   public static final float VASE_BODY_HEIGHT = 0.375F;
   public static final float VASE_NECK_BASE_WIDTH = 0.5F;
   public static final float VASE_NECK_BASE_HALF_WIDTH = 0.25F;
   public static final float VASE_NECK_BASE_HEIGHT = 0.0625F;
   public static final float VASE_NECK_WIDTH = 0.25F;
   public static final float VASE_NECK_HALF_WIDTH = 0.125F;
   public static final float VASE_NECK_HEIGHT = 0.4375F;
   public static final float VASE_TOP_WIDTH = 0.375F;
   public static final float VASE_TOP_HALF_WIDTH = 0.1875F;
   public static final float VASE_TOP_HEIGHT = 0.0625F;
   @Environment(EnvType.CLIENT)
   private Icon[] iconByColor = new Icon[16];

   public VaseBlock(int iBlockID) {
      super(iBlockID, Material.glass);
      this.c(0.0F);
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
      this.a(l);
      this.c("fcBlockVase");
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
   public int quantityDropped(Random random) {
      return 0;
   }

   @Override
   public int damageDropped(int i) {
      return i;
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new VaseTileEntity();
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      ItemStack playerEquippedItem = player.getCurrentEquippedItem();
      if (world.isRemote) {
         return true;
      } else {
         if (playerEquippedItem != null && playerEquippedItem.stackSize > 0) {
            VaseTileEntity tileEntityVase = (VaseTileEntity)world.getBlockTileEntity(i, j, k);
            int iTempStackSize = playerEquippedItem.stackSize;
            if (InventoryUtils.addItemStackToInventory(tileEntityVase, playerEquippedItem)) {
               player.destroyCurrentEquippedItem();
               world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
               return true;
            }

            if (playerEquippedItem.stackSize < iTempStackSize) {
               world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      VaseTileEntity tileEntity = (VaseTileEntity)world.getBlockTileEntity(i, j, k);
      if (tileEntity != null) {
         InventoryUtils.ejectInventoryContents(world, i, j, k, tileEntity);
      }

      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   public void onBlockHarvested(World world, int i, int j, int k, int iMetadata, EntityPlayer player) {
      if (!world.isRemote && !EnchantmentHelper.getSilkTouchModifier(player)) {
         this.checkForExplosion(world, i, j, k);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.25, 0.0, 0.25, 0.75, 0.0625, 0.75);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.1875, 0.0625, 0.1875, 0.8125, 0.4375, 0.8125);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.25, 0.4375, 0.25, 0.75, 0.5, 0.75);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.375, 0.5, 0.375, 0.625, 0.9375, 0.625);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.9375, 0.3125, 0.6875, 1.0, 0.6875);
      return rayTrace.getFirstIntersection();
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 1;
   }

   @Override
   public void onArrowImpact(World world, int i, int j, int k, EntityArrow arrow) {
      if (!world.isRemote) {
         this.breakVase(world, i, j, k);
      }
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   public void breakVase(World world, int i, int j, int k) {
      world.playAuxSFX(2001, i, j, k, this.blockID);
      this.checkForExplosion(world, i, j, k);
      world.setBlockWithNotify(i, j, k, 0);
   }

   private boolean checkForExplosion(World world, int i, int j, int k) {
      VaseTileEntity tileEntity = (VaseTileEntity)world.getBlockTileEntity(i, j, k);
      if (tileEntity != null && InventoryUtils.getFirstOccupiedStackOfItem(tileEntity, BTWItems.blastingOil.itemID) >= 0) {
         InventoryUtils.clearInventoryContents(tileEntity);
         world.createExplosion(null, i + 0.5, j + 0.5, k + 0.5, 1.5F, true);
         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconByColor[0] = register.registerIcon("fcBlockVase_c00");
      this.iconByColor[1] = register.registerIcon("fcBlockVase_c01");
      this.iconByColor[2] = register.registerIcon("fcBlockVase_c02");
      this.iconByColor[3] = register.registerIcon("fcBlockVase_c03");
      this.iconByColor[4] = register.registerIcon("fcBlockVase_c04");
      this.iconByColor[5] = register.registerIcon("fcBlockVase_c05");
      this.iconByColor[6] = register.registerIcon("fcBlockVase_c06");
      this.iconByColor[7] = register.registerIcon("fcBlockVase_c07");
      this.iconByColor[8] = register.registerIcon("fcBlockVase_c08");
      this.iconByColor[9] = register.registerIcon("fcBlockVase_c09");
      this.iconByColor[10] = register.registerIcon("fcBlockVase_c10");
      this.iconByColor[11] = register.registerIcon("fcBlockVase_c11");
      this.iconByColor[12] = register.registerIcon("fcBlockVase_c12");
      this.iconByColor[13] = register.registerIcon("fcBlockVase_c13");
      this.iconByColor[14] = register.registerIcon("fcBlockVase_c14");
      this.iconByColor[15] = register.registerIcon("fcBlockVase_c15");
      this.blockIcon = this.iconByColor[0];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata != 0) {
         iMetadata = 1 + (~iMetadata & 15);
      }

      return this.iconByColor[iMetadata];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return renderVaseBlock(renderBlocks, renderBlocks.blockAccess, i, j, k, this);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderInvBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderVaseBlock(RenderBlocks renderBlocks, IBlockAccess blockAcces, int i, int j, int k, Block block) {
      renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.0625, 0.75);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.1875, 0.0625, 0.1875, 0.8125, 0.4375, 0.8125);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.25, 0.4375, 0.25, 0.75, 0.5, 0.75);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.375, 0.5, 0.375, 0.625, 0.9375, 0.625);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.3125, 0.9375, 0.3125, 0.6875, 1.0, 0.6875);
      renderBlocks.renderStandardBlock(block, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public static void renderInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.0625, 0.75);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.1875, 0.0625, 0.1875, 0.8125, 0.4375, 0.8125);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.25, 0.4375, 0.25, 0.75, 0.5, 0.75);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.375, 0.5, 0.375, 0.625, 0.9375, 0.625);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.3125, 0.9375, 0.3125, 0.6875, 1.0, 0.6875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
   }
}
