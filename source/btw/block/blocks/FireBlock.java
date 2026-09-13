package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.texture.FireTexture;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFire;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class FireBlock extends BlockFire {
   @Environment(EnvType.CLIENT)
   private Icon[] stokedFireTopTextureArray;

   public FireBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (!this.c(world, i, j, k)) {
         world.setBlockWithNotify(i, j, k, 0);
      } else if (this.doesBlockBelowExtiguishFire(world, i, j, k)) {
         world.setBlockWithNotify(i, j, k, 0);
      } else {
         int iBlockBelowID = world.getBlockId(i, j - 1, k);
         boolean bInfiniteBurn = false;
         if (iBlockBelowID == Block.netherrack.blockID) {
            bInfiniteBurn = true;
         } else if (world.provider.dimensionId == 1 && iBlockBelowID == Block.bedrock.blockID) {
            bInfiniteBurn = true;
         } else if (this.hasInfiniteBurnNeighbor(world, i, j, k)) {
            bInfiniteBurn = true;
         }

         if (bInfiniteBurn
            || !world.isRaining()
            || !world.isRainingAtPos(i, j, k)
               && !world.isRainingAtPos(i - 1, j, k)
               && !world.isRainingAtPos(i + 1, j, k)
               && !world.isRainingAtPos(i, j, k - 1)
               && !world.isRainingAtPos(i, j, k + 1)) {
            if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
               int iMetadata = world.getBlockMetadata(i, j, k);
               if (iMetadata < 15) {
                  world.setBlockMetadata(i, j, k, iMetadata + random.nextInt(3) / 2);
               }

               world.scheduleBlockUpdate(i, j, k, this.blockID, this.a(world) + random.nextInt(10));
               if (!bInfiniteBurn && !this.k(world, i, j, k)) {
                  if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k) || iMetadata > 3) {
                     world.setBlockWithNotify(i, j, k, 0);
                  }
               } else if (!bInfiniteBurn && !this.canBlockCatchFire(world, i, j - 1, k) && iMetadata == 15 && random.nextInt(4) == 0) {
                  world.setBlockWithNotify(i, j, k, 0);
               } else {
                  boolean bHighHumidity = world.isBlockHighHumidity(i, j, k);
                  byte bDestroyModifier = 0;
                  if (bHighHumidity) {
                     bDestroyModifier = -50;
                  }

                  this.tryToDestroyBlockWithFire(world, i + 1, j, k, 300 + bDestroyModifier, random, iMetadata);
                  this.tryToDestroyBlockWithFire(world, i - 1, j, k, 300 + bDestroyModifier, random, iMetadata);
                  this.tryToDestroyBlockWithFire(world, i, j - 1, k, 250 + bDestroyModifier, random, iMetadata);
                  this.tryToDestroyBlockWithFire(world, i, j + 1, k, 250 + bDestroyModifier, random, iMetadata);
                  this.tryToDestroyBlockWithFire(world, i, j, k - 1, 300 + bDestroyModifier, random, iMetadata);
                  this.tryToDestroyBlockWithFire(world, i, j, k + 1, 300 + bDestroyModifier, random, iMetadata);
                  checkForFireSpreadFromLocation(world, i, j, k, random, iMetadata);
               }
            }
         } else {
            world.setBlockWithNotify(i, j, k, 0);
         }
      }
   }

   @Override
   public boolean canBlockCatchFire(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      if (iBlockID == BTWBlocks.aestheticOpaque.blockID) {
         int iSubtype = blockAccess.getBlockMetadata(i, j, k);
         if (iSubtype == 3) {
            return true;
         }
      }

      return super.canBlockCatchFire(blockAccess, i, j, k);
   }

   @Override
   public boolean getDoesFireDamageToEntities(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   public boolean hasInfiniteBurnNeighbor(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iTempFacing);
         int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         Block targetBlock = Block.blocksList[iTargetBlockID];
         if (targetBlock != null && targetBlock.doesInfiniteBurnToFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iTempFacing))) {
            return true;
         }
      }

      return false;
   }

   public boolean doesBlockBelowExtiguishFire(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      return blockBelow != null ? blockBelow.doesExtinguishFireAbove(world, i, j - 1, k) : false;
   }

   protected void tryToDestroyBlockWithFire(World world, int i, int j, int k, int iChanceToDestroy, Random random, int iSourceMetadata) {
      int iAbilityToCatchFire = unlocalizedName[world.getBlockId(i, j, k)];
      if (random.nextInt(iChanceToDestroy) < iAbilityToCatchFire && world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         onBlockDestroyedByFire(world, i, j, k, iSourceMetadata, false);
      }
   }

   protected static void onBlockDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      if (block != null) {
         block.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
      }
   }

   public static void checkForFireSpreadFromLocation(World world, int i, int j, int k, Random rand, int iSourceFireAge) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         boolean bHighHumidity = world.isBlockHighHumidity(i, j, k);

         for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
            for (int iTempK = k - 1; iTempK <= k + 1; iTempK++) {
               for (int iTempJ = j - 1; iTempJ <= j + 4; iTempJ++) {
                  if (iTempI != i || iTempJ != j || iTempK != k) {
                     int iSpreadTopBound = 100;
                     if (iTempJ > j + 1) {
                        iSpreadTopBound += (iTempJ - (j + 1)) * 100;
                     }

                     checkForFireSpreadToOneBlockLocation(world, iTempI, iTempJ, iTempK, rand, iSourceFireAge, bHighHumidity, iSpreadTopBound);
                  }
               }
            }
         }
      }
   }

   public static void checkForSmoulderingSpreadFromLocation(World world, int i, int j, int k) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         boolean bHighHumidity = world.isBlockHighHumidity(i, j, k);

         for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
            for (int iTempK = k - 1; iTempK <= k + 1; iTempK++) {
               for (int iTempJ = j; iTempJ <= j + 1; iTempJ++) {
                  if (iTempI != i || iTempJ != j || iTempK != k) {
                     int iSpreadTopBound = 50;
                     checkForFireSpreadToOneBlockLocation(world, iTempI, iTempJ, iTempK, world.rand, 0, bHighHumidity, iSpreadTopBound);
                  }
               }
            }
         }
      }
   }

   public static boolean hasFlammableNeighborsWithinSmoulderRange(World world, int i, int j, int k) {
      for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
         for (int iTempK = k - 1; iTempK <= k + 1; iTempK++) {
            for (int iTempJ = j; iTempJ <= j + 1; iTempJ++) {
               if ((iTempI != i || iTempJ != j || iTempK != k) && isFlammableOrHasFlammableNeighbors(world, iTempI, iTempJ, iTempK)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean isFlammableOrHasFlammableNeighbors(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return unlocalizedName[iBlockID] > 0
         || displayOnCreativeTab[iBlockID] > 0
         || block != null && block.getCanBeSetOnFireDirectly(world, i, j, k)
         || canFireReplaceBlock(world, i, j, k)
            && (
               displayOnCreativeTab[world.getBlockId(i - 1, j, k)] > 0
                  || displayOnCreativeTab[world.getBlockId(i + 1, j, k)] > 0
                  || displayOnCreativeTab[world.getBlockId(i, j - 1, k)] > 0
                  || displayOnCreativeTab[world.getBlockId(i, j + 1, k)] > 0
                  || displayOnCreativeTab[world.getBlockId(i, j, k - 1)] > 0
                  || displayOnCreativeTab[world.getBlockId(i, j, k + 1)] > 0
            );
   }

   private static void checkForFireSpreadToOneBlockLocation(
      World world, int iTempI, int iTempJ, int iTempK, Random rand, int iSourceFireAge, boolean bHighHumidity, int iSpreadTopBound
   ) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         int iNeighborChance = getChanceOfNeighborsEncouragingFireCustom(world, iTempI, iTempJ, iTempK);
         if (iNeighborChance > 0) {
            int iSpreadChance = (iNeighborChance + 61) / (iSourceFireAge + 30);
            if (bHighHumidity) {
               iSpreadChance /= 2;
            }

            if (iSpreadChance > 0
               && rand.nextInt(iSpreadTopBound) <= iSpreadChance
               && (!world.isRaining() || !world.isRainingAtPos(iTempI, iTempJ, iTempK))
               && !world.isRainingAtPos(iTempI - 1, iTempJ, iTempK)
               && !world.isRainingAtPos(iTempI + 1, iTempJ, iTempK)
               && !world.isRainingAtPos(iTempI, iTempJ, iTempK - 1)
               && !world.isRainingAtPos(iTempI, iTempJ, iTempK + 1)) {
               int iStartMetadata = iSourceFireAge + rand.nextInt(5) / 4;
               if (iStartMetadata > 15) {
                  iStartMetadata = 15;
               }

               if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
                  if (canFireReplaceBlock(world, iTempI, iTempJ, iTempK)) {
                     world.setBlockAndMetadataWithNotify(iTempI, iTempJ, iTempK, Block.fire.blockID, iStartMetadata);
                  } else {
                     Block block = Block.blocksList[world.getBlockId(iTempI, iTempJ, iTempK)];
                     if (block != null && block.getCanBeSetOnFireDirectly(world, iTempI, iTempJ, iTempK)) {
                        block.setOnFireDirectly(world, iTempI, iTempJ, iTempK);
                     }
                  }
               }
            }
         }
      }
   }

   public static void checkForFireSpreadAndDestructionToOneBlockLocation(World world, int i, int j, int k) {
      checkForFireSpreadAndDestructionToOneBlockLocation(world, i, j, k, world.rand, 0, 100);
   }

   public static void checkForFireSpreadAndDestructionToOneBlockLocation(World world, int i, int j, int k, Random rand, int iSourceFireAge, int iSpreadTopBound) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         int iAbilityToCatchFire = unlocalizedName[world.getBlockId(i, j, k)];
         boolean bHighHumidity = world.isBlockHighHumidity(i, j, k);
         int iChanceToDestroy = 250;
         if (bHighHumidity) {
            iChanceToDestroy -= 50;
         }

         if (rand.nextInt(iChanceToDestroy) < iAbilityToCatchFire) {
            onBlockDestroyedByFire(world, i, j, k, iSourceFireAge, true);
         } else {
            checkForFireSpreadToOneBlockLocation(world, i, j, k, rand, iSourceFireAge, bHighHumidity, iSpreadTopBound);
         }
      }
   }

   protected static int getChanceOfNeighborsEncouragingFireCustom(World world, int i, int j, int k) {
      if (!canFireReplaceBlock(world, i, j, k)) {
         Block block = Block.blocksList[world.getBlockId(i, j, k)];
         return block != null && block.getCanBeSetOnFireDirectly(world, i, j, k) ? block.getChanceOfFireSpreadingDirectlyTo(world, i, j, k) : 0;
      } else {
         int iChance = getChanceToEncourageFire(world, i + 1, j, k, 0);
         iChance = getChanceToEncourageFire(world, i - 1, j, k, iChance);
         iChance = getChanceToEncourageFire(world, i, j - 1, k, iChance);
         iChance = getChanceToEncourageFire(world, i, j + 1, k, iChance);
         iChance = getChanceToEncourageFire(world, i, j, k - 1, iChance);
         return getChanceToEncourageFire(world, i, j, k + 1, iChance);
      }
   }

   public static int getChanceToEncourageFire(World par1World, int par2, int par3, int par4, int iPrevChance) {
      int iChance = displayOnCreativeTab[par1World.getBlockId(par2, par3, par4)];
      return iChance > iPrevChance ? iChance : iPrevChance;
   }

   public static boolean canBlockBeDestroyedByFire(int iBlockID) {
      return unlocalizedName[iBlockID] > 0;
   }

   public static boolean canFireReplaceBlock(World world, int i, int j, int k) {
      Block block = Block.blocksList[world.getBlockId(i, j, k)];
      return block == null || world.getGameRules().getGameRuleBooleanValue("doFireTick") && block.getCanBlockBeReplacedByFire(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.stokedFireTopTextureArray = new Icon[]{
         register.registerIcon("fcBlockFireStokedTopStub_0", new FireTexture("fcBlockFireStokedTopStub_0", 0)),
         register.registerIcon("fcBlockFireStokedTopStub_1", new FireTexture("fcBlockFireStokedTopStub_1", 1))
      };
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderBlockFire(this, i, j, k);
      if (blockAccess.getBlockId(i, j - 1, k) == BTWBlocks.stokedFire.blockID) {
         Tessellator tessellator = Tessellator.instance;
         Icon texture1 = this.stokedFireTopTextureArray[0];
         Icon texture2 = this.stokedFireTopTextureArray[1];
         if ((i + k & 1) != 0) {
            texture1 = this.stokedFireTopTextureArray[1];
            texture2 = this.stokedFireTopTextureArray[0];
         }

         tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
         tessellator.setBrightness(this.e(blockAccess, i, j, k));
         float fRenderHeight = 1.0F;
         double var18 = i + 0.5 - 0.5;
         double var20 = i + 0.5 + 0.5;
         double var22 = k + 0.5 - 0.5;
         double var24 = k + 0.5 + 0.5;
         double var26 = i + 0.5 - 0.5;
         double var28 = i + 0.5 + 0.5;
         double var30 = k + 0.5 - 0.5;
         double var32 = k + 0.5 + 0.5;
         double dMinU = texture2.getMinU();
         double dMinV = texture2.getMinV();
         double dMaxU = texture2.getMaxU();
         double dMaxV = texture2.getMaxV();
         tessellator.addVertexWithUV(var26, j + fRenderHeight, k + 0, dMinU, dMinV);
         tessellator.addVertexWithUV(var18, j + 0, k + 0, dMinU, dMaxV);
         tessellator.addVertexWithUV(var18, j + 0, k + 1, dMaxU, dMaxV);
         tessellator.addVertexWithUV(var26, j + fRenderHeight, k + 1, dMaxU, dMinV);
         tessellator.addVertexWithUV(var28, j + fRenderHeight, k + 1, dMinU, dMinV);
         tessellator.addVertexWithUV(var20, j + 0, k + 1, dMinU, dMaxV);
         tessellator.addVertexWithUV(var20, j + 0, k + 0, dMaxU, dMaxV);
         tessellator.addVertexWithUV(var28, j + fRenderHeight, k + 0, dMaxU, dMinV);
         dMinU = texture1.getMinU();
         dMinV = texture1.getMinV();
         dMaxU = texture1.getMaxU();
         dMaxV = texture1.getMaxV();
         tessellator.addVertexWithUV(i + 0, j + fRenderHeight, var32, dMinU, dMinV);
         tessellator.addVertexWithUV(i + 0, j + 0, var24, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + 1, j + 0, var24, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + 1, j + fRenderHeight, var32, dMaxU, dMinV);
         tessellator.addVertexWithUV(i + 1, j + fRenderHeight, var30, dMinU, dMinV);
         tessellator.addVertexWithUV(i + 1, j + 0, var22, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + 0, j + 0, var22, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + 0, j + fRenderHeight, var30, dMaxU, dMinV);
      }

      return true;
   }
}
