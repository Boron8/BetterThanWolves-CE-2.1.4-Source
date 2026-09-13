package btw.item.items;

import btw.world.util.BlockPos;
import java.util.List;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.Item;
import net.minecraft.src.ItemPotion;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class PotionItem extends ItemPotion {
   public PotionItem(int iItemID) {
      super(iItemID);
      this.setNeutralBuoyant();
      this.d(8);
      this.b("potion");
   }

   @Override
   public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
      if (!player.capabilities.isCreativeMode) {
         itemStack.stackSize--;
      }

      if (!world.isRemote) {
         List effectsList = this.g(itemStack);
         if (effectsList != null) {
            for (PotionEffect tempEffect : effectsList) {
               player.d(new PotionEffect(tempEffect));
            }
         }
      }

      if (!player.capabilities.isCreativeMode) {
         ItemStack bottleStack = new ItemStack(Item.glassBottle);
         if (!player.inventory.addItemStackToInventory(bottleStack)) {
            player.dropPlayerItem(bottleStack);
         }
      }

      return itemStack;
   }

   @Override
   public boolean isMultiUsePerClick() {
      return false;
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      if (ItemPotion.isSplash(stack.getItemDamage())) {
         BlockPos offsetPos = new BlockPos(0, 0, 0, iFacing);
         double dXPos = i + offsetPos.x * 0.6 + 0.5;
         double dYPos = j + offsetPos.y * 0.6 + 0.5;
         double dZPos = k + offsetPos.z * 0.6 + 0.5;
         double dYHeading;
         if (iFacing > 2) {
            dYHeading = 0.1F;
         } else {
            dYHeading = offsetPos.y;
         }

         EntityThrowable entity = (EntityThrowable)EntityList.createEntityOfType(
            EntityPotion.class, world, dXPos, dYPos, dZPos, new ItemStack(Item.potion, 1, stack.getItemDamage())
         );
         entity.setThrowableHeading(offsetPos.x, dYHeading, offsetPos.z, 1.375F, 3.0F);
         world.spawnEntityInWorld(entity);
         world.playAuxSFX(1002, i, j, k, 0);
         return true;
      } else {
         return super.onItemUsedByBlockDispenser(stack, world, i, j, k, iFacing);
      }
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!f(stack.getItemDamage()) && !player.canDrink()) {
         player.onCantConsume();
         return stack;
      } else {
         return super.onItemRightClick(stack, world, player);
      }
   }
}
