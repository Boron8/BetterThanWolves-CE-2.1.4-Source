package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ScoreboardSaveData extends WorldSavedData {
   private Scoreboard field_96507_a;
   private NBTTagCompound field_96506_b;

   public ScoreboardSaveData() {
      this("scoreboard");
   }

   public ScoreboardSaveData(String var1) {
      super(var1);
   }

   public void func_96499_a(Scoreboard var1) {
      this.field_96507_a = var1;
      if (this.field_96506_b != null) {
         this.readFromNBT(this.field_96506_b);
      }
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      if (this.field_96507_a == null) {
         this.field_96506_b = var1;
      } else {
         this.func_96501_b(var1.getTagList("Objectives"));
         this.func_96500_c(var1.getTagList("PlayerScores"));
         if (var1.hasKey("DisplaySlots")) {
            this.func_96504_c(var1.getCompoundTag("DisplaySlots"));
         }

         if (var1.hasKey("Teams")) {
            this.func_96498_a(var1.getTagList("Teams"));
         }
      }
   }

   protected void func_96498_a(NBTTagList var1) {
      for (int var2 = 0; var2 < var1.tagCount(); var2++) {
         NBTTagCompound var3 = (NBTTagCompound)var1.tagAt(var2);
         ScorePlayerTeam var4 = this.field_96507_a.func_96527_f(var3.getString("Name"));
         var4.func_96664_a(var3.getString("DisplayName"));
         var4.func_96666_b(var3.getString("Prefix"));
         var4.func_96662_c(var3.getString("Suffix"));
         if (var3.hasKey("AllowFriendlyFire")) {
            var4.func_96660_a(var3.getBoolean("AllowFriendlyFire"));
         }

         if (var3.hasKey("SeeFriendlyInvisibles")) {
            var4.func_98300_b(var3.getBoolean("SeeFriendlyInvisibles"));
         }

         this.func_96502_a(var4, var3.getTagList("Players"));
      }
   }

   protected void func_96502_a(ScorePlayerTeam var1, NBTTagList var2) {
      for (int var3 = 0; var3 < var2.tagCount(); var3++) {
         this.field_96507_a.func_96521_a(((NBTTagString)var2.tagAt(var3)).data, var1);
      }
   }

   protected void func_96504_c(NBTTagCompound var1) {
      for (int var2 = 0; var2 < 3; var2++) {
         if (var1.hasKey("slot_" + var2)) {
            String var3 = var1.getString("slot_" + var2);
            ScoreObjective var4 = this.field_96507_a.getObjective(var3);
            this.field_96507_a.func_96530_a(var2, var4);
         }
      }
   }

   protected void func_96501_b(NBTTagList var1) {
      for (int var2 = 0; var2 < var1.tagCount(); var2++) {
         NBTTagCompound var3 = (NBTTagCompound)var1.tagAt(var2);
         ScoreObjectiveCriteria var4 = (ScoreObjectiveCriteria)ScoreObjectiveCriteria.field_96643_a.get(var3.getString("CriteriaName"));
         ScoreObjective var5 = this.field_96507_a.func_96535_a(var3.getString("Name"), var4);
         var5.setDisplayName(var3.getString("DisplayName"));
      }
   }

   protected void func_96500_c(NBTTagList var1) {
      for (int var2 = 0; var2 < var1.tagCount(); var2++) {
         NBTTagCompound var3 = (NBTTagCompound)var1.tagAt(var2);
         ScoreObjective var4 = this.field_96507_a.getObjective(var3.getString("Objective"));
         Score var5 = this.field_96507_a.func_96529_a(var3.getString("Name"), var4);
         var5.func_96647_c(var3.getInteger("Score"));
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound var1) {
      if (this.field_96507_a == null) {
         MinecraftServer.getServer().getLogAgent().logWarning("Tried to save scoreboard without having a scoreboard...");
      } else {
         var1.setTag("Objectives", this.func_96505_b());
         var1.setTag("PlayerScores", this.func_96503_e());
         var1.setTag("Teams", this.func_96496_a());
         this.func_96497_d(var1);
      }
   }

   protected NBTTagList func_96496_a() {
      NBTTagList var1 = new NBTTagList();

      for (ScorePlayerTeam var4 : this.field_96507_a.func_96525_g()) {
         NBTTagCompound var5 = new NBTTagCompound();
         var5.setString("Name", var4.func_96661_b());
         var5.setString("DisplayName", var4.func_96669_c());
         var5.setString("Prefix", var4.func_96668_e());
         var5.setString("Suffix", var4.func_96663_f());
         var5.setBoolean("AllowFriendlyFire", var4.func_96665_g());
         var5.setBoolean("SeeFriendlyInvisibles", var4.func_98297_h());
         NBTTagList var6 = new NBTTagList();

         for (String var8 : var4.getMembershipCollection()) {
            var6.appendTag(new NBTTagString("", var8));
         }

         var5.setTag("Players", var6);
         var1.appendTag(var5);
      }

      return var1;
   }

   protected void func_96497_d(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      boolean var3 = false;

      for (int var4 = 0; var4 < 3; var4++) {
         ScoreObjective var5 = this.field_96507_a.func_96539_a(var4);
         if (var5 != null) {
            var2.setString("slot_" + var4, var5.getName());
            var3 = true;
         }
      }

      if (var3) {
         var1.setCompoundTag("DisplaySlots", var2);
      }
   }

   protected NBTTagList func_96505_b() {
      NBTTagList var1 = new NBTTagList();

      for (ScoreObjective var4 : this.field_96507_a.getScoreObjectives()) {
         NBTTagCompound var5 = new NBTTagCompound();
         var5.setString("Name", var4.getName());
         var5.setString("CriteriaName", var4.getCriteria().func_96636_a());
         var5.setString("DisplayName", var4.getDisplayName());
         var1.appendTag(var5);
      }

      return var1;
   }

   protected NBTTagList func_96503_e() {
      NBTTagList var1 = new NBTTagList();

      for (Score var4 : this.field_96507_a.func_96528_e()) {
         NBTTagCompound var5 = new NBTTagCompound();
         var5.setString("Name", var4.func_96653_e());
         var5.setString("Objective", var4.func_96645_d().getName());
         var5.setInteger("Score", var4.func_96652_c());
         var1.appendTag(var5);
      }

      return var1;
   }
}
