package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class RottenFleshBlock extends Block {
   public static final float HARDNESS = 0.6F;

   public RottenFleshBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.6F);
      this.setBuoyancy(1.0F);
      this.setShovelsEffectiveOn(true);
      this.a(BTWBlocks.stepSoundSquish);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockRottenFlesh");
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockRottenFlesh");
   }
}
