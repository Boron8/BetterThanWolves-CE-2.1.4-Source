package btw.block.blocks;

import btw.block.tileentity.FiniteTorchTileEntity;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ITileEntityProvider;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class FiniteBurningTorchBlock extends TorchBlockBurningBase implements ITileEntityProvider {
   private boolean isBeingCrushed = false;
   @Environment(EnvType.CLIENT)
   private Icon iconSputtering;

   public FiniteBurningTorchBlock(int iBlockID) {
      super(iBlockID);
      this.isBlockContainer = true;
      this.a(0.875F);
      this.c("fcBlockTorchFiniteBurning");
      this.a(null);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new FiniteTorchTileEntity();
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      super.a(world, i, j, k, iBlockID, iMetadata);
      if (!world.isRemote) {
         FiniteTorchTileEntity tileEntity = (FiniteTorchTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity != null) {
            int iBurnCountdown = tileEntity.burnTimeCountdown;
            if (iBurnCountdown > 0 && !this.isBeingCrushed) {
               int iNewItemDamage = (int)(0.0013333333F * (24000 - tileEntity.burnTimeCountdown));
               iNewItemDamage = MathHelper.clamp_int(iNewItemDamage, 1, 31);
               ItemStack stack = new ItemStack(this.blockID, 1, iNewItemDamage);
               long iExpiryTime = WorldUtils.getOverworldTimeServerOnly() + iBurnCountdown;
               stack.setTagCompound(new NBTTagCompound());
               stack.getTagCompound().setLong("outTime", iExpiryTime);
               this.b(world, i, j, k, stack);
            }
         }
      }

      this.isBeingCrushed = false;
      world.removeBlockTileEntity(i, j, k);
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entity, ItemStack stack) {
      TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
      if (tileEntity != null && tileEntity instanceof FiniteTorchTileEntity && stack.hasTagCompound() && stack.getTagCompound().hasKey("outTime")) {
         long lExpiryTime = stack.getTagCompound().getLong("outTime");
         int iCountDown = (int)(lExpiryTime - WorldUtils.getOverworldTimeServerOnly());
         if (iCountDown < 0 || iCountDown > 24000) {
            iCountDown = 24000;
         }

         ((FiniteTorchTileEntity)tileEntity).burnTimeCountdown = iCountDown;
         if (iCountDown < 600) {
            this.setIsSputtering(world, i, j, k, true);
         }
      }
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      this.isBeingCrushed = true;
   }

   @Override
   public void onFluidFlowIntoBlock(World world, int i, int j, int k, BlockFluid newBlock) {
      world.playAuxSFX(2227, i, j, k, 0);
      this.isBeingCrushed = true;
   }

   @Override
   public boolean onRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      world.setBlockToAir(i, j, k);
      return false;
   }

   public void setIsSputtering(World world, int i, int j, int k, boolean bSputtering) {
      int iMetadata = setIsSputtering(world.getBlockMetadata(i, j, k), bSputtering);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public static int setIsSputtering(int iMetadata, boolean bIsSputtering) {
      if (bIsSputtering) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }

   public boolean getIsSputtering(IBlockAccess blockAccess, int i, int j, int k) {
      return getIsSputtering(blockAccess.getBlockMetadata(i, j, k));
   }

   public static boolean getIsSputtering(int iMetadata) {
      return (iMetadata & 8) != 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconSputtering = register.registerIcon("fcBlockTorchFiniteSputtering");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return getIsSputtering(iMetadata) ? this.iconSputtering : super.a(iSide, iMetadata);
   }
}
