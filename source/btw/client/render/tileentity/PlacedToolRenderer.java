package btw.client.render.tileentity;

import btw.block.BTWBlocks;
import btw.block.tileentity.PlacedToolTileEntity;
import btw.item.PlaceableAsItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class PlacedToolRenderer extends TileEntitySpecialRenderer {
   @Override
   public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount) {
      PlacedToolTileEntity tileEntityTool = (PlacedToolTileEntity)tileEntity;
      ItemStack toolStack = tileEntityTool.getToolStack();
      if (toolStack != null) {
         this.renderToolStack(tileEntityTool, toolStack, xCoord, yCoord, zCoord);
      }
   }

   private void renderToolStack(PlacedToolTileEntity tileEntityTool, ItemStack toolStack, double xCoord, double yCoord, double zCoord) {
      int iMetadata = tileEntityTool.worldObj.getBlockMetadata(tileEntityTool.xCoord, tileEntityTool.yCoord, tileEntityTool.zCoord);
      int iFacing = BTWBlocks.placedTool.getFacing(iMetadata);
      int iFacingLevel = BTWBlocks.placedTool.getVerticalOrientation(iMetadata);
      EntityItem entityTool = (EntityItem)EntityList.createEntityOfType(EntityItem.class, tileEntityTool.worldObj, 0.0, 0.0, 0.0, toolStack);
      PlaceableAsItem toolItem = (PlaceableAsItem)toolStack.getItem();
      entityTool.getEntityItem().stackSize = 1;
      entityTool.hoverStart = 0.0F;
      GL11.glPushMatrix();
      float fVerticalOffset = toolItem.getVisualVerticalOffsetAsBlock();
      float fHorizontalOffset = toolItem.getVisualHorizontalOffsetAsBlock();
      float fXOffset = 0.5F;
      float fZOffset = 0.5F;
      float fYOffset = 0.5F;
      if (iFacingLevel == 2) {
         if (iFacing == 2) {
            fZOffset = fVerticalOffset;
         } else if (iFacing == 3) {
            fZOffset = 1.0F - fVerticalOffset;
         } else if (iFacing == 4) {
            fXOffset = fVerticalOffset;
         } else if (iFacing == 5) {
            fXOffset = 1.0F - fVerticalOffset;
         }

         fYOffset = fHorizontalOffset;
      } else if (iFacingLevel == 1) {
         fYOffset = 1.0F - fVerticalOffset;
         if (iFacing == 2) {
            fZOffset = fHorizontalOffset;
         } else if (iFacing == 3) {
            fZOffset = 1.0F - fHorizontalOffset;
         } else if (iFacing == 4) {
            fXOffset = fHorizontalOffset;
         } else if (iFacing == 5) {
            fXOffset = 1.0F - fHorizontalOffset;
         }
      } else {
         fYOffset = fVerticalOffset;
         if (iFacing == 2) {
            fZOffset = 1.0F - fHorizontalOffset;
         } else if (iFacing == 3) {
            fZOffset = fHorizontalOffset;
         } else if (iFacing == 4) {
            fXOffset = 1.0F - fHorizontalOffset;
         } else if (iFacing == 5) {
            fXOffset = fHorizontalOffset;
         }
      }

      GL11.glTranslatef((float)xCoord + fXOffset, (float)yCoord + fYOffset, (float)zCoord + fZOffset);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      float fPitch = 0.0F;
      float fRoll = 0.0F;
      if (iFacing == 2) {
         fPitch = 90.0F;
      }

      if (iFacing == 3) {
         fPitch = 270.0F;
      } else if (iFacing == 4) {
         fPitch = 180.0F;
      } else if (iFacing == 5) {
         fPitch = 0.0F;
      }

      if (iFacingLevel == 0) {
         fRoll = 180.0F;
      } else if (iFacingLevel == 2) {
         fRoll = 270.0F;
      }

      fRoll += toolItem.getVisualRollOffsetAsBlock();
      GL11.glRotatef(fPitch, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(fRoll, 0.0F, 0.0F, 1.0F);
      RenderItem.forceFancyItemRender = true;
      RenderManager.instance.renderEntityWithPosYaw(entityTool, 0.0, 0.0, 0.0, 0.0F, 0.0F);
      RenderItem.forceFancyItemRender = false;
      GL11.glPopMatrix();
   }
}
