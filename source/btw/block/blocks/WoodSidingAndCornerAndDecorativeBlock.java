package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.blockitems.WoodSidingDecorativeStubBlockItem;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class WoodSidingAndCornerAndDecorativeBlock extends SidingAndCornerAndDecorativeBlock {
   public WoodSidingAndCornerAndDecorativeBlock(int iBlockID, String sTextureName, String name) {
      super(iBlockID, BTWBlocks.plankMaterial, sTextureName, 2.0F, 5.0F, Block.soundWoodFootstep, name);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.setFireProperties(5, 20);
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      if (isDecorativeFromMetadata(iMetadata)) {
         return BTWItems.woodSidingDecorativeStubID;
      } else {
         return this.getIsCorner(iMetadata) ? BTWItems.woodCornerStubID : BTWItems.woodSidingStubID;
      }
   }

   @Override
   public int damageDropped(int metadata) {
      return this.damageDropped(this.blockID, metadata);
   }

   @Override
   public boolean doesBenchHaveLeg(IBlockAccess blockAccess, int i, int j, int k) {
      if (this.blockID == BTWBlocks.oakWoodSidingAndCorner.blockID) {
         int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
         if (iBlockBelowID == Block.fence.blockID) {
            return true;
         }
      }

      return super.doesBenchHaveLeg(blockAccess, i, j, k);
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
      if (blockID == BTWBlocks.oakWoodSidingAndCorner.blockID) {
         woodType = 0;
      } else if (blockID == BTWBlocks.spruceWoodSidingAndCorner.blockID) {
         woodType = 1;
      } else if (blockID == BTWBlocks.birchWoodSidingAndCorner.blockID) {
         woodType = 2;
      } else if (blockID == BTWBlocks.jungleWoodSidingAndCorner.blockID) {
         woodType = 3;
      } else {
         woodType = 4;
      }

      return woodType;
   }

   public int getNumSawDustDroppedForType(int iMetadata) {
      return !isDecorativeFromMetadata(iMetadata) && this.getIsCorner(iMetadata) ? 1 : 2;
   }

   private int damageDropped(int blockID, int metadata) {
      int woodType = this.getWoodTypeFromBlockID(blockID);
      if (isDecorativeFromMetadata(metadata)) {
         int blockType;
         if (metadata == 12) {
            blockType = 0;
         } else {
            blockType = 1;
         }

         return WoodSidingDecorativeStubBlockItem.getItemDamageForType(woodType, blockType);
      } else {
         return woodType;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      if (iBlockID == BTWBlocks.oakWoodSidingAndCorner.blockID) {
         list.add(new ItemStack(iBlockID, 1, 12));
         list.add(new ItemStack(iBlockID, 1, 0));
         list.add(new ItemStack(iBlockID, 1, 1));
      } else {
         super.getSubBlocks(iBlockID, creativeTabs, list);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      int blockID = world.getBlockId(x, y, z);
      int metadata = world.getBlockMetadata(x, y, z);
      return this.damageDropped(blockID, metadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int itemDamage, float brightness) {
      int subtype = itemDamage;
      Block block = this;
      int itemType = WoodSidingDecorativeStubBlockItem.getBlockType(itemDamage);
      int woodType = WoodSidingDecorativeStubBlockItem.getWoodType(itemDamage);
      if (this.blockID == BTWItems.woodSidingDecorativeStubID) {
         if (itemType == 0) {
            subtype = 12;
         } else {
            subtype = 14;
         }

         if (woodType == 0) {
            block = BTWBlocks.oakWoodSidingAndCorner;
         } else if (woodType == 1) {
            block = BTWBlocks.spruceWoodSidingAndCorner;
         } else if (woodType == 2) {
            block = BTWBlocks.birchWoodSidingAndCorner;
         } else if (woodType == 3) {
            block = BTWBlocks.jungleWoodSidingAndCorner;
         } else {
            block = BTWBlocks.bloodWoodSidingAndCorner;
         }
      }

      if (subtype == 12) {
         this.renderBenchInvBlock(renderBlocks, block, subtype);
      } else if (subtype == 14) {
         this.renderFenceInvBlock(renderBlocks, block, subtype);
      } else {
         super.renderBlockAsItem(renderBlocks, itemDamage, brightness);
      }
   }
}
