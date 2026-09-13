package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.tileentity.CookingVesselTileEntity;
import btw.block.tileentity.CrucibleTileEntity;
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

public class CrucibleBlock extends CookingVesselBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconContents;
   @Environment(EnvType.CLIENT)
   private Icon iconContentsHeated;

   public CrucibleBlock(int iBlockID) {
      super(iBlockID, Material.glass);
      this.c(0.6F);
      this.b(3.0F);
      this.setPicksEffectiveOn(true);
      this.a(Block.soundGlassFootstep);
      this.c("fcBlockCrucible");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new CrucibleTileEntity();
   }

   @Override
   protected void validateFireUnderState(World world, int i, int j, int k) {
      if (!world.isRemote) {
         TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
         if (tileEnt instanceof CrucibleTileEntity) {
            CrucibleTileEntity tileEntityCrucible = (CrucibleTileEntity)tileEnt;
            tileEntityCrucible.validateFireUnderType();
         }
      }
   }

   @Override
   protected int getContainerID() {
      return BTWContainers.crucibleContainerID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon sideIcon = register.registerIcon("fcBlockCrucible_side");
      this.blockIcon = sideIcon;
      this.iconInteriorBySideArray[0] = this.iconWideBandBySideArray[0] = this.iconCenterColumnBySideArray[0] = register.registerIcon("fcBlockCrucible_bottom");
      this.iconInteriorBySideArray[1] = this.iconCenterColumnBySideArray[1] = register.registerIcon("fcBlockCrucible_top");
      this.iconWideBandBySideArray[1] = register.registerIcon("fcBlockCrucibleWideBand_top");
      this.iconInteriorBySideArray[2] = this.iconWideBandBySideArray[2] = this.iconCenterColumnBySideArray[2] = sideIcon;
      this.iconInteriorBySideArray[3] = this.iconWideBandBySideArray[3] = this.iconCenterColumnBySideArray[3] = sideIcon;
      this.iconInteriorBySideArray[4] = this.iconWideBandBySideArray[4] = this.iconCenterColumnBySideArray[4] = sideIcon;
      this.iconInteriorBySideArray[5] = this.iconWideBandBySideArray[5] = this.iconCenterColumnBySideArray[5] = sideIcon;
      this.iconContents = register.registerIcon("fcBlockCrucible_contents");
      this.iconContentsHeated = register.registerIcon("lava");
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
            if (iItemCount > 0) {
               float fHeightRatio = iItemCount / 27.0F;
               float fBottom = 0.1875F;
               float fTop = fBottom + 0.0625F + (0.875F - (fBottom + 0.0625F)) * fHeightRatio;
               renderBlocks.setRenderBounds(0.125, fBottom, 0.125, 0.875, fTop, 0.875);
               if (blockAccess.getBlockId(i, j - 1, k) == BTWBlocks.stokedFire.blockID) {
                  RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.iconContentsHeated);
               } else {
                  RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.iconContents);
               }
            }
         }
      }

      return true;
   }
}
