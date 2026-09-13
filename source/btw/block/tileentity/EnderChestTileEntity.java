package btw.block.tileentity;

import net.minecraft.src.InventoryEnderChest;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntityEnderChest;

public class EnderChestTileEntity extends TileEntityEnderChest {
   private InventoryEnderChest localChestInventory = new InventoryEnderChest();

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.a(tag);
      if (tag.hasKey("FCEnderItems")) {
         NBTTagList itemList = tag.getTagList("FCEnderItems");
         this.localChestInventory.loadInventoryFromNBT(itemList);
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.b(tag);
      if (this.localChestInventory != null) {
         tag.setTag("FCEnderItems", this.localChestInventory.saveInventoryToNBT());
      }
   }

   public InventoryEnderChest getLocalEnderChestInventory() {
      return this.localChestInventory;
   }
}
