package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class DedicatedPlayerList extends ServerConfigurationManager {
   private File opsList;
   private File whiteList;

   public DedicatedPlayerList(DedicatedServer var1) {
      super(var1);
      this.opsList = var1.e("ops.txt");
      this.whiteList = var1.e("white-list.txt");
      this.viewDistance = var1.getIntProperty("view-distance", 10);
      this.maxPlayers = var1.getIntProperty("max-players", 20);
      this.setWhiteListEnabled(var1.getBooleanProperty("white-list", false));
      if (!var1.I()) {
         this.e().setListActive(true);
         this.f().setListActive(true);
      }

      this.e().loadBanList();
      this.e().saveToFileWithHeader();
      this.f().loadBanList();
      this.f().saveToFileWithHeader();
      this.loadOpsList();
      this.readWhiteList();
      this.saveOpsList();
      if (!this.whiteList.exists()) {
         this.saveWhiteList();
      }
   }

   @Override
   public void setWhiteListEnabled(boolean var1) {
      super.setWhiteListEnabled(var1);
      this.getDedicatedServerInstance().setProperty("white-list", var1);
      this.getDedicatedServerInstance().saveProperties();
   }

   @Override
   public void addOp(String var1) {
      super.addOp(var1);
      this.saveOpsList();
   }

   @Override
   public void removeOp(String var1) {
      super.removeOp(var1);
      this.saveOpsList();
   }

   @Override
   public void removeFromWhitelist(String var1) {
      super.removeFromWhitelist(var1);
      this.saveWhiteList();
   }

   @Override
   public void addToWhiteList(String var1) {
      super.addToWhiteList(var1);
      this.saveWhiteList();
   }

   @Override
   public void loadWhiteList() {
      this.readWhiteList();
   }

   private void loadOpsList() {
      try {
         this.i().clear();
         BufferedReader var1 = new BufferedReader(new FileReader(this.opsList));
         String var2 = "";

         while ((var2 = var1.readLine()) != null) {
            this.i().add(var2.trim().toLowerCase());
         }

         var1.close();
      } catch (Exception var3) {
         this.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load operators list: " + var3);
      }
   }

   private void saveOpsList() {
      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(this.opsList, false));

         for (String var3 : this.i()) {
            var1.println(var3);
         }

         var1.close();
      } catch (Exception var4) {
         this.getDedicatedServerInstance().getLogAgent().logWarning("Failed to save operators list: " + var4);
      }
   }

   private void readWhiteList() {
      try {
         this.h().clear();
         BufferedReader var1 = new BufferedReader(new FileReader(this.whiteList));
         String var2 = "";

         while ((var2 = var1.readLine()) != null) {
            this.h().add(var2.trim().toLowerCase());
         }

         var1.close();
      } catch (Exception var3) {
         this.getDedicatedServerInstance().getLogAgent().logWarning("Failed to load white-list: " + var3);
      }
   }

   private void saveWhiteList() {
      try {
         PrintWriter var1 = new PrintWriter(new FileWriter(this.whiteList, false));

         for (String var3 : this.h()) {
            var1.println(var3);
         }

         var1.close();
      } catch (Exception var4) {
         this.getDedicatedServerInstance().getLogAgent().logWarning("Failed to save white-list: " + var4);
      }
   }

   @Override
   public boolean isAllowedToLogin(String var1) {
      var1 = var1.trim().toLowerCase();
      return !this.n() || this.e(var1) || this.h().contains(var1);
   }

   public DedicatedServer getDedicatedServerInstance() {
      return (DedicatedServer)super.getServerInstance();
   }
}
