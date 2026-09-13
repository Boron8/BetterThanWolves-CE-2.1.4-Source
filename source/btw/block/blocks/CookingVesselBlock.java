package btw.block.blocks;

import btw.BTWMod;
import btw.block.tileentity.CookingVesselTileEntity;
import btw.inventory.container.CookingVesselContainer;
import btw.inventory.util.InventoryUtils;
import java.util.List;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Container;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IInventory;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class CookingVesselBlock extends VesselBlock {
   public CookingVesselBlock(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      InventoryUtils.ejectInventoryContents(world, i, j, k, (IInventory)world.getBlockTileEntity(i, j, k));
      super.a(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote) {
         List collisionList = null;
         if (!this.getMechanicallyPoweredFlag(world, i, j, k)) {
            collisionList = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(i, j + 1.0, k, i + 1, j + 1.0 + 0.05F, k + 1));
            if (collisionList != null && collisionList.size() > 0) {
               TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
               if (!(tileEnt instanceof IInventory)) {
                  return;
               }

               IInventory inventoryEntity = (IInventory)tileEnt;

               for (int listIndex = 0; listIndex < collisionList.size(); listIndex++) {
                  EntityItem targetEntityItem = (EntityItem)collisionList.get(listIndex);
                  if (!targetEntityItem.isDead && InventoryUtils.addItemStackToInventory(inventoryEntity, targetEntityItem.getEntityItem())) {
                     world.playSoundEffect(
                        i + 0.5, j + 0.5, k + 0.5, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F
                     );
                     targetEntityItem.w();
                  }
               }
            }
         }
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      this.validateFireUnderState(world, i, j, k);
      super.updateTick(world, i, j, k, rand);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      this.validateFireUnderState(world, i, j, k);
      super.onNeighborBlockChange(world, i, j, k, iBlockID);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (!this.isOpenSideBlocked(world, i, j, k) && !world.isRemote) {
         TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
         if (tileEnt instanceof CookingVesselTileEntity) {
            CookingVesselTileEntity vesselEntity = (CookingVesselTileEntity)world.getBlockTileEntity(i, j, k);
            if (player instanceof EntityPlayerMP) {
               CookingVesselContainer container = new CookingVesselContainer(player.inventory, vesselEntity);
               BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, this.getContainerID());
            }
         }
      }

      return true;
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
   }

   protected abstract void validateFireUnderState(World var1, int var2, int var3, int var4);

   protected abstract int getContainerID();
}
