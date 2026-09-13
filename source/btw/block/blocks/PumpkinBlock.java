package btw.block.blocks;

import btw.util.CustomDamageSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.DamageSource;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;

public class PumpkinBlock extends GourdBlock {
   public PumpkinBlock(int iBlockID, boolean bStub) {
      super(iBlockID);
      this.c(1.0F);
      this.a(g);
      this.c("fcBlockPumpkinFresh");
   }

   @Override
   protected Item itemToDropOnExplode() {
      return Item.pumpkinSeeds;
   }

   @Override
   protected int itemCountToDropOnExplode() {
      return 4;
   }

   @Override
   protected int auxFXIDOnExplode() {
      return 2250;
   }

   @Override
   protected DamageSource getFallDamageSource() {
      return CustomDamageSource.damageSourcePumpkin;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("pumpkin_side");
      this.iconTop = register.registerIcon("pumpkin_top");
   }
}
