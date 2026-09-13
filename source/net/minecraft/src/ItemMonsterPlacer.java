package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeItem;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemMonsterPlacer extends Item {
   @Environment(EnvType.CLIENT)
   private Icon theIcon;

   public ItemMonsterPlacer(int par1) {
      super(par1);
      this.a(true);
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public String getItemDisplayName(ItemStack par1ItemStack) {
      String var2 = ("" + StatCollector.translateToLocal(this.a() + ".name")).trim();
      String var3 = EntityList.getStringFromID(par1ItemStack.getItemDamage());
      if (var3 != null) {
         var2 = var2 + " " + StatCollector.translateToLocal("entity." + var3 + ".name");
      }

      return var2;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      EntityEggInfo var3 = (EntityEggInfo)EntityList.entityEggs.get(par1ItemStack.getItemDamage());
      return var3 != null
         ? (
            par2 == 0
               ? ColorizeItem.colorizeSpawnerEgg(var3.primaryColor, par1ItemStack.getItemDamage(), par2)
               : ColorizeItem.colorizeSpawnerEgg(var3.secondaryColor, par1ItemStack.getItemDamage(), par2)
         )
         : ColorizeItem.colorizeSpawnerEgg(16777215, par1ItemStack.getItemDamage(), par2);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(par1, par2);
   }

   @Override
   public boolean onItemUse(
      ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10
   ) {
      if (par3World.isRemote) {
         return true;
      } else {
         int var11 = par3World.getBlockId(par4, par5, par6);
         par4 += Facing.offsetsXForSide[par7];
         par5 += Facing.offsetsYForSide[par7];
         par6 += Facing.offsetsZForSide[par7];
         double var12 = 0.0;
         if (par7 == 1 && Block.blocksList[var11] != null && Block.blocksList[var11].getRenderType() == 11) {
            var12 = 0.5;
         }

         Entity var14 = spawnCreature(par3World, par1ItemStack.getItemDamage(), par4 + 0.5, par5 + var12, par6 + 0.5);
         if (var14 != null) {
            if (var14 instanceof EntityLiving && par1ItemStack.hasDisplayName()) {
               ((EntityLiving)var14).func_94058_c(par1ItemStack.getDisplayName());
            }

            if (!par2EntityPlayer.capabilities.isCreativeMode) {
               par1ItemStack.stackSize--;
            }
         }

         return true;
      }
   }

   public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6) {
      if (!EntityList.entityEggs.containsKey(par1)) {
         return null;
      } else {
         Entity var8 = null;

         for (int var9 = 0; var9 < 1; var9++) {
            var8 = EntityList.createEntityByID(par1, par0World);
            if (var8 != null && var8 instanceof EntityLiving) {
               EntityLiving var10 = (EntityLiving)var8;
               var8.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
               var10.rotationYawHead = var10.rotationYaw;
               var10.renderYawOffset = var10.rotationYaw;
               var10.initCreature();
               par0World.spawnEntityInWorld(var8);
               var10.playLivingSound();
            }
         }

         return var8;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      for (EntityEggInfo var5 : EntityList.entityEggs.values()) {
         par3List.add(new ItemStack(par1, 1, var5.spawnedID));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      this.theIcon = par1IconRegister.registerIcon("monsterPlacer_overlay");
   }
}
