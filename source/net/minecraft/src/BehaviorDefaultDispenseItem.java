package net.minecraft.src;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem {
   @Override
   public final ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
      ItemStack var3 = this.dispenseStack(par1IBlockSource, par2ItemStack);
      this.playDispenseSound(par1IBlockSource);
      this.spawnDispenseParticles(par1IBlockSource, BlockDispenser.getDispenserFacing(par1IBlockSource.getBlockMetadata()));
      return var3;
   }

   protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
      EnumFacing var3 = BlockDispenser.getDispenserFacing(par1IBlockSource.getBlockMetadata());
      IPosition var4 = BlockDispenser.getIPositionFromBlockSource(par1IBlockSource);
      ItemStack var5 = par2ItemStack.splitStack(1);
      doDispense(par1IBlockSource.k(), var5, 6, var3, var4);
      return par2ItemStack;
   }

   public static void doDispense(World par0World, ItemStack par1ItemStack, int par2, EnumFacing par3EnumFacing, IPosition par4IPosition) {
      double var5 = par4IPosition.getX();
      double var7 = par4IPosition.getY();
      double var9 = par4IPosition.getZ();
      EntityItem var11 = (EntityItem)EntityList.createEntityOfType(EntityItem.class, par0World, var5, var7 - 0.3, var9, par1ItemStack);
      double var12 = par0World.rand.nextDouble() * 0.1 + 0.2;
      var11.motionX = par3EnumFacing.getFrontOffsetX() * var12;
      var11.motionY = 0.2F;
      var11.motionZ = par3EnumFacing.getFrontOffsetZ() * var12;
      var11.motionX = var11.motionX + par0World.rand.nextGaussian() * 0.0075F * par2;
      var11.motionY = var11.motionY + par0World.rand.nextGaussian() * 0.0075F * par2;
      var11.motionZ = var11.motionZ + par0World.rand.nextGaussian() * 0.0075F * par2;
      par0World.spawnEntityInWorld(var11);
   }

   protected void playDispenseSound(IBlockSource par1IBlockSource) {
      par1IBlockSource.k().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
   }

   protected void spawnDispenseParticles(IBlockSource par1IBlockSource, EnumFacing par2EnumFacing) {
      par1IBlockSource.k()
         .playAuxSFX(2000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), this.func_82488_a(par2EnumFacing));
   }

   private int func_82488_a(EnumFacing par1EnumFacing) {
      return par1EnumFacing.getFrontOffsetX() + 1 + (par1EnumFacing.getFrontOffsetZ() + 1) * 3;
   }
}
