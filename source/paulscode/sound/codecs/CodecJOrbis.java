package paulscode.sound.codecs;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

public class CodecJOrbis implements ICodec {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   protected URL url;
   protected URLConnection urlConnection = null;
   private InputStream inputStream;
   private AudioFormat audioFormat;
   private boolean endOfStream = false;
   private boolean initialized = false;
   private byte[] buffer = null;
   private int bufferSize;
   private int count = 0;
   private int index = 0;
   private int convertedBufferSize;
   private byte[] convertedBuffer = null;
   private float[][][] pcmInfo;
   private int[] pcmIndex;
   private Packet joggPacket = new Packet();
   private Page joggPage = new Page();
   private StreamState joggStreamState = new StreamState();
   private SyncState joggSyncState = new SyncState();
   private DspState jorbisDspState = new DspState();
   private Block jorbisBlock = new Block(this.jorbisDspState);
   private Comment jorbisComment = new Comment();
   private Info jorbisInfo = new Info();
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();

   @Override
   public void reverseByteOrder(boolean var1) {
   }

   @Override
   public boolean initialize(URL var1) {
      this.initialized(true, false);
      if (this.joggStreamState != null) {
         this.joggStreamState.clear();
      }

      if (this.jorbisBlock != null) {
         this.jorbisBlock.clear();
      }

      if (this.jorbisDspState != null) {
         this.jorbisDspState.clear();
      }

      if (this.jorbisInfo != null) {
         this.jorbisInfo.clear();
      }

      if (this.joggSyncState != null) {
         this.joggSyncState.clear();
      }

      if (this.inputStream != null) {
         try {
            this.inputStream.close();
         } catch (IOException var7) {
         }
      }

      this.url = var1;
      this.bufferSize = 8192;
      this.buffer = null;
      this.count = 0;
      this.index = 0;
      this.joggStreamState = new StreamState();
      this.jorbisBlock = new Block(this.jorbisDspState);
      this.jorbisDspState = new DspState();
      this.jorbisInfo = new Info();
      this.joggSyncState = new SyncState();

      try {
         this.urlConnection = var1.openConnection();
      } catch (UnknownServiceException var5) {
         this.errorMessage("Unable to create a UrlConnection in method 'initialize'.");
         this.printStackTrace(var5);
         this.cleanup();
         return false;
      } catch (IOException var6) {
         this.errorMessage("Unable to create a UrlConnection in method 'initialize'.");
         this.printStackTrace(var6);
         this.cleanup();
         return false;
      }

      if (this.urlConnection != null) {
         try {
            this.inputStream = this.openInputStream();
         } catch (IOException var4) {
            this.errorMessage("Unable to acquire inputstream in method 'initialize'.");
            this.printStackTrace(var4);
            this.cleanup();
            return false;
         }
      }

      this.endOfStream(true, false);
      this.joggSyncState.init();
      this.joggSyncState.buffer(this.bufferSize);
      this.buffer = this.joggSyncState.data;

      try {
         if (!this.readHeader()) {
            this.errorMessage("Error reading the header");
            return false;
         }
      } catch (IOException var8) {
         this.errorMessage("Error reading the header");
         return false;
      }

      this.convertedBufferSize = this.bufferSize * 2;
      this.jorbisDspState.synthesis_init(this.jorbisInfo);
      this.jorbisBlock.init(this.jorbisDspState);
      int var2 = this.jorbisInfo.channels;
      int var3 = this.jorbisInfo.rate;
      this.audioFormat = new AudioFormat(var3, 16, var2, true, false);
      this.pcmInfo = new float[1][][];
      this.pcmIndex = new int[this.jorbisInfo.channels];
      this.initialized(true, true);
      return true;
   }

   protected InputStream openInputStream() {
      return this.urlConnection.getInputStream();
   }

   @Override
   public boolean initialized() {
      return this.initialized(false, false);
   }

   @Override
   public SoundBuffer read() {
      byte[] var1 = null;

      while (!this.endOfStream(false, false) && (var1 == null || var1.length < SoundSystemConfig.getStreamingBufferSize())) {
         if (var1 == null) {
            var1 = this.readBytes();
         } else {
            var1 = appendByteArrays(var1, this.readBytes());
         }
      }

      return var1 == null ? null : new SoundBuffer(var1, this.audioFormat);
   }

   @Override
   public SoundBuffer readAll() {
      byte[] var1 = null;

      while (!this.endOfStream(false, false)) {
         if (var1 == null) {
            var1 = this.readBytes();
         } else {
            var1 = appendByteArrays(var1, this.readBytes());
         }
      }

      return var1 == null ? null : new SoundBuffer(var1, this.audioFormat);
   }

   @Override
   public boolean endOfStream() {
      return this.endOfStream(false, false);
   }

   @Override
   public void cleanup() {
      this.joggStreamState.clear();
      this.jorbisBlock.clear();
      this.jorbisDspState.clear();
      this.jorbisInfo.clear();
      this.joggSyncState.clear();
      if (this.inputStream != null) {
         try {
            this.inputStream.close();
         } catch (IOException var2) {
         }
      }

      this.joggStreamState = null;
      this.jorbisBlock = null;
      this.jorbisDspState = null;
      this.jorbisInfo = null;
      this.joggSyncState = null;
      this.inputStream = null;
   }

   @Override
   public AudioFormat getAudioFormat() {
      return this.audioFormat;
   }

   private boolean readHeader() {
      this.index = this.joggSyncState.buffer(this.bufferSize);
      int var1 = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
      if (var1 < 0) {
         var1 = 0;
      }

      this.joggSyncState.wrote(var1);
      if (this.joggSyncState.pageout(this.joggPage) != 1) {
         if (var1 < this.bufferSize) {
            return true;
         } else {
            this.errorMessage("Ogg header not recognized in method 'readHeader'.");
            return false;
         }
      } else {
         this.joggStreamState.init(this.joggPage.serialno());
         this.jorbisInfo.init();
         this.jorbisComment.init();
         if (this.joggStreamState.pagein(this.joggPage) < 0) {
            this.errorMessage("Problem with first Ogg header page in method 'readHeader'.");
            return false;
         } else if (this.joggStreamState.packetout(this.joggPacket) != 1) {
            this.errorMessage("Problem with first Ogg header packet in method 'readHeader'.");
            return false;
         } else if (this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket) < 0) {
            this.errorMessage("File does not contain Vorbis header in method 'readHeader'.");
            return false;
         } else {
            int var2 = 0;

            while (var2 < 2) {
               while (var2 < 2) {
                  int var3 = this.joggSyncState.pageout(this.joggPage);
                  if (var3 == 0) {
                     break;
                  }

                  if (var3 == 1) {
                     this.joggStreamState.pagein(this.joggPage);

                     while (var2 < 2) {
                        var3 = this.joggStreamState.packetout(this.joggPacket);
                        if (var3 == 0) {
                           break;
                        }

                        if (var3 == -1) {
                           this.errorMessage("Secondary Ogg header corrupt in method 'readHeader'.");
                           return false;
                        }

                        this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket);
                        var2++;
                     }
                  }
               }

               this.index = this.joggSyncState.buffer(this.bufferSize);
               var1 = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
               if (var1 < 0) {
                  var1 = 0;
               }

               if (var1 == 0 && var2 < 2) {
                  this.errorMessage("End of file reached before finished readingOgg header in method 'readHeader'");
                  return false;
               }

               this.joggSyncState.wrote(var1);
            }

            this.index = this.joggSyncState.buffer(this.bufferSize);
            this.buffer = this.joggSyncState.data;
            return true;
         }
      }
   }

   private byte[] readBytes() {
      if (!this.initialized(false, false)) {
         return null;
      } else if (this.endOfStream(false, false)) {
         return null;
      } else {
         if (this.convertedBuffer == null) {
            this.convertedBuffer = new byte[this.convertedBufferSize];
         }

         byte[] var1 = null;
         switch (this.joggSyncState.pageout(this.joggPage)) {
            default:
               this.joggStreamState.pagein(this.joggPage);
               if (this.joggPage.granulepos() == 0L) {
                  this.endOfStream(true, true);
                  return null;
               } else {
                  label96:
                  while (true) {
                     switch (this.joggStreamState.packetout(this.joggPacket)) {
                        case -1:
                           continue;
                        case 0:
                           if (this.joggPage.eos() != 0) {
                              this.endOfStream(true, true);
                           }
                           break label96;
                     }

                     if (this.jorbisBlock.synthesis(this.joggPacket) == 0) {
                        this.jorbisDspState.synthesis_blockin(this.jorbisBlock);
                     }

                     int var3;
                     while ((var3 = this.jorbisDspState.synthesis_pcmout(this.pcmInfo, this.pcmIndex)) > 0) {
                        float[][] var2 = this.pcmInfo[0];
                        int var4 = var3 < this.convertedBufferSize ? var3 : this.convertedBufferSize;

                        for (int var8 = 0; var8 < this.jorbisInfo.channels; var8++) {
                           int var5 = var8 * 2;
                           int var6 = this.pcmIndex[var8];

                           for (int var9 = 0; var9 < var4; var9++) {
                              int var7 = (int)(var2[var8][var6 + var9] * 32767.0);
                              if (var7 > 32767) {
                                 var7 = 32767;
                              }

                              if (var7 < -32768) {
                                 var7 = -32768;
                              }

                              if (var7 < 0) {
                                 var7 |= 32768;
                              }

                              this.convertedBuffer[var5] = (byte)var7;
                              this.convertedBuffer[var5 + 1] = (byte)(var7 >>> 8);
                              var5 += 2 * this.jorbisInfo.channels;
                           }
                        }

                        this.jorbisDspState.synthesis_read(var4);
                        var1 = appendByteArrays(var1, this.convertedBuffer, 2 * this.jorbisInfo.channels * var4);
                     }
                  }
               }
            case -1:
            case 0:
               if (!this.endOfStream(false, false)) {
                  this.index = this.joggSyncState.buffer(this.bufferSize);
                  this.buffer = this.joggSyncState.data;

                  try {
                     this.count = this.inputStream.read(this.buffer, this.index, this.bufferSize);
                  } catch (Exception var11) {
                     this.printStackTrace(var11);
                     return null;
                  }

                  if (this.count == -1) {
                     return var1;
                  }

                  this.joggSyncState.wrote(this.count);
                  if (this.count == 0) {
                     this.endOfStream(true, true);
                  }
               }

               return var1;
         }
      }
   }

   private synchronized boolean initialized(boolean var1, boolean var2) {
      if (var1) {
         this.initialized = var2;
      }

      return this.initialized;
   }

   private synchronized boolean endOfStream(boolean var1, boolean var2) {
      if (var1) {
         this.endOfStream = var2;
      }

      return this.endOfStream;
   }

   private static byte[] trimArray(byte[] var0, int var1) {
      byte[] var2 = null;
      if (var0 != null && var0.length > var1) {
         var2 = new byte[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
      }

      return var2;
   }

   private static byte[] appendByteArrays(byte[] var0, byte[] var1, int var2) {
      int var4 = var2;
      if (var1 != null && var1.length != 0) {
         if (var1.length < var2) {
            var4 = var1.length;
         }
      } else {
         var4 = 0;
      }

      if (var0 != null || var1 != null && var4 > 0) {
         byte[] var3;
         if (var0 == null) {
            var3 = new byte[var4];
            System.arraycopy(var1, 0, var3, 0, var4);
            Object var7 = null;
         } else if (var1 != null && var4 > 0) {
            var3 = new byte[var0.length + var4];
            System.arraycopy(var0, 0, var3, 0, var0.length);
            System.arraycopy(var1, 0, var3, var0.length, var4);
            Object var6 = null;
            Object var8 = null;
         } else {
            var3 = new byte[var0.length];
            System.arraycopy(var0, 0, var3, 0, var0.length);
            Object var5 = null;
         }

         return var3;
      } else {
         return null;
      }
   }

   private static byte[] appendByteArrays(byte[] var0, byte[] var1) {
      if (var0 == null && var1 == null) {
         return null;
      } else {
         byte[] var2;
         if (var0 == null) {
            var2 = new byte[var1.length];
            System.arraycopy(var1, 0, var2, 0, var1.length);
            Object var5 = null;
         } else if (var1 == null) {
            var2 = new byte[var0.length];
            System.arraycopy(var0, 0, var2, 0, var0.length);
            Object var3 = null;
         } else {
            var2 = new byte[var0.length + var1.length];
            System.arraycopy(var0, 0, var2, 0, var0.length);
            System.arraycopy(var1, 0, var2, var0.length, var1.length);
            Object var4 = null;
            Object var6 = null;
         }

         return var2;
      }
   }

   private void errorMessage(String var1) {
      this.logger.errorMessage("CodecJOrbis", var1, 0);
   }

   private void printStackTrace(Exception var1) {
      this.logger.printStackTrace(var1, 1);
   }
}
