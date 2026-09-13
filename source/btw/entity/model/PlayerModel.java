package btw.entity.model;

import btw.util.status.BTWStatusCategory;
import btw.util.status.StatusEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;

@Environment(EnvType.CLIENT)
public class PlayerModel extends ModelBiped {
   public PlayerModel() {
   }

   public PlayerModel(float par1) {
      super(par1);
   }

   public PlayerModel(float par1, float par2, int par3, int par4) {
      super(par1, par2, par3, par4);
   }

   @Override
   public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      EntityPlayer player = (EntityPlayer)entity;
      int iFatPenalty = player.getStatusForCategory(BTWStatusCategory.FAT).map(StatusEffect::getLevel).orElse(0);
      if (iFatPenalty == 0) {
         super.render(entity, par2, par3, par4, par5, par6, par7);
      } else {
         this.a(par2, par3, par4, par5, par6, par7, entity);
         this.bipedHead.render(par7);
         if (iFatPenalty == 1) {
            this.bipedBody.renderWithScaleToBaseModel(par7, 1.125F, 1.0F, 1.625F);
         } else if (iFatPenalty == 2) {
            this.bipedBody.renderWithScaleToBaseModel(par7, 1.25F, 1.0F, 2.25F);
         } else if (iFatPenalty == 3) {
            this.bipedBody.renderWithScaleToBaseModel(par7, 1.375F, 1.0F, 2.875F);
         } else {
            this.bipedBody.renderWithScaleToBaseModel(par7, 1.5F, 1.0F, 3.5F);
         }

         this.bipedRightArm.render(par7);
         this.bipedLeftArm.render(par7);
         this.bipedRightLeg.render(par7);
         this.bipedLeftLeg.render(par7);
         this.bipedHeadwear.render(par7);
      }
   }
}
