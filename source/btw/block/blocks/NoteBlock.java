package btw.block.blocks;

import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import net.minecraft.src.BlockNote;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityNote;
import net.minecraft.src.World;

public class NoteBlock extends BlockNote {
   public NoteBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.8F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.WOOD_BASED_BLOCK);
      this.c("musicBlock");
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote) {
         ItemStack playerItem = player.getCurrentEquippedItem();
         if (playerItem != null && playerItem.getItem().itemID == BTWItems.tuningFork.itemID) {
            TileEntityNote tileEnt = (TileEntityNote)world.getBlockTileEntity(i, j, k);
            if (tileEnt != null) {
               tileEnt.note = (byte)playerItem.getItemDamage();
               tileEnt.triggerNote(world, i, j, k);
            }

            return true;
         }
      }

      return super.onBlockActivated(world, i, j, k, player, iFacing, fXClick, fYClick, fZClick);
   }

   @Override
   public boolean isIncineratedInCrucible() {
      return false;
   }
}
