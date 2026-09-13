package com.prupe.mcpatcher.mal.block;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;

@Environment(EnvType.CLIENT)
public class BlockStateMatcher {
   private final String fullString;
   private final ThreadLocal<Object> threadLocal = new ThreadLocal<>();
   private static final int MAX_METADATA = 15;
   private static final int NO_METADATA = -1;
   private final int metadataBits;
   private static Block doublePlantBlock;
   private static Block logBlock;
   protected final Block block;
   protected Object data;

   protected BlockStateMatcher(PropertiesFile source, String metaString, Block block, String metadataList, Map<String, String> properties) {
      this.fullString = BlockAPI.getBlockName(block) + metaString;
      this.block = block;
      if (MCPatcherUtils.isNullOrEmpty(metadataList)) {
         this.metadataBits = -1;
      } else {
         int bits = 0;

         for (int i : MCPatcherUtils.parseIntegerList(metadataList, 0, 15)) {
            bits |= 1 << i;
         }

         this.metadataBits = bits;
      }

      doublePlantBlock = BlockAPI.parseBlockName("minecraft:double_plant");
      logBlock = BlockAPI.parseBlockName("minecraft:log");
   }

   public final Block getBlock() {
      return this.block;
   }

   public final Object getData() {
      return this.data;
   }

   public final void setData(Object data) {
      this.data = data;
   }

   public final Object getThreadData() {
      return this.threadLocal.get();
   }

   public final void setThreadData(Object data) {
      this.threadLocal.set(data);
   }

   @Override
   public final String toString() {
      return this.fullString;
   }

   public boolean match(IBlockAccess blockAccess, int i, int j, int k) {
      Block block = BlockAPI.getBlockAt(blockAccess, i, j, k);
      if (block != this.block) {
         return false;
      } else {
         int metadata = BlockAPI.getMetadataAt(blockAccess, i, j, k);
         if (block == doublePlantBlock) {
            if ((metadata & 8) != 0 && BlockAPI.getBlockAt(blockAccess, i, j - 1, k) == block) {
               metadata = BlockAPI.getMetadataAt(blockAccess, i, j - 1, k);
            }

            metadata &= 7;
         } else if (block == logBlock) {
            metadata &= -13;
         }

         return (this.metadataBits & 1 << metadata) != 0;
      }
   }

   public boolean match(Block block, int metadata) {
      return block == this.block && (this.metadataBits & 1 << metadata) != 0;
   }

   public boolean matchBlockState(Object blockState) {
      throw new UnsupportedOperationException("match by block state");
   }

   public boolean isUnfiltered() {
      return this.metadataBits == -1;
   }
}
