package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;

public class SandstoneSidingAndCornerAndDecorativeBlock extends SidingAndCornerAndDecorativeWallBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];

   public SandstoneSidingAndCornerAndDecorativeBlock(int iBlockID) {
      super(iBlockID, Material.rock, "fcBlockDecorativeSandstone_top", 0.8F, 1.34F, Block.soundStoneFootstep, "fcSandstoneSiding");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconBySideArray[0] = register.registerIcon("fcBlockDecorativeSandstone_bottom");
      this.iconBySideArray[1] = register.registerIcon("fcBlockDecorativeSandstone_top");
      Icon sideIcon = register.registerIcon("fcBlockDecorativeSandstone_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconBySideArray[iSide];
   }
}
