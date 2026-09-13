package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class ChiselItemDiamond extends ChiselItem {
   public ChiselItemDiamond(int var1) {
      super(var1, EnumToolMaterial.EMERALD, 500);
      this.setFilterableProperties(4);
      this.b("fcItemChiselDiamond");
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(4);
   }

   @Override
   public boolean getCanBePlacedAsBlock() {
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack var1, World var2, int var3, int var4, int var5, int var6, EntityLiving var7) {
      if (var3 == Block.wood.blockID && var2.getBlockId(var4, var5, var6) == BTWBlocks.workStump.blockID) {
         var1.damageItem(5, var7);
         return true;
      } else {
         return super.a(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   @Override
   public boolean isDamagedInCrafting() {
      return false;
   }

   @Override
   public boolean isConsumedInCrafting() {
      return false;
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack stack) {
      playStoneSplitSoundOnPlayer(player);
   }

   @Override
   public boolean canToolStickInBlock(ItemStack var1, Block var2, World var3, int var4, int var5, int var6) {
      return var2.blockMaterial == Material.rock && var2.blockID != Block.bedrock.blockID
         ? true
         : super.canToolStickInBlock(var1, var2, var3, var4, var5, var6);
   }

   public static void playStoneSplitSoundOnPlayer(EntityPlayer var0) {
      if (var0.timesCraftedThisTick == 0) {
         var0.playSound("random.anvil_land", 0.5F, var0.worldObj.rand.nextFloat() * 0.25F + 1.75F);
      }
   }

   @Override
   public int getItemEnchantability() {
      return this.toolMaterial.getEnchantability();
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment var1) {
      return var1.type == EnumEnchantmentType.digger ? true : super.isEnchantmentApplicable(var1);
   }
}
