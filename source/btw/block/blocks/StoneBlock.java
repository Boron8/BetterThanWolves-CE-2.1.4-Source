package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.items.ChiselItem;
import btw.item.items.PickaxeItem;
import btw.item.items.ToolItem;
import btw.item.util.ItemUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class StoneBlock extends FullBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconByMetadataArray = new Icon[16];

   public StoneBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(2.25F);
      this.b(10.0F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(j);
      this.c("stone");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public float getBlockHardness(World world, int i, int j, int k) {
      int iStrata = this.getStrata(world, i, j, k);
      if (iStrata != 0) {
         return iStrata == 1 ? 3.0F : 4.5F;
      } else {
         return super.l(world, i, j, k);
      }
   }

   @Override
   public float getExplosionResistance(Entity entity, World world, int i, int j, int k) {
      int iStrata = this.getStrata(world, i, j, k);
      if (iStrata != 0) {
         return iStrata == 1 ? 7.8F : 12.0F;
      } else {
         return super.getExplosionResistance(entity, world, i, j, k);
      }
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return BTWBlocks.looseCobblestone.blockID;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.getStrata(metadata) << 2;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      super.a(world, i, j, k, iMetadata, fChance, iFortuneModifier);
      if (!world.isRemote) {
         this.b(world, i, j, k, new ItemStack(BTWItems.stone, 1, this.getStrata(iMetadata)));
         if (!this.getIsCracked(iMetadata)) {
            this.b(world, i, j, k, new ItemStack(BTWItems.gravelPile));
         }
      }
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.stone.itemID, 5, this.getStrata(iMetadata), fChanceOfDrop);
      int iNumGravel = this.getIsCracked(iMetadata) ? 2 : 3;
      this.dropItemsIndividually(world, i, j, k, BTWItems.gravelPile.itemID, iNumGravel, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iStrata = this.getStrata(iMetadata);
      int iToolLevel = this.getConversionLevelForTool(stack, world, i, j, k);
      if (this.getIsCracked(iMetadata)) {
         world.setBlockAndMetadataWithNotify(i, j, k, RoughStoneBlock.strataLevelBlockArray[iStrata].blockID, 0);
         if (!world.isRemote && iToolLevel > 0) {
            world.playAuxSFX(2269, i, j, k, 0);
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.stone, 1, iStrata), iFromSide);
         }
      } else if (iToolLevel == 2) {
         world.setBlockAndMetadataWithNotify(i, j, k, RoughStoneBlock.strataLevelBlockArray[iStrata].blockID, 4);
         if (!world.isRemote) {
            world.playAuxSFX(2269, i, j, k, 0);
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.stone, 3, iStrata), iFromSide);
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.gravelPile, 1), iFromSide);
         }
      } else if (iToolLevel == 3) {
         world.setBlockAndMetadataWithNotify(i, j, k, RoughStoneBlock.strataLevelBlockArray[iStrata].blockID, 2);
         if (!world.isRemote) {
            world.playAuxSFX(2269, i, j, k, 0);
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.stoneBrick, 1, iStrata), iFromSide);
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.gravelPile, 1), iFromSide);
         }
      } else {
         if (!world.isRemote) {
            world.playAuxSFX(2270, i, j, k, 0);
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.gravelPile, 1), iFromSide);
         }

         this.setIsCracked(world, i, j, k, true);
      }

      return true;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      int iStrata = this.getStrata(blockAccess, i, j, k);
      return iStrata > 1 ? iStrata + 1 : 2;
   }

   @Override
   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      int iStrata = this.getStrata(blockAccess, i, j, k);
      return iStrata > 0 ? iStrata + 1 : 0;
   }

   @Override
   public boolean isNaturalStone(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBlockInfestable(EntityLiving entity, int metadata) {
      return entity instanceof EntitySilverfish;
   }

   @Override
   public int getBlockIDOnInfest(EntityLiving entity, int metadata) {
      int strata = this.getStrata(metadata);
      if (strata == 1) {
         return BTWBlocks.infestedMidStrataStone.blockID;
      } else {
         return strata == 2 ? BTWBlocks.infestedDeepStrataStone.blockID : BTWBlocks.infestedStone.blockID;
      }
   }

   private int getConversionLevelForTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null) {
         if (stack.getItem() instanceof PickaxeItem) {
            int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();
            if (iToolLevel >= this.getEfficientToolLevel(world, i, j, k)) {
               return 2;
            }
         } else if (stack.getItem() instanceof ChiselItem) {
            int iToolLevel = ((ToolItem)stack.getItem()).toolMaterial.getHarvestLevel();
            if (iToolLevel >= this.getEfficientToolLevel(world, i, j, k)) {
               if (iToolLevel >= this.getUberToolLevel(world, i, j, k)) {
                  return 3;
               }

               return 1;
            }
         }
      }

      return 0;
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return iMetadata & 3;
   }

   public void setIsCracked(World world, int i, int j, int k, boolean bCracked) {
      int iMetadata = this.setIsCracked(world.getBlockMetadata(i, j, k), bCracked);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setIsCracked(int iMetadata, boolean bIsCracked) {
      if (bIsCracked) {
         iMetadata |= 4;
      } else {
         iMetadata &= -5;
      }

      return iMetadata;
   }

   public boolean getIsCracked(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsCracked(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsCracked(int iMetadata) {
      return (iMetadata & 4) != 0;
   }

   public int getUberToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 1));
      list.add(new ItemStack(iBlockID, 1, 2));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconByMetadataArray[0] = this.blockIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcBlockStone_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcBlockStone_2");
      this.iconByMetadataArray[3] = this.blockIcon;
      this.iconByMetadataArray[4] = register.registerIcon("fcBlockStone_cracked");
      this.iconByMetadataArray[5] = register.registerIcon("fcBlockStone_1_cracked");
      this.iconByMetadataArray[6] = register.registerIcon("fcBlockStone_2_cracked");
      this.iconByMetadataArray[7] = this.blockIcon;

      for (int iTempIndex = 8; iTempIndex < 16; iTempIndex++) {
         this.iconByMetadataArray[iTempIndex] = this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[iMetadata];
   }
}
