package btw.block.blocks;

import btw.block.util.Flammability;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockMushroomCap;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class MushroomCapBlock extends BlockMushroomCap {
   protected final int mushroomType;

   public MushroomCapBlock(int iBlockID, int iMushroomType) {
      super(iBlockID, Material.wood, iMushroomType);
      this.mushroomType = iMushroomType;
      this.c(0.2F);
      this.a(g);
      this.setBuoyant();
      this.setFireProperties(Flammability.HIGH);
      this.c("fcBlockMushroomCap");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return this.mushroomType != 0 ? BTWItems.redMushroom.itemID : BTWItems.brownMushroom.itemID;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }
}
