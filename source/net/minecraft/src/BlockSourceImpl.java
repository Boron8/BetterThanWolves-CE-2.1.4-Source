package net.minecraft.src;

public class BlockSourceImpl implements IBlockSource {
   private final World worldObj;
   private final int xPos;
   private final int yPos;
   private final int zPos;

   public BlockSourceImpl(World var1, int var2, int var3, int var4) {
      this.worldObj = var1;
      this.xPos = var2;
      this.yPos = var3;
      this.zPos = var4;
   }

   @Override
   public World getWorld() {
      return this.worldObj;
   }

   @Override
   public double getX() {
      return this.xPos + 0.5;
   }

   @Override
   public double getY() {
      return this.yPos + 0.5;
   }

   @Override
   public double getZ() {
      return this.zPos + 0.5;
   }

   @Override
   public int getXInt() {
      return this.xPos;
   }

   @Override
   public int getYInt() {
      return this.yPos;
   }

   @Override
   public int getZInt() {
      return this.zPos;
   }

   @Override
   public int getBlockMetadata() {
      return this.worldObj.getBlockMetadata(this.xPos, this.yPos, this.zPos);
   }

   @Override
   public TileEntity getBlockTileEntity() {
      return this.worldObj.getBlockTileEntity(this.xPos, this.yPos, this.zPos);
   }
}
