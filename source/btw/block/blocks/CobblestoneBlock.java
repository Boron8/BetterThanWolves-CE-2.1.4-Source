package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class CobblestoneBlock extends Block {
   private Icon[] iconByMetadataArray = new Icon[3];

   public CobblestoneBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.setPicksEffectiveOn();
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseCobblestone.blockID;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.getStrata(metadata) << 2;
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
      int strata = this.getStrata(metadata);
      if (strata == 1) {
         return BTWBlocks.infestedMidStrataCobblestone.blockID;
      } else {
         return strata == 2 ? BTWBlocks.infestedDeepstrataCobblestone.blockID : BTWBlocks.infestedCobblestone.blockID;
      }
   }

   @Override
   public void dropItemsOnDestroyedByMiningCharge(World world, int x, int y, int z, int metadata) {
      if (!world.isRemote) {
         this.b(world, x, y, z, new ItemStack(Block.gravel));
      }
   }

   @Override
   public boolean canBeConvertedByMobSpawner(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public void convertBlockFromMobSpawner(World world, int x, int y, int z) {
      world.setBlockAndMetadataWithNotify(x, y, z, Block.cobblestoneMossy.blockID, this.getStrata(world.getBlockMetadata(x, y, z)));
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return iMetadata & 3;
   }

   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 1));
      list.add(new ItemStack(iBlockID, 1, 2));
   }

   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }

   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconByMetadataArray[0] = this.blockIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcBlockCobblestone_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcBlockCobblestone_2");
   }

   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[this.getStrata(iMetadata)];
   }
}
