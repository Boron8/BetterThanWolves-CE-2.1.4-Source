package net.minecraft.src;

import btw.item.items.ArmorItemMod;
import com.prupe.mcpatcher.cit.CITUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderBiped extends RenderLiving {
   protected ModelBiped modelBipedMain;
   protected float field_77070_b;
   protected ModelBiped field_82423_g;
   protected ModelBiped field_82425_h;
   private static final String[] bipedArmorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

   public RenderBiped(ModelBiped par1ModelBiped, float par2) {
      this(par1ModelBiped, par2, 1.0F);
   }

   public RenderBiped(ModelBiped par1ModelBiped, float par2, float par3) {
      super(par1ModelBiped, par2);
      this.modelBipedMain = par1ModelBiped;
      this.field_77070_b = par3;
      this.func_82421_b();
   }

   protected void func_82421_b() {
      this.field_82423_g = new ModelBiped(1.0F);
      this.field_82425_h = new ModelBiped(0.5F);
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      ItemStack var4 = par1EntityLiving.getCurrentArmor(3 - par2);
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
                     FakeResourceLocation.wrap("/armor/" + bipedArmorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + ".png"),
                     par1EntityLiving,
                     var4
                  )
               )
            );
            ModelBiped var7 = par2 == 2 ? this.field_82425_h : this.field_82423_g;
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

   @Override
   protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
      ItemStack var4 = par1EntityLiving.getCurrentArmor(3 - par2);
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
                     FakeResourceLocation.wrap("/armor/" + bipedArmorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + "_b.png"),
                     par1EntityLiving,
                     var4
                  )
               )
            );
            float var7 = 1.0F;
            GL11.glColor3f(var7, var7, var7);
         }
      }
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      float var10 = 1.0F;
      GL11.glColor3f(var10, var10, var10);
      ItemStack var11 = par1EntityLiving.getHeldItem();
      this.func_82420_a(par1EntityLiving, var11);
      double var12 = par4 - par1EntityLiving.yOffset;
      if (par1EntityLiving.ag() && !(par1EntityLiving instanceof EntityPlayerSP)) {
         var12 -= 0.125;
      }

      super.doRenderLiving(par1EntityLiving, par2, var12, par6, par8, par9);
      this.field_82423_g.aimedBow = this.field_82425_h.aimedBow = this.modelBipedMain.aimedBow = false;
      this.field_82423_g.isSneak = this.field_82425_h.isSneak = this.modelBipedMain.isSneak = false;
      this.field_82423_g.heldItemRight = this.field_82425_h.heldItemRight = this.modelBipedMain.heldItemRight = 0;
   }

   protected void func_82420_a(EntityLiving par1EntityLiving, ItemStack par2ItemStack) {
      this.field_82423_g.heldItemRight = this.field_82425_h.heldItemRight = this.modelBipedMain.heldItemRight = par2ItemStack != null ? 1 : 0;
      this.field_82423_g.isSneak = this.field_82425_h.isSneak = this.modelBipedMain.isSneak = par1EntityLiving.ag();
   }

   @Override
   protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
      float var3 = 1.0F;
      GL11.glColor3f(var3, var3, var3);
      super.renderEquippedItems(par1EntityLiving, par2);
      ItemStack var4 = par1EntityLiving.getHeldItem();
      ItemStack var5 = par1EntityLiving.getCurrentArmor(3);
      if (var5 != null) {
         GL11.glPushMatrix();
         this.modelBipedMain.bipedHead.postRender(0.0625F);
         if (var5.getItem().itemID < 4096 && Block.blocksList[var5.itemID] != null) {
            if (Block.blocksList[var5.itemID].doesItemRenderAsBlock(var5.getItemDamage())) {
               float var6 = 0.625F;
               GL11.glTranslatef(0.0F, -0.25F, 0.0F);
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glScalef(var6, -var6, -var6);
            }

            this.renderManager.itemRenderer.renderItem(par1EntityLiving, var5, 0);
         } else if (var5.getItem().itemID == Item.skull.itemID) {
            float var6 = 1.0625F;
            GL11.glScalef(var6, -var6, -var6);
            String var7 = "";
            if (var5.hasTagCompound() && var5.getTagCompound().hasKey("SkullOwner")) {
               var7 = var5.getTagCompound().getString("SkullOwner");
            }

            TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, var5.getItemDamage(), var7);
         }

         GL11.glPopMatrix();
      }

      if (var4 != null) {
         GL11.glPushMatrix();
         if (this.mainModel.isChild) {
            float var6 = 0.5F;
            GL11.glTranslatef(0.0F, 0.625F, 0.0F);
            GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
            GL11.glScalef(var6, var6, var6);
         }

         this.modelBipedMain.bipedRightArm.postRender(0.0625F);
         GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
         if (var4.itemID < 4096 && Block.blocksList[var4.itemID] != null && Block.blocksList[var4.itemID].doesItemRenderAsBlock(var4.getItemDamage())) {
            float var6 = 0.5F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            var6 *= 0.75F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-var6, -var6, var6);
         } else if (var4.itemID == Item.bow.itemID) {
            float var6 = 0.625F;
            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(var6, -var6, var6);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else if (Item.itemsList[var4.itemID].isFull3D()) {
            float var6 = 0.625F;
            if (Item.itemsList[var4.itemID].shouldRotateAroundWhenRendering()) {
               GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef(0.0F, -0.125F, 0.0F);
            }

            this.func_82422_c();
            GL11.glScalef(var6, -var6, var6);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         } else {
            float var6 = 0.375F;
            GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
            GL11.glScalef(var6, var6, var6);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
         }

         this.renderManager.itemRenderer.renderItem(par1EntityLiving, var4, 0);
         if (var4.getItem().requiresMultipleRenderPasses()) {
            this.renderManager.itemRenderer.renderItem(par1EntityLiving, var4, 1);
         }

         GL11.glPopMatrix();
      }
   }

   protected void func_82422_c() {
      GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
   }

   private int shouldRenderPassModArmor(ItemStack stack, int iArmorSlot, ArmorItemMod armorItem) {
      this.a(armorItem.getWornTextureDirectory() + armorItem.getWornTexturePrefix() + "_" + (iArmorSlot == 2 ? 2 : 1) + ".png");
      ModelBiped model = iArmorSlot == 2 ? this.field_82425_h : this.field_82423_g;
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
