package net.minecraft.src;

public class EntityMinecartEmpty extends EntityMinecart {
   public EntityMinecartEmpty(World var1) {
      super(var1);
   }

   public EntityMinecartEmpty(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   public boolean interact(EntityPlayer var1) {
      if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != var1) {
         return true;
      } else if (this.riddenByEntity != null && this.riddenByEntity != var1) {
         return false;
      } else {
         if (!this.worldObj.isRemote) {
            var1.mountEntity(this);
         }

         return true;
      }
   }

   @Override
   public int getMinecartType() {
      return 0;
   }
}
