package btw.block.blocks;

import btw.item.BTWItems;
import btw.item.items.ChiselItem;
import btw.item.items.PickaxeItem;
import btw.item.items.ToolItem;
import btw.item.util.ItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class RoughStoneBlock extends FullBlock {
   public static RoughStoneBlock[] strataLevelBlockArray = new RoughStoneBlock[3];
   public int strataLevel;
   @Environment(EnvType.CLIENT)
   private Icon iconBroken;
   @Environment(EnvType.CLIENT)
   private Icon[] crackIcons;

   public RoughStoneBlock(int iBlockID, int iStrataLevel) {
      super(iBlockID, Material.rock);
      this.strataLevel = iStrataLevel;
      strataLevelBlockArray[iStrataLevel] = this;
      if (iStrataLevel == 0) {
         this.c(2.25F);
         this.b(10.0F);
      } else if (iStrataLevel == 1) {
         this.c(3.0F);
         this.b(13.0F);
      } else {
         this.c(4.5F);
         this.b(20.0F);
      }

      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(j);
      this.c("fcBlockStoneRough");
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      if (this.strataLevel == 0 && stack != null && stack.getItem() instanceof PickaxeItem) {
         int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();
         if (iToolLevel <= 1) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iMetadata < 15) {
         iMetadata++;
         if (!world.isRemote && this.isEffectiveItemConversionTool(stack, world, i, j, k)) {
            if (iMetadata <= 8) {
               if ((iMetadata & 1) == 0) {
                  world.playAuxSFX(2269, i, j, k, 0);
                  ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.stone, 1, this.strataLevel), iFromSide);
               } else if (iMetadata <= 5 && this.isUberItemConversionTool(stack, world, i, j, k)) {
                  iMetadata += 3;
                  world.playAuxSFX(2269, i, j, k, 0);
                  ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.stoneBrick, 1, this.strataLevel), iFromSide);
               }
            } else if (iMetadata == 12) {
               world.playAuxSFX(2270, i, j, k, 0);
               ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.gravelPile, 1), iFromSide);
            }
         }

         world.setBlockMetadataWithNotify(i, j, k, iMetadata);
         return true;
      } else {
         if (!world.isRemote && this.isEffectiveItemConversionTool(stack, world, i, j, k)) {
            world.playAuxSFX(2270, i, j, k, 0);
            ItemUtils.dropStackAsIfBlockHarvested(world, i, j, k, new ItemStack(BTWItems.gravelPile, 1));
         }

         return false;
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (!world.isRemote) {
         int iItemIDDropped = BTWItems.stone.itemID;
         int iNumDropped = 1;
         int metadataDropped = this.strataLevel;
         if (iMetadata < 8) {
            iNumDropped = 8 - iMetadata / 2;
         } else {
            iItemIDDropped = BTWItems.gravelPile.itemID;
            metadataDropped = 0;
            if (iMetadata < 12) {
               iNumDropped = 2;
            }
         }

         for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
            this.b(world, i, j, k, new ItemStack(iItemIDDropped, 1, metadataDropped));
         }
      }
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      world.playAuxSFX(2270, i, j, k, 0);
      this.dropComponentItemsWithChance(world, i, j, k, iMetadata, 1.0F);
   }

   @Override
   public boolean canDropFromExplosion(Explosion explosion) {
      return false;
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      float fChanceOfPileDrop = 1.0F;
      if (explosion != null) {
         fChanceOfPileDrop = 1.0F / explosion.explosionSize;
      }

      this.dropComponentItemsWithChance(world, i, j, k, iMetadata, fChanceOfPileDrop);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.strataLevel > 1 ? this.strataLevel + 1 : 2;
   }

   @Override
   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.strataLevel > 0 ? this.strataLevel + 1 : 0;
   }

   @Override
   public boolean arechiselseffectiveon(World world, int i, int j, int k) {
      return world.getBlockMetadata(i, j, k) >= 8 ? false : super.arechiselseffectiveon(world, i, j, k);
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public boolean isNaturalStone(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return world.getBlockMetadata(i, j, k) != 0 ? null : super.getStackRetrievedByBlockDispenser(world, i, j, k);
   }

   @Override
   public void onRemovedByBlockDispenser(World world, int i, int j, int k) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata != 0) {
         this.c(world, i, j, k, metadata, 0);
      }

      super.onRemovedByBlockDispenser(world, i, j, k);
   }

   public boolean isEffectiveItemConversionTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null && stack.getItem() instanceof ChiselItem) {
         int iToolLevel = ((ChiselItem)stack.getItem()).toolMaterial.getHarvestLevel();
         return iToolLevel >= this.getEfficientToolLevel(world, i, j, k);
      } else {
         return false;
      }
   }

   public boolean isUberItemConversionTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null && stack.getItem() instanceof ChiselItem) {
         int iToolLevel = ((ChiselItem)stack.getItem()).toolMaterial.getHarvestLevel();
         return iToolLevel >= this.getUberToolLevel(world, i, j, k);
      } else {
         return false;
      }
   }

   public int getUberToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   private void dropComponentItemsWithChance(World world, int i, int j, int k, int iMetadata, float fChanceOfItemDrop) {
      if (iMetadata < 8) {
         int iNumStoneDropped = 4 - iMetadata / 2;
         this.dropItemsIndividually(world, i, j, k, BTWItems.stone.itemID, iNumStoneDropped, this.strataLevel, fChanceOfItemDrop);
      }

      int iNumGravelDropped = 1;
      if (iMetadata < 12) {
         iNumGravelDropped = 2;
      }

      this.dropItemsIndividually(world, i, j, k, BTWItems.gravelPile.itemID, iNumGravelDropped, 0, fChanceOfItemDrop);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      String sBaseName = this.B();
      if (this.strataLevel > 0) {
         sBaseName = sBaseName + "_" + this.strataLevel;
      }

      this.blockIcon = register.registerIcon(sBaseName);
      this.iconBroken = register.registerIcon(sBaseName + "_broken");
      this.crackIcons = new Icon[7];

      for (int iTempIndex = 0; iTempIndex < 7; iTempIndex++) {
         this.crackIcons[iTempIndex] = register.registerIcon("fcOverlayStoneRough_" + (iTempIndex + 1));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iMetadata >= 8 ? this.iconBroken : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         IBlockAccess blockAccess = renderBlocks.blockAccess;
         int iMetadata = blockAccess.getBlockMetadata(i, j, k);
         if (iMetadata > 0 && iMetadata != 8) {
            int iTextureIndex = 0;
            if (iMetadata < 8) {
               iTextureIndex = MathHelper.clamp_int(iMetadata - 1, 0, 6);
            } else {
               iTextureIndex = MathHelper.clamp_int(iMetadata - 9, 0, 6);
            }

            Icon overlayTexture = this.crackIcons[iTextureIndex];
            if (overlayTexture != null) {
               this.renderBlockWithTexture(renderBlocks, i, j, k, overlayTexture);
            }
         }
      }
   }
}
