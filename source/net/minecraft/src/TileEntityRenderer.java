package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityRenderer {
   public Map specialRendererMap = new HashMap();
   public static TileEntityRenderer instance = new TileEntityRenderer();
   private FontRenderer fontRenderer;
   public static double staticPlayerX;
   public static double staticPlayerY;
   public static double staticPlayerZ;
   public RenderEngine renderEngine;
   public World worldObj;
   public EntityLiving entityLivingPlayer;
   public float playerYaw;
   public float playerPitch;
   public double playerX;
   public double playerY;
   public double playerZ;

   private TileEntityRenderer() {
      this.specialRendererMap.put(TileEntitySign.class, new TileEntitySignRenderer());
      this.specialRendererMap.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
      this.specialRendererMap.put(TileEntityPiston.class, new TileEntityRendererPiston());
      this.specialRendererMap.put(TileEntityChest.class, new TileEntityChestRenderer());
      this.specialRendererMap.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
      this.specialRendererMap.put(TileEntityEnchantmentTable.class, new RenderEnchantmentTable());
      this.specialRendererMap.put(TileEntityEndPortal.class, new RenderEndPortal());
      this.specialRendererMap.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
      this.specialRendererMap.put(TileEntitySkull.class, new TileEntitySkullRenderer());

      for (TileEntitySpecialRenderer var2 : this.specialRendererMap.values()) {
         var2.setTileEntityRenderer(this);
      }
   }

   public void addSpecialRendererForClass(Class<? extends TileEntity> tileEntity, TileEntitySpecialRenderer renderer) {
      this.specialRendererMap.put(tileEntity, renderer);
      renderer.setTileEntityRenderer(this);
   }

   public TileEntitySpecialRenderer getSpecialRendererForClass(Class par1Class) {
      TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.specialRendererMap.get(par1Class);
      if (var2 == null && par1Class != TileEntity.class) {
         var2 = this.getSpecialRendererForClass(par1Class.getSuperclass());
         this.specialRendererMap.put(par1Class, var2);
      }

      return var2;
   }

   public boolean hasSpecialRenderer(TileEntity par1TileEntity) {
      return this.getSpecialRendererForEntity(par1TileEntity) != null;
   }

   public TileEntitySpecialRenderer getSpecialRendererForEntity(TileEntity par1TileEntity) {
      return par1TileEntity == null ? null : this.getSpecialRendererForClass(par1TileEntity.getClass());
   }

   public void cacheActiveRenderInfo(World par1World, RenderEngine par2RenderEngine, FontRenderer par3FontRenderer, EntityLiving par4EntityLiving, float par5) {
      if (this.worldObj != par1World) {
         this.setWorld(par1World);
      }

      this.renderEngine = par2RenderEngine;
      this.entityLivingPlayer = par4EntityLiving;
      this.fontRenderer = par3FontRenderer;
      this.playerYaw = par4EntityLiving.prevRotationYaw + (par4EntityLiving.rotationYaw - par4EntityLiving.prevRotationYaw) * par5;
      this.playerPitch = par4EntityLiving.prevRotationPitch + (par4EntityLiving.rotationPitch - par4EntityLiving.prevRotationPitch) * par5;
      this.playerX = par4EntityLiving.lastTickPosX + (par4EntityLiving.posX - par4EntityLiving.lastTickPosX) * par5;
      this.playerY = par4EntityLiving.lastTickPosY + (par4EntityLiving.posY - par4EntityLiving.lastTickPosY) * par5;
      this.playerZ = par4EntityLiving.lastTickPosZ + (par4EntityLiving.posZ - par4EntityLiving.lastTickPosZ) * par5;
   }

   public void renderTileEntity(TileEntity par1TileEntity, float par2) {
      if (par1TileEntity.getDistanceFrom(this.playerX, this.playerY, this.playerZ) < par1TileEntity.getMaxRenderDistanceSquared()) {
         int var3 = this.worldObj.getLightBrightnessForSkyBlocks(par1TileEntity.xCoord, par1TileEntity.yCoord, par1TileEntity.zCoord, 0);
         int var4 = var3 % 65536;
         int var5 = var3 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var4 / 1.0F, var5 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.renderTileEntityAt(
            par1TileEntity, par1TileEntity.xCoord - staticPlayerX, par1TileEntity.yCoord - staticPlayerY, par1TileEntity.zCoord - staticPlayerZ, par2
         );
      }
   }

   public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
      TileEntitySpecialRenderer var9 = this.getSpecialRendererForEntity(par1TileEntity);
      if (var9 != null) {
         try {
            var9.renderTileEntityAt(par1TileEntity, par2, par4, par6, par8);
         } catch (Throwable var13) {
            CrashReport var11 = CrashReport.makeCrashReport(var13, "Rendering Tile Entity");
            CrashReportCategory var12 = var11.makeCategory("Tile Entity Details");
            par1TileEntity.func_85027_a(var12);
            throw new ReportedException(var11);
         }
      }
   }

   public void setWorld(World par1World) {
      this.worldObj = par1World;

      for (TileEntitySpecialRenderer var3 : this.specialRendererMap.values()) {
         if (var3 != null) {
            var3.onWorldChange(par1World);
         }
      }
   }

   public FontRenderer getFontRenderer() {
      return this.fontRenderer;
   }

   public Map getSpecialRendererMap() {
      return this.specialRendererMap;
   }
}
