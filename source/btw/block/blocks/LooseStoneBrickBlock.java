package btw.block.blocks;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LooseStoneBrickBlock extends LavaReceiverBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconLavaCracks;
   private Icon[] iconByMetadataArray = new Icon[3];

   public LooseStoneBrickBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.c("fcBlockStoneBrickLoose");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int damageDropped(int metadata) {
      return this.getStrata(metadata) << 2;
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      world.setBlockAndMetadataWithNotify(i, j, k, Block.stoneBrick.blockID, this.getStrata(world, i, j, k) << 2);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconLavaCracks = register.registerIcon("fcOverlayStoneBrickLava");
      this.iconByMetadataArray[0] = this.blockIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcBlockStoneBrickLoose_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcBlockStoneBrickLoose_2");
   }

   @Environment(EnvType.CLIENT)
   @Override
   protected Icon getLavaCracksOverlay() {
      return this.iconLavaCracks;
   }

   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 4));
      list.add(new ItemStack(iBlockID, 1, 8));
   }

   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }

   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[this.getStrata(iMetadata)];
   }
}
