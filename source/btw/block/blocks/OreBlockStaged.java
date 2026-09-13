package btw.block.blocks;

import btw.item.BTWItems;
import btw.item.items.ChiselItem;
import btw.item.items.PickaxeItem;
import btw.item.items.ToolItem;
import btw.item.util.ItemUtils;
import java.util.Random;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class OreBlockStaged extends OreBlock {
   public OreBlockStaged(int iBlockID) {
      super(iBlockID);
      this.setChiselsEffectiveOn();
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      int iStrata = this.getStrata(iOldMetadata);
      world.setBlockAndMetadataWithNotify(i, j, k, RoughStoneBlock.strataLevelBlockArray[iStrata].blockID, 4);
      if (!world.isRemote) {
         int iLevel = this.getConversionLevelForTool(stack, world, i, j, k);
         if (iLevel > 0) {
            world.playAuxSFX(2269, i, j, k, 0);
            if (iLevel >= 3) {
               this.ejectItemsOnGoodPickConversion(stack, world, i, j, k, iOldMetadata, iFromSide);
            } else if (iLevel == 2) {
               this.ejectItemsOnStonePickConversion(stack, world, i, j, k, iOldMetadata, iFromSide);
            } else {
               this.ejectItemsOnChiselConversion(stack, world, i, j, k, iOldMetadata, iFromSide);
            }
         }
      }

      return true;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      super.a(world, i, j, k, iMetadata, fChance, iFortuneModifier);
      if (!world.isRemote) {
         this.dropItemsIndividually(world, i, j, k, BTWItems.stone.itemID, 6, this.getStrata(iMetadata), 1.0F);
      }
   }

   @Override
   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getRequiredToolLevelForOre(blockAccess, i, j, k);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      int iLevelForOre = this.getRequiredToolLevelForOre(blockAccess, i, j, k);
      int iLevelForStrata = this.getRequiredToolLevelForStrata(blockAccess, i, j, k);
      return iLevelForStrata > iLevelForOre ? iLevelForStrata : iLevelForOre;
   }

   public abstract int idDroppedOnConversion(int var1);

   public int damageDroppedOnConversion(int iMetadata) {
      return 0;
   }

   public int quantityDroppedOnConversion(Random rand) {
      return 1;
   }

   public int idDroppedOnStonePickConversion(int iMetadata, Random rand, int iFortuneModifier) {
      return this.a(iMetadata, rand, iFortuneModifier);
   }

   public int damageDroppedOnStonePickConversion(int iMetadata) {
      return this.a(iMetadata);
   }

   public int quantityDroppedOnStonePickConversion(Random rand) {
      return this.a(rand);
   }

   protected void ejectItemsOnGoodPickConversion(ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide) {
      ItemUtils.ejectStackFromBlockTowardsFacing(
         world, i, j, k, new ItemStack(this.a(iOldMetadata, world.rand, 0), this.a(world.rand), this.a(iOldMetadata)), iFromSide
      );
   }

   protected void ejectItemsOnStonePickConversion(ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide) {
      ItemUtils.ejectStackFromBlockTowardsFacing(
         world,
         i,
         j,
         k,
         new ItemStack(
            this.idDroppedOnStonePickConversion(iOldMetadata, world.rand, 0),
            this.quantityDroppedOnStonePickConversion(world.rand),
            this.damageDroppedOnStonePickConversion(iOldMetadata)
         ),
         iFromSide
      );
   }

   protected void ejectItemsOnChiselConversion(ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide) {
      ItemUtils.ejectStackFromBlockTowardsFacing(
         world,
         i,
         j,
         k,
         new ItemStack(this.idDroppedOnConversion(iOldMetadata), this.quantityDroppedOnConversion(world.rand), this.damageDroppedOnConversion(iOldMetadata)),
         iFromSide
      );
   }

   public int getRequiredToolLevelForOre(IBlockAccess blockAccess, int i, int j, int k) {
      return 0;
   }

   private int getConversionLevelForTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null) {
         if (stack.getItem() instanceof PickaxeItem) {
            int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();
            if (iToolLevel >= this.getRequiredToolLevelForOre(world, i, j, k)) {
               if (iToolLevel > 1) {
                  return 3;
               }

               return 2;
            }
         } else if (stack.getItem() instanceof ChiselItem) {
            int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();
            if (iToolLevel >= this.getRequiredToolLevelForOre(world, i, j, k)) {
               return 1;
            }
         }
      }

      return 0;
   }
}
