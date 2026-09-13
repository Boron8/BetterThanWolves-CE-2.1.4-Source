package btw.entity;

import btw.entity.util.CanvasArt;
import btw.item.BTWItems;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class CanvasEntity extends Entity implements EntityWithCustomPacket {
   private int tickCounter1 = 0;
   public int direction = 0;
   public int canvasPosX;
   public int canvasPosY;
   public int canvasPosZ;
   public CanvasArt art;

   public CanvasEntity(World par1World) {
      super(par1World);
      this.yOffset = 0.0F;
      this.a(0.5F, 0.5F);
   }

   public CanvasEntity(World par1World, int i, int j, int k, int iFacing) {
      this(par1World);
      this.canvasPosX = i;
      this.canvasPosY = j;
      this.canvasPosZ = k;
      ArrayList arraylist = new ArrayList();

      for (CanvasArt enumart : CanvasArt.values()) {
         this.art = enumart;
         this.func_412_b(iFacing);
         if (this.onValidSurface()) {
            arraylist.add(enumart);
         }
      }

      if (arraylist.size() > 0) {
         this.art = (CanvasArt)arraylist.get(this.rand.nextInt(arraylist.size()));
      }

      this.func_412_b(iFacing);
   }

   public CanvasEntity(World par1World, int i, int j, int k, int iFacing, int iArtOrdinal) {
      this(par1World);
      this.canvasPosX = i;
      this.canvasPosY = j;
      this.canvasPosZ = k;
      CanvasArt[] aenumart = CanvasArt.values();
      this.art = aenumart[iArtOrdinal];
      this.func_412_b(iFacing);
   }

   public CanvasEntity(World par1World, int par2, int par3, int par4, int par5, String par6Str) {
      this(par1World);
      this.canvasPosX = par2;
      this.canvasPosY = par3;
      this.canvasPosZ = par4;

      for (CanvasArt enumart : CanvasArt.values()) {
         if (enumart.title.equals(par6Str)) {
            this.art = enumart;
            break;
         }
      }

      this.func_412_b(par5);
   }

   @Override
   protected void entityInit() {
   }

   public void func_412_b(int iFacing) {
      this.direction = iFacing;
      this.prevRotationYaw = this.rotationYaw = iFacing * 90;
      float f = this.art.sizeX;
      float f1 = this.art.sizeY;
      float f2 = this.art.sizeX;
      if (iFacing != 0 && iFacing != 2) {
         f = 0.5F;
      } else {
         f2 = 0.5F;
      }

      f /= 32.0F;
      f1 /= 32.0F;
      f2 /= 32.0F;
      float f3 = this.canvasPosX + 0.5F;
      float f4 = this.canvasPosY + 0.5F;
      float f5 = this.canvasPosZ + 0.5F;
      float f6 = 0.5625F;
      if (iFacing == 0) {
         f5 -= f6;
         f3 -= this.computeBlockOffset(this.art.sizeX);
      } else if (iFacing == 1) {
         f3 -= f6;
         f5 += this.computeBlockOffset(this.art.sizeX);
      } else if (iFacing == 2) {
         f5 += f6;
         f3 += this.computeBlockOffset(this.art.sizeX);
      } else if (iFacing == 3) {
         f3 += f6;
         f5 -= this.computeBlockOffset(this.art.sizeX);
      }

      f4 += this.computeBlockOffset(this.art.sizeY);
      this.b(f3, f4, f5);
      float f7 = -0.00625F;
      this.boundingBox.setBounds(f3 - f - f7, f4 - f1 - f7, f5 - f2 - f7, f3 + f + f7, f4 + f1 + f7, f5 + f2 + f7);
   }

   private float computeBlockOffset(int iEdgeSize) {
      return iEdgeSize % 32 == 0 ? 0.5F : 0.0F;
   }

   @Override
   public void onUpdate() {
      if (this.tickCounter1++ == 100 && !this.worldObj.isRemote) {
         this.tickCounter1 = 0;
         if (!this.isDead && !this.onValidSurface()) {
            this.w();
            this.worldObj
               .spawnEntityInWorld(
                  EntityList.createEntityOfType(EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(BTWItems.canvas))
               );
         }
      }
   }

   public boolean onValidSurface() {
      if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0) {
         return false;
      } else {
         int i = this.art.sizeX / 16;
         int j = this.art.sizeY / 16;
         int k = this.canvasPosX;
         int l = this.canvasPosY;
         int i1 = this.canvasPosZ;
         if (this.direction == 0) {
            k = MathHelper.floor_double(this.posX - this.art.sizeX / 32.0F);
         }

         if (this.direction == 1) {
            i1 = MathHelper.floor_double(this.posZ - this.art.sizeX / 32.0F);
         }

         if (this.direction == 2) {
            k = MathHelper.floor_double(this.posX - this.art.sizeX / 32.0F);
         }

         if (this.direction == 3) {
            i1 = MathHelper.floor_double(this.posZ - this.art.sizeX / 32.0F);
         }

         l = MathHelper.floor_double(this.posY - this.art.sizeY / 32.0F);

         for (int j1 = 0; j1 < i; j1++) {
            for (int k1 = 0; k1 < j; k1++) {
               Material material;
               if (this.direction != 0 && this.direction != 2) {
                  material = this.worldObj.getBlockMaterial(this.canvasPosX, l + k1, i1 + j1);
               } else {
                  material = this.worldObj.getBlockMaterial(k + j1, l + k1, this.canvasPosZ);
               }

               if (!material.isSolid()) {
                  return false;
               }
            }
         }

         List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);

         for (int l1 = 0; l1 < list.size(); l1++) {
            if (list.get(l1) instanceof EntityPainting || list.get(l1) instanceof CanvasEntity) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (!this.isDead && !this.worldObj.isRemote) {
         this.w();
         this.J();
         this.worldObj
            .spawnEntityInWorld(EntityList.createEntityOfType(EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(BTWItems.canvas)));
      }

      return true;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setByte("Dir", (byte)this.direction);
      par1NBTTagCompound.setString("Motive", this.art.title);
      par1NBTTagCompound.setInteger("TileX", this.canvasPosX);
      par1NBTTagCompound.setInteger("TileY", this.canvasPosY);
      par1NBTTagCompound.setInteger("TileZ", this.canvasPosZ);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.direction = par1NBTTagCompound.getByte("Dir");
      this.canvasPosX = par1NBTTagCompound.getInteger("TileX");
      this.canvasPosY = par1NBTTagCompound.getInteger("TileY");
      this.canvasPosZ = par1NBTTagCompound.getInteger("TileZ");
      String s = par1NBTTagCompound.getString("Motive");

      for (CanvasArt enumart : CanvasArt.values()) {
         if (enumart.title.equals(s)) {
            this.art = enumart;
         }
      }

      if (this.art == null) {
         this.art = CanvasArt.Icarus;
      }

      this.func_412_b(this.direction);
   }

   @Override
   public void moveEntity(double par1, double par3, double par5) {
      if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0) {
         this.w();
         this.worldObj
            .spawnEntityInWorld(EntityList.createEntityOfType(EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(BTWItems.canvas)));
      }
   }

   @Override
   public void addVelocity(double par1, double par3, double par5) {
      if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0) {
         this.w();
         this.worldObj
            .spawnEntityInWorld(EntityList.createEntityOfType(EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(BTWItems.canvas)));
      }
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(0);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(this.canvasPosX);
         dataStream.writeInt(this.canvasPosY);
         dataStream.writeInt(this.canvasPosZ);
         dataStream.writeInt(this.direction);
         dataStream.writeInt(this.art.ordinal());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   @Override
   public int getTrackerViewDistance() {
      return 160;
   }

   @Override
   public int getTrackerUpdateFrequency() {
      return Integer.MAX_VALUE;
   }

   @Override
   public boolean getTrackMotion() {
      return false;
   }

   @Override
   public boolean shouldServerTreatAsOversized() {
      return true;
   }

   @Override
   protected boolean shouldSetPositionOnLoad() {
      return false;
   }
}
