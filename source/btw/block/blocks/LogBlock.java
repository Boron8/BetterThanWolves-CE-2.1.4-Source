package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import btw.item.items.ChiselItem;
import btw.item.util.ItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLog;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LogBlock extends BlockLog {
   public static Block[] chewedLogArray;
   @Environment(EnvType.CLIENT)
   public static final String[] topTextureTypes = new String[]{"tree_top", "fcBlockLogTopSpruce", "fcBlockLogTopBirch", "fcBlockLogTopJungle"};
   @Environment(EnvType.CLIENT)
   private Icon[] topIconArray;
   @Environment(EnvType.CLIENT)
   public static final String[] trunkTextureTypes = new String[]{"fcBlockTrunkOak", "fcBlockTrunkSpruce", "fcBlockTrunkBirch", "fcBlockTrunkJungle"};
   @Environment(EnvType.CLIENT)
   private Icon[] trunkIconArray;
   @Environment(EnvType.CLIENT)
   public static final String[] trunkTopTextureTypes = new String[]{
      "fcBlockTrunkTop", "fcBlockTrunkTopSpruce", "fcBlockTrunkTopBirch", "fcBlockTrunkTopJungle"
   };
   @Environment(EnvType.CLIENT)
   private Icon[] trunkTopIconArray;

   public LogBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.logMaterial);
      this.c(1.25F);
      this.b(3.33F);
      this.setAxesEffectiveOn();
      this.setChiselsEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.LOGS);
      this.a(g);
      this.c("log");
   }

   @Override
   public float getBlockHardness(World world, int i, int j, int k) {
      float fHardness = super.l(world, i, j, k);
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (this.getIsStump(iMetadata)) {
         fHardness *= 3.0F;
      }

      return fHardness;
   }

   @Override
   public boolean getIsProblemToRemove(ItemStack toolStack, IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsStump(blockAccess, i, j, k) ? !this.isWorkStumpItemConversionTool(toolStack, (World)blockAccess, i, j, k) : false;
   }

   @Override
   public boolean getDoesStumpRemoverWorkOnBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsStump(blockAccess, i, j, k);
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int x, int y, int z, int iFromSide) {
      int oldMetadata = world.getBlockMetadata(x, y, z);
      int newMetadata = 0;
      int chewedLogID = chewedLogArray[oldMetadata & 3].blockID;
      if (this.getIsStump(oldMetadata)) {
         if (this.isWorkStumpItemConversionTool(stack, world, x, y, z)) {
            world.playAuxSFX(2268, x, y, z, 0);
            world.setBlockAndMetadataWithNotify(x, y, z, BTWBlocks.workStump.blockID, oldMetadata & 3 | 8);
            return true;
         }

         newMetadata = BTWBlocks.oakChewedLog.setIsStump(oldMetadata & 12);
      } else {
         int orientation = oldMetadata >> 2 & 3;
         newMetadata = BTWBlocks.oakChewedLog.setOrientation(oldMetadata & 12, orientation);
      }

      world.setBlockAndMetadataWithNotify(x, y, z, chewedLogID, newMetadata);
      if (!world.isRemote) {
         ItemUtils.ejectStackFromBlockTowardsFacing(world, x, y, z, new ItemStack(BTWItems.bark, 1, oldMetadata & 3), iFromSide);
      }

      return true;
   }

   @Override
   public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {
      byte leafCheckRange = 4;
      int chunkCheckRange = leafCheckRange + 1;
      if (world.checkChunksExist(x - chunkCheckRange, y - chunkCheckRange, z - chunkCheckRange, x + chunkCheckRange, y + chunkCheckRange, z + chunkCheckRange)) {
         for (int i = -leafCheckRange; i <= leafCheckRange; i++) {
            for (int j = -leafCheckRange; j <= leafCheckRange; j++) {
               for (int k = -leafCheckRange; k <= leafCheckRange; k++) {
                  int offsetBlockID = world.getBlockId(x + i, y + j, z + k);
                  Block offsetBlock = Block.blocksList[offsetBlockID];
                  if (offsetBlock != null && offsetBlock.isLeafBlock(world, x, y, z)) {
                     int oldMetadata = world.getBlockMetadata(x + i, y + j, z + k);
                     if ((oldMetadata & 8) == 0) {
                        world.setBlockMetadataWithNotify(x + i, y + j, z + k, oldMetadata | 8, 4);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return !this.getIsStump(world, i, j, k);
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 6, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.bark.itemID, 1, iMetadata & 3, fChanceOfDrop);
      return true;
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      this.convertToSmouldering(world, i, j, k);
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iAxisAlignment = iMetadata & 12;
      if (iAxisAlignment != 0) {
         if (iAxisAlignment == 4) {
            iAxisAlignment = 8;
         } else if (iAxisAlignment == 8) {
            iAxisAlignment = 4;
         }

         iMetadata = iMetadata & -13 | iAxisAlignment;
      }

      return iMetadata;
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return PlanksBlock.getFurnaceBurnTimeByWoodType(iItemDamage) * 4;
   }

   @Override
   public boolean isLog(IBlockAccess blockAccess, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean canSupportLeaves(IBlockAccess blockAccess, int x, int y, int z) {
      return !this.isDeadStump(blockAccess, x, y, z);
   }

   public void convertToSmouldering(World world, int i, int j, int k) {
      int iNewMetadata = BTWBlocks.smolderingLog.setIsStump(0, this.getIsStump(world, i, j, k));
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.smolderingLog.blockID, iNewMetadata);
   }

   public boolean getIsStump(int iMetadata) {
      return (iMetadata & 12) == 12;
   }

   public boolean getIsStump(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      if (iBlockID == Block.wood.blockID) {
         int iMetadata = blockAccess.getBlockMetadata(i, j, k);
         if (this.getIsStump(iMetadata)) {
            return true;
         }
      }

      return false;
   }

   public boolean isDeadStump(IBlockAccess blockAccess, int i, int j, int k) {
      if (this.getIsStump(blockAccess, i, j, k)) {
         int iBlockAboveID = blockAccess.getBlockMetadata(i, j + 1, k);
         if (iBlockAboveID != Block.wood.blockID) {
            return true;
         }
      }

      return false;
   }

   public boolean isWorkStumpItemConversionTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null && stack.getItem() instanceof ChiselItem) {
         int iToolLevel = ((ChiselItem)stack.getItem()).toolMaterial.getHarvestLevel();
         return iToolLevel >= 2;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int side, int metadata) {
      int facing = metadata >> 2 & 3;
      if ((metadata & 12) == 12) {
         return side > 1 ? this.trunkIconArray[metadata & 3] : this.trunkTopIconArray[metadata & 3];
      } else if (facing != 0 || side != 0 && side != 1) {
         if (facing != 1 || side != 4 && side != 5) {
            return facing != 2 || side != 2 && side != 3 ? super.getIcon(side, metadata) : this.topIconArray[metadata & 3];
         } else {
            return this.topIconArray[metadata & 3];
         }
      } else {
         return this.topIconArray[metadata & 3];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister iconRegister) {
      this.topIconArray = new Icon[trunkTextureTypes.length];
      this.trunkIconArray = new Icon[trunkTextureTypes.length];
      this.trunkTopIconArray = new Icon[trunkTextureTypes.length];

      for (int i = 0; i < this.trunkIconArray.length; i++) {
         this.topIconArray[i] = iconRegister.registerIcon(topTextureTypes[i]);
         this.trunkIconArray[i] = iconRegister.registerIcon(trunkTextureTypes[i]);
         this.trunkTopIconArray[i] = iconRegister.registerIcon(trunkTopTextureTypes[i]);
      }

      super.registerIcons(iconRegister);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return renderer.renderBlockLog(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      this.renderCookingByKiLnOverlay(renderBlocks, i, j, k, bFirstPassResult);
   }
}
