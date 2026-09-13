package net.minecraft.src;

import java.util.HashMap;
import java.util.TimerTask;

class PlayerUsageSnooperThread extends TimerTask {
   PlayerUsageSnooperThread(PlayerUsageSnooper var1) {
      this.snooper = var1;
   }

   @Override
   public void run() {
      if (PlayerUsageSnooper.getStatsCollectorFor(this.snooper).isSnooperEnabled()) {
         HashMap var1;
         synchronized (PlayerUsageSnooper.getSyncLockFor(this.snooper)) {
            var1 = new HashMap(PlayerUsageSnooper.getDataMapFor(this.snooper));
            var1.put("snooper_count", PlayerUsageSnooper.getSelfCounterFor(this.snooper));
         }

         HttpUtil.sendPost(PlayerUsageSnooper.getStatsCollectorFor(this.snooper).getLogAgent(), PlayerUsageSnooper.getServerUrlFor(this.snooper), var1, true);
      }
   }
}
