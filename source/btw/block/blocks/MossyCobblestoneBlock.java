package btw.block.blocks;

import btw.item.BTWItems;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class MossyCobblestoneBlock extends Block {
   private Icon[] iconByMetadataArray = new Icon[3];

   public MossyCobblestoneBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(2.0F);
      this.b(10.0F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.c("stoneMoss");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.stone.itemID, 6, iMetadata, fChanceOfDrop);
      return true;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.getStrata(metadata);
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
      this.iconByMetadataArray[1] = register.registerIcon("stoneMoss_1");
      this.iconByMetadataArray[2] = register.registerIcon("stoneMoss_2");
   }

   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[iMetadata];
   }
}
