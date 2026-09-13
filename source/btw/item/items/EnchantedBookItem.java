package btw.item.items;

import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemEnchantedBook;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class EnchantedBookItem extends ItemEnchantedBook {
   public EnchantedBookItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
      if (world.isRemote) {
         player.addChatMessage(this.a() + ".languageUnfamiliar");
      }

      return itemStack;
   }

   @Override
   public void initializeStackOnGiveCommand(Random rand, ItemStack stack) {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void addInformation(ItemStack itemStack, EntityPlayer player, List infoList, boolean bAdvamcedToolTips) {
   }
}
