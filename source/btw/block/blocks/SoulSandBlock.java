package btw.block.blocks;

import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockSoulSand;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class SoulSandBlock extends BlockSoulSand {
   @Environment(EnvType.CLIENT)
   private Icon filterIcon;

   public SoulSandBlock(int iBlockID) {
      super(iBlockID);
      this.setShovelsEffectiveOn();
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.soulSandPile.itemID, 3, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      return filteredItem.itemID == BTWItems.groundNetherrack.itemID
         || filteredItem.itemID == BTWItems.soulDust.itemID
         || filteredItem.itemID == Item.lightStoneDust.itemID;
   }

   @Override
   public boolean canNetherWartGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.filterIcon = register.registerIcon("fcBlockHopper_soulsand");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return this.filterIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderStandardFullBlock(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesItemRenderAsBlock(int iItemDamage) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.renderStandardFullBlockMovedByPiston(this, i, j, k);
   }
}
