package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class NameTagItem extends Item {
   public NameTagItem(int id) {
      super(id);
      this.b("fcItemNameTag");
      this.d(1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean itemInteractionForEntity(ItemStack stack, EntityLiving entity) {
      String name = stack.getDisplayName();
      if (!name.equals("") && stack.hasDisplayName()) {
         entity.func_94058_c(name);
         entity.setPersistent(true);
         stack.stackSize--;
         return true;
      } else {
         return false;
      }
   }
}
