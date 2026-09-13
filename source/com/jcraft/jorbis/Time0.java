package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Time0 extends FuncTime {
   @Override
   void pack(Object var1, Buffer var2) {
   }

   @Override
   Object unpack(Info var1, Buffer var2) {
      return "";
   }

   @Override
   Object look(DspState var1, InfoMode var2, Object var3) {
      return "";
   }

   @Override
   void free_info(Object var1) {
   }

   @Override
   void free_look(Object var1) {
   }

   @Override
   int inverse(Block var1, Object var2, float[] var3, float[] var4) {
      return 0;
   }
}
