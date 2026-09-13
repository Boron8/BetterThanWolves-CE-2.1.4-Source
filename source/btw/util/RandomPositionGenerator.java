package btw.util;

import btw.world.util.BlockPos;
import java.util.Random;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.MathHelper;

public class RandomPositionGenerator extends net.minecraft.src.RandomPositionGenerator {
   public static boolean findSimpleRandomTargetBlock(EntityCreature entity, int iHorizontalRange, int iVerticalRange, BlockPos foundPos) {
      Random rand = entity.aE();
      boolean bFoundBlock = false;
      float fMaxFoundWeight = -99999.0F;
      int iMinX = MathHelper.floor_double(entity.posX) - iHorizontalRange;
      int iMinY = (int)entity.posY - iVerticalRange;
      int iMinZ = MathHelper.floor_double(entity.posZ) - iHorizontalRange;
      iHorizontalRange = iHorizontalRange * 2 + 1;
      iVerticalRange = iVerticalRange * 2 + 1;

      for (int iAttemptCount = 0; iAttemptCount < 10; iAttemptCount++) {
         int iTempX = iMinX + rand.nextInt(iHorizontalRange);
         int iTempY = iMinY + rand.nextInt(iVerticalRange);
         int iTempZ = iMinZ + rand.nextInt(iHorizontalRange);
         float fTempWeight = entity.getBlockPathWeight(iTempX, iTempY, iTempZ);
         if (fTempWeight > fMaxFoundWeight) {
            fMaxFoundWeight = fTempWeight;
            foundPos.x = iTempX;
            foundPos.y = iTempY;
            foundPos.z = iTempZ;
            bFoundBlock = true;
         }
      }

      return bFoundBlock;
   }
}
