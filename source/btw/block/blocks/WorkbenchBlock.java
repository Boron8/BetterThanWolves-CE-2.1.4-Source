package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import net.minecraft.src.BlockWorkbench;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class WorkbenchBlock extends BlockWorkbench {
   public WorkbenchBlock(int iBlockID) {
      super(iBlockID);
      this.setBlockMaterial(BTWBlocks.plankMaterial);
      this.c(1.5F);
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.WOOD_BASED_BLOCK);
      this.a(g);
      this.c("workbench");
      this.a(null);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j + 1, k, 0)
         ? true
         : super.onBlockActivated(world, i, j, k, player, iFacing, fClickX, fClickY, fClickZ);
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 3, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 1, 0, fChanceOfDrop);
      return true;
   }
}
