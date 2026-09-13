package btw.client.render.entity;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.entity.mechanical.platform.MovingPlatformEntity;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class MovingPlatformRenderer extends Render {
   private RenderBlocks localRenderBlocks = new RenderBlocks();

   public MovingPlatformRenderer() {
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
      Block block = BTWBlocks.platform;
      List list = entity.worldObj
         .getEntitiesWithinAABB(
            MovingPlatformEntity.class,
            AxisAlignedBB.getAABBPool()
               .getAABB(entity.posX - 1.0, entity.posY - 0.1F, entity.posZ - 0.1F, entity.posX - 0.9F, entity.posY + 0.1F, entity.posZ + 0.1F)
         );
      if (list == null || list.size() <= 0) {
         this.localRenderBlocks.setRenderBounds(1.0E-4F, 0.0625, 1.0E-4F, 0.0625, 0.9375, 0.9999F);
         RenderUtils.renderMovingBlock(this.localRenderBlocks, block, world, i, j, k);
      }

      list = entity.worldObj
         .getEntitiesWithinAABB(
            MovingPlatformEntity.class,
            AxisAlignedBB.getAABBPool()
               .getAABB(entity.posX - 0.1F, entity.posY - 0.1F, entity.posZ + 0.9F, entity.posX + 0.1F, entity.posY + 0.1F, entity.posZ + 1.0)
         );
      if (list == null || list.size() <= 0) {
         this.localRenderBlocks.setRenderBounds(0.0, 0.0625, 0.9375, 1.0, 0.9375, 1.0);
         RenderUtils.renderMovingBlock(this.localRenderBlocks, block, world, i, j, k);
      }

      list = entity.worldObj
         .getEntitiesWithinAABB(
            MovingPlatformEntity.class,
            AxisAlignedBB.getAABBPool()
               .getAABB(entity.posX + 0.9F, entity.posY - 0.1F, entity.posZ - 0.1F, entity.posX + 1.0, entity.posY + 0.1F, entity.posZ + 0.1F)
         );
      if (list == null || list.size() <= 0) {
         this.localRenderBlocks.setRenderBounds(0.9375, 0.0625, 1.0E-4F, 0.9999F, 0.9375, 0.9999F);
         RenderUtils.renderMovingBlock(this.localRenderBlocks, block, world, i, j, k);
      }

      list = entity.worldObj
         .getEntitiesWithinAABB(
            MovingPlatformEntity.class,
            AxisAlignedBB.getAABBPool()
               .getAABB(entity.posX - 0.1F, entity.posY - 0.1F, entity.posZ - 1.0, entity.posX + 0.1F, entity.posY + 0.1F, entity.posZ - 0.9F)
         );
      if (list == null || list.size() <= 0) {
         this.localRenderBlocks.setRenderBounds(0.0, 0.0625, 0.0, 1.0, 0.9375, 0.0625);
         RenderUtils.renderMovingBlock(this.localRenderBlocks, block, world, i, j, k);
      }

      this.localRenderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
      RenderUtils.renderMovingBlock(this.localRenderBlocks, block, world, i, j, k);
      this.localRenderBlocks.setRenderBounds(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderMovingBlock(this.localRenderBlocks, block, world, i, j, k);
      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }
}
