package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;

public class BarkItem extends Item {
   public static final int SUBTYPE_OAK = 0;
   public static final int SUBTYPE_SPRUCE = 1;
   public static final int SUBTYPE_BIRCH = 2;
   public static final int SUBTYPE_JUNGLE = 3;
   public static final int SUBTYPE_BLOOD_WOOD = 4;
   public static final int NUM_SUBTYPES = 5;
   private String[] nameExtensionsBySubtype = new String[]{"oak", "spruce", "birch", "jungle", "bloodwood"};
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySubtype = new Icon[5];
   @Environment(EnvType.CLIENT)
   private String[] iconNamesBySubtype = new String[]{"fcItemBarkOak", "fcItemBarkSpruce", "fcItemBarkBirch", "fcItemBarkJungle", "fcItemBarkBloodWood"};

   public BarkItem(int iItemID) {
      super(iItemID);
      this.a(true);
      this.e(0);
      this.setBuoyant();
      this.setBellowsBlowDistance(2);
      this.setfurnaceburntime(FurnaceBurnTime.KINDLING);
      this.setFilterableProperties(18);
      this.b("fcItemBark");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public String getUnlocalizedName(ItemStack stack) {
      int iSubtype = MathHelper.clamp_int(stack.getItemDamage(), 0, 4);
      return super.getUnlocalizedName() + "." + this.nameExtensionsBySubtype[iSubtype];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      for (int iTempSubtype = 0; iTempSubtype < this.iconNamesBySubtype.length; iTempSubtype++) {
         this.iconBySubtype[iTempSubtype] = par1IconRegister.registerIcon(this.iconNamesBySubtype[iTempSubtype]);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int iDamage) {
      int iIconIndex = MathHelper.clamp_int(iDamage, 0, 4);
      return this.iconBySubtype[iIconIndex];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubItems(int iItemID, CreativeTabs creativeTabs, List list) {
      for (int iSubtype = 0; iSubtype < 5; iSubtype++) {
         list.add(new ItemStack(iItemID, 1, iSubtype));
      }
   }
}
