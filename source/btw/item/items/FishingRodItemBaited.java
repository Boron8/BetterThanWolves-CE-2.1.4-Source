package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityFishHook;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class FishingRodItemBaited extends FishingRodItem {
   public FishingRodItemBaited(int iItemID) {
      super(iItemID);
      this.b("fcItemFishingRodBaited");
      this.a(null);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (player.fishEntity != null) {
         int iItemDamage = player.fishEntity.catchFish();
         stack = player.getCurrentEquippedItem();
         stack.damageItem(iItemDamage, player);
         player.bK();
      } else {
         world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
         if (!world.isRemote) {
            world.spawnEntityInWorld(EntityList.createEntityOfType(EntityFishHook.class, world, player, true));
         }

         player.bK();
      }

      return stack;
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         player.playSound("mob.slime.attack", 0.25F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F + 0.7F);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getAnimationIcon(EntityPlayer player) {
      return player.getHeldItem() != null && player.getHeldItem().itemID == this.itemID && player.fishEntity != null ? this.getCastIcon() : this.itemIcon;
   }
}
