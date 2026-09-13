package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WickerWeavingItem extends ProgressiveCraftingItem {
   public static final int WICKER_WEAVING_MAX_DAMAGE = 300;

   public WickerWeavingItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setBellowsBlowDistance(2);
      this.setIncineratedInCrucible();
      this.setfurnaceburntime(FurnaceBurnTime.WICKER_PIECE);
      this.setFilterableProperties(16);
      this.b("fcItemWickerWeaving");
   }

   @Override
   protected void playCraftingFX(ItemStack stack, World world, EntityPlayer player) {
      player.playSound("step.grass", 0.25F + 0.25F * world.rand.nextInt(2), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.25F + 1.75F);
   }

   @Override
   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      world.playSoundAtEntity(player, "step.grass", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
      return new ItemStack(BTWItems.wickerPane, 1, 0);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         player.playSound("step.grass", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
      }

      super.d(stack, world, player);
   }

   @Override
   public boolean getCanBeFedDirectlyIntoCampfire(int iItemDamage) {
      return false;
   }

   @Override
   public boolean getCanBeFedDirectlyIntoBrickOven(int iItemDamage) {
      return false;
   }

   @Override
   protected int getProgressiveCraftingMaxDamage() {
      return 300;
   }
}
