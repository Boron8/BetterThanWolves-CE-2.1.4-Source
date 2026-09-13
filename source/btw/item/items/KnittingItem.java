package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class KnittingItem extends ProgressiveCraftingItem {
   @Environment(EnvType.CLIENT)
   private Icon iconWool;

   public KnittingItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setfurnaceburntime(FurnaceBurnTime.SHAFT.burnTime + 2 * FurnaceBurnTime.WOOL.burnTime);
      this.b("fcItemKnitting");
   }

   @Override
   protected void playCraftingFX(ItemStack stack, World world, EntityPlayer player) {
      player.playSound("step.wood", 0.25F + 0.25F * world.rand.nextInt(2), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.25F + 1.75F);
   }

   @Override
   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      int iColorIndex = WoolItem.getClosestColorIndex(getColor(stack));
      ItemStack woolStack = new ItemStack(BTWItems.woolKnit, 1, iColorIndex);
      world.playSoundAtEntity(player, "step.cloth", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
      ItemUtils.givePlayerStackOrEject(player, woolStack);
      return new ItemStack(BTWItems.knittingNeedles);
   }

   @Override
   public boolean getCanBeFedDirectlyIntoCampfire(int iItemDamage) {
      return false;
   }

   @Override
   public boolean getCanBeFedDirectlyIntoBrickOven(int iItemDamage) {
      return false;
   }

   public static void setColor(ItemStack stack, int iColor) {
      NBTTagCompound tag = stack.getTagCompound();
      if (tag == null) {
         tag = new NBTTagCompound();
         stack.setTagCompound(tag);
      }

      tag.setInteger("fcColor", iColor);
   }

   public static int getColor(ItemStack stack) {
      NBTTagCompound tag = stack.getTagCompound();
      return tag != null && tag.hasKey("fcColor") ? tag.getInteger("fcColor") : 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getColorFromItemStack(ItemStack stack, int iRenderPass) {
      return iRenderPass == 0 ? getColor(stack) : super.a(stack, iRenderPass);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconWool = register.registerIcon("fcItemKnitting_Wool");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamageForRenderPass(int iDamage, int iRenderPass) {
      return iRenderPass == 0 ? this.iconWool : this.itemIcon;
   }
}
