package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import btw.item.blockitems.WoodMouldingDecorativeStubBlockItem;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class WoodMouldingAndDecorativeBlock extends MouldingAndDecorativeBlock {
   public static final int OAK_TABLE_TOP_TEXTURE_ID = 93;
   public static final int OAK_TABLE_LEG_TEXTURE_ID = 94;

   public WoodMouldingAndDecorativeBlock(int iBlockID, String sTextureName, String sColumnSideTextureName, int iMatchingCornerBlockID, String name) {
      super(iBlockID, BTWBlocks.plankMaterial, sTextureName, sColumnSideTextureName, iMatchingCornerBlockID, 2.0F, 5.0F, Block.soundWoodFootstep, name);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.setFurnaceBurnTime(FurnaceBurnTime.PLANKS_OAK.burnTime / 4);
      this.setFireProperties(5, 20);
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return this.isDecorative(iMetadata) ? BTWItems.woodMouldingDecorativeStubID : BTWItems.woodMouldingStubID;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.damageDropped(this.blockID, metadata);
   }

   @Override
   public boolean doesTableHaveLeg(IBlockAccess blockAccess, int i, int j, int k) {
      if (this.blockID == BTWBlocks.oakWoodMouldingAndDecorative.blockID) {
         int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
         if (iBlockBelowID == Block.fence.blockID) {
            return true;
         }
      }

      return super.doesTableHaveLeg(blockAccess, i, j, k);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      world.playAuxSFX(2271, i, j, k, 0);
      int iNumDropped = this.getNumSawDustDroppedForType(iMetadata);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, iNumDropped, 0, 1.0F);
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

      int iNumDropped = this.getNumSawDustDroppedForType(world.getBlockMetadata(i, j, k));
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, iNumDropped, 0, fChanceOfPileDrop);
   }

   protected int getWoodTypeFromBlockID(int blockID) {
      int woodType;
      if (blockID == BTWBlocks.oakWoodMouldingAndDecorative.blockID) {
         woodType = 0;
      } else if (blockID == BTWBlocks.spruceWoodMouldingAndDecorative.blockID) {
         woodType = 1;
      } else if (blockID == BTWBlocks.birchWoodMouldingAndDecorative.blockID) {
         woodType = 2;
      } else if (blockID == BTWBlocks.jungleWoodMouldingAndDecorative.blockID) {
         woodType = 3;
      } else {
         woodType = 4;
      }

      return woodType;
   }

   public int getNumSawDustDroppedForType(int iMetadata) {
      return this.isDecorative(iMetadata) ? 2 : 1;
   }

   private int damageDropped(int blockID, int metadata) {
      int woodType = this.getWoodTypeFromBlockID(blockID);
      if (!this.isDecorative(metadata)) {
         return woodType;
      } else {
         int iBlockType;
         if (metadata == 12) {
            iBlockType = 0;
         } else if (metadata != 13 && metadata != 14) {
            iBlockType = 2;
         } else {
            iBlockType = 1;
         }

         return WoodMouldingDecorativeStubBlockItem.getItemDamageForType(woodType, iBlockType);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return iMetadata == 15 && this.blockID == BTWBlocks.oakWoodMouldingAndDecorative.blockID
         ? renderOakTable(renderBlocks, blockAccess, i, j, k, this)
         : super.renderBlock(renderBlocks, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderOakTable(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      MouldingAndDecorativeBlock tableBlock = (MouldingAndDecorativeBlock)block;
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, ((AestheticNonOpaqueBlock)BTWBlocks.aestheticNonOpaque).iconTableWoodOakTop);
      if (tableBlock.doesTableHaveLeg(blockAccess, i, j, k)) {
         renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.875, 0.625);
         RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, ((AestheticNonOpaqueBlock)BTWBlocks.aestheticNonOpaque).iconTableWoodOakLeg);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      if (this.blockID == BTWItems.woodMouldingDecorativeStubID) {
         int iItemType = WoodMouldingDecorativeStubBlockItem.getBlockType(iItemDamage);
         int iWoodType = WoodMouldingDecorativeStubBlockItem.getWoodType(iItemDamage);
         byte var7;
         if (iItemType == 0) {
            var7 = 12;
         } else if (iItemType == 1) {
            var7 = 13;
         } else {
            var7 = 15;
         }

         Block woodTexture;
         if (iWoodType == 0) {
            woodTexture = BTWBlocks.oakWoodMouldingAndDecorative;
         } else if (iWoodType == 1) {
            woodTexture = BTWBlocks.spruceWoodMouldingAndDecorative;
         } else if (iWoodType == 2) {
            woodTexture = BTWBlocks.birchWoodMouldingAndDecorative;
         } else if (iWoodType == 3) {
            woodTexture = BTWBlocks.jungleWoodMouldingAndDecorative;
         } else {
            woodTexture = BTWBlocks.bloodWoodMouldingAndDecorative;
         }

         this.renderDecorativeInvBlock(renderBlocks, woodTexture, var7, fBrightness);
      } else {
         renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolForItemRender(iItemDamage));
         Icon woodTexture;
         switch (iItemDamage) {
            case 1:
               woodTexture = BTWBlocks.spruceWoodMouldingAndDecorative.blockIcon;
               break;
            case 2:
               woodTexture = BTWBlocks.birchWoodMouldingAndDecorative.blockIcon;
               break;
            case 3:
               woodTexture = BTWBlocks.jungleWoodMouldingAndDecorative.blockIcon;
               break;
            case 4:
               woodTexture = BTWBlocks.bloodWoodMouldingAndDecorative.blockIcon;
               break;
            default:
               woodTexture = BTWBlocks.oakWoodMouldingAndDecorative.blockIcon;
         }

         RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, woodTexture);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      int blockID = world.getBlockId(x, y, z);
      int metadata = world.getBlockMetadata(x, y, z);
      return this.damageDropped(blockID, metadata);
   }
}
