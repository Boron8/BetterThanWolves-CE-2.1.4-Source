package btw.entity.mob;

import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityBat;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BatEntity extends EntityBat {
   public BatEntity(World world) {
      super(world);
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(250) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.featherFalling.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   protected void dropFewItems(boolean bPlayerKilled, int iFortuneLevel) {
      int iNumDrop = 1;
      if (this.rand.nextInt(4) - iFortuneLevel <= 0) {
         iNumDrop = 2;
      }

      for (int iTempCount = 0; iTempCount < iNumDrop; iTempCount++) {
         this.b(BTWItems.batWing.itemID, 1);
      }
   }

   @Override
   public boolean attractsLightning() {
      return false;
   }
}
