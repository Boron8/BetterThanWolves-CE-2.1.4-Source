package btw.item.blockitems.legacy;

import btw.block.BTWBlocks;
import btw.block.blocks.legacy.LegacyCornerBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemBlock;

public class LegacyCornerBlockItem extends ItemBlock {
   public LegacyCornerBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcCorner");
   }

   @Override
   public int getMetadata(int iDamage) {
      return iDamage;
   }

   @Override
   public float getBuoyancy(int iItemDamage) {
      return iItemDamage > 0 ? -1.0F : super.getBuoyancy(iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int iDamage) {
      return iDamage > 0 ? BTWBlocks.legacyStoneAndOakCorner.blockIcon : ((LegacyCornerBlock)BTWBlocks.legacyStoneAndOakCorner).iconWood;
   }
}
