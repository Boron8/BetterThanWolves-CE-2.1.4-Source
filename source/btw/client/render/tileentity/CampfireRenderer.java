package btw.client.render.tileentity;

import btw.block.BTWBlocks;
import btw.block.tileentity.CampfireTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class CampfireRenderer extends TileEntitySpecialRenderer {
   @Override
   public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount) {
      CampfireTileEntity campfire = (CampfireTileEntity)tileEntity;
      this.renderCookStack(campfire, xCoord, yCoord, zCoord);
   }

   private void renderCookStack(CampfireTileEntity campfire, double xCoord, double yCoord, double zCoord) {
      ItemStack stack = campfire.getCookStack();
      if (stack != null) {
         int iMetadata = campfire.worldObj.getBlockMetadata(campfire.xCoord, campfire.yCoord, campfire.zCoord);
         boolean bIAligned = BTWBlocks.unlitCampfire.getIsIAligned(iMetadata);
         EntityItem entity = (EntityItem)EntityList.createEntityOfType(EntityItem.class, campfire.worldObj, 0.0, 0.0, 0.0, stack);
         entity.getEntityItem().stackSize = 1;
         entity.hoverStart = 0.0F;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)xCoord + 0.5F, (float)yCoord + 0.5625F, (float)zCoord + 0.5F);
         if (!bIAligned && RenderManager.instance.options.fancyGraphics) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         }

         RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0F, 0.0F);
         GL11.glPopMatrix();
      }
   }
}
