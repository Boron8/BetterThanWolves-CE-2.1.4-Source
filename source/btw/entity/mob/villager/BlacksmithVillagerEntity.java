package btw.entity.mob.villager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class BlacksmithVillagerEntity extends VillagerEntity {
   public BlacksmithVillagerEntity(World world) {
      super(world, 3);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return "/mob/villager/smith.png";
   }
}
