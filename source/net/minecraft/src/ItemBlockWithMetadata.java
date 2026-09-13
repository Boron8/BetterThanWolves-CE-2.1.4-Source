package net.minecraft.src;

public class ItemBlockWithMetadata extends ItemBlock {
   private Block theBlock;

   public ItemBlockWithMetadata(int var1, Block var2) {
      super(var1);
      this.theBlock = var2;
      this.e(0);
      this.a(true);
   }

   @Override
   public Icon getIconFromDamage(int var1) {
      return this.theBlock.getIcon(2, var1);
   }

   @Override
   public int getMetadata(int var1) {
      return var1;
   }
}
