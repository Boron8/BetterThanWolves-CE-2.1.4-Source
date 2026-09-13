package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.tileentity.EnderChestTileEntity;
import btw.block.tileentity.beacon.BeaconTileEntity;
import btw.inventory.util.InventoryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.BlockEnderChest;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryEnderChest;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class EnderChestBlock extends BlockEnderChest {
   public EnderChestBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new EnderChestTileEntity();
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote && !world.isBlockNormalCube(i, j + 1, k)) {
         EnderChestTileEntity tileEntity = (EnderChestTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity != null) {
            InventoryEnderChest chestInventory = null;
            int iAntennaLevel = this.computeLevelOfEnderChestsAntenna(world, i, j, k);
            if (iAntennaLevel == 0) {
               chestInventory = tileEntity.getLocalEnderChestInventory();
            } else if (iAntennaLevel == 1) {
               chestInventory = world.getLocalLowPowerEnderChestInventory();
            } else if (iAntennaLevel == 2) {
               chestInventory = world.getLocalEnderChestInventory();
            } else if (iAntennaLevel == 3) {
               chestInventory = MinecraftServer.getServer().worldServers[0].worldInfo.getGlobalEnderChestInventory();
            } else {
               chestInventory = player.getInventoryEnderChest();
            }

            if (chestInventory != null) {
               chestInventory.setAssociatedChest(tileEntity);
               player.displayGUIChest(chestInventory);
               if (iAntennaLevel >= 1) {
                  world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.endermen.stare", 0.25F * iAntennaLevel, world.rand.nextFloat() * 0.4F + 1.2F);
               }
            }
         }
      }

      return true;
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      if (!world.isRemote) {
         EnderChestTileEntity tileEntity = (EnderChestTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity != null) {
            InventoryEnderChest chestInventory = tileEntity.getLocalEnderChestInventory();
            if (chestInventory != null) {
               InventoryUtils.ejectInventoryContents(world, i, j, k, chestInventory);
            }
         }
      }

      super.a(world, i, j, k, iBlockID, iMetadata);
   }

   private int computeLevelOfEnderChestsAntenna(World world, int i, int j, int k) {
      return world.getBlockId(i, j - 1, k) == BTWBlocks.aestheticOpaque.blockID && world.getBlockMetadata(i, j - 1, k) == 14
         ? this.getLevelOfAntennaBeaconBlockIsPartOf(world, i, j - 1, k)
         : 0;
   }

   private int getLevelOfAntennaBeaconBlockIsPartOf(World world, int i, int j, int k) {
      for (int iTempLevel = 4; iTempLevel >= 1; iTempLevel--) {
         int iTempJ = j + iTempLevel;

         for (int iTempI = i - iTempLevel; iTempI <= i + iTempLevel; iTempI++) {
            for (int iTempK = k - iTempLevel; iTempK <= k + iTempLevel; iTempK++) {
               if (world.getBlockId(iTempI, iTempJ, iTempK) == Block.beacon.blockID) {
                  BeaconTileEntity tileEntity = (BeaconTileEntity)world.getBlockTileEntity(iTempI, iTempJ, iTempK);
                  if (tileEntity != null && tileEntity.l() >= iTempLevel) {
                     return iTempLevel;
                  }
               }
            }
         }
      }

      return 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }
}
