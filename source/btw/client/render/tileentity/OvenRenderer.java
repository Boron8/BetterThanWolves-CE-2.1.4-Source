package btw.client.render.tileentity;

import btw.block.tileentity.OvenTileEntity;
import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.ItemStack;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.Vec3;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class OvenRenderer extends TileEntitySpecialRenderer {
   private static final double COOKING_ITEM_VISUAL_OFFSET = 0.25;

   @Override
   public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount) {
      OvenTileEntity furnace = (OvenTileEntity)tileEntity;
      ItemStack cookStack = furnace.getCookStack();
      if (cookStack != null) {
         this.renderCookStack(furnace, xCoord, yCoord, zCoord, cookStack, fPartialTickCount);
      }
   }

   private void renderCookStack(OvenTileEntity furnace, double xCoord, double yCoord, double zCoord, ItemStack stack, float fPartialTickCount) {
      int iMetadata = furnace.worldObj.getBlockMetadata(furnace.xCoord, furnace.yCoord, furnace.zCoord);
      EntityItem entityItem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, furnace.worldObj, 0.0, 0.0, 0.0, stack);
      entityItem.getEntityItem().stackSize = 1;
      entityItem.hoverStart = 0.0F;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)xCoord + 0.5F, (float)yCoord + 0.40625F, (float)zCoord + 0.5F);
      GL11.glScalef(0.7F, 0.7F, 0.7F);
      int iFacing = iMetadata & 7;
      Vec3 vOffset = Vec3.createVectorHelper(0.0, 0.0, 0.0);
      float fYaw = 0.0F;
      if (iFacing == 2) {
         fYaw = 0.0F;
         vOffset.zCoord = -0.25;
      } else if (iFacing == 3) {
         fYaw = 180.0F;
         vOffset.zCoord = 0.25;
      } else if (iFacing == 4) {
         fYaw = 90.0F;
         vOffset.xCoord = -0.25;
      } else if (iFacing == 5) {
         fYaw = 270.0F;
         vOffset.xCoord = 0.25;
      }

      GL11.glTranslatef((float)vOffset.xCoord, (float)vOffset.yCoord, (float)vOffset.zCoord);
      if (RenderManager.instance.options.fancyGraphics) {
         GL11.glRotatef(fYaw, 0.0F, 1.0F, 0.0F);
      }

      int iBrightness = this.getItemRenderBrightnessForBlockToFacing(furnace, iFacing);
      int var11 = iBrightness % 65536;
      int var12 = iBrightness / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 / 1.0F, var12 / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderManager.instance.renderEntityWithPosYaw(entityItem, 0.0, 0.0, 0.0, 0.0F, 0.0F);
      GL11.glPopMatrix();
   }

   protected int getItemRenderBrightnessForBlockToFacing(OvenTileEntity furnace, int iFacing) {
      BlockPos targetPos = new BlockPos(furnace.xCoord, furnace.yCoord, furnace.zCoord, iFacing);
      return furnace.worldObj.blockExists(targetPos.x, targetPos.y, targetPos.z)
         ? furnace.worldObj.getLightBrightnessForSkyBlocks(targetPos.x, targetPos.y, targetPos.z, 0)
         : 0;
   }
}
