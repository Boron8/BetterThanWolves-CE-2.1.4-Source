package net.minecraft.src;

import btw.AddonHandler;
import btw.command.ServerLocCommand;
import net.minecraft.server.MinecraftServer;

public class ServerCommandManager extends CommandHandler implements IAdminCommand {
   public ServerCommandManager() {
      this.a(new CommandTime());
      this.a(new CommandGameMode());
      this.a(new CommandDifficulty());
      this.a(new CommandDefaultGameMode());
      this.a(new CommandKill());
      this.a(new CommandToggleDownfall());
      this.a(new CommandWeather());
      this.a(new CommandXP());
      this.a(new CommandServerTp());
      this.a(new CommandGive());
      this.a(new CommandEffect());
      this.a(new CommandEnchant());
      this.a(new CommandServerEmote());
      this.a(new CommandShowSeed());
      this.a(new CommandHelp());
      this.a(new CommandDebug());
      this.a(new CommandServerMessage());
      this.a(new CommandServerSay());
      this.a(new CommandSetSpawnpoint());
      this.a(new CommandGameRule());
      this.a(new CommandClearInventory());
      this.a(new ServerCommandTestFor());
      this.a(new ServerCommandScoreboard());
      this.a(new ServerLocCommand());
      if (MinecraftServer.getServer().isDedicatedServer()) {
         this.a(new CommandServerOp());
         this.a(new CommandServerDeop());
         this.a(new CommandServerStop());
         this.a(new CommandServerSaveAll());
         this.a(new CommandServerSaveOff());
         this.a(new CommandServerSaveOn());
         this.a(new CommandServerBanIp());
         this.a(new CommandServerPardonIp());
         this.a(new CommandServerBan());
         this.a(new CommandServerBanlist());
         this.a(new CommandServerPardon());
         this.a(new CommandServerKick());
         this.a(new CommandServerList());
         this.a(new CommandServerWhitelist());
      } else {
         this.a(new CommandServerPublishLocal());
      }

      if (!MinecraftServer.getIsServer()) {
         for (ICommand command : AddonHandler.commandList) {
            this.a(command);
         }
      }

      CommandBase.setAdminCommander(this);
   }

   @Override
   public void notifyAdmins(ICommandSender par1ICommandSender, int par2, String par3Str, Object... par4ArrayOfObj) {
      boolean var5 = true;
      if (par1ICommandSender instanceof TileEntityCommandBlock
         && !MinecraftServer.getServer().worldServers[0].N().getGameRuleBooleanValue("commandBlockOutput")) {
         var5 = false;
      }

      if (var5) {
         for (EntityPlayerMP var7 : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (var7 != par1ICommandSender && MinecraftServer.getServer().getConfigurationManager().areCommandsAllowed(var7.username)) {
               var7.sendChatToPlayer(
                  ""
                     + EnumChatFormatting.GRAY
                     + ""
                     + EnumChatFormatting.ITALIC
                     + "["
                     + par1ICommandSender.getCommandSenderName()
                     + ": "
                     + var7.a(par3Str, par4ArrayOfObj)
                     + "]"
               );
            }
         }
      }

      if (par1ICommandSender != MinecraftServer.getServer()) {
         MinecraftServer.getServer()
            .getLogAgent()
            .logInfo("[" + par1ICommandSender.getCommandSenderName() + ": " + MinecraftServer.getServer().translateString(par3Str, par4ArrayOfObj) + "]");
      }

      if ((par2 & 1) != 1) {
         par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString(par3Str, par4ArrayOfObj));
      }
   }
}
