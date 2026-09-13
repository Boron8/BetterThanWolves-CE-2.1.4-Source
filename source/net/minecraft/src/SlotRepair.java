package net.minecraft.src;

class SlotRepair extends Slot {
   SlotRepair(ContainerRepair var1, IInventory var2, int var3, int var4, int var5, World var6, int var7, int var8, int var9) {
      super(var2, var3, var4, var5);
      this.anvil = var1;
      this.theWorld = var6;
      this.blockPosX = var7;
      this.blockPosY = var8;
      this.blockPosZ = var9;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return false;
   }

   @Override
   public boolean canTakeStack(EntityPlayer var1) {
      return (var1.capabilities.isCreativeMode || var1.experienceLevel >= this.anvil.maximumCost) && this.anvil.maximumCost > 0 && this.d();
   }

   @Override
   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      if (!var1.capabilities.isCreativeMode) {
         var1.addExperienceLevel(-this.anvil.maximumCost);
      }

      ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(0, null);
      if (ContainerRepair.getStackSizeUsedInRepair(this.anvil) > 0) {
         ItemStack var3 = ContainerRepair.getRepairInputInventory(this.anvil).getStackInSlot(1);
         if (var3 != null && var3.stackSize > ContainerRepair.getStackSizeUsedInRepair(this.anvil)) {
            var3.stackSize = var3.stackSize - ContainerRepair.getStackSizeUsedInRepair(this.anvil);
            ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, var3);
         } else {
            ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, null);
         }
      } else {
         ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, null);
      }

      this.anvil.maximumCost = 0;
      if (!var1.capabilities.isCreativeMode
         && !this.theWorld.isRemote
         && this.theWorld.getBlockId(this.blockPosX, this.blockPosY, this.blockPosZ) == Block.anvil.blockID
         && var1.aE().nextFloat() < 0.12F) {
         int var6 = this.theWorld.getBlockMetadata(this.blockPosX, this.blockPosY, this.blockPosZ);
         int var4 = var6 & 3;
         int var5 = var6 >> 2;
         if (++var5 > 2) {
            this.theWorld.setBlockToAir(this.blockPosX, this.blockPosY, this.blockPosZ);
            this.theWorld.playAuxSFX(1020, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
         } else {
            this.theWorld.setBlockMetadataWithNotify(this.blockPosX, this.blockPosY, this.blockPosZ, var4 | var5 << 2, 2);
            this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
         }
      } else if (!this.theWorld.isRemote) {
         this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
      }
   }
}
