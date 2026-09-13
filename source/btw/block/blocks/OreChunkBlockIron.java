package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class OreChunkBlockIron extends OreChunkBlock {
   public OreChunkBlockIron(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockChunkOreIron");
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return BTWItems.ironOreChunk.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int x, int y, int z) {
      return BTWItems.ironOreChunk.itemID;
   }
}
