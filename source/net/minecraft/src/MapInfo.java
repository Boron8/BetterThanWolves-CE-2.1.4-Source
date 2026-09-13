package net.minecraft.src;

public class MapInfo {
   public final EntityPlayer entityplayerObj;
   public int[] field_76209_b;
   public int[] field_76210_c;
   private int currentRandomNumber;
   private int ticksUntilPlayerLocationMapUpdate;
   private byte[] lastPlayerLocationOnMap;
   public int field_82569_d;
   private boolean field_82570_i;

   public MapInfo(MapData var1, EntityPlayer var2) {
      this.mapDataObj = var1;
      this.field_76209_b = new int[128];
      this.field_76210_c = new int[128];
      this.currentRandomNumber = 0;
      this.ticksUntilPlayerLocationMapUpdate = 0;
      this.field_82570_i = false;
      this.entityplayerObj = var2;

      for (int var3 = 0; var3 < this.field_76209_b.length; var3++) {
         this.field_76209_b[var3] = 0;
         this.field_76210_c[var3] = 127;
      }
   }

   public byte[] getPlayersOnMap(ItemStack var1) {
      if (!this.field_82570_i) {
         byte[] var9 = new byte[]{2, this.mapDataObj.scale};
         this.field_82570_i = true;
         return var9;
      } else {
         if (--this.ticksUntilPlayerLocationMapUpdate < 0) {
            this.ticksUntilPlayerLocationMapUpdate = 4;
            byte[] var2 = new byte[this.mapDataObj.playersVisibleOnMap.size() * 3 + 1];
            var2[0] = 1;
            int var3 = 0;

            for (MapCoord var5 : this.mapDataObj.playersVisibleOnMap.values()) {
               var2[var3 * 3 + 1] = (byte)(var5.iconSize << 4 | var5.iconRotation & 15);
               var2[var3 * 3 + 2] = var5.centerX;
               var2[var3 * 3 + 3] = var5.centerZ;
               var3++;
            }

            boolean var11 = !var1.isOnItemFrame();
            if (this.lastPlayerLocationOnMap != null && this.lastPlayerLocationOnMap.length == var2.length) {
               for (int var13 = 0; var13 < var2.length; var13++) {
                  if (var2[var13] != this.lastPlayerLocationOnMap[var13]) {
                     var11 = false;
                     break;
                  }
               }
            } else {
               var11 = false;
            }

            if (!var11) {
               this.lastPlayerLocationOnMap = var2;
               return var2;
            }
         }

         for (int var8 = 0; var8 < 1; var8++) {
            int var10 = this.currentRandomNumber++ * 11 % 128;
            if (this.field_76209_b[var10] >= 0) {
               int var12 = this.field_76210_c[var10] - this.field_76209_b[var10] + 1;
               int var14 = this.field_76209_b[var10];
               byte[] var6 = new byte[var12 + 3];
               var6[0] = 0;
               var6[1] = (byte)var10;
               var6[2] = (byte)var14;

               for (int var7 = 0; var7 < var6.length - 3; var7++) {
                  var6[var7 + 3] = this.mapDataObj.colors[(var7 + var14) * 128 + var10];
               }

               this.field_76210_c[var10] = -1;
               this.field_76209_b[var10] = -1;
               return var6;
            }
         }

         return null;
      }
   }
}
