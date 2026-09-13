package btw.item.items;

import btw.item.BTWItems;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class BoneCarvingItem extends ProgressiveCraftingItem {
   public BoneCarvingItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setFilterableProperties(2);
      this.b("fcItemCarvingBone");
   }

   @Override
   protected void playCraftingFX(ItemStack stack, World world, EntityPlayer player) {
      player.playSound("random.eat", 0.5F + 0.5F * world.rand.nextInt(2), world.rand.nextFloat() * 0.25F + 1.25F);
      this.spawnUseParticles(stack, world, player);
   }

   @Override
   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      player.playSound("mob.zombie.woodbreak", 0.1F, 1.25F + world.rand.nextFloat() * 0.25F);
      return new ItemStack(BTWItems.boneFishHook, 1, 0);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         player.playSound("mob.zombie.woodbreak", 0.1F, 1.25F + world.rand.nextFloat() * 0.25F);
      }

      super.d(stack, world, player);
   }

   protected void spawnUseParticles(ItemStack stack, World world, EntityPlayer player) {
      if (world.isRemote) {
         Vec3 velVec = world.getWorldVec3Pool().getVecFromPool((world.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
         velVec.rotateAroundX(-player.rotationPitch * (float) Math.PI / 180.0F);
         velVec.rotateAroundY(-player.rotationYaw * (float) Math.PI / 180.0F);
         Vec3 posVec = world.getWorldVec3Pool().getVecFromPool((world.rand.nextFloat() - 0.5) * 0.3, -world.rand.nextFloat() * 0.6 - 0.3, 0.6);
         posVec.rotateAroundX(-player.rotationPitch * (float) Math.PI / 180.0F);
         posVec.rotateAroundY(-player.rotationYaw * (float) Math.PI / 180.0F);
         posVec = posVec.addVector(player.posX, player.posY + player.getEyeHeight(), player.posZ);
         world.spawnParticle(
            "iconcrack_" + stack.getItem().itemID, posVec.xCoord, posVec.yCoord, posVec.zCoord, velVec.xCoord, velVec.yCoord + 0.05, velVec.zCoord
         );
      }
   }
}
