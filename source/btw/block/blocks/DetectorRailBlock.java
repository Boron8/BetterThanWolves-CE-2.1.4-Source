package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDetectorRail;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class DetectorRailBlock extends BlockDetectorRail {
   @Environment(EnvType.CLIENT)
   private Icon iconOn;

   public DetectorRailBlock(int iBlockID) {
      super(iBlockID);
      this.setPicksEffectiveOn(true);
      this.a(CreativeTabs.tabTransport);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (!world.isRemote && this.isOn(world, i, j, k)) {
         this.setStateIfMinecartInteractsWithRailLocal(world, i, j, k, world.getBlockMetadata(i, j, k));
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && !this.isOn(world, i, j, k)) {
         this.setStateIfMinecartInteractsWithRailLocal(world, i, j, k, world.getBlockMetadata(i, j, k));
      }
   }

   private boolean isOn(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isOnFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   private boolean isOnFromMetadata(int iMetadata) {
      return (iMetadata & 8) > 0;
   }

   private void setIsOn(World world, int i, int j, int k, boolean bOn) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      iMetadata = this.setIsOnInMetadata(iMetadata, bOn);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
      world.notifyBlocksOfNeighborChange(i, j - 1, k, this.blockID);
   }

   private int setIsOnInMetadata(int iMetadata, boolean bOn) {
      if (bOn) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }

   private void setStateIfMinecartInteractsWithRailLocal(World world, int i, int j, int k, int iMetadata) {
      boolean bIsOn = this.isOnFromMetadata(iMetadata);
      boolean bTriggeredByCart = false;
      float fBoxBorder = 0.125F;
      List collidingMinecarts = world.getEntitiesWithinAABB(
         EntityMinecart.class,
         AxisAlignedBB.getAABBPool().getAABB(i + fBoxBorder, j, k + fBoxBorder, i + 1 - fBoxBorder, j + 1 - fBoxBorder, k + 1 - fBoxBorder)
      );
      if (collidingMinecarts != null && !collidingMinecarts.isEmpty()) {
         for (int listIndex = 0; listIndex < collidingMinecarts.size(); listIndex++) {
            EntityMinecart minecartEntity = (EntityMinecart)collidingMinecarts.get(listIndex);
            if (this.shouldPlateActivateBasedOnMinecart(world, i, j, k, minecartEntity.getMinecartType(), minecartEntity.riddenByEntity)) {
               bTriggeredByCart = true;
               break;
            }
         }
      }

      if (bTriggeredByCart != bIsOn) {
         this.setIsOn(world, i, j, k, bTriggeredByCart);
      }

      if (bTriggeredByCart) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.a(world));
      }
   }

   public boolean shouldPlateActivateBasedOnMinecart(World world, int i, int j, int k, int iMinecartType, Entity riddenByEntity) {
      int iLocalBlockID = world.getBlockId(i, j, k);
      if (iLocalBlockID == BTWBlocks.woodenDetectorRail.blockID) {
         return true;
      } else {
         if (iLocalBlockID == BTWBlocks.steelDetectorRail.blockID) {
            if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer) {
               return true;
            }
         } else if (iLocalBlockID == Block.railDetector.blockID && (iMinecartType > 0 || riddenByEntity != null)) {
            return true;
         }

         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      if (this.blockID == Block.railDetector.blockID) {
         this.blockIcon = register.registerIcon("detectorRail");
         this.iconOn = register.registerIcon("detectorRail_on");
      } else {
         this.blockIcon = register.registerIcon(this.B());
         this.iconOn = register.registerIcon(this.B() + "_on");
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.isOnFromMetadata(iMetadata) ? this.iconOn : this.blockIcon;
   }
}
