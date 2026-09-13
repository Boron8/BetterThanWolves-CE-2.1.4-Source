package btw.item.items;

import btw.block.BTWBlocks;
import btw.entity.mob.ZombiePigmanEntity;
import btw.world.util.BlockPos;
import java.util.List;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.BiomeGenHell;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

public class NetherGrothSporesItem extends Item {
   public NetherGrothSporesItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(false);
      this.setBuoyant();
      this.b("fcItemSporesNetherGroth");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (player != null && !player.canPlayerEdit(i, j, k, iFacing, itemStack)) {
         return false;
      } else if (itemStack.stackSize == 0) {
         return false;
      } else {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iFacing);
         WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
         if (worldchunkmanager != null) {
            BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(i, k);
            if (biomegenbase instanceof BiomeGenHell) {
               int iBlockID = BTWBlocks.netherGroth.blockID;
               int iMetadata = 0;
               if (world.canPlaceEntityOnSide(iBlockID, targetPos.x, targetPos.y, targetPos.z, false, iFacing, player, itemStack)) {
                  if (!world.isRemote) {
                     iMetadata = Block.blocksList[iBlockID]
                        .onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iFacing, fClickX, fClickY, fClickZ, iMetadata);
                     iMetadata = Block.blocksList[iBlockID].preBlockPlacedBy(world, targetPos.x, targetPos.y, targetPos.z, iMetadata, player);
                     if (world.setBlockAndMetadataWithNotify(targetPos.x, targetPos.y, targetPos.z, iBlockID, iMetadata)) {
                        Block block = Block.blocksList[iBlockID];
                        if (world.getBlockId(targetPos.x, targetPos.y, targetPos.z) == iBlockID) {
                           Block.blocksList[iBlockID].onBlockPlacedBy(world, targetPos.x, targetPos.y, targetPos.z, player, itemStack);
                           Block.blocksList[iBlockID].onPostBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iMetadata);
                        }

                        world.playSoundEffect(
                           targetPos.x + 0.5F,
                           targetPos.y + 0.5F,
                           targetPos.z + 0.5F,
                           block.stepSound.getPlaceSound(),
                           (block.stepSound.getPlaceVolume() + 1.0F) / 2.0F,
                           block.stepSound.getPlacePitch() * 0.8F
                        );
                        this.angerPigmen(world, player);
                     }
                  }

                  itemStack.stackSize--;
                  return true;
               }
            }
         }

         return false;
      }
   }

   private void angerPigmen(World world, EntityPlayer entityPlayer) {
      List list = world.getEntitiesWithinAABB(ZombiePigmanEntity.class, entityPlayer.boundingBox.expand(32.0, 32.0, 32.0));

      for (int tempIndex = 0; tempIndex < list.size(); tempIndex++) {
         Entity targetEntity = (Entity)list.get(tempIndex);
         targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), 0);
      }
   }
}
