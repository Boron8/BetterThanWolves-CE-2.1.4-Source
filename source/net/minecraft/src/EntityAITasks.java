package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityAITasks {
   private List taskEntries = new ArrayList();
   private List executingTaskEntries = new ArrayList();
   private final Profiler theProfiler;
   private int field_75778_d = 0;
   private int field_75779_e = 3;

   public EntityAITasks(Profiler par1Profiler) {
      this.theProfiler = par1Profiler;
   }

   public void addTask(int par1, EntityAIBase par2EntityAIBase) {
      this.taskEntries.add(new EntityAITaskEntry(this, par1, par2EntityAIBase));
   }

   public void removeTask(EntityAIBase par1EntityAIBase) {
      Iterator var2 = this.taskEntries.iterator();

      while (var2.hasNext()) {
         EntityAITaskEntry var3 = (EntityAITaskEntry)var2.next();
         EntityAIBase var4 = var3.action;
         if (var4 == par1EntityAIBase) {
            if (this.executingTaskEntries.contains(var3)) {
               var4.resetTask();
               this.executingTaskEntries.remove(var3);
            }

            var2.remove();
         }
      }
   }

   public void onUpdateTasks() {
      ArrayList var1 = new ArrayList();
      if (this.field_75778_d++ % this.field_75779_e == 0) {
         for (EntityAITaskEntry var3 : this.taskEntries) {
            boolean var4 = this.executingTaskEntries.contains(var3);
            if (var4) {
               if (this.canUse(var3) && this.canContinue(var3)) {
                  continue;
               }

               var3.action.resetTask();
               this.executingTaskEntries.remove(var3);
            }

            if (this.canUse(var3) && var3.action.shouldExecute()) {
               var1.add(var3);
               this.executingTaskEntries.add(var3);
            }
         }
      } else {
         Iterator var2 = this.executingTaskEntries.iterator();

         while (var2.hasNext()) {
            EntityAITaskEntry var3 = (EntityAITaskEntry)var2.next();
            if (!var3.action.continueExecuting()) {
               var3.action.resetTask();
               var2.remove();
            }
         }
      }

      this.theProfiler.startSection("goalStart");

      for (EntityAITaskEntry var3 : var1) {
         this.theProfiler.startSection(var3.action.getClass().getSimpleName());
         var3.action.startExecuting();
         this.theProfiler.endSection();
      }

      this.theProfiler.endSection();
      this.theProfiler.startSection("goalTick");

      for (EntityAITaskEntry var3 : this.executingTaskEntries) {
         var3.action.updateTask();
      }

      this.theProfiler.endSection();
   }

   private boolean canContinue(EntityAITaskEntry par1EntityAITaskEntry) {
      this.theProfiler.startSection("canContinue");
      boolean var2 = par1EntityAITaskEntry.action.continueExecuting();
      this.theProfiler.endSection();
      return var2;
   }

   private boolean canUse(EntityAITaskEntry par1EntityAITaskEntry) {
      this.theProfiler.startSection("canUse");

      for (EntityAITaskEntry var3 : this.taskEntries) {
         if (var3 != par1EntityAITaskEntry) {
            if (par1EntityAITaskEntry.priority >= var3.priority) {
               if (this.executingTaskEntries.contains(var3) && !this.areTasksCompatible(par1EntityAITaskEntry, var3)) {
                  this.theProfiler.endSection();
                  return false;
               }
            } else if (this.executingTaskEntries.contains(var3) && !var3.action.isInterruptible()) {
               this.theProfiler.endSection();
               return false;
            }
         }
      }

      this.theProfiler.endSection();
      return true;
   }

   private boolean areTasksCompatible(EntityAITaskEntry par1EntityAITaskEntry, EntityAITaskEntry par2EntityAITaskEntry) {
      return (par1EntityAITaskEntry.action.getMutexBits() & par2EntityAITaskEntry.action.getMutexBits()) == 0;
   }

   public void removeAllTasksOfClass(Class classToRemove) {
      Iterator<EntityAITaskEntry> entries = this.taskEntries.iterator();

      while (entries.hasNext()) {
         EntityAITaskEntry tempEntry = entries.next();
         EntityAIBase tempAction = tempEntry.action;
         if (classToRemove.isAssignableFrom(tempAction.getClass())) {
            if (this.executingTaskEntries.contains(tempEntry)) {
               tempAction.resetTask();
               this.executingTaskEntries.remove(tempEntry);
            }

            entries.remove();
         }
      }
   }

   public void removeAllTasks() {
      for (Iterator<EntityAITaskEntry> entries = this.taskEntries.iterator(); entries.hasNext(); entries.remove()) {
         EntityAITaskEntry tempEntry = entries.next();
         if (this.executingTaskEntries.contains(tempEntry)) {
            tempEntry.action.resetTask();
            this.executingTaskEntries.remove(tempEntry);
         }
      }
   }
}
