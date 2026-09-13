package btw.world.util;

import net.minecraft.src.Facing;

public class BlockPos {
   public int x;
   public int y;
   public int z;

   public BlockPos() {
      this.x = this.y = this.z = 0;
   }

   public BlockPos(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public BlockPos(int baseX, int baseY, int baseZ, int facing) {
      this(baseX, baseY, baseZ);
      this.addFacingAsOffset(facing);
   }

   public void addFacingAsOffset(int facing) {
      this.x = this.x + Facing.offsetsXForSide[facing];
      this.y = this.y + Facing.offsetsYForSide[facing];
      this.z = this.z + Facing.offsetsZForSide[facing];
   }

   public void invert() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
   }

   public void addPos(BlockPos pos) {
      this.x = this.x + pos.x;
      this.y = this.y + pos.y;
      this.z = this.z + pos.z;
   }

   public void set(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void set(BlockPos pos) {
      this.x = pos.x;
      this.y = pos.y;
      this.z = pos.z;
   }
}
