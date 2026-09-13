package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.FileInputStream;

class DecodeExample {
   static int convsize = 8192;
   static byte[] convbuffer = new byte[convsize];

   public static void main(String[] var0) {
      Object var1 = System.in;
      if (var0.length > 0) {
         try {
            var1 = new FileInputStream(var0[0]);
         } catch (Exception var28) {
            System.err.println(var28);
         }
      }

      SyncState var2 = new SyncState();
      StreamState var3 = new StreamState();
      Page var4 = new Page();
      Packet var5 = new Packet();
      Info var6 = new Info();
      Comment var7 = new Comment();
      DspState var8 = new DspState();
      Block var9 = new Block(var8);
      int var11 = 0;
      var2.init();

      while (true) {
         boolean var12 = false;
         int var13 = var2.buffer(4096);
         byte[] var10 = var2.data;

         try {
            var11 = var1.read(var10, var13, 4096);
         } catch (Exception var26) {
            System.err.println(var26);
            System.exit(-1);
         }

         var2.wrote(var11);
         if (var2.pageout(var4) != 1) {
            if (var11 < 4096) {
               var2.clear();
               System.err.println("Done.");
               return;
            }

            System.err.println("Input does not appear to be an Ogg bitstream.");
            System.exit(1);
         }

         var3.init(var4.serialno());
         var6.init();
         var7.init();
         if (var3.pagein(var4) < 0) {
            System.err.println("Error reading first page of Ogg bitstream data.");
            System.exit(1);
         }

         if (var3.packetout(var5) != 1) {
            System.err.println("Error reading initial header packet.");
            System.exit(1);
         }

         if (var6.synthesis_headerin(var7, var5) < 0) {
            System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
            System.exit(1);
         }

         for (int var14 = 0; var14 < 2; var2.wrote(var11)) {
            while (var14 < 2) {
               int var15 = var2.pageout(var4);
               if (var15 == 0) {
                  break;
               }

               if (var15 == 1) {
                  var3.pagein(var4);

                  while (var14 < 2) {
                     var15 = var3.packetout(var5);
                     if (var15 == 0) {
                        break;
                     }

                     if (var15 == -1) {
                        System.err.println("Corrupt secondary header.  Exiting.");
                        System.exit(1);
                     }

                     var6.synthesis_headerin(var7, var5);
                     var14++;
                  }
               }
            }

            var13 = var2.buffer(4096);
            var10 = var2.data;

            try {
               var11 = var1.read(var10, var13, 4096);
            } catch (Exception var25) {
               System.err.println(var25);
               System.exit(1);
            }

            if (var11 == 0 && var14 < 2) {
               System.err.println("End of file before finding all Vorbis headers!");
               System.exit(1);
            }
         }

         byte[][] var35 = var7.user_comments;

         for (int var16 = 0; var16 < var35.length && var35[var16] != null; var16++) {
            System.err.println(new String(var35[var16], 0, var35[var16].length - 1));
         }

         System.err.println("\nBitstream is " + var6.channels + " channel, " + var6.rate + "Hz");
         System.err.println("Encoded by: " + new String(var7.vendor, 0, var7.vendor.length - 1) + "\n");
         convsize = 4096 / var6.channels;
         var8.synthesis_init(var6);
         var9.init(var8);
         float[][][] var36 = new float[1][][];
         int[] var37 = new int[var6.channels];

         while (!var12) {
            while (!var12) {
               int var17 = var2.pageout(var4);
               if (var17 == 0) {
                  break;
               }

               if (var17 == -1) {
                  System.err.println("Corrupt or missing data in bitstream; continuing...");
               } else {
                  var3.pagein(var4);

                  while (true) {
                     var17 = var3.packetout(var5);
                     if (var17 == 0) {
                        if (var4.eos() != 0) {
                           var12 = true;
                        }
                        break;
                     }

                     if (var17 != -1) {
                        if (var9.synthesis(var5) == 0) {
                           var8.synthesis_blockin(var9);
                        }

                        int var18;
                        while ((var18 = var8.synthesis_pcmout(var36, var37)) > 0) {
                           float[][] var19 = var36[0];
                           int var20 = var18 < convsize ? var18 : convsize;

                           for (int var33 = 0; var33 < var6.channels; var33++) {
                              int var21 = var33 * 2;
                              int var22 = var37[var33];

                              for (int var23 = 0; var23 < var20; var23++) {
                                 int var24 = (int)(var19[var33][var22 + var23] * 32767.0);
                                 if (var24 > 32767) {
                                    var24 = 32767;
                                 }

                                 if (var24 < -32768) {
                                    var24 = -32768;
                                 }

                                 if (var24 < 0) {
                                    var24 |= 32768;
                                 }

                                 convbuffer[var21] = (byte)var24;
                                 convbuffer[var21 + 1] = (byte)(var24 >>> 8);
                                 var21 += 2 * var6.channels;
                              }
                           }

                           System.out.write(convbuffer, 0, 2 * var6.channels * var20);
                           var8.synthesis_read(var20);
                        }
                     }
                  }
               }
            }

            if (!var12) {
               var13 = var2.buffer(4096);
               var10 = var2.data;

               try {
                  var11 = var1.read(var10, var13, 4096);
               } catch (Exception var27) {
                  System.err.println(var27);
                  System.exit(1);
               }

               var2.wrote(var11);
               if (var11 == 0) {
                  var12 = true;
               }
            }
         }

         var3.clear();
         var9.clear();
         var8.clear();
         var6.clear();
      }
   }
}
