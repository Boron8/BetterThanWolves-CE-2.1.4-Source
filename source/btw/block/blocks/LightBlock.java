package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LightBlock extends Block {
   private static final int LIGHT_BULB_TICK_RATE = 2;
   private boolean glowing;

   public LightBlock(int iBlockID, boolean bGlowing) {
      super(iBlockID, Material.glass);
      this.c(0.4F);
      this.setPicksEffectiveOn();
      this.a(Block.soundGlassFootstep);
      this.c("fcBlockLightBlock");
      this.glowing = bGlowing;
      if (bGlowing) {
         this.a(1.0F);
      } else {
         this.a(CreativeTabs.tabRedstone);
      }

      this.b(true);
   }

   @Override
   public int tickRate(World world) {
      return 2;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return BTWBlocks.lightBlockOff.blockID;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bPowered = world.isBlockIndirectlyGettingPowered(i, j, k);
      if (bPowered) {
         if (!this.isLightOn(world, i, j, k)) {
            this.lightBulbTurnOn(world, i, j, k);
            return;
         }
      } else if (this.isLightOn(world, i, j, k)) {
         this.lightBulbTurnOff(world, i, j, k);
         return;
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   public boolean isCurrentStateValid(World world, int i, int j, int k) {
      boolean bPowered = world.isBlockIndirectlyGettingPowered(i, j, k);
      return bPowered == this.isLightOn(world, i, j, k);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return bIgnoreTransparency;
   }

   private void lightBulbTurnOn(World world, int i, int j, int k) {
      world.setBlockWithNotify(i, j, k, BTWBlocks.lightBlockOn.blockID);
   }

   private void lightBulbTurnOff(World world, int i, int j, int k) {
      world.setBlockWithNotify(i, j, k, BTWBlocks.lightBlockOff.blockID);
   }

   public boolean isLightOn(World world, int i, int j, int k) {
      return world.getBlockId(i, j, k) == BTWBlocks.lightBlockOn.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      if (this.glowing) {
         this.blockIcon = register.registerIcon("fcBlockLightBlock_lit");
      } else {
         this.blockIcon = register.registerIcon("fcBlockLightBlock");
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getBlockBrightness(IBlockAccess iblockaccess, int i, int j, int k) {
      return this.blockID == BTWBlocks.lightBlockOn.blockID ? 100.0F : iblockaccess.getLightBrightness(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }
}
