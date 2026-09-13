package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneLight extends Block {
   private final boolean powered;

   public BlockRedstoneLight(int var1, boolean var2) {
      super(var1, Material.redstoneLight);
      this.powered = var2;
      if (var2) {
         this.a(1.0F);
      }
   }

   @Override
   public void registerIcons(IconRegister var1) {
      if (this.powered) {
         this.blockIcon = var1.registerIcon("redstoneLight_lit");
      } else {
         this.blockIcon = var1.registerIcon("redstoneLight");
      }
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
      if (!var1.isRemote) {
         if (this.powered && !var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, 4);
         } else if (!this.powered && var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            var1.setBlock(var2, var3, var4, Block.redstoneLampActive.blockID, 0, 2);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (!var1.isRemote) {
         if (this.powered && !var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, 4);
         } else if (!this.powered && var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
            var1.setBlock(var2, var3, var4, Block.redstoneLampActive.blockID, 0, 2);
         }
      }
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (!var1.isRemote && this.powered && !var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
         var1.setBlock(var2, var3, var4, Block.redstoneLampIdle.blockID, 0, 2);
      }
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Block.redstoneLampIdle.blockID;
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return Block.redstoneLampIdle.blockID;
   }
}
