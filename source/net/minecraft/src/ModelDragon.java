package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelDragon extends ModelBase {
   private ModelRenderer head;
   private ModelRenderer neck;
   private ModelRenderer jaw;
   private ModelRenderer body;
   private ModelRenderer rearLeg;
   private ModelRenderer frontLeg;
   private ModelRenderer rearLegTip;
   private ModelRenderer frontLegTip;
   private ModelRenderer rearFoot;
   private ModelRenderer frontFoot;
   private ModelRenderer wing;
   private ModelRenderer wingTip;
   private float partialTicks;

   public ModelDragon(float var1) {
      this.textureWidth = 256;
      this.textureHeight = 256;
      this.a("body.body", 0, 0);
      this.a("wing.skin", -56, 88);
      this.a("wingtip.skin", -56, 144);
      this.a("rearleg.main", 0, 0);
      this.a("rearfoot.main", 112, 0);
      this.a("rearlegtip.main", 196, 0);
      this.a("head.upperhead", 112, 30);
      this.a("wing.bone", 112, 88);
      this.a("head.upperlip", 176, 44);
      this.a("jaw.jaw", 176, 65);
      this.a("frontleg.main", 112, 104);
      this.a("wingtip.bone", 112, 136);
      this.a("frontfoot.main", 144, 104);
      this.a("neck.box", 192, 104);
      this.a("frontlegtip.main", 226, 138);
      this.a("body.scale", 220, 53);
      this.a("head.scale", 0, 0);
      this.a("neck.scale", 48, 0);
      this.a("head.nostril", 112, 0);
      float var2 = -16.0F;
      this.head = new ModelRenderer(this, "head");
      this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + var2, 12, 5, 16);
      this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + var2, 16, 16, 16);
      this.head.mirror = true;
      this.head.addBox("scale", -5.0F, -12.0F, 12.0F + var2, 2, 4, 6);
      this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + var2, 2, 2, 4);
      this.head.mirror = false;
      this.head.addBox("scale", 3.0F, -12.0F, 12.0F + var2, 2, 4, 6);
      this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + var2, 2, 2, 4);
      this.jaw = new ModelRenderer(this, "jaw");
      this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + var2);
      this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
      this.head.addChild(this.jaw);
      this.neck = new ModelRenderer(this, "neck");
      this.neck.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
      this.neck.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
      this.body = new ModelRenderer(this, "body");
      this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
      this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
      this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
      this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
      this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
      this.wing = new ModelRenderer(this, "wing");
      this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
      this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
      this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
      this.wingTip = new ModelRenderer(this, "wingtip");
      this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
      this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
      this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
      this.wing.addChild(this.wingTip);
      this.frontLeg = new ModelRenderer(this, "frontleg");
      this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
      this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
      this.frontLegTip = new ModelRenderer(this, "frontlegtip");
      this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
      this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
      this.frontLeg.addChild(this.frontLegTip);
      this.frontFoot = new ModelRenderer(this, "frontfoot");
      this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
      this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
      this.frontLegTip.addChild(this.frontFoot);
      this.rearLeg = new ModelRenderer(this, "rearleg");
      this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
      this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
      this.rearLegTip = new ModelRenderer(this, "rearlegtip");
      this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
      this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
      this.rearLeg.addChild(this.rearLegTip);
      this.rearFoot = new ModelRenderer(this, "rearfoot");
      this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
      this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
      this.rearLegTip.addChild(this.rearFoot);
   }

   @Override
   public void setLivingAnimations(EntityLiving var1, float var2, float var3, float var4) {
      this.partialTicks = var4;
   }

   @Override
   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      GL11.glPushMatrix();
      EntityDragon var8 = (EntityDragon)var1;
      float var9 = var8.prevAnimTime + (var8.animTime - var8.prevAnimTime) * this.partialTicks;
      this.jaw.rotateAngleX = (float)(Math.sin(var9 * (float) Math.PI * 2.0F) + 1.0) * 0.2F;
      float var10 = (float)(Math.sin(var9 * (float) Math.PI * 2.0F - 1.0F) + 1.0);
      var10 = (var10 * var10 * 1.0F + var10 * 2.0F) * 0.05F;
      GL11.glTranslatef(0.0F, var10 - 2.0F, -3.0F);
      GL11.glRotatef(var10 * 2.0F, 1.0F, 0.0F, 0.0F);
      float var11 = -30.0F;
      float var13 = 0.0F;
      float var14 = 1.5F;
      double[] var15 = var8.getMovementOffsets(6, this.partialTicks);
      float var16 = this.updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] - var8.getMovementOffsets(10, this.partialTicks)[0]);
      float var17 = this.updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] + var16 / 2.0F);
      var11 += 2.0F;
      float var18 = var9 * (float) Math.PI * 2.0F;
      var11 = 20.0F;
      float var12 = -12.0F;

      for (int var19 = 0; var19 < 5; var19++) {
         double[] var20 = var8.getMovementOffsets(5 - var19, this.partialTicks);
         float var21 = (float)Math.cos(var19 * 0.45F + var18) * 0.15F;
         this.neck.rotateAngleY = this.updateRotations(var20[0] - var15[0]) * (float) Math.PI / 180.0F * var14;
         this.neck.rotateAngleX = var21 + (float)(var20[1] - var15[1]) * (float) Math.PI / 180.0F * var14 * 5.0F;
         this.neck.rotateAngleZ = -this.updateRotations(var20[0] - var17) * (float) Math.PI / 180.0F * var14;
         this.neck.rotationPointY = var11;
         this.neck.rotationPointZ = var12;
         this.neck.rotationPointX = var13;
         var11 = (float)(var11 + Math.sin(this.neck.rotateAngleX) * 10.0);
         var12 = (float)(var12 - Math.cos(this.neck.rotateAngleY) * Math.cos(this.neck.rotateAngleX) * 10.0);
         var13 = (float)(var13 - Math.sin(this.neck.rotateAngleY) * Math.cos(this.neck.rotateAngleX) * 10.0);
         this.neck.render(var7);
      }

      this.head.rotationPointY = var11;
      this.head.rotationPointZ = var12;
      this.head.rotationPointX = var13;
      double[] var30 = var8.getMovementOffsets(0, this.partialTicks);
      this.head.rotateAngleY = this.updateRotations(var30[0] - var15[0]) * (float) Math.PI / 180.0F * 1.0F;
      this.head.rotateAngleZ = -this.updateRotations(var30[0] - var17) * (float) Math.PI / 180.0F * 1.0F;
      this.head.render(var7);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-var16 * var14 * 1.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      this.body.rotateAngleZ = 0.0F;
      this.body.render(var7);

      for (int var32 = 0; var32 < 2; var32++) {
         GL11.glEnable(2884);
         float var34 = var9 * (float) Math.PI * 2.0F;
         this.wing.rotateAngleX = 0.125F - (float)Math.cos(var34) * 0.2F;
         this.wing.rotateAngleY = 0.25F;
         this.wing.rotateAngleZ = (float)(Math.sin(var34) + 0.125) * 0.8F;
         this.wingTip.rotateAngleZ = -((float)(Math.sin(var34 + 2.0F) + 0.5)) * 0.75F;
         this.rearLeg.rotateAngleX = 1.0F + var10 * 0.1F;
         this.rearLegTip.rotateAngleX = 0.5F + var10 * 0.1F;
         this.rearFoot.rotateAngleX = 0.75F + var10 * 0.1F;
         this.frontLeg.rotateAngleX = 1.3F + var10 * 0.1F;
         this.frontLegTip.rotateAngleX = -0.5F - var10 * 0.1F;
         this.frontFoot.rotateAngleX = 0.75F + var10 * 0.1F;
         this.wing.render(var7);
         this.frontLeg.render(var7);
         this.rearLeg.render(var7);
         GL11.glScalef(-1.0F, 1.0F, 1.0F);
         if (var32 == 0) {
            GL11.glCullFace(1028);
         }
      }

      GL11.glPopMatrix();
      GL11.glCullFace(1029);
      GL11.glDisable(2884);
      float var33 = -((float)Math.sin(var9 * (float) Math.PI * 2.0F)) * 0.0F;
      var18 = var9 * (float) Math.PI * 2.0F;
      var11 = 10.0F;
      var12 = 60.0F;
      var13 = 0.0F;
      var15 = var8.getMovementOffsets(11, this.partialTicks);

      for (int var35 = 0; var35 < 12; var35++) {
         var30 = var8.getMovementOffsets(12 + var35, this.partialTicks);
         var33 = (float)(var33 + Math.sin(var35 * 0.45F + var18) * 0.05F);
         this.neck.rotateAngleY = (this.updateRotations(var30[0] - var15[0]) * var14 + 180.0F) * (float) Math.PI / 180.0F;
         this.neck.rotateAngleX = var33 + (float)(var30[1] - var15[1]) * (float) Math.PI / 180.0F * var14 * 5.0F;
         this.neck.rotateAngleZ = this.updateRotations(var30[0] - var17) * (float) Math.PI / 180.0F * var14;
         this.neck.rotationPointY = var11;
         this.neck.rotationPointZ = var12;
         this.neck.rotationPointX = var13;
         var11 = (float)(var11 + Math.sin(this.neck.rotateAngleX) * 10.0);
         var12 = (float)(var12 - Math.cos(this.neck.rotateAngleY) * Math.cos(this.neck.rotateAngleX) * 10.0);
         var13 = (float)(var13 - Math.sin(this.neck.rotateAngleY) * Math.cos(this.neck.rotateAngleX) * 10.0);
         this.neck.render(var7);
      }

      GL11.glPopMatrix();
   }

   private float updateRotations(double var1) {
      while (var1 >= 180.0) {
         var1 -= 360.0;
      }

      while (var1 < -180.0) {
         var1 += 360.0;
      }

      return (float)var1;
   }
}
