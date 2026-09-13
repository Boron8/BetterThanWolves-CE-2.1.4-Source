package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;

public class AestheticOpaqueBlock extends Block {
   public static final int SUBTYPE_WICKER = 0;
   public static final int SUBTYPE_DUNG = 1;
   public static final int SUBTYPE_STEEL = 2;
   public static final int SUBTYPE_HELLFIRE = 3;
   public static final int SUBTYPE_PADDING = 4;
   public static final int SUBTYPE_SOAP = 5;
   public static final int SUBTYPE_ROPE = 6;
   public static final int SUBTYPE_FLINT = 7;
   public static final int SUBTYPE_NETHERRACK_WITH_GROWTH = 8;
   public static final int SUBTYPE_WHITE_STONE = 9;
   public static final int SUBTYPE_WHITE_COBBLE = 10;
   public static final int SUBTYPE_BARREL = 11;
   public static final int SUBTYPE_CHOPPING_BLOCK_DIRTY = 12;
   public static final int SUBTYPE_CHOPPING_BLOCK_CLEAN = 13;
   public static final int SUBTYPE_ENDER_BLOCK = 14;
   public static final int SUBTYPE_BONE = 15;
   public static final int NUM_SUBTYPES = 16;
   private static final float DEFAULT_HARDNESS = 2.0F;
   @Environment(EnvType.CLIENT)
   private Icon iconWicker;
   @Environment(EnvType.CLIENT)
   private Icon iconDung;
   @Environment(EnvType.CLIENT)
   private Icon iconSteel;
   @Environment(EnvType.CLIENT)
   private Icon iconHellfire;
   @Environment(EnvType.CLIENT)
   private Icon iconPadding;
   @Environment(EnvType.CLIENT)
   private Icon iconSoap;
   @Environment(EnvType.CLIENT)
   private Icon iconSoapTop;
   @Environment(EnvType.CLIENT)
   private Icon iconRopeSide;
   @Environment(EnvType.CLIENT)
   private Icon iconRopeTop;
   @Environment(EnvType.CLIENT)
   private Icon iconFlint;
   @Environment(EnvType.CLIENT)
   private Icon iconNetherrackWithGrothSide;
   @Environment(EnvType.CLIENT)
   private Icon iconNetherrackWithGrothTop;
   @Environment(EnvType.CLIENT)
   private Icon iconNetherrackWithGrothBottom;
   @Environment(EnvType.CLIENT)
   private Icon iconWhiteStone;
   @Environment(EnvType.CLIENT)
   private Icon iconWhiteCobble;
   @Environment(EnvType.CLIENT)
   private Icon iconBarrelTop;
   @Environment(EnvType.CLIENT)
   private Icon iconBarrelSide;
   @Environment(EnvType.CLIENT)
   private Icon iconChoppingBlock;
   @Environment(EnvType.CLIENT)
   private Icon iconChoppingBlockDirty;
   @Environment(EnvType.CLIENT)
   private Icon iconEnderBlock;
   @Environment(EnvType.CLIENT)
   private Icon iconBoneSide;
   @Environment(EnvType.CLIENT)
   private Icon iconBoneTop;

   public AestheticOpaqueBlock(int blockID) {
      super(blockID, BTWBlocks.miscMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setPicksEffectiveOn(true);
      this.a(j);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockAestheticOpaque");
   }

   @Override
   public int damageDropped(int metadata) {
      if (metadata == 0) {
         return 0;
      } else if (metadata == 2) {
         return 0;
      } else if (metadata == 7) {
         return 0;
      } else if (metadata == 8) {
         return 0;
      } else {
         return metadata == 9 ? 10 : metadata;
      }
   }

   @Override
   public int idDropped(int metadata, Random random, int fortuneModifier) {
      if (metadata == 0) {
         return BTWBlocks.wickerBlock.blockID;
      } else if (metadata == 2) {
         return BTWBlocks.soulforgedSteelBlock.blockID;
      } else if (metadata == 7) {
         return Item.flint.itemID;
      } else {
         return metadata == 8 ? Block.netherrack.blockID : this.blockID;
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int metadata, float chance, int fortuneModifier) {
      if (metadata == 7) {
         if (world.isRemote) {
            return;
         }

         int iNumDropped = 9;

         for (int k1 = 0; k1 < iNumDropped; k1++) {
            int iItemID = this.idDropped(metadata, world.rand, fortuneModifier);
            if (iItemID > 0) {
               this.b(world, i, j, k, new ItemStack(iItemID, 1, this.damageDropped(metadata)));
            }
         }
      } else {
         super.dropBlockAsItemWithChance(world, i, j, k, metadata, chance, fortuneModifier);
      }
   }

   @Override
   protected boolean canSilkHarvest(int metadata) {
      return metadata != 8 && metadata != 2;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int changedBlockID) {
      int iSubType = world.getBlockMetadata(i, j, k);
      if (iSubType == 8) {
         int iBlockAboveID = world.getBlockId(i, j + 1, k);
         if (iBlockAboveID != BTWBlocks.netherGroth.blockID) {
            world.setBlock(i, j, k, Block.netherrack.blockID);
         }
      }
   }

   @Override
   public boolean doesInfiniteBurnToFacing(IBlockAccess blockAccess, int i, int j, int k, int facing) {
      int iSubType = blockAccess.getBlockMetadata(i, j, k);
      return iSubType == 3;
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype != 0
         && iSubtype != 1
         && iSubtype != 4
         && iSubtype != 5
         && iSubtype != 6
         && iSubtype != 11
         && iSubtype != 12
         && iSubtype != 13
         && iSubtype != 15;
   }

   @Override
   public boolean onBlockSawed(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype != 12 && iSubtype != 13 ? super.onBlockSawed(world, i, j, k) : false;
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype == 1 ? 1.0F : 1.2F;
   }

   @Override
   public StepSound getStepSound(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      if (iSubtype == 1) {
         return BTWBlocks.stepSoundSquish;
      } else {
         return iSubtype == 15 ? h : this.stepSound;
      }
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype == 1 || iSubtype == 15 || iSubtype == 5;
   }

   @Override
   public boolean canToolsStickInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype != 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
      this.iconWicker = register.registerIcon("fcBlockWicker");
      this.iconDung = register.registerIcon("fcBlockDung");
      this.iconSteel = register.registerIcon("fcBlockSoulforgedSteel");
      this.iconHellfire = register.registerIcon("fcBlockConcentratedHellfire");
      this.iconPadding = register.registerIcon("fcBlockPadding");
      this.iconSoap = register.registerIcon("fcBlockSoap");
      this.iconSoapTop = register.registerIcon("fcBlockSoap_top");
      this.iconRopeSide = register.registerIcon("fcBlockRope_side");
      this.iconRopeTop = register.registerIcon("fcBlockRope_top");
      this.iconFlint = register.registerIcon("bedrock");
      this.iconNetherrackWithGrothSide = register.registerIcon("fcBlockNetherrackGrothed_side");
      this.iconNetherrackWithGrothTop = register.registerIcon("fcBlockNetherrackGrothed_top");
      this.iconNetherrackWithGrothBottom = register.registerIcon("fcBlockNetherrackGrothed_bottom");
      this.iconWhiteStone = register.registerIcon("fcBlockWhiteStone");
      this.iconWhiteCobble = register.registerIcon("fcBlockWhiteCobble");
      this.iconBarrelTop = register.registerIcon("fcBlockBarrel_top");
      this.iconBarrelSide = register.registerIcon("fcBlockBarrel_side");
      this.iconChoppingBlock = register.registerIcon("fcBlockChoppingBlock");
      this.iconChoppingBlockDirty = register.registerIcon("fcBlockChoppingBlock_dirty");
      this.iconEnderBlock = register.registerIcon("fcBlockEnder");
      this.iconBoneSide = register.registerIcon("fcBlockBone_side");
      this.iconBoneTop = register.registerIcon("fcBlockBone_top");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int side, int metaData) {
      switch (metaData) {
         case 0:
            return this.iconWicker;
         case 1:
            return this.iconDung;
         case 2:
            return this.iconSteel;
         case 3:
            return this.iconHellfire;
         case 4:
            return this.iconPadding;
         case 5:
            if (side == 1) {
               return this.iconSoapTop;
            }

            return this.iconSoap;
         case 6:
            if (side < 2) {
               return this.iconRopeTop;
            }

            return this.iconRopeSide;
         case 7:
            return this.iconFlint;
         case 8:
            if (side == 0) {
               return this.iconNetherrackWithGrothBottom;
            } else {
               if (side == 1) {
                  return this.iconNetherrackWithGrothTop;
               }

               return this.iconNetherrackWithGrothSide;
            }
         case 9:
            return this.iconWhiteStone;
         case 10:
            return this.iconWhiteCobble;
         case 11:
            if (side < 2) {
               return this.iconBarrelTop;
            }

            return this.iconBarrelSide;
         case 12:
            return this.iconChoppingBlockDirty;
         case 13:
            return this.iconChoppingBlock;
         case 14:
            return this.iconEnderBlock;
         case 15:
            if (side < 2) {
               return this.iconBoneTop;
            }

            return this.iconBoneSide;
         default:
            return this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(blockID, 1, 3));
      list.add(new ItemStack(blockID, 1, 4));
      list.add(new ItemStack(blockID, 1, 5));
      list.add(new ItemStack(blockID, 1, 6));
      list.add(new ItemStack(blockID, 1, 7));
      list.add(new ItemStack(blockID, 1, 9));
      list.add(new ItemStack(blockID, 1, 10));
      list.add(new ItemStack(blockID, 1, 11));
      list.add(new ItemStack(blockID, 1, 12));
      list.add(new ItemStack(blockID, 1, 13));
      list.add(new ItemStack(blockID, 1, 14));
      list.add(new ItemStack(blockID, 1, 15));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      return metadata == 7 ? this.blockID : this.idDropped(metadata, world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 7) {
         return 7;
      } else {
         return metadata == 9 ? 9 : super.getDamageValue(world, x, y, z);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int side) {
      return side == 1 && blockAccess.getBlockId(i, j, k) == BTWBlocks.netherGroth.blockID ? false : !blockAccess.isBlockOpaqueCube(i, j, k);
   }
}
