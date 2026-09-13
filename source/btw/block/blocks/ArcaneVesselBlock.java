package btw.block.blocks;

import btw.block.tileentity.ArcaneVesselTileEntity;
import btw.client.texture.ArcaneVesselXPTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ArcaneVesselBlock extends VesselBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconContents;

   public ArcaneVesselBlock(int iBlockID) {
      super(iBlockID, Material.iron);
      this.c(3.5F);
      this.b(10.0F);
      this.setPicksEffectiveOn(true);
      this.a(Block.soundMetalFootstep);
      this.a(CreativeTabs.tabRedstone);
      this.c("fcBlockArcaneVessel");
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new ArcaneVesselTileEntity();
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
      if (tileEnt != null && tileEnt instanceof ArcaneVesselTileEntity) {
         ArcaneVesselTileEntity vesselEnt = (ArcaneVesselTileEntity)tileEnt;
         vesselEnt.ejectContentsOnBlockBreak();
      }

      super.a(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && entity instanceof EntityXPOrb) {
         this.onEntityXPOrbCollidedWithBlock(world, i, j, k, (EntityXPOrb)entity);
         world.func_96440_m(i, j, k, this.blockID);
      }
   }

   private void onEntityXPOrbCollidedWithBlock(World world, int i, int j, int k, EntityXPOrb entityXPOrb) {
      if (!entityXPOrb.isDead) {
         if (!this.getMechanicallyPoweredFlag(world, i, j, k)) {
            float fVesselHeight = 1.0F;
            AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB(i, j + 1.0F, k, i + 1, j + 1.0F + 0.05F, k + 1);
            if (entityXPOrb.boundingBox.intersectsWith(collectionZone)) {
               boolean bSwallowed = false;
               TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
               if (tileEnt != null && tileEnt instanceof ArcaneVesselTileEntity) {
                  ArcaneVesselTileEntity vesselTileEnt = (ArcaneVesselTileEntity)tileEnt;
                  if (vesselTileEnt.attemptToSwallowXPOrb(world, i, j, k, entityXPOrb)) {
                     world.playAuxSFX(2231, i, j, k, 0);
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      ArcaneVesselTileEntity tileEntity = (ArcaneVesselTileEntity)par1World.getBlockTileEntity(par2, par3, par4);
      int currentXP = tileEntity.getContainedTotalExperience();
      float xpPercent = currentXP / 1000.0F;
      return MathHelper.floor_float(xpPercent * 14.0F) + (currentXP > 0 ? 1 : 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon sideIcon = register.registerIcon("fcBlockVessel_side");
      this.blockIcon = sideIcon;
      this.iconWideBandBySideArray[0] = this.iconCenterColumnBySideArray[0] = register.registerIcon("fcBlockVessel_bottom");
      this.iconCenterColumnBySideArray[1] = register.registerIcon("fcBlockVessel_top");
      this.iconWideBandBySideArray[1] = register.registerIcon("fcBlockVesselWideBand_top");
      this.iconWideBandBySideArray[2] = this.iconCenterColumnBySideArray[2] = sideIcon;
      this.iconWideBandBySideArray[3] = this.iconCenterColumnBySideArray[3] = sideIcon;
      this.iconWideBandBySideArray[4] = this.iconCenterColumnBySideArray[4] = sideIcon;
      this.iconWideBandBySideArray[5] = this.iconCenterColumnBySideArray[5] = sideIcon;
      this.iconInteriorBySideArray[0] = this.iconWideBandBySideArray[0];
      this.iconInteriorBySideArray[1] = this.iconWideBandBySideArray[0];
      this.iconInteriorBySideArray[2] = this.iconWideBandBySideArray[0];
      this.iconInteriorBySideArray[3] = this.iconWideBandBySideArray[0];
      this.iconInteriorBySideArray[4] = this.iconWideBandBySideArray[0];
      this.iconInteriorBySideArray[5] = this.iconWideBandBySideArray[0];
      this.iconContents = register.registerIcon("fcBlockVessel_xp", new ArcaneVesselXPTexture("fcBlockVessel_xp"));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      super.renderBlock(renderBlocks, i, j, k);
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      if (this.getFacing(blockAccess, i, j, k) == 1) {
         TileEntity tileEntity = blockAccess.getBlockTileEntity(i, j, k);
         if (tileEntity instanceof ArcaneVesselTileEntity) {
            ArcaneVesselTileEntity vesselEntity = (ArcaneVesselTileEntity)tileEntity;
            int iContainedExperience = vesselEntity.getVisualExperienceLevel();
            if (iContainedExperience > 0) {
               float fHeightRatio = iContainedExperience / 10.0F;
               float fBottom = 0.1875F;
               float fTop = fBottom + 0.0625F + (0.875F - (fBottom + 0.0625F)) * fHeightRatio;
               renderBlocks.setRenderBounds(0.125, fBottom, 0.125, 0.875, fTop, 0.875);
               Tessellator tesselator = Tessellator.instance;
               tesselator.setBrightness(240);
               renderBlocks.renderFaceYPos(this, i, j, k, this.iconContents);
            }
         }
      }

      return true;
   }
}
