package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NBTTagCompound extends NBTBase {
   private Map tagMap = new HashMap();

   public NBTTagCompound() {
      super("");
   }

   public NBTTagCompound(String var1) {
      super(var1);
   }

   @Override
   void write(DataOutput var1) {
      for (NBTBase var3 : this.tagMap.values()) {
         NBTBase.writeNamedTag(var3, var1);
      }

      var1.writeByte(0);
   }

   @Override
   void load(DataInput var1) {
      this.tagMap.clear();

      NBTBase var2;
      while ((var2 = NBTBase.readNamedTag(var1)).getId() != 0) {
         this.tagMap.put(var2.getName(), var2);
      }
   }

   public Collection getTags() {
      return this.tagMap.values();
   }

   @Override
   public byte getId() {
      return 10;
   }

   public void setTag(String var1, NBTBase var2) {
      this.tagMap.put(var1, var2.setName(var1));
   }

   public void setByte(String var1, byte var2) {
      this.tagMap.put(var1, new NBTTagByte(var1, var2));
   }

   public void setShort(String var1, short var2) {
      this.tagMap.put(var1, new NBTTagShort(var1, var2));
   }

   public void setInteger(String var1, int var2) {
      this.tagMap.put(var1, new NBTTagInt(var1, var2));
   }

   public void setLong(String var1, long var2) {
      this.tagMap.put(var1, new NBTTagLong(var1, var2));
   }

   public void setFloat(String var1, float var2) {
      this.tagMap.put(var1, new NBTTagFloat(var1, var2));
   }

   public void setDouble(String var1, double var2) {
      this.tagMap.put(var1, new NBTTagDouble(var1, var2));
   }

   public void setString(String var1, String var2) {
      this.tagMap.put(var1, new NBTTagString(var1, var2));
   }

   public void setByteArray(String var1, byte[] var2) {
      this.tagMap.put(var1, new NBTTagByteArray(var1, var2));
   }

   public void setIntArray(String var1, int[] var2) {
      this.tagMap.put(var1, new NBTTagIntArray(var1, var2));
   }

   public void setCompoundTag(String var1, NBTTagCompound var2) {
      this.tagMap.put(var1, var2.p(var1));
   }

   public void setBoolean(String var1, boolean var2) {
      this.setByte(var1, (byte)(var2 ? 1 : 0));
   }

   public NBTBase getTag(String var1) {
      return (NBTBase)this.tagMap.get(var1);
   }

   public boolean hasKey(String var1) {
      return this.tagMap.containsKey(var1);
   }

   public byte getByte(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? 0 : ((NBTTagByte)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 1, var3));
      }
   }

   public short getShort(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? 0 : ((NBTTagShort)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 2, var3));
      }
   }

   public int getInteger(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? 0 : ((NBTTagInt)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 3, var3));
      }
   }

   public long getLong(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? 0L : ((NBTTagLong)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 4, var3));
      }
   }

   public float getFloat(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? 0.0F : ((NBTTagFloat)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 5, var3));
      }
   }

   public double getDouble(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? 0.0 : ((NBTTagDouble)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 6, var3));
      }
   }

   public String getString(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? "" : ((NBTTagString)this.tagMap.get(var1)).data;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 8, var3));
      }
   }

   public byte[] getByteArray(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(var1)).byteArray;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 7, var3));
      }
   }

   public int[] getIntArray(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? new int[0] : ((NBTTagIntArray)this.tagMap.get(var1)).intArray;
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 11, var3));
      }
   }

   public NBTTagCompound getCompoundTag(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? new NBTTagCompound(var1) : (NBTTagCompound)this.tagMap.get(var1);
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 10, var3));
      }
   }

   public NBTTagList getTagList(String var1) {
      try {
         return !this.tagMap.containsKey(var1) ? new NBTTagList(var1) : (NBTTagList)this.tagMap.get(var1);
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 9, var3));
      }
   }

   public boolean getBoolean(String var1) {
      return this.getByte(var1) != 0;
   }

   public void removeTag(String var1) {
      this.tagMap.remove(var1);
   }

   @Override
   public String toString() {
      String var1 = this.e() + ":[";

      for (String var3 : this.tagMap.keySet()) {
         var1 = var1 + var3 + ":" + this.tagMap.get(var3) + ",";
      }

      return var1 + "]";
   }

   public boolean hasNoTags() {
      return this.tagMap.isEmpty();
   }

   private CrashReport createCrashReport(String var1, int var2, ClassCastException var3) {
      CrashReport var4 = CrashReport.makeCrashReport(var3, "Reading NBT data");
      CrashReportCategory var5 = var4.makeCategoryDepth("Corrupt NBT tag", 1);
      var5.addCrashSectionCallable("Tag type found", new CallableTagCompound1(this, var1));
      var5.addCrashSectionCallable("Tag type expected", new CallableTagCompound2(this, var2));
      var5.addCrashSection("Tag name", var1);
      if (this.e() != null && this.e().length() > 0) {
         var5.addCrashSection("Tag parent", this.e());
      }

      return var4;
   }

   @Override
   public NBTBase copy() {
      NBTTagCompound var1 = new NBTTagCompound(this.e());

      for (String var3 : this.tagMap.keySet()) {
         var1.setTag(var3, ((NBTBase)this.tagMap.get(var3)).copy());
      }

      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagCompound var2 = (NBTTagCompound)var1;
         return this.tagMap.entrySet().equals(var2.tagMap.entrySet());
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.tagMap.hashCode();
   }
}
