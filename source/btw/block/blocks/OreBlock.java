package btw.block.blocks;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockOre;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class OreBlock extends BlockOre {
   @Environment(EnvType.CLIENT)
   private Icon[] iconByMetadataArray = new Icon[16];

   public OreBlock(int iBlockID) {
      super(iBlockID);
      this.setPicksEffectiveOn();
   }

   @Override
   public boolean hasStrata() {
      return true;
   }

   @Override
   public int getMetadataConversionForStrataLevel(int iLevel, int iMetadata) {
      return iLevel;
   }

   @Override
   public float getBlockHardness(World world, int i, int j, int k) {
      int iStrata = this.getStrata(world, i, j, k);
      if (iStrata != 0) {
         return iStrata == 1 ? 4.0F : 6.0F;
      } else {
         return super.l(world, i, j, k);
      }
   }

   @Override
   public float getExplosionResistance(Entity entity, World world, int i, int j, int k) {
      int iStrata = this.getStrata(world, i, j, k);
      if (iStrata != 0) {
         return iStrata == 1 ? 4.2000003F : 6.0F;
      } else {
         return super.getExplosionResistance(entity, world, i, j, k);
      }
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return iMetadata & 3;
   }

   public int getRequiredToolLevelForStrata(IBlockAccess blockAccess, int i, int j, int k) {
      int iStrata = this.getStrata(blockAccess, i, j, k);
      return iStrata > 1 ? iStrata + 1 : 2;
   }

   @Override
   public boolean isNaturalStone(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconByMetadataArray[0] = this.blockIcon;
      this.iconByMetadataArray[1] = register.registerIcon("fcBlock" + this.B() + "Strata_1");
      this.iconByMetadataArray[2] = register.registerIcon("fcBlock" + this.B() + "Strata_2");

      for (int iTempIndex = 3; iTempIndex < 16; iTempIndex++) {
         this.iconByMetadataArray[iTempIndex] = this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByMetadataArray[iMetadata];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderStandardFullBlock(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      this.renderCookingByKiLnOverlay(renderBlocks, i, j, k, bFirstPassResult);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesItemRenderAsBlock(int iItemDamage) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.renderStandardFullBlockMovedByPiston(this, i, j, k);
   }
}
