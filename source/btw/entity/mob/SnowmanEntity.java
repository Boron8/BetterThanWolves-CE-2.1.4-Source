package btw.entity.mob;

import btw.entity.mob.behavior.SimpleWanderBehavior;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class SnowmanEntity extends EntitySnowman {
   public SnowmanEntity(World world) {
      super(world);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.tasks.addTask(2, new SimpleWanderBehavior(this, 0.2F));
   }

   @Override
   public void onLivingUpdate() {
      this.entityLivingOnLivingUpdate();
      if (this.F()) {
         this.a(DamageSource.drown, 1);
      }

      int iEntityI = MathHelper.floor_double(this.posX);
      int iEntityK = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getBiomeGenForCoords(iEntityI, iEntityK).getFloatTemperature() > 1.0F) {
         this.a(DamageSource.onFire, 1);
      } else {
         for (int iTempCount = 0; iTempCount < 4; iTempCount++) {
            int iTempI = MathHelper.floor_double(this.posX + (iTempCount % 2 * 2 - 1) * 0.25F);
            int iTempJ = (int)this.posY;
            int iTempK = MathHelper.floor_double(this.posZ + (iTempCount / 2 % 2 * 2 - 1) * 0.25F);
            if (this.worldObj.getBiomeGenForCoords(iTempI, iTempK).getFloatTemperature() < 0.8F) {
               if (this.worldObj.isAirBlock(iTempI, iTempJ, iTempK)) {
                  if (Block.snow.canPlaceBlockAt(this.worldObj, iTempI, iTempJ, iTempK)) {
                     this.worldObj.setBlock(iTempI, iTempJ, iTempK, Block.snow.blockID);
                  }
               } else if (this.worldObj.isAirBlock(iTempI, iTempJ + 1, iTempK) && Block.snow.canPlaceBlockAt(this.worldObj, iTempI, iTempJ + 1, iTempK)) {
                  this.worldObj.setBlock(iTempI, iTempJ + 1, iTempK, Block.snow.blockID);
               }
            }
         }
      }
   }
}
