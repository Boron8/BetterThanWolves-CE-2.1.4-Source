package net.minecraft.src;

import btw.AddonHandler;
import btw.BTWMod;
import btw.entity.EntityWithCustomPacket;
import btw.entity.IgnoreServerValidationEntity;
import btw.network.packet.PlayerSyncPacket;
import btw.network.packet.StartBlockHarvestPacket;
import btw.world.util.WorldUtils;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;

public class NetServerHandler extends NetHandler {
   public final INetworkManager netManager;
   public final MinecraftServer mcServer;
   public boolean connectionClosed = false;
   public EntityPlayerMP playerEntity;
   private int currentTicks;
   private int ticksForFloatKick;
   private boolean field_72584_h;
   private int keepAliveRandomID;
   private long keepAliveTimeSent;
   private static Random randomGenerator = new Random();
   private long ticksOfLastKeepAlive;
   private int chatSpamThresholdCount = 0;
   private int creativeItemCreationSpamThresholdTally = 0;
   private double lastPosX;
   private double lastPosY;
   private double lastPosZ;
   private boolean hasMoved = true;
   private IntHashMap field_72586_s = new IntHashMap();

   public NetServerHandler(MinecraftServer par1, INetworkManager par2, EntityPlayerMP par3) {
      this.mcServer = par1;
      this.netManager = par2;
      par2.setNetHandler(this);
      this.playerEntity = par3;
      par3.playerNetServerHandler = this;
   }

   public void networkTick() {
      this.field_72584_h = false;
      this.currentTicks++;
      this.mcServer.theProfiler.startSection("packetflow");
      this.netManager.processReadPackets();
      this.mcServer.theProfiler.endStartSection("keepAlive");
      if (this.currentTicks - this.ticksOfLastKeepAlive > 20L) {
         this.ticksOfLastKeepAlive = this.currentTicks;
         this.keepAliveTimeSent = System.nanoTime() / 1000000L;
         this.keepAliveRandomID = randomGenerator.nextInt();
         this.sendPacketToPlayer(new Packet0KeepAlive(this.keepAliveRandomID));
      }

      if (this.chatSpamThresholdCount > 0) {
         this.chatSpamThresholdCount--;
      }

      if (this.creativeItemCreationSpamThresholdTally > 0) {
         this.creativeItemCreationSpamThresholdTally--;
      }

      this.mcServer.theProfiler.endStartSection("playerTick");
      this.mcServer.theProfiler.endSection();
   }

   public void kickPlayerFromServer(String par1Str) {
      if (!this.connectionClosed) {
         this.playerEntity.mountEntityAndWakeUp();
         this.sendPacketToPlayer(new Packet255KickDisconnect(par1Str));
         this.netManager.serverShutdown();
         this.mcServer
            .getConfigurationManager()
            .sendPacketToAllPlayers(new Packet3Chat(EnumChatFormatting.YELLOW + this.playerEntity.username + " left the game."));
         this.mcServer.getConfigurationManager().playerLoggedOut(this.playerEntity);
         this.connectionClosed = true;
      }
   }

   @Override
   public void handleFlying(Packet10Flying par1Packet10Flying) {
      WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
      this.field_72584_h = true;
      if (!this.playerEntity.playerConqueredTheEnd) {
         if (!this.hasMoved) {
            double var3 = par1Packet10Flying.yPosition - this.lastPosY;
            if (par1Packet10Flying.xPosition == this.lastPosX && var3 * var3 < 0.01 && par1Packet10Flying.zPosition == this.lastPosZ) {
               this.hasMoved = true;
            }
         }

         if (this.hasMoved) {
            if (this.playerEntity.ridingEntity != null) {
               float var34 = this.playerEntity.rotationYaw;
               float var4 = this.playerEntity.rotationPitch;
               this.playerEntity.ridingEntity.updateRiderPosition();
               double var5 = this.playerEntity.posX;
               double var7 = this.playerEntity.posY;
               double var9 = this.playerEntity.posZ;
               double var35 = 0.0;
               double var13 = 0.0;
               if (par1Packet10Flying.rotating) {
                  var34 = par1Packet10Flying.yaw;
                  var4 = par1Packet10Flying.pitch;
               }

               if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999.0 && par1Packet10Flying.stance == -999.0) {
                  if (Math.abs(par1Packet10Flying.xPosition) > 1.0 || Math.abs(par1Packet10Flying.zPosition) > 1.0) {
                     System.err.println(this.playerEntity.username + " was caught trying to crash the server with an invalid position.");
                     this.kickPlayerFromServer("Nope!");
                     return;
                  }

                  var35 = par1Packet10Flying.xPosition;
                  var13 = par1Packet10Flying.zPosition;
               }

               this.playerEntity.onGround = par1Packet10Flying.onGround;
               this.playerEntity.onUpdateEntity();
               this.playerEntity.d(var35, 0.0, var13);
               this.playerEntity.a(var5, var7, var9, var34, var4);
               this.playerEntity.motionX = var35;
               this.playerEntity.motionZ = var13;
               if (this.playerEntity.ridingEntity != null) {
                  var2.uncheckedUpdateEntity(this.playerEntity.ridingEntity, true);
               }

               if (this.playerEntity.ridingEntity != null) {
                  this.playerEntity.ridingEntity.updateRiderPosition();
               }

               this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
               this.lastPosX = this.playerEntity.posX;
               this.lastPosY = this.playerEntity.posY;
               this.lastPosZ = this.playerEntity.posZ;
               var2.g(this.playerEntity);
               return;
            }

            if (this.playerEntity.bz()) {
               this.playerEntity.onUpdateEntity();
               this.playerEntity.a(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
               var2.g(this.playerEntity);
               return;
            }

            double var3 = this.playerEntity.posY;
            this.lastPosX = this.playerEntity.posX;
            this.lastPosY = this.playerEntity.posY;
            this.lastPosZ = this.playerEntity.posZ;
            double var5x = this.playerEntity.posX;
            double var7x = this.playerEntity.posY;
            double var9x = this.playerEntity.posZ;
            float var11 = this.playerEntity.rotationYaw;
            float var12 = this.playerEntity.rotationPitch;
            if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999.0 && par1Packet10Flying.stance == -999.0) {
               par1Packet10Flying.moving = false;
            }

            if (par1Packet10Flying.moving) {
               var5x = par1Packet10Flying.xPosition;
               var7x = par1Packet10Flying.yPosition;
               var9x = par1Packet10Flying.zPosition;
               double var13x = par1Packet10Flying.stance - par1Packet10Flying.yPosition;
               if (!this.playerEntity.bz() && (var13x > 1.65 || var13x < 0.1)) {
                  this.kickPlayerFromServer("Illegal stance");
                  this.mcServer.getLogAgent().logWarning(this.playerEntity.username + " had an illegal stance: " + var13x);
                  return;
               }

               if (Math.abs(par1Packet10Flying.xPosition) > 3.2E7 || Math.abs(par1Packet10Flying.zPosition) > 3.2E7) {
                  this.kickPlayerFromServer("Illegal position");
                  return;
               }
            }

            if (par1Packet10Flying.rotating) {
               var11 = par1Packet10Flying.yaw;
               var12 = par1Packet10Flying.pitch;
            }

            this.playerEntity.onUpdateEntity();
            this.playerEntity.ySize = 0.0F;
            this.playerEntity.a(this.lastPosX, this.lastPosY, this.lastPosZ, var11, var12);
            if (!this.hasMoved) {
               return;
            }

            double var13xx = var5x - this.playerEntity.posX;
            double var15 = var7x - this.playerEntity.posY;
            double var17 = var9x - this.playerEntity.posZ;
            double var19 = Math.min(Math.abs(var13xx), Math.abs(this.playerEntity.motionX));
            double var21 = Math.min(Math.abs(var15), Math.abs(this.playerEntity.motionY));
            double var23 = Math.min(Math.abs(var17), Math.abs(this.playerEntity.motionZ));
            double var25 = var19 * var19 + var21 * var21 + var23 * var23;
            if (var25 > 100.0 && (!this.mcServer.isSinglePlayer() || !this.mcServer.getServerOwner().equals(this.playerEntity.username))) {
               this.mcServer
                  .getLogAgent()
                  .logWarning(
                     this.playerEntity.username
                        + " moved too quickly! "
                        + var13xx
                        + ","
                        + var15
                        + ","
                        + var17
                        + " ("
                        + var19
                        + ", "
                        + var21
                        + ", "
                        + var23
                        + ")"
                  );
               this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
               return;
            }

            float var27 = 0.0625F;
            boolean var28 = this.getCollidingBoundingBoxesIgnoreSpecifiedEntities(
                  var2, this.playerEntity, this.playerEntity.boundingBox.copy().contract(var27, var27, var27)
               )
               .isEmpty();
            if (this.playerEntity.onGround && !par1Packet10Flying.onGround && var15 > 0.0 && !this.playerEntity.g_()) {
               this.playerEntity.addExhaustionForJump();
            }

            this.playerEntity.d(var13xx, var15, var17);
            this.playerEntity.onGround = par1Packet10Flying.onGround;
            this.playerEntity.j(var13xx, var15, var17);
            var13xx = var5x - this.playerEntity.posX;
            var15 = var7x - this.playerEntity.posY;
            if (var15 > -0.5 || var15 < 0.5) {
               var15 = 0.0;
            }

            var17 = var9x - this.playerEntity.posZ;
            var25 = var13xx * var13xx + var15 * var15 + var17 * var17;
            boolean var31 = false;
            if (var25 > 0.0625 && !this.playerEntity.bz() && !this.playerEntity.theItemInWorldManager.isCreative()) {
               var31 = true;
               this.mcServer.getLogAgent().logWarning(this.playerEntity.username + " moved wrongly!");
            }

            this.playerEntity.a(var5x, var7x, var9x, var11, var12);
            boolean var32 = this.getCollidingBoundingBoxesIgnoreSpecifiedEntities(
                  var2, this.playerEntity, this.playerEntity.boundingBox.copy().contract(var27, var27, var27)
               )
               .isEmpty();
            if (var28 && (var31 || !var32) && !this.playerEntity.bz()) {
               this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, var11, var12);
               return;
            }

            AxisAlignedBB var33 = this.playerEntity.boundingBox.copy().expand(var27, var27, var27).addCoord(0.0, -0.55, 0.0);
            if (this.mcServer.isFlightAllowed() || this.playerEntity.theItemInWorldManager.isCreative() || var2.c(var33) || !var2.a(var33, this.playerEntity)) {
               this.ticksForFloatKick = 0;
            } else if (var15 >= -0.03125) {
               this.ticksForFloatKick++;
               if (this.ticksForFloatKick > 80) {
                  this.mcServer.getLogAgent().logWarning(this.playerEntity.username + " was kicked for floating too long!");
                  this.kickPlayerFromServer("Flying is not enabled on this server");
                  return;
               }
            }

            this.playerEntity.onGround = par1Packet10Flying.onGround;
            this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
            this.playerEntity.updateFlyingState(this.playerEntity.posY - var3, par1Packet10Flying.onGround);
         }
      }
   }

   public void setPlayerLocation(double par1, double par3, double par5, float par7, float par8) {
      this.hasMoved = false;
      this.lastPosX = par1;
      this.lastPosY = par3;
      this.lastPosZ = par5;
      this.playerEntity.a(par1, par3, par5, par7, par8);
      this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet13PlayerLookMove(par1, par3 + 1.62F, par3, par5, par7, par8, false));
   }

   @Override
   public void handleBlockDig(Packet14BlockDig par1Packet14BlockDig) {
      WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
      if (par1Packet14BlockDig.status == 4) {
         this.playerEntity.a(false);
      } else if (par1Packet14BlockDig.status == 3) {
         this.playerEntity.a(true);
      } else if (par1Packet14BlockDig.status == 5) {
         this.playerEntity.bZ();
      } else {
         boolean var3 = false;
         if (par1Packet14BlockDig.status == 0) {
            var3 = true;
         }

         if (par1Packet14BlockDig.status == 1) {
            var3 = true;
         }

         if (par1Packet14BlockDig.status == 2) {
            var3 = true;
         }

         int var4 = par1Packet14BlockDig.xPosition;
         int var5 = par1Packet14BlockDig.yPosition;
         int var6 = par1Packet14BlockDig.zPosition;
         if (var3) {
            double var7 = this.playerEntity.posX - (var4 + 0.5);
            double var9 = this.playerEntity.posY - (var5 + 0.5) + 1.5;
            double var11 = this.playerEntity.posZ - (var6 + 0.5);
            double var13 = var7 * var7 + var9 * var9 + var11 * var11;
            if (var13 > 36.0) {
               return;
            }

            if (var5 >= this.mcServer.getBuildLimit()) {
               return;
            }
         }

         if (par1Packet14BlockDig.status == 0) {
            if (!this.mcServer.func_96290_a(var2, var4, var5, var6, this.playerEntity)) {
               this.playerEntity.theItemInWorldManager.onBlockClicked(var4, var5, var6, par1Packet14BlockDig.face);
            } else {
               this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(var4, var5, var6, var2));
            }
         } else if (par1Packet14BlockDig.status == 2) {
            this.playerEntity.theItemInWorldManager.uncheckedTryHarvestBlock(var4, var5, var6, par1Packet14BlockDig.face);
            if (var2.a(var4, var5, var6) != 0) {
               this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(var4, var5, var6, var2));
            }
         } else if (par1Packet14BlockDig.status == 1) {
            this.playerEntity.theItemInWorldManager.cancelDestroyingBlock(var4, var5, var6);
            if (var2.a(var4, var5, var6) != 0) {
               this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(var4, var5, var6, var2));
            }
         }
      }
   }

   @Override
   public void handlePlace(Packet15Place par1Packet15Place) {
      WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
      ItemStack var3 = this.playerEntity.inventory.getCurrentItem();
      boolean var4 = false;
      int var5 = par1Packet15Place.getXPosition();
      int var6 = par1Packet15Place.getYPosition();
      int var7 = par1Packet15Place.getZPosition();
      int var8 = par1Packet15Place.getDirection();
      if (par1Packet15Place.getDirection() == 255) {
         if (var3 == null) {
            return;
         }

         this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, var2, var3);
      } else if (par1Packet15Place.getYPosition() < this.mcServer.getBuildLimit() - 1
         || par1Packet15Place.getDirection() != 1 && par1Packet15Place.getYPosition() < this.mcServer.getBuildLimit()) {
         if (this.hasMoved
            && this.playerEntity.e(var5 + 0.5, var6 + 0.5, var7 + 0.5) < 64.0
            && !this.mcServer.func_96290_a(var2, var5, var6, var7, this.playerEntity)) {
            this.playerEntity
               .theItemInWorldManager
               .activateBlockOrUseItem(
                  this.playerEntity,
                  var2,
                  var3,
                  var5,
                  var6,
                  var7,
                  var8,
                  par1Packet15Place.getXOffset(),
                  par1Packet15Place.getYOffset(),
                  par1Packet15Place.getZOffset()
               );
         }

         var4 = true;
      } else {
         this.playerEntity
            .playerNetServerHandler
            .sendPacketToPlayer(new Packet3Chat("" + EnumChatFormatting.GRAY + "Height limit for building is " + this.mcServer.getBuildLimit()));
         var4 = true;
      }

      if (var4) {
         this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(var5, var6, var7, var2));
         if (var8 == 0) {
            var6--;
         }

         if (var8 == 1) {
            var6++;
         }

         if (var8 == 2) {
            var7--;
         }

         if (var8 == 3) {
            var7++;
         }

         if (var8 == 4) {
            var5--;
         }

         if (var8 == 5) {
            var5++;
         }

         this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(var5, var6, var7, var2));
      }

      var3 = this.playerEntity.inventory.getCurrentItem();
      if (var3 != null && var3.stackSize == 0) {
         this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
         var3 = null;
      }

      if (var3 == null || var3.getMaxItemUseDuration() == 0) {
         this.playerEntity.playerInventoryBeingManipulated = true;
         this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]
         );
         Slot var9 = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
         this.playerEntity.openContainer.detectAndSendChanges();
         this.playerEntity.playerInventoryBeingManipulated = false;
         if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), par1Packet15Place.getItemStack())) {
            this.sendPacketToPlayer(
               new Packet103SetSlot(this.playerEntity.openContainer.windowId, var9.slotNumber, this.playerEntity.inventory.getCurrentItem())
            );
         }
      }
   }

   @Override
   public void handleErrorMessage(String par1Str, Object[] par2ArrayOfObj) {
      this.mcServer.getLogAgent().logInfo(this.playerEntity.username + " lost connection: " + par1Str);
      this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat(EnumChatFormatting.YELLOW + this.playerEntity.ax() + " left the game."));
      this.mcServer.getConfigurationManager().playerLoggedOut(this.playerEntity);
      this.connectionClosed = true;
      if (this.mcServer.isSinglePlayer() && this.playerEntity.username.equals(this.mcServer.getServerOwner())) {
         this.mcServer.getLogAgent().logInfo("Stopping singleplayer server as player logged out");
         this.mcServer.initiateShutdown();
      }
   }

   @Override
   public void unexpectedPacket(Packet par1Packet) {
      this.mcServer.getLogAgent().logWarning(this.getClass() + " wasn't prepared to deal with a " + par1Packet.getClass());
      this.kickPlayerFromServer("Protocol error, unexpected packet");
   }

   public void sendPacket(Packet packet) {
      this.sendPacketToPlayer(packet);
   }

   public void sendPacketToPlayer(Packet par1Packet) {
      if (par1Packet instanceof Packet3Chat) {
         Packet3Chat var2 = (Packet3Chat)par1Packet;
         int var3 = this.playerEntity.getChatVisibility();
         if (var3 == 2) {
            return;
         }

         if (var3 == 1 && !var2.getIsServer()) {
            return;
         }
      }

      try {
         this.netManager.addToSendQueue(par1Packet);
      } catch (Throwable var51) {
         CrashReport var6 = CrashReport.makeCrashReport(var51, "Sending packet");
         CrashReportCategory var4 = var6.makeCategory("Packet being sent");
         var4.addCrashSectionCallable("Packet ID", new CallablePacketID(this, par1Packet));
         var4.addCrashSectionCallable("Packet class", new CallablePacketClass(this, par1Packet));
         throw new ReportedException(var6);
      }
   }

   @Override
   public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch) {
      if (par1Packet16BlockItemSwitch.id >= 0 && par1Packet16BlockItemSwitch.id < InventoryPlayer.getHotbarSize()) {
         this.playerEntity.inventory.currentItem = par1Packet16BlockItemSwitch.id;
      } else {
         this.mcServer.getLogAgent().logWarning(this.playerEntity.username + " tried to set an invalid carried item");
      }
   }

   @Override
   public void handleChat(Packet3Chat par1Packet3Chat) {
      if (this.playerEntity.getChatVisibility() == 2) {
         this.sendPacketToPlayer(new Packet3Chat("Cannot send chat message."));
      } else {
         String var2 = par1Packet3Chat.message;
         if (var2.length() > 100) {
            this.kickPlayerFromServer("Chat message too long");
         } else {
            var2 = var2.trim();

            for (int var3 = 0; var3 < var2.length(); var3++) {
               if (!ChatAllowedCharacters.isAllowedCharacter(var2.charAt(var3))) {
                  this.kickPlayerFromServer("Illegal characters in chat");
                  return;
               }
            }

            if (var2.startsWith("/")) {
               this.handleSlashCommand(var2);
            } else {
               if (this.playerEntity.getChatVisibility() == 1) {
                  this.sendPacketToPlayer(new Packet3Chat("Cannot send chat message."));
                  return;
               }

               var2 = "<" + this.playerEntity.ax() + "> " + var2;
               this.mcServer.getLogAgent().logInfo(var2);
               this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat(var2, false));
            }

            this.chatSpamThresholdCount += 20;
            if (this.chatSpamThresholdCount > 200 && !this.mcServer.getConfigurationManager().areCommandsAllowed(this.playerEntity.username)) {
               this.kickPlayerFromServer("disconnect.spam");
            }
         }
      }
   }

   private void handleSlashCommand(String par1Str) {
      this.mcServer.getCommandManager().executeCommand(this.playerEntity, par1Str);
   }

   @Override
   public void handleAnimation(Packet18Animation par1Packet18Animation) {
      if (par1Packet18Animation.animate == 1) {
         this.playerEntity.bK();
      }
   }

   @Override
   public void handleEntityAction(Packet19EntityAction par1Packet19EntityAction) {
      int state = par1Packet19EntityAction.state & 7;
      if (state == 1) {
         this.playerEntity.b(true);
      } else if (state == 2) {
         this.playerEntity.b(false);
      } else if (state == 4) {
         this.playerEntity.c(true);
      } else if (state == 5) {
         this.playerEntity.c(false);
      } else if (state == 3) {
         this.playerEntity.wakeUpPlayer(false, true, true);
         this.hasMoved = false;
      }

      if ((par1Packet19EntityAction.state & 8) == 8) {
         this.playerEntity.setUsingSpecialKey(true);
      } else {
         this.playerEntity.setUsingSpecialKey(false);
      }
   }

   @Override
   public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect) {
      this.netManager.networkShutdown("disconnect.quitting");
   }

   public int packetSize() {
      return this.netManager.packetSize();
   }

   @Override
   public void handleUseEntity(Packet7UseEntity par1Packet7UseEntity) {
      WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
      Entity var3 = var2.getEntityByID(par1Packet7UseEntity.targetEntity);
      if (var3 != null) {
         boolean var4 = this.playerEntity.n(var3);
         double var5 = 36.0;
         if (!var4) {
            var5 = 9.0;
         }

         if (var3 instanceof EntityWithCustomPacket && ((EntityWithCustomPacket)var3).shouldServerTreatAsOversized()) {
            var4 = true;
            var5 = 256.0;
         }

         if (this.playerEntity.e(var3) < var5) {
            if (par1Packet7UseEntity.isLeftClick == 0) {
               this.playerEntity.p(var3);
            } else if (par1Packet7UseEntity.isLeftClick == 1) {
               this.playerEntity.q(var3);
            }
         }
      }
   }

   @Override
   public void handleClientCommand(Packet205ClientCommand par1Packet205ClientCommand) {
      if (par1Packet205ClientCommand.forceRespawn == 1) {
         if (this.playerEntity.playerConqueredTheEnd) {
            this.playerEntity = this.mcServer.getConfigurationManager().respawnPlayer(this.playerEntity, 0, true);
         } else if (this.playerEntity.getServerForPlayer().M().isHardcoreModeEnabled()) {
            if (this.mcServer.isSinglePlayer() && this.playerEntity.username.equals(this.mcServer.getServerOwner())) {
               this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
               this.mcServer.deleteWorldAndStopServer();
            } else {
               BanEntry var2 = new BanEntry(this.playerEntity.username);
               var2.setBanReason("Death in Hardcore");
               this.mcServer.getConfigurationManager().getBannedPlayers().put(var2);
               this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
            }
         } else {
            if (this.playerEntity.aX() > 0) {
               return;
            }

            this.playerEntity = this.mcServer.getConfigurationManager().respawnPlayer(this.playerEntity, 0, false);
         }
      }
   }

   @Override
   public boolean canProcessPacketsAsync() {
      return true;
   }

   @Override
   public void handleRespawn(Packet9Respawn par1Packet9Respawn) {
   }

   @Override
   public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow) {
      this.playerEntity.closeInventory();
   }

   @Override
   public void handleWindowClick(Packet102WindowClick par1Packet102WindowClick) {
      if (this.playerEntity.openContainer.windowId == par1Packet102WindowClick.window_Id
         && this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity)) {
         ItemStack var2 = this.playerEntity
            .openContainer
            .slotClick(par1Packet102WindowClick.inventorySlot, par1Packet102WindowClick.mouseClick, par1Packet102WindowClick.holdingShift, this.playerEntity);
         if (ItemStack.areItemStacksEqual(par1Packet102WindowClick.itemStack, var2)) {
            this.playerEntity
               .playerNetServerHandler
               .sendPacketToPlayer(new Packet106Transaction(par1Packet102WindowClick.window_Id, par1Packet102WindowClick.action, true));
            this.playerEntity.playerInventoryBeingManipulated = true;
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.updateHeldItem();
            this.playerEntity.playerInventoryBeingManipulated = false;
            if (par1Packet102WindowClick.holdingShift == 2) {
               int iSlotTo = par1Packet102WindowClick.mouseClick;
               if (iSlotTo >= 0 && iSlotTo < 9) {
                  int iSlotFrom = par1Packet102WindowClick.inventorySlot;
                  this.playerEntity
                     .sendSlotContents(
                        this.playerEntity.openContainer, iSlotFrom, (ItemStack)this.playerEntity.openContainer.inventoryItemStacks.get(iSlotFrom)
                     );
                  iSlotTo += this.playerEntity.openContainer.inventorySlots.size() - 9;
                  if (iSlotTo >= 0 && iSlotTo < this.playerEntity.openContainer.inventorySlots.size()) {
                     this.playerEntity
                        .sendSlotContents(this.playerEntity.openContainer, iSlotTo, (ItemStack)this.playerEntity.openContainer.inventoryItemStacks.get(iSlotTo));
                  }
               }
            }
         } else {
            this.field_72586_s.addKey(this.playerEntity.openContainer.windowId, par1Packet102WindowClick.action);
            this.playerEntity
               .playerNetServerHandler
               .sendPacketToPlayer(new Packet106Transaction(par1Packet102WindowClick.window_Id, par1Packet102WindowClick.action, false));
            this.playerEntity.openContainer.setPlayerIsPresent(this.playerEntity, false);
            ArrayList var3 = new ArrayList();

            for (int var4 = 0; var4 < this.playerEntity.openContainer.inventorySlots.size(); var4++) {
               var3.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(var4)).getStack());
            }

            this.playerEntity.sendContainerAndContentsToPlayer(this.playerEntity.openContainer, var3);
         }
      }
   }

   @Override
   public void handleEnchantItem(Packet108EnchantItem par1Packet108EnchantItem) {
      if (this.playerEntity.openContainer.windowId == par1Packet108EnchantItem.windowId
         && this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity)) {
         this.playerEntity.openContainer.enchantItem(this.playerEntity, par1Packet108EnchantItem.enchantment);
         this.playerEntity.openContainer.detectAndSendChanges();
      }
   }

   @Override
   public void handleCreativeSetSlot(Packet107CreativeSetSlot par1Packet107CreativeSetSlot) {
      if (this.playerEntity.theItemInWorldManager.isCreative()) {
         boolean var2 = par1Packet107CreativeSetSlot.slot < 0;
         ItemStack var3 = par1Packet107CreativeSetSlot.itemStack;
         boolean var4 = par1Packet107CreativeSetSlot.slot >= 1 && par1Packet107CreativeSetSlot.slot < 36 + InventoryPlayer.getHotbarSize();
         boolean var5 = var3 == null || var3.itemID < Item.itemsList.length && var3.itemID >= 0 && Item.itemsList[var3.itemID] != null;
         boolean var6 = var3 == null || var3.getItemDamage() >= 0 && var3.getItemDamage() >= 0 && var3.stackSize <= 64 && var3.stackSize > 0;
         if (var4 && var5 && var6) {
            if (var3 == null) {
               this.playerEntity.inventoryContainer.putStackInSlot(par1Packet107CreativeSetSlot.slot, (ItemStack)null);
            } else {
               this.playerEntity.inventoryContainer.putStackInSlot(par1Packet107CreativeSetSlot.slot, var3);
            }

            this.playerEntity.inventoryContainer.setPlayerIsPresent(this.playerEntity, true);
         } else if (var2 && var5 && var6 && this.creativeItemCreationSpamThresholdTally < 200) {
            this.creativeItemCreationSpamThresholdTally += 20;
            EntityItem var7 = this.playerEntity.c(var3);
            if (var7 != null) {
               var7.setAgeToCreativeDespawnTime();
            }
         }
      }
   }

   @Override
   public void handleTransaction(Packet106Transaction par1Packet106Transaction) {
      Short var2 = (Short)this.field_72586_s.lookup(this.playerEntity.openContainer.windowId);
      if (var2 != null
         && par1Packet106Transaction.shortWindowId == var2
         && this.playerEntity.openContainer.windowId == par1Packet106Transaction.windowId
         && !this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity)) {
         this.playerEntity.openContainer.setPlayerIsPresent(this.playerEntity, true);
      }
   }

   @Override
   public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign) {
      WorldServer var2 = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
      if (var2.f(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition)) {
         TileEntity var3 = var2.r(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition);
         if (var3 instanceof TileEntitySign) {
            TileEntitySign var4 = (TileEntitySign)var3;
            if (!var4.isEditable()) {
               this.mcServer.logWarning("Player " + this.playerEntity.username + " just tried to change non-editable sign");
               return;
            }
         }

         for (int var8 = 0; var8 < 4; var8++) {
            boolean var5 = true;
            if (par1Packet130UpdateSign.signLines[var8].length() > 15) {
               var5 = false;
            } else {
               for (int var6 = 0; var6 < par1Packet130UpdateSign.signLines[var8].length(); var6++) {
                  if (ChatAllowedCharacters.allowedCharacters.indexOf(par1Packet130UpdateSign.signLines[var8].charAt(var6)) < 0) {
                     var5 = false;
                  }
               }
            }

            if (!var5) {
               par1Packet130UpdateSign.signLines[var8] = "!?";
            }
         }

         if (var3 instanceof TileEntitySign) {
            int var10 = par1Packet130UpdateSign.xPosition;
            int var9 = par1Packet130UpdateSign.yPosition;
            int var6x = par1Packet130UpdateSign.zPosition;
            TileEntitySign var7 = (TileEntitySign)var3;
            System.arraycopy(par1Packet130UpdateSign.signLines, 0, var7.signText, 0, 4);
            var7.k_();
            var2.j(var10, var9, var6x);
         }
      }
   }

   @Override
   public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive) {
      if (par1Packet0KeepAlive.randomId == this.keepAliveRandomID) {
         int var2 = (int)(System.nanoTime() / 1000000L - this.keepAliveTimeSent);
         this.playerEntity.ping = (this.playerEntity.ping * 3 + var2) / 4;
      }
   }

   @Override
   public boolean isServerHandler() {
      return true;
   }

   @Override
   public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities) {
      this.playerEntity.capabilities.isFlying = par1Packet202PlayerAbilities.getFlying() && this.playerEntity.capabilities.allowFlying;
   }

   @Override
   public void handleAutoComplete(Packet203AutoComplete par1Packet203AutoComplete) {
      StringBuilder var2 = new StringBuilder();

      for (String var4 : this.mcServer.getPossibleCompletions(this.playerEntity, par1Packet203AutoComplete.getText())) {
         if (var2.length() > 0) {
            var2.append("\u0000");
         }

         var2.append(var4);
      }

      this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet203AutoComplete(var2.toString()));
   }

   @Override
   public void handleClientInfo(Packet204ClientInfo par1Packet204ClientInfo) {
      this.playerEntity.updateClientInfo(par1Packet204ClientInfo);
      if (!par1Packet204ClientInfo.isBTWInstalled()) {
         this.playerEntity
            .playerNetServerHandler
            .sendPacketToPlayer(new Packet3Chat("\u00a74" + "WARNING: You do not currently have Better Than Wolves installed on your system."));
         this.playerEntity
            .playerNetServerHandler
            .sendPacketToPlayer(
               new Packet3Chat("\u00a74" + "This server requires all clients to have Better Than Wolves version " + BTWMod.instance.getVersionString())
            );
         this.mcServer.getLogAgent().logInfo(this.playerEntity.username + " logged in without BTW installed");
      }
   }

   @Override
   public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload) {
      if ("MC|BEdit".equals(par1Packet250CustomPayload.channel)) {
         try {
            DataInputStream var2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
            ItemStack var3 = Packet.readItemStack(var2);
            if (!ItemWritableBook.validBookTagPages(var3.getTagCompound())) {
               throw new IOException("Invalid book tag!");
            }

            ItemStack var4 = this.playerEntity.inventory.getCurrentItem();
            if (var3 != null && var3.itemID == Item.writableBook.itemID && var3.itemID == var4.itemID) {
               var4.setTagInfo("pages", var3.getTagCompound().getTagList("pages"));
            }
         } catch (Exception var141) {
            var141.printStackTrace();
         }
      } else if ("MC|BSign".equals(par1Packet250CustomPayload.channel)) {
         try {
            DataInputStream var2x = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
            ItemStack var3x = Packet.readItemStack(var2x);
            if (!ItemEditableBook.validBookTagContents(var3x.getTagCompound())) {
               throw new IOException("Invalid book tag!");
            }

            ItemStack var4 = this.playerEntity.inventory.getCurrentItem();
            if (var3x != null && var3x.itemID == Item.writtenBook.itemID && var4.itemID == Item.writableBook.itemID) {
               var4.setTagInfo("author", new NBTTagString("author", this.playerEntity.username));
               var4.setTagInfo("title", new NBTTagString("title", var3x.getTagCompound().getString("title")));
               var4.setTagInfo("pages", var3x.getTagCompound().getTagList("pages"));
               var4.itemID = Item.writtenBook.itemID;
            }
         } catch (Exception var131) {
            var131.printStackTrace();
         }
      } else if ("MC|TrSel".equals(par1Packet250CustomPayload.channel)) {
         try {
            DataInputStream var2xx = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
            int var14 = var2xx.readInt();
            Container var16 = this.playerEntity.openContainer;
            if (var16 instanceof ContainerMerchant) {
               ((ContainerMerchant)var16).setCurrentRecipeIndex(var14);
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      } else if ("MC|AdvCdm".equals(par1Packet250CustomPayload.channel)) {
         if (!this.mcServer.isCommandBlockEnabled()) {
            this.playerEntity.sendChatToPlayer(this.playerEntity.a("advMode.notEnabled", new Object[0]));
         } else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
            try {
               DataInputStream var2xx = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
               int var14 = var2xx.readInt();
               int var18 = var2xx.readInt();
               int var5 = var2xx.readInt();
               String var6 = Packet.readString(var2xx, 256);
               TileEntity var7 = this.playerEntity.worldObj.getBlockTileEntity(var14, var18, var5);
               if (var7 != null && var7 instanceof TileEntityCommandBlock) {
                  ((TileEntityCommandBlock)var7).setCommand(var6);
                  this.playerEntity.worldObj.markBlockForUpdate(var14, var18, var5);
                  this.playerEntity.sendChatToPlayer("Command set: " + var6);
               }
            } catch (Exception var11) {
               var11.printStackTrace();
            }
         } else {
            this.playerEntity.sendChatToPlayer(this.playerEntity.a("advMode.notAllowed", new Object[0]));
         }
      } else if ("MC|Beacon".equals(par1Packet250CustomPayload.channel)) {
         if (this.playerEntity.openContainer instanceof ContainerBeacon) {
            try {
               DataInputStream var2xx = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
               int var14 = var2xx.readInt();
               int var18 = var2xx.readInt();
               ContainerBeacon var17 = (ContainerBeacon)this.playerEntity.openContainer;
               Slot var19 = var17.a(0);
               if (var19.getHasStack()) {
                  var19.decrStackSize(1);
                  TileEntityBeacon var20 = var17.getBeacon();
                  var20.setPrimaryEffect(var14);
                  var20.setSecondaryEffect(var18);
                  var20.k_();
               }
            } catch (Exception var10) {
               var10.printStackTrace();
            }
         }
      } else if ("MC|ItemName".equals(par1Packet250CustomPayload.channel) && this.playerEntity.openContainer instanceof ContainerRepair) {
         ContainerRepair var13 = (ContainerRepair)this.playerEntity.openContainer;
         if (par1Packet250CustomPayload.data != null && par1Packet250CustomPayload.data.length >= 1) {
            String var15 = ChatAllowedCharacters.filerAllowedCharacters(new String(par1Packet250CustomPayload.data));
            if (var15.length() <= 30) {
               var13.updateItemName(var15);
            }
         } else {
            var13.updateItemName("");
         }
      } else {
         AddonHandler.serverCustomPacketReceived(this, par1Packet250CustomPayload);
      }
   }

   public List getCollidingBoundingBoxesIgnoreSpecifiedEntities(World world, Entity par1Entity, AxisAlignedBB par2AxisAlignedBB) {
      ArrayList collidingBoundingBoxes = new ArrayList();
      int i = MathHelper.floor_double(par2AxisAlignedBB.minX);
      int j = MathHelper.floor_double(par2AxisAlignedBB.maxX + 1.0);
      int k = MathHelper.floor_double(par2AxisAlignedBB.minY);
      int l = MathHelper.floor_double(par2AxisAlignedBB.maxY + 1.0);
      int i1 = MathHelper.floor_double(par2AxisAlignedBB.minZ);
      int j1 = MathHelper.floor_double(par2AxisAlignedBB.maxZ + 1.0);

      for (int k1 = i; k1 < j; k1++) {
         for (int l1 = i1; l1 < j1; l1++) {
            if (world.blockExists(k1, 64, l1)) {
               for (int i2 = k - 1; i2 < l; i2++) {
                  Block block = Block.blocksList[world.getBlockId(k1, i2, l1)];
                  if (block != null) {
                     block.addCollisionBoxesToList(world, k1, i2, l1, par2AxisAlignedBB, collidingBoundingBoxes, par1Entity);
                  }
               }
            }
         }
      }

      double d = 0.25;

      for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB.expand(d, 2.0, d))) {
         if (!(entity instanceof IgnoreServerValidationEntity)) {
            AxisAlignedBB axisalignedbb = entity.getBoundingBox();
            if (axisalignedbb != null && axisalignedbb.intersectsWith(par2AxisAlignedBB)) {
               collidingBoundingBoxes.add(axisalignedbb);
            }

            axisalignedbb = par1Entity.getCollisionBox(entity);
            if (axisalignedbb != null && axisalignedbb.intersectsWith(par2AxisAlignedBB)) {
               collidingBoundingBoxes.add(axisalignedbb);
            }
         }
      }

      return collidingBoundingBoxes;
   }

   @Override
   public void handleStartBlockHarvest(StartBlockHarvestPacket packet) {
      WorldServer world = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
      int i = packet.posX;
      int j = packet.posY;
      int k = packet.posZ;
      double dDeltaX = this.playerEntity.posX - (i + 0.5);
      double dDeltaY = this.playerEntity.posY - (j + 0.5) + 1.5;
      double dDeltaZ = this.playerEntity.posZ - (k + 0.5);
      double dDistSq = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
      if (dDistSq <= 36.0 && j < this.mcServer.getBuildLimit()) {
         if (!this.isBlockWithinSpawnProtection(world, i, j, k)) {
            this.playerEntity.setMiningSpeedModifier(packet.getMiningSpeedModifier());
            this.playerEntity.theItemInWorldManager.onBlockClicked(i, j, k, packet.face);
         } else {
            WorldUtils.sendPacketToPlayer(this.playerEntity.playerNetServerHandler, new Packet53BlockChange(i, j, k, world));
         }
      }
   }

   private boolean isBlockWithinSpawnProtection(World world, int i, int j, int k) {
      return this.mcServer.func_96290_a(world, i, j, k, this.playerEntity);
   }

   @Override
   public void handlePlayerSync(PlayerSyncPacket packet) {
      for (Object other : this.playerEntity.worldObj.playerEntities) {
         EntityPlayerMP otherPlayer = (EntityPlayerMP)other;
         if (otherPlayer.username.equals(packet.playerName)) {
            this.sendPacketToPlayer(new Packet20NamedEntitySpawn(otherPlayer));
            WorldServer worldServer = this.mcServer.worldServerForDimension(this.playerEntity.dimension);

            for (EntityTrackerEntry entry : worldServer.getEntityTracker().trackedEntities) {
               if (entry.myEntity != this.playerEntity) {
                  entry.tryStartWachingThis(this.playerEntity);
               }
            }
         }
      }
   }
}
