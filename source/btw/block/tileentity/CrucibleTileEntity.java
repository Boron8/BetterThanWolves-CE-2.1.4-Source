package btw.block.tileentity;

import btw.crafting.manager.BulkCraftingManager;
import btw.crafting.manager.CrucibleCraftingManager;
import btw.crafting.manager.CrucibleStokedCraftingManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class CrucibleTileEntity extends CookingVesselTileEntity {
   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.cookCounter = nbttagcompound.getInteger("m_iCrucibleCookCounter");
      if (nbttagcompound.hasKey("m_iStokedCooldownCounter")) {
         this.stokedCooldownCounter = nbttagcompound.getInteger("m_iStokedCooldownCounter");
      }

      if (nbttagcompound.hasKey("m_bContainsValidIngrediantsForState")) {
         this.containsValidIngredientsForState = nbttagcompound.getBoolean("m_bContainsValidIngrediantsForState");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("m_iCrucibleCookCounter", this.cookCounter);
      nbttagcompound.setInteger("m_iStokedCooldownCounter", this.stokedCooldownCounter);
   }

   @Override
   public String getInvName() {
      return "Crucible";
   }

   @Override
   public boolean isStackValidForSlot(int iSlot, ItemStack stack) {
      return true;
   }

   @Override
   public boolean isInvNameLocalized() {
      return true;
   }

   @Override
   public void validateContentsForState() {
      this.containsValidIngredientsForState = false;
      if (this.fireUnderType == 1) {
         if (CrucibleCraftingManager.getInstance().getCraftingResult(this) != null) {
            this.containsValidIngredientsForState = true;
         }
      } else if (this.fireUnderType == 2) {
         if (this.doesContainExplosives()) {
            this.containsValidIngredientsForState = true;
         } else if (CrucibleStokedCraftingManager.getInstance().getCraftingResult(this) != null) {
            this.containsValidIngredientsForState = true;
         } else if (this.getFirstStackThatContainsItemsDestroyedByStokedFire() >= 0) {
            this.containsValidIngredientsForState = true;
         }
      }
   }

   @Override
   protected BulkCraftingManager getCraftingManager(int iFireType) {
      if (iFireType == 1) {
         return CrucibleCraftingManager.getInstance();
      } else {
         return iFireType == 2 ? CrucibleStokedCraftingManager.getInstance() : null;
      }
   }

   @Override
   protected boolean attemptToCookStoked() {
      int iBurnableSlot = this.getFirstStackThatContainsItemsDestroyedByStokedFire();
      if (iBurnableSlot >= 0) {
         this.a(iBurnableSlot, 1);
         return true;
      } else {
         return super.attemptToCookStoked();
      }
   }

   private int getFirstStackThatContainsItemsDestroyedByStokedFire() {
      for (int i = 0; i < this.j_(); i++) {
         if (this.a(i) != null && this.a(i).getItem().isIncineratedInCrucible()) {
            return i;
         }
      }

      return -1;
   }
}
