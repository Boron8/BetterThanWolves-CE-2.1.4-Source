package btw.item.blockitems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class SaplingBlockItem extends ItemBlock {
   private final Block block;

   public SaplingBlockItem(int itemID, Block block) {
      super(itemID);
      this.block = block;
      this.e(0);
      this.a(true);
   }

   @Override
   public int getMetadata(int itemDamage) {
      return itemDamage;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int itemDamage) {
      return this.block.getIcon(2, itemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getUnlocalizedName(ItemStack itemStack) {
      int itemDamage = itemStack.getItemDamage();
      return itemDamage >= 7 ? super.getUnlocalizedName() + ".mature" : super.getUnlocalizedName();
   }
}
