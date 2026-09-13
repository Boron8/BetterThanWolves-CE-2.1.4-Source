package btw.block.blocks;

import btw.block.tileentity.beacon.BeaconEffect;
import btw.block.tileentity.beacon.BeaconTileEntity;
import btw.inventory.util.InventoryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockBeacon;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BeaconBlock extends BlockBeacon {
   private static final long RESPAWN_COOLDOWN_DURATION = 60L;

   public BeaconBlock(int iBlockID) {
      super(iBlockID);
      this.setPicksEffectiveOn(true);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new BeaconTileEntity();
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      BeaconTileEntity tileEntity = (BeaconTileEntity)world.getBlockTileEntity(i, j, k);
      if (tileEntity != null) {
         InventoryUtils.ejectInventoryContents(world, i, j, k, tileEntity);
         if (tileEntity.l() > 0) {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.wither.death", 1.0F + world.rand.nextFloat() * 0.1F, 1.0F + world.rand.nextFloat() * 0.1F);
         }

         tileEntity.setPowerState(false, 0, null);
      }

      super.a(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      BeaconTileEntity beaconEnt = (BeaconTileEntity)world.getBlockTileEntity(i, j, k);
      if (beaconEnt != null) {
         BeaconEffect beaconEffect = beaconEnt.getActiveEffect();
         if (beaconEffect == BeaconTileEntity.SPAWN_ANCHOR_EFFECT) {
            int iBeaconPowerLevel = beaconEnt.l();
            if (iBeaconPowerLevel > 0) {
               if (!world.isRemote
                  && (world.getWorldTime() < player.respawnAssignmentCooldownTimer || world.getWorldTime() - player.respawnAssignmentCooldownTimer > 60L)) {
                  player.addChatMessage(this.a() + ".playerBound");
                  ChunkCoordinates newSpawnPos = new ChunkCoordinates(i, j, k);
                  player.setSpawnChunk(newSpawnPos, false, world.provider.dimensionId);
                  player.respawnAssignmentCooldownTimer = world.getWorldTime();
                  world.playAuxSFX(2225, i, j, k, 1);
                  world.playSoundEffect(
                     i + 0.5, j + 0.5, k + 0.5, "mob.wither.spawn", 1.0F + world.rand.nextFloat() * 0.1F, 1.0F + world.rand.nextFloat() * 0.1F
                  );
               }

               return true;
            }
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide != 0 ? true : !blockAccess.isBlockOpaqueCube(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockBeacon(this, i, j, k);
   }
}
