package btw.block.blocks;

import btw.block.tileentity.CauldronTileEntity;
import btw.block.tileentity.CookingVesselTileEntity;
import btw.client.render.util.RenderUtils;
import btw.inventory.BTWContainers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class CauldronBlock extends CookingVesselBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconContents;

   public CauldronBlock(int iBlockID) {
      super(iBlockID, Material.iron);
      this.c(3.5F);
      this.b(10.0F);
      this.a(Block.soundMetalFootstep);
      this.c("fcBlockCauldron");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new CauldronTileEntity();
   }

   @Override
   protected void validateFireUnderState(World world, int i, int j, int k) {
      if (!world.isRemote) {
         TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
         if (tileEnt instanceof CauldronTileEntity) {
            CauldronTileEntity tileEntityCauldron = (CauldronTileEntity)tileEnt;
            tileEntityCauldron.validateFireUnderType();
         }
      }
   }

   @Override
   protected int getContainerID() {
      return BTWContainers.cauldronContainerID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon sideIcon = register.registerIcon("fcBlockCauldron_side");
      this.blockIcon = sideIcon;
      this.iconInteriorBySideArray[0] = this.iconWideBandBySideArray[0] = this.iconCenterColumnBySideArray[0] = register.registerIcon("fcBlockCauldron_bottom");
      this.iconInteriorBySideArray[1] = this.iconCenterColumnBySideArray[1] = register.registerIcon("fcBlockCauldron_top");
      this.iconWideBandBySideArray[1] = register.registerIcon("fcBlockCauldronWideBand_top");
      this.iconInteriorBySideArray[2] = this.iconWideBandBySideArray[2] = this.iconCenterColumnBySideArray[2] = sideIcon;
      this.iconInteriorBySideArray[3] = this.iconWideBandBySideArray[3] = this.iconCenterColumnBySideArray[3] = sideIcon;
      this.iconInteriorBySideArray[4] = this.iconWideBandBySideArray[4] = this.iconCenterColumnBySideArray[4] = sideIcon;
      this.iconInteriorBySideArray[5] = this.iconWideBandBySideArray[5] = this.iconCenterColumnBySideArray[5] = sideIcon;
      this.iconContents = register.registerIcon("fcBlockCauldron_contents");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      super.renderBlock(renderBlocks, i, j, k);
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      if (this.getFacing(blockAccess, i, j, k) == 1) {
         TileEntity tileEntity = blockAccess.getBlockTileEntity(i, j, k);
         if (tileEntity instanceof CookingVesselTileEntity) {
            CookingVesselTileEntity vesselEntity = (CookingVesselTileEntity)blockAccess.getBlockTileEntity(i, j, k);
            short iItemCount = vesselEntity.storageSlotsOccupied;
            float fHeightRatio = iItemCount / 27.0F;
            float fBottom = 0.5625F;
            float fTop = fBottom + 0.0625F + (0.875F - (fBottom + 0.0625F)) * fHeightRatio;
            renderBlocks.setRenderBounds(0.125, fBottom, 0.125, 0.875, fTop, 0.875);
            RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.iconContents);
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      super.renderBlockAsItem(renderBlocks, iItemDamage, fBrightness);
      float fBottom = 0.5625F;
      float fTop = 0.625F;
      renderBlocks.setRenderBounds(0.125, 0.5625, 0.125, 0.875, 0.625, 0.875);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, this.iconContents);
   }
}
