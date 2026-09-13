package btw.util;

import btw.block.util.RayTraceUtils;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public abstract class PrimitiveGeometric {
   public abstract PrimitiveGeometric makeTemporaryCopy();

   public abstract void rotateAroundYToFacing(int var1);

   public abstract void tiltToFacingAlongY(int var1);

   public abstract void addToRayTrace(RayTraceUtils var1);

   public abstract void translate(double var1, double var3, double var5);

   public void addIntersectingBoxesToCollisionList(World world, int i, int j, int k, AxisAlignedBB boxToIntersect, List collisionList) {
   }

   public int getAssemblyID() {
      return -1;
   }

   @Environment(EnvType.CLIENT)
   public abstract boolean renderAsBlock(RenderBlocks var1, Block var2, int var3, int var4, int var5);

   @Environment(EnvType.CLIENT)
   public abstract boolean renderAsBlockWithColorMultiplier(RenderBlocks var1, Block var2, int var3, int var4, int var5, float var6, float var7, float var8);

   @Environment(EnvType.CLIENT)
   public boolean renderAsBlockWithColorMultiplier(RenderBlocks renderBlocks, Block block, int x, int y, int z) {
      int colorMultiplier = block.colorMultiplier(renderBlocks.blockAccess, x, y, z);
      float red = (colorMultiplier >> 16 & 0xFF) / 255.0F;
      float green = (colorMultiplier >> 8 & 0xFF) / 255.0F;
      float blue = (colorMultiplier & 0xFF) / 255.0F;
      RenderBlocksUtils.setupColorMultiplierForceNoAO(block, renderBlocks.blockAccess, x, y, z, red, green, blue, true);
      return this.renderAsBlockWithColorMultiplier(renderBlocks, block, x, y, z, red, green, blue);
   }

   @Environment(EnvType.CLIENT)
   public abstract boolean renderAsBlockWithTexture(RenderBlocks var1, Block var2, int var3, int var4, int var5, Icon var6);

   @Environment(EnvType.CLIENT)
   public abstract boolean renderAsBlockFullBrightWithTexture(RenderBlocks var1, Block var2, int var3, int var4, int var5, Icon var6);

   @Environment(EnvType.CLIENT)
   public abstract void renderAsItemBlock(RenderBlocks var1, Block var2, int var3);

   @Environment(EnvType.CLIENT)
   public abstract void renderAsFallingBlock(RenderBlocks var1, Block var2, int var3, int var4, int var5, int var6);
}
