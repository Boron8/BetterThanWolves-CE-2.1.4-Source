package btw.item.items;

import btw.entity.CorpseEyeEntity;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class CorpseEyeItem extends Item {
   public CorpseEyeItem(int par1) {
      super(par1);
      this.e(9);
      this.b("fcItemCorpseEye");
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
      if (!world.isRemote) {
         int targetX = MathHelper.floor_double(player.posX);
         int targetY = MathHelper.floor_double(player.boundingBox.minY + 1.62);
         int targetZ = MathHelper.floor_double(player.posZ);
         if (player.deathCount > 0 && player.lastDeathDimension == player.dimension) {
            targetX = player.lastDeathLocationX;
            targetY = player.lastDeathLocationY;
            targetZ = player.lastDeathLocationZ;
         }

         CorpseEyeEntity var6 = (CorpseEyeEntity)EntityList.createEntityOfType(
            CorpseEyeEntity.class, world, player.posX, player.posY + 1.62 - player.yOffset, player.posZ
         );
         var6.setItemDamage(itemStack.getItemDamage());
         var6.moveTowards(targetX, targetY, targetZ);
         world.spawnEntityInWorld(var6);
         world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
         world.playAuxSFXAtEntity((EntityPlayer)null, 1002, (int)player.posX, (int)player.posY, (int)player.posZ, 0);
         if (!player.capabilities.isCreativeMode) {
            itemStack.stackSize--;
         }
      }

      return itemStack;
   }
}
