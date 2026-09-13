package net.minecraft.src;

import btw.item.util.ItemUtils;

public class SlotCrafting extends Slot {
   private final IInventory craftMatrix;
   private EntityPlayer thePlayer;
   private IRecipe currentRecipe = null;
   private int amountCrafted;

   public SlotCrafting(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
      super(par3IInventory, par4, par5, par6);
      this.thePlayer = par1EntityPlayer;
      this.craftMatrix = par2IInventory;
   }

   @Override
   public boolean isItemValid(ItemStack par1ItemStack) {
      return false;
   }

   @Override
   public ItemStack decrStackSize(int par1) {
      if (this.d()) {
         this.amountCrafted = this.amountCrafted + Math.min(par1, this.c().stackSize);
      }

      return super.decrStackSize(par1);
   }

   @Override
   protected void onCrafting(ItemStack par1ItemStack, int par2) {
      this.amountCrafted += par2;
      this.onCrafting(par1ItemStack);
   }

   @Override
   protected void onCrafting(ItemStack par1ItemStack) {
      par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
      this.amountCrafted = 0;
      if (par1ItemStack.itemID == Block.workbench.blockID) {
         this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
      } else if (par1ItemStack.itemID == Item.pickaxeWood.itemID) {
         this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
      } else if (par1ItemStack.itemID == Block.furnaceIdle.blockID) {
         this.thePlayer.addStat(AchievementList.buildFurnace, 1);
      } else if (par1ItemStack.itemID == Item.hoeWood.itemID) {
         this.thePlayer.addStat(AchievementList.buildHoe, 1);
      } else if (par1ItemStack.itemID == Item.bread.itemID) {
         this.thePlayer.addStat(AchievementList.makeBread, 1);
      } else if (par1ItemStack.itemID == Item.cake.itemID) {
         this.thePlayer.addStat(AchievementList.bakeCake, 1);
      } else if (par1ItemStack.itemID == Item.pickaxeStone.itemID) {
         this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
      } else if (par1ItemStack.itemID == Item.swordWood.itemID) {
         this.thePlayer.addStat(AchievementList.buildSword, 1);
      } else if (par1ItemStack.itemID == Block.enchantmentTable.blockID) {
         this.thePlayer.addStat(AchievementList.enchantments, 1);
      } else if (par1ItemStack.itemID == Block.bookShelf.blockID) {
         this.thePlayer.addStat(AchievementList.bookcase, 1);
      }
   }

   @Override
   public void onPickupFromSlot(EntityPlayer player, ItemStack par2ItemStack) {
      this.onCrafting(par2ItemStack);
      if (!player.worldObj.isRemote && this.currentRecipe.getSecondaryOutput(this.craftMatrix) != null) {
         for (ItemStack stack : this.currentRecipe.getSecondaryOutput(this.craftMatrix)) {
            ItemUtils.ejectStackWithRandomVelocity(player.worldObj, player.posX, player.posY, player.posZ, stack.copy());
         }
      }

      for (int var3 = 0; var3 < this.craftMatrix.getSizeInventory(); var3++) {
         ItemStack var4 = this.craftMatrix.getStackInSlot(var3);
         if (var4 != null) {
            var4.getItem().onUsedInCrafting(var4.getItemDamage(), player, par2ItemStack);
            if (var4.getItem().isConsumedInCrafting()) {
               if (var4.getItem().isDamagedInCrafting()) {
                  if (var4.getItemDamage() >= var4.getMaxDamage() - 1) {
                     var4.getItem().onBrokenInCrafting(player);
                     this.craftMatrix.decrStackSize(var3, 1);
                  } else {
                     var4.getItem().onDamagedInCrafting(player);
                     var4.damageItem(1, player);
                  }
               } else {
                  this.craftMatrix.decrStackSize(var3, 1);
                  if (var4.getItem().hasContainerItem() && !par2ItemStack.getItem().doesConsumeContainerItemWhenCrafted(var4.getItem().getContainerItem())) {
                     ItemStack var5 = new ItemStack(var4.getItem().getContainerItem());
                     if (!var4.getItem().doesContainerItemLeaveCraftingGrid(var4) || !this.thePlayer.inventory.addItemStackToInventory(var5)) {
                        if (this.craftMatrix.getStackInSlot(var3) == null) {
                           this.craftMatrix.setInventorySlotContents(var3, var5);
                        } else {
                           this.thePlayer.dropPlayerItem(var5);
                        }
                     }
                  }
               }
            }
         }
      }

      player.timesCraftedThisTick++;
   }

   public void setRecipe(IRecipe recipe) {
      this.currentRecipe = recipe;
   }
}
