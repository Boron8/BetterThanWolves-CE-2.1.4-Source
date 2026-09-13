package btw.entity.mob;

import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class MagmaCubeEntity extends EntityMagmaCube {
   public MagmaCubeEntity(World world) {
      super(world);
      this.landMovementFactor = 0.5F;
   }

   @Override
   protected boolean canDamagePlayer() {
      return this.R() && this.attackTime <= 0;
   }

   @Override
   public void checkForScrollDrop() {
      if (this.p() == 1 && this.rand.nextInt(250) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.fireAspect.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   protected EntitySlime createInstance() {
      return (EntitySlime)EntityList.createEntityOfType(MagmaCubeEntity.class, this.worldObj);
   }
}
