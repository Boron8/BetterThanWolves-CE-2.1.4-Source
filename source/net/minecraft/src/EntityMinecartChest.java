package net.minecraft.src;

import btw.item.BTWItems;

public class EntityMinecartChest extends EntityMinecartContainer {
   public EntityMinecartChest(World par1World) {
      super(par1World);
   }

   public EntityMinecartChest(World par1, double par2, double par4, double par6) {
      super(par1, par2, par4, par6);
   }

   @Override
   public void killMinecart(DamageSource par1DamageSource) {
      super.killMinecart(par1DamageSource);
      this.a(BTWItems.sawDust.itemID, 6, 0.0F);
      this.a(Item.stick.itemID, 2, 0.0F);
      if (!this.worldObj.isRemote) {
         this.a("mob.zombie.woodbreak", 0.25F, 0.75F + this.worldObj.rand.nextFloat() * 0.25F);
      }
   }

   @Override
   public int getSizeInventory() {
      return 27;
   }

   @Override
   public int getMinecartType() {
      return 1;
   }

   @Override
   public Block getDefaultDisplayTile() {
      return Block.chest;
   }

   @Override
   public int getDefaultDisplayTileOffset() {
      return 8;
   }
}
