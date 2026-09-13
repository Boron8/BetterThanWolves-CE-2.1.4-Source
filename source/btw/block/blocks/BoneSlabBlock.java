package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class BoneSlabBlock extends SlabBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconBoneSide;

   public BoneSlabBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.miscMaterial);
      this.c(2.0F);
      this.setPicksEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.a(Block.soundGravelFootstep);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockBoneSlab");
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.aestheticOpaque.blockID;
   }

   @Override
   public int getCombinedMetadata(int iMetadata) {
      return 15;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockBoneSlab_top");
      this.iconBoneSide = register.registerIcon("fcBlockBoneSlab_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide >= 2 ? this.iconBoneSide : this.blockIcon;
   }
}
