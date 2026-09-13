package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.util.CustomDamageSource;
import btw.world.util.BlockPos;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemFood;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class NetherGrothBlock extends Block {
   private static final float BLOCK_HARDNESS = 0.2F;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconTopGrown;

   public NetherGrothBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.netherGrothMaterial);
      this.c(0.2F);
      this.setAxesEffectiveOn(true);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockGroth");
      this.b(true);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      if (iBlockBelowID == Block.netherrack.blockID) {
         world.setBlockAndMetadata(i, j - 1, k, BTWBlocks.aestheticOpaque.blockID, 8);
      }
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      int iHeight = this.getHeightLevel(world, i, j, k);
      if (iHeight == 7) {
         this.releaseSpores(world, i, j, k);
      }

      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
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
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      double dHeight = (this.getHeightLevel(blockAccess, i, j, k) + 1) / 16.0;
      return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, dHeight, 1.0);
   }

   @Override
   public int getMobilityFlag() {
      return 1;
   }

   @Override
   public int quantityDropped(Random random) {
      return 0;
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return 0;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iChangedBlockID) {
      if (!this.canPlaceBlockAt(world, i, j, k)) {
         world.playAuxSFX(2001, i, j, k, this.blockID + (world.getBlockMetadata(i, j, k) << 12));
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      int iHeight = this.getHeightLevel(world, i, j, k);
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      boolean bOnNetherrack = false;
      if (iBlockBelowID == Block.netherrack.blockID) {
         world.setBlockAndMetadata(i, j - 1, k, BTWBlocks.aestheticOpaque.blockID, 8);
         bOnNetherrack = true;
      } else if (iBlockBelowID == BTWBlocks.aestheticOpaque.blockID) {
         int iSubtype = world.getBlockMetadata(i, j - 1, k);
         if (iSubtype == 8) {
            bOnNetherrack = true;
         }
      }

      if (iHeight < 7) {
         boolean bGrow = false;
         if (bOnNetherrack) {
            bGrow = true;
         } else if (this.getMaxHeightOfNeighbors(world, i, j, k) > iHeight + 1) {
            bGrow = true;
         }

         if (bGrow) {
            this.setHeightLevel(world, i, j, k, ++iHeight);
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         }
      }

      if (iHeight >= 1) {
         int iFacing = random.nextInt(4) + 2;
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iFacing);
         if (this.isBlockOpenToSpread(world, targetPos.x, targetPos.y, targetPos.z)) {
            if (world.doesBlockHaveSolidTopSurface(targetPos.x, targetPos.y - 1, targetPos.z)) {
               this.spreadToBlock(world, targetPos.x, targetPos.y, targetPos.z);
            } else if (bOnNetherrack) {
               targetPos.y--;
               if (this.isBlockOpenToSpread(world, targetPos.x, targetPos.y, targetPos.z)
                  && world.doesBlockHaveSolidTopSurface(targetPos.x, targetPos.y - 1, targetPos.z)) {
                  this.spreadToBlock(world, targetPos.x, targetPos.y, targetPos.z);
               }
            }
         } else if (world.isAirBlock(i, j + 1, k) && world.getBlockId(targetPos.x, targetPos.y, targetPos.z) == Block.netherrack.blockID) {
            targetPos.y++;
            if (this.isBlockOpenToSpread(world, targetPos.x, targetPos.y, targetPos.z)) {
               this.spreadToBlock(world, targetPos.x, targetPos.y, targetPos.z);
            }
         }
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!entity.isDead && !world.isRemote) {
         int iHeight = this.getHeightLevel(world, i, j, k);
         if (iHeight >= 7) {
            if (entity instanceof EntityLiving) {
               boolean bAttack = true;
               if (entity instanceof EntityPlayer) {
                  EntityPlayer player = (EntityPlayer)entity;
                  if (player.isWearingSoulforgedBoots()) {
                     bAttack = false;
                  }
               }

               if (bAttack && entity.attackEntityFrom(CustomDamageSource.damageSourceGroth, 2)) {
                  entity.isAirBorne = true;
                  entity.motionY += 0.84;
                  world.playAuxSFX(2225, i, j, k, 0);
               }
            } else if (entity instanceof EntityItem) {
               EntityItem entityItem = (EntityItem)entity;
               if (entityItem.delayBeforeCanPickup <= 0
                  && (
                     entityItem.getEntityItem().getItem() instanceof ItemFood
                        || entityItem.getEntityItem().itemID == Block.mushroomRed.blockID
                        || entityItem.getEntityItem().itemID == Block.mushroomBrown.blockID
                  )) {
                  entityItem.w();
                  world.playAuxSFX(2226, i, j, k, 0);
               }
            }
         }
      }
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 0.8F;
   }

   public int getHeightLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHeightLevelFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getHeightLevelFromMetadata(int iMetdata) {
      return iMetdata & 7;
   }

   public void setHeightLevel(World world, int i, int j, int k, int iHeight) {
      int iMetadata = world.getBlockMetadata(i, j, k) & 8;
      iMetadata |= iHeight;
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   private int getMaxHeightOfNeighbors(World world, int i, int j, int k) {
      int iMaxHeight = -1;

      for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
         BlockPos tempPos = new BlockPos(i, j, k);
         tempPos.addFacingAsOffset(iTempFacing);
         if (world.getBlockId(tempPos.x, tempPos.y, tempPos.z) == this.blockID) {
            int iTempHeight = this.getHeightLevel(world, tempPos.x, tempPos.y, tempPos.z);
            if (iTempHeight > iMaxHeight) {
               iMaxHeight = iTempHeight;
            }
         }
      }

      return iMaxHeight;
   }

   private void spreadToBlock(World world, int i, int j, int k) {
      if (world.getBlockId(i, j, k) == Block.fire.blockID) {
         world.playAuxSFX(2227, i, j, k, 0);
      } else if (world.getBlockId(i, j, k) == Block.mushroomBrown.blockID || world.getBlockId(i, j, k) == Block.mushroomRed.blockID) {
         world.playAuxSFX(2226, i, j, k, 0);
      }

      if (world.setBlockWithNotify(i, j, k, this.blockID)) {
         world.playAuxSFX(2228, i, j, k, 0);
      }
   }

   private boolean isBlockOpenToSpread(World world, int i, int j, int k) {
      if (world.isAirBlock(i, j, k)) {
         return true;
      } else {
         int iBlockID = world.getBlockId(i, j, k);
         return iBlockID == Block.fire.blockID || iBlockID == Block.mushroomRed.blockID || iBlockID == Block.mushroomBrown.blockID;
      }
   }

   private void releaseSpores(World world, int i, int j, int k) {
      world.playAuxSFX(2224, i, j, k, 0);

      for (int iTempI = i - 3; iTempI <= i + 3; iTempI++) {
         for (int iTempJ = j - 3; iTempJ <= j + 3; iTempJ++) {
            for (int iTempK = k - 3; iTempK <= k + 3; iTempK++) {
               if ((iTempI != i || iTempJ != j || iTempK != k)
                  && this.isBlockOpenToSpread(world, iTempI, iTempJ, iTempK)
                  && world.doesBlockHaveSolidTopSurface(iTempI, iTempJ - 1, iTempK)
                  && world.rand.nextInt(2) == 0) {
                  world.setBlockWithNotify(iTempI, iTempJ, iTempK, this.blockID);
               }
            }
         }
      }

      double posX = i + 0.5;
      double posY = j + 0.5;
      double posZ = k + 0.5;
      List list = world.getEntitiesWithinAABB(
         EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(posX - 5.0, posY - 5.0, posZ - 5.0, posX + 5.0, posY + 5.0, posZ + 5.0)
      );
      if (list != null && list.size() > 0) {
         for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            EntityLiving targetEntity = (EntityLiving)list.get(listIndex);
            boolean bDamageEntity = true;
            if (targetEntity instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)targetEntity;
               if (player.isWearingFullSuitSoulforgedArmor()) {
                  bDamageEntity = false;
               }
            }

            if (bDamageEntity) {
               targetEntity.attackEntityFrom(CustomDamageSource.damageSourceGrothSpores, 4);
               targetEntity.addPotionEffect(new PotionEffect(Potion.poison.id, 300, 0));
            }
         }
      }
   }

   @Override
   public boolean attemptToAffectBlockWithSoul(World world, int x, int y, int z) {
      int iHeightLevel = this.getHeightLevel(world, x, y, z);
      if (iHeightLevel < 7) {
         this.setHeightLevel(world, x, y, z, 7);
         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon bottomIcon = register.registerIcon("fcBlockGroth_bottom");
      this.blockIcon = bottomIcon;
      this.iconBySideArray[0] = bottomIcon;
      this.iconBySideArray[1] = register.registerIcon("fcBlockGroth_top");
      Icon sideIcon = register.registerIcon("fcBlockGroth_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
      this.iconTopGrown = register.registerIcon("fcBlockGroth_top_grown");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide == 1) {
         int iHeight = this.getHeightLevelFromMetadata(iMetadata);
         if (iHeight >= 7) {
            return this.iconTopGrown;
         }
      }

      return this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      super.randomDisplayTick(world, i, j, k, random);
      if (random.nextInt(10) == 0) {
         float fHeight = (this.getHeightLevel(world, i, j, k) + 1) / 16.0F;
         world.spawnParticle("townaura", i + random.nextFloat(), j + fHeight + 0.1F, k + random.nextFloat(), 0.0, 0.0, 0.0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (iSide == 0) {
         return false;
      } else {
         if (iSide != 1) {
            int iNeighborBlockID = blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK);
            if (iNeighborBlockID == this.blockID) {
               int iNeighborHeightLevel = this.getHeightLevel(blockAccess, iNeighborI, iNeighborJ, iNeighborK);
               if (iNeighborHeightLevel >= 7) {
                  return false;
               }

               BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, Block.getOppositeFacing(iSide));
               int iMyHeightLevel = this.getHeightLevel(blockAccess, myPos.x, myPos.y, myPos.z);
               if (iNeighborHeightLevel >= iMyHeightLevel) {
                  return false;
               }
            } else {
               Block neighborBlock = Block.blocksList[iNeighborBlockID];
               if (neighborBlock != null) {
                  return neighborBlock.shouldRenderNeighborHalfSlabSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide, false);
               }
            }
         }

         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown) {
      return bNeighborUpsideDown || this.getHeightLevel(blockAccess, i, j, k) < 7;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
      return iNeighborSide != 1;
   }
}
