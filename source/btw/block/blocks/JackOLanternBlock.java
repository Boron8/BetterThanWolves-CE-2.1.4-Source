package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class JackOLanternBlock extends Block {
   @Environment(EnvType.CLIENT)
   private Icon iconTop;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;

   public JackOLanternBlock(int iBlockID) {
      super(iBlockID, Material.pumpkin);
      this.b(true);
      this.c(1.0F);
      this.setBuoyant();
      this.a(g);
      this.a(1.0F);
      this.c("litpumpkin");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving placingEntity, ItemStack itemStack) {
      int iFacing = MathHelper.floor_double(placingEntity.rotationYaw * 4.0 / 360.0 + 2.5) & 3;
      int iMetadata = world.getBlockMetadata(i, j, k) & -4;
      iMetadata |= iFacing;
      world.SetBlockMetadataWithNotify(i, j, k, iMetadata, 2);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      int iMetadata = world.getBlockMetadata(i, j, k) | 8;
      world.SetBlockMetadataWithNotify(i, j, k, iMetadata, 2);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.a(world));
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      this.checkForExtinguish(world, i, j, k);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      this.checkForExtinguish(world, i, j, k);
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iDirection = iMetadata & 3;
      if (bReverse) {
         if (++iDirection > 3) {
            iDirection = 0;
         }
      } else if (--iDirection < 0) {
         iDirection = 3;
      }

      return iMetadata & -4 | iDirection;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return animal.canGrazeOnRoughVegetation();
   }

   private void checkForExtinguish(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if ((iMetadata & 8) != 0 && this.hasWaterToSidesOrTop(world, i, j, k)) {
         this.extinguishLantern(world, i, j, k);
      }
   }

   private void extinguishLantern(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      world.setBlockAndMetadataWithNotify(i, j, k, Block.pumpkin.blockID, iMetadata & 3);
      world.playAuxSFX(2227, i, j, k, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.iconFront = par1IconRegister.registerIcon("pumpkin_jack");
      this.iconTop = par1IconRegister.registerIcon("pumpkin_top");
      this.blockIcon = par1IconRegister.registerIcon("pumpkin_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide != 1 && iSide != 0) {
         int iFacing = iMetadata & 3;
         return (iFacing != 2 || iSide != 2) && (iFacing != 3 || iSide != 5) && (iFacing != 0 || iSide != 3) && (iFacing != 1 || iSide != 4)
            ? this.blockIcon
            : this.iconFront;
      } else {
         return this.iconTop;
      }
   }
}
