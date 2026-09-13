package btw.block.blocks;

import java.util.Random;
import net.minecraft.src.BlockPumpkin;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class CarvedPumpkinBlock extends BlockPumpkin {
   public CarvedPumpkinBlock(int iBlockID) {
      super(iBlockID, false);
      this.c(1.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.a(g);
      this.c("pumpkin");
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == 0 || r[iBlockID].blockMaterial.isReplaceable();
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      super.a(world, i, j, k, iBlockID, iMetadata);
      if (!world.isRemote) {
         world.playAuxSFX(2251, i, j, k, 0);
      }
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iDirection = iMetadata & 3;
      if (bReverse) {
         if (++iDirection > 3) {
            iDirection = 0;
         }
      } else if (--iDirection < 0) {
         iDirection = 3;
      }

      return iMetadata & -4 | iDirection;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return animal.canGrazeOnRoughVegetation();
   }
}
