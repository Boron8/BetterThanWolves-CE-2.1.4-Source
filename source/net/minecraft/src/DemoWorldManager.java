package net.minecraft.src;

public class DemoWorldManager extends ItemInWorldManager {
   private boolean field_73105_c = false;
   private boolean demoTimeExpired = false;
   private int field_73104_e = 0;
   private int field_73102_f = 0;

   public DemoWorldManager(World var1) {
      super(var1);
   }

   @Override
   public void updateBlockRemoving() {
      super.updateBlockRemoving();
      this.field_73102_f++;
      long var1 = this.theWorld.getTotalWorldTime();
      long var3 = var1 / 24000L + 1L;
      if (!this.field_73105_c && this.field_73102_f > 20) {
         this.field_73105_c = true;
         this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 0));
      }

      this.demoTimeExpired = var1 > 120500L;
      if (this.demoTimeExpired) {
         this.field_73104_e++;
      }

      if (var1 % 24000L == 500L) {
         if (var3 <= 6L) {
            this.thisPlayerMP.sendChatToPlayer(this.thisPlayerMP.a("demo.day." + var3, new Object[0]));
         }
      } else if (var3 == 1L) {
         if (var1 == 100L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 101));
         } else if (var1 == 175L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 102));
         } else if (var1 == 250L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 103));
         }
      } else if (var3 == 5L && var1 % 24000L == 22000L) {
         this.thisPlayerMP.sendChatToPlayer(this.thisPlayerMP.a("demo.day.warning", new Object[0]));
      }
   }

   private void sendDemoReminder() {
      if (this.field_73104_e > 100) {
         this.thisPlayerMP.sendChatToPlayer(this.thisPlayerMP.a("demo.reminder", new Object[0]));
         this.field_73104_e = 0;
      }
   }

   @Override
   public void onBlockClicked(int var1, int var2, int var3, int var4) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
      } else {
         super.onBlockClicked(var1, var2, var3, var4);
      }
   }

   @Override
   public void uncheckedTryHarvestBlock(int var1, int var2, int var3) {
      if (!this.demoTimeExpired) {
         super.uncheckedTryHarvestBlock(var1, var2, var3);
      }
   }

   @Override
   public boolean tryHarvestBlock(int var1, int var2, int var3) {
      return this.demoTimeExpired ? false : super.tryHarvestBlock(var1, var2, var3);
   }

   @Override
   public boolean tryUseItem(EntityPlayer var1, World var2, ItemStack var3) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
         return false;
      } else {
         return super.tryUseItem(var1, var2, var3);
      }
   }

   @Override
   public boolean activateBlockOrUseItem(
      EntityPlayer var1, World var2, ItemStack var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10
   ) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
         return false;
      } else {
         return super.activateBlockOrUseItem(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }
   }
}
