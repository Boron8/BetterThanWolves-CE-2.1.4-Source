package btw.inventory.container;

import btw.block.BTWBlocks;
import btw.block.tileentity.AnvilTileEntity;
import btw.crafting.manager.SoulforgeCraftingManager;
import btw.item.BTWItems;
import net.minecraft.src.Container;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCraftResult;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotCrafting;
import net.minecraft.src.World;

public class SoulforgeContainer extends Container {
   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 4, 4);
   public IInventory craftResult = new InventoryCraftResult();
   private World localWorld;
   private int anvilX;
   private int anvilY;
   private int anvilZ;
   private static final double MAX_INTERACTION_DISTANCE = 8.0;
   private static final double MAX_INTERACTION_DISTANCE_SQ = 64.0;
   private static final int CRAFTING_GRID_WIDTH = 4;
   private static final int CRAFTING_GRID_HEIGHT = 4;
   private static final int CRAFTING_GRID_SIZE = 16;
   private static final int SLOT_SCREEN_WIDTH = 18;
   private static final int SLOT_SCREEN_HEIGHT = 18;
   private static final int GRID_SCREEN_POS_X = 23;
   private static final int GRID_SCREEN_POS_Y = 17;
   private static final int PLAYER_INVENTORY_SCREEN_POS_X = 8;
   private static final int PLAYER_INVENTORY_SCREEN_POS_Y = 102;
   private static final int PLAYER_HOTBAR_SCREEN_POS_Y = 160;
   private static final int PLAYER_INVENTORY_MIN_SLOT = 17;
   private static final int PLAYER_INVENTORY_MAX_SLOT = 43;
   private static final int PLAYER_HOTBAR_MIN_SLOT = 44;
   private static final int PLAYER_HOTBAR_MAX_SLOT = 52;

   public SoulforgeContainer(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
      this.localWorld = world;
      this.anvilX = i;
      this.anvilY = j;
      this.anvilZ = k;
      this.a(new SlotCrafting(inventoryplayer.player, this.craftMatrix, this.craftResult, 0, 135, 44));

      for (int tempSlotY = 0; tempSlotY < 4; tempSlotY++) {
         for (int tempSlotX = 0; tempSlotX < 4; tempSlotX++) {
            this.a(new Slot(this.craftMatrix, tempSlotX + tempSlotY * 4, 23 + tempSlotX * 18, 17 + tempSlotY * 18));
         }
      }

      for (int tempSlotY = 0; tempSlotY < 3; tempSlotY++) {
         for (int tempSlotX = 0; tempSlotX < 9; tempSlotX++) {
            this.a(new Slot(inventoryplayer, tempSlotX + tempSlotY * 9 + 9, 8 + tempSlotX * 18, 102 + tempSlotY * 18));
         }
      }

      for (int tempSlotX = 0; tempSlotX < 9; tempSlotX++) {
         this.a(new Slot(inventoryplayer, tempSlotX, 8 + tempSlotX * 18, 160));
      }

      if (world != null && !world.isRemote) {
         AnvilTileEntity tileEntityAnvil = (AnvilTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntityAnvil != null) {
            for (int tempSlotY = 0; tempSlotY < 4; tempSlotY++) {
               for (int tempSlotX = 0; tempSlotX < 4; tempSlotX++) {
                  if (tileEntityAnvil.doesSlotContainMould(tempSlotX, tempSlotY)) {
                     ItemStack mouldStack = new ItemStack(BTWItems.mould);
                     Slot slot = (Slot)this.inventorySlots.get(tempSlotX + tempSlotY * 4 + 1);
                     slot.putStack(mouldStack);
                  }
               }
            }

            tileEntityAnvil.clearMouldContents();
         }
      }

      this.onCraftMatrixChanged(this.craftMatrix);
   }

   @Override
   public void onCraftMatrixChanged(IInventory iinventory) {
      ItemStack craftedStack = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.localWorld);
      IRecipe recipe = CraftingManager.getInstance().findMatchingIRecipe(this.craftMatrix, this.localWorld);
      if (craftedStack == null) {
         craftedStack = SoulforgeCraftingManager.getInstance().findMatchingRecipeStack(this.craftMatrix, this.localWorld);
         recipe = SoulforgeCraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.localWorld);
      }

      this.craftResult.setInventorySlotContents(0, craftedStack);
      ((SlotCrafting)this.a(0)).setRecipe(recipe);
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer entityplayer) {
      super.onCraftGuiClosed(entityplayer);
      if (this.localWorld != null && !this.localWorld.isRemote) {
         for (int i = 0; i < 16; i++) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            if (itemstack != null) {
               entityplayer.dropPlayerItem(itemstack);
            }
         }
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer entityplayer) {
      if (this.localWorld != null && !this.localWorld.isRemote) {
         return this.localWorld.getBlockId(this.anvilX, this.anvilY, this.anvilZ) != BTWBlocks.soulforge.blockID
            ? false
            : entityplayer.e(this.anvilX + 0.5, this.anvilY + 0.5, this.anvilZ + 0.5) <= 64.0;
      } else {
         return true;
      }
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotClicked) {
      ItemStack oldStackInSlotClicked = null;
      Slot slot = (Slot)this.inventorySlots.get(iSlotClicked);
      if (slot != null && slot.getHasStack()) {
         ItemStack newStackInSlotClicked = slot.getStack();
         oldStackInSlotClicked = newStackInSlotClicked.copy();
         if (iSlotClicked == 0) {
            if (!this.a(newStackInSlotClicked, 17, 53, true)) {
               return null;
            }
         } else if (iSlotClicked > 16 && iSlotClicked <= 52) {
            if (!this.a(newStackInSlotClicked, 1, 17, false)) {
               return null;
            }
         } else if (!this.a(newStackInSlotClicked, 17, 53, true)) {
            return null;
         }

         if (newStackInSlotClicked.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }

         if (newStackInSlotClicked.stackSize == oldStackInSlotClicked.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(player, newStackInSlotClicked);
      }

      return oldStackInSlotClicked;
   }
}
