package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class LadderBlockFlaming extends LadderBlockBase {
   private static final int CHANCE_OF_LIGHTING_LADDER_ABOVE = 4;
   private static final int TICK_RATE = 60;
   private static final int TICK_RATE_VARIANCE = 30;

   public LadderBlockFlaming(int iBlockID) {
      super(iBlockID);
      this.a(1.0F);
      this.b(true);
      this.c("fcBlockLadderOnFire");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
   }

   @Override
   public int tickRate(World world) {
      return 60;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world) + world.rand.nextInt(30));
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (this.isRainingOnLadder(world, i, j, k)) {
         this.extinguish(world, i, j, k);
      } else {
         FireBlock.checkForFireSpreadFromLocation(world, i, j, k, rand, 0);
         int iCounter = this.getBurnCounter(world, i, j, k);
         if (iCounter < 3) {
            if (rand.nextInt(4) == 0) {
               this.lightLadderAtLocationIfPresent(world, i, j + 1, k);
            }

            this.setBurnCounter(world, i, j, k, iCounter + 1);
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world) + rand.nextInt(30));
         } else {
            this.lightLadderAtLocationIfPresent(world, i, j + 1, k);
            world.setBlockToAir(i, j, k);
         }
      }
   }

   @Override
   public boolean getDoesFireDamageToEntities(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   protected void lightLadderAtLocationIfPresent(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID == BTWBlocks.ladder.blockID) {
         BTWBlocks.ladder.setOnFireDirectly(world, i, j, k);
      }
   }

   protected void extinguish(World world, int i, int j, int k) {
      int iNewMetadata = BTWBlocks.ladder.setFacing(0, this.getFacing(world, i, j, k));
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.ladder.blockID, iNewMetadata);
   }

   protected boolean isRainingOnLadder(World world, int i, int j, int k) {
      return world.isRainingAtPos(i, j, k);
   }

   protected int getBurnCounter(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getBurnCounter(blockAccess.getBlockMetadata(i, j, k));
   }

   protected int getBurnCounter(int iMetadata) {
      return iMetadata >> 2 & 3;
   }

   protected void setBurnCounter(World world, int i, int j, int k, int iCounter) {
      int iMetadata = this.setBurnCounter(world.getBlockMetadata(i, j, k), iCounter);
      world.setBlockMetadata(i, j, k, iMetadata);
   }

   protected int setBurnCounter(int iMetadata, int iCounter) {
      iMetadata &= -13;
      return iMetadata | iCounter << 2;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int y, int z) {
      boolean bReturnValue = super.renderBlock(renderBlocks, i, y, z);
      if (bReturnValue) {
         this.renderFirePortion(renderBlocks, i, y, z);
      }

      return bReturnValue;
   }

   @Environment(EnvType.CLIENT)
   private void renderFirePortion(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iFacing = this.getFacing(blockAccess, i, j, k);
      Tessellator tessellator = Tessellator.instance;
      Icon fireIcon;
      if ((i + j + k & 1) != 0) {
         fireIcon = Block.fire.func_94438_c(1);
      } else {
         fireIcon = Block.fire.func_94438_c(0);
      }

      tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tessellator.setBrightness(Block.fire.e(blockAccess, i, j, k));
      double dMinU = fireIcon.getMinU();
      double dMinV = fireIcon.getMinV();
      double dMaxU = fireIcon.getMaxU();
      double dMaxV = fireIcon.getMaxV();
      float dFireHeight = 1.4F;
      double dFireWidth = 0.2F;
      double dFireHorizontalOffset = 0.0;
      float fVerticalOffset = 0.0625F;
      if ((i / 2 + j / 2 + k / 2 & 1) != 0) {
         double dTempU = dMaxU;
         dMaxU = dMinU;
         dMinU = dTempU;
      }

      if (iFacing == 5) {
         tessellator.addVertexWithUV(i + dFireWidth + dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k + 1, dMaxU, dMinV);
         tessellator.addVertexWithUV(i + dFireHorizontalOffset, j + fVerticalOffset, k + 1, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + dFireHorizontalOffset, j + fVerticalOffset, k, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + dFireWidth + dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k, dMinU, dMinV);
         tessellator.addVertexWithUV(i + dFireWidth + dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k, dMinU, dMinV);
         tessellator.addVertexWithUV(i + dFireHorizontalOffset, j + fVerticalOffset, k, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + dFireHorizontalOffset, j + fVerticalOffset, k + 1, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + dFireWidth + dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k + 1, dMaxU, dMinV);
      } else if (iFacing == 4) {
         tessellator.addVertexWithUV(i + 1.0 - dFireWidth - dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k, dMinU, dMinV);
         tessellator.addVertexWithUV(i + 1 - dFireHorizontalOffset, j + fVerticalOffset, k, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + 1 - dFireHorizontalOffset, j + fVerticalOffset, k + 1, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + 1.0 - dFireWidth - dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k + 1, dMaxU, dMinV);
         tessellator.addVertexWithUV(i + 1.0 - dFireWidth - dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k + 1, dMaxU, dMinV);
         tessellator.addVertexWithUV(i + 1 - dFireHorizontalOffset, j + fVerticalOffset, k + 1, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + 1 - dFireHorizontalOffset, j + fVerticalOffset, k, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + 1.0 - dFireWidth - dFireHorizontalOffset, j + dFireHeight + fVerticalOffset, k, dMinU, dMinV);
      } else if (iFacing == 3) {
         tessellator.addVertexWithUV(i, j + dFireHeight + fVerticalOffset, k + dFireWidth + dFireHorizontalOffset, dMaxU, dMinV);
         tessellator.addVertexWithUV(i, j + fVerticalOffset, k + dFireHorizontalOffset, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + 1, j + fVerticalOffset, k + dFireHorizontalOffset, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + 1, j + dFireHeight + fVerticalOffset, k + dFireWidth + dFireHorizontalOffset, dMinU, dMinV);
         tessellator.addVertexWithUV(i + 1, j + dFireHeight + fVerticalOffset, k + dFireWidth + dFireHorizontalOffset, dMinU, dMinV);
         tessellator.addVertexWithUV(i + 1, j + fVerticalOffset, k + dFireHorizontalOffset, dMinU, dMaxV);
         tessellator.addVertexWithUV(i, j + fVerticalOffset, k + dFireHorizontalOffset, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i, j + dFireHeight + fVerticalOffset, k + dFireWidth + dFireHorizontalOffset, dMaxU, dMinV);
      } else if (iFacing == 2) {
         tessellator.addVertexWithUV(i + 1, j + dFireHeight + fVerticalOffset, k + 1.0 - dFireWidth - dFireHorizontalOffset, dMinU, dMinV);
         tessellator.addVertexWithUV(i + 1, j + fVerticalOffset, k + 1 - dFireHorizontalOffset, dMinU, dMaxV);
         tessellator.addVertexWithUV(i, j + fVerticalOffset, k + 1 - dFireHorizontalOffset, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i, j + dFireHeight + fVerticalOffset, k + 1.0 - dFireWidth - dFireHorizontalOffset, dMaxU, dMinV);
         tessellator.addVertexWithUV(i, j + dFireHeight + fVerticalOffset, k + 1.0 - dFireWidth - dFireHorizontalOffset, dMaxU, dMinV);
         tessellator.addVertexWithUV(i, j + fVerticalOffset, k + 1 - dFireHorizontalOffset, dMaxU, dMaxV);
         tessellator.addVertexWithUV(i + 1, j + fVerticalOffset, k + 1 - dFireHorizontalOffset, dMinU, dMaxV);
         tessellator.addVertexWithUV(i + 1, j + dFireHeight + fVerticalOffset, k + 1.0 - dFireWidth - dFireHorizontalOffset, dMinU, dMinV);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (rand.nextInt(24) == 0) {
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
      }

      for (int iTempCount = 0; iTempCount < 3; iTempCount++) {
         double xPos = i + rand.nextDouble();
         double yPos = j + rand.nextDouble() * 0.5 + 0.5;
         double zPos = k + rand.nextDouble();
         world.spawnParticle("largesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      }
   }
}
