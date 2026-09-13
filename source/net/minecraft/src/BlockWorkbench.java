package net.minecraft.src;

public class BlockWorkbench extends Block {
   private Icon workbenchIconTop;
   private Icon workbenchIconFront;

   protected BlockWorkbench(int var1) {
      super(var1, Material.wood);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      if (var1 == 1) {
         return this.workbenchIconTop;
      } else if (var1 == 0) {
         return Block.planks.getBlockTextureFromSide(var1);
      } else {
         return var1 != 2 && var1 != 4 ? this.blockIcon : this.workbenchIconFront;
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon("workbench_side");
      this.workbenchIconTop = var1.registerIcon("workbench_top");
      this.workbenchIconFront = var1.registerIcon("workbench_front");
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      if (var1.isRemote) {
         return true;
      } else {
         var5.displayGUIWorkbench(var2, var3, var4);
         return true;
      }
   }
}
