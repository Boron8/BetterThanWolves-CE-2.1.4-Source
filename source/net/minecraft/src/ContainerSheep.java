package net.minecraft.src;

class ContainerSheep extends Container {
   ContainerSheep(EntitySheep var1) {
      this.field_90034_a = var1;
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return false;
   }
}
