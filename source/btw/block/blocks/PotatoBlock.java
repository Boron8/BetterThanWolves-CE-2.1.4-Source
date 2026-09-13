package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PotatoBlock extends DailyGrowthCropsBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconArray;

   public PotatoBlock(int blockID) {
      super(blockID);
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chanceOfDrop, int fortuneModifier) {
      if (!world.isRemote && this.isFullyGrown(metadata)) {
         super.a(world, x, y, z, metadata, chanceOfDrop, 0);
         if (world.rand.nextInt(3) == 0) {
            super.a(world, x, y, z, metadata, chanceOfDrop, 0);
         }
      }
   }

   @Override
   protected int getCropItemID() {
      return Item.potato.itemID;
   }

   @Override
   protected int getSeedItemID() {
      return 0;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      return this.getGrowthLevel(metadata) >= 7 ? super.getStackRetrievedByBlockDispenser(world, x, y, z) : null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      par2 = this.getGrowthLevel(par2);
      if (par2 < 7) {
         if (par2 == 6) {
            par2 = 5;
         }

         return this.iconArray[par2 >> 1];
      } else {
         return this.iconArray[3];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.iconArray = new Icon[4];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = par1IconRegister.registerIcon("fcBlockPotatoes_" + var2);
      }
   }
}
