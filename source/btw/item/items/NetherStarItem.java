package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemSimpleFoiled;
import net.minecraft.src.ItemStack;

public class NetherStarItem extends ItemSimpleFoiled {
   public NetherStarItem(int iItemID) {
      super(iItemID);
      this.setFilterableProperties(2);
      this.b("netherStar");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      if (player.timesCraftedThisTick == 0) {
         player.playSound("ambient.cave.cave4", 0.5F, player.worldObj.rand.nextFloat() * 0.05F + 0.5F);
      }
   }
}
