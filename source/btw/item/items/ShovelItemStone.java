package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ShovelItemStone extends ShovelItem {
   public ShovelItemStone(int iItemID) {
      super(iItemID, EnumToolMaterial.STONE);
      this.efficiencyOnProperMaterial /= 3.0F;
      this.b("shovelStone");
   }

   @Override
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block == Block.blockClay;
   }
}
