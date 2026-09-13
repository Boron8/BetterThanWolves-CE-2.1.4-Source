package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;

public class SandstoneMouldingAndDecorativeBlock extends MouldingAndDecorativeWallBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];

   public SandstoneMouldingAndDecorativeBlock(int iBlockID, int iMatchingCornerBlockID) {
      super(
         iBlockID,
         Material.rock,
         "fcBlockDecorativeSandstone_top",
         "fcBlockColumnSandstone_side",
         iMatchingCornerBlockID,
         0.8F,
         1.34F,
         Block.soundStoneFootstep,
         "fcSandstoneMoulding"
      );
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
      return iMetadata != 12 ? this.iconBySideArray[iSide] : super.a(iSide, iMetadata);
   }
}
