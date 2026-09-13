package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.block.BlockAPI;
import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import com.prupe.mcpatcher.mal.block.RenderPassAPI;
import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.ResourceList;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;

@Environment(EnvType.CLIENT)
public class CTMUtils {
   private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
   private static final boolean enableStandard = Config.getBoolean("Connected Textures", "standard", true);
   private static final boolean enableNonStandard = Config.getBoolean("Connected Textures", "nonStandard", true);
   private static final List<ITileOverride> allOverrides = new ArrayList<>();
   private static final Map<Block, List<BlockStateMatcher>> blockOverrides = new IdentityHashMap<>();
   private static final Map<String, List<ITileOverride>> tileOverrides = new HashMap<>();
   private static TileLoader tileLoader;
   private static ITileOverride lastOverride;
   private static final TileOverrideIterator.IJK ijkIterator = newIJKIterator();
   private static final TileOverrideIterator.Metadata metadataIterator = newMetadataIterator();
   private static boolean haveBlockFace;
   private static final BlockOrientation renderBlockState = new BlockOrientation();

   private static void clearBlockFace() {
      haveBlockFace = false;
   }

   public static Icon getBlockIcon(Icon icon, RenderBlocks renderBlocks, Block block, IBlockAccess blockAccess, int i, int j, int k, int face) {
      lastOverride = null;
      if (blockAccess != null && checkFace(face)) {
         if (!haveBlockFace) {
            renderBlockState.setBlock(block, blockAccess, i, j, k);
            renderBlockState.setFace(face);
         }

         lastOverride = ijkIterator.go(renderBlockState, icon);
         if (lastOverride != null) {
            icon = ijkIterator.getIcon();
         }
      }

      clearBlockFace();
      return lastOverride == null && skipDefaultRendering(block) ? RenderBlocksUtils.blankIcon : icon;
   }

   public static Icon getBlockIcon(Icon icon, RenderBlocks renderBlocks, Block block, int face, int metadata) {
      lastOverride = null;
      if (checkFace(face) && checkRenderType(block)) {
         renderBlockState.setBlockMetadata(block, metadata, face);
         lastOverride = metadataIterator.go(renderBlockState, icon);
         if (lastOverride != null) {
            icon = metadataIterator.getIcon();
         }
      }

      return icon;
   }

   public static Icon getBlockIcon(Icon icon, RenderBlocks renderBlocks, Block block, int face) {
      return getBlockIcon(icon, renderBlocks, block, face, 0);
   }

   public static void reset() {
   }

   private static boolean checkFace(int face) {
      return face < 0 ? enableNonStandard : enableStandard;
   }

   private static boolean checkRenderType(Block block) {
      switch (block.getRenderType()) {
         case 11:
         case 21:
            return false;
         default:
            return true;
      }
   }

   private static boolean skipDefaultRendering(Block block) {
      return RenderPassAPI.instance.skipDefaultRendering(block);
   }

   private static void registerOverride(ITileOverride override) {
      if (override != null && !override.isDisabled()) {
         boolean registered = false;
         List<BlockStateMatcher> matchingBlocks = override.getMatchingBlocks();
         if (!MCPatcherUtils.isNullOrEmpty(matchingBlocks)) {
            for (BlockStateMatcher matcher : matchingBlocks) {
               if (matcher != null) {
                  Block block = matcher.getBlock();
                  List<BlockStateMatcher> list = blockOverrides.get(block);
                  if (list == null) {
                     list = new ArrayList<>();
                     blockOverrides.put(block, list);
                  }

                  list.add(matcher);
                  logger.fine("using %s for block %s", override, BlockAPI.getBlockName(block));
                  registered = true;
               }
            }
         }

         Set<String> matchingTiles = override.getMatchingTiles();
         if (!MCPatcherUtils.isNullOrEmpty(matchingTiles)) {
            for (String name : matchingTiles) {
               List<ITileOverride> list = tileOverrides.get(name);
               if (list == null) {
                  list = new ArrayList<>();
                  tileOverrides.put(name, list);
               }

               list.add(override);
               logger.fine("using %s for tile %s", override, name);
               registered = true;
            }
         }

         if (registered) {
            allOverrides.add(override);
         }
      }
   }

   public static void setBlankResource() {
      RenderBlocksUtils.blankIcon = tileLoader.getIcon(RenderPassAPI.instance.getBlankResource());
   }

   public static TileOverrideIterator.IJK newIJKIterator() {
      return new TileOverrideIterator.IJK(blockOverrides, tileOverrides);
   }

   public static TileOverrideIterator.Metadata newMetadataIterator() {
      return new TileOverrideIterator.Metadata(blockOverrides, tileOverrides);
   }

   static {
      try {
         Class.forName("com.prupe.mcpatcher.renderpass.RenderPass").getMethod("finish").invoke(null);
      } catch (Throwable var1) {
      }

      TexturePackChangeHandler.register(new TexturePackChangeHandler("Connected Textures", 3) {
         @Override
         public void initialize() {
         }

         @Override
         public void beforeChange() {
            RenderPassAPI.instance.clear();

            try {
               GlassPaneRenderer.clear();
            } catch (NoClassDefFoundError var3) {
            } catch (Throwable var4) {
               var4.printStackTrace();
            }

            CTMUtils.renderBlockState.clear();
            CTMUtils.ijkIterator.clear();
            CTMUtils.metadataIterator.clear();
            CTMUtils.allOverrides.clear();
            CTMUtils.blockOverrides.clear();
            CTMUtils.tileOverrides.clear();
            CTMUtils.lastOverride = null;
            RenderBlocksUtils.blankIcon = null;
            CTMUtils.tileLoader = new TileLoader("textures/blocks", CTMUtils.logger);
            RenderPassAPI.instance.refreshBlendingOptions();
            if (CTMUtils.enableStandard || CTMUtils.enableNonStandard) {
               for (FakeResourceLocation resource : ResourceList.getInstance().listResources("/ctm", ".properties", true)) {
                  CTMUtils.registerOverride(TileOverride.create(resource, CTMUtils.tileLoader));
               }
            }

            for (FakeResourceLocation resource : BlendMethod.getAllBlankResources()) {
               CTMUtils.tileLoader.preloadTile(resource, false);
            }
         }

         @Override
         public void afterChange() {
            for (ITileOverride override : CTMUtils.allOverrides) {
               override.registerIcons();
            }

            for (Entry<Block, List<BlockStateMatcher>> entry : CTMUtils.blockOverrides.entrySet()) {
               for (BlockStateMatcher matcher : entry.getValue()) {
                  ITileOverride override = (ITileOverride)matcher.getData();
                  if (override.getRenderPass() >= 0) {
                     RenderPassAPI.instance.setRenderPassForBlock(entry.getKey(), override.getRenderPass());
                  }
               }
            }

            for (List<BlockStateMatcher> overrides : CTMUtils.blockOverrides.values()) {
               Collections.sort(overrides, new Comparator<BlockStateMatcher>() {
                  public int compare(BlockStateMatcher m1, BlockStateMatcher m2) {
                     ITileOverride o1 = (ITileOverride)m1.getData();
                     ITileOverride o2 = (ITileOverride)m2.getData();
                     return o1.compareTo(o2);
                  }
               });
            }

            for (List<ITileOverride> overrides : CTMUtils.tileOverrides.values()) {
               Collections.sort(overrides);
            }

            CTMUtils.setBlankResource();
         }
      });
   }
}
