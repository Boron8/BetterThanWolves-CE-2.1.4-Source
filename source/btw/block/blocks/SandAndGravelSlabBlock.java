package btw.block.blocks;

import btw.item.BTWItems;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;

public class SandAndGravelSlabBlock extends FallingSlabBlock {
   public static final int SUBTYPE_GRAVEL = 0;
   public static final int SUBTYPE_SAND = 1;
   @Environment(EnvType.CLIENT)
   private Icon iconSand;

   public SandAndGravelSlabBlock(int iBlockID) {
      super(iBlockID, Material.sand);
      this.c(0.5F);
      this.setShovelsEffectiveOn(true);
      this.a(h);
      this.c("fcBlockSlabFalling");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int damageDropped(int iMetadata) {
      return this.getSubtypeFromMetadata(iMetadata);
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      float fModifier = 1.0F;
      int iSubtype = this.getSubtype(world, i, j, k);
      if (iSubtype == 0) {
         fModifier = 1.2F;
      } else if (iSubtype == 1) {
         fModifier = 0.8F;
      }

      return fModifier;
   }

   @Override
   public StepSound getStepSound(World world, int i, int j, int k) {
      int iSubtype = this.getSubtype(world, i, j, k);
      return iSubtype == 1 ? n : this.stepSound;
   }

   @Override
   public boolean attemptToCombineWithFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (entity.blockID == this.blockID) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         if (iMetadata == entity.metadata) {
            world.setBlockWithNotify(i, j, k, this.getCombinedBlockID(iMetadata));
            return true;
         }
      }

      return false;
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return iMetadata == 1 ? Block.sand.blockID : Block.gravel.blockID;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      int iIDToDrop = BTWItems.gravelPile.itemID;
      if (this.getSubtypeFromMetadata(iMetadata) == 1) {
         iIDToDrop = BTWItems.sandPile.itemID;
      }

      this.dropItemsIndividually(world, i, j, k, iIDToDrop, 3, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean getIsUpsideDown(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean getIsUpsideDown(int iMetadata) {
      return false;
   }

   @Override
   public void setIsUpsideDown(World world, int i, int j, int k, boolean bUpsideDown) {
   }

   @Override
   public int setIsUpsideDown(int iMetadata, boolean bUpsideDown) {
      return iMetadata;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public int getFilterableProperties(ItemStack stack) {
      return stack.getItemDamage() == 0 ? 2 : 8;
   }

   public int getSubtype(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getSubtypeFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getSubtypeFromMetadata(int iMetadata) {
      return iMetadata;
   }

   public void setSubtype(World world, int i, int j, int k, int iSubtype) {
      int iMetadata = world.getBlockMetadata(i, j, k) & 0;
      iMetadata |= iSubtype;
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("gravel");
      this.iconSand = register.registerIcon("sand");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iSubtype = this.getSubtypeFromMetadata(iMetadata);
      return iSubtype == 1 ? this.iconSand : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 1));
   }
}
