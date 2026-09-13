package btw.item.items;

import btw.crafting.recipe.types.customcrafting.FishingRodBaitingRecipe;
import btw.item.BTWItems;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemFishingRod;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class FishingRodItem extends ItemFishingRod {
   public FishingRodItem(int iItemID) {
      super(iItemID);
      this.e(32);
      this.setBuoyant();
      this.setFilterableProperties(4);
      this.b("fishingRod");
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      boolean appliedBait = false;
      if (player.fishEntity == null) {
         for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = player.inventory.getStackInSlot(i);
            if (stackInSlot != null && FishingRodBaitingRecipe.isFishingBait(stackInSlot)) {
               world.playSoundAtEntity(player, "mob.slime.attack", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
               player.inventory.consumeInventoryItem(stackInSlot.itemID);
               ItemStack baitedRodStack = player.getCurrentEquippedItem().copy();
               baitedRodStack.itemID = BTWItems.baitedFishingRod.itemID;
               player.inventory.setInventorySlotContents(player.inventory.currentItem, baitedRodStack);
               appliedBait = true;
               break;
            }
         }

         if (appliedBait) {
            return stack;
         }
      }

      player.bK();
      return super.onItemRightClick(stack, world, player);
   }

   public Icon getCastIcon() {
      return this.g();
   }

   @Override
   public Icon getAnimationIcon(EntityPlayer player) {
      return player.getHeldItem() != null && player.getHeldItem().itemID == this.itemID && player.fishEntity != null ? this.getCastIcon() : this.itemIcon;
   }
}
