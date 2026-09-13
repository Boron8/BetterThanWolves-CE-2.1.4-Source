package net.minecraft.src;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LowerStringMap implements Map {
   private final Map internalMap = new LinkedHashMap();

   @Override
   public int size() {
      return this.internalMap.size();
   }

   @Override
   public boolean isEmpty() {
      return this.internalMap.isEmpty();
   }

   @Override
   public boolean containsKey(Object var1) {
      return this.internalMap.containsKey(var1.toString().toLowerCase());
   }

   @Override
   public boolean containsValue(Object var1) {
      return this.internalMap.containsKey(var1);
   }

   @Override
   public Object get(Object var1) {
      return this.internalMap.get(var1.toString().toLowerCase());
   }

   public Object putLower(String var1, Object var2) {
      return this.internalMap.put(var1.toLowerCase(), var2);
   }

   @Override
   public Object remove(Object var1) {
      return this.internalMap.remove(var1.toString().toLowerCase());
   }

   @Override
   public void putAll(Map var1) {
      for (Entry var3 : var1.entrySet()) {
         this.putLower((String)var3.getKey(), var3.getValue());
      }
   }

   @Override
   public void clear() {
      this.internalMap.clear();
   }

   @Override
   public Set keySet() {
      return this.internalMap.keySet();
   }

   @Override
   public Collection values() {
      return this.internalMap.values();
   }

   @Override
   public Set entrySet() {
      return this.internalMap.entrySet();
   }
}
