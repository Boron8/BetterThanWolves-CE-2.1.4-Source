package btw.client.network.packet.handler;

import btw.entity.item.BloodWoodSaplingItemEntity;
import btw.entity.item.FloatingItemEntity;
import java.io.DataInputStream;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.WorldClient;

@Environment(EnvType.CLIENT)
public class CustomItemEntityHandler implements CustomEntityPacketHandler.EntityPacketHandlerEntry {
   private int entityType;

   public CustomItemEntityHandler(int entityType) {
      this.entityType = entityType;
   }

   @Override
   public Entity handleEntitySpawn(WorldClient world, DataInputStream dataStream, Packet250CustomPayload packet) throws IOException {
      double x = dataStream.readInt() / 32.0;
      double y = dataStream.readInt() / 32.0;
      double z = dataStream.readInt() / 32.0;
      int itemID = dataStream.readInt();
      int stackSize = dataStream.readInt();
      int itemDamage = dataStream.readInt();
      double motionX = dataStream.readByte() * 128.0;
      double motionY = dataStream.readByte() * 128.0;
      double motionZ = dataStream.readByte() * 128.0;
      Entity entityToSpawn;
      if (this.entityType == 4) {
         entityToSpawn = EntityList.createEntityOfType(BloodWoodSaplingItemEntity.class, world, x, y, z, new ItemStack(itemID, stackSize, itemDamage));
      } else {
         entityToSpawn = EntityList.createEntityOfType(FloatingItemEntity.class, world, x, y, z, new ItemStack(itemID, stackSize, itemDamage));
      }

      entityToSpawn.motionX = motionX;
      entityToSpawn.motionY = motionY;
      entityToSpawn.motionZ = motionZ;
      entityToSpawn.serverPosX = (int)(x * 32.0);
      entityToSpawn.serverPosY = (int)(y * 32.0);
      entityToSpawn.serverPosZ = (int)(z * 32.0);
      return entityToSpawn;
   }
}
