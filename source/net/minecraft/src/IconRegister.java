package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IconRegister {
   Icon registerIcon(String var1);

   Icon registerIcon(String var1, TextureStitched var2);
}
