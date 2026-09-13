package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import btw.item.items.ChiselItem;
import btw.item.util.ItemUtils;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WorkStumpBlock extends Block {
   @Environment(EnvType.CLIENT)
   public static final String[] sideTextureNames = new String[]{
      "fcBlockWorkStumpOak", "fcBlockWorkStumpSpruce", "fcBlockWorkStumpBirch", "fcBlockWorkStumpJungle"
   };
   @Environment(EnvType.CLIENT)
   public static final String[] topTextureNames = new String[]{"fcBlockTrunkTop", "fcBlockTrunkTopSpruce", "fcBlockTrunkTopBirch", "fcBlockTrunkTopJungle"};
   @Environment(EnvType.CLIENT)
   private Icon[] iconSideArray;
   @Environment(EnvType.CLIENT)
   private Icon[] iconTopArray;

   public WorkStumpBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.logMaterial);
      this.c(1.25F);
      this.setChiselsEffectiveOn();
      this.setFireProperties(Flammability.LOGS);
      this.c("fcBlockWorkStump");
   }

   @Override
   public float getBlockHardness(World world, int i, int j, int k) {
      return super.getBlockHardness(world, i, j, k) * 3.0F;
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (!world.isRemote && !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j + 1, k, 0)) {
         int metadata = world.getBlockMetadata(i, j, k);
         if (this.isFinishedWorkStump(metadata)) {
            player.displayGUIWorkbench(i, j, k);
         }
      }

      return true;
   }

   private boolean isFinishedWorkStump(int metadata) {
      return (metadata & 8) == 0;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 6, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (!world.isRemote) {
         this.b(world, i, j, k, new ItemStack(BTWItems.sawDust, 3, 0));
         this.b(world, i, j, k, new ItemStack(Block.planks.blockID, 1, 0));
      }
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int oldMetadata = world.getBlockMetadata(i, j, k);
      int chewedLogID = LogBlock.chewedLogArray[oldMetadata & 3].blockID;
      if (!this.isFinishedWorkStump(oldMetadata) && this.isWorkStumpItemConversionTool(stack, world, i, j, k)) {
         world.playAuxSFX(2268, i, j, k, 0);
         world.setBlockMetadataWithNotify(i, j, k, oldMetadata & 3);
         return true;
      } else {
         int newMetadata = BTWBlocks.oakChewedLog.setIsStump(0);
         world.setBlockAndMetadataWithNotify(i, j, k, chewedLogID, newMetadata);
         if (!world.isRemote) {
            ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.bark, 1, oldMetadata & 3), iFromSide);
         }

         return true;
      }
   }

   public boolean isWorkStumpItemConversionTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null && stack.getItem() instanceof ChiselItem) {
         int iToolLevel = ((ChiselItem)stack.getItem()).toolMaterial.getHarvestLevel();
         return iToolLevel >= 2;
      } else {
         return false;
      }
   }

   @Override
   public boolean getIsProblemToRemove(ItemStack toolStack, IBlockAccess blockAccess, int i, int j, int k) {
      int metadata = blockAccess.getBlockMetadata(i, j, k);
      return this.isFinishedWorkStump(metadata) || !this.isWorkStumpItemConversionTool(toolStack, (World)blockAccess, i, j, k);
   }

   @Override
   public boolean getDoesStumpRemoverWorkOnBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return Block.wood.getStackRetrievedByBlockDispenser(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide > 1) {
         return this.iconSideArray[iMetadata & 3];
      } else if (iSide == 1) {
         return !this.isFinishedWorkStump(iMetadata) ? this.iconTopArray[iMetadata & 3] : this.blockIcon;
      } else {
         return this.iconTopArray[iMetadata & 3];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("workbench_top");
      this.iconSideArray = new Icon[sideTextureNames.length];
      this.iconTopArray = new Icon[sideTextureNames.length];

      for (int iTextureID = 0; iTextureID < this.iconSideArray.length; iTextureID++) {
         this.iconSideArray[iTextureID] = register.registerIcon(sideTextureNames[iTextureID]);
         this.iconTopArray[iTextureID] = register.registerIcon(topTextureNames[iTextureID]);
      }
   }
}
