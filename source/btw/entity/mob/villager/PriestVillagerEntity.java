package btw.entity.mob.villager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class PriestVillagerEntity extends VillagerEntity {
   public PriestVillagerEntity(World world) {
      super(world, 2);
      registerEffectForLevelUp(2, 5, new VillagerEntity.TradeEffect() {
         @Override
         public void playEffect(VillagerEntity villager) {
            villager.worldObj.playSoundAtEntity(villager, "mob.enderdragon.growl", 1.0F, 0.5F);
            villager.worldObj.playSoundAtEntity(villager, "ambient.weather.thunder", 1.0F, PriestVillagerEntity.this.rand.nextFloat() * 0.4F + 0.8F);
            villager.worldObj.playSoundAtEntity(villager, "random.levelup", 0.75F + PriestVillagerEntity.this.rand.nextFloat() * 0.25F, 0.5F);
         }
      });
   }

   @Override
   protected void spawnCustomParticles() {
      if (this.getCurrentTradeLevel() >= 5) {
         this.worldObj
            .spawnParticle(
               "portal",
               this.posX + (this.rand.nextDouble() - 0.5) * this.width,
               this.posY + this.rand.nextDouble() * this.height - 0.25,
               this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
               (this.rand.nextDouble() - 0.5) * 2.0,
               -this.rand.nextDouble(),
               (this.rand.nextDouble() - 0.5) * 2.0
            );
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.getCurrentTradeLevel() >= 5 ? "/btwmodtex/fcPriestLvl.png" : "/mob/villager/priest.png";
   }
}
