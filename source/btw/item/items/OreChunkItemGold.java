package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;

public class OreChunkItemGold extends PlaceAsBlockItem {
   public OreChunkItemGold(int iItemID) {
      super(iItemID, BTWBlocks.goldOreChunk.blockID);
      this.setFilterableProperties(2);
      this.b("fcItemChunkGoldOre");
      this.a(CreativeTabs.tabMaterials);
   }
}
