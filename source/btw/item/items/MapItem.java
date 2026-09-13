package btw.item.items;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemMap;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapData;
import net.minecraft.src.World;

public class MapItem extends ItemMap {
   public MapItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setBellowsBlowDistance(3);
      this.setFilterableProperties(16);
      this.b("map");
   }

   @Override
   public void updateMapData(World world, Entity entity, MapData mapData) {
      if (world.provider.dimensionId == mapData.dimension && entity instanceof EntityPlayer && mapData.isEntityLocationVisibleOnMap(entity)) {
         super.updateMapData(world, entity, mapData);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean bAdvancedTips) {
      MapData var5 = this.a(stack, player.worldObj);
      if (var5 != null) {
         infoList.add("Scale: x" + (1 << var5.scale));
      }
   }
}
