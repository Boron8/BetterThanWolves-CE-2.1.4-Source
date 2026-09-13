package net.minecraft.src;

public interface IBlockSource extends ILocatableSource {
   @Override
   double getX();

   @Override
   double getY();

   @Override
   double getZ();

   int getXInt();

   int getYInt();

   int getZInt();

   int getBlockMetadata();

   TileEntity getBlockTileEntity();
}
