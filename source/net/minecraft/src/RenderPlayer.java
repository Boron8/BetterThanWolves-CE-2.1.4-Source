package net.minecraft.src;

import btw.BTWMod;
import btw.entity.model.PlayerArmorModel;
import btw.entity.model.PlayerModel;
import btw.item.BTWItems;
import btw.item.items.ArmorItemMod;
import com.prupe.mcpatcher.cit.CITUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderPlayer extends RenderLiving {
   private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
   private ModelBiped modelArmorChestplate = new PlayerArmorModel(1.0F);
   private ModelBiped modelArmor = new PlayerArmorModel(0.5F);
   private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

   public RenderPlayer() {
      super(new PlayerModel(0.0F), 0.5F);
   }

   protected void func_98191_a(EntityPlayer par1EntityPlayer) {
      this.a(par1EntityPlayer.skinUrl, par1EntityPlayer.N());
   }

   protected int setArmorModel(EntityPlayer par1EntityPlayer, int par2, float par3) {
      ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);
      if (var4 != null) {
         Item var5 = var4.getItem();
         if (var5 instanceof ItemArmor) {
            ItemArmor var6 = (ItemArmor)var5;
            if (var5 instanceof ArmorItemMod) {
               return this.shouldRenderPassModArmor(var4, par2, (ArmorItemMod)var5);
            }

            this.a(
               FakeResourceLocation.unwrap(
                  CITUtils.getArmorTexture(
                     FakeResourceLocation.wrap("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + ".png"), par1EntityPlayer, var4
                  )
               )
            );
            ModelBiped var7 = par2 == 2 ? this.modelArmor : this.modelArmorChestplate;
            var7.bipedHead.showModel = par2 == 0;
            var7.bipedHeadwear.showModel = par2 == 0;
            var7.bipedBody.showModel = par2 == 1 || par2 == 2;
            var7.bipedRightArm.showModel = par2 == 1;
            var7.bipedLeftArm.showModel = par2 == 1;
            var7.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
            var7.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
            this.a(var7);
            if (var7 != null) {
               var7.onGround = this.mainModel.onGround;
            }

            if (var7 != null) {
               var7.isRiding = this.mainModel.isRiding;
            }

            if (var7 != null) {
               var7.isChild = this.mainModel.isChild;
            }

            float var8 = 1.0F;
            if (var6.getArmorMaterial() == EnumArmorMaterial.CLOTH) {
               int var9 = var6.getColor(var4);
               float var10 = (var9 >> 16 & 0xFF) / 255.0F;
               float var11 = (var9 >> 8 & 0xFF) / 255.0F;
               float var12 = (var9 & 0xFF) / 255.0F;
               GL11.glColor3f(var8 * var10, var8 * var11, var8 * var12);
               if (var4.isItemEnchanted()) {
                  return 31;
               }

               return 16;
            }

            GL11.glColor3f(var8, var8, var8);
            if (var4.isItemEnchanted()) {
               return 15;
            }

            return 1;
         }
      }

      return -1;
   }

   protected void func_82439_b(EntityPlayer par1EntityPlayer, int par2, float par3) {
      ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);
      if (var4 != null) {
         Item var5 = var4.getItem();
         if (var5 instanceof ItemArmor) {
            if (var5 instanceof ArmorItemMod) {
               this.loadSecondLayerOfModArmorTexture(par2, (ArmorItemMod)var5);
               return;
            }

            ItemArmor var6 = (ItemArmor)var5;
            this.a(
               FakeResourceLocation.unwrap(
                  CITUtils.getArmorTexture(
                     FakeResourceLocation.wrap("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + "_b.png"),
                     par1EntityPlayer,
                     var4
                  )
               )
            );
            float var7 = 1.0F;
            GL11.glColor3f(var7, var7, var7);
         }
      }
   }

   public void renderPlayer(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, float par8, float par9) {
      float var10 = 1.0F;
      GL11.glColor3f(var10, var10, var10);
      ItemStack var11 = par1EntityPlayer.inventory.getCurrentItem();
      this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = var11 != null ? 1 : 0;
      if (var11 != null && par1EntityPlayer.getItemInUseCount() > 0) {
         EnumAction var12 = var11.getItemUseAction();
         if (var12 == EnumAction.block) {
            this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 3;
         } else if (var12 == EnumAction.bow) {
            this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = true;
         }
      }

      this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = par1EntityPlayer.ag();
      double var14 = par4 - par1EntityPlayer.yOffset;
      if (par1EntityPlayer.ag() && !(par1EntityPlayer instanceof EntityPlayerSP)) {
         var14 -= 0.125;
      }

      super.doRenderLiving(par1EntityPlayer, par2, var14, par6, par8, par9);
      this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
      this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
      this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
   }

   protected void renderSpecials(EntityPlayer par1EntityPlayer, float par2) {
      float var3 = 1.0F;
      GL11.glColor3f(var3, var3, var3);
      super.renderEquippedItems(par1EntityPlayer, par2);
      super.renderArrowsStuckInEntity(par1EntityPlayer, par2);
      ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3);
      if (var4 != null) {
         GL11.glPushMatrix();
         this.modelBipedMain.bipedHead.postRender(0.0625F);
         if (var4.getItem().itemID < 4096 && Block.blocksList[var4.itemID] != null) {
            if (Block.blocksList[var4.itemID].doesItemRenderAsBlock(var4.getItemDamage())) {
               float var5 = 0.625F;
               GL11.glTranslatef(0.0F, -0.25F, 0.0F);
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glScalef(var5, -var5, -var5);
            }

            this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var4, 0);
         } else if (var4.getItem().itemID == Item.skull.itemID) {
            float var5 = 1.0625F;
            GL11.glScalef(var5, -var5, -var5);
            String var6 = "";
            if (var4.hasTagCompound() && var4.getTagCompound().hasKey("SkullOwner")) {
               var6 = var4.getTagCompound().getString("SkullOwner");
            }

            TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, var4.getItemDamage(), var6);
         }

         GL11.glPopMatrix();
      }

      if (par1EntityPlayer.username.equals("deadmau5") && this.a(par1EntityPlayer.skinUrl, (String)null)) {
         for (int var20 = 0; var20 < 2; var20++) {
            float var25 = par1EntityPlayer.prevRotationYaw
               + (par1EntityPlayer.rotationYaw - par1EntityPlayer.prevRotationYaw) * par2
               - (par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2);
            float var7 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * par2;
            GL11.glPushMatrix();
            GL11.glRotatef(var25, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var7, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.375F * (var20 * 2 - 1), 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.375F, 0.0F);
            GL11.glRotatef(-var7, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-var25, 0.0F, 1.0F, 0.0F);
            float var8 = 1.3333334F;
            GL11.glScalef(var8, var8, var8);
            this.modelBipedMain.renderEars(0.0625F);
            GL11.glPopMatrix();
         }
      }

      if (this.a(par1EntityPlayer.cloakUrl, (String)null) && !par1EntityPlayer.ai() && !par1EntityPlayer.getHideCape()) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 0.125F);
         double var22 = par1EntityPlayer.field_71091_bM
            + (par1EntityPlayer.field_71094_bP - par1EntityPlayer.field_71091_bM) * par2
            - (par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * par2);
         double var24 = par1EntityPlayer.field_71096_bN
            + (par1EntityPlayer.field_71095_bQ - par1EntityPlayer.field_71096_bN) * par2
            - (par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * par2);
         double var9 = par1EntityPlayer.field_71097_bO
            + (par1EntityPlayer.field_71085_bR - par1EntityPlayer.field_71097_bO) * par2
            - (par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * par2);
         float var11 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2;
         double var12 = MathHelper.sin(var11 * (float) Math.PI / 180.0F);
         double var14 = -MathHelper.cos(var11 * (float) Math.PI / 180.0F);
         float var16 = (float)var24 * 10.0F;
         if (var16 < -6.0F) {
            var16 = -6.0F;
         }

         if (var16 > 32.0F) {
            var16 = 32.0F;
         }

         float var17 = (float)(var22 * var12 + var9 * var14) * 100.0F;
         float var18 = (float)(var22 * var14 - var9 * var12) * 100.0F;
         if (var17 < 0.0F) {
            var17 = 0.0F;
         }

         float var19 = par1EntityPlayer.prevCameraYaw + (par1EntityPlayer.cameraYaw - par1EntityPlayer.prevCameraYaw) * par2;
         var16 += MathHelper.sin(
               (par1EntityPlayer.prevDistanceWalkedModified + (par1EntityPlayer.distanceWalkedModified - par1EntityPlayer.prevDistanceWalkedModified) * par2)
                  * 6.0F
            )
            * 32.0F
            * var19;
         if (par1EntityPlayer.ag()) {
            var16 += 25.0F;
         }

         GL11.glRotatef(6.0F + var17 / 2.0F + var16, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(var18 / 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(-var18 / 2.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         this.modelBipedMain.renderCloak(0.0625F);
         GL11.glPopMatrix();
      }

      ItemStack var21 = par1EntityPlayer.inventory.getCurrentItem();
      if (var21 != null) {
         GL11.glPushMatrix();
         this.modelBipedMain.bipedRightArm.postRender(0.0625F);
         GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
         if (par1EntityPlayer.fishEntity != null) {
            var21 = new ItemStack(Item.stick);
         }

         EnumAction var23 = null;
         if (par1EntityPlayer.getItemInUseCount() > 0) {
            var23 = var21.getItemUseAction();
         }

         if (var21.itemID < 4096 && Block.blocksList[var21.itemID] != null && Block.blocksList[var21.itemID].doesItemRenderAsBlock(var21.getItemDamage())) {
            float var7 = 0.5F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            var7 *= 0.75F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-var7, -var7, var7);
         } else if (var21.itemID == Item.bow.itemID || var21.itemID == BTWItems.compositeBow.itemID) {
            float var7 = 0.625F;
            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(var7, -var7, var7);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else if (Item.itemsList[var21.itemID].isFull3D()) {
            float var7 = 0.625F;
            if (Item.itemsList[var21.itemID].shouldRotateAroundWhenRendering()) {
               GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef(0.0F, -0.125F, 0.0F);
            }

            if (par1EntityPlayer.getItemInUseCount() > 0 && var23 == EnumAction.block) {
               GL11.glTranslatef(0.05F, 0.0F, -0.1F);
               GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
            GL11.glScalef(var7, -var7, var7);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else {
            float var7x = 0.375F;
            GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
            GL11.glScalef(var7x, var7x, var7x);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
         }

         if (var21.getItem().requiresMultipleRenderPasses()) {
            for (int var27 = 0; var27 <= 1; var27++) {
               int var26 = var21.getItem().getColorFromItemStack(var21, var27);
               float var28 = (var26 >> 16 & 0xFF) / 255.0F;
               float var10 = (var26 >> 8 & 0xFF) / 255.0F;
               float var11x = (var26 & 0xFF) / 255.0F;
               GL11.glColor4f(var28, var10, var11x, 1.0F);
               this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var21, var27);
            }
         } else {
            int var27 = var21.getItem().getColorFromItemStack(var21, 0);
            float var8 = (var27 >> 16 & 0xFF) / 255.0F;
            float var28 = (var27 >> 8 & 0xFF) / 255.0F;
            float var10 = (var27 & 0xFF) / 255.0F;
            GL11.glColor4f(var8, var28, var10, 1.0F);
            this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var21, 0);
         }

         GL11.glPopMatrix();
      }
   }

   protected void renderPlayerScale(EntityPlayer par1EntityPlayer, float par2) {
      float var3 = 0.9375F;
      GL11.glScalef(var3, var3, var3);
   }

   protected void func_96450_a(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, String par8Str, float par9, double par10) {
      if (!BTWMod.isHardcorePlayerNamesEnabled(par1EntityPlayer.worldObj)) {
         if (par10 < 100.0) {
            Scoreboard var12 = par1EntityPlayer.getWorldScoreboard();
            ScoreObjective var13 = var12.func_96539_a(2);
            if (var13 != null) {
               Score var14 = var12.func_96529_a(par1EntityPlayer.getEntityName(), var13);
               if (par1EntityPlayer.isPlayerSleeping()) {
                  this.a(par1EntityPlayer, var14.func_96652_c() + " " + var13.getDisplayName(), par2, par4 - 1.5, par6, 64);
               } else {
                  this.a(par1EntityPlayer, var14.func_96652_c() + " " + var13.getDisplayName(), par2, par4, par6, 64);
               }

               par4 += this.a().FONT_HEIGHT * 1.15F * par9;
            }
         }

         super.func_96449_a(par1EntityPlayer, par2, par4, par6, par8Str, par9, par10);
      }
   }

   public void renderFirstPersonArm(EntityPlayer par1EntityPlayer) {
      float var2 = 1.0F;
      GL11.glColor3f(var2, var2, var2);
      this.modelBipedMain.onGround = 0.0F;
      this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, par1EntityPlayer);
      this.modelBipedMain.bipedRightArm.render(0.0625F);
   }

   protected void renderPlayerSleep(EntityPlayer par1EntityPlayer, double par2, double par4, double par6) {
      if (par1EntityPlayer.R() && par1EntityPlayer.isPlayerSleeping()) {
         super.renderLivingAt(
            par1EntityPlayer, par2 + par1EntityPlayer.field_71079_bU, par4 + par1EntityPlayer.field_71082_cx, par6 + par1EntityPlayer.field_71089_bV
         );
      } else {
         super.renderLivingAt(par1EntityPlayer, par2, par4, par6);
      }
   }

   protected void rotatePlayer(EntityPlayer par1EntityPlayer, float par2, float par3, float par4) {
      if (par1EntityPlayer.R() && par1EntityPlayer.isPlayerSleeping()) {
         GL11.glRotatef(par1EntityPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.b(par1EntityPlayer), 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
      } else {
         super.rotateCorpse(par1EntityPlayer, par2, par3, par4);
      }
   }

   @Override
   protected void func_96449_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, String par8Str, float par9, double par10) {
      this.func_96450_a((EntityPlayer)par1EntityLiving, par2, par4, par6, par8Str, par9, par10);
   }

   @Override
   protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
      this.renderPlayerScale((EntityPlayer)par1EntityLiving, par2);
   }

   @Override
   protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
      this.func_82439_b((EntityPlayer)par1EntityLiving, par2, par3);
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return this.setArmorModel((EntityPlayer)par1EntityLiving, par2, par3);
   }

   @Override
   protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
      this.renderSpecials((EntityPlayer)par1EntityLiving, par2);
   }

   @Override
   protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
      this.rotatePlayer((EntityPlayer)par1EntityLiving, par2, par3, par4);
   }

   @Override
   protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
      this.renderPlayerSleep((EntityPlayer)par1EntityLiving, par2, par4, par6);
   }

   @Override
   protected void func_98190_a(EntityLiving par1EntityLiving) {
      this.func_98191_a((EntityPlayer)par1EntityLiving);
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderPlayer((EntityPlayer)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderPlayer((EntityPlayer)par1Entity, par2, par4, par6, par8, par9);
   }

   private int shouldRenderPassModArmor(ItemStack stack, int iArmorSlot, ArmorItemMod armorItem) {
      this.a(armorItem.getWornTextureDirectory() + armorItem.getWornTexturePrefix() + "_" + (iArmorSlot == 2 ? 2 : 1) + ".png");
      ModelBiped model = iArmorSlot == 2 ? this.modelArmor : this.modelArmorChestplate;
      model.bipedHead.showModel = iArmorSlot == 0;
      model.bipedHeadwear.showModel = iArmorSlot == 0;
      model.bipedBody.showModel = iArmorSlot == 1 || iArmorSlot == 2;
      model.bipedRightArm.showModel = iArmorSlot == 1;
      model.bipedLeftArm.showModel = iArmorSlot == 1;
      model.bipedRightLeg.showModel = iArmorSlot == 2 || iArmorSlot == 3;
      model.bipedLeftLeg.showModel = iArmorSlot == 2 || iArmorSlot == 3;
      this.a(model);
      if (model != null) {
         model.onGround = this.mainModel.onGround;
         model.isRiding = this.mainModel.isRiding;
         model.isChild = this.mainModel.isChild;
      }

      if (armorItem.hasCustomColors()) {
         int iColor = armorItem.getColor(stack);
         float fRed = (iColor >> 16 & 0xFF) / 255.0F;
         float fGreen = (iColor >> 8 & 0xFF) / 255.0F;
         float fBlue = (iColor & 0xFF) / 255.0F;
         GL11.glColor3f(fRed, fGreen, fBlue);
      } else {
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }

      if (armorItem.hasSecondRenderLayerWhenWorn()) {
         return stack.isItemEnchanted() ? 31 : 16;
      } else {
         return stack.isItemEnchanted() ? 15 : 1;
      }
   }

   private void loadSecondLayerOfModArmorTexture(int iArmorSlot, ArmorItemMod armorItem) {
      this.a(armorItem.getWornTextureDirectory() + armorItem.getWornTexturePrefix() + "_" + (iArmorSlot == 2 ? 2 : 1) + "_b.png");
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
   }
}
