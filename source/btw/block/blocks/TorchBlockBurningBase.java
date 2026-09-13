package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class TorchBlockBurningBase extends TorchBlockBase {
   protected TorchBlockBurningBase(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public void onFluidFlowIntoBlock(World world, int i, int j, int k, BlockFluid newBlock) {
      if (newBlock.blockMaterial == Material.water) {
         world.playAuxSFX(2227, i, j, k, 0);
         this.b(world, i, j, k, new ItemStack(BTWBlocks.infiniteUnlitTorch.blockID, 1, 0));
      } else {
         super.onFluidFlowIntoBlock(world, i, j, k, newBlock);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      Vec3 pos = this.getParticalPos(world, i, j, k);
      world.spawnParticle("smoke", pos.xCoord, pos.yCoord, pos.zCoord, 0.0, 0.0, 0.0);
      world.spawnParticle("flame", pos.xCoord, pos.yCoord, pos.zCoord, 0.0, 0.0, 0.0);
   }

   @Environment(EnvType.CLIENT)
   protected Vec3 getParticalPos(World world, int i, int j, int k) {
      Vec3 pos = Vec3.createVectorHelper(i + 0.5, j + 0.92, k + 0.5);
      int iOrientation = this.getOrientation(world, i, j, k);
      double dHorizontalOffset = 0.27;
      if (iOrientation == 1) {
         pos.xCoord -= dHorizontalOffset;
      } else if (iOrientation == 2) {
         pos.xCoord += dHorizontalOffset;
      } else if (iOrientation == 3) {
         pos.zCoord -= dHorizontalOffset;
      } else if (iOrientation == 4) {
         pos.zCoord += dHorizontalOffset;
      } else {
         pos.yCoord -= 0.22;
      }

      return pos;
   }
}
