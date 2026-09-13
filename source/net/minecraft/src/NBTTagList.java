package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTBase {
   private List tagList = new ArrayList();
   private byte tagType;

   public NBTTagList() {
      super("");
   }

   public NBTTagList(String var1) {
      super(var1);
   }

   @Override
   void write(DataOutput var1) {
      if (!this.tagList.isEmpty()) {
         this.tagType = ((NBTBase)this.tagList.get(0)).getId();
      } else {
         this.tagType = 1;
      }

      var1.writeByte(this.tagType);
      var1.writeInt(this.tagList.size());

      for (int var2 = 0; var2 < this.tagList.size(); var2++) {
         ((NBTBase)this.tagList.get(var2)).write(var1);
      }
   }

   @Override
   void load(DataInput var1) {
      this.tagType = var1.readByte();
      int var2 = var1.readInt();
      this.tagList = new ArrayList();

      for (int var3 = 0; var3 < var2; var3++) {
         NBTBase var4 = NBTBase.newTag(this.tagType, null);
         var4.load(var1);
         this.tagList.add(var4);
      }
   }

   @Override
   public byte getId() {
      return 9;
   }

   @Override
   public String toString() {
      return "" + this.tagList.size() + " entries of type " + NBTBase.getTagName(this.tagType);
   }

   public void appendTag(NBTBase var1) {
      this.tagType = var1.getId();
      this.tagList.add(var1);
   }

   public NBTBase removeTag(int var1) {
      return (NBTBase)this.tagList.remove(var1);
   }

   public NBTBase tagAt(int var1) {
      return (NBTBase)this.tagList.get(var1);
   }

   public int tagCount() {
      return this.tagList.size();
   }

   @Override
   public NBTBase copy() {
      NBTTagList var1 = new NBTTagList(this.e());
      var1.tagType = this.tagType;

      for (NBTBase var3 : this.tagList) {
         NBTBase var4 = var3.copy();
         var1.tagList.add(var4);
      }

      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagList var2 = (NBTTagList)var1;
         if (this.tagType == var2.tagType) {
            return this.tagList.equals(var2.tagList);
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.tagList.hashCode();
   }
}
