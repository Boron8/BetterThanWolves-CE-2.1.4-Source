package btw.client.render.entity;

import btw.block.BTWBlocks;
import btw.block.blocks.AnchorBlock;
import btw.block.blocks.RopeBlock;
import btw.client.render.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class MovingAnchorRenderer extends Render {
   private RenderBlocks localRenderBlocks = new RenderBlocks();

   public MovingAnchorRenderer() {
      this.shadowSize = 0.0F;
   }

   @Override
   public void doRender(Entity entity, double x, double y, double z, float fYaw, float renderPartialTicks) {
      World world = entity.worldObj;
      this.localRenderBlocks.blockAccess = world;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x, (float)y, (float)z);
      GL11.glDisable(2896);
      int i = MathHelper.floor_double(entity.posX);
      int j = MathHelper.floor_double(entity.posY);
      int k = MathHelper.floor_double(entity.posZ);
      this.a("/terrain.png");
      Block block = BTWBlocks.anchor;
      double fHalfLength = 0.5;
      double fHalfWidth = 0.5;
      double dBlockHeight = AnchorBlock.anchorBaseHeight;
      this.localRenderBlocks.setRenderBounds(0.5 - fHalfWidth, 0.0, 0.5 - fHalfLength, 0.5 + fHalfWidth, dBlockHeight, 0.5 + fHalfLength);
      RenderUtils.renderMovingBlockWithMetadata(this.localRenderBlocks, block, world, i, j, k, 1);
      fHalfLength = 0.125;
      fHalfWidth = 0.125;
      dBlockHeight = 0.25;
      this.localRenderBlocks
         .setRenderBounds(
            0.5 - fHalfWidth, AnchorBlock.anchorBaseHeight, 0.5 - fHalfLength, 0.5 + fHalfWidth, AnchorBlock.anchorBaseHeight + dBlockHeight, 0.5 + fHalfLength
         );
      RenderUtils.renderMovingBlockWithTexture(this.localRenderBlocks, block, world, i, j, k, ((AnchorBlock)BTWBlocks.anchor).iconNub);
      if (world.getBlockId(i, j, k) != BTWBlocks.ropeBlock.blockID) {
         fHalfLength = 0.062375F;
         fHalfWidth = 0.062375F;
         double yOffset = 1.0 - (entity.posY - j);
         this.localRenderBlocks
            .setRenderBounds(0.5 - fHalfWidth, AnchorBlock.anchorBaseHeight, 0.5 - fHalfLength, 0.5 + fHalfWidth, (float)(yOffset + 1.0), 0.5 + fHalfLength);
         RenderUtils.renderMovingBlockWithTexture(this.localRenderBlocks, block, world, i, j, k, ((RopeBlock)BTWBlocks.ropeBlock).blockIcon);
      }

      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }
}
