package btw.block.tileentity;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntityMobSpawner;

public class MobSpawnerTileEntity extends TileEntityMobSpawner {
   @Override
   public void updateEntity() {
      if (!this.worldObj.isRemote
         && this.worldObj.rand.nextInt(1200) == 0
         && this.worldObj.checkChunksExist(this.xCoord - 4, this.yCoord - 1, this.zCoord - 4, this.xCoord + 4, this.yCoord + 4, this.zCoord + 4)) {
         int xOffset = this.worldObj.rand.nextInt(9);
         int yOffset = this.worldObj.rand.nextInt(6);
         int zOffset = this.worldObj.rand.nextInt(9);
         int x = this.xCoord - 4 + xOffset;
         int y = this.yCoord - 1 + yOffset;
         int z = this.zCoord - 4 + zOffset;
         int targetBlockID = this.worldObj.getBlockId(x, y, z);
         Block targetBlock = Block.blocksList[targetBlockID];
         if (targetBlock != null && targetBlock.canBeConvertedByMobSpawner(this.worldObj, x, y, z)) {
            targetBlock.convertBlockFromMobSpawner(this.worldObj, x, y, z);
         }
      }

      super.updateEntity();
   }
}
