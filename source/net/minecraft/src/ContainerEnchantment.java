package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ContainerEnchantment extends Container {
   public IInventory tableInventory = new SlotEnchantmentTable(this, "Enchant", true, 1);
   private World worldPointer;
   private int posX;
   private int posY;
   private int posZ;
   private Random rand = new Random();
   public long nameSeed;
   public int[] enchantLevels = new int[3];

   public ContainerEnchantment(InventoryPlayer par1InventoryPlayer, World par2World, int par3, int par4, int par5) {
      this.worldPointer = par2World;
      this.posX = par3;
      this.posY = par4;
      this.posZ = par5;
      this.a(new SlotEnchantment(this, this.tableInventory, 0, 25, 47));

      for (int var6 = 0; var6 < 3; var6++) {
         for (int var7 = 0; var7 < 9; var7++) {
            this.a(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
         }
      }

      for (int var8 = 0; var8 < 9; var8++) {
         this.a(new Slot(par1InventoryPlayer, var8, 8 + var8 * 18, 142));
      }
   }

   @Override
   public void addCraftingToCrafters(ICrafting par1ICrafting) {
      super.addCraftingToCrafters(par1ICrafting);
      par1ICrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
      par1ICrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
      par1ICrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (int var1 = 0; var1 < this.crafters.size(); var1++) {
         ICrafting var2 = (ICrafting)this.crafters.get(var1);
         var2.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
         var2.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
         var2.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int par1, int par2) {
      if (par1 >= 0 && par1 <= 2) {
         this.enchantLevels[par1] = par2;
      } else {
         super.updateProgressBar(par1, par2);
      }
   }

   @Override
   public void onCraftMatrixChanged(IInventory inventory) {
      if (inventory == this.tableInventory) {
         ItemStack stackToEnchant = inventory.getStackInSlot(0);
         if (stackToEnchant != null && stackToEnchant.isItemEnchantable()) {
            this.nameSeed = this.rand.nextLong();
            if (!this.worldPointer.isRemote) {
               int numBookshelves = 0;

               for (int i = -1; i <= 1; i++) {
                  for (int k = -1; k <= 1; k++) {
                     if (k != 0 || i != 0) {
                        for (int j = -1; j <= 2; j++) {
                           if (this.worldPointer.isAirBlock(this.posX + i, this.posY + j, this.posZ + k)) {
                              if (this.worldPointer.getBlockId(this.posX + i * 2, this.posY + j, this.posZ + k * 2) == Block.bookShelf.blockID) {
                                 numBookshelves++;
                              }

                              if (i != 0 && k != 0) {
                                 if (this.worldPointer.getBlockId(this.posX + i * 2, this.posY + j, this.posZ + k) == Block.bookShelf.blockID) {
                                    numBookshelves++;
                                 }

                                 if (this.worldPointer.getBlockId(this.posX + i, this.posY + j, this.posZ + k * 2) == Block.bookShelf.blockID) {
                                    numBookshelves++;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (numBookshelves > 30) {
                  numBookshelves = 30;
               }

               for (int i = 0; i < 3; i++) {
                  this.enchantLevels[i] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i, numBookshelves, stackToEnchant);
               }

               this.detectAndSendChanges();
            }
         } else {
            for (int i = 0; i < 3; i++) {
               this.enchantLevels[i] = 0;
            }
         }
      }
   }

   @Override
   public boolean enchantItem(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack var3 = this.tableInventory.getStackInSlot(0);
      if (this.enchantLevels[par2] > 0
         && var3 != null
         && (par1EntityPlayer.experienceLevel >= this.enchantLevels[par2] || par1EntityPlayer.capabilities.isCreativeMode)) {
         if (!this.worldPointer.isRemote) {
            List var4 = EnchantmentHelper.buildEnchantmentList(this.rand, var3, this.enchantLevels[par2]);
            boolean var5 = var3.itemID == Item.book.itemID;
            if (var4 != null) {
               par1EntityPlayer.addExperienceLevel(-this.enchantLevels[par2]);
               if (var5) {
                  var3.itemID = Item.enchantedBook.itemID;
               }

               int var6 = var5 ? this.rand.nextInt(var4.size()) : -1;

               for (int var7 = 0; var7 < var4.size(); var7++) {
                  EnchantmentData var8 = (EnchantmentData)var4.get(var7);
                  if (!var5 || var7 == var6) {
                     if (var5) {
                        Item.enchantedBook.func_92115_a(var3, var8);
                     } else {
                        var3.addEnchantment(var8.enchantmentobj, var8.enchantmentLevel);
                     }
                  }
               }

               this.onCraftMatrixChanged(this.tableInventory);
               this.worldPointer.playSoundAtEntity(par1EntityPlayer, "random.levelup", 0.25F, this.worldPointer.rand.nextFloat() * 0.1F + 0.5F);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
      super.onCraftGuiClosed(par1EntityPlayer);
      if (!this.worldPointer.isRemote) {
         ItemStack var2 = this.tableInventory.getStackInSlotOnClosing(0);
         if (var2 != null) {
            par1EntityPlayer.dropPlayerItem(var2);
         }
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.worldPointer.getBlockId(this.posX, this.posY, this.posZ) != Block.enchantmentTable.blockID
         ? false
         : par1EntityPlayer.e(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5) <= 64.0;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(par2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (par2 == 0) {
            if (!this.a(var5, 1, 37, true)) {
               return null;
            }
         } else {
            if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(var5)) {
               return null;
            }

            if (var5.hasTagCompound() && var5.stackSize == 1) {
               ((Slot)this.inventorySlots.get(0)).putStack(var5.copy());
               var5.stackSize = 0;
            } else if (var5.stackSize >= 1) {
               ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(var5.itemID, 1, var5.getItemDamage()));
               var5.stackSize--;
            }
         }

         if (var5.stackSize == 0) {
            var4.putStack((ItemStack)null);
         } else {
            var4.onSlotChanged();
         }

         if (var5.stackSize == var3.stackSize) {
            return null;
         }

         var4.onPickupFromSlot(par1EntityPlayer, var5);
      }

      return var3;
   }
}
