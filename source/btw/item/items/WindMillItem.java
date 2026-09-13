package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.blocks.AxleBlock;
import btw.entity.mechanical.source.WindMillEntity;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WindMillItem extends Item {
   public WindMillItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 1;
      this.setBuoyant();
      this.b("fcItemWindMill");
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

            WindMillEntity windMill = (WindMillEntity)EntityList.createEntityOfType(WindMillEntity.class, world, i + 0.5F, j + 0.5F, k + 0.5F, bIAligned);
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

         return true;
      } else {
         return false;
      }
   }
}
