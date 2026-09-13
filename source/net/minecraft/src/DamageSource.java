package net.minecraft.src;

public class DamageSource {
   public static DamageSource inFire = new DamageSource("inFire").setFireDamage();
   public static DamageSource onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
   public static DamageSource lava = new DamageSource("lava").setFireDamage();
   public static DamageSource inWall = new DamageSource("inWall").setDamageBypassesArmor();
   public static DamageSource drown = new DamageSource("drown").setDamageBypassesArmor();
   public static DamageSource starve = new DamageSource("starve").setDamageBypassesArmor();
   public static DamageSource cactus = new DamageSource("cactus");
   public static DamageSource fall = new DamageSource("fall").setDamageBypassesArmor();
   public static DamageSource outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
   public static DamageSource generic = new DamageSource("generic").setDamageBypassesArmor();
   public static DamageSource magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
   public static DamageSource wither = new DamageSource("wither").setDamageBypassesArmor();
   public static DamageSource anvil = new DamageSource("anvil");
   public static DamageSource fallingBlock = new DamageSource("fallingBlock");
   private boolean isUnblockable = false;
   private boolean isDamageAllowedInCreativeMode = false;
   private float hungerDamage = 0.3F;
   private boolean fireDamage;
   private boolean projectile;
   private boolean difficultyScaled;
   private boolean magicDamage = false;
   private boolean explosion = false;
   public String damageType;

   public static DamageSource causeMobDamage(EntityLiving par0EntityLiving) {
      return new EntityDamageSource("mob", par0EntityLiving);
   }

   public static DamageSource causePlayerDamage(EntityPlayer par0EntityPlayer) {
      return new EntityDamageSource("player", par0EntityPlayer);
   }

   public static DamageSource causeArrowDamage(EntityArrow par0EntityArrow, Entity par1Entity) {
      return new EntityDamageSourceIndirect("arrow", par0EntityArrow, par1Entity).b();
   }

   public static DamageSource causeFireballDamage(EntityFireball par0EntityFireball, Entity par1Entity) {
      return par1Entity == null
         ? new EntityDamageSourceIndirect("onFire", par0EntityFireball, par0EntityFireball).l().setProjectile()
         : new EntityDamageSourceIndirect("fireball", par0EntityFireball, par1Entity).l().setProjectile();
   }

   public static DamageSource causeThrownDamage(Entity par0Entity, Entity par1Entity) {
      return new EntityDamageSourceIndirect("thrown", par0Entity, par1Entity).b();
   }

   public static DamageSource causeIndirectMagicDamage(Entity par0Entity, Entity par1Entity) {
      return new EntityDamageSourceIndirect("indirectMagic", par0Entity, par1Entity).j().setMagicDamage();
   }

   public static DamageSource causeThornsDamage(Entity par0Entity) {
      return new EntityDamageSource("thorns", par0Entity).r();
   }

   public static DamageSource setExplosionSource(Explosion par0Explosion) {
      return par0Explosion != null && par0Explosion.func_94613_c() != null
         ? new EntityDamageSource("explosion.player", par0Explosion.func_94613_c()).o().setExplosion()
         : new DamageSource("explosion").setDifficultyScaled().setExplosion();
   }

   public boolean isProjectile() {
      return this.projectile;
   }

   public DamageSource setProjectile() {
      this.projectile = true;
      return this;
   }

   public boolean isExplosion() {
      return this.explosion;
   }

   public DamageSource setExplosion() {
      this.explosion = true;
      return this;
   }

   public boolean isUnblockable() {
      return this.isUnblockable;
   }

   public float getHungerDamage() {
      return this.hungerDamage;
   }

   public boolean canHarmInCreative() {
      return this.isDamageAllowedInCreativeMode;
   }

   protected DamageSource(String par1Str) {
      this.damageType = par1Str;
   }

   public Entity getSourceOfDamage() {
      return this.getEntity();
   }

   public Entity getEntity() {
      return null;
   }

   public DamageSource setDamageBypassesArmor() {
      this.isUnblockable = true;
      this.hungerDamage = 0.0F;
      return this;
   }

   public DamageSource setDamageAllowedInCreativeMode() {
      this.isDamageAllowedInCreativeMode = true;
      return this;
   }

   public DamageSource setFireDamage() {
      this.fireDamage = true;
      return this;
   }

   public String getDeathMessage(EntityLiving par1EntityLiving) {
      EntityLiving var2 = par1EntityLiving.func_94060_bK();
      String var3 = "death.attack." + this.damageType;
      String var4 = var3 + ".player";
      return var2 != null && StatCollector.func_94522_b(var4)
         ? StatCollector.translateToLocalFormatted(var4, par1EntityLiving.ax(), var2.ax())
         : StatCollector.translateToLocalFormatted(var3, par1EntityLiving.ax());
   }

   public boolean isFireDamage() {
      return this.fireDamage;
   }

   public String getDamageType() {
      return this.damageType;
   }

   public DamageSource setDifficultyScaled() {
      this.difficultyScaled = true;
      return this;
   }

   public boolean isDifficultyScaled() {
      return this.difficultyScaled;
   }

   public boolean isMagicDamage() {
      return this.magicDamage;
   }

   public DamageSource setMagicDamage() {
      this.magicDamage = true;
      return this;
   }
}
