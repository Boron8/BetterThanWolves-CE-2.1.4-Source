package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.MCPatcherUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BlockFaceMatcher {
   private final int faces;

   public static BlockFaceMatcher create(String propertyValue) {
      if (!MCPatcherUtils.isNullOrEmpty(propertyValue)) {
         String[] values = propertyValue.toLowerCase().split("\\s+");
         return new BlockFaceMatcher(values);
      } else {
         return null;
      }
   }

   protected BlockFaceMatcher(String[] values) {
      int flags = 0;

      for (String face : values) {
         if (face.equals("bottom") || face.equals("down")) {
            flags |= 1;
         } else if (face.equals("top") || face.equals("up")) {
            flags |= 2;
         } else if (face.equals("north")) {
            flags |= 4;
         } else if (face.equals("south")) {
            flags |= 8;
         } else if (face.equals("east")) {
            flags |= 32;
         } else if (face.equals("west")) {
            flags |= 16;
         } else if (face.equals("side") || face.equals("sides")) {
            flags |= 60;
         } else if (face.equals("all")) {
            flags = -1;
         }
      }

      this.faces = flags;
   }

   public boolean match(RenderBlockState renderBlockState) {
      int face = renderBlockState.getTextureFace();
      return face >= 0 && (this.faces & 1 << face) != 0;
   }

   protected boolean isAll() {
      return this.faces == -1;
   }
}
