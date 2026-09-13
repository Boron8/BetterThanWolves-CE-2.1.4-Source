package net.minecraft.src;

import java.util.Random;

public class TileEntityEnchantmentTable extends TileEntity {
   public int tickCount;
   public float pageFlip;
   public float pageFlipPrev;
   public float field_70373_d;
   public float field_70374_e;
   public float bookSpread;
   public float bookSpreadPrev;
   public float bookRotation2;
   public float bookRotationPrev;
   public float bookRotation;
   private static Random rand = new Random();
   private String field_94136_s;

   @Override
   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      if (this.func_94135_b()) {
         var1.setString("CustomName", this.field_94136_s);
      }
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      if (var1.hasKey("CustomName")) {
         this.field_94136_s = var1.getString("CustomName");
      }
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      this.bookSpreadPrev = this.bookSpread;
      this.bookRotationPrev = this.bookRotation2;
      EntityPlayer var1 = this.worldObj.getClosestPlayer(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, 3.0);
      if (var1 != null) {
         double var2 = var1.posX - (this.xCoord + 0.5F);
         double var4 = var1.posZ - (this.zCoord + 0.5F);
         this.bookRotation = (float)Math.atan2(var4, var2);
         this.bookSpread += 0.1F;
         if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
            float var6 = this.field_70373_d;

            do {
               this.field_70373_d = this.field_70373_d + (rand.nextInt(4) - rand.nextInt(4));
            } while (var6 == this.field_70373_d);
         }
      } else {
         this.bookRotation += 0.02F;
         this.bookSpread -= 0.1F;
      }

      while (this.bookRotation2 >= (float) Math.PI) {
         this.bookRotation2 -= (float) (Math.PI * 2);
      }

      while (this.bookRotation2 < (float) -Math.PI) {
         this.bookRotation2 += (float) (Math.PI * 2);
      }

      while (this.bookRotation >= (float) Math.PI) {
         this.bookRotation -= (float) (Math.PI * 2);
      }

      while (this.bookRotation < (float) -Math.PI) {
         this.bookRotation += (float) (Math.PI * 2);
      }

      float var7 = this.bookRotation - this.bookRotation2;

      while (var7 >= (float) Math.PI) {
         var7 -= (float) (Math.PI * 2);
      }

      while (var7 < (float) -Math.PI) {
         var7 += (float) (Math.PI * 2);
      }

      this.bookRotation2 += var7 * 0.4F;
      if (this.bookSpread < 0.0F) {
         this.bookSpread = 0.0F;
      }

      if (this.bookSpread > 1.0F) {
         this.bookSpread = 1.0F;
      }

      this.tickCount++;
      this.pageFlipPrev = this.pageFlip;
      float var3 = (this.field_70373_d - this.pageFlip) * 0.4F;
      float var8 = 0.2F;
      if (var3 < -var8) {
         var3 = -var8;
      }

      if (var3 > var8) {
         var3 = var8;
      }

      this.field_70374_e = this.field_70374_e + (var3 - this.field_70374_e) * 0.9F;
      this.pageFlip = this.pageFlip + this.field_70374_e;
   }

   public String func_94133_a() {
      return this.func_94135_b() ? this.field_94136_s : "container.enchant";
   }

   public boolean func_94135_b() {
      return this.field_94136_s != null && this.field_94136_s.length() > 0;
   }

   public void func_94134_a(String var1) {
      this.field_94136_s = var1;
   }
}
