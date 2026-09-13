package btw.item.items;

import btw.entity.BroadheadArrowEntity;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class CompositeBowItem extends BowItem {
   private static int maxDamage = 576;
   @Environment(EnvType.CLIENT)
   private Icon[] drawIconArray = new Icon[3];

   public CompositeBowItem(int iItemID) {
      super(iItemID);
      this.e(maxDamage);
      this.b("fcItemBowComposite");
   }

   @Override
   public boolean canItemBeFiredAsArrow(int iItemID) {
      return iItemID == Item.arrow.itemID || iItemID == BTWItems.rottenArrow.itemID || iItemID == BTWItems.broadheadArrow.itemID;
   }

   @Override
   public float getPullStrengthToArrowVelocityMultiplier() {
      return 3.0F;
   }

   @Override
   protected EntityArrow createArrowEntityForItem(World world, EntityPlayer player, int iItemID, float fPullStrength) {
      if (iItemID == BTWItems.broadheadArrow.itemID) {
         return (EntityArrow)EntityList.createEntityOfType(
            BroadheadArrowEntity.class, world, player, fPullStrength * this.getPullStrengthToArrowVelocityMultiplier()
         );
      } else if (iItemID != BTWItems.rottenArrow.itemID) {
         return super.createArrowEntityForItem(world, player, iItemID, fPullStrength);
      } else {
         world.playSoundAtEntity(player, "random.break", 0.8F, 0.8F + world.rand.nextFloat() * 0.4F);
         if (world.isRemote) {
            float motionX = -MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI)
               * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI)
               * fPullStrength;
            float motionZ = MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI)
               * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI)
               * fPullStrength;
            float motionY = -MathHelper.sin(player.rotationPitch / 180.0F * (float) Math.PI) * fPullStrength;

            for (int i = 0; i < 32; i++) {
               world.spawnParticle(
                  "iconcrack_333",
                  player.posX,
                  player.posY + player.getEyeHeight(),
                  player.posZ,
                  (double)motionX + (float)(Math.random() * 2.0 - 1.0) * 0.4F,
                  (double)motionY + (float)(Math.random() * 2.0 - 1.0) * 0.4F,
                  (double)motionZ + (float)(Math.random() * 2.0 - 1.0) * 0.4F
               );
            }
         }

         return null;
      }
   }

   @Override
   protected void playerBowSound(World world, EntityPlayer player, float fPullStrength) {
      world.playSoundAtEntity(player, "random.bow", 1.0F, 1.5F / (e.nextFloat() * 0.4F + 1.2F) + fPullStrength * 0.5F);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.itemIcon = register.registerIcon("fcItemBowComposite");
      this.drawIconArray[0] = register.registerIcon("fcItemBowComposite_pull_0");
      this.drawIconArray[1] = register.registerIcon("fcItemBowComposite_pull_1");
      this.drawIconArray[2] = register.registerIcon("fcItemBowComposite_pull_2");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getDrawIcon(int itemInUseDuration) {
      if (itemInUseDuration >= 18) {
         return this.drawIconArray[2];
      } else if (itemInUseDuration > 12) {
         return this.drawIconArray[1];
      } else {
         return itemInUseDuration > 0 ? this.drawIconArray[0] : this.itemIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getAnimationIcon(EntityPlayer player) {
      ItemStack itemInUse = player.getItemInUse();
      if (itemInUse != null && itemInUse.itemID == this.itemID) {
         int timeInUse = itemInUse.getMaxItemUseDuration() - player.getItemInUseCount();
         return this.getDrawIcon(timeInUse);
      } else {
         return this.itemIcon;
      }
   }
}
