package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

public class ServerCommandScoreboard extends CommandBase {
   @Override
   public String getCommandName() {
      return "scoreboard";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length >= 1) {
         if (var2[0].equalsIgnoreCase("objectives")) {
            if (var2.length == 1) {
               throw new WrongUsageException("commands.scoreboard.objectives.usage");
            }

            if (var2[1].equalsIgnoreCase("list")) {
               this.getObjectivesList(var1);
            } else if (var2[1].equalsIgnoreCase("add")) {
               if (var2.length < 4) {
                  throw new WrongUsageException("commands.scoreboard.objectives.add.usage");
               }

               this.addObjective(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("remove")) {
               if (var2.length != 3) {
                  throw new WrongUsageException("commands.scoreboard.objectives.remove.usage");
               }

               this.removeObjective(var1, var2[2]);
            } else {
               if (!var2[1].equalsIgnoreCase("setdisplay")) {
                  throw new WrongUsageException("commands.scoreboard.objectives.usage");
               }

               if (var2.length != 3 && var2.length != 4) {
                  throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage");
               }

               this.setObjectivesDisplay(var1, var2, 2);
            }

            return;
         }

         if (var2[0].equalsIgnoreCase("players")) {
            if (var2.length == 1) {
               throw new WrongUsageException("commands.scoreboard.players.usage");
            }

            if (var2[1].equalsIgnoreCase("list")) {
               if (var2.length > 3) {
                  throw new WrongUsageException("commands.scoreboard.players.list.usage");
               }

               this.listPlayers(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("add")) {
               if (var2.length != 5) {
                  throw new WrongUsageException("commands.scoreboard.players.add.usage");
               }

               this.setPlayerScore(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("remove")) {
               if (var2.length != 5) {
                  throw new WrongUsageException("commands.scoreboard.players.remove.usage");
               }

               this.setPlayerScore(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("set")) {
               if (var2.length != 5) {
                  throw new WrongUsageException("commands.scoreboard.players.set.usage");
               }

               this.setPlayerScore(var1, var2, 2);
            } else {
               if (!var2[1].equalsIgnoreCase("reset")) {
                  throw new WrongUsageException("commands.scoreboard.players.usage");
               }

               if (var2.length != 3) {
                  throw new WrongUsageException("commands.scoreboard.players.reset.usage");
               }

               this.resetPlayerScore(var1, var2, 2);
            }

            return;
         }

         if (var2[0].equalsIgnoreCase("teams")) {
            if (var2.length == 1) {
               throw new WrongUsageException("commands.scoreboard.teams.usage");
            }

            if (var2[1].equalsIgnoreCase("list")) {
               if (var2.length > 3) {
                  throw new WrongUsageException("commands.scoreboard.teams.list.usage");
               }

               this.getTeamList(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("add")) {
               if (var2.length < 3) {
                  throw new WrongUsageException("commands.scoreboard.teams.add.usage");
               }

               this.addTeam(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("remove")) {
               if (var2.length != 3) {
                  throw new WrongUsageException("commands.scoreboard.teams.remove.usage");
               }

               this.removeTeam(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("empty")) {
               if (var2.length != 3) {
                  throw new WrongUsageException("commands.scoreboard.teams.empty.usage");
               }

               this.emptyTeam(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("join")) {
               if (var2.length < 4 && (var2.length != 3 || !(var1 instanceof EntityPlayer))) {
                  throw new WrongUsageException("commands.scoreboard.teams.join.usage");
               }

               this.joinTeam(var1, var2, 2);
            } else if (var2[1].equalsIgnoreCase("leave")) {
               if (var2.length < 3 && !(var1 instanceof EntityPlayer)) {
                  throw new WrongUsageException("commands.scoreboard.teams.leave.usage");
               }

               this.leaveTeam(var1, var2, 2);
            } else {
               if (!var2[1].equalsIgnoreCase("option")) {
                  throw new WrongUsageException("commands.scoreboard.teams.usage");
               }

               if (var2.length != 4 && var2.length != 5) {
                  throw new WrongUsageException("commands.scoreboard.teams.option.usage");
               }

               this.setTeamOption(var1, var2, 2);
            }

            return;
         }
      }

      throw new WrongUsageException("commands.scoreboard.usage");
   }

   protected Scoreboard getScoreboardFromWorldServer() {
      return MinecraftServer.getServer().worldServerForDimension(0).W();
   }

   protected ScoreObjective getScoreObjective(String var1, boolean var2) {
      Scoreboard var3 = this.getScoreboardFromWorldServer();
      ScoreObjective var4 = var3.getObjective(var1);
      if (var4 == null) {
         throw new CommandException("commands.scoreboard.objectiveNotFound", var1);
      } else if (var2 && var4.getCriteria().isReadOnly()) {
         throw new CommandException("commands.scoreboard.objectiveReadOnly", var1);
      } else {
         return var4;
      }
   }

   protected ScorePlayerTeam getTeam(String var1) {
      Scoreboard var2 = this.getScoreboardFromWorldServer();
      ScorePlayerTeam var3 = var2.func_96508_e(var1);
      if (var3 == null) {
         throw new CommandException("commands.scoreboard.teamNotFound", var1);
      } else {
         return var3;
      }
   }

   protected void addObjective(ICommandSender var1, String[] var2, int var3) {
      String var4 = var2[var3++];
      String var5 = var2[var3++];
      Scoreboard var6 = this.getScoreboardFromWorldServer();
      ScoreObjectiveCriteria var7 = (ScoreObjectiveCriteria)ScoreObjectiveCriteria.field_96643_a.get(var5);
      if (var7 == null) {
         String[] var12 = ScoreObjectiveCriteria.field_96643_a.keySet().toArray(new String[0]);
         throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", a(var12));
      } else if (var6.getObjective(var4) != null) {
         throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", var4);
      } else if (var4.length() > 16) {
         throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", var4, 16);
      } else {
         ScoreObjective var8 = var6.func_96535_a(var4, var7);
         if (var2.length > var3) {
            String var9 = a(var1, var2, var3);
            if (var9.length() > 32) {
               throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", var9, 32);
            }

            if (var9.length() > 0) {
               var8.setDisplayName(var9);
            }
         }

         a(var1, "commands.scoreboard.objectives.add.success", new Object[]{var4});
      }
   }

   protected void addTeam(ICommandSender var1, String[] var2, int var3) {
      String var4 = var2[var3++];
      Scoreboard var5 = this.getScoreboardFromWorldServer();
      if (var5.func_96508_e(var4) != null) {
         throw new CommandException("commands.scoreboard.teams.add.alreadyExists", var4);
      } else if (var4.length() > 16) {
         throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", var4, 16);
      } else {
         ScorePlayerTeam var6 = var5.func_96527_f(var4);
         if (var2.length > var3) {
            String var7 = a(var1, var2, var3);
            if (var7.length() > 32) {
               throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", var7, 32);
            }

            if (var7.length() > 0) {
               var6.func_96664_a(var7);
            }
         }

         a(var1, "commands.scoreboard.teams.add.success", new Object[]{var4});
      }
   }

   protected void setTeamOption(ICommandSender var1, String[] var2, int var3) {
      ScorePlayerTeam var4 = this.getTeam(var2[var3++]);
      String var5 = var2[var3++].toLowerCase();
      if (!var5.equalsIgnoreCase("color") && !var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles")) {
         throw new WrongUsageException("commands.scoreboard.teams.option.usage");
      } else if (var2.length == 4) {
         if (var5.equalsIgnoreCase("color")) {
            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", var5, a(EnumChatFormatting.func_96296_a(true, false)));
         } else if (!var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles")) {
            throw new WrongUsageException("commands.scoreboard.teams.option.usage");
         } else {
            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", var5, a(Arrays.asList("true", "false")));
         }
      } else {
         String var6 = var2[var3++];
         if (var5.equalsIgnoreCase("color")) {
            EnumChatFormatting var7 = EnumChatFormatting.func_96300_b(var6);
            if (var6 == null) {
               throw new WrongUsageException("commands.scoreboard.teams.option.noValue", var5, a(EnumChatFormatting.func_96296_a(true, false)));
            }

            var4.func_96666_b(var7.toString());
            var4.func_96662_c(EnumChatFormatting.RESET.toString());
         } else if (var5.equalsIgnoreCase("friendlyfire")) {
            if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false")) {
               throw new WrongUsageException("commands.scoreboard.teams.option.noValue", var5, a(Arrays.asList("true", "false")));
            }

            var4.func_96660_a(var6.equalsIgnoreCase("true"));
         } else if (var5.equalsIgnoreCase("seeFriendlyInvisibles")) {
            if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false")) {
               throw new WrongUsageException("commands.scoreboard.teams.option.noValue", var5, a(Arrays.asList("true", "false")));
            }

            var4.func_98300_b(var6.equalsIgnoreCase("true"));
         }

         a(var1, "commands.scoreboard.teams.option.success", new Object[]{var5, var4.func_96661_b(), var6});
      }
   }

   protected void removeTeam(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      ScorePlayerTeam var5 = this.getTeam(var2[var3++]);
      var4.func_96511_d(var5);
      a(var1, "commands.scoreboard.teams.remove.success", new Object[]{var5.func_96661_b()});
   }

   protected void getTeamList(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      if (var2.length > var3) {
         ScorePlayerTeam var5 = this.getTeam(var2[var3++]);
         Collection var6 = var5.getMembershipCollection();
         if (var6.size() <= 0) {
            throw new CommandException("commands.scoreboard.teams.list.player.empty", var5.func_96661_b());
         }

         var1.sendChatToPlayer(
            EnumChatFormatting.DARK_GREEN + var1.translateString("commands.scoreboard.teams.list.player.count", var6.size(), var5.func_96661_b())
         );
         var1.sendChatToPlayer(a(var6.toArray()));
      } else {
         Collection var9 = var4.func_96525_g();
         if (var9.size() <= 0) {
            throw new CommandException("commands.scoreboard.teams.list.empty");
         }

         var1.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + var1.translateString("commands.scoreboard.teams.list.count", var9.size()));

         for (ScorePlayerTeam var7 : var9) {
            var1.sendChatToPlayer(
               var1.translateString("commands.scoreboard.teams.list.entry", var7.func_96661_b(), var7.func_96669_c(), var7.getMembershipCollection().size())
            );
         }
      }
   }

   protected void joinTeam(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      ScorePlayerTeam var5 = var4.func_96508_e(var2[var3++]);
      HashSet var6 = new HashSet();
      if (var1 instanceof EntityPlayer && var3 == var2.length) {
         String var9 = c(var1).am();
         var4.func_96521_a(var9, var5);
         var6.add(var9);
      } else {
         while (var3 < var2.length) {
            String var7 = d(var1, var2[var3++]);
            var4.func_96521_a(var7, var5);
            var6.add(var7);
         }
      }

      if (!var6.isEmpty()) {
         a(var1, "commands.scoreboard.teams.join.success", new Object[]{var6.size(), var5.func_96661_b(), a(var6.toArray(new String[0]))});
      }
   }

   protected void leaveTeam(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      HashSet var5 = new HashSet();
      HashSet var6 = new HashSet();
      if (var1 instanceof EntityPlayer && var3 == var2.length) {
         String var8 = c(var1).am();
         if (var4.func_96524_g(var8)) {
            var5.add(var8);
         } else {
            var6.add(var8);
         }
      } else {
         while (var3 < var2.length) {
            String var7 = d(var1, var2[var3++]);
            if (var4.func_96524_g(var7)) {
               var5.add(var7);
            } else {
               var6.add(var7);
            }
         }
      }

      if (!var5.isEmpty()) {
         a(var1, "commands.scoreboard.teams.leave.success", new Object[]{var5.size(), a(var5.toArray(new String[0]))});
      }

      if (!var6.isEmpty()) {
         throw new CommandException("commands.scoreboard.teams.leave.failure", var6.size(), a(var6.toArray(new String[0])));
      }
   }

   protected void emptyTeam(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      ScorePlayerTeam var5 = this.getTeam(var2[var3++]);
      ArrayList var6 = new ArrayList(var5.getMembershipCollection());
      if (var6.isEmpty()) {
         throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", var5.func_96661_b());
      } else {
         for (String var8 : var6) {
            var4.removePlayerFromTeam(var8, var5);
         }

         a(var1, "commands.scoreboard.teams.empty.success", new Object[]{var6.size(), var5.func_96661_b()});
      }
   }

   protected void removeObjective(ICommandSender var1, String var2) {
      Scoreboard var3 = this.getScoreboardFromWorldServer();
      ScoreObjective var4 = this.getScoreObjective(var2, false);
      var3.func_96519_k(var4);
      a(var1, "commands.scoreboard.objectives.remove.success", new Object[]{var2});
   }

   protected void getObjectivesList(ICommandSender var1) {
      Scoreboard var2 = this.getScoreboardFromWorldServer();
      Collection var3 = var2.getScoreObjectives();
      if (var3.size() <= 0) {
         throw new CommandException("commands.scoreboard.objectives.list.empty");
      } else {
         var1.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + var1.translateString("commands.scoreboard.objectives.list.count", var3.size()));

         for (ScoreObjective var5 : var3) {
            var1.sendChatToPlayer(
               var1.translateString("commands.scoreboard.objectives.list.entry", var5.getName(), var5.getDisplayName(), var5.getCriteria().func_96636_a())
            );
         }
      }
   }

   protected void setObjectivesDisplay(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      String var5 = var2[var3++];
      int var6 = Scoreboard.getObjectiveDisplaySlotNumber(var5);
      ScoreObjective var7 = null;
      if (var2.length == 4) {
         var7 = this.getScoreObjective(var2[var3++], false);
      }

      if (var6 < 0) {
         throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", var5);
      } else {
         var4.func_96530_a(var6, var7);
         if (var7 != null) {
            a(var1, "commands.scoreboard.objectives.setdisplay.successSet", new Object[]{Scoreboard.getObjectiveDisplaySlot(var6), var7.getName()});
         } else {
            a(var1, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[]{Scoreboard.getObjectiveDisplaySlot(var6)});
         }
      }
   }

   protected void listPlayers(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      if (var2.length > var3) {
         String var5 = d(var1, var2[var3++]);
         Map var6 = var4.func_96510_d(var5);
         if (var6.size() <= 0) {
            throw new CommandException("commands.scoreboard.players.list.player.empty", var5);
         }

         var1.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + var1.translateString("commands.scoreboard.players.list.player.count", var6.size(), var5));

         for (Score var8 : var6.values()) {
            var1.sendChatToPlayer(
               var1.translateString(
                  "commands.scoreboard.players.list.player.entry", var8.func_96652_c(), var8.func_96645_d().getDisplayName(), var8.func_96645_d().getName()
               )
            );
         }
      } else {
         Collection var10 = var4.getObjectiveNames();
         if (var10.size() <= 0) {
            throw new CommandException("commands.scoreboard.players.list.empty");
         }

         var1.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + var1.translateString("commands.scoreboard.players.list.count", var10.size()));
         var1.sendChatToPlayer(a(var10.toArray()));
      }
   }

   protected void setPlayerScore(ICommandSender var1, String[] var2, int var3) {
      String var4 = var2[var3 - 1];
      String var5 = d(var1, var2[var3++]);
      ScoreObjective var6 = this.getScoreObjective(var2[var3++], true);
      int var7 = var4.equalsIgnoreCase("set") ? a(var1, var2[var3++]) : a(var1, var2[var3++], 1);
      Scoreboard var8 = this.getScoreboardFromWorldServer();
      Score var9 = var8.func_96529_a(var5, var6);
      if (var4.equalsIgnoreCase("set")) {
         var9.func_96647_c(var7);
      } else if (var4.equalsIgnoreCase("add")) {
         var9.func_96649_a(var7);
      } else {
         var9.func_96646_b(var7);
      }

      a(var1, "commands.scoreboard.players.set.success", new Object[]{var6.getName(), var5, var9.func_96652_c()});
   }

   protected void resetPlayerScore(ICommandSender var1, String[] var2, int var3) {
      Scoreboard var4 = this.getScoreboardFromWorldServer();
      String var5 = d(var1, var2[var3++]);
      var4.func_96515_c(var5);
      a(var1, "commands.scoreboard.players.reset.success", new Object[]{var5});
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      if (var2.length == 1) {
         return a(var2, new String[]{"objectives", "players", "teams"});
      } else {
         if (var2[0].equalsIgnoreCase("objectives")) {
            if (var2.length == 2) {
               return a(var2, new String[]{"list", "add", "remove", "setdisplay"});
            }

            if (var2[1].equalsIgnoreCase("add")) {
               if (var2.length == 4) {
                  return a(var2, ScoreObjectiveCriteria.field_96643_a.keySet());
               }
            } else if (var2[1].equalsIgnoreCase("remove")) {
               if (var2.length == 3) {
                  return a(var2, this.getScoreObjectivesList(false));
               }
            } else if (var2[1].equalsIgnoreCase("setdisplay")) {
               if (var2.length == 3) {
                  return a(var2, new String[]{"list", "sidebar", "belowName"});
               }

               if (var2.length == 4) {
                  return a(var2, this.getScoreObjectivesList(false));
               }
            }
         } else if (var2[0].equalsIgnoreCase("players")) {
            if (var2.length == 2) {
               return a(var2, new String[]{"set", "add", "remove", "reset", "list"});
            }

            if (!var2[1].equalsIgnoreCase("set") && !var2[1].equalsIgnoreCase("add") && !var2[1].equalsIgnoreCase("remove")) {
               if ((var2[1].equalsIgnoreCase("reset") || var2[1].equalsIgnoreCase("list")) && var2.length == 3) {
                  return a(var2, this.getScoreboardFromWorldServer().getObjectiveNames());
               }
            } else {
               if (var2.length == 3) {
                  return a(var2, MinecraftServer.getServer().getAllUsernames());
               }

               if (var2.length == 4) {
                  return a(var2, this.getScoreObjectivesList(true));
               }
            }
         } else if (var2[0].equalsIgnoreCase("teams")) {
            if (var2.length == 2) {
               return a(var2, new String[]{"add", "remove", "join", "leave", "empty", "list", "option"});
            }

            if (var2[1].equalsIgnoreCase("join")) {
               if (var2.length == 3) {
                  return a(var2, this.getScoreboardFromWorldServer().func_96531_f());
               }

               if (var2.length >= 4) {
                  return a(var2, MinecraftServer.getServer().getAllUsernames());
               }
            } else {
               if (var2[1].equalsIgnoreCase("leave")) {
                  return a(var2, MinecraftServer.getServer().getAllUsernames());
               }

               if (!var2[1].equalsIgnoreCase("empty") && !var2[1].equalsIgnoreCase("list") && !var2[1].equalsIgnoreCase("remove")) {
                  if (var2[1].equalsIgnoreCase("option")) {
                     if (var2.length == 3) {
                        return a(var2, this.getScoreboardFromWorldServer().func_96531_f());
                     }

                     if (var2.length == 4) {
                        return a(var2, new String[]{"color", "friendlyfire", "seeFriendlyInvisibles"});
                     }

                     if (var2.length == 5) {
                        if (var2[3].equalsIgnoreCase("color")) {
                           return a(var2, EnumChatFormatting.func_96296_a(true, false));
                        }

                        if (var2[3].equalsIgnoreCase("friendlyfire") || var2[3].equalsIgnoreCase("seeFriendlyInvisibles")) {
                           return a(var2, new String[]{"true", "false"});
                        }
                     }
                  }
               } else if (var2.length == 3) {
                  return a(var2, this.getScoreboardFromWorldServer().func_96531_f());
               }
            }
         }

         return null;
      }
   }

   protected List getScoreObjectivesList(boolean var1) {
      Collection var2 = this.getScoreboardFromWorldServer().getScoreObjectives();
      ArrayList var3 = new ArrayList();

      for (ScoreObjective var5 : var2) {
         if (!var1 || !var5.getCriteria().isReadOnly()) {
            var3.add(var5.getName());
         }
      }

      return var3;
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      if (var1[0].equalsIgnoreCase("players")) {
         return var2 == 2;
      } else {
         return !var1[0].equalsIgnoreCase("teams") ? false : var2 == 2 || var2 == 3;
      }
   }
}
