package btw.entity;

import net.minecraft.src.Packet;

public interface EntityWithCustomPacket {
   Packet getSpawnPacketForThisEntity();

   int getTrackerViewDistance();

   int getTrackerUpdateFrequency();

   boolean getTrackMotion();

   boolean shouldServerTreatAsOversized();
}
