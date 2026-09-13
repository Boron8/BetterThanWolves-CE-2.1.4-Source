package btw.item.items;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class TuningForkItem extends Item {
   public static final String[] pitchNames = new String[]{
      "0 - F#",
      "1 - G",
      "2 - G#",
      "3 - A",
      "4 - A#",
      "5 - B",
      "6 - C",
      "7 - C#",
      "8 - D",
      "9 - D#",
      "10 - E",
      "11 - F",
      "12 - F#",
      "13 - G",
      "14 - G#",
      "15 - A",
      "16 - A#",
      "17 - B",
      "18 - C",
      "19 - C#",
      "20 - D",
      "21 - D#",
      "22 - E",
      "23 - F",
      "24 - F#"
   };

   public TuningForkItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcItemTuningFork");
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      byte note = (byte)itemStack.getItemDamage();
      float f = (float)Math.pow(2.0, (note - 12) / 12.0);
      String s = "harp";
      Material material = world.getBlockMaterial(i, j, k);
      byte byte0 = 0;
      if (material == Material.rock) {
         byte0 = 1;
      }

      if (material == Material.sand) {
         byte0 = 2;
      }

      if (material == Material.glass) {
         byte0 = 3;
      }

      if (material == Material.wood) {
         byte0 = 4;
      }

      if (byte0 == 1) {
         s = "bd";
      }

      if (byte0 == 2) {
         s = "snare";
      }

      if (byte0 == 3) {
         s = "hat";
      }

      if (byte0 == 4) {
         s = "bassattack";
      }

      world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "note." + s, 3.0F, f);
      world.spawnParticle("note", i + 0.5, j + 1.2, k + 0.5, note / 24.0, 0.0, 0.0);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void addInformation(ItemStack itemStack, EntityPlayer player, List infoList, boolean bAdvamcedToolTips) {
      infoList.add("Pitch: " + pitchNames[itemStack.getItemDamage()]);
   }
}
