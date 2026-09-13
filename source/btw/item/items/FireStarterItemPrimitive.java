package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class FireStarterItemPrimitive extends FireStarterItem {
   private final float baseChance;
   private final float maxChance;
   private final float chanceIncreasePerUse;
   private static final float CHANCE_DECAY_PER_TICK = 2.5E-4F;
   private static final long DELAY_BEFORE_DECAY = 40L;

   public FireStarterItemPrimitive(int iItemID, int iMaxUses, float fExhaustionPerUse, float fBaseChance, float fMaxChance, float fChanceIncreasePerUse) {
      super(iItemID, iMaxUses, fExhaustionPerUse);
      this.baseChance = fBaseChance;
      this.maxChance = fMaxChance;
      this.chanceIncreasePerUse = fChanceIncreasePerUse;
      this.setBuoyant();
      this.setfurnaceburntime(FurnaceBurnTime.SHAFT);
   }

   @Override
   protected boolean checkChanceOfStart(ItemStack stack, Random rand) {
      boolean bReturnValue = false;
      float fChance = stack.getAccumulatedChance(this.baseChance);
      long lCurrentTime = WorldUtils.getOverworldTimeServerOnly();
      long lLastTime = stack.getTimeOfLastUse();
      if (lLastTime > 0L) {
         if (lCurrentTime > lLastTime) {
            long lDecayTime = lCurrentTime - lLastTime - 40L;
            if (lDecayTime > 0L) {
               fChance -= (float)lDecayTime * 2.5E-4F;
               if (fChance < this.baseChance) {
                  fChance = this.baseChance;
               }
            }
         } else if (lCurrentTime < lLastTime) {
            fChance = this.baseChance;
         }
      }

      if (rand.nextFloat() <= fChance) {
         bReturnValue = true;
      }

      fChance += this.chanceIncreasePerUse;
      if (fChance > this.maxChance) {
         fChance = this.maxChance;
      }

      stack.setAccumulatedChance(fChance);
      stack.setTimeOfLastUse(lCurrentTime);
      return bReturnValue;
   }

   @Override
   protected void performUseEffects(EntityPlayer player) {
      player.playSound("random.eat", 0.5F + 0.5F * player.rand.nextInt(2), player.rand.nextFloat() * 0.25F + 1.75F);
      if (player.worldObj.isRemote) {
         for (int var3 = 0; var3 < 5; var3++) {
            Vec3 var4 = player.worldObj.getWorldVec3Pool().getVecFromPool((player.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            var4.rotateAroundX(-player.rotationPitch * (float) Math.PI / 180.0F);
            var4.rotateAroundY(-player.rotationYaw * (float) Math.PI / 180.0F);
            Vec3 var5 = player.worldObj.getWorldVec3Pool().getVecFromPool((player.rand.nextFloat() - 0.5) * 0.3, -player.rand.nextFloat() * 0.6 - 0.3, 0.6);
            var5.rotateAroundX(-player.rotationPitch * (float) Math.PI / 180.0F);
            var5.rotateAroundY(-player.rotationYaw * (float) Math.PI / 180.0F);
            var5 = var5.addVector(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            player.worldObj.spawnParticle("iconcrack_" + this.itemID, var5.xCoord, var5.yCoord, var5.zCoord, var4.xCoord, var4.yCoord + 0.05, var4.zCoord);
         }
      }
   }

   @Override
   protected boolean attemptToLightBlock(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      if (super.attemptToLightBlock(stack, world, i, j, k, iFacing)) {
         stack.setAccumulatedChance(this.baseChance);
         return true;
      } else {
         return false;
      }
   }
}
