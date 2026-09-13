package btw.item.items;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemEmptyMap;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapData;
import net.minecraft.src.World;

public class EmptyMapItem extends ItemEmptyMap {
   public EmptyMapItem(int iItemID) {
      super(iItemID);
      this.a(true);
      this.setBuoyant();
      this.setFilterableProperties(16);
      this.b("emptyMap");
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      ItemStack newStack = new ItemStack(Item.map, 1, world.getUniqueDataId("map"));
      String sMapName = "map_" + newStack.getItemDamage();
      MapData newMapData = new MapData(sMapName);
      world.setItemData(sMapName, newMapData);
      newMapData.scale = (byte)stack.getItemDamage();
      int var7 = 128 * (1 << newMapData.scale);
      newMapData.xCenter = (int)(Math.round(player.posX / var7) * var7);
      newMapData.zCenter = (int)(Math.round(player.posZ / var7) * var7);
      newMapData.dimension = (byte)world.provider.dimensionId;
      newMapData.c();
      stack.stackSize--;
      if (stack.stackSize <= 0) {
         return newStack;
      } else {
         if (!player.inventory.addItemStackToInventory(newStack.copy())) {
            player.dropPlayerItem(newStack);
         }

         return stack;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean bAdvancedTips) {
      infoList.add("Scale: x" + (1 << stack.getItemDamage()));
   }
}
