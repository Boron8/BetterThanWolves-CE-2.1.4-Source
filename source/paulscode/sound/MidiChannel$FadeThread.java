package paulscode.sound;

class MidiChannel$FadeThread extends SimpleThread {
   private MidiChannel$FadeThread(MidiChannel var1) {
      this.this$0 = var1;
   }

   @Override
   public void run() {
      while (!this.dying()) {
         if (this.this$0.fadeOutGain == -1.0F && this.this$0.fadeInGain == 1.0F) {
            this.snooze(3600000L);
         }

         MidiChannel.access$100(this.this$0);
         this.snooze(50L);
      }

      this.cleanup();
   }
}
