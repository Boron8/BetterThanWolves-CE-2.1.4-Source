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

public class LooseCobblestoneBlock extends LavaReceiverBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconLavaCracks;
   @Environment(EnvType.CLIENT)
   private Icon[] iconByMetadataArray = new Icon[3];

   public LooseCobblestoneBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockCobblestoneLoose");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      world.setBlockAndMetadataWithNotify(i, j, k, Block.cobblestone.blockID, this.getStrata(world, i, j, k));
      return true;
   }

   @Override
   public int damageDropped(int metadata) {
      return this.getStrata(metadata) << 2;
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
      world.setBlockAndMetadataWithNotify(x, y, z, Block.cobblestoneMossy.blockID, this.getStrata(world, x, y, z));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconLavaCracks = register.registerIcon("fcOverlayCobblestoneLava");
      this.iconByMetadataArray[0] = this.blockIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcBlockCobblestoneLoose_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcBlockCobblestoneLoose_2");
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

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[this.getStrata(iMetadata)];
   }

   @Environment(EnvType.CLIENT)
   @Override
   protected Icon getLavaCracksOverlay() {
      return this.iconLavaCracks;
   }
}
