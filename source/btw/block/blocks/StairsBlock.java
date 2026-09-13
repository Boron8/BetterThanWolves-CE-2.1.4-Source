package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class StairsBlock extends StairsBlockBase {
   protected final Block referenceBlock;
   protected final int referenceBlockMetadata;

   public StairsBlock(int iBlockID, Block referenceBlock, int iReferenceBlockMetadata) {
      super(iBlockID, referenceBlock.blockMaterial);
      this.referenceBlock = referenceBlock;
      this.referenceBlockMetadata = iReferenceBlockMetadata;
      this.c(referenceBlock.blockHardness);
      this.b(referenceBlock.blockResistance / 3.0F);
      this.a(referenceBlock.stepSound);
   }

   @Override
   public void onBlockClicked(World world, int i, int j, int k, EntityPlayer player) {
      this.referenceBlock.onBlockClicked(world, i, j, k, player);
   }

   @Override
   public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int iMetadata) {
      this.referenceBlock.onBlockDestroyedByPlayer(world, i, j, k, iMetadata);
   }

   @Override
   public float getExplosionResistance(Entity entity) {
      return this.referenceBlock.getExplosionResistance(entity);
   }

   @Override
   public int tickRate(World world) {
      return this.referenceBlock.tickRate(world);
   }

   @Override
   public void velocityToAddToEntity(World world, int i, int j, int k, Entity entity, Vec3 velocityVec) {
      this.referenceBlock.velocityToAddToEntity(world, i, j, k, entity, velocityVec);
   }

   @Override
   public boolean isCollidable() {
      return this.referenceBlock.isCollidable();
   }

   @Override
   public boolean canCollideCheck(int iMetadata, boolean flag) {
      return this.referenceBlock.canCollideCheck(iMetadata, flag);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return this.referenceBlock.canPlaceBlockAt(world, i, j, k);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      this.referenceBlock.onBlockAdded(world, i, j, k);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
      this.referenceBlock.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
      this.referenceBlock.onEntityWalking(world, i, j, k, entity);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      this.referenceBlock.updateTick(world, i, j, k, rand);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      return this.referenceBlock.onBlockActivated(world, i, j, k, player, 0, 0.0F, 0.0F, 0.0F);
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      this.referenceBlock.onBlockDestroyedByExplosion(world, i, j, k, explosion);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      this.referenceBlock.randomDisplayTick(world, i, j, k, rand);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getMixedBrightnessForBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return this.referenceBlock.getMixedBrightnessForBlock(blockAccess, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getBlockBrightness(IBlockAccess blockAccess, int i, int j, int k) {
      return this.referenceBlock.getBlockBrightness(blockAccess, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderBlockPass() {
      return this.referenceBlock.getRenderBlockPass();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.referenceBlock.getIcon(iSide, this.referenceBlockMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.referenceBlock.getSelectedBoundingBoxFromPool(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
   }
}
