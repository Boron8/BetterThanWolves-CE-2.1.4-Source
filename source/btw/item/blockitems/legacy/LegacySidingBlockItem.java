package btw.item.blockitems.legacy;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemBlock;

public class LegacySidingBlockItem extends ItemBlock {
   public LegacySidingBlockItem(int i) {
      super(i);
      this.e(0);
      this.a(true);
      this.b("fcBlockOmniSlab");
   }

   @Override
   public int getMetadata(int i) {
      return i;
   }

   @Override
   public float getBuoyancy(int iItemDamage) {
      return iItemDamage == 0 ? -1.0F : super.getBuoyancy(iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int i) {
      return BTWBlocks.legacyStoneAndOakSiding.getIcon(2, i);
   }
}
