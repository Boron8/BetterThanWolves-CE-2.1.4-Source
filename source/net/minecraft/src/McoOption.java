package net.minecraft.src;

public abstract class McoOption {
   public abstract Object func_98155_a();

   public static McoOptionSome func_98153_a(Object var0) {
      return new McoOptionSome(var0);
   }

   public static McoOptionNone func_98154_b() {
      return new McoOptionNone();
   }
}
