package btw.item.items;

import java.util.List;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;

public class StoneBrickItem extends Item {
   private Icon[] iconByMetadataArray = new Icon[3];

   public StoneBrickItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public Icon getIconFromDamage(int metadata) {
      int var2 = MathHelper.clamp_int(metadata, 0, 2);
      return this.iconByMetadataArray[var2];
   }

   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconByMetadataArray[0] = this.itemIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcItemBrickStone_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcItemBrickStone_2");
   }

   @Override
   public void getSubItems(int par1, CreativeTabs tab, List list) {
      list.add(new ItemStack(par1, 1, 0));
      list.add(new ItemStack(par1, 1, 1));
      list.add(new ItemStack(par1, 1, 2));
   }
}
