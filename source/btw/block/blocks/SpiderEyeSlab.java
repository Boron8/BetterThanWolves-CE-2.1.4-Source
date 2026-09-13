package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class SpiderEyeSlab extends SlabBlock {
   public SpiderEyeSlab(int blockID) {
      super(blockID, Material.ground);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockSpiderEyeSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.spiderEyeBlock.blockID;
   }

   @Override
   public boolean canBePistonShoveled(World world, int x, int y, int z) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int side, int metadata) {
      return BTWBlocks.spiderEyeBlock.getIcon(side, metadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
   }
}
