package btw.entity.mob;

import btw.world.util.WorldUtils;
import net.minecraft.src.EntityList;
import net.minecraft.src.World;

public class WitherEntityPersistent extends WitherEntity {
   public WitherEntityPersistent(World world) {
      super(world);
   }

   public static void summonWitherAtLocation(World world, int i, int j, int k) {
      WitherEntity wither = (WitherEntity)EntityList.createEntityOfType(WitherEntity.class, world);
      wither.b(i + 0.5, j - 1.45, k + 0.5, 0.0F, 0.0F);
      wither.m();
      world.spawnEntityInWorld(wither);
      world.playAuxSFX(2279, i, j, k, 0);
      WorldUtils.gameProgressSetWitherHasBeenSummonedServerOnly();
   }
}
