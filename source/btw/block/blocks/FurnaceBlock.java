package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFurnace;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

public class FurnaceBlock extends BlockFurnace {
   @Environment(EnvType.CLIENT)
   protected Icon iconFullFront;
   @Environment(EnvType.CLIENT)
   protected Icon iconFullFrontLit;

   public FurnaceBlock(int iBlockID, boolean bIsLit) {
      super(iBlockID, bIsLit);
      this.a(j);
      this.c(3.0F);
      this.b(5.83F);
      if (!bIsLit) {
         this.a(CreativeTabs.tabDecorations);
      } else {
         this.a(0.875F);
      }

      this.c("furnace");
   }

   @Override
   public int quantityDropped(Random rand) {
      return 12 + rand.nextInt(5);
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return BTWItems.stone.itemID;
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
      player.addExhaustion(0.025F);
      if (EnchantmentHelper.getSilkTouchModifier(player)) {
         ItemStack dropStack = new ItemStack(this.iddroppedsilktouch(), 1, 0);
         if (dropStack != null) {
            this.b(world, i, j, k, dropStack);
         }
      } else {
         int iFortuneModifier = EnchantmentHelper.getFortuneModifier(player);
         this.c(world, i, j, k, iMetadata, iFortuneModifier);
      }
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      return super.onBlockActivated(world, i, j, k, player, iFacing, fXClick, fYClick, fZClick);
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isActive;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iFacing = iMetadata & 7;
      iFacing = Block.rotateFacingAroundY(iFacing, bReverse);
      return iMetadata & -8 | iFacing;
   }

   protected int iddroppedsilktouch() {
      return Block.furnaceIdle.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iMetadataStripped = iMetadata & 7;
      boolean bHasContents = (iMetadata & 8) != 0;
      if (iMetadataStripped != iSide || !bHasContents) {
         return super.getIcon(iSide, iMetadataStripped);
      } else {
         return this.isActive ? this.iconFullFrontLit : this.iconFullFront;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconFullFront = register.registerIcon("fcBlockFurnaceFullFront");
      this.iconFullFrontLit = register.registerIcon("fcBlockFurnaceFullFrontLit");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (this.isActive && rand.nextInt(3) == 0) {
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "fire.fire", 0.25F + rand.nextFloat() * 0.25F, 0.5F + rand.nextFloat() * 0.25F, false);
      }

      super.randomDisplayTick(world, i, j, k, rand);
   }
}
