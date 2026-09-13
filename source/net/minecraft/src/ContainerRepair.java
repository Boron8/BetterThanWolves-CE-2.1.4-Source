package net.minecraft.src;

import java.util.Map;

public class ContainerRepair extends Container {
   private IInventory outputSlot = new InventoryCraftResult();
   private IInventory inputSlots = new InventoryRepair(this, "Repair", true, 2);
   private World theWorld;
   private int field_82861_i;
   private int field_82858_j;
   private int field_82859_k;
   public int maximumCost = 0;
   private int stackSizeToBeUsedInRepair = 0;
   private String repairedItemName;
   private final EntityPlayer thePlayer;

   public ContainerRepair(InventoryPlayer var1, World var2, int var3, int var4, int var5, EntityPlayer var6) {
      this.theWorld = var2;
      this.field_82861_i = var3;
      this.field_82858_j = var4;
      this.field_82859_k = var5;
      this.thePlayer = var6;
      this.a(new Slot(this.inputSlots, 0, 27, 47));
      this.a(new Slot(this.inputSlots, 1, 76, 47));
      this.a(new SlotRepair(this, this.outputSlot, 2, 134, 47, var2, var3, var4, var5));

      for (int var7 = 0; var7 < 3; var7++) {
         for (int var8 = 0; var8 < 9; var8++) {
            this.a(new Slot(var1, var8 + var7 * 9 + 9, 8 + var8 * 18, 84 + var7 * 18));
         }
      }

      for (int var9 = 0; var9 < 9; var9++) {
         this.a(new Slot(var1, var9, 8 + var9 * 18, 142));
      }
   }

   @Override
   public void onCraftMatrixChanged(IInventory var1) {
      super.onCraftMatrixChanged(var1);
      if (var1 == this.inputSlots) {
         this.updateRepairOutput();
      }
   }

   public void updateRepairOutput() {
      ItemStack var1 = this.inputSlots.getStackInSlot(0);
      this.maximumCost = 0;
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      if (var1 == null) {
         this.outputSlot.setInventorySlotContents(0, null);
         this.maximumCost = 0;
      } else {
         ItemStack var5 = var1.copy();
         ItemStack var6 = this.inputSlots.getStackInSlot(1);
         Map var7 = EnchantmentHelper.getEnchantments(var5);
         boolean var8 = false;
         var3 += var1.getRepairCost() + (var6 == null ? 0 : var6.getRepairCost());
         this.stackSizeToBeUsedInRepair = 0;
         if (var6 != null) {
            var8 = var6.itemID == Item.enchantedBook.itemID && Item.enchantedBook.func_92110_g(var6).tagCount() > 0;
            if (var5.isItemStackDamageable() && Item.itemsList[var5.itemID].getIsRepairable(var1, var6)) {
               int var21 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);
               if (var21 <= 0) {
                  this.outputSlot.setInventorySlotContents(0, null);
                  this.maximumCost = 0;
                  return;
               }

               int var24;
               for (var24 = 0; var21 > 0 && var24 < var6.stackSize; var24++) {
                  int var29 = var5.getItemDamageForDisplay() - var21;
                  var5.setItemDamage(var29);
                  var2 += Math.max(1, var21 / 100) + var7.size();
                  var21 = Math.min(var5.getItemDamageForDisplay(), var5.getMaxDamage() / 4);
               }

               this.stackSizeToBeUsedInRepair = var24;
            } else {
               if (!var8 && (var5.itemID != var6.itemID || !var5.isItemStackDamageable())) {
                  this.outputSlot.setInventorySlotContents(0, null);
                  this.maximumCost = 0;
                  return;
               }

               if (var5.isItemStackDamageable() && !var8) {
                  int var9 = var1.getMaxDamage() - var1.getItemDamageForDisplay();
                  int var10 = var6.getMaxDamage() - var6.getItemDamageForDisplay();
                  int var11 = var10 + var5.getMaxDamage() * 12 / 100;
                  int var12 = var9 + var11;
                  int var13 = var5.getMaxDamage() - var12;
                  if (var13 < 0) {
                     var13 = 0;
                  }

                  if (var13 < var5.getItemDamage()) {
                     var5.setItemDamage(var13);
                     var2 += Math.max(1, var11 / 100);
                  }
               }

               Map var20 = EnchantmentHelper.getEnchantments(var6);

               for (int var28 : var20.keySet()) {
                  Enchantment var31 = Enchantment.enchantmentsList[var28];
                  int var33 = var7.containsKey(var28) ? (Integer)var7.get(var28) : 0;
                  int var14 = (Integer)var20.get(var28);
                  var14 = var33 == var14 ? ++var14 : Math.max(var14, var33);
                  int var15 = var14 - var33;
                  boolean var16 = var31.canApply(var1);
                  if (this.thePlayer.capabilities.isCreativeMode || var1.itemID == ItemEnchantedBook.bX.itemID) {
                     var16 = true;
                  }

                  for (int var18 : var7.keySet()) {
                     if (var18 != var28 && !var31.canApplyTogether(Enchantment.enchantmentsList[var18])) {
                        var16 = false;
                        var2 += var15;
                     }
                  }

                  if (var16) {
                     if (var14 > var31.getMaxLevel()) {
                        var14 = var31.getMaxLevel();
                     }

                     var7.put(var28, var14);
                     int var38 = 0;
                     switch (var31.getWeight()) {
                        case 1:
                           var38 = 8;
                           break;
                        case 2:
                           var38 = 4;
                        case 3:
                        case 4:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        default:
                           break;
                        case 5:
                           var38 = 2;
                           break;
                        case 10:
                           var38 = 1;
                     }

                     if (var8) {
                        var38 = Math.max(1, var38 / 2);
                     }

                     var2 += var38 * var15;
                  }
               }
            }
         }

         if (this.repairedItemName != null
            && this.repairedItemName.length() > 0
            && !this.repairedItemName.equalsIgnoreCase(this.thePlayer.getTranslator().translateNamedKey(var1.getItemName()))
            && !this.repairedItemName.equals(var1.getDisplayName())) {
            var4 = var1.isItemStackDamageable() ? 7 : var1.stackSize * 5;
            var2 += var4;
            if (var1.hasDisplayName()) {
               var3 += var4 / 2;
            }

            var5.setItemName(this.repairedItemName);
         }

         int var22 = 0;

         for (int var30 : var7.keySet()) {
            Enchantment var32 = Enchantment.enchantmentsList[var30];
            int var34 = (Integer)var7.get(var30);
            int var37 = 0;
            var22++;
            switch (var32.getWeight()) {
               case 1:
                  var37 = 8;
                  break;
               case 2:
                  var37 = 4;
               case 3:
               case 4:
               case 6:
               case 7:
               case 8:
               case 9:
               default:
                  break;
               case 5:
                  var37 = 2;
                  break;
               case 10:
                  var37 = 1;
            }

            if (var8) {
               var37 = Math.max(1, var37 / 2);
            }

            var3 += var22 + var34 * var37;
         }

         if (var8) {
            var3 = Math.max(1, var3 / 2);
         }

         this.maximumCost = var3 + var2;
         if (var2 <= 0) {
            var5 = null;
         }

         if (var4 == var2 && var4 > 0 && this.maximumCost >= 40) {
            this.theWorld.getWorldLogAgent().logInfo("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
            this.maximumCost = 39;
         }

         if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
            var5 = null;
         }

         if (var5 != null) {
            int var26 = var5.getRepairCost();
            if (var6 != null && var26 < var6.getRepairCost()) {
               var26 = var6.getRepairCost();
            }

            if (var5.hasDisplayName()) {
               var26 -= 9;
            }

            if (var26 < 0) {
               var26 = 0;
            }

            var26 += 2;
            var5.setRepairCost(var26);
            EnchantmentHelper.setEnchantments(var7, var5);
         }

         this.outputSlot.setInventorySlotContents(0, var5);
         this.b();
      }
   }

   @Override
   public void addCraftingToCrafters(ICrafting var1) {
      super.addCraftingToCrafters(var1);
      var1.sendProgressBarUpdate(this, 0, this.maximumCost);
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      if (var1 == 0) {
         this.maximumCost = var2;
      }
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer var1) {
      super.onCraftGuiClosed(var1);
      if (!this.theWorld.isRemote) {
         for (int var2 = 0; var2 < this.inputSlots.getSizeInventory(); var2++) {
            ItemStack var3 = this.inputSlots.getStackInSlotOnClosing(var2);
            if (var3 != null) {
               var1.dropPlayerItem(var3);
            }
         }
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.theWorld.getBlockId(this.field_82861_i, this.field_82858_j, this.field_82859_k) != Block.anvil.blockID
         ? false
         : !(var1.e(this.field_82861_i + 0.5, this.field_82858_j + 0.5, this.field_82859_k + 0.5) > 64.0);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 == 2) {
            if (!this.a(var5, 3, 39, true)) {
               return null;
            }

            var4.onSlotChange(var5, var3);
         } else if (var2 != 0 && var2 != 1) {
            if (var2 >= 3 && var2 < 39 && !this.a(var5, 0, 2, false)) {
               return null;
            }
         } else if (!this.a(var5, 3, 39, false)) {
            return null;
         }

         if (var5.stackSize == 0) {
            var4.putStack(null);
         } else {
            var4.onSlotChanged();
         }

         if (var5.stackSize == var3.stackSize) {
            return null;
         }

         var4.onPickupFromSlot(var1, var5);
      }

      return var3;
   }

   public void updateItemName(String var1) {
      this.repairedItemName = var1;
      if (this.a(2).getHasStack()) {
         this.a(2).getStack().setItemName(this.repairedItemName);
      }

      this.updateRepairOutput();
   }
}
