package btw.item.blockitems;

import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class FiniteBurningTorchBlockItem extends InfiniteBurningTorchBlockItem {
   private static final float CHANCE_OF_GOING_OUT_FROM_RAIN = 0.0025F;
   public static final int MAX_DAMAGE = 32;
   public static final float DAMAGE_TO_BURN_TIME_RATIO = 0.0013333333F;
   public static final int SPUTTER_DAMAGE = 32;
   @Environment(EnvType.CLIENT)
   private Icon iconSputtering;

   public FiniteBurningTorchBlockItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 1;
      this.e(32);
      this.b("fcBlockTorchFiniteBurning");
   }

   @Override
   public void onUpdate(ItemStack stack, World world, EntityPlayer entity, int iInventorySlot, boolean bIsHandHeldItem) {
      if (!world.isRemote && stack.stackSize > 0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("outTime") && !entity.capabilities.isCreativeMode) {
         long lExpiryTime = stack.getTagCompound().getLong("outTime");
         int iCountdown = (int)(lExpiryTime - WorldUtils.getOverworldTimeServerOnly());
         if (iCountdown > 0 && iCountdown <= 24000) {
            if (entity.G() && entity.a(Material.water) || entity.isBeingRainedOn() && entity.worldObj.rand.nextFloat() <= 0.0025F) {
               int iFXI = MathHelper.floor_double(entity.posX);
               int iFXJ = MathHelper.floor_double(entity.posY) + 1;
               int iFXK = MathHelper.floor_double(entity.posZ);
               world.playAuxSFX(1004, iFXI, iFXJ, iFXK, 0);
               stack.stackSize--;
               if (stack.stackSize <= 0) {
                  entity.inventory.mainInventory[iInventorySlot] = null;
               }
            } else {
               int iNewItemDamage = (int)(0.0013333333F * (24000 - iCountdown));
               iNewItemDamage = MathHelper.clamp_int(iNewItemDamage, 1, 31);
               if (iNewItemDamage != stack.getItemDamage()) {
                  stack.setItemDamage(iNewItemDamage);
               }
            }
         } else {
            int iFXI = MathHelper.floor_double(entity.posX);
            int iFXJ = MathHelper.floor_double(entity.posY) + 1;
            int iFXK = MathHelper.floor_double(entity.posZ);
            world.playAuxSFX(1004, iFXI, iFXJ, iFXK, 0);
            stack.stackSize--;
            if (stack.stackSize <= 0) {
               entity.inventory.mainInventory[iInventorySlot] = null;
            }
         }
      }
   }

   @Override
   public boolean ignoreDamageWhenComparingDuringUse() {
      return true;
   }

   @Override
   public int getBlockID() {
      return this.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int iDamage) {
      return iDamage >= 32 ? this.iconSputtering : super.a_(iDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconSputtering = register.registerIcon("fcBlockTorchFiniteSputtering");
   }
}
