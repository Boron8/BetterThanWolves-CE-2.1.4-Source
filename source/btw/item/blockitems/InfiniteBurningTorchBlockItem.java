package btw.item.blockitems;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class InfiniteBurningTorchBlockItem extends ItemBlock {
   public InfiniteBurningTorchBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return player.canPlayerEdit(i, j, k, iFacing, stack) && this.attemptToLightBlock(stack, world, i, j, k, iFacing)
         ? true
         : super.onItemUse(stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
   }

   @Override
   public boolean getCanItemStartFireOnUse(int iItemDamage) {
      return true;
   }

   @Override
   public void onUpdate(ItemStack stack, World world, EntityPlayer entity, int iInventorySlot, boolean bIsHandHeldItem) {
      if (!world.isRemote && stack.stackSize > 0 && entity.G() && entity.a(Material.water) && !entity.capabilities.isCreativeMode) {
         int iFXI = MathHelper.floor_double(entity.posX);
         int iFXJ = MathHelper.floor_double(entity.posY) + 1;
         int iFXK = MathHelper.floor_double(entity.posZ);
         world.playAuxSFX(1004, iFXI, iFXJ, iFXK, 0);
         stack.itemID = BTWBlocks.infiniteUnlitTorch.blockID;
      }
   }

   @Override
   public int getBlockID() {
      if (MinecraftServer.getIsServer()) {
         return MinecraftServer.getServer().worldServers[0].getDifficulty().shouldNetherCoalTorchesStartFires() ? this.blockID : Block.torchWood.blockID;
      } else {
         return Minecraft.getMinecraft().theWorld.getDifficulty().shouldNetherCoalTorchesStartFires() ? this.blockID : Block.torchWood.blockID;
      }
   }

   protected boolean attemptToLightBlock(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      if (targetBlock != null && targetBlock.getCanBeSetOnFireDirectlyByItem(world, i, j, k)) {
         if (!world.isRemote) {
            targetBlock.setOnFireDirectly(world, i, j, k);
         }

         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean canPlaceItemBlockOnSide(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack itemStack) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      return targetBlock != null && targetBlock.getCanBeSetOnFireDirectlyByItem(world, i, j, k)
         ? true
         : super.canPlaceItemBlockOnSide(world, i, j, k, iFacing, player, itemStack);
   }
}
