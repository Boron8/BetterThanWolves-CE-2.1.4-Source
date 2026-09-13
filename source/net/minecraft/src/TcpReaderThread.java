package net.minecraft.src;

class TcpReaderThread extends Thread {
   TcpReaderThread(TcpConnection var1, String var2) {
      super(var2);
      this.theTcpConnection = var1;
   }

   @Override
   public void run() {
      TcpConnection.field_74471_a.getAndIncrement();

      try {
         while (TcpConnection.isRunning(this.theTcpConnection) && !TcpConnection.isServerTerminating(this.theTcpConnection)) {
            while (TcpConnection.readNetworkPacket(this.theTcpConnection)) {
            }

            try {
               sleep(2L);
            } catch (InterruptedException var5) {
            }
         }
      } finally {
         TcpConnection.field_74471_a.getAndDecrement();
      }
   }
}
