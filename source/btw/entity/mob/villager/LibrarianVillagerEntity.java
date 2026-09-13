package btw.entity.mob.villager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

public class LibrarianVillagerEntity extends VillagerEntity {
   public LibrarianVillagerEntity(World world) {
      super(world, 1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.getCurrentTradeLevel() >= 5 ? "/btwmodtex/fcLibrarianSpecs.png" : "/mob/villager/librarian.png";
   }
}
