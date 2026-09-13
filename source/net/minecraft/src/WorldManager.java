package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess {
   private MinecraftServer mcServer;
   private WorldServer theWorldServer;

   public WorldManager(MinecraftServer par1MinecraftServer, WorldServer par2WorldServer) {
      this.mcServer = par1MinecraftServer;
      this.theWorldServer = par2WorldServer;
   }

   @Override
   public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {
   }

   @Override
   public void onEntityCreate(Entity par1Entity) {
      this.theWorldServer.getEntityTracker().addEntityToTracker(par1Entity);
   }

   @Override
   public void onEntityDestroy(Entity par1Entity) {
      this.theWorldServer.getEntityTracker().removeEntityFromAllTrackingPlayers(par1Entity);
   }

   @Override
   public void playSound(String par1Str, double par2, double par4, double par6, float par8, float par9) {
      this.mcServer
         .getConfigurationManager()
         .sendToAllNear(
            par2,
            par4,
            par6,
            par8 > 1.0F ? 16.0F * par8 : 16.0,
            this.theWorldServer.provider.dimensionId,
            new Packet62LevelSound(par1Str, par2, par4, par6, par8, par9)
         );
   }

   @Override
   public void playSoundToNearExcept(EntityPlayer par1EntityPlayer, String par2Str, double par3, double par5, double par7, float par9, float par10) {
      this.mcServer
         .getConfigurationManager()
         .sendToAllNearExcept(
            par1EntityPlayer,
            par3,
            par5,
            par7,
            par9 > 1.0F ? 16.0F * par9 : 16.0,
            this.theWorldServer.provider.dimensionId,
            new Packet62LevelSound(par2Str, par3, par5, par7, par9, par10)
         );
   }

   @Override
   public void markBlockRangeForRenderUpdate(int par1, int par2, int par3, int par4, int par5, int par6) {
   }

   @Override
   public void markBlockForUpdate(int par1, int par2, int par3) {
      this.theWorldServer.getChunkTracker().flagBlockForClientUpdate(par1, par2, par3);
   }

   @Override
   public void markBlockForRenderUpdate(int par1, int par2, int par3) {
   }

   @Override
   public void playRecord(String par1Str, int par2, int par3, int par4) {
   }

   @Override
   public void playAuxSFX(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6) {
      this.mcServer
         .getConfigurationManager()
         .sendToAllNearExcept(
            par1EntityPlayer, par3, par4, par5, 64.0, this.theWorldServer.provider.dimensionId, new Packet61DoorChange(par2, par3, par4, par5, par6, false)
         );
   }

   @Override
   public void broadcastSound(int par1, int par2, int par3, int par4, int par5) {
      this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet61DoorChange(par1, par2, par3, par4, par5, true));
   }

   @Override
   public void destroyBlockPartially(int par1, int par2, int par3, int par4, int par5) {
      for (EntityPlayerMP var7 : this.mcServer.getConfigurationManager().playerEntityList) {
         if (var7 != null && var7.worldObj == this.theWorldServer && var7.entityId != par1) {
            double var8 = par2 - var7.posX;
            double var10 = par3 - var7.posY;
            double var12 = par4 - var7.posZ;
            if (var8 * var8 + var10 * var10 + var12 * var12 < 1024.0) {
               var7.playerNetServerHandler.sendPacketToPlayer(new Packet55BlockDestroy(par1, par2, par3, par4, par5));
            }
         }
      }
   }
}
