package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.blocks.CandleBlock;
import btw.util.ColorUtils;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;

public class CandleItem extends PlaceAsBlockItem {
   private Icon[] icons = new Icon[17];

   public CandleItem(int itemID) {
      super(itemID, BTWBlocks.plainCandle.blockID, 0, "fcItemCandle");
      this.a(CreativeTabs.tabDecorations);
      this.a(true);
      this.setBuoyant();
   }

   @Override
   public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int facing, float clickX, float clickY, float clickZ) {
      if (itemStack.stackSize == 0) {
         return false;
      } else if (!player.canPlayerEdit(x, y, z, facing, itemStack)) {
         return false;
      } else if (this.attemptToCombineWithBlock(itemStack, player, world, x, y, z)) {
         return true;
      } else {
         BlockPos targetPos = new BlockPos(x, y, z, facing);
         return this.attemptToCombineWithBlock(itemStack, player, world, targetPos.x, targetPos.y, targetPos.z)
            ? true
            : super.onItemUse(itemStack, player, world, x, y, z, facing, clickX, clickY, clickZ);
      }
   }

   @Override
   public int getBlockIDToPlace(int itemDamage, int facing, float clickX, float clickY, float clickZ) {
      return this.getBlockIDForItemDamage(itemDamage);
   }

   @Override
   public String getItemDisplayName(ItemStack stack) {
      int itemDamage = stack.getItemDamage();
      return itemDamage < 16 ? StringTranslate.getInstance().translateNamedKey("candle." + ColorUtils.colorOrder[itemDamage]).trim() : super.l(stack);
   }

   public boolean attemptToCombineWithBlock(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z) {
      if (this.canCombineWithBlock(world, x, y, z, itemStack.getItemDamage())) {
         int targetBlockID = world.getBlockId(x, y, z);
         Block targetBlock = Block.blocksList[targetBlockID];
         if (this.incrementCandleCount(world, x, y, z)) {
            world.playSoundEffect(
               x + 0.5F,
               y + 0.5F,
               z + 0.5F,
               targetBlock.getStepSound(world, x, y, z).getPlaceSound(),
               (targetBlock.getStepSound(world, x, y, z).getPlaceVolume() + 1.0F) / 2.0F,
               targetBlock.getStepSound(world, x, y, z).getPlacePitch() * 0.8F
            );
            itemStack.stackSize--;
            return true;
         }
      }

      return false;
   }

   public boolean canCombineWithBlock(World world, int x, int y, int z, int itemDamage) {
      int blockID = world.getBlockId(x, y, z);
      Block block = Block.blocksList[blockID];
      return blockID == this.getBlockIDForItemDamage(itemDamage) && ((CandleBlock)block).getCandleCount(world, x, y, z) < 4;
   }

   public boolean incrementCandleCount(World world, int x, int y, int z) {
      int candleCount = ((CandleBlock)BTWBlocks.plainCandle).getCandleCount(world, x, y, z);
      ((CandleBlock)BTWBlocks.plainCandle).setCandleCount(world, x, y, z, candleCount + 1);
      return true;
   }

   public int getBlockIDForItemDamage(int itemDamage) {
      return itemDamage < 16 ? BTWBlocks.coloredCandle[itemDamage].blockID : BTWBlocks.plainCandle.blockID;
   }

   @Override
   public void registerIcons(IconRegister register) {
      for (int i = 0; i < 16; i++) {
         this.icons[i] = register.registerIcon("fcItemCandle_" + ColorUtils.colorOrder[i]);
      }

      this.icons[16] = register.registerIcon("fcItemCandle_plain");
   }

   @Override
   public Icon getIconFromDamage(int itemDamage) {
      return this.icons[itemDamage];
   }

   @Override
   public void getSubItems(int itemID, CreativeTabs creativeTabs, List list) {
      for (int i = 0; i <= 16; i++) {
         list.add(new ItemStack(itemID, 1, i));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int facing, EntityPlayer player, ItemStack itemStack) {
      BlockPos targetPos = new BlockPos(x, y, z);
      if (this.canCombineWithBlock(world, targetPos.x, targetPos.y, targetPos.z, itemStack.getItemDamage())) {
         return true;
      } else {
         targetPos.addFacingAsOffset(facing);
         return this.canCombineWithBlock(world, targetPos.x, targetPos.y, targetPos.z, itemStack.getItemDamage())
            ? true
            : super.canPlaceItemBlockOnSide(world, x, y, z, facing, player, itemStack);
      }
   }
}
