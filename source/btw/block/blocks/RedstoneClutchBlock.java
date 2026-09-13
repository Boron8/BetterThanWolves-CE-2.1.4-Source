package btw.block.blocks;

import java.util.Random;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class RedstoneClutchBlock extends GearBoxBlock {
   public RedstoneClutchBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockRedstoneClutch");
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      boolean bMechPowered = this.isInputtingMechanicalPower(world, i, j, k);
      if (bMechPowered && (world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k))) {
         bMechPowered = false;
      }

      this.updateMechPoweredState(world, i, j, k, bMechPowered);
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      super.dropComponentItemsOnBadBreak(world, i, j, k, iMetadata, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.goldNugget.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean isIncineratedInCrucible() {
      return false;
   }

   @Override
   protected boolean isCurrentStateValid(World world, int i, int j, int k) {
      boolean bMechPowered = this.isInputtingMechanicalPower(world, i, j, k);
      if (bMechPowered && (world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k))) {
         bMechPowered = false;
      }

      return this.isGearBoxOn(world, i, j, k) == bMechPowered;
   }
}
