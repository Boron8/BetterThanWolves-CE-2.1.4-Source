package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.texture.StokedFireTexture;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class StokedFireBlock extends FireBlock {
   private static final int TICK_RATE = 42;
   @Environment(EnvType.CLIENT)
   private Icon[] fireTextureArray;

   public StokedFireBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.0F);
      this.a(1.0F);
      this.setFireProperties(60, 0);
      this.a(g);
      this.c("fcBlockStokedFire");
      this.D();
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public int tickRate(World world) {
      return 42;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (this.validateState(world, i, j, k)) {
         if (world.getBlockId(i, j + 1, k) == Block.brick.blockID) {
            world.setBlockWithNotify(i, j + 1, k, BTWBlocks.kiln.blockID);
         }

         int iMetaData = world.getBlockMetadata(i, j, k);
         if (iMetaData < 15) {
            world.setBlockMetadata(i, j, k, ++iMetaData);
         }

         this.tryToDestroyBlockWithFire(world, i + 1, j, k, 300, random, 0);
         this.tryToDestroyBlockWithFire(world, i - 1, j, k, 300, random, 0);
         this.tryToDestroyBlockWithFire(world, i, j - 1, k, 250, random, 0);
         this.tryToDestroyBlockWithFire(world, i, j + 1, k, 250, random, 0);
         this.tryToDestroyBlockWithFire(world, i, j, k - 1, 300, random, 0);
         this.tryToDestroyBlockWithFire(world, i, j, k + 1, 300, random, 0);
         checkForFireSpreadFromLocation(world, i, j, k, random, 0);
         if (iMetaData >= 3) {
            world.setBlockAndMetadataWithNotify(i, j, k, Block.fire.blockID, 0);
         } else {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world) + world.rand.nextInt(10));
         }
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.setBlockMetadata(i, j, k, 0);
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world) * 4);
      }
   }

   @Override
   public boolean doesInfiniteBurnToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return iFacing == 1;
   }

   public boolean validateState(World world, int i, int j, int k) {
      if (!this.c(world, i, j, k)) {
         world.setBlockWithNotify(i, j, k, 0);
         return false;
      } else if (world.getBlockId(i, j - 1, k) == BTWBlocks.hibachi.blockID) {
         if (!BTWBlocks.hibachi.isLit(world, i, j - 1, k)) {
            world.setBlockWithNotify(i, j, k, 0);
            return false;
         } else {
            return true;
         }
      } else {
         world.setBlockAndMetadataWithNotify(i, j, k, Block.fire.blockID, 0);
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.fireTextureArray = new Icon[]{
         register.registerIcon("fcBlockFireStokedStub_0", new StokedFireTexture("fcBlockFireStokedStub_0", 0)),
         register.registerIcon("fcBlockFireStokedStub_1", new StokedFireTexture("fcBlockFireStokedStub_1", 1))
      };
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon func_94438_c(int par1) {
      return this.fireTextureArray[par1];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return this.fireTextureArray[0];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      Tessellator tessellator = Tessellator.instance;
      Icon texture1 = this.func_94438_c(0);
      Icon texture2 = this.func_94438_c(1);
      if ((i + k & 1) != 0) {
         texture1 = this.func_94438_c(1);
         texture2 = this.func_94438_c(0);
      }

      tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tessellator.setBrightness(this.e(blockAccess, i, j, k));
      double dMinU = texture1.getMinU();
      double dMinV = texture1.getMinV();
      double dMaxU = texture1.getMaxU();
      double dMaxV = texture1.getMaxV();
      float fRenderHeight = 1.0F;
      double var18 = i + 0.5 + 0.2;
      double var20 = i + 0.5 - 0.2;
      double var22 = k + 0.5 + 0.2;
      double var24 = k + 0.5 - 0.2;
      double var26 = i + 0.5 - 0.3;
      double var28 = i + 0.5 + 0.3;
      double var30 = k + 0.5 - 0.3;
      double var32 = k + 0.5 + 0.3;
      tessellator.addVertexWithUV(var26, j + fRenderHeight, k + 1, dMaxU, dMinV);
      tessellator.addVertexWithUV(var18, j + 0, k + 1, dMaxU, dMaxV);
      tessellator.addVertexWithUV(var18, j + 0, k + 0, dMinU, dMaxV);
      tessellator.addVertexWithUV(var26, j + fRenderHeight, k + 0, dMinU, dMinV);
      tessellator.addVertexWithUV(var28, j + fRenderHeight, k + 0, dMaxU, dMinV);
      tessellator.addVertexWithUV(var20, j + 0, k + 0, dMaxU, dMaxV);
      tessellator.addVertexWithUV(var20, j + 0, k + 1, dMinU, dMaxV);
      tessellator.addVertexWithUV(var28, j + fRenderHeight, k + 1, dMinU, dMinV);
      dMinU = texture2.getMinU();
      dMinV = texture2.getMinV();
      dMaxU = texture2.getMaxU();
      dMaxV = texture2.getMaxV();
      tessellator.addVertexWithUV(i + 1, j + fRenderHeight, var32, dMaxU, dMinV);
      tessellator.addVertexWithUV(i + 1, j + 0, var24, dMaxU, dMaxV);
      tessellator.addVertexWithUV(i + 0, j + 0, var24, dMinU, dMaxV);
      tessellator.addVertexWithUV(i + 0, j + fRenderHeight, var32, dMinU, dMinV);
      tessellator.addVertexWithUV(i + 0, j + fRenderHeight, var30, dMaxU, dMinV);
      tessellator.addVertexWithUV(i + 0, j + 0, var22, dMaxU, dMaxV);
      tessellator.addVertexWithUV(i + 1, j + 0, var22, dMinU, dMaxV);
      tessellator.addVertexWithUV(i + 1, j + fRenderHeight, var30, dMinU, dMinV);
      var18 = i + 0.5 - 0.5;
      var20 = i + 0.5 + 0.5;
      var22 = k + 0.5 - 0.5;
      var24 = k + 0.5 + 0.5;
      var26 = i + 0.5 - 0.5;
      var28 = i + 0.5 + 0.5;
      var30 = k + 0.5 - 0.5;
      var32 = k + 0.5 + 0.5;
      tessellator.addVertexWithUV(var26, j + fRenderHeight, k + 0, dMinU, dMinV);
      tessellator.addVertexWithUV(var18, j + 0, k + 0, dMinU, dMaxV);
      tessellator.addVertexWithUV(var18, j + 0, k + 1, dMaxU, dMaxV);
      tessellator.addVertexWithUV(var26, j + fRenderHeight, k + 1, dMaxU, dMinV);
      tessellator.addVertexWithUV(var28, j + fRenderHeight, k + 1, dMinU, dMinV);
      tessellator.addVertexWithUV(var20, j + 0, k + 1, dMinU, dMaxV);
      tessellator.addVertexWithUV(var20, j + 0, k + 0, dMaxU, dMaxV);
      tessellator.addVertexWithUV(var28, j + fRenderHeight, k + 0, dMaxU, dMinV);
      dMinU = texture1.getMinU();
      dMinV = texture1.getMinV();
      dMaxU = texture1.getMaxU();
      dMaxV = texture1.getMaxV();
      tessellator.addVertexWithUV(i + 0, j + fRenderHeight, var32, dMinU, dMinV);
      tessellator.addVertexWithUV(i + 0, j + 0, var24, dMinU, dMaxV);
      tessellator.addVertexWithUV(i + 1, j + 0, var24, dMaxU, dMaxV);
      tessellator.addVertexWithUV(i + 1, j + fRenderHeight, var32, dMaxU, dMinV);
      tessellator.addVertexWithUV(i + 1, j + fRenderHeight, var30, dMinU, dMinV);
      tessellator.addVertexWithUV(i + 1, j + 0, var22, dMinU, dMaxV);
      tessellator.addVertexWithUV(i + 0, j + 0, var22, dMaxU, dMaxV);
      tessellator.addVertexWithUV(i + 0, j + fRenderHeight, var30, dMaxU, dMinV);
      return true;
   }
}
