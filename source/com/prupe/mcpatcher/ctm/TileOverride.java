package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.biome.BiomeAPI;
import com.prupe.mcpatcher.mal.block.BlockAPI;
import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import com.prupe.mcpatcher.mal.block.RenderPassAPI;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
abstract class TileOverride implements ITileOverride {
   private static final MCLogger logger = MCLogger.getLogger("Connected Textures", "CTM");
   private static final int META_MASK = 65535;
   private final PropertiesFile properties;
   private final String baseFilename;
   private final TileLoader tileLoader;
   private final int renderPass;
   private final int weight;
   private final List<BlockStateMatcher> matchBlocks;
   private final Set<String> matchTiles;
   private final int defaultMetaMask;
   private final BlockFaceMatcher faceMatcher;
   private final int connectType;
   private final boolean innerSeams;
   private final BitSet biomes;
   private final BitSet height;
   private final List<FakeResourceLocation> tileNames = new ArrayList<>();
   protected Icon[] icons;
   private int matchMetadata = 65535;

   static TileOverride create(FakeResourceLocation propertiesFile, TileLoader tileLoader) {
      if (propertiesFile == null) {
         return null;
      } else {
         PropertiesFile properties = PropertiesFile.get(logger, propertiesFile);
         if (properties == null) {
            return null;
         } else {
            String method = properties.getString("method", "default").toLowerCase();
            TileOverride override = null;
            if (method.equals("default") || method.equals("glass") || method.equals("ctm")) {
               override = new TileOverrideImpl.CTM(properties, tileLoader);
            } else if (method.equals("random")) {
               override = new TileOverrideImpl.Random1(properties, tileLoader);
               if (override.getNumberOfTiles() == 1) {
                  override = new TileOverrideImpl.Fixed(properties, tileLoader);
               }
            } else if (method.equals("fixed") || method.equals("static")) {
               override = new TileOverrideImpl.Fixed(properties, tileLoader);
            } else if (method.equals("bookshelf") || method.equals("horizontal")) {
               override = new TileOverrideImpl.Horizontal(properties, tileLoader);
            } else if (method.equals("horizontal+vertical") || method.equals("h+v")) {
               override = new TileOverrideImpl.HorizontalVertical(properties, tileLoader);
            } else if (method.equals("vertical")) {
               override = new TileOverrideImpl.Vertical(properties, tileLoader);
            } else if (method.equals("vertical+horizontal") || method.equals("v+h")) {
               override = new TileOverrideImpl.VerticalHorizontal(properties, tileLoader);
            } else if (method.equals("sandstone") || method.equals("top")) {
               override = new TileOverrideImpl.Top(properties, tileLoader);
            } else if (!method.equals("repeat") && !method.equals("pattern")) {
               properties.error("unknown method \"%s\"", method);
            } else {
               override = new TileOverrideImpl.Repeat(properties, tileLoader);
            }

            if (override != null && !properties.valid()) {
               String status = override.checkTileMap();
               if (status != null) {
                  override.properties.error("invalid %s tile map: %s", override.getMethod(), status);
               }
            }

            return override != null && !override.isDisabled() ? override : null;
         }
      }
   }

   protected TileOverride(PropertiesFile properties, TileLoader tileLoader) {
      this.properties = properties;
      String texturesDirectory = properties.getResource().getPath().replaceFirst("/[^/]*$", "");
      this.baseFilename = properties.getResource().getPath().replaceFirst(".*/", "").replaceFirst("\\.properties$", "");
      this.tileLoader = tileLoader;
      String renderPassStr = properties.getString("renderPass", "");
      this.renderPass = RenderPassAPI.instance.parseRenderPass(renderPassStr);
      if (renderPassStr.matches("\\d+") && this.renderPass >= 0 && this.renderPass <= RenderPassAPI.MAX_EXTRA_RENDER_PASS) {
         properties.warning("renderPass=%s is deprecated, use renderPass=%s instead", renderPassStr, RenderPassAPI.instance.getRenderPassName(this.renderPass));
      }

      this.loadIcons();
      if (this.tileNames.isEmpty()) {
         properties.error("no images found in %s/", texturesDirectory);
      }

      String value;
      if (this.baseFilename.matches("block\\d+.*")) {
         value = this.baseFilename.replaceFirst("block(\\d+).*", "$1");
      } else {
         value = "";
      }

      this.matchBlocks = this.getBlockList(properties.getString("matchBlocks", value), properties.getString("metadata", ""));
      this.matchTiles = this.getTileList("matchTiles");
      if (this.matchBlocks.isEmpty() && this.matchTiles.isEmpty()) {
         this.matchTiles.add(this.baseFilename);
      }

      int bits = 0;

      for (int i : properties.getIntList("metadata", 0, 15, "0-15")) {
         bits |= 1 << i;
      }

      this.defaultMetaMask = bits;
      this.faceMatcher = BlockFaceMatcher.create(properties.getString("faces", ""));
      String connectType1 = properties.getString("connect", "").toLowerCase();
      if (connectType1.equals("")) {
         this.connectType = this.matchTiles.isEmpty() ? 0 : 1;
      } else if (connectType1.equals("block")) {
         this.connectType = 0;
      } else if (connectType1.equals("tile")) {
         this.connectType = 1;
      } else if (connectType1.equals("material")) {
         this.connectType = 2;
      } else {
         properties.error("invalid connect type %s", connectType1);
         this.connectType = 0;
      }

      this.innerSeams = properties.getBoolean("innerSeams", false);
      String biomeList = properties.getString("biomes", "");
      if (biomeList.isEmpty()) {
         this.biomes = null;
      } else {
         this.biomes = new BitSet();
         BiomeAPI.parseBiomeList(biomeList, this.biomes);
      }

      this.height = BiomeAPI.getHeightListProperty(properties, "");
      if (this.renderPass > RenderPassAPI.MAX_EXTRA_RENDER_PASS) {
         properties.error("invalid renderPass %s", renderPassStr);
      } else if (this.renderPass >= 0 && !this.matchTiles.isEmpty()) {
         properties.error("renderPass=%s must be block-based not tile-based", RenderPassAPI.instance.getRenderPassName(this.renderPass));
      }

      this.weight = properties.getInt("weight", 0);
   }

   private boolean addIcon(FakeResourceLocation resource) {
      this.tileNames.add(resource);
      return this.tileLoader.preloadTile(resource, this.renderPass > 4);
   }

   private void loadIcons() {
      this.tileNames.clear();
      String tileList = this.properties.getString("tiles", "");
      FakeResourceLocation blankResource = RenderPassAPI.instance.getBlankResource(this.renderPass);
      if (tileList.equals("")) {
         int i = 0;

         while (true) {
            FakeResourceLocation resource = TileLoader.parseTileAddress(this.properties.getResource(), String.valueOf(i), blankResource);
            if (!TexturePackAPI.hasResource(resource) || !this.addIcon(resource)) {
               break;
            }

            i++;
         }
      } else {
         Pattern range = Pattern.compile("(\\d+)-(\\d+)");

         for (String token : tileList.split("\\s+")) {
            Matcher matcher = range.matcher(token);
            if (!token.equals("")) {
               if (matcher.matches()) {
                  try {
                     int from = Integer.parseInt(matcher.group(1));
                     int to = Integer.parseInt(matcher.group(2));

                     for (int i = from; i <= to; i++) {
                        FakeResourceLocation resource = TileLoader.parseTileAddress(this.properties.getResource(), String.valueOf(i), blankResource);
                        if (TexturePackAPI.hasResource(resource)) {
                           this.addIcon(resource);
                        } else {
                           this.properties.warning("could not find image %s", resource);
                           this.tileNames.add(null);
                        }
                     }
                  } catch (NumberFormatException var13) {
                     var13.printStackTrace();
                  }
               } else {
                  FakeResourceLocation resource = TileLoader.parseTileAddress(this.properties.getResource(), token, blankResource);
                  if (resource == null) {
                     this.tileNames.add(null);
                  } else if (TexturePackAPI.hasResource(resource)) {
                     this.addIcon(resource);
                  } else {
                     this.properties.warning("could not find image %s", resource);
                     this.tileNames.add(null);
                  }
               }
            }
         }
      }
   }

   private List<BlockStateMatcher> getBlockList(String property, String defaultMetadata) {
      List<BlockStateMatcher> blocks = new ArrayList<>();
      if (!MCPatcherUtils.isNullOrEmpty(defaultMetadata)) {
         defaultMetadata = ':' + defaultMetadata;
      }

      for (String token : property.split("\\s+")) {
         if (!token.equals("")) {
            if (token.matches("\\d+-\\d+")) {
               for (int id : MCPatcherUtils.parseIntegerList(token, 0, 65535)) {
                  BlockStateMatcher matcher = BlockAPI.createMatcher(this.properties, id + defaultMetadata);
                  if (matcher == null) {
                     this.properties.warning("unknown block id %d", id);
                  } else {
                     blocks.add(matcher);
                  }
               }
            } else {
               BlockStateMatcher matcher = BlockAPI.createMatcher(this.properties, token + defaultMetadata);
               if (matcher == null) {
                  this.properties.warning("unknown block %s", token);
               } else {
                  blocks.add(matcher);
               }
            }
         }
      }

      for (BlockStateMatcher matcher : blocks) {
         matcher.setData(this);
      }

      return blocks;
   }

   private Set<String> getTileList(String key) {
      Set<String> list = new HashSet<>();
      String property = this.properties.getString(key, "");

      for (String token : property.split("\\s+")) {
         if (!token.equals("")) {
            if (token.contains("/")) {
               if (!token.endsWith(".png")) {
                  token = token + ".png";
               }

               FakeResourceLocation resource = TexturePackAPI.parseResourceLocation(this.properties.getResource(), token);
               if (resource != null) {
                  list.add(resource.toString());
               }
            } else {
               list.add(token);
            }
         }
      }

      return list;
   }

   protected int getNumberOfTiles() {
      return this.tileNames.size();
   }

   String checkTileMap() {
      return null;
   }

   boolean requiresFace() {
      return false;
   }

   @Override
   public String toString() {
      return String.format("%s[%s] (%d tiles)", this.getMethod(), this.properties, this.getNumberOfTiles());
   }

   @Override
   public final void registerIcons() {
      this.icons = new Icon[this.tileNames.size()];

      for (int i = 0; i < this.icons.length; i++) {
         this.icons[i] = this.tileLoader.getIcon(this.tileNames.get(i));
      }
   }

   @Override
   public final boolean isDisabled() {
      return !this.properties.valid();
   }

   @Override
   public final List<BlockStateMatcher> getMatchingBlocks() {
      return this.matchBlocks;
   }

   @Override
   public final Set<String> getMatchingTiles() {
      if (MCPatcherUtils.isNullOrEmpty(this.matchTiles)) {
         return null;
      } else {
         Set<String> m = new HashSet<>();

         for (String s : this.matchTiles) {
            m.add(BlockAPI.expandTileName(s));
         }

         return m;
      }
   }

   @Override
   public final int getRenderPass() {
      return this.renderPass;
   }

   @Override
   public final int getWeight() {
      return this.weight;
   }

   public int compareTo(ITileOverride o) {
      int result = o.getWeight() - this.getWeight();
      if (result != 0) {
         return result;
      } else {
         return o instanceof TileOverride ? this.baseFilename.compareTo(((TileOverride)o).baseFilename) : -1;
      }
   }

   final boolean shouldConnect(RenderBlockState renderBlockState, Icon icon, int relativeDirection) {
      return this.shouldConnect(renderBlockState, icon, renderBlockState.getOffset(renderBlockState.getBlockFace(), relativeDirection));
   }

   final boolean shouldConnect(RenderBlockState renderBlockState, Icon icon, int blockFace, int relativeDirection) {
      return this.shouldConnect(renderBlockState, icon, renderBlockState.getOffset(blockFace, relativeDirection));
   }

   private boolean shouldConnect(RenderBlockState renderBlockState, Icon icon, int[] offset) {
      IBlockAccess blockAccess = renderBlockState.getBlockAccess();
      Block block = renderBlockState.getBlock();
      int i = renderBlockState.getI();
      int j = renderBlockState.getJ();
      int k = renderBlockState.getK();
      i += offset[0];
      j += offset[1];
      k += offset[2];
      Block neighbor = BlockAPI.getBlockAt(blockAccess, i, j, k);
      if (neighbor == null) {
         return false;
      } else {
         if (block == neighbor) {
            BlockStateMatcher filter = renderBlockState.getFilter();
            if (filter != null && !filter.match(blockAccess, i, j, k)) {
               return false;
            }
         }

         if (this.innerSeams) {
            int blockFace = renderBlockState.getBlockFace();
            if (blockFace >= 0) {
               int[] normal = RenderBlockState.NORMALS[blockFace];
               if (!BlockAPI.shouldSideBeRendered(neighbor, blockAccess, i + normal[0], j + normal[1], k + normal[2], blockFace)) {
                  return false;
               }
            }
         }

         switch (this.connectType) {
            case 0:
               return renderBlockState.shouldConnectByBlock(neighbor, i, j, k);
            case 1:
               return renderBlockState.shouldConnectByTile(neighbor, icon, i, j, k);
            case 2:
               return block.blockMaterial == neighbor.blockMaterial;
            default:
               return false;
         }
      }
   }

   @Override
   public final Icon getTileWorld(RenderBlockState renderBlockState, Icon origIcon) {
      if (this.icons == null) {
         this.properties.error("no images loaded, disabling");
         return null;
      } else {
         IBlockAccess blockAccess = renderBlockState.getBlockAccess();
         Block block = renderBlockState.getBlock();
         int i = renderBlockState.getI();
         int j = renderBlockState.getJ();
         int k = renderBlockState.getK();
         if (renderBlockState.getBlockFace() < 0 && this.requiresFace()) {
            this.properties
               .warning(
                  "method=%s is not supported for non-standard block %s:%d @ %d %d %d",
                  this.getMethod(),
                  BlockAPI.getBlockName(block),
                  BlockAPI.getMetadataAt(blockAccess, i, j, k),
                  i,
                  j,
                  k
               );
            return null;
         } else if (block != null && !RenderPassAPI.instance.skipThisRenderPass(block, this.renderPass)) {
            BlockStateMatcher filter = renderBlockState.getFilter();
            if (filter != null && !filter.match(blockAccess, i, j, k)) {
               return null;
            } else if (this.faceMatcher != null && !this.faceMatcher.match(renderBlockState)) {
               return null;
            } else if (this.height != null && !this.height.get(j)) {
               return null;
            } else {
               return this.biomes != null && !this.biomes.get(BiomeAPI.getBiomeIDAt(blockAccess, i, j, k))
                  ? null
                  : this.getTileWorld_Impl(renderBlockState, origIcon);
            }
         } else {
            return null;
         }
      }
   }

   @Override
   public final Icon getTileHeld(RenderBlockState renderBlockState, Icon origIcon) {
      if (this.icons == null) {
         this.properties.error("no images loaded, disabling");
         return null;
      } else {
         Block block = renderBlockState.getBlock();
         if (block != null && !RenderPassAPI.instance.skipThisRenderPass(block, this.renderPass)) {
            int face = renderBlockState.getTextureFace();
            if (face < 0 && this.requiresFace()) {
               this.properties.warning("method=%s is not supported for non-standard block %s", this.getMethod(), renderBlockState);
               return null;
            } else if (this.height != null || this.biomes != null) {
               return null;
            } else {
               return this.faceMatcher != null && !this.faceMatcher.match(renderBlockState) ? null : this.getTileHeld_Impl(renderBlockState, origIcon);
            }
         } else {
            return null;
         }
      }
   }

   abstract String getMethod();

   abstract Icon getTileWorld_Impl(RenderBlockState var1, Icon var2);

   abstract Icon getTileHeld_Impl(RenderBlockState var1, Icon var2);
}
