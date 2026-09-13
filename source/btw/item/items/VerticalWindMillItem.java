package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.blocks.AxleBlock;
import btw.entity.mechanical.source.VerticalWindMillEntity;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class VerticalWindMillItem extends Item {
   public VerticalWindMillItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.maxStackSize = 1;
      this.b("fcItemWindMillVertical");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      if (iTargetBlockID == BTWBlocks.axle.blockID) {
         int iAxisAlignment = ((AxleBlock)BTWBlocks.axle).getAxisAlignment(world, i, j, k);
         if (iAxisAlignment == 0) {
            if (player.rotationPitch <= 0.0F) {
               j += 3;
            } else {
               j -= 3;
            }

            if (!this.checkForSupportingAxles(world, i, j, k)) {
               if (world.isRemote) {
                  player.addChatMessage(this.a() + ".tooFewAxles");
               }
            } else {
               VerticalWindMillEntity windMill = (VerticalWindMillEntity)EntityList.createEntityOfType(
                  VerticalWindMillEntity.class, world, i + 0.5F, j + 0.5F, k + 0.5F
               );
               if (windMill.validateAreaAroundDevice()) {
                  if (windMill.isClearOfBlockingEntities()) {
                     if (!world.isRemote) {
                        windMill.setRotationSpeed(windMill.computeRotation());
                        world.spawnEntityInWorld(windMill);
                     }

                     itemStack.stackSize--;
                  } else if (world.isRemote) {
                     player.addChatMessage("message.windMill.placementObstructed");
                  }
               } else if (world.isRemote) {
                  player.addChatMessage("message.windMill.notEnoughRoom");
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean checkForSupportingAxles(World world, int i, int j, int k) {
      for (int iTempJ = j - 3; iTempJ <= j + 3; iTempJ++) {
         int iTargetBlockID = world.getBlockId(i, iTempJ, k);
         if (iTargetBlockID != BTWBlocks.axle.blockID) {
            return false;
         }

         int iAxisAlignment = ((AxleBlock)BTWBlocks.axle).getAxisAlignment(world, i, iTempJ, k);
         if (iAxisAlignment != 0) {
            return false;
         }
      }

      return true;
   }
}
