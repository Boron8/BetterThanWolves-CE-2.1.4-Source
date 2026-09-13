package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanterBlock;
import btw.entity.mob.ZombiePigmanEntity;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.BiomeGenHell;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

public class AestheticVegetationBlockItem extends ItemBlock {
   public AestheticVegetationBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcAestheticVegetation");
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage == 3 ? 4 : iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 0:
         case 1:
            return super.getUnlocalizedName() + "." + "vinetrap";
         case 2:
            return super.getUnlocalizedName() + "." + "bloodwoodsapling";
         case 3:
            return super.getUnlocalizedName() + "." + "bloodleaves";
         default:
            return super.getUnlocalizedName();
      }
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      int iSubtype = itemStack.getItemDamage();
      if (iSubtype == 2) {
         if (iFacing != 1) {
            return false;
         } else if (player == null || player.canPlayerEdit(i, j, k, iFacing, itemStack) && player.canPlayerEdit(i, j + 1, k, iFacing, itemStack)) {
            int iTargetBlockID = world.getBlockId(i, j, k);
            boolean bValidBlockForGrowth = false;
            if (iTargetBlockID == Block.slowSand.blockID) {
               bValidBlockForGrowth = true;
            } else if (iTargetBlockID == BTWBlocks.planter.blockID && ((PlanterBlock)BTWBlocks.planter).getPlanterType(world, i, j, k) == 8) {
               bValidBlockForGrowth = true;
            }

            if (bValidBlockForGrowth && world.isAirBlock(i, j + 1, k)) {
               world.setBlockAndMetadataWithNotify(i, j + 1, k, BTWBlocks.aestheticVegetation.blockID, 2);
               itemStack.stackSize--;
               WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
               if (worldchunkmanager != null) {
                  BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(i, k);
                  if (biomegenbase instanceof BiomeGenHell) {
                     this.angerPigmen(world, player);
                  }
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return super.onItemUse(itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
      }
   }

   @Override
   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return iItemDamage == 3 ? BTWBlocks.bloodWoodLeaves.blockID : super.getBlockIDToPlace(iItemDamage, iFacing, fClickX, fClickY, fClickZ);
   }

   private void angerPigmen(World world, EntityPlayer entityPlayer) {
      List list = world.getEntitiesWithinAABB(ZombiePigmanEntity.class, entityPlayer.boundingBox.expand(32.0, 32.0, 32.0));

      for (int tempIndex = 0; tempIndex < list.size(); tempIndex++) {
         Entity targetEntity = (Entity)list.get(tempIndex);
         targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), 0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamage(int iItemDamage) {
      return iItemDamage == 2 ? Block.blocksList[this.g()].getIcon(1, iItemDamage) : super.getIconFromDamage(iItemDamage);
   }
}
