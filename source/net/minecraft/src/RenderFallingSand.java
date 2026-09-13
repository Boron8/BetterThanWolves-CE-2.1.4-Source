package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderFallingSand extends Render {
   private RenderBlocks sandRenderBlocks = new RenderBlocks();

   public RenderFallingSand() {
      this.shadowSize = 0.5F;
   }

   public void doRenderFallingSand(EntityFallingSand par1EntityFallingSand, double par2, double par4, double par6, float par8, float par9) {
      World var10 = par1EntityFallingSand.getWorld();
      Block var11 = Block.blocksList[par1EntityFallingSand.blockID];
      if (this.shouldRender(var10, par1EntityFallingSand)) {
         this.shadowSize = 0.5F;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2, (float)par4, (float)par6);
         this.a("/terrain.png");
         GL11.glDisable(2896);
         if (var11 instanceof BlockAnvil && var11.getRenderType() == 35) {
            this.sandRenderBlocks.blockAccess = var10;
            Tessellator var12 = Tessellator.instance;
            var12.startDrawingQuads();
            var12.setTranslation(
               -MathHelper.floor_double(par1EntityFallingSand.posX) - 0.5F,
               -MathHelper.floor_double(par1EntityFallingSand.posY) - 0.5F,
               -MathHelper.floor_double(par1EntityFallingSand.posZ) - 0.5F
            );
            this.sandRenderBlocks
               .renderBlockAnvilMetadata(
                  (BlockAnvil)var11,
                  MathHelper.floor_double(par1EntityFallingSand.posX),
                  MathHelper.floor_double(par1EntityFallingSand.posY),
                  MathHelper.floor_double(par1EntityFallingSand.posZ),
                  par1EntityFallingSand.metadata
               );
            var12.setTranslation(0.0, 0.0, 0.0);
            var12.draw();
         } else if (var11.getRenderType() == 27) {
            this.sandRenderBlocks.blockAccess = var10;
            Tessellator var12 = Tessellator.instance;
            var12.startDrawingQuads();
            var12.setTranslation(
               -MathHelper.floor_double(par1EntityFallingSand.posX) - 0.5F,
               -MathHelper.floor_double(par1EntityFallingSand.posY) - 0.5F,
               -MathHelper.floor_double(par1EntityFallingSand.posZ) - 0.5F
            );
            this.sandRenderBlocks
               .renderBlockDragonEgg(
                  (BlockDragonEgg)var11,
                  MathHelper.floor_double(par1EntityFallingSand.posX),
                  MathHelper.floor_double(par1EntityFallingSand.posY),
                  MathHelper.floor_double(par1EntityFallingSand.posZ)
               );
            var12.setTranslation(0.0, 0.0, 0.0);
            var12.draw();
         } else if (var11 != null) {
            this.sandRenderBlocks.blockAccess = var10;
            Tessellator.instance.startDrawingQuads();
            Tessellator.instance
               .setTranslation(
                  -MathHelper.floor_double(par1EntityFallingSand.posX) - 0.5,
                  -MathHelper.floor_double(par1EntityFallingSand.posY) - 0.5,
                  -MathHelper.floor_double(par1EntityFallingSand.posZ) - 0.5
               );
            var11.currentBlockRenderer = this.sandRenderBlocks;
            var11.renderFallingBlock(
               this.sandRenderBlocks,
               MathHelper.floor_double(par1EntityFallingSand.posX),
               MathHelper.floor_double(par1EntityFallingSand.posY),
               MathHelper.floor_double(par1EntityFallingSand.posZ),
               par1EntityFallingSand.metadata
            );
            Tessellator.instance.setTranslation(0.0, 0.0, 0.0);
            Tessellator.instance.draw();
         }

         GL11.glEnable(2896);
         GL11.glPopMatrix();
      } else {
         this.shadowSize = 0.0F;
      }
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderFallingSand((EntityFallingSand)par1Entity, par2, par4, par6, par8, par9);
   }

   private boolean shouldRender(World world, EntityFallingSand entity) {
      Block fallingBlock = Block.blocksList[entity.blockID];
      return fallingBlock != null ? fallingBlock.shouldRenderWhileFalling(world, entity) : false;
   }
}
