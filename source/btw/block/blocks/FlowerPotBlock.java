package btw.block.blocks;

import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockFlowerPot;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class FlowerPotBlock extends BlockFlowerPot {
   protected static final double HEIGHT = 0.375;
   protected static final double WIDTH = 0.375;
   protected static final double HALF_WIDTH = 0.1875;

   public FlowerPotBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iSide, float fClickX, float fClickY, float fClickZ) {
      ItemStack playerStack = player.inventory.getCurrentItem();
      if (playerStack != null && world.getBlockMetadata(i, j, k) == 0) {
         int iMetadataForStack = this.getMetadataForItemStack(playerStack);
         if (iMetadataForStack > 0) {
            world.SetBlockMetadataWithNotify(i, j, k, iMetadataForStack, 2);
            if (!player.capabilities.isCreativeMode) {
               playerStack.stackSize--;
               if (playerStack.stackSize <= 0) {
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
               }
            }

            return true;
         }
      }

      return false;
   }

   @Override
   public int getDamageValue(World world, int i, int j, int k) {
      ItemStack stack = this.getPlantStackForMetadata(world.getBlockMetadata(i, j, k));
      return stack == null ? Item.flowerPot.itemID : stack.getItemDamage();
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (!world.isRemote) {
         int iQuantityDropped = this.a(iFortuneModifier, world.rand);

         for (int iDropCount = 0; iDropCount < iQuantityDropped; iDropCount++) {
            if (world.rand.nextFloat() <= fChance) {
               int itemID = this.a(iMetadata, world.rand, iFortuneModifier);
               if (itemID > 0) {
                  this.b(world, i, j, k, new ItemStack(itemID, 1, this.a(iMetadata)));
               }
            }
         }
      }

      if (iMetadata > 0) {
         ItemStack stack = this.getPlantStackForMetadata(iMetadata);
         if (stack != null) {
            this.b(world, i, j, k, stack);
         }
      }
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   private ItemStack getPlantStackForMetadata(int iMetadata) {
      if (iMetadata == 7) {
         return new ItemStack(BTWItems.redMushroom);
      } else {
         return iMetadata == 8 ? new ItemStack(BTWItems.brownMushroom) : n_(iMetadata);
      }
   }

   private int getMetadataForItemStack(ItemStack stack) {
      int itemID = stack.getItem().itemID;
      if (itemID == BTWItems.redMushroom.itemID) {
         return 7;
      } else {
         return itemID == BTWItems.brownMushroom.itemID ? 8 : a(stack);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockFlowerpot(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide == 0 ? !blockAccess.isBlockOpaqueCube(i, j, k) : true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      ItemStack stack = this.getPlantStackForMetadata(world.getBlockMetadata(i, j, k));
      return stack == null ? Item.flowerPot.itemID : stack.itemID;
   }
}
