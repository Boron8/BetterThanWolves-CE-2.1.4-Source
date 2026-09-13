package btw.entity.mob.villager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class FarmerVillagerEntity extends VillagerEntity {
   public FarmerVillagerEntity(World world) {
      super(world, 0);
   }

   @Override
   public int getDirtyPeasant() {
      return this.dataWatcher.getWatchableObjectInt(26);
   }

   @Override
   public void setDirtyPeasant(int iDirtyPeasant) {
      this.dataWatcher.updateObject(26, iDirtyPeasant);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.getDirtyPeasant() > 0 ? "/btwmodtex/fcDirtyPeasant.png" : "/mob/villager/farmer.png";
   }
}
