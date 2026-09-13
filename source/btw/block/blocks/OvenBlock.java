package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.block.model.OvenModel;
import btw.block.tileentity.OvenTileEntity;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class OvenBlock extends FurnaceBlock {
   protected final BlockModel modelBlockInterior = new OvenModel();
   protected final float clickYTopPortion = 0.375F;
   protected final float clickYBottomPortion = 0.375F;
   @Environment(EnvType.CLIENT)
   private Icon[] fuelOverlays;
   @Environment(EnvType.CLIENT)
   private Icon currentFuelOverlay = null;
   @Environment(EnvType.CLIENT)
   private Icon blankOverlay;
   @Environment(EnvType.CLIENT)
   protected boolean isRenderingInterior = false;
   @Environment(EnvType.CLIENT)
   private int interiorBrightness = 0;

   protected OvenBlock(int iBlockID, boolean bIsLit) {
      super(iBlockID, bIsLit);
      this.setPicksEffectiveOn();
      this.c(2.0F);
      this.b(3.33F);
      this.c("fcBlockFurnaceBrick");
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new OvenTileEntity();
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iBlockFacing = iMetadata & 7;
      if (iBlockFacing != iFacing) {
         return false;
      } else {
         ItemStack heldStack = player.getCurrentEquippedItem();
         OvenTileEntity tileEntity = (OvenTileEntity)world.getBlockTileEntity(i, j, k);
         ItemStack cookStack = tileEntity.getCookStack();
         if (fYClick > 0.375F) {
            if (cookStack != null) {
               tileEntity.givePlayerCookStack(player, iFacing);
               return true;
            }

            if (heldStack != null && this.isValidCookItem(heldStack)) {
               if (!world.isRemote) {
                  tileEntity.addCookStack(new ItemStack(heldStack.itemID, 1, heldStack.getItemDamage()));
               }

               heldStack.stackSize--;
               return true;
            }
         } else if (fYClick < 0.375F && heldStack != null) {
            Item item = heldStack.getItem();
            int iItemDamage = heldStack.getItemDamage();
            if (item.getCanBeFedDirectlyIntoBrickOven(iItemDamage)) {
               if (!world.isRemote) {
                  int iItemsConsumed = tileEntity.attemptToAddFuel(heldStack);
                  if (iItemsConsumed > 0) {
                     if (this.isActive) {
                        world.playSoundEffect(
                           i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 0.2F + world.rand.nextFloat() * 0.1F, world.rand.nextFloat() * 0.25F + 1.25F
                        );
                     } else {
                        world.playSoundEffect(
                           i + 0.5, j + 0.5, k + 0.5, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F
                        );
                     }

                     heldStack.stackSize -= iItemsConsumed;
                  }
               }

               return true;
            }
         }

         return false;
      }
   }

   @Override
   public int quantityDropped(Random rand) {
      return 4 + rand.nextInt(6);
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return Item.brick.itemID;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return !WorldUtils.doesBlockHaveSolidTopSurface(world, i, j - 1, k) ? false : super.c(world, i, j, k);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveSolidTopSurface(world, i, j - 1, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iBlockFacing = blockAccess.getBlockMetadata(i, j, k) & 7;
      return iBlockFacing != iFacing;
   }

   @Override
   public void updateFurnaceBlockState(boolean bBurning, World world, int i, int j, int k, boolean bHasContents) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
      c = true;
      if (bBurning) {
         world.setBlock(i, j, k, BTWBlocks.burningOven.blockID);
      } else {
         world.setBlock(i, j, k, BTWBlocks.idleOven.blockID);
      }

      c = false;
      if (!bHasContents) {
         iMetadata &= 7;
      } else {
         iMetadata |= 8;
      }

      world.SetBlockMetadataWithNotify(i, j, k, iMetadata, 2);
      if (tileEntity != null) {
         tileEntity.validate();
         world.setBlockTileEntity(i, j, k, tileEntity);
      }
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      if (!this.isActive) {
         OvenTileEntity tileEntity = (OvenTileEntity)blockAccess.getBlockTileEntity(i, j, k);
         if (tileEntity.getVisualFuelLevel() > 0) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      if (!this.isActive) {
         OvenTileEntity tileEntity = (OvenTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity.attemptToLight()) {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
            return true;
         }
      }

      return false;
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      if (!this.isActive) {
         OvenTileEntity tileEntity = (OvenTileEntity)blockAccess.getBlockTileEntity(i, j, k);
         if (tileEntity.hasValidFuel()) {
            return 60;
         }
      }

      return 0;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   protected int iddroppedsilktouch() {
      return BTWBlocks.idleOven.blockID;
   }

   @Override
   public boolean getIsBlockWarm(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isActive;
   }

   @Override
   public boolean doesBlockHopperInsert(World world, int i, int j, int k) {
      return true;
   }

   public boolean isValidCookItem(ItemStack stack) {
      return FurnaceRecipes.smelting().getSmeltingResult(stack.getItem().itemID) != null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockFurnaceBrick_side");
      this.furnaceIconTop = register.registerIcon("fcBlockFurnaceBrick_top");
      if (this.isActive) {
         this.furnaceIconFront = register.registerIcon("fcBlockFurnaceBrick_front_lit");
      } else {
         this.furnaceIconFront = register.registerIcon("fcBlockFurnaceBrick_front");
      }

      this.fuelOverlays = new Icon[9];

      for (int iTempIndex = 0; iTempIndex < 9; iTempIndex++) {
         this.fuelOverlays[iTempIndex] = register.registerIcon("fcOverlayFurnaceFuel_" + iTempIndex);
      }

      this.blankOverlay = register.registerIcon("fcOverlayBlank");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return BTWBlocks.idleOven.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iFacing = iMetadata & 7;
      if (iFacing < 2 || iFacing > 5) {
         iFacing = 3;
      }

      if (this.currentFuelOverlay == null) {
         if (iFacing == iSide) {
            return this.furnaceIconFront;
         } else {
            return iSide < 2 ? this.furnaceIconTop : this.blockIcon;
         }
      } else {
         return iFacing == iSide ? this.currentFuelOverlay : this.blankOverlay;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (this.isRenderingInterior) {
         BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, Block.getOppositeFacing(iSide));
         int iFacing = blockAccess.getBlockMetadata(myPos.x, myPos.y, myPos.z) & 7;
         return iSide != Block.getOppositeFacing(iFacing);
      } else {
         return super.a(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderStandardBlock(this, i, j, k);
      int iFacing = renderer.blockAccess.getBlockMetadata(i, j, k) & 7;
      BlockModel transformedModel = this.modelBlockInterior.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      BlockPos interiorFacesPos = new BlockPos(i, j, k, iFacing);
      this.interiorBrightness = this.getMixedBrightnessForBlock(renderer.blockAccess, interiorFacesPos.x, interiorFacesPos.y, interiorFacesPos.z);
      renderer.setOverrideBlockTexture(this.blockIcon);
      this.isRenderingInterior = true;
      boolean bReturnValue = transformedModel.renderAsBlockWithColorMultiplier(renderer, this, i, j, k);
      this.isRenderingInterior = false;
      renderer.clearOverrideBlockTexture();
      return bReturnValue;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderer, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         TileEntity tileEntity = renderer.blockAccess.getBlockTileEntity(i, j, k);
         if (tileEntity instanceof OvenTileEntity) {
            int iFuelLevel = ((OvenTileEntity)tileEntity).getVisualFuelLevel();
            if (iFuelLevel > 0) {
               iFuelLevel = MathHelper.clamp_int(iFuelLevel - 2, 0, 8);
               this.currentFuelOverlay = this.fuelOverlays[iFuelLevel];
               renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
               renderer.renderStandardBlock(this, i, j, k);
               this.currentFuelOverlay = null;
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return this.isRenderingInterior ? this.interiorBrightness : super.e(par1IBlockAccess, par2, par3, par4);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlockWithTexture(RenderBlocks renderer, int i, int j, int k, Icon texture) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.setOverrideBlockTexture(texture);
      boolean bReturnValue = renderer.renderStandardBlock(this, i, j, k);
      renderer.clearOverrideBlockTexture();
      return bReturnValue;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.renderBlockAsItemVanilla(this, iItemDamage, fBrightness);
      BlockModel transformedModel = this.modelBlockInterior.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(3);
      transformedModel.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (this.isActive) {
         OvenTileEntity tileEntity = (OvenTileEntity)world.getBlockTileEntity(i, j, k);
         int iFuelLevel = tileEntity.getVisualFuelLevel();
         if (iFuelLevel == 1) {
            int iFacing = world.getBlockMetadata(i, j, k) & 7;
            float fX = i + 0.5F;
            float fY = j + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float fZ = k + 0.5F;
            float fFacingOffset = 0.52F;
            float fRandOffset = rand.nextFloat() * 0.6F - 0.3F;
            if (iFacing == 4) {
               world.spawnParticle("largesmoke", fX - fFacingOffset, fY, fZ + fRandOffset, 0.0, 0.0, 0.0);
            } else if (iFacing == 5) {
               world.spawnParticle("largesmoke", fX + fFacingOffset, fY, fZ + fRandOffset, 0.0, 0.0, 0.0);
            } else if (iFacing == 2) {
               world.spawnParticle("largesmoke", fX + fRandOffset, fY, fZ - fFacingOffset, 0.0, 0.0, 0.0);
            } else if (iFacing == 3) {
               world.spawnParticle("largesmoke", fX + fRandOffset, fY, fZ + fFacingOffset, 0.0, 0.0, 0.0);
            }
         }

         ItemStack cookStack = tileEntity.getCookStack();
         if (cookStack != null && this.isValidCookItem(cookStack)) {
            for (int iTempCount = 0; iTempCount < 1; iTempCount++) {
               float fX = i + 0.375F + rand.nextFloat() * 0.25F;
               float fY = j + 0.45F + rand.nextFloat() * 0.1F;
               float fZ = k + 0.375F + rand.nextFloat() * 0.25F;
               world.spawnParticle("fcwhitecloud", fX, fY, fZ, 0.0, 0.0, 0.0);
            }
         }
      }

      super.randomDisplayTick(world, i, j, k, rand);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      this.renderBlock(renderBlocks, i, j, k);
   }
}
