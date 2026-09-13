package btw.entity.mob;

import btw.entity.LightningBoltEntity;
import btw.entity.mob.behavior.CreeperSwellBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.item.BTWItems;
import btw.item.items.ShearsItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityAICreeperSwell;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class CreeperEntity extends EntityCreeper {
   private static final int NEUTERED_STATE_DATA_WATCHER_ID = 25;
   private boolean determinedToExplode = false;

   public CreeperEntity(World world) {
      super(world);
      this.tasks.removeAllTasksOfClass(EntityAICreeperSwell.class);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.tasks.addTask(2, new CreeperSwellBehavior(this));
      this.tasks.addTask(5, new SimpleWanderBehavior(this, 0.2F));
   }

   @Override
   protected void fall(float par1) {
      this.entityLivingFall(par1);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(25, new Byte((byte)0));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      tag.setByte("fcNeuteredState", (byte)this.getNeuteredState());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("fcNeuteredState")) {
         this.setNeuteredState(tag.getByte("fcNeuteredState"));
      }
   }

   @Override
   public void onDeath(DamageSource source) {
      if (this.getNeuteredState() == 0) {
         super.onDeath(source);
      } else {
         this.entityLivingOnDeath(source);
      }
   }

   @Override
   protected int getDropItemId() {
      return BTWItems.nitre.itemID;
   }

   @Override
   protected void dropHead() {
      this.a(new ItemStack(Item.skull.itemID, 1, 4), 0.0F);
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iFortuneModifier) {
      super.a(bKilledByPlayer, iFortuneModifier);
      if (this.getNeuteredState() == 0 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + iFortuneModifier) > 0)) {
         this.b(BTWItems.creeperOysters.itemID, 1);
      }
   }

   @Override
   public boolean interact(EntityPlayer player) {
      ItemStack playersCurrentItem = player.inventory.getCurrentItem();
      if (playersCurrentItem != null && playersCurrentItem.getItem() instanceof ShearsItem && this.getNeuteredState() == 0) {
         if (!this.worldObj.isRemote) {
            this.setNeuteredState(1);
            EntityItem oysterItem = this.a(new ItemStack(BTWItems.creeperOysters, 1), 0.25F);
            oysterItem.motionY = oysterItem.motionY + this.rand.nextFloat() * 0.025F;
            oysterItem.motionX = oysterItem.motionX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            oysterItem.motionZ = oysterItem.motionZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);
            this.worldObj.playAuxSFX(2258, i, j, k, 0);
         }

         playersCurrentItem.damageItem(10, player);
         if (playersCurrentItem.stackSize <= 0) {
            player.inventory.mainInventory[player.inventory.currentItem] = null;
         }

         return true;
      } else {
         return super.a_(player);
      }
   }

   @Override
   public int getTalkInterval() {
      return 120;
   }

   @Override
   public void playLivingSound() {
      if (this.getNeuteredState() > 0) {
         String var1 = this.getLivingSound();
         if (var1 != null) {
            this.a(var1, 0.25F, this.aY() + 0.25F);
         }
      } else {
         super.aR();
      }
   }

   @Override
   protected String getLivingSound() {
      return this.getNeuteredState() > 0 ? "mob.creeper.say" : super.bb();
   }

   @Override
   public void onKickedByCow(CowEntity cow) {
      this.determinedToExplode = true;
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.blastProtection.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   public void onStruckByLightning(LightningBoltEntity entityBolt) {
      this.dataWatcher.updateObject(17, (byte)1);
   }

   public boolean getIsDeterminedToExplode() {
      return this.determinedToExplode;
   }

   public int getNeuteredState() {
      return this.dataWatcher.getWatchableObjectByte(25);
   }

   public void setNeuteredState(int iNeuteredState) {
      this.dataWatcher.updateObject(25, (byte)iNeuteredState);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.getNeuteredState() > 0 ? "/btwmodtex/fcCreeperDepressed.png" : super.N();
   }
}
