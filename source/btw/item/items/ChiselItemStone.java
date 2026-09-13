package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ChiselItemStone extends ChiselItem {
   public ChiselItemStone(int iItemID) {
      super(iItemID, EnumToolMaterial.STONE, 8);
      this.setFilterableProperties(2);
      this.efficiencyOnProperMaterial /= 2.0F;
      this.b("fcItemChiselStone");
   }

   @Override
   public float getStrVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      float fStrength = super.getStrVsBlock(stack, world, block, i, j, k);
      if (block.blockID == Block.web.blockID || block.blockID == BTWBlocks.web.blockID) {
         fStrength *= 2.0F;
      }

      return fStrength;
   }
}
