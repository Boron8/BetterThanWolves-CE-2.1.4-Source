package btw.client.fx;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

@Environment(EnvType.CLIENT)
public class EffectHandler {
   public static Map<Integer, EffectHandler.Effect> effectMap = new HashMap<>();

   public static boolean playEffect(int effectID, Minecraft mcInstance, World world, EntityPlayer player, int x, int y, int z, int data) {
      EffectHandler.Effect effect = effectMap.get(effectID);
      if (effect != null) {
         effect.playEffect(mcInstance, world, player, x + 0.5, y + 0.5, z + 0.5, data);
         return true;
      } else {
         return false;
      }
   }

   public interface Effect {
      void playEffect(Minecraft var1, World var2, EntityPlayer var3, double var4, double var6, double var8, int var10);
   }
}
