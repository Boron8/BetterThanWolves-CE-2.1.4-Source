package btw.client.render.entity;

import btw.entity.mob.SpiderEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderSpider;

@Environment(EnvType.CLIENT)
public class SpiderRenderer extends RenderSpider {
   @Override
   protected int shouldRenderPass(EntityLiving entity, int iRenderPass, float par3) {
      SpiderEntity spider = (SpiderEntity)entity;
      return !spider.doEyesGlow() ? -1 : this.a(spider, iRenderPass, par3);
   }
}
