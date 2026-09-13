package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockStoneBrick;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class StoneBrickBlock extends BlockStoneBrick {
   public static final String[] stoneBrickTypesStratified = new String[]{
      "default", "mossy", "cracked", "chiseled", "default", "mossy", "cracked", "chiseled", "default", "mossy", "cracked", "chiseled"
   };
   private Icon[] iconArray;

   public StoneBrickBlock(int iBlockID) {
      super(iBlockID);
      this.c(2.25F);
      this.b(10.0F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.c("stonebricksmooth");
      this.b(true);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int strataOffset = this.getStrata(iMetadata) << 2;
      if (this.getStoneType(iMetadata) == 0 && !world.getBlockMaterial(i, j - 1, k).blocksMovement()) {
         int iBlockAboveID = world.getBlockId(i, j + 1, k);
         if (iBlockAboveID != Block.waterMoving.blockID && iBlockAboveID != Block.waterStill.blockID) {
            if ((iBlockAboveID == Block.lavaMoving.blockID || iBlockAboveID == Block.lavaStill.blockID) && random.nextInt(15) == 0) {
               world.setBlockMetadataWithNotify(i, j, k, 2 + strataOffset);
               world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
            }
         } else if (random.nextInt(15) == 0) {
            world.setBlockMetadataWithNotify(i, j, k, 1 + strataOffset);
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         }
      }
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseStoneBrick.blockID;
   }

   @Override
   public int damageDropped(int iMetadata) {
      return this.getStrata(iMetadata) << 2;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBlockInfestable(EntityLiving entity, int metadata) {
      return entity instanceof EntitySilverfish;
   }

   @Override
   public int getBlockIDOnInfest(EntityLiving entity, int metadata) {
      switch (metadata) {
         case 0:
            return BTWBlocks.infestedStoneBrick.blockID;
         case 1:
            return BTWBlocks.infestedMossyStoneBrick.blockID;
         case 2:
            return BTWBlocks.infestedCrackedStoneBrick.blockID;
         case 3:
            return BTWBlocks.infestedChiseledStoneBrick.blockID;
         case 4:
            return BTWBlocks.infestedMidStrataStoneBrick.blockID;
         case 5:
            return BTWBlocks.infestedMidStrataMossyStoneBrick.blockID;
         case 6:
            return BTWBlocks.infestedMidStrataCrackedStoneBrick.blockID;
         case 7:
            return BTWBlocks.infestedMidStrataChiseledStoneBrick.blockID;
         case 8:
            return BTWBlocks.infestedDeepStrataStoneBrick.blockID;
         case 9:
            return BTWBlocks.infestedDeepStrataMossyStoneBrick.blockID;
         case 10:
            return BTWBlocks.infestedDeepStrataCrackedStoneBrick.blockID;
         case 11:
            return BTWBlocks.infestedDeepStrataChiseledStoneBrick.blockID;
         default:
            return BTWBlocks.infestedStoneBrick.blockID;
      }
   }

   public int getStoneType(int metadata) {
      return metadata & 3;
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return (iMetadata & 12) >>> 2;
   }

   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return this.iconArray[par2];
   }

   @Override
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      for (int var4 = 0; var4 < 12; var4++) {
         par3List.add(new ItemStack(par1, 1, var4));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.iconArray = new Icon[12];

      for (int strata = 0; strata < 3; strata++) {
         for (int blockType = 0; blockType < unlocalizedName.length; blockType++) {
            String name = unlocalizedName[blockType];
            if (strata != 0) {
               name = name + "_" + strata;
            }

            this.iconArray[blockType + (strata << 2)] = par1IconRegister.registerIcon(name);
         }
      }
   }
}
