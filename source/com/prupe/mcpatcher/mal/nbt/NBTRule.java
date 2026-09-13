package com.prupe.mcpatcher.mal.nbt;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.NBTTagFloat;
import net.minecraft.src.NBTTagInt;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagLong;
import net.minecraft.src.NBTTagShort;
import net.minecraft.src.NBTTagString;

@Environment(EnvType.CLIENT)
public abstract class NBTRule {
   public static final String NBT_RULE_PREFIX = "nbt.";
   public static final String NBT_RULE_SEPARATOR = ".";
   public static final String NBT_RULE_WILDCARD = "*";
   public static final String NBT_REGEX_PREFIX = "regex:";
   public static final String NBT_IREGEX_PREFIX = "iregex:";
   public static final String NBT_GLOB_PREFIX = "pattern:";
   public static final String NBT_IGLOB_PREFIX = "ipattern:";
   private final String[] tagName;
   private final Integer[] tagIndex;

   public static NBTRule create(String tag, String value) {
      if (tag != null && value != null && tag.startsWith("nbt.")) {
         try {
            tag = tag.substring("nbt.".length());
            if (value.startsWith("regex:")) {
               return new NBTRule.Regex(tag, value.substring("regex:".length()), true);
            } else if (value.startsWith("iregex:")) {
               return new NBTRule.Regex(tag, value.substring("iregex:".length()), false);
            } else if (value.startsWith("pattern:")) {
               return new NBTRule.Glob(tag, value.substring("pattern:".length()), true);
            } else {
               return (NBTRule)(value.startsWith("ipattern:")
                  ? new NBTRule.Glob(tag, value.substring("ipattern:".length()), false)
                  : new NBTRule.Exact(tag, value));
            }
         } catch (Throwable var3) {
            var3.printStackTrace();
            return null;
         }
      } else {
         return null;
      }
   }

   protected NBTRule(String tag, String value) {
      this.tagName = tag.split(Pattern.quote("."));
      this.tagIndex = new Integer[this.tagName.length];

      for (int i = 0; i < this.tagName.length; i++) {
         if ("*".equals(this.tagName[i])) {
            this.tagName[i] = null;
            this.tagIndex[i] = null;
         } else {
            try {
               this.tagIndex[i] = Integer.valueOf(this.tagName[i]);
            } catch (NumberFormatException var5) {
               this.tagIndex[i] = -1;
            }
         }
      }
   }

   public final boolean match(NBTTagCompound nbt) {
      return nbt != null && this.match(nbt, 0);
   }

   private boolean match(NBTTagCompound nbt, int index) {
      if (index >= this.tagName.length) {
         return false;
      } else if (this.tagName[index] == null) {
         for (Object nbtBase : nbt.getTags()) {
            if (this.match1((NBTBase)nbtBase, index + 1)) {
               return true;
            }
         }

         return false;
      } else {
         NBTBase nbtBasex = nbt.getTag(this.tagName[index]);
         return this.match1(nbtBasex, index + 1);
      }
   }

   private boolean match(NBTTagList nbt, int index) {
      if (index >= this.tagIndex.length) {
         return false;
      } else if (this.tagIndex[index] == null) {
         for (int i = 0; i < nbt.tagCount(); i++) {
            if (this.match1(nbt.tagAt(i), index + 1)) {
               return true;
            }
         }

         return false;
      } else {
         int tagNum = this.tagIndex[index];
         return tagNum >= 0 && tagNum < nbt.tagCount() && this.match1(nbt.tagAt(tagNum), index + 1);
      }
   }

   private boolean match1(NBTBase nbt, int index) {
      if (nbt == null) {
         return false;
      } else if (nbt instanceof NBTTagCompound) {
         return this.match((NBTTagCompound)nbt, index);
      } else if (nbt instanceof NBTTagList) {
         return this.match((NBTTagList)nbt, index);
      } else if (index < this.tagName.length) {
         return false;
      } else if (nbt instanceof NBTTagString) {
         return this.match((NBTTagString)nbt);
      } else if (nbt instanceof NBTTagInt) {
         return this.match((NBTTagInt)nbt);
      } else if (nbt instanceof NBTTagDouble) {
         return this.match((NBTTagDouble)nbt);
      } else if (nbt instanceof NBTTagFloat) {
         return this.match((NBTTagFloat)nbt);
      } else if (nbt instanceof NBTTagLong) {
         return this.match((NBTTagLong)nbt);
      } else if (nbt instanceof NBTTagShort) {
         return this.match((NBTTagShort)nbt);
      } else {
         return nbt instanceof NBTTagByte ? this.match((NBTTagByte)nbt) : false;
      }
   }

   protected boolean match(NBTTagByte nbt) {
      return false;
   }

   protected boolean match(NBTTagDouble nbt) {
      return false;
   }

   protected boolean match(NBTTagFloat nbt) {
      return false;
   }

   protected boolean match(NBTTagInt nbt) {
      return false;
   }

   protected boolean match(NBTTagLong nbt) {
      return false;
   }

   protected boolean match(NBTTagShort nbt) {
      return false;
   }

   protected boolean match(NBTTagString nbt) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   private static final class Exact extends NBTRule {
      private final Byte byteValue;
      private final Double doubleValue;
      private final Float floatValue;
      private final Integer integerValue;
      private final Long longValue;
      private final Short shortValue;
      private final String stringValue;

      Exact(String tag, String value) {
         super(tag, value);
         this.stringValue = value;
         this.doubleValue = parse(Double.class, value);
         if (this.doubleValue == null) {
            this.floatValue = null;
         } else {
            this.floatValue = this.doubleValue.floatValue();
         }

         this.longValue = parse(Long.class, value);
         if (this.longValue == null) {
            this.byteValue = null;
            this.integerValue = null;
            this.shortValue = null;
         } else {
            this.byteValue = this.longValue.byteValue();
            this.integerValue = this.longValue.intValue();
            this.shortValue = this.longValue.shortValue();
         }
      }

      private static <T extends Number> T parse(Class<T> cl, String value) {
         try {
            Method valueOf = cl.getDeclaredMethod("valueOf", String.class);
            Object result = valueOf.invoke(null, value);
            if (result != null && cl.isAssignableFrom(result.getClass())) {
               return cl.cast(result);
            }
         } catch (Throwable var4) {
         }

         return null;
      }

      @Override
      protected boolean match(NBTTagByte nbt) {
         return this.byteValue != null && this.byteValue == nbt.data;
      }

      @Override
      protected boolean match(NBTTagDouble nbt) {
         return this.doubleValue != null && this.doubleValue == nbt.data;
      }

      @Override
      protected boolean match(NBTTagFloat nbt) {
         return this.floatValue != null && this.floatValue == nbt.data;
      }

      @Override
      protected boolean match(NBTTagInt nbt) {
         return this.integerValue != null && this.integerValue == nbt.data;
      }

      @Override
      protected boolean match(NBTTagLong nbt) {
         return this.longValue != null && this.longValue == nbt.data;
      }

      @Override
      protected boolean match(NBTTagShort nbt) {
         return this.shortValue != null && this.shortValue == nbt.data;
      }

      @Override
      protected boolean match(NBTTagString nbt) {
         return nbt.data.equals(this.stringValue);
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class Glob extends NBTRule {
      private static final char STAR = '*';
      private static final char SINGLE = '?';
      private static final char ESCAPE = '\\';
      private final String glob;
      private final boolean caseSensitive;

      protected Glob(String tag, String value, boolean caseSensitive) {
         super(tag, value);
         this.caseSensitive = caseSensitive;
         if (!caseSensitive) {
            value = value.toLowerCase();
         }

         this.glob = value;
      }

      @Override
      protected boolean match(NBTTagString nbt) {
         String value = nbt.data;
         return this.matchPartial(value, 0, value.length(), 0, this.glob.length());
      }

      private boolean matchPartial(String value, int curV, int maxV, int curG, int maxG) {
         while (curG < maxG) {
            char g = this.glob.charAt(curG);
            if (g == '*') {
               while (!this.matchPartial(value, curV, maxV, curG + 1, maxG)) {
                  if (curV >= maxV) {
                     return false;
                  }

                  curV++;
               }

               return true;
            }

            if (curV >= maxV) {
               break;
            }

            if (g != '?') {
               if (g == '\\' && curG + 1 < maxG) {
                  g = this.glob.charAt(++curG);
               }

               if (!this.matchChar(g, value.charAt(curV))) {
                  return false;
               }
            }

            curG++;
            curV++;
         }

         return curG == maxG && curV == maxV;
      }

      private boolean matchChar(char a, char b) {
         return a == (this.caseSensitive ? b : Character.toLowerCase(b));
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class Regex extends NBTRule {
      private final Pattern pattern;

      Regex(String tag, String value, boolean caseSensitive) {
         super(tag, value);
         this.pattern = Pattern.compile(value, caseSensitive ? 0 : 2);
      }

      @Override
      protected boolean match(NBTTagString nbt) {
         return this.pattern.matcher(nbt.data).matches();
      }
   }
}
