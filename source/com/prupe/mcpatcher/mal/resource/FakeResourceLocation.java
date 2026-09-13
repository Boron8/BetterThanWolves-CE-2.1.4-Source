package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FakeResourceLocation {
   private static final MCLogger logger = MCLogger.getLogger("Texture Pack");
   private static final String GRID = "##";
   private static final String BLUR = "%blur%";
   private static final String CLAMP = "%clamp%";
   private final String path;
   private final boolean grid;
   private final boolean blur;
   private final boolean clamp;
   private final String toString;
   private final int hashCode;

   public static FakeResourceLocation wrap(String path) {
      return MCPatcherUtils.isNullOrEmpty(path) ? null : new FakeResourceLocation(path);
   }

   public static String unwrap(FakeResourceLocation resourceLocation) {
      return resourceLocation == null ? null : resourceLocation.toString();
   }

   public static IntBuffer getIntBuffer(IntBuffer buffer, int[] data) {
      ((Buffer)buffer).clear();
      int have = buffer.capacity();
      int needed = data.length;
      if (needed > have) {
         logger.finest("resizing gl buffer from 0x%x to 0x%x", have, needed);
         buffer = ByteBuffer.allocateDirect(4 * needed).order(buffer.order()).asIntBuffer();
      }

      buffer.put(data);
      ((Buffer)buffer).position(0).limit(needed);
      return buffer;
   }

   public FakeResourceLocation(String namespace, String path) {
      if (this.grid = path.startsWith("##")) {
         path = path.substring("##".length());
      }

      if (this.blur = path.startsWith("%blur%")) {
         path = path.substring("%blur%".length());
      }

      if (this.clamp = path.startsWith("%clamp%")) {
         path = path.substring("%clamp%".length());
      }

      if (!path.startsWith("/")) {
         path = "/" + path;
      }

      this.path = path;
      this.toString = (this.grid ? "##" : "") + (this.blur ? "%blur%" : "") + (this.clamp ? "%clamp%" : "") + path;
      this.hashCode = this.toString.hashCode();
   }

   public FakeResourceLocation(String path) {
      this("minecraft", path);
   }

   public String getNamespace() {
      return "minecraft";
   }

   public String getPath() {
      return this.path;
   }

   public boolean isGrid() {
      return this.grid;
   }

   public boolean isBlur() {
      return this.blur;
   }

   public boolean isClamp() {
      return this.clamp;
   }

   @Override
   public String toString() {
      return this.toString;
   }

   @Override
   public boolean equals(Object that) {
      if (this == that) {
         return true;
      } else {
         return !(that instanceof FakeResourceLocation) ? false : this.toString().equals(that.toString());
      }
   }

   @Override
   public int hashCode() {
      return this.hashCode;
   }
}
