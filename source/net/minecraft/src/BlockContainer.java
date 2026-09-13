package net.minecraft.src;

public abstract class BlockContainer extends Block implements ITileEntityProvider {
   protected BlockContainer(int var1, Material var2) {
      super(var1, var2);
      this.isBlockContainer = true;
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
      super.onBlockAdded(var1, var2, var3, var4);
   }

   @Override
   public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
      super.breakBlock(var1, var2, var3, var4, var5, var6);
      var1.removeBlockTileEntity(var2, var3, var4);
   }

   @Override
   public boolean onBlockEventReceived(World var1, int var2, int var3, int var4, int var5, int var6) {
      super.onBlockEventReceived(var1, var2, var3, var4, var5, var6);
      TileEntity var7 = var1.getBlockTileEntity(var2, var3, var4);
      return var7 != null ? var7.receiveClientEvent(var5, var6) : false;
   }
}
