package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class ChiselItemIron extends ChiselItem {
   public ChiselItemIron(int iItemID) {
      super(iItemID, EnumToolMaterial.IRON, 50);
      this.efficiencyOnProperMaterial /= 6.0F;
      this.setFilterableProperties(4);
      this.b("fcItemChiselIron");
   }

   @Override
   public boolean getCanBePlacedAsBlock() {
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack stack, World world, int iBlockID, int i, int j, int k, EntityLiving usingEntity) {
      if (iBlockID == Block.wood.blockID && world.getBlockId(i, j, k) == BTWBlocks.workStump.blockID) {
         stack.damageItem(5, usingEntity);
         return true;
      } else {
         return super.a(stack, world, iBlockID, i, j, k, usingEntity);
      }
   }

   @Override
   public boolean isDamagedInCrafting() {
      return true;
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      playStoneSplitSoundOnPlayer(player);
   }

   @Override
   public void onBrokenInCrafting(EntityPlayer player) {
      playBreakSoundOnPlayer(player);
   }

   @Override
   public boolean canToolStickInBlock(ItemStack stack, Block block, World world, int i, int j, int k) {
      return block.blockMaterial == Material.rock && block.blockID != Block.bedrock.blockID ? true : super.canToolStickInBlock(stack, block, world, i, j, k);
   }

   public static void playStoneSplitSoundOnPlayer(EntityPlayer player) {
      if (player.timesCraftedThisTick == 0) {
         player.playSound("random.anvil_land", 0.5F, player.worldObj.rand.nextFloat() * 0.25F + 1.75F);
      }
   }

   public static void playBreakSoundOnPlayer(EntityPlayer player) {
      player.playSound("random.break", 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
   }
}
