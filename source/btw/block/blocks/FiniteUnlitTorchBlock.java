package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class FiniteUnlitTorchBlock extends TorchBlockUnlitBase {
   @Environment(EnvType.CLIENT)
   private Icon burnedIcon;

   public FiniteUnlitTorchBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockTorchFiniteIdle");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return getIsBurnedOut(iMetadata) ? 0 : super.a(iMetadata, rand, iFortuneModifier);
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsBurnedOut(blockAccess, i, j, k);
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      if (!this.getIsBurnedOut(world, i, j, k)) {
         if (this.isRainingOnTorch(world, i, j, k)) {
            world.playAuxSFX(2227, i, j, k, 0);
            return true;
         } else {
            return super.setOnFireDirectly(world, i, j, k);
         }
      } else {
         return false;
      }
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsBurnedOut(blockAccess, i, j, k) ? 0 : super.getChanceOfFireSpreadingDirectlyTo(blockAccess, i, j, k);
   }

   @Override
   protected int getLitBlockID() {
      return BTWBlocks.finiteBurningTorch.blockID;
   }

   public void setIsBurnedOut(World world, int i, int j, int k, boolean bBurnedOut) {
      int iMetadata = setIsBurnedOut(world.getBlockMetadata(i, j, k), bBurnedOut);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public static int setIsBurnedOut(int iMetadata, boolean bIsBurnedOut) {
      if (bIsBurnedOut) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }

   public boolean getIsBurnedOut(IBlockAccess blockAccess, int i, int j, int k) {
      return getIsBurnedOut(blockAccess.getBlockMetadata(i, j, k));
   }

   public static boolean getIsBurnedOut(int iMetadata) {
      return (iMetadata & 8) != 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.burnedIcon = register.registerIcon("fcBlockTorchFiniteIdle_burned");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return getIsBurnedOut(iMetadata) ? this.burnedIcon : super.a(iSide, iMetadata);
   }
}
