package btw.block.blocks;

import btw.util.MiscUtils;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.World;

public class DispenserBlock extends BlockDispenser {
   public DispenserBlock(int iBlockID) {
      super(iBlockID);
      this.c(3.5F);
      this.a(j);
      this.c("dispenser");
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & 7;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iMetadata & -8 | iFacing;
   }

   @Override
   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack stack) {
      int facing = MiscUtils.convertPlacingEntityOrientationToBlockFacingReversed(entityLiving);
      this.setFacing(world, x, y, z, facing);
      if (stack.hasDisplayName()) {
         ((TileEntityDispenser)world.getBlockTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
      }
   }
}
