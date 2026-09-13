package btw.block.blocks;

import btw.block.tileentity.MobSpawnerTileEntity;
import net.minecraft.src.BlockMobSpawner;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class MobSpawnerBlock extends BlockMobSpawner {
   public MobSpawnerBlock(int iBlockID) {
      super(iBlockID);
      this.c(5.0F);
      this.a(k);
      this.c("mobSpawner");
      this.D();
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new MobSpawnerTileEntity();
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public int getMobilityFlag() {
      return 2;
   }
}
