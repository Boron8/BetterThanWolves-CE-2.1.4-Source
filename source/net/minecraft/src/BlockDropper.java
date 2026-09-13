package net.minecraft.src;

public class BlockDropper extends BlockDispenser {
   private final IBehaviorDispenseItem dropperDefaultBehaviour = new BehaviorDefaultDispenseItem();

   protected BlockDropper(int var1) {
      super(var1);
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon("furnace_side");
      this.furnaceTopIcon = var1.registerIcon("furnace_top");
      this.furnaceFrontIcon = var1.registerIcon("dropper_front");
      this.field_96473_e = var1.registerIcon("dropper_front_vertical");
   }

   @Override
   protected IBehaviorDispenseItem getBehaviorForItemStack(ItemStack var1) {
      return this.dropperDefaultBehaviour;
   }

   @Override
   public TileEntity createNewTileEntity(World var1) {
      return new TileEntityDropper();
   }

   @Override
   protected void dispense(World var1, int var2, int var3, int var4) {
      BlockSourceImpl var5 = new BlockSourceImpl(var1, var2, var3, var4);
      TileEntityDispenser var6 = (TileEntityDispenser)var5.getBlockTileEntity();
      if (var6 != null) {
         int var7 = var6.getRandomStackFromInventory();
         if (var7 < 0) {
            var1.playAuxSFX(1001, var2, var3, var4, 0);
         } else {
            ItemStack var8 = var6.getStackInSlot(var7);
            int var9 = var1.getBlockMetadata(var2, var3, var4) & 7;
            IInventory var10 = TileEntityHopper.getInventoryAtLocation(
               var1, var2 + Facing.offsetsXForSide[var9], var3 + Facing.offsetsYForSide[var9], var4 + Facing.offsetsZForSide[var9]
            );
            ItemStack var12;
            if (var10 != null) {
               var12 = TileEntityHopper.insertStack(var10, var8.copy().splitStack(1), Facing.oppositeSide[var9]);
               if (var12 == null) {
                  var12 = var8.copy();
                  if (--var12.stackSize == 0) {
                     var12 = null;
                  }
               } else {
                  var12 = var8.copy();
               }
            } else {
               var12 = this.dropperDefaultBehaviour.dispense(var5, var8);
               if (var12 != null && var12.stackSize == 0) {
                  var12 = null;
               }
            }

            var6.setInventorySlotContents(var7, var12);
         }
      }
   }
}
