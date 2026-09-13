package btw.item.items;

import btw.entity.mob.SheepEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class DyeItem extends ItemDye {
   @Environment(EnvType.CLIENT)
   private Icon cocoaPowderIcon;

   public DyeItem(int iItemID) {
      super(iItemID);
      this.setBellowsBlowDistance(2);
      this.b("dyePowder");
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (itemStack.getItemDamage() == 15 && this.applyBoneMeal(world, i, j, k)) {
         itemStack.stackSize--;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean itemInteractionForEntity(ItemStack stack, EntityLiving entity) {
      if (entity instanceof SheepEntity) {
         SheepEntity sheep = (SheepEntity)entity;
         int i = BlockCloth.getBlockFromDye(stack.getItemDamage());
         if (!sheep.n() && sheep.m() != i) {
            sheep.setSuperficialFleeceColor(i);
            stack.stackSize--;
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getFilterableProperties(ItemStack stack) {
      return stack.getItemDamage() == 0 ? 2 : 8;
   }

   private boolean applyBoneMeal(World world, int i, int j, int k) {
      Block targetBlock = Block.blocksList[world.getBlockId(i, j, k)];
      return targetBlock != null && targetBlock.attemptToApplyFertilizerTo(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      this.cocoaPowderIcon = par1IconRegister.registerIcon("fcItemCocoaPowder");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int iDamage) {
      return iDamage == 3 ? this.cocoaPowderIcon : super.getIconFromDamage(iDamage);
   }
}
