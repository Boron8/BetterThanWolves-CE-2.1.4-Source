package btw.item.items;

import btw.entity.mob.CowEntity;
import btw.entity.mob.PigEntity;
import btw.entity.mob.SheepEntity;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class BreedingHarnessItem extends Item {
   public BreedingHarnessItem(int itemID) {
      super(itemID);
      this.setBuoyant();
      this.b("fcItemHarnessBreeding");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean itemInteractionForEntity(ItemStack itemStack, EntityLiving targetEntity) {
      if (targetEntity instanceof EntityAnimal) {
         EntityAnimal animal = (EntityAnimal)targetEntity;
         if (!animal.h_() && !animal.getWearingBreedingHarness()) {
            if (targetEntity instanceof SheepEntity) {
               if (!animal.worldObj.isRemote) {
                  SheepEntity sheep = (SheepEntity)animal;
                  sheep.i(true);
               }
            } else if (!(targetEntity instanceof PigEntity) && !(targetEntity instanceof CowEntity)) {
               return false;
            }

            itemStack.stackSize--;
            if (!animal.worldObj.isRemote) {
               animal.setWearingBreedingHarness(true);
            }

            return true;
         }
      }

      return false;
   }
}
