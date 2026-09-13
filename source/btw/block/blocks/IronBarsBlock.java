package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

public class IronBarsBlock extends PaneBlock {
   @Environment(EnvType.CLIENT)
   private Icon filterIcon;

   public IronBarsBlock(int iBlockID) {
      super(iBlockID, "fenceIron", "fenceIron", Material.iron, true);
      this.c(5.0F);
      this.b(10.0F);
      this.a(Block.soundMetalFootstep);
      this.c("fenceIron");
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      int iFilterableProperties = filteredItem.getItem().getFilterableProperties(filteredItem);
      return filteredItem.getMaxStackSize() > 1 && (iFilterableProperties & 1) == 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.filterIcon = register.registerIcon("fcBlockHopper_ironbars");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return this.filterIcon;
   }
}
