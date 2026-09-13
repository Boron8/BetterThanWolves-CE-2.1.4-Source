package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class CandleBlock extends Block {
   private int metaDropped;

   public CandleBlock(int id, int metaDropped, String name) {
      super(id, BTWBlocks.candleMaterial);
      this.c(0.0F);
      this.setPicksEffectiveOn(true);
      this.setAxesEffectiveOn(true);
      this.a(1.0F);
      this.a(j);
      this.c(name);
      this.metaDropped = metaDropped;
   }

   @Override
   public int idDropped(int par1, Random rand, int par3) {
      return BTWItems.candle.itemID;
   }

   @Override
   public int damageDropped(int meta) {
      return this.metaDropped;
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
   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      int blockBelowID = world.getBlockId(x, y - 1, z);
      int blockBelowMetadata = world.getBlockMetadata(x, y - 1, z);
      return blockBelowID == BTWBlocks.aestheticNonOpaque.blockID && blockBelowMetadata == 12
         ? true
         : WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(world, x, y - 1, z, 1, true);
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if (player.getCurrentEquippedItem() == null) {
         int metadata = world.getBlockMetadata(x, y, z);
         ItemStack stack = new ItemStack(this.idDropped(metadata, world.rand, 0), 1, this.damageDropped(metadata));
         ItemUtils.givePlayerStackOrEject(player, stack, x, y, z);
         this.setCandleCount(world, x, y, z, this.getCandleCount(metadata) - 1);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
      if (!this.canPlaceBlockAt(world, x, y, z)) {
         this.c(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
         world.setBlockWithNotify(x, y, z, 0);
      }
   }

   @Override
   public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int x, int y, int z) {
      return true;
   }

   @Override
   public void onNeighborDisrupted(World world, int x, int y, int z, int toFacing) {
      if (toFacing == 0) {
         this.c(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
         world.setBlockWithNotify(x, y, z, 0);
      }
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int x, int y, int z, Block fluidBlock) {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
      int candleCount = this.getCandleCount(blockAccess, x, y, z);
      switch (candleCount) {
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.4375, 0.0, 0.4375, 0.5625, 0.4375, 0.5625);
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.375, 0.6875, 0.4375, 0.5625);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.375, 0.6875, 0.4375, 0.6875);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.3125, 0.6875, 0.4375, 0.625);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      }
   }

   @Override
   public int getLightValue(IBlockAccess blockAccess, int x, int y, int z) {
      if (this.isLit(blockAccess, x, y, z)) {
         int candleCount = this.getCandleCount(blockAccess, x, y, z);
         return (int)(v[this.blockID] * (candleCount / 4.0F));
      } else {
         return 0;
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortuneModifier) {
      if (!world.isRemote) {
         this.dropItemsIndividually(
            world, x, y, z, this.idDropped(metadata, world.rand, 0), this.getCandleCount(world, x, y, z), this.damageDropped(metadata), 1.0F
         );
      }
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int x, int y, int z, int fromSide) {
      int candleCount = this.getCandleCount(world, x, y, z);
      int metadata = world.getBlockMetadata(x, y, z);
      this.setCandleCount(world, x, y, z, candleCount - 1);
      this.dropItemsIndividually(world, x, y, z, this.idDropped(metadata, world.rand, 0), 1, this.damageDropped(metadata), 1.0F);
      return true;
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int x, int y, int z) {
      return !this.isLit(blockAccess, x, y, z);
   }

   @Override
   public boolean setOnFireDirectly(World world, int x, int y, int z) {
      if (!this.isLit(world, x, y, z)) {
         this.setLit(world, x, y, z, true);
         world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "mob.ghast.fireball", 0.1F + world.rand.nextFloat() * 0.1F, world.rand.nextFloat() * 0.25F + 1.25F);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int x, int y, int z) {
      return this.isLit(blockAccess, x, y, z);
   }

   public int getCandleCount(IBlockAccess blockAccess, int x, int y, int z) {
      int meta = blockAccess.getBlockMetadata(x, y, z);
      return this.getCandleCount(meta);
   }

   public int getCandleCount(int metadata) {
      return (metadata & 3) + 1;
   }

   public void setCandleCount(World world, int x, int y, int z, int count) {
      if (count == 0) {
         world.setBlockWithNotify(x, y, z, 0);
      } else {
         if (count > 4) {
            Exception e = new IllegalArgumentException("Cannot set candle count higher than 4");
            e.printStackTrace();
            count = 4;
         }

         int meta = world.getBlockMetadata(x, y, z);
         int newMeta = meta & 4;
         newMeta += count - 1;
         world.setBlockMetadataWithNotify(x, y, z, newMeta);
      }
   }

   public boolean isLit(IBlockAccess blockAccess, int x, int y, int z) {
      return blockAccess.getBlockMetadata(x, y, z) >> 2 == 1;
   }

   public void setLit(World world, int x, int y, int z, boolean isLit) {
      int meta = world.getBlockMetadata(x, y, z);
      int newMeta;
      if (isLit) {
         newMeta = meta | 4;
      } else {
         newMeta = meta & 3;
      }

      world.setBlockMetadataWithNotify(x, y, z, newMeta);
   }

   public int setLit(int meta, boolean isLit) {
      int newMeta;
      if (isLit) {
         newMeta = meta | 4;
      } else {
         newMeta = meta & 3;
      }

      return newMeta;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return BTWItems.candle.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      if (this.isLit(world, x, y, z)) {
         int meta = world.getBlockMetadata(x, y, z);
         int candleCount = this.getCandleCount(world, x, y, z);
         double xPos = x + 0.0625F;
         double yPos = y + 0.5F;
         double zPos = z + 0.0625F;
         switch (candleCount) {
            case 1:
               xPos = x + 0.5F;
               yPos = y + 0.5F;
               zPos = z + 0.5F;
               this.spawnParticles(world, xPos, yPos, zPos);
               break;
            case 2:
               xPos = x + 0.625F;
               yPos = y + 0.5F;
               zPos = z + 0.5F;
               this.spawnParticles(world, xPos, yPos, zPos);
               xPos = x + 0.375F;
               yPos = y + 0.4375F;
               zPos = z + 0.4375F;
               this.spawnParticles(world, xPos, yPos, zPos);
               break;
            case 3:
               xPos = x + 0.625F;
               yPos = y + 0.5F;
               zPos = z + 0.5F;
               this.spawnParticles(world, xPos, yPos, zPos);
               xPos = x + 0.4375F;
               yPos = y + 0.4375F;
               zPos = z + 0.4375F;
               this.spawnParticles(world, xPos, yPos, zPos);
               xPos = x + 0.5F;
               yPos = y + 0.3125F;
               zPos = z + 0.625F;
               this.spawnParticles(world, xPos, yPos, zPos);
               break;
            case 4:
               xPos = x + 0.625F;
               yPos = y + 0.5F;
               zPos = z + 0.375F;
               this.spawnParticles(world, xPos, yPos, zPos);
               xPos = x + 0.4375F;
               yPos = y + 0.4375F;
               zPos = z + 0.375F;
               this.spawnParticles(world, xPos, yPos, zPos);
               xPos = x + 0.5625F;
               yPos = y + 0.3125F;
               zPos = z + 0.5625F;
               this.spawnParticles(world, xPos, yPos, zPos);
               xPos = x + 0.375F;
               yPos = y + 0.375F;
               zPos = z + 0.5625F;
               this.spawnParticles(world, xPos, yPos, zPos);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   private void spawnParticles(World world, double xPos, double yPos, double zPos) {
      world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      world.spawnParticle("fcsmallflame", xPos, yPos, zPos, 0.0, 0.0, 0.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
      return this.renderBlockCandle(render, this, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   private boolean renderBlockCandle(RenderBlocks render, Block block, int x, int y, int z) {
      Tessellator tess = Tessellator.instance;
      tess.setBrightness(block.getMixedBrightnessForBlock(render.blockAccess, x, y, z));
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      int meta = render.blockAccess.getBlockMetadata(x, y, z);
      Icon icon = this.b_(render.blockAccess, x, y, z, meta);
      int candleCount = this.getCandleCount(render.blockAccess, x, y, z);
      double minU = icon.getInterpolatedU(0.0);
      double maxU = icon.getInterpolatedU(2.0);
      double minV = icon.getInterpolatedV(8.0);
      double maxV = icon.getInterpolatedV(14.0);
      double minXOffset = 0.0;
      double maxXOffset = 0.125;
      double minYOffset = 0.0;
      double maxYOffset = 0.375;
      double minZOffset = 0.0;
      double maxZOffset = 0.125;
      double minX = x + minXOffset;
      double maxX = x + maxXOffset;
      double minY = y + minYOffset;
      double maxY = y + maxYOffset;
      double minZ = z + minZOffset;
      double maxZ = z + maxZOffset;
      switch (candleCount) {
         case 1:
            minX = x + 0.4375;
            maxX = x + 0.5625;
            minY = y + 0.0;
            maxY = y + 0.375;
            minZ = z + 0.4375;
            maxZ = z + 0.5625;
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            break;
         case 2:
            minX = x + 0.5625;
            maxX = x + 0.6875;
            minY = y + 0.0;
            maxY = y + 0.375;
            minZ = z + 0.4375;
            maxZ = z + 0.5625;
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            minX = x + 0.3125;
            maxX = x + 0.4375;
            minY = y + 0.0;
            maxY = y + 0.3125;
            minZ = z + 0.375;
            maxZ = z + 0.5;
            maxV = icon.getInterpolatedV(13.0);
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            break;
         case 3:
            minX = x + 0.5625;
            maxX = x + 0.6875;
            minY = y + 0.0;
            maxY = y + 0.375;
            minZ = z + 0.4375;
            maxZ = z + 0.5625;
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            minX = x + 0.375;
            maxX = x + 0.5;
            minY = y + 0.0;
            maxY = y + 0.3125;
            minZ = z + 0.375;
            maxZ = z + 0.5;
            maxV = icon.getInterpolatedV(13.0);
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            minX = x + 0.4375;
            maxX = x + 0.5625;
            minY = y + 0.0;
            maxY = y + 0.1875;
            minZ = z + 0.5625;
            maxZ = z + 0.6875;
            maxV = icon.getInterpolatedV(11.0);
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            break;
         case 4:
            minX = x + 0.5625;
            maxX = x + 0.6875;
            minY = y + 0.0;
            maxY = y + 0.375;
            minZ = z + 0.3125;
            maxZ = z + 0.4375;
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            minX = x + 0.375;
            maxX = x + 0.5;
            minY = y + 0.0;
            maxY = y + 0.3125;
            minZ = z + 0.3125;
            maxZ = z + 0.4375;
            maxV = icon.getInterpolatedV(13.0);
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            minX = x + 0.5;
            maxX = x + 0.625;
            minY = y + 0.0;
            maxY = y + 0.1875;
            minZ = z + 0.5;
            maxZ = z + 0.625;
            maxV = icon.getInterpolatedV(11.0);
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            minX = x + 0.3125;
            maxX = x + 0.4375;
            minY = y + 0.0;
            maxY = y + 0.25;
            minZ = z + 0.5;
            maxZ = z + 0.625;
            maxV = icon.getInterpolatedV(12.0);
            this.drawCandle(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
            this.drawWick(tess, minX, maxX, minY, maxY, minZ, maxZ, minU, maxU, minV, maxV, icon);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   private boolean drawCandle(
      Tessellator tess,
      double minX,
      double maxX,
      double minY,
      double maxY,
      double minZ,
      double maxZ,
      double minU,
      double maxU,
      double minV,
      double maxV,
      Icon icon
   ) {
      tess.addVertexWithUV(minX, minY, minZ, minU, maxV);
      tess.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
      tess.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
      tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
      tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
      tess.addVertexWithUV(maxX, minY, minZ, minU, maxV);
      tess.addVertexWithUV(minX, minY, minZ, maxU, maxV);
      tess.addVertexWithUV(minX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
      tess.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
      tess.addVertexWithUV(minX, maxY, maxZ, minU, minV);
      minU = icon.getMinU();
      maxU = icon.getInterpolatedU(2.0);
      minV = icon.getInterpolatedV(6.0);
      maxV = icon.getInterpolatedV(8.0);
      tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
      tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(minX, minY, minZ, minU, minV);
      tess.addVertexWithUV(maxX, minY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
      tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
      return true;
   }

   @Environment(EnvType.CLIENT)
   private boolean drawWick(
      Tessellator tess,
      double minX,
      double maxX,
      double minY,
      double maxY,
      double minZ,
      double maxZ,
      double minU,
      double maxU,
      double minV,
      double maxV,
      Icon icon
   ) {
      minU = icon.getInterpolatedU(0.0);
      maxU = icon.getInterpolatedU(1.0);
      minV = icon.getInterpolatedV(5.0);
      maxV = icon.getInterpolatedV(6.0);
      minX += 0.046875;
      maxX = minX + 0.03125;
      double var26 = maxY + 0.0625;
      minZ += 0.046875;
      maxZ = minZ + 0.03125;
      tess.addVertexWithUV(minX, maxY, minZ, minU, maxV);
      tess.addVertexWithUV(minX, maxY, maxZ, maxU, maxV);
      tess.addVertexWithUV(minX, var26, maxZ, maxU, minV);
      tess.addVertexWithUV(minX, var26, minZ, minU, minV);
      tess.addVertexWithUV(maxX, maxY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, maxY, minZ, maxU, maxV);
      tess.addVertexWithUV(maxX, var26, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, var26, maxZ, minU, minV);
      tess.addVertexWithUV(maxX, maxY, minZ, minU, maxV);
      tess.addVertexWithUV(minX, maxY, minZ, maxU, maxV);
      tess.addVertexWithUV(minX, var26, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, var26, minZ, minU, minV);
      tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
      tess.addVertexWithUV(maxX, var26, maxZ, maxU, minV);
      tess.addVertexWithUV(minX, var26, maxZ, minU, minV);
      tess.addVertexWithUV(minX, var26, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, var26, maxZ, maxU, maxV);
      tess.addVertexWithUV(maxX, var26, minZ, maxU, minV);
      tess.addVertexWithUV(minX, var26, minZ, minU, minV);
      tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
      tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
      return true;
   }
}
