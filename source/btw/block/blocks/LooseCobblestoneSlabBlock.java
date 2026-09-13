package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LooseCobblestoneSlabBlock extends MortarReceiverSlabBlock {
   private Icon[] iconByMetadataArray = new Icon[3];

   public LooseCobblestoneSlabBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockCobblestoneLooseSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.looseCobblestone.blockID;
   }

   @Override
   public int getCombinedMetadata(int iMetadata) {
      return this.getStrata(iMetadata) << 2;
   }

   @Override
   public boolean attemptToCombineWithFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (entity.blockID == this.blockID && !this.getIsUpsideDown(world, i, j, k) && entity.metadata == world.getBlockMetadata(i, j, k)) {
         this.convertToFullBlock(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int damageDropped(int metadata) {
      int var2;
      return var2 = metadata & -2;
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      int iNewMetadata = this.getStrata(world, i, j, k);
      if (this.getIsUpsideDown(world, i, j, k)) {
         iNewMetadata |= 8;
      }

      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.cobblestoneSlab.blockID, iNewMetadata);
      return true;
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return (iMetadata & 12) >>> 2;
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
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockCobblestoneLoose");
      this.iconByMetadataArray[0] = this.blockIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcBlockCobblestoneLoose_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcBlockCobblestoneLoose_2");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[this.getStrata(iMetadata)];
   }
}
