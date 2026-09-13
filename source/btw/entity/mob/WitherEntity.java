package btw.entity.mob;

import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityWither;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WitherEntity extends EntityWither {
   public WitherEntity(World world) {
      super(world);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.tasks.addTask(5, new SimpleWanderBehavior(this, this.moveSpeed));
   }

   @Override
   public void checkForScrollDrop() {
      ItemStack stack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.knockback.effectId);
      this.a(stack, 0.0F);
   }

   @Override
   protected void modSpecificOnLivingUpdate() {
      super.modSpecificOnLivingUpdate();
      if (!this.worldObj.isRemote) {
         WorldUtils.gameProgressSetWitherHasBeenSummonedServerOnly();
      }
   }
}
