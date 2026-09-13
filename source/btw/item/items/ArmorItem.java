package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ArmorItem extends ItemArmor {
   private final int armorWeight;

   public ArmorItem(int iItemID, EnumArmorMaterial armorMaterial, int iRenderIndex, int iArmorType, int iWeight) {
      super(iItemID, armorMaterial, iRenderIndex, iArmorType);
      this.armorWeight = iWeight;
   }

   @Override
   public int getWeightWhenWorn() {
      return this.armorWeight;
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      super.d(stack, world, player);
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         if (this.d() == EnumArmorMaterial.CLOTH) {
            player.playSound("step.cloth", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
         } else {
            player.playSound("random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F);
         }
      }
   }
}
