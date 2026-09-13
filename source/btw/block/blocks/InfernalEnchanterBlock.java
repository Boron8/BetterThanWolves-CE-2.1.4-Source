package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.block.tileentity.InfernalEnchanterTileEntity;
import btw.client.render.util.RenderUtils;
import btw.inventory.BTWContainers;
import btw.inventory.container.InfernalEnchanterContainer;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class InfernalEnchanterBlock extends BlockContainer {
   public static final float BLOCK_HEIGHT = 0.5F;
   public static final float CANDLE_HEIGHT = 0.25F;
   private static final float BLOCK_HARDNESS = 100.0F;
   private static final float BLOCK_EXPLOSION_RESISTANCE = 2000.0F;
   private static final int HORIZONTAL_BOOK_SHELF_CHECK_DISTANCE = 8;
   private static final int VERTICAL_POSITIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
   private static final int VERTICAL_NEGATIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconCandle;

   public InfernalEnchanterBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.soulforgedSteelMaterial);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.k(0);
      this.c(100.0F);
      this.b(2000.0F);
      this.a(k);
      this.c("fcBlockInfernalEnchanter");
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new InfernalEnchanterTileEntity();
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote && player instanceof EntityPlayerMP) {
         InfernalEnchanterContainer container = new InfernalEnchanterContainer(player.inventory, world, i, j, k);
         BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.infernalEnchanterContainerID);
      }

      return true;
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

   private boolean isValidBookshelf(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == Block.bookShelf.blockID
         && (world.isAirBlock(i + 1, j, k) || world.isAirBlock(i - 1, j, k) || world.isAirBlock(i, j, k + 1) || world.isAirBlock(i, j, k - 1));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon bottomIcon = register.registerIcon("fcBlockInfernalEnchanter_bottom");
      this.blockIcon = bottomIcon;
      this.iconBySideArray[0] = bottomIcon;
      this.iconBySideArray[1] = register.registerIcon("fcBlockInfernalEnchanter_top");
      Icon sideIcon = register.registerIcon("fcBlockInfernalEnchanter_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
      this.iconCandle = register.registerIcon("fcBlockCandle_c00");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      super.b(world, i, j, k, random);
      this.displayMagicLetters(world, i, j, k, random);
   }

   @Environment(EnvType.CLIENT)
   private void displayMagicLetters(World world, int i, int j, int k, Random rand) {
      TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
      if (tileEntity != null && tileEntity instanceof InfernalEnchanterTileEntity) {
         InfernalEnchanterTileEntity enchanterEntity = (InfernalEnchanterTileEntity)tileEntity;
         if (enchanterEntity.playerNear) {
            for (int iTempCount = 0; iTempCount < 64; iTempCount++) {
               int iTargetI = rand.nextInt(17) - 8 + i;
               int iTargetJ = rand.nextInt(17) - 8 + j;
               int iTargetK = rand.nextInt(17) - 8 + k;
               if (this.isValidBookshelf(world, iTargetI, iTargetJ, iTargetK)) {
                  Vec3 velocity = Vec3.createVectorHelper(iTargetI - i, iTargetJ - j, iTargetK - k);
                  world.spawnParticle("enchantmenttable", i + 0.5, j + 0.5, k + 0.5, velocity.xCoord, velocity.yCoord, velocity.zCoord);
               }
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderStandardBlock(this, i, j, k);
      renderer.setRenderBounds(0.0625, 0.5, 0.0625, 0.1875, 0.75, 0.1875);
      RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconCandle);
      renderer.setRenderBounds(0.8125, 0.5, 0.0625, 0.9375, 0.75, 0.1875);
      RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconCandle);
      renderer.setRenderBounds(0.0625, 0.5, 0.8125, 0.1875, 0.75, 0.9375);
      RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconCandle);
      renderer.setRenderBounds(0.8125, 0.5, 0.8125, 0.9375, 0.75, 0.9375);
      RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconCandle);
      return true;
   }
}
