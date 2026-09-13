package net.minecraft.src;

import java.util.Random;

public class BlockStone extends Block {
   public BlockStone(int var1) {
      super(var1, Material.rock);
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Block.cobblestone.blockID;
   }
}
