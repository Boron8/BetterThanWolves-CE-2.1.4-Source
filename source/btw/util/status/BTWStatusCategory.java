package btw.util.status;

public enum BTWStatusCategory implements StatusCategory {
   GLOOM("gloom"),
   HUNGER("hunger"),
   HEALTH("health"),
   FAT("fat");

   private String name;

   private BTWStatusCategory(String name) {
      this.name = name;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
