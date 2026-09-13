package argo.saj;

import java.io.PushbackReader;
import java.io.Reader;

final class PositionTrackingPushbackReader implements ThingWithPosition {
   private final PushbackReader pushbackReader;
   private int characterCount = 0;
   private int lineCount = 1;
   private boolean lastCharacterWasCarriageReturn = false;

   public PositionTrackingPushbackReader(Reader var1) {
      this.pushbackReader = new PushbackReader(var1);
   }

   public void unread(char var1) {
      this.characterCount--;
      if (this.characterCount < 0) {
         this.characterCount = 0;
      }

      this.pushbackReader.unread(var1);
   }

   public void uncount(char[] var1) {
      this.characterCount -= var1.length;
      if (this.characterCount < 0) {
         this.characterCount = 0;
      }
   }

   public int read() {
      int var1 = this.pushbackReader.read();
      this.updateCharacterAndLineCounts(var1);
      return var1;
   }

   public int read(char[] var1) {
      int var2 = this.pushbackReader.read(var1);

      for (char var6 : var1) {
         this.updateCharacterAndLineCounts(var6);
      }

      return var2;
   }

   private void updateCharacterAndLineCounts(int var1) {
      if (13 == var1) {
         this.characterCount = 0;
         this.lineCount++;
         this.lastCharacterWasCarriageReturn = true;
      } else {
         if (10 == var1 && !this.lastCharacterWasCarriageReturn) {
            this.characterCount = 0;
            this.lineCount++;
         } else {
            this.characterCount++;
         }

         this.lastCharacterWasCarriageReturn = false;
      }
   }

   @Override
   public int getColumn() {
      return this.characterCount;
   }

   @Override
   public int getRow() {
      return this.lineCount;
   }
}
