package btw.inventory.container;

import btw.block.tileentity.CookingVesselTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class CookingVesselContainer extends InventoryContainer {
   private CookingVesselTileEntity associatedTileEntity;
   private int lastCookCounter;

   public CookingVesselContainer(IInventory playerinventory, CookingVesselTileEntity tileEntity) {
      super(playerinventory, tileEntity, 3, 9, 8, 43, 8, 111);
      this.associatedTileEntity = tileEntity;
      this.lastCookCounter = 0;
   }

   @Override
   public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer) {
      ItemStack returnValue = super.a(i, j, k, entityplayer);
      this.associatedTileEntity.onInventoryChanged();
      return returnValue;
   }

   @Override
   public void addCraftingToCrafters(ICrafting craftingInterface) {
      super.a(craftingInterface);
      craftingInterface.sendProgressBarUpdate(this, 0, this.associatedTileEntity.scaledCookCounter);
   }

   @Override
   public void detectAndSendChanges() {
      super.b();

      for (ICrafting icrafting : this.crafters) {
         if (this.lastCookCounter != this.associatedTileEntity.scaledCookCounter) {
            icrafting.sendProgressBarUpdate(this, 0, this.associatedTileEntity.scaledCookCounter);
         }
      }

      this.lastCookCounter = this.associatedTileEntity.scaledCookCounter;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int iVariableIndex, int iValue) {
      if (iVariableIndex == 0) {
         this.associatedTileEntity.scaledCookCounter = iValue;
      }
   }
}
