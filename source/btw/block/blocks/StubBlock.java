package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.Block;
import net.minecraft.src.IBehaviorDispenseItem;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;

public class StubBlock extends Block {
   private final IBehaviorDispenseItem dropperDefaultBehaviour = new BehaviorDefaultDispenseItem();

   public StubBlock(int par1) {
      super(par1, Material.rock);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockStub");
   }
}
