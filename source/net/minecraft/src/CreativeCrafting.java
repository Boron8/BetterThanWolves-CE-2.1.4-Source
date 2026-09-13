package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CreativeCrafting implements ICrafting {
   private final Minecraft mc;

   public CreativeCrafting(Minecraft var1) {
      this.mc = var1;
   }

   @Override
   public void sendContainerAndContentsToPlayer(Container var1, List var2) {
   }

   @Override
   public void sendSlotContents(Container var1, int var2, ItemStack var3) {
      this.mc.playerController.sendSlotPacket(var3, var2);
   }

   @Override
   public void sendProgressBarUpdate(Container var1, int var2, int var3) {
   }
}
