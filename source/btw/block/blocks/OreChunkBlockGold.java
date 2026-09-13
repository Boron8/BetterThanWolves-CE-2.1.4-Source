package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class OreChunkBlockGold extends OreChunkBlock {
   public OreChunkBlockGold(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockChunkOreGold");
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return BTWItems.goldOreChunk.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int x, int y, int z) {
      return BTWItems.goldOreChunk.itemID;
   }
}
