package btw.util;

import btw.block.BTWBlocks;
import btw.block.util.BlockState;
import btw.world.util.BlockPos;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.src.EntityList;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class Schematic {
   public int sizeX;
   public int sizeY;
   public int sizeZ;
   private Map<Integer, BlockState> blockMap;
   private List<NBTTagCompound> entities;
   private List<NBTTagCompound> tileEntities;

   private Schematic() {
   }

   public Schematic(NBTTagCompound tag) {
      this.loadFromNBT(tag);
   }

   public void loadFromNBT(NBTTagCompound tag) {
      this.sizeX = tag.getShort("Width");
      this.sizeY = tag.getShort("Height");
      this.sizeZ = tag.getShort("Length");
      byte[] blockIDs = tag.getByteArray("Blocks");
      byte[] blockIDAdd = tag.getByteArray("AddBlocks");
      byte[] metadata = tag.getByteArray("Data");
      this.processBlockData(blockIDs, blockIDAdd, metadata);
      NBTTagCompound entityTag = tag.getCompoundTag("Entities");
      this.processEntityData(entityTag);
      NBTTagCompound tileEntityTag = tag.getCompoundTag("TileEntities");
      this.processTileEntityData(tileEntityTag);
   }

   protected void processBlockData(byte[] blockIDs, byte[] blockIDsAdd, byte[] metadata) {
      int size = blockIDs.length;
      int[] blockIDsFull = new int[size];

      for (int i = 0; i < size; i++) {
         byte blockIDAdd = blockIDsAdd[i / 2];
         if (i % 2 == 0) {
            blockIDAdd = (byte)(blockIDAdd >> 4);
         } else {
            blockIDAdd = (byte)(blockIDAdd & 15);
         }

         blockIDsFull[i] = blockIDAdd << 8 + blockIDs[i];
      }

      for (int i = 0; i < size; i++) {
         this.blockMap.put(i, new BlockState(blockIDsFull[i], metadata[i]));
      }
   }

   protected void putBlockAtCoords(int x, int y, int z, BlockState blockState) {
      this.blockMap.put(this.getIndexForCoords(x, y, z), blockState);
   }

   protected void processEntityData(NBTTagCompound tag) {
      for (NBTTagCompound entity : tag.getTags()) {
         this.entities.add(entity);
      }
   }

   protected void processTileEntityData(NBTTagCompound tag) {
      for (NBTTagCompound tileEntity : tag.getTags()) {
         this.tileEntities.add(tileEntity);
      }
   }

   public void addSchematicToWorld(World world, int x, int y, int z) {
      for (int index : this.blockMap.keySet()) {
         BlockPos pos = this.getCoordsForIndex(index);
         int i = x + pos.x;
         int j = y + pos.y;
         int k = z + pos.z;
         BlockState blockState = this.blockMap.get(index);
         int id = blockState.id;
         int metadata = blockState.metadata;
         if (id != 0) {
            if (id == BTWBlocks.structureVoid.blockID) {
               id = 0;
            }

            world.setBlockAndMetadata(i, j, k, id, metadata);
         }
      }

      for (NBTTagCompound tag : this.entities) {
         EntityList.createEntityFromNBT(tag, world);
      }

      for (NBTTagCompound tag : this.tileEntities) {
         TileEntity.createAndLoadEntity(tag);
      }
   }

   public Schematic copy() {
      Schematic schematic = new Schematic();
      schematic.sizeX = this.sizeX;
      schematic.sizeY = this.sizeY;
      schematic.sizeZ = this.sizeZ;
      Map<Integer, BlockState> newBlockMap = new HashMap<>();

      for (Entry<Integer, BlockState> entry : this.blockMap.entrySet()) {
         newBlockMap.put(entry.getKey(), entry.getValue());
      }

      schematic.blockMap = newBlockMap;
      Collections.copy(schematic.entities, this.entities);
      Collections.copy(schematic.tileEntities, this.tileEntities);
      return schematic;
   }

   public void rotate(boolean clockwise) {
      this.rotate(clockwise, 1);
   }

   public void rotate(boolean clockwise, int count) {
   }

   public void mirror(boolean useXPlane) {
   }

   protected int getIndexForCoords(int x, int y, int z) {
      return (y * this.sizeZ + z) * this.sizeX + x;
   }

   protected BlockPos getCoordsForIndex(int index) {
      int x = index % this.sizeX;
      index /= this.sizeX;
      int z = index % this.sizeZ;
      index /= this.sizeZ;
      return new BlockPos(x, index, z);
   }

   public BlockState getBlockAtCoords(int x, int y, int z) {
      return this.blockMap.get(this.getIndexForCoords(x, y, z));
   }

   public int getSizeX() {
      return this.sizeX;
   }

   public int getSizeY() {
      return this.sizeY;
   }

   public int getSizeZ() {
      return this.sizeZ;
   }
}
