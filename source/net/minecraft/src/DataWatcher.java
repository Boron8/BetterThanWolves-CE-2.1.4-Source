package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataWatcher {
   private boolean isBlank = true;
   private static final HashMap dataTypes = new HashMap();
   private final Map watchedObjects = new HashMap();
   private boolean objectChanged;
   private ReadWriteLock lock = new ReentrantReadWriteLock();

   public void addObject(int var1, Object var2) {
      Integer var3 = (Integer)dataTypes.get(var2.getClass());
      if (var3 == null) {
         throw new IllegalArgumentException("Unknown data type: " + var2.getClass());
      } else if (var1 > 31) {
         throw new IllegalArgumentException("Data value id is too big with " + var1 + "! (Max is " + 31 + ")");
      } else if (this.watchedObjects.containsKey(var1)) {
         throw new IllegalArgumentException("Duplicate id value for " + var1 + "!");
      } else {
         WatchableObject var4 = new WatchableObject(var3, var1, var2);
         this.lock.writeLock().lock();
         this.watchedObjects.put(var1, var4);
         this.lock.writeLock().unlock();
         this.isBlank = false;
      }
   }

   public void addObjectByDataType(int var1, int var2) {
      WatchableObject var3 = new WatchableObject(var2, var1, null);
      this.lock.writeLock().lock();
      this.watchedObjects.put(var1, var3);
      this.lock.writeLock().unlock();
      this.isBlank = false;
   }

   public byte getWatchableObjectByte(int var1) {
      return (Byte)this.getWatchedObject(var1).getObject();
   }

   public short getWatchableObjectShort(int var1) {
      return (Short)this.getWatchedObject(var1).getObject();
   }

   public int getWatchableObjectInt(int var1) {
      return (Integer)this.getWatchedObject(var1).getObject();
   }

   public String getWatchableObjectString(int var1) {
      return (String)this.getWatchedObject(var1).getObject();
   }

   public ItemStack getWatchableObjectItemStack(int var1) {
      return (ItemStack)this.getWatchedObject(var1).getObject();
   }

   private WatchableObject getWatchedObject(int var1) {
      this.lock.readLock().lock();

      WatchableObject var2;
      try {
         var2 = (WatchableObject)this.watchedObjects.get(var1);
      } catch (Throwable var6) {
         CrashReport var4 = CrashReport.makeCrashReport(var6, "Getting synched entity data");
         CrashReportCategory var5 = var4.makeCategory("Synched entity data");
         var5.addCrashSection("Data ID", var1);
         throw new ReportedException(var4);
      }

      this.lock.readLock().unlock();
      return var2;
   }

   public void updateObject(int var1, Object var2) {
      WatchableObject var3 = this.getWatchedObject(var1);
      if (!var2.equals(var3.getObject())) {
         var3.setObject(var2);
         var3.setWatched(true);
         this.objectChanged = true;
      }
   }

   public void setObjectWatched(int var1) {
      WatchableObject.setWatchableObjectWatched(this.getWatchedObject(var1), true);
      this.objectChanged = true;
   }

   public boolean hasChanges() {
      return this.objectChanged;
   }

   public static void writeObjectsInListToStream(List var0, DataOutputStream var1) {
      if (var0 != null) {
         for (WatchableObject var3 : var0) {
            writeWatchableObject(var1, var3);
         }
      }

      var1.writeByte(127);
   }

   public List unwatchAndReturnAllWatched() {
      ArrayList var1 = null;
      if (this.objectChanged) {
         this.lock.readLock().lock();

         for (WatchableObject var3 : this.watchedObjects.values()) {
            if (var3.isWatched()) {
               var3.setWatched(false);
               if (var1 == null) {
                  var1 = new ArrayList();
               }

               var1.add(var3);
            }
         }

         this.lock.readLock().unlock();
      }

      this.objectChanged = false;
      return var1;
   }

   public void writeWatchableObjects(DataOutputStream var1) {
      this.lock.readLock().lock();

      for (WatchableObject var3 : this.watchedObjects.values()) {
         writeWatchableObject(var1, var3);
      }

      this.lock.readLock().unlock();
      var1.writeByte(127);
   }

   public List getAllWatched() {
      ArrayList var1 = null;
      this.lock.readLock().lock();

      for (WatchableObject var3 : this.watchedObjects.values()) {
         if (var1 == null) {
            var1 = new ArrayList();
         }

         var1.add(var3);
      }

      this.lock.readLock().unlock();
      return var1;
   }

   private static void writeWatchableObject(DataOutputStream var0, WatchableObject var1) {
      int var2 = (var1.getObjectType() << 5 | var1.getDataValueId() & 31) & 0xFF;
      var0.writeByte(var2);
      switch (var1.getObjectType()) {
         case 0:
            var0.writeByte((Byte)var1.getObject());
            break;
         case 1:
            var0.writeShort((Short)var1.getObject());
            break;
         case 2:
            var0.writeInt((Integer)var1.getObject());
            break;
         case 3:
            var0.writeFloat((Float)var1.getObject());
            break;
         case 4:
            Packet.writeString((String)var1.getObject(), var0);
            break;
         case 5:
            ItemStack var4 = (ItemStack)var1.getObject();
            Packet.writeItemStack(var4, var0);
            break;
         case 6:
            ChunkCoordinates var3 = (ChunkCoordinates)var1.getObject();
            var0.writeInt(var3.posX);
            var0.writeInt(var3.posY);
            var0.writeInt(var3.posZ);
      }
   }

   public static List readWatchableObjects(DataInputStream var0) {
      ArrayList var1 = null;

      for (byte var2 = var0.readByte(); var2 != 127; var2 = var0.readByte()) {
         if (var1 == null) {
            var1 = new ArrayList();
         }

         int var3 = (var2 & 224) >> 5;
         int var4 = var2 & 31;
         WatchableObject var5 = null;
         switch (var3) {
            case 0:
               var5 = new WatchableObject(var3, var4, var0.readByte());
               break;
            case 1:
               var5 = new WatchableObject(var3, var4, var0.readShort());
               break;
            case 2:
               var5 = new WatchableObject(var3, var4, var0.readInt());
               break;
            case 3:
               var5 = new WatchableObject(var3, var4, var0.readFloat());
               break;
            case 4:
               var5 = new WatchableObject(var3, var4, Packet.readString(var0, 64));
               break;
            case 5:
               var5 = new WatchableObject(var3, var4, Packet.readItemStack(var0));
               break;
            case 6:
               int var6 = var0.readInt();
               int var7 = var0.readInt();
               int var8 = var0.readInt();
               var5 = new WatchableObject(var3, var4, new ChunkCoordinates(var6, var7, var8));
         }

         var1.add(var5);
      }

      return var1;
   }

   public void updateWatchedObjectsFromList(List var1) {
      this.lock.writeLock().lock();

      for (WatchableObject var3 : var1) {
         WatchableObject var4 = (WatchableObject)this.watchedObjects.get(var3.getDataValueId());
         if (var4 != null) {
            var4.setObject(var3.getObject());
         }
      }

      this.lock.writeLock().unlock();
   }

   public boolean getIsBlank() {
      return this.isBlank;
   }

   static {
      dataTypes.put(Byte.class, 0);
      dataTypes.put(Short.class, 1);
      dataTypes.put(Integer.class, 2);
      dataTypes.put(Float.class, 3);
      dataTypes.put(String.class, 4);
      dataTypes.put(ItemStack.class, 5);
      dataTypes.put(ChunkCoordinates.class, 6);
   }
}
