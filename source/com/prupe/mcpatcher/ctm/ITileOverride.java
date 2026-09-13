package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
interface ITileOverride extends Comparable<ITileOverride> {
   boolean isDisabled();

   void registerIcons();

   List<BlockStateMatcher> getMatchingBlocks();

   Set<String> getMatchingTiles();

   int getRenderPass();

   int getWeight();

   Icon getTileWorld(RenderBlockState var1, Icon var2);

   Icon getTileHeld(RenderBlockState var1, Icon var2);
}
