package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;

public class OreChunkItemIron extends PlaceAsBlockItem {
   public OreChunkItemIron(int iItemID) {
      super(iItemID, BTWBlocks.ironOreChunk.blockID);
      this.setFilterableProperties(2);
      this.b("fcItemChunkIronOre");
      this.a(CreativeTabs.tabMaterials);
   }
}
