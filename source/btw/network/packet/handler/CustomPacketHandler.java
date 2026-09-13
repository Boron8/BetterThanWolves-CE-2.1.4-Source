package btw.network.packet.handler;

import java.io.IOException;
import net.minecraft.src.Packet250CustomPayload;

public interface CustomPacketHandler {
   void handleCustomPacket(Packet250CustomPayload var1) throws IOException;
}
