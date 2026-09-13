package btw.item.items;

import btw.entity.SoulSandEntity;
import btw.util.hardcorespawn.SpawnLocation;
import btw.util.hardcorespawn.SpawnLocationList;
import net.minecraft.src.ChunkProviderHell;
import net.minecraft.src.ChunkProviderServer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StructureStart;
import net.minecraft.src.World;

public class SoulSandPileItem extends Item {
   public SoulSandPileItem(int iItemID) {
      super(iItemID);
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(8);
      this.b("fcItemPileSoulSand");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!world.isRemote) {
         boolean bHasTarget = false;
         double dTargetXPos = player.posX;
         double dTargetZPos = player.posZ;
         if (world.provider.dimensionId == 0) {
            SpawnLocationList spawnList = world.getSpawnLocationList();
            SpawnLocation closestSpawnLoc = spawnList.getClosestSpawnLocationForPosition(player.posX, player.posZ);
            if (closestSpawnLoc != null) {
               dTargetXPos = closestSpawnLoc.posX;
               dTargetZPos = closestSpawnLoc.posZ;
               bHasTarget = true;
            }
         } else if (world.provider.dimensionId == -1) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider != null && provider instanceof ChunkProviderServer) {
               ChunkProviderServer serverProvider = (ChunkProviderServer)provider;
               provider = serverProvider.getCurrentProvider();
               if (provider != null && provider instanceof ChunkProviderHell) {
                  ChunkProviderHell hellProvider = (ChunkProviderHell)provider;
                  StructureStart closestFortress = hellProvider.genNetherBridge.getClosestStructureWithinRangeSq(player.posX, player.posZ, 90000.0);
                  if (closestFortress != null) {
                     dTargetXPos = closestFortress.boundingBox.getCenterX();
                     dTargetZPos = closestFortress.boundingBox.getCenterZ();
                     bHasTarget = true;
                  }
               }
            }
         }

         SoulSandEntity sandEntity = (SoulSandEntity)EntityList.createEntityOfType(
            SoulSandEntity.class, world, player.posX, player.posY + 2.0 - player.yOffset, player.posZ
         );
         sandEntity.moveTowards(dTargetXPos, dTargetZPos);
         world.spawnEntityInWorld(sandEntity);
         if (bHasTarget) {
            world.playAuxSFX(2228, (int)Math.round(sandEntity.posX), (int)Math.round(sandEntity.posY), (int)Math.round(sandEntity.posZ), 0);
         }

         if (!player.capabilities.isCreativeMode) {
            stack.stackSize--;
         }
      }

      return stack;
   }
}
