package btw.entity.mob.villager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class ButcherVillagerEntity extends VillagerEntity {
   public ButcherVillagerEntity(World world) {
      super(world, 4);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.getCurrentTradeLevel() >= 4 ? "/btwmodtex/fcButcherLvl.png" : "/mob/villager/butcher.png";
   }
}
