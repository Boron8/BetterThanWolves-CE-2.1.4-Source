package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.blocks.AxleBlock;
import btw.entity.mechanical.source.WaterWheelEntity;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WaterWheelItem extends Item {
   public WaterWheelItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 1;
      this.setBuoyant();
      this.b("fcItemWaterWheel");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      if (iTargetBlockID == BTWBlocks.axle.blockID) {
         int iAxisAlignment = ((AxleBlock)BTWBlocks.axle).getAxisAlignment(world, i, j, k);
         if (iAxisAlignment != 0) {
            boolean bIAligned = false;
            if (iAxisAlignment == 2) {
               bIAligned = true;
            }

            WaterWheelEntity waterWheel = (WaterWheelEntity)EntityList.createEntityOfType(
               WaterWheelEntity.class, world, i + 0.5F, j + 0.5F, k + 0.5F, bIAligned
            );
            if (waterWheel.validateAreaAroundDevice()) {
               if (waterWheel.isClearOfBlockingEntities()) {
                  if (!world.isRemote) {
                     waterWheel.setRotationSpeed(waterWheel.computeRotation());
                     world.spawnEntityInWorld(waterWheel);
                  }

                  itemStack.stackSize--;
                  return true;
               }

               if (world.isRemote) {
                  player.addChatMessage(this.a() + ".placementObstructed");
               }
            } else if (world.isRemote) {
               player.addChatMessage(this.a() + ".notEnoughRoom");
            }
         }
      }

      return false;
   }
}
