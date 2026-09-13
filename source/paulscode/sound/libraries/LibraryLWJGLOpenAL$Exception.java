package paulscode.sound.libraries;

import paulscode.sound.SoundSystemException;

public class LibraryLWJGLOpenAL$Exception extends SoundSystemException {
   public static final int CREATE = 101;
   public static final int INVALID_NAME = 102;
   public static final int INVALID_ENUM = 103;
   public static final int INVALID_VALUE = 104;
   public static final int INVALID_OPERATION = 105;
   public static final int OUT_OF_MEMORY = 106;
   public static final int LISTENER = 107;
   public static final int NO_AL_PITCH = 108;

   public LibraryLWJGLOpenAL$Exception(String var1) {
      super(var1);
   }

   public LibraryLWJGLOpenAL$Exception(String var1, int var2) {
      super(var1, var2);
   }
}
