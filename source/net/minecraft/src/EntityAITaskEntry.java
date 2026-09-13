package net.minecraft.src;

class EntityAITaskEntry {
   public EntityAIBase action;
   public int priority;

   public EntityAITaskEntry(EntityAITasks var1, int var2, EntityAIBase var3) {
      this.tasks = var1;
      this.priority = var2;
      this.action = var3;
   }
}
