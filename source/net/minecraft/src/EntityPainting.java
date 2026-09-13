package net.minecraft.src;

import java.util.ArrayList;

public class EntityPainting extends EntityHanging {
   public EnumArt art;

   public EntityPainting(World var1) {
      super(var1);
   }

   public EntityPainting(World var1, int var2, int var3, int var4, int var5) {
      super(var1, var2, var3, var4, var5);
      ArrayList var6 = new ArrayList();

      for (EnumArt var10 : EnumArt.values()) {
         this.art = var10;
         this.a(var5);
         if (this.c()) {
            var6.add(var10);
         }
      }

      if (!var6.isEmpty()) {
         this.art = (EnumArt)var6.get(this.rand.nextInt(var6.size()));
      }

      this.a(var5);
   }

   public EntityPainting(World var1, int var2, int var3, int var4, int var5, String var6) {
      this(var1, var2, var3, var4, var5);

      for (EnumArt var10 : EnumArt.values()) {
         if (var10.title.equals(var6)) {
            this.art = var10;
            break;
         }
      }

      this.a(var5);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setString("Motive", this.art.title);
      super.writeEntityToNBT(var1);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      String var2 = var1.getString("Motive");

      for (EnumArt var6 : EnumArt.values()) {
         if (var6.title.equals(var2)) {
            this.art = var6;
         }
      }

      if (this.art == null) {
         this.art = EnumArt.Kebab;
      }

      super.readEntityFromNBT(var1);
   }

   @Override
   public int func_82329_d() {
      return this.art.sizeX;
   }

   @Override
   public int func_82330_g() {
      return this.art.sizeY;
   }

   @Override
   public void dropItemStack() {
      this.a(new ItemStack(Item.painting), 0.0F);
   }
}
