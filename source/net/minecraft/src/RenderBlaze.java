package net.minecraft.src;

public class RenderBlaze extends RenderLiving {
   private int field_77068_a = ((ModelBlaze)this.mainModel).func_78104_a();

   public RenderBlaze() {
      super(new ModelBlaze(), 0.5F);
   }

   public void renderBlaze(EntityBlaze var1, double var2, double var4, double var6, float var8, float var9) {
      int var10 = ((ModelBlaze)this.mainModel).func_78104_a();
      if (var10 != this.field_77068_a) {
         this.field_77068_a = var10;
         this.mainModel = new ModelBlaze();
      }

      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }
}
