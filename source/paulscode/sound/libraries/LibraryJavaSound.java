package paulscode.sound.libraries;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.ICodec;
import paulscode.sound.Library;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.Source;

public class LibraryJavaSound extends Library {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final int XXX = 0;
   private final int maxClipSize = 1048576;
   private static Mixer myMixer = null;
   private static LibraryJavaSound$MixerRanking myMixerRanking = null;
   private static LibraryJavaSound instance = null;
   private static int minSampleRate = 4000;
   private static int maxSampleRate = 48000;
   private static int lineCount = 32;
   private static boolean useGainControl = true;
   private static boolean usePanControl = true;
   private static boolean useSampleRateControl = true;

   public LibraryJavaSound() {
      instance = this;
   }

   @Override
   public void init() {
      LibraryJavaSound$MixerRanking var1 = null;
      if (myMixer == null) {
         for (Info var5 : AudioSystem.getMixerInfo()) {
            if (var5.getName().equals("Java Sound Audio Engine")) {
               var1 = new LibraryJavaSound$MixerRanking();

               try {
                  var1.rank(var5);
               } catch (LibraryJavaSound$Exception var10) {
                  break;
               }

               if (var1.rank >= 14) {
                  myMixer = AudioSystem.getMixer(var5);
                  mixerRanking(true, var1);
               }
               break;
            }
         }

         if (myMixer == null) {
            LibraryJavaSound$MixerRanking var12 = var1;

            for (Info var6 : AudioSystem.getMixerInfo()) {
               var1 = new LibraryJavaSound$MixerRanking();

               try {
                  var1.rank(var6);
               } catch (LibraryJavaSound$Exception var9) {
               }

               if (var12 == null || var1.rank > var12.rank) {
                  var12 = var1;
               }
            }

            if (var12 == null) {
               throw new LibraryJavaSound$Exception("No useable mixers found!", new LibraryJavaSound$MixerRanking());
            }

            try {
               myMixer = AudioSystem.getMixer(var12.mixerInfo);
               mixerRanking(true, var12);
            } catch (Exception var8) {
               throw new LibraryJavaSound$Exception("No useable mixers available!", new LibraryJavaSound$MixerRanking());
            }
         }
      }

      this.setMasterVolume(1.0F);
      this.message("JavaSound initialized.");
      super.init();
   }

   public static boolean libraryCompatible() {
      for (Info var3 : AudioSystem.getMixerInfo()) {
         if (var3.getName().equals("Java Sound Audio Engine")) {
            return true;
         }
      }

      return false;
   }

   @Override
   protected Channel createChannel(int var1) {
      return new ChannelJavaSound(var1, myMixer);
   }

   @Override
   public void cleanup() {
      super.cleanup();
      instance = null;
      myMixer = null;
      myMixerRanking = null;
   }

   @Override
   public boolean loadSound(FilenameURL var1) {
      if (this.bufferMap == null) {
         this.bufferMap = new HashMap();
         this.importantMessage("Buffer Map was null in method 'loadSound'");
      }

      if (this.errorCheck(var1 == null, "Filename/URL not specified in method 'loadSound'")) {
         return false;
      } else if (this.bufferMap.get(var1.getFilename()) != null) {
         return true;
      } else {
         ICodec var2 = SoundSystemConfig.getCodec(var1.getFilename());
         if (this.errorCheck(var2 == null, "No codec found for file '" + var1.getFilename() + "' in method 'loadSound'")) {
            return false;
         } else {
            URL var3 = var1.getURL();
            if (this.errorCheck(var3 == null, "Unable to open file '" + var1.getFilename() + "' in method 'loadSound'")) {
               return false;
            } else {
               var2.initialize(var3);
               SoundBuffer var4 = var2.readAll();
               var2.cleanup();
               Object var5 = null;
               if (var4 != null) {
                  this.bufferMap.put(var1.getFilename(), var4);
               } else {
                  this.errorMessage("Sound buffer null in method 'loadSound'");
               }

               return true;
            }
         }
      }
   }

   @Override
   public boolean loadSound(SoundBuffer var1, String var2) {
      if (this.bufferMap == null) {
         this.bufferMap = new HashMap();
         this.importantMessage("Buffer Map was null in method 'loadSound'");
      }

      if (this.errorCheck(var2 == null, "Identifier not specified in method 'loadSound'")) {
         return false;
      } else if (this.bufferMap.get(var2) != null) {
         return true;
      } else {
         if (var1 != null) {
            this.bufferMap.put(var2, var1);
         } else {
            this.errorMessage("Sound buffer null in method 'loadSound'");
         }

         return true;
      }
   }

   @Override
   public void setMasterVolume(float var1) {
      super.setMasterVolume(var1);

      for (String var4 : this.sourceMap.keySet()) {
         Source var5 = (Source)this.sourceMap.get(var4);
         if (var5 != null) {
            var5.positionChanged();
         }
      }
   }

   @Override
   public void newSource(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10) {
      SoundBuffer var11 = null;
      if (!var2) {
         var11 = (SoundBuffer)this.bufferMap.get(var5.getFilename());
         if (var11 == null && !this.loadSound(var5)) {
            this.errorMessage("Source '" + var4 + "' was not created " + "because an error occurred while loading " + var5.getFilename());
            return;
         }

         var11 = (SoundBuffer)this.bufferMap.get(var5.getFilename());
         if (var11 == null) {
            this.errorMessage("Source '" + var4 + "' was not created " + "because audio data was not found for " + var5.getFilename());
            return;
         }
      }

      if (!var2 && var11 != null) {
         var11.trimData(1048576);
      }

      this.sourceMap.put(var4, new SourceJavaSound(this.listener, var1, var2, var3, var4, var5, var11, var6, var7, var8, var9, var10, false));
   }

   @Override
   public void rawDataStream(AudioFormat var1, boolean var2, String var3, float var4, float var5, float var6, int var7, float var8) {
      this.sourceMap.put(var3, new SourceJavaSound(this.listener, var1, var2, var3, var4, var5, var6, var7, var8));
   }

   @Override
   public void quickPlay(
      boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10, boolean var11
   ) {
      SoundBuffer var12 = null;
      if (!var2) {
         var12 = (SoundBuffer)this.bufferMap.get(var5.getFilename());
         if (var12 == null && !this.loadSound(var5)) {
            this.errorMessage("Source '" + var4 + "' was not created " + "because an error occurred while loading " + var5.getFilename());
            return;
         }

         var12 = (SoundBuffer)this.bufferMap.get(var5.getFilename());
         if (var12 == null) {
            this.errorMessage("Source '" + var4 + "' was not created " + "because audio data was not found for " + var5.getFilename());
            return;
         }
      }

      if (!var2 && var12 != null) {
         var12.trimData(1048576);
      }

      this.sourceMap.put(var4, new SourceJavaSound(this.listener, var1, var2, var3, var4, var5, var12, var6, var7, var8, var9, var10, var11));
   }

   @Override
   public void copySources(HashMap var1) {
      if (var1 != null) {
         Set var2 = var1.keySet();
         Iterator var3 = var2.iterator();
         if (this.bufferMap == null) {
            this.bufferMap = new HashMap();
            this.importantMessage("Buffer Map was null in method 'copySources'");
         }

         this.sourceMap.clear();

         while (var3.hasNext()) {
            String var4 = (String)var3.next();
            Source var5 = (Source)var1.get(var4);
            if (var5 != null) {
               SoundBuffer var6 = null;
               if (!var5.toStream) {
                  this.loadSound(var5.filenameURL);
                  var6 = (SoundBuffer)this.bufferMap.get(var5.filenameURL.getFilename());
               }

               if (!var5.toStream && var6 != null) {
                  var6.trimData(1048576);
               }

               if (var5.toStream || var6 != null) {
                  this.sourceMap.put(var4, new SourceJavaSound(this.listener, var5, var6));
               }
            }
         }
      }
   }

   @Override
   public void setListenerVelocity(float var1, float var2, float var3) {
      super.setListenerVelocity(var1, var2, var3);
      this.listenerMoved();
   }

   @Override
   public void dopplerChanged() {
      super.dopplerChanged();
      this.listenerMoved();
   }

   public static Mixer getMixer() {
      return mixer(false, null);
   }

   public static void setMixer(Mixer var0) {
      mixer(true, var0);
      SoundSystemException var1 = SoundSystem.getLastException();
      SoundSystem.setException(null);
      if (var1 != null) {
         throw var1;
      }
   }

   private static synchronized Mixer mixer(boolean var0, Mixer var1) {
      if (var0) {
         if (var1 == null) {
            return myMixer;
         }

         LibraryJavaSound$MixerRanking var2 = new LibraryJavaSound$MixerRanking();

         try {
            var2.rank(var1.getMixerInfo());
         } catch (LibraryJavaSound$Exception var5) {
            SoundSystemConfig.getLogger().printStackTrace(var5, 1);
            SoundSystem.setException(var5);
         }

         myMixer = var1;
         mixerRanking(true, var2);
         if (instance != null) {
            ListIterator var4 = instance.normalChannels.listIterator();
            SoundSystem.setException(null);

            while (var4.hasNext()) {
               ChannelJavaSound var3 = (ChannelJavaSound)var4.next();
               var3.newMixer(var1);
            }

            for (ChannelJavaSound var6 : instance.streamingChannels) {
               var6.newMixer(var1);
            }
         }
      }

      return myMixer;
   }

   public static LibraryJavaSound$MixerRanking getMixerRanking() {
      return mixerRanking(false, null);
   }

   private static synchronized LibraryJavaSound$MixerRanking mixerRanking(boolean var0, LibraryJavaSound$MixerRanking var1) {
      if (var0) {
         myMixerRanking = var1;
      }

      return myMixerRanking;
   }

   public static void setMinSampleRate(int var0) {
      minSampleRate(true, var0);
   }

   private static synchronized int minSampleRate(boolean var0, int var1) {
      if (var0) {
         minSampleRate = var1;
      }

      return minSampleRate;
   }

   public static void setMaxSampleRate(int var0) {
      maxSampleRate(true, var0);
   }

   private static synchronized int maxSampleRate(boolean var0, int var1) {
      if (var0) {
         maxSampleRate = var1;
      }

      return maxSampleRate;
   }

   public static void setLineCount(int var0) {
      lineCount(true, var0);
   }

   private static synchronized int lineCount(boolean var0, int var1) {
      if (var0) {
         lineCount = var1;
      }

      return lineCount;
   }

   public static void useGainControl(boolean var0) {
      useGainControl(true, var0);
   }

   private static synchronized boolean useGainControl(boolean var0, boolean var1) {
      if (var0) {
         useGainControl = var1;
      }

      return useGainControl;
   }

   public static void usePanControl(boolean var0) {
      usePanControl(true, var0);
   }

   private static synchronized boolean usePanControl(boolean var0, boolean var1) {
      if (var0) {
         usePanControl = var1;
      }

      return usePanControl;
   }

   public static void useSampleRateControl(boolean var0) {
      useSampleRateControl(true, var0);
   }

   private static synchronized boolean useSampleRateControl(boolean var0, boolean var1) {
      if (var0) {
         useSampleRateControl = var1;
      }

      return useSampleRateControl;
   }

   public static String getTitle() {
      return "Java Sound";
   }

   public static String getDescription() {
      return "The Java Sound API.  For more information, see http://java.sun.com/products/java-media/sound/";
   }

   @Override
   public String getClassName() {
      return "LibraryJavaSound";
   }
}
