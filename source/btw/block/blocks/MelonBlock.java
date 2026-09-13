package btw.block.blocks;

import btw.item.BTWItems;
import btw.util.CustomDamageSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.DamageSource;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;

public class MelonBlock extends GourdBlock {
   public MelonBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   protected Item itemToDropOnExplode() {
      return BTWItems.mashedMelon;
   }

   @Override
   protected int itemCountToDropOnExplode() {
      return 2;
   }

   @Override
   protected int auxFXIDOnExplode() {
      return 2249;
   }

   @Override
   protected DamageSource getFallDamageSource() {
      return CustomDamageSource.damageSourceMelon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("melon_side");
      this.iconTop = register.registerIcon("melon_top");
   }
}
