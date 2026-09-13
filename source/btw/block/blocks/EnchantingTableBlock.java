package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockEnchantmentTable;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class EnchantingTableBlock extends BlockEnchantmentTable {
   public EnchantingTableBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return iSide != 1 ? super.a(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      super.randomDisplayTick(world, x, y, z, rand);

      for (int i = x - 2; i <= x + 2; i++) {
         for (int k = z - 2; k <= z + 2; k++) {
            if (i > x - 2 && i < x + 2 && k == z - 1) {
               k = z + 2;
            }

            if (rand.nextInt(16) == 0) {
               for (int j = y - 1; j <= y + 2; j++) {
                  if (world.getBlockId(i, j, k) == Block.bookShelf.blockID) {
                     if (!world.isAirBlock((i - x) / 2 + x, j, (k - z) / 2 + z)) {
                        break;
                     }

                     world.spawnParticle(
                        "enchantmenttable",
                        x + 0.5,
                        y + 2.0,
                        z + 0.5,
                        i - x + rand.nextFloat() - 0.5,
                        j - y - rand.nextFloat() - 1.0F,
                        k - z + rand.nextFloat() - 0.5
                     );
                  }
               }
            }
         }
      }
   }
}
