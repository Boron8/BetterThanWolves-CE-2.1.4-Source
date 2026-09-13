package btw.block.blocks;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class FlowerBlock extends BlockFlower {
   public FlowerBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.0F);
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.DAMP_VEGETATION);
      this.setFilterableProperties(2);
      this.a(Block.soundGrassFootstep);
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess access, int i, int j, int k, EntityAnimal animal) {
      return true;
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }
}
