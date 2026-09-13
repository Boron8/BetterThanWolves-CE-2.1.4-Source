package net.minecraft.src;

import com.prupe.mcpatcher.renderpass.RenderPass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class WorldRenderer {
   public World worldObj;
   private int glRenderList = -1;
   private static Tessellator tessellator = Tessellator.instance;
   public static int chunksUpdated = 0;
   public int posX;
   public int posY;
   public int posZ;
   public int posXMinus;
   public int posYMinus;
   public int posZMinus;
   public int posXClip;
   public int posYClip;
   public int posZClip;
   public boolean isInFrustum = false;
   public boolean[] skipRenderPass = new boolean[4];
   public int posXPlus;
   public int posYPlus;
   public int posZPlus;
   public boolean needsUpdate;
   public AxisAlignedBB rendererBoundingBox;
   public int chunkIndex;
   public boolean isVisible = true;
   public boolean isWaitingOnOcclusionQuery;
   public int glOcclusionQuery;
   public boolean isChunkLit;
   private boolean isInitialized = false;
   public List tileEntityRenderers = new ArrayList();
   private List tileEntities;
   private int bytesDrawn;

   public WorldRenderer(World par1World, List par2List, int par3, int par4, int par5, int par6) {
      this.worldObj = par1World;
      this.tileEntities = par2List;
      this.glRenderList = par6;
      this.posX = -999;
      this.setPosition(par3, par4, par5);
      this.needsUpdate = false;
   }

   public void setPosition(int par1, int par2, int par3) {
      if (par1 != this.posX || par2 != this.posY || par3 != this.posZ) {
         this.setDontDraw();
         this.posX = par1;
         this.posY = par2;
         this.posZ = par3;
         this.posXPlus = par1 + 8;
         this.posYPlus = par2 + 8;
         this.posZPlus = par3 + 8;
         this.posXClip = par1 & 1023;
         this.posYClip = par2;
         this.posZClip = par3 & 1023;
         this.posXMinus = par1 - this.posXClip;
         this.posYMinus = par2 - this.posYClip;
         this.posZMinus = par3 - this.posZClip;
         float var4 = 6.0F;
         this.rendererBoundingBox = AxisAlignedBB.getBoundingBox(par1 - var4, par2 - var4, par3 - var4, par1 + 16 + var4, par2 + 16 + var4, par3 + 16 + var4);
         GL11.glNewList(this.glRenderList + 4, 4864);
         RenderItem.a(
            AxisAlignedBB.getAABBPool()
               .getAABB(
                  this.posXClip - var4,
                  this.posYClip - var4,
                  this.posZClip - var4,
                  this.posXClip + 16 + var4,
                  this.posYClip + 16 + var4,
                  this.posZClip + 16 + var4
               )
         );
         GL11.glEndList();
         this.markDirty();
      }
   }

   private void setupGLTranslation() {
      GL11.glTranslatef(this.posXClip, this.posYClip, this.posZClip);
   }

   public void updateRenderer() {
      if (!this.needsUpdate) {
         RenderPass.finish();
      } else {
         this.needsUpdate = false;
         int var1 = this.posX;
         int var2 = this.posY;
         int var3 = this.posZ;
         int var4 = this.posX + 16;
         int var5 = this.posY + 16;
         int var6 = this.posZ + 16;

         for (int var7 = 0; var7 < 4; var7++) {
            this.skipRenderPass[var7] = true;
         }

         Chunk.isLit = false;
         HashSet var21 = new HashSet();
         var21.addAll(this.tileEntityRenderers);
         this.tileEntityRenderers.clear();
         byte var8 = 1;
         ChunkCache var9 = new ChunkCache(this.worldObj, var1 - var8, var2 - var8, var3 - var8, var4 + var8, var5 + var8, var6 + var8, var8);
         if (!var9.extendedLevelsInChunkCache()) {
            chunksUpdated++;
            RenderBlocks var10 = new RenderBlocks(var9);
            this.bytesDrawn = 0;

            for (int var11 = 0; var11 < 4; var11++) {
               boolean var12 = false;
               boolean var13 = false;
               boolean var14 = false;
               RenderPass.start(var11);

               for (int var15 = var2; var15 < var5; var15++) {
                  for (int var16 = var3; var16 < var6; var16++) {
                     for (int var17 = var1; var17 < var4; var17++) {
                        int var18 = var9.getBlockId(var17, var15, var16);
                        if (var18 > 0) {
                           if (!var14) {
                              var14 = true;
                              GL11.glNewList(this.glRenderList + var11, 4864);
                              GL11.glPushMatrix();
                              this.setupGLTranslation();
                              float var19 = 1.000001F;
                              GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
                              GL11.glScalef(var19, var19, var19);
                              GL11.glTranslatef(8.0F, 8.0F, 8.0F);
                              tessellator.startDrawingQuads();
                              tessellator.setTranslation(-this.posX, -this.posY, -this.posZ);
                           }

                           Block var23 = Block.blocksList[var18];
                           if (var23 != null) {
                              if (var11 == 0 && var23.hasTileEntity()) {
                                 TileEntity var20 = var9.getBlockTileEntity(var17, var15, var16);
                                 if (TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
                                    this.tileEntityRenderers.add(var20);
                                 }
                              }

                              int var24 = var23.getRenderBlockPass();
                              var12 = RenderPass.checkRenderPasses(var23, var12);
                              if (RenderPass.canRenderInThisPass(var24 == var11)) {
                                 var13 |= var10.renderBlockByRenderType(var23, var17, var15, var16);
                              }
                           }
                        }
                     }
                  }
               }

               if (var14) {
                  this.bytesDrawn = this.bytesDrawn + tessellator.draw();
                  GL11.glPopMatrix();
                  GL11.glEndList();
                  tessellator.setTranslation(0.0, 0.0, 0.0);
               } else {
                  var13 = false;
               }

               if (var13) {
                  this.skipRenderPass[var11] = false;
               }

               if (!var12) {
                  break;
               }
            }
         }

         HashSet var22 = new HashSet();
         var22.addAll(this.tileEntityRenderers);
         var22.removeAll(var21);
         this.tileEntities.addAll(var22);
         var21.removeAll(this.tileEntityRenderers);
         this.tileEntities.removeAll(var21);
         this.isChunkLit = Chunk.isLit;
         this.isInitialized = true;
         RenderPass.finish();
      }
   }

   public float distanceToEntitySquared(Entity par1Entity) {
      float var2 = (float)(par1Entity.posX - this.posXPlus);
      float var3 = (float)(par1Entity.posY - this.posYPlus);
      float var4 = (float)(par1Entity.posZ - this.posZPlus);
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   public void setDontDraw() {
      for (int var1 = 0; var1 < 4; var1++) {
         this.skipRenderPass[var1] = true;
      }

      this.isInFrustum = false;
      this.isInitialized = false;
   }

   public void stopRendering() {
      this.setDontDraw();
      this.worldObj = null;
   }

   public int getGLCallListForPass(int par1) {
      return !this.isInFrustum ? -1 : (!this.skipRenderPass[par1] ? this.glRenderList + par1 : -1);
   }

   public void updateInFrustum(ICamera par1ICamera) {
      this.isInFrustum = par1ICamera.isBoundingBoxInFrustum(this.rendererBoundingBox);
   }

   public void callOcclusionQueryList() {
      GL11.glCallList(this.glRenderList + 4);
   }

   public boolean skipAllRenderPasses() {
      return !this.isInitialized ? false : RenderPass.skipAllRenderPasses(this.skipRenderPass) && !this.needsUpdate;
   }

   public void markDirty() {
      this.needsUpdate = true;
   }
}
