package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.block.model.CampfireModel;
import btw.block.tileentity.CampfireTileEntity;
import btw.client.render.util.RenderUtils;
import btw.crafting.manager.CampfireCraftingManager;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class CampfireBlock extends BlockContainer {
   public final int fireLevel;
   public static final int CAMPFIRE_FUEL_STATE_NORMAL = 0;
   public static final int CAMPFIRE_FUEL_STATE_BURNED_OUT = 1;
   public static final int CAMPFIRE_FUEL_STATE_SMOULDERING = 2;
   private CampfireModel modelCampfire = new CampfireModel();
   private BlockModel modelCollisionBase;
   private BlockModel modelCollisionWithSpit;
   public static CampfireBlock[] fireLevelBlockArray = new CampfireBlock[4];
   public static boolean campfireChangingState = false;
   private static final float SPIT_THICKNESS = 0.0625F;
   private static final float HALF_SPIT_THICKNESS = 0.03125F;
   private static final float SPIT_HEIGHT = 0.75F;
   private static final float SPIT_MIN_Y = 0.71875F;
   private static final float SPIT_MAX_Y = 0.78125F;
   private static final float SPIT_SUPPORT_WIDTH = 0.0625F;
   private static final float HALF_SPIT_SUPPORT_WIDTH = 0.03125F;
   private static final float SPIT_SUPPORT_BORDER = 0.03125F;
   private static final float SPIT_FORK_WIDTH = 0.0625F;
   private static final float SPIT_FORK_HEIGHT = 0.1875F;
   private static final float SPIT_FORK_HEIGHT_OFFSET = 0.0625F;
   private static final float SPIT_FORK_MIN_Y = 0.65625F;
   private static final float SPIT_FORK_MAX_Y = 0.84375F;
   private static final double SPIT_COLLISION_HEIGHT = 0.84375;
   private static final double SPIT_COLLISION_WIDTH = 0.1875;
   private static final double SPIT_COLLISION_HALF_WIDTH = 0.09375;
   @Environment(EnvType.CLIENT)
   private Icon spitIcon;
   @Environment(EnvType.CLIENT)
   private Icon spitSupportIcon;
   @Environment(EnvType.CLIENT)
   private Icon burnedIcon;
   @Environment(EnvType.CLIENT)
   private Icon embersIcon;
   @Environment(EnvType.CLIENT)
   static final double[] fireAnimationScaleArray = new double[]{0.0, 0.25, 0.5, 0.875};

   public CampfireBlock(int iBlockID, int iFireLevel) {
      super(iBlockID, Material.circuits);
      this.fireLevel = iFireLevel;
      fireLevelBlockArray[iFireLevel] = this;
      this.c(0.1F);
      this.setBuoyant();
      this.setFurnaceBurnTime(4 * FurnaceBurnTime.SHAFT.burnTime);
      this.a(g);
      this.setAlwaysStartlesAnimals();
      this.c("fcBlockCampfire");
      this.initModels();
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new CampfireTileEntity();
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      if (!campfireChangingState) {
         CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
         tileEntity.ejectContents();
         super.breakBlock(world, i, j, k, iBlockID, iMetadata);
      }
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
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHasSpit(blockAccess, i, j, k)
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.84375, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true) ? false : super.c(world, i, j, k);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setIAligned(iMetadata, this.isFacingIAligned(iFacing));
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityLiving);
      this.setIAligned(world, i, j, k, this.isFacingIAligned(iFacing));
      world.notifyNearbyAnimalsOfPlayerBlockAddOrRemove((EntityPlayer)entityLiving, this, i, j, k);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return this.fireLevel == 0 && this.getFuelState(iMetadata) == 0 ? super.a(iMetadata, rand, iFortuneModifier) : 0;
   }

   @Override
   public int tickRate(World world) {
      return 2;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         if (this.fireLevel > 0) {
            world.playAuxSFX(2227, i, j, k, 1);
         }

         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return this.fireLevel == 0 && this.getFuelState(blockAccess, i, j, k) == 0;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      if (!this.getCanBeSetOnFireDirectly(world, i, j, k)) {
         return false;
      } else {
         if (!this.isRainingOnCampfire(world, i, j, k)) {
            this.changeFireLevel(world, i, j, k, 1, world.getBlockMetadata(i, j, k));
            CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
            tileEntity.onFirstLit();
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
            if (!Block.portal.tryToCreatePortal(world, i, j, k)) {
               int iBlockBelowID = world.getBlockId(i, j - 1, k);
               if (iBlockBelowID == Block.netherrack.blockID || iBlockBelowID == BTWBlocks.fallingNetherrack.blockID) {
                  world.setBlockWithNotify(i, j, k, Block.fire.blockID);
               }
            }
         } else {
            world.playAuxSFX(2227, i, j, k, 0);
         }

         return true;
      }
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return this.fireLevel == 0 && this.getFuelState(blockAccess, i, j, k) == 0 ? 60 : 0;
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      ItemStack stack = player.getCurrentEquippedItem();
      if (stack != null) {
         Item item = stack.getItem();
         if (!this.getHasSpit(world, i, j, k)) {
            if (item == BTWItems.pointyStick) {
               this.setHasSpit(world, i, j, k, true);
               CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
               tileEntity.setSpitStack(stack);
               stack.stackSize--;
               return true;
            }
         } else {
            CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
            ItemStack cookStack = tileEntity.getCookStack();
            if (cookStack == null) {
               if (this.isValidCookItem(stack)) {
                  ItemStack spitStack = tileEntity.getSpitStack();
                  if (spitStack.getItemDamage() == 0) {
                     tileEntity.setCookStack(stack);
                  } else {
                     tileEntity.setSpitStack(null);
                     this.setHasSpit(world, i, j, k, false);
                     if (!world.isRemote) {
                        ItemStack ejectStack = stack.copy();
                        ejectStack.stackSize = 1;
                        ItemUtils.ejectStackWithRandomOffset(world, i, j, k, ejectStack);
                        ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWItems.sawDust.itemID, 0);
                        world.playAuxSFX(2271, i, j, k, 0);
                     }
                  }

                  stack.stackSize--;
                  return true;
               }
            } else if (cookStack.itemID == stack.itemID && stack.stackSize < stack.getMaxStackSize()) {
               player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
               stack.stackSize++;
               tileEntity.setCookStack(null);
               return true;
            }
         }

         if (this.fireLevel > 0 || this.getFuelState(world, i, j, k) == 2) {
            int iItemDamage = stack.getItemDamage();
            if (item.getCanBeFedDirectlyIntoCampfire(iItemDamage)) {
               if (!world.isRemote) {
                  CampfireTileEntity tileEntityx = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
                  world.playSoundEffect(
                     i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 0.2F + world.rand.nextFloat() * 0.1F, world.rand.nextFloat() * 0.25F + 1.25F
                  );
                  tileEntityx.addBurnTime(item.getCampfireBurnTime(iItemDamage));
               }

               stack.stackSize--;
               return true;
            }
         }
      } else {
         CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
         ItemStack cookStack = tileEntity.getCookStack();
         if (cookStack != null) {
            ItemUtils.givePlayerStackOrEject(player, cookStack, i, j, k);
            tileEntity.setCookStack(null);
            return true;
         }

         ItemStack spitStack = tileEntity.getSpitStack();
         if (spitStack != null) {
            ItemUtils.givePlayerStackOrEject(player, spitStack, i, j, k);
            tileEntity.setSpitStack(null);
            this.setHasSpit(world, i, j, k, false);
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean shouldDeleteTileEntityOnBlockChange(int iNewBlockID) {
      for (int iTempIndex = 0; iTempIndex < fireLevelBlockArray.length; iTempIndex++) {
         if (fireLevelBlockArray[iTempIndex].blockID == iNewBlockID) {
            return false;
         }
      }

      return true;
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && entity.isEntityAlive() && (this.fireLevel > 0 || this.getFuelState(world, i, j, k) == 2) && entity instanceof EntityItem) {
         EntityItem entityItem = (EntityItem)entity;
         ItemStack targetStack = entityItem.getEntityItem();
         Item item = targetStack.getItem();
         int iBurnTime = item.getCampfireBurnTime(targetStack.getItemDamage());
         if (iBurnTime > 0) {
            iBurnTime *= targetStack.stackSize;
            CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", world.rand.nextFloat() * 0.1F + 0.2F, world.rand.nextFloat() * 0.25F + 1.25F);
            tileEntity.addBurnTime(iBurnTime);
            entity.setDead();
         }
      }
   }

   @Override
   public boolean getDoesFireDamageToEntities(World world, int i, int j, int k, Entity entity) {
      return this.fireLevel > 2 || this.fireLevel == 2 && entity instanceof EntityLiving;
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return this.fireLevel > 0;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      BlockModel collisionModel = this.modelCollisionBase;
      if (this.getHasSpit(iMetadata)) {
         collisionModel = this.modelCollisionWithSpit;
      }

      if (this.getIsIAligned(iMetadata)) {
         collisionModel = collisionModel.makeTemporaryCopy();
         collisionModel.rotateAroundYToFacing(4);
      }

      return collisionModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public void onFluidFlowIntoBlock(World world, int i, int j, int k, BlockFluid newBlock) {
      if (this.fireLevel > 0) {
         world.playAuxSFX(2227, i, j, k, 0);
      }

      super.onFluidFlowIntoBlock(world, i, j, k, newBlock);
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (!world.isRemote && this.fireLevel > 0) {
         world.playAuxSFX(2227, i, j, k, 0);
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return this.getIsIAligned(iMetadata) ? 4 : 2;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return this.setIAligned(iMetadata, this.isFacingIAligned(iFacing));
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      this.setIAligned(world, i, j, k, !this.getIsIAligned(world, i, j, k));
      return true;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      return this.setIAligned(iMetadata, !this.getIsIAligned(iMetadata));
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return this.fireLevel == 0 && world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   public void setIAligned(World world, int i, int j, int k, boolean bIAligned) {
      int iMetadata = this.setIAligned(world.getBlockMetadata(i, j, k), bIAligned);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setIAligned(int iMetadata, boolean bIAligned) {
      if (bIAligned) {
         iMetadata |= 1;
      } else {
         iMetadata &= -2;
      }

      return iMetadata;
   }

   public boolean getIsIAligned(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsIAligned(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsIAligned(int iMetadata) {
      return (iMetadata & 1) != 0;
   }

   public boolean isFacingIAligned(int iFacing) {
      return iFacing >= 4;
   }

   public void setHasSpit(World world, int i, int j, int k, boolean bHasSpit) {
      int iMetadata = this.setHasSpit(world.getBlockMetadata(i, j, k), bHasSpit);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setHasSpit(int iMetadata, boolean bHasSpit) {
      if (bHasSpit) {
         iMetadata |= 2;
      } else {
         iMetadata &= -3;
      }

      return iMetadata;
   }

   public boolean getHasSpit(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHasSpit(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getHasSpit(int iMetadata) {
      return (iMetadata & 2) != 0;
   }

   public void setFuelState(World world, int i, int j, int k, int iCampfireState) {
      int iMetadata = this.setFuelState(world.getBlockMetadata(i, j, k), iCampfireState);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setFuelState(int iMetadata, int iCampfireState) {
      iMetadata &= -13;
      return iMetadata | iCampfireState << 2;
   }

   public int getFuelState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getFuelState(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getFuelState(int iMetadata) {
      return (iMetadata & 12) >> 2;
   }

   public boolean isValidCookItem(ItemStack stack) {
      return CampfireCraftingManager.instance.getRecipeResult(stack.getItem().itemID) != null;
   }

   public void extinguishFire(World world, int i, int j, int k, boolean bSmoulder) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (bSmoulder) {
         iMetadata = this.setFuelState(iMetadata, 2);
      } else {
         iMetadata = this.setFuelState(iMetadata, 1);
      }

      this.changeFireLevel(world, i, j, k, 0, iMetadata);
      if (!world.isRemote) {
         world.playAuxSFX(2227, i, j, k, 1);
      }
   }

   public void relightFire(World world, int i, int j, int k) {
      this.changeFireLevel(world, i, j, k, 1, this.setFuelState(world.getBlockMetadata(i, j, k), 0));
   }

   public void stopSmouldering(World world, int i, int j, int k) {
      this.setFuelState(world, i, j, k, 1);
   }

   public void changeFireLevel(World world, int i, int j, int k, int iFireLevel, int iMetadata) {
      campfireChangingState = true;
      world.setBlockAndMetadataWithNotify(i, j, k, fireLevelBlockArray[iFireLevel].blockID, iMetadata);
      campfireChangingState = false;
   }

   public boolean isRainingOnCampfire(World world, int i, int j, int k) {
      return world.isRainingAtPos(i, j, k);
   }

   private void initModels() {
      this.modelCollisionBase = new BlockModel();
      this.modelCollisionWithSpit = new BlockModel();
      this.modelCollisionBase.addBox(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.modelCollisionWithSpit.addBox(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.modelCollisionWithSpit.addBox(0.0, 0.0, 0.40625, 1.0, 0.84375, 0.59375);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int x, int y, int z) {
      return BTWBlocks.unlitCampfire.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockCampfire");
      this.spitIcon = register.registerIcon("fcBlockCampfire_spit");
      this.spitSupportIcon = register.registerIcon("fcBlockCampfire_support");
      this.burnedIcon = register.registerIcon("fcBlockCampfire_burned");
      this.embersIcon = register.registerIcon("fcOverlayEmbers");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      if (this.renderCampfireModel(renderBlocks, i, j, k)) {
         if (this.getHasSpit(renderBlocks.blockAccess, i, j, k)) {
            this.renderSpit(renderBlocks, i, j, k);
         }

         if (this.fireLevel > 0 && !renderBlocks.hasOverrideBlockTexture()) {
            this.renderFirePortion(renderBlocks, i, j, k);
         }

         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderer, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult && this.fireLevel == 0 && this.getFuelState(renderer.blockAccess, i, j, k) == 2) {
         this.renderCampfireModelEmbers(renderer, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.modelCampfire.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   private boolean renderCampfireModelEmbers(RenderBlocks renderBlocks, int i, int j, int k) {
      int iMetadata = renderBlocks.blockAccess.getBlockMetadata(i, j, k);
      BlockModel transformedModel;
      if (!WorldUtils.isGroundCoverOnBlock(renderBlocks.blockAccess, i, j, k)) {
         transformedModel = this.modelCampfire.makeTemporaryCopy();
      } else {
         transformedModel = this.modelCampfire.modelInSnow.makeTemporaryCopy();
      }

      if (this.getIsIAligned(iMetadata)) {
         transformedModel.rotateAroundYToFacing(4);
      }

      return transformedModel.renderAsBlockFullBrightWithTexture(renderBlocks, this, i, j, k, this.embersIcon);
   }

   @Environment(EnvType.CLIENT)
   private boolean renderCampfireModel(RenderBlocks renderBlocks, int i, int j, int k) {
      int iMetadata = renderBlocks.blockAccess.getBlockMetadata(i, j, k);
      BlockModel transformedModel;
      if (!WorldUtils.isGroundCoverOnBlock(renderBlocks.blockAccess, i, j, k)) {
         transformedModel = this.modelCampfire.makeTemporaryCopy();
      } else {
         transformedModel = this.modelCampfire.modelInSnow.makeTemporaryCopy();
      }

      if (this.getIsIAligned(iMetadata)) {
         transformedModel.rotateAroundYToFacing(4);
      }

      return this.getFuelState(iMetadata) != 0 && !renderBlocks.hasOverrideBlockTexture()
         ? transformedModel.renderAsBlockWithTexture(renderBlocks, this, i, j, k, this.burnedIcon)
         : transformedModel.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   private void renderSpit(RenderBlocks renderBlocks, int i, int j, int k) {
      boolean bIAligned = this.getIsIAligned(renderBlocks.blockAccess, i, j, k);
      RenderUtils.setRenderBoundsWithAxisAlignment(renderBlocks, 0.0F, 0.71875F, 0.46875F, 1.0F, 0.78125F, 0.53125F, bIAligned);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.spitIcon);
      boolean bRenderSupport = true;
      if (bIAligned && !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(renderBlocks.blockAccess, i, j, k - 1, 3)
         || !bIAligned && !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(renderBlocks.blockAccess, i - 1, j, k, 5)) {
         this.renderSpitSupport(renderBlocks, i, j, k, 0.03125F);
      }

      if (bIAligned && !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(renderBlocks.blockAccess, i, j, k + 1, 2)
         || !bIAligned && !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(renderBlocks.blockAccess, i + 1, j, k, 4)) {
         this.renderSpitSupport(renderBlocks, i, j, k, 0.90625F);
      }
   }

   @Environment(EnvType.CLIENT)
   private void renderSpitSupport(RenderBlocks renderBlocks, int i, int j, int k, float fOffset) {
      boolean bIAligned = this.getIsIAligned(renderBlocks.blockAccess, i, j, k);
      RenderUtils.setRenderBoundsWithAxisAlignment(renderBlocks, fOffset, 0.0F, 0.46875F, fOffset + 0.0625F, 0.71875F, 0.53125F, bIAligned);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.spitSupportIcon);
      RenderUtils.setRenderBoundsWithAxisAlignment(renderBlocks, fOffset, 0.65625F, 0.53125F, fOffset + 0.0625F, 0.84375F, 0.59375F, bIAligned);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.spitSupportIcon);
      RenderUtils.setRenderBoundsWithAxisAlignment(renderBlocks, fOffset, 0.65625F, 0.40625F, fOffset + 0.0625F, 0.84375F, 0.46875F, bIAligned);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.spitSupportIcon);
   }

   @Environment(EnvType.CLIENT)
   private void renderFirePortion(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      Tessellator tesselator = Tessellator.instance;
      double dScale = fireAnimationScaleArray[this.fireLevel];
      double dI = i;
      double dJ = j;
      double dK = k;
      Icon fireTexture1 = Block.fire.func_94438_c(0);
      Icon fireTexture2 = Block.fire.func_94438_c(1);
      if ((i + k & 1) != 0) {
         fireTexture1 = Block.fire.func_94438_c(1);
         fireTexture2 = Block.fire.func_94438_c(0);
      }

      tesselator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tesselator.setBrightness(this.e(blockAccess, i, j, k));
      double dMinU = fireTexture1.getMinU();
      double dMinV = fireTexture1.getMinV();
      double dMaxU = fireTexture1.getMaxU();
      double dMaxV = fireTexture1.getMaxV();
      double dFireHeight = 1.4 * dScale;
      double dHorizontalMin = 0.5 - 0.5 * dScale;
      double dHorizontalMax = 0.5 + 0.5 * dScale;
      double dOffset = 0.2 * dScale;
      double var18 = dI + 0.5 + dOffset;
      double var20 = dI + 0.5 - dOffset;
      double var22 = dK + 0.5 + dOffset;
      double var24 = dK + 0.5 - dOffset;
      dOffset = 0.3 * dScale;
      double var26 = dI + 0.5 - dOffset;
      double var28 = dI + 0.5 + dOffset;
      double var30 = dK + 0.5 - dOffset;
      double var32 = dK + 0.5 + dOffset;
      tesselator.addVertexWithUV(var26, dJ + dFireHeight, dK + dHorizontalMax, dMaxU, dMinV);
      tesselator.addVertexWithUV(var18, dJ + 0.0, dK + dHorizontalMax, dMaxU, dMaxV);
      tesselator.addVertexWithUV(var18, dJ + 0.0, dK + dHorizontalMin, dMinU, dMaxV);
      tesselator.addVertexWithUV(var26, dJ + dFireHeight, dK + dHorizontalMin, dMinU, dMinV);
      tesselator.addVertexWithUV(var28, dJ + dFireHeight, dK + dHorizontalMin, dMaxU, dMinV);
      tesselator.addVertexWithUV(var20, dJ + 0.0, dK + dHorizontalMin, dMaxU, dMaxV);
      tesselator.addVertexWithUV(var20, dJ + 0.0, dK + dHorizontalMax, dMinU, dMaxV);
      tesselator.addVertexWithUV(var28, dJ + dFireHeight, dK + dHorizontalMax, dMinU, dMinV);
      dMinU = fireTexture2.getMinU();
      dMinV = fireTexture2.getMinV();
      dMaxU = fireTexture2.getMaxU();
      dMaxV = fireTexture2.getMaxV();
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + dFireHeight, var32, dMaxU, dMinV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + 0.0, var24, dMaxU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + 0.0, var24, dMinU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + dFireHeight, var32, dMinU, dMinV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + dFireHeight, var30, dMaxU, dMinV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + 0.0, var22, dMaxU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + 0.0, var22, dMinU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + dFireHeight, var30, dMinU, dMinV);
      dOffset = 0.5 * dScale;
      var18 = dI + 0.5 - dOffset;
      var20 = dI + 0.5 + dOffset;
      var22 = dK + 0.5 - dOffset;
      var24 = dK + 0.5 + dOffset;
      dOffset = 0.4 * dScale;
      var26 = dI + 0.5 - dOffset;
      var28 = dI + 0.5 + dOffset;
      var30 = dK + 0.5 - dOffset;
      var32 = dK + 0.5 + dOffset;
      tesselator.addVertexWithUV(var26, dJ + dFireHeight, dK + dHorizontalMin, dMinU, dMinV);
      tesselator.addVertexWithUV(var18, dJ + 0.0, dK + dHorizontalMin, dMinU, dMaxV);
      tesselator.addVertexWithUV(var18, dJ + 0.0, dK + dHorizontalMax, dMaxU, dMaxV);
      tesselator.addVertexWithUV(var26, dJ + dFireHeight, dK + dHorizontalMax, dMaxU, dMinV);
      tesselator.addVertexWithUV(var28, dJ + dFireHeight, dK + dHorizontalMax, dMinU, dMinV);
      tesselator.addVertexWithUV(var20, dJ + 0.0, dK + dHorizontalMax, dMinU, dMaxV);
      tesselator.addVertexWithUV(var20, dJ + 0.0, dK + dHorizontalMin, dMaxU, dMaxV);
      tesselator.addVertexWithUV(var28, dJ + dFireHeight, dK + dHorizontalMin, dMaxU, dMinV);
      dMinU = fireTexture1.getMinU();
      dMinV = fireTexture1.getMinV();
      dMaxU = fireTexture1.getMaxU();
      dMaxV = fireTexture1.getMaxV();
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + dFireHeight, var32, dMinU, dMinV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + 0.0, var24, dMinU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + 0.0, var24, dMaxU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + dFireHeight, var32, dMaxU, dMinV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + dFireHeight, var30, dMinU, dMinV);
      tesselator.addVertexWithUV(dI + dHorizontalMax, dJ + 0.0, var22, dMinU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + 0.0, var22, dMaxU, dMaxV);
      tesselator.addVertexWithUV(dI + dHorizontalMin, dJ + dFireHeight, var30, dMaxU, dMinV);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (this.fireLevel > 1) {
         for (int iTempCount = 0; iTempCount < this.fireLevel; iTempCount++) {
            double xPos = i + rand.nextFloat();
            double yPos = j + 0.5F + rand.nextFloat() * 0.5F;
            double zPos = k + rand.nextFloat();
            world.spawnParticle("smoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
         }

         CampfireTileEntity tileEntity = (CampfireTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity.getIsFoodBurning()) {
            for (int iTempCount = 0; iTempCount < 1; iTempCount++) {
               double xPos = i + 0.375F + rand.nextFloat() * 0.25F;
               double yPos = j + 0.5F + rand.nextFloat() * 0.5F;
               double zPos = k + 0.375F + rand.nextFloat() * 0.25F;
               world.spawnParticle("largesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
            }
         } else if (tileEntity.getIsCooking()) {
            for (int iTempCount = 0; iTempCount < 1; iTempCount++) {
               double xPos = i + 0.375F + rand.nextFloat() * 0.25F;
               double yPos = j + 0.5F + rand.nextFloat() * 0.5F;
               double zPos = k + 0.375F + rand.nextFloat() * 0.25F;
               world.spawnParticle("fcwhitesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
            }
         }
      } else if (this.fireLevel == 1 || this.getFuelState(world, i, j, k) == 2) {
         double xPos = i + 0.375 + rand.nextDouble() * 0.25;
         double yPos = j + 0.25 + rand.nextDouble() * 0.25;
         double zPos = k + 0.375 + rand.nextDouble() * 0.25;
         world.spawnParticle("smoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      }

      if (this.fireLevel > 0 && rand.nextInt(24) == 0) {
         float fVolume = this.fireLevel * 0.25F + rand.nextFloat();
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "fire.fire", fVolume, rand.nextFloat() * 0.7F + 0.3F, false);
      }
   }
}
