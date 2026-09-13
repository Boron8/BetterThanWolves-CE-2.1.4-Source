package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.items.ChiselItem;
import btw.item.items.ShearsItem;
import btw.item.util.ItemUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockWeb;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

public class WebBlock extends BlockWeb {
   @Environment(EnvType.CLIENT)
   private Icon[] iconByDamageArray = new Icon[4];

   public WebBlock(int iBlockID) {
      super(iBlockID);
      this.setAxesEffectiveOn(true);
      this.setChiselsEffectiveOn(true);
      this.c(4.0F);
      this.setBuoyant();
      this.a(BTWBlocks.stepSoundSquish);
      this.c("web");
      this.a(null);
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ShearsItem && this.getDamageLevel(iMetadata) == 0) {
         player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
         this.b(world, i, j, k, new ItemStack(BTWBlocks.web, 1, 0));
      } else {
         super.a(world, player, i, j, k, iMetadata);
      }
   }

   @Override
   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 1;
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return 0;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      int iDamageLevel = this.getDamageLevel(iOldMetadata);
      if (iDamageLevel < 3) {
         this.setDamageLevel(world, i, j, k, iDamageLevel + 1);
         return true;
      } else {
         if (!world.isRemote && this.isEffectiveItemConversionTool(stack, world, i, j, k)) {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.bow", 0.75F + world.rand.nextFloat() * 0.25F, world.rand.nextFloat() * 0.25F + 1.25F);
            ItemUtils.dropStackAsIfBlockHarvested(world, i, j, k, new ItemStack(Item.silk, 1));
         }

         return false;
      }
   }

   public void setDamageLevel(World world, int i, int j, int k, int iDamageLevel) {
      int iMetadata = this.setDamageLevel(world.getBlockMetadata(i, j, k), iDamageLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setDamageLevel(int iMetadata, int iDamageLevel) {
      iMetadata &= -4;
      return iMetadata | iDamageLevel;
   }

   public int getDamageLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getDamageLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getDamageLevel(int iMetadata) {
      return iMetadata & 3;
   }

   public boolean isEffectiveItemConversionTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null && stack.getItem() instanceof ChiselItem) {
         int iToolLevel = ((ChiselItem)stack.getItem()).toolMaterial.getHarvestLevel();
         return iToolLevel >= this.getEfficientToolLevel(world, i, j, k);
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("web");
      this.iconByDamageArray[0] = this.blockIcon;
      this.iconByDamageArray[1] = register.registerIcon("fcBlockWeb_1");
      this.iconByDamageArray[2] = register.registerIcon("fcBlockWeb_2");
      this.iconByDamageArray[3] = register.registerIcon("fcBlockWeb_3");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconByDamageArray[this.getDamageLevel(iMetadata)];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderCrossedSquares(this, i, j, k);
   }
}
