package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class HibachiBlock extends Block {
   private static final int TICK_RATE = 4;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];

   public HibachiBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(3.5F);
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockHibachi");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 4;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bPowered = this.isGettingPowered(world, i, j, k);
      if (bPowered) {
         if (!this.isLit(world, i, j, k)) {
            this.ignite(world, i, j, k);
         } else {
            int iBlockAboveID = world.getBlockId(i, j + 1, k);
            if (iBlockAboveID != Block.fire.blockID && iBlockAboveID != BTWBlocks.stokedFire.blockID && this.shouldIgniteAbove(world, i, j, k)) {
               world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
               world.setBlockWithNotify(i, j + 1, k, av.blockID);
            }
         }
      } else if (this.isLit(world, i, j, k)) {
         this.extinguish(world, i, j, k);
      } else {
         int iBlockAboveID = world.getBlockId(i, j + 1, k);
         if (iBlockAboveID == Block.fire.blockID || iBlockAboveID == BTWBlocks.stokedFire.blockID) {
            world.setBlockWithNotify(i, j + 1, k, 0);
         }
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public boolean doesExtinguishFireAbove(World world, int i, int j, int k) {
      return !this.isLit(world, i, j, k);
   }

   @Override
   public boolean doesInfiniteBurnToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return iFacing == 1 ? this.isLit(blockAccess, i, j, k) : false;
   }

   public boolean isLit(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetaData = blockAccess.getBlockMetadata(i, j, k);
      return (iMetaData & 4) > 0;
   }

   private void setLitFlag(World world, int i, int j, int k) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      world.setBlockMetadataWithNotify(i, j, k, iMetaData | 4);
   }

   private void clearLitFlag(World world, int i, int j, int k) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      world.setBlockMetadataWithNotify(i, j, k, iMetaData & -5);
   }

   private boolean isGettingPowered(World world, int i, int j, int k) {
      return world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k);
   }

   private boolean shouldIgniteAbove(World world, int i, int j, int k) {
      return world.isAirBlock(i, j + 1, k) || this.canIncinerateBlock(world, i, j + 1, k);
   }

   private boolean canIncinerateBlock(World world, int i, int j, int k) {
      Block targetBlock = Block.blocksList[world.getBlockId(i, j, k)];
      return targetBlock == null || targetBlock.getCanBlockBeIncinerated(world, i, j, k);
   }

   private void ignite(World world, int i, int j, int k) {
      this.setLitFlag(world, i, j, k);
      world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 1.0F);
      if (this.shouldIgniteAbove(world, i, j, k)) {
         world.setBlockWithNotify(i, j + 1, k, av.blockID);
      }
   }

   private void extinguish(World world, int i, int j, int k) {
      this.clearLitFlag(world, i, j, k);
      world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      boolean isFireAbove = world.getBlockId(i, j + 1, k) == av.blockID || world.getBlockId(i, j + 1, k) == BTWBlocks.stokedFire.blockID;
      if (isFireAbove) {
         world.setBlockWithNotify(i, j + 1, k, 0);
      }
   }

   public boolean isCurrentStateValid(World world, int i, int j, int k) {
      boolean bPowered = this.isGettingPowered(world, i, j, k);
      if (this.isLit(world, i, j, k) != bPowered) {
         return false;
      } else {
         if (this.isLit(world, i, j, k)) {
            int iBlockAboveID = world.getBlockId(i, j + 1, k);
            if (iBlockAboveID != Block.fire.blockID && iBlockAboveID != BTWBlocks.stokedFire.blockID && this.shouldIgniteAbove(world, i, j, k)) {
               return false;
            }
         }

         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
      this.iconBySideArray[0] = register.registerIcon("fcBlockHibachi_bottom");
      this.iconBySideArray[1] = register.registerIcon("fcBlockHibachi_top");
      Icon sideIcon = register.registerIcon("fcBlockHibachi_side");
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
