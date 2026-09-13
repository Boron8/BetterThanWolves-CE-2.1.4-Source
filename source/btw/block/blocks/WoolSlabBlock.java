package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class WoolSlabBlock extends SlabBlock {
   private boolean isUpsideDown;
   public static final int NUM_SUBTYPES = 16;
   @Environment(EnvType.CLIENT)
   private Icon[] iconByColorArray;

   public WoolSlabBlock(int iBlockID, boolean bIsUpsideDown) {
      super(iBlockID, Material.cloth);
      this.c(0.8F);
      this.setBuoyancy(1.0F);
      this.isUpsideDown = bIsUpsideDown;
      if (!bIsUpsideDown) {
         this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      } else {
         this.initBlockBounds(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
      }

      this.a(m);
      this.c("fcBlockWoolSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return BTWBlocks.woolSlab.blockID;
   }

   @Override
   public int damageDropped(int i) {
      return i;
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
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public boolean getIsUpsideDown(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isUpsideDown;
   }

   @Override
   public boolean getIsUpsideDown(int iMetadata) {
      return this.isUpsideDown;
   }

   @Override
   public void setIsUpsideDown(World world, int i, int j, int k, boolean bUpsideDown) {
      if (this.isUpsideDown != bUpsideDown) {
         int iNewBlockID = BTWBlocks.woolSlabTop.blockID;
         int iMetadata = world.getBlockMetadata(i, j, k);
         if (this.blockID == BTWBlocks.woolSlabTop.blockID) {
            iNewBlockID = BTWBlocks.woolSlab.blockID;
         }

         world.setBlockAndMetadataWithNotify(i, j, k, iNewBlockID, iMetadata);
      }
   }

   @Override
   public int setIsUpsideDown(int iMetadata, boolean bUpsideDown) {
      return iMetadata;
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return Block.cloth.blockID;
   }

   @Override
   public int getCombinedMetadata(int iMetadata) {
      return iMetadata;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconByColorArray = new Icon[16];

      for (int iColor = 0; iColor < this.iconByColorArray.length; iColor++) {
         this.iconByColorArray[iColor] = register.registerIcon("cloth_" + iColor);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByColorArray[iMetadata % this.iconByColorArray.length];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      if (!this.isUpsideDown) {
         for (int iSubtype = 0; iSubtype < 16; iSubtype++) {
            list.add(new ItemStack(iBlockID, 1, iSubtype));
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }
}
