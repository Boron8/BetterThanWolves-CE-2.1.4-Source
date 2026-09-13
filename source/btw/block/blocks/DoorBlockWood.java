package btw.block.blocks;

import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.PathFinder;
import net.minecraft.src.World;

public class DoorBlockWood extends DoorBlock {
   public DoorBlockWood(int iBlockID) {
      super(iBlockID, Material.wood);
      this.c(1.5F);
      this.setBuoyant();
      this.a(g);
      this.c("doorWood");
      this.D();
   }

   @Override
   public boolean canPathThroughBlock(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return pathFinder.CanPathThroughClosedWoodDoor() || pathFinder.canPathThroughOpenWoodDoor() && this.b(blockAccess, i, j, k);
   }

   @Override
   public boolean isBreakableBarricade(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBreakableBarricadeOpen(IBlockAccess blockAccess, int i, int j, int k) {
      return this.b_(blockAccess, i, j, k);
   }

   @Override
   public void onPoweredBlockChange(World par1World, int par2, int par3, int par4, boolean par5) {
   }

   @Override
   public void onAIOpenDoor(World world, int i, int j, int k, boolean bOpen) {
      super.onPoweredBlockChange(world, i, j, k, bOpen);
   }
}
