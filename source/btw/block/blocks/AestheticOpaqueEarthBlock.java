package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.items.HoeItem;
import btw.world.util.BlockPos;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;

public class AestheticOpaqueEarthBlock extends Block {
   public static final int SUBTYPE_BLIGHT_LEVEL_0 = 0;
   public static final int SUBTYPE_BLIGHT_LEVEL_1 = 1;
   public static final int SUBTYPE_BLIGHT_LEVEL_2 = 2;
   public static final int SUBTYPE_BLIGHT_ROOTS_LEVEL_2 = 3;
   public static final int SUBTYPE_BLIGHT_LEVEL_3 = 4;
   public static final int SUBTYPE_BLIGHT_ROOTS_LEVEL_3 = 5;
   public static final int SUBTYPE_PACKED_EARTH = 6;
   public static final int SUBTYPE_DUNG = 7;
   public static final int[] subtypeToBlightLevel = new int[]{0, 1, 2, 2, 3, 3, -1, -1};
   public static final int[] blightLevelToSubtype = new int[]{0, 1, 2, 4};
   public static final int M_I_NUM_SUBTYPES = 8;
   @Environment(EnvType.CLIENT)
   private Icon iconDung;
   @Environment(EnvType.CLIENT)
   private Icon iconPackedEarth;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBlightLevel0SideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon[] iconBlightLevel1SideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon[] iconBlightLevel2SideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon[] iconBlightLevel3SideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon[] iconBlightRootsLevel2SideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconBlightRootsLevel3;

   public AestheticOpaqueEarthBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.setHoesEffectiveOn();
      this.b(true);
      this.a(h);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockAestheticOpaqueEarth");
   }

   @Override
   public int damageDropped(int iMetadata) {
      return this.isBlightFromMetadata(iMetadata) ? 0 : iMetadata;
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return this.isBlightFromMetadata(iMetadata) ? BTWBlocks.looseDirt.blockID : this.blockID;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (this.isBlightFromMetadata(iMetadata)) {
         if (!world.isRemote) {
            int iNumDropped = 8;

            for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
               this.b(world, i, j, k, new ItemStack(BTWItems.dirtPile));
            }
         }
      } else {
         super.dropBlockAsItemWithChance(world, i, j, k, iMetadata, fChance, iFortuneModifier);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      if (this.isBlightFromMetadata(iSubtype)) {
         this.blightRandomUpdateTick(world, i, j, k, rand);
      }
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      int iItemDamage = iMetadata;
      if (iMetadata == 1 || iMetadata == 2 || iMetadata == 3) {
         iItemDamage = 0;
      } else if (iMetadata == 5) {
         iItemDamage = 4;
      }

      return new ItemStack(this.blockID, 1, iItemDamage);
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      float fModifier = 1.0F;
      int iSubtype = world.getBlockMetadata(i, j, k);
      if (iSubtype == 6) {
         fModifier = 1.2F;
      } else if (iSubtype == 7) {
         fModifier = 0.8F;
      }

      return fModifier;
   }

   @Override
   public StepSound getStepSound(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype == 7 ? BTWBlocks.stepSoundSquish : this.stepSound;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.dropAsPiles(world, i, j, k, iMetadata, 1.0F);
      if (iMetadata != 7 && iMetadata != 6) {
         this.onDirtDugWithImproperTool(world, i, j, k);
      }
   }

   private void dropAsPiles(World world, int i, int j, int k, int iMetadata, float fChanceOfPileDrop) {
      Item itemToDrop = BTWItems.dirtPile;
      int iCountToDrop = 6;
      if (iMetadata == 6) {
         iCountToDrop = 12;
      } else if (iMetadata == 7) {
         itemToDrop = BTWItems.dung;
         iCountToDrop = 8;
      }

      for (int iTempCount = 0; iTempCount < iCountToDrop; iTempCount++) {
         if (world.rand.nextFloat() <= fChanceOfPileDrop) {
            ItemStack tempStack = new ItemStack(itemToDrop);
            this.b(world, i, j, k, tempStack);
         }
      }
   }

   @Override
   public boolean canDropFromExplosion(Explosion par1Explosion) {
      return false;
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      float fChanceOfPileDrop = 1.0F;
      if (explosion != null) {
         fChanceOfPileDrop = 1.0F / explosion.explosionSize;
      }

      this.dropAsPiles(world, i, j, k, iMetadata, fChanceOfPileDrop);
      if (iMetadata != 7 && iMetadata != 6) {
         this.onDirtDugWithImproperTool(world, i, j, k);
      }
   }

   @Override
   public boolean canSaplingsGrowOnBlock(World world, int i, int j, int k) {
      return this.isBlightFromMetadata(world.getBlockMetadata(i, j, k));
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return stack != null && stack.getItem() instanceof HoeItem;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      if (this.isBlightFromMetadata(world.getBlockMetadata(i, j, k))) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
         if (!world.isRemote) {
            world.playAuxSFX(2001, i, j, k, this.blockID);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isSurfaceBlightFromMetadata(int iMetadata) {
      return iMetadata >= 0 && iMetadata <= 4 && iMetadata != 3;
   }

   public boolean isBlightFromMetadata(int iMetadata) {
      return iMetadata >= 0 && iMetadata <= 5;
   }

   private int getRootsSubtypeForLevel(int iLevel) {
      return iLevel >= 3 ? 5 : 3;
   }

   private void blightRandomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.checkForBlightSurfaceConversions(world, i, j, k)) {
         int iBlockSubtype = world.getBlockMetadata(i, j, k);
         if (iBlockSubtype == 1) {
            this.blightKillLeaves(world, i, j, k, rand);
         } else if (iBlockSubtype >= 2) {
            this.blightKillVinesAndLeaves(world, i, j, k, rand);
         }

         if (world.provider.dimensionId != 1) {
            this.checkForBlightSpread(world, i, j, k, rand);
            this.checkForBlightEvolution(world, i, j, k, rand);
         }
      }
   }

   private void checkForSpreadToLocation(World world, int i, int j, int k, int iBlightSubtype) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      if (iTargetBlockID > 0) {
         int iBlightLevel = subtypeToBlightLevel[iBlightSubtype];
         if (iTargetBlockID == this.blockID) {
            int iTargetMetadata = world.getBlockMetadata(i, j, k);
            int iTargetBlightLevel = subtypeToBlightLevel[iTargetMetadata];
            if (iTargetBlightLevel < iBlightLevel && iTargetBlightLevel >= 0) {
               if (this.isSurfaceBlightFromMetadata(iTargetMetadata)) {
                  if (iBlightLevel == 3) {
                     iTargetBlightLevel = 3;
                  } else {
                     iTargetBlightLevel++;
                  }

                  world.setBlockMetadataWithNotify(i, j, k, blightLevelToSubtype[iTargetBlightLevel]);
               } else {
                  world.setBlockMetadataWithNotify(i, j, k, 5);
               }
            }
         } else {
            Block targetBlock = Block.blocksList[iTargetBlockID];
            if (targetBlock.getCanBlightSpreadToBlock(world, i, j, k, iBlightLevel)) {
               if (iBlightLevel < 3) {
                  if (Block.lightOpacity[world.getBlockId(i, j + 1, k)] <= 2) {
                     world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 0);
                  }
               } else if (Block.lightOpacity[world.getBlockId(i, j + 1, k)] <= 2) {
                  world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 4);
               } else {
                  world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 5);
               }
            }
         }
      }
   }

   private void checkForBlightSpread(World world, int i, int j, int k, Random rand) {
      int iBlockSubtype = world.getBlockMetadata(i, j, k);
      if (iBlockSubtype == 0) {
         int iRandI = i + rand.nextInt(3) - 1;
         int iRandJ = j + rand.nextInt(3) - 1;
         int iRandK = k + rand.nextInt(3) - 1;
         this.checkForSpreadToLocation(world, iRandI, iRandJ, iRandK, iBlockSubtype);
      } else if (iBlockSubtype == 1) {
         for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
            int iRandI = i + rand.nextInt(3) - 1;
            int iRandJ = j + rand.nextInt(4) - 1;
            int iRandK = k + rand.nextInt(3) - 1;
            this.checkForSpreadToLocation(world, iRandI, iRandJ, iRandK, iBlockSubtype);
         }
      } else {
         for (int iTempCount = 0; iTempCount < 4; iTempCount++) {
            int iRandI = i + rand.nextInt(3) - 1;
            int iRandJ = j + rand.nextInt(5) - 2;
            int iRandK = k + rand.nextInt(3) - 1;
            this.checkForSpreadToLocation(world, iRandI, iRandJ, iRandK, iBlockSubtype);
         }

         int iRootsSubtype = this.getRootsSubtypeForLevel(subtypeToBlightLevel[iBlockSubtype]);
         if (world.getBlockId(i, j - 1, k) == Block.dirt.blockID) {
            world.setBlockAndMetadataWithNotify(i, j - 1, k, this.blockID, iRootsSubtype);
         }

         if (world.getBlockId(i, j + 1, k) == Block.dirt.blockID) {
            world.setBlockAndMetadataWithNotify(i, j + 1, k, this.blockID, iRootsSubtype);
         }
      }
   }

   private void checkForBlightEvolution(World world, int i, int j, int k, Random rand) {
      int iBlockSubtype = world.getBlockMetadata(i, j, k);
      if (iBlockSubtype == 0) {
         int iRandomFacing = rand.nextInt(6);
         BlockPos targetPos = new BlockPos(i, j, k, iRandomFacing);
         if (world.getBlockMaterial(targetPos.x, targetPos.y, targetPos.z) == Material.water) {
            world.setBlockMetadataWithNotify(i, j, k, 1);
         }
      } else if (iBlockSubtype == 1) {
         int iRandomFacing = rand.nextInt(6);
         BlockPos targetPos = new BlockPos(i, j, k, iRandomFacing);
         if (world.getBlockMaterial(targetPos.x, targetPos.y, targetPos.z) == Material.lava) {
            world.setBlockMetadataWithNotify(i, j, k, 2);
         }
      } else if (iBlockSubtype == 2 || iBlockSubtype == 3) {
         int iRandI = i + rand.nextInt(7) - 3;
         int iRandJ = j + rand.nextInt(7) - 3;
         int iRandK = k + rand.nextInt(7) - 3;
         int iTargetBlockID = world.getBlockId(iRandI, iRandJ, iRandK);
         if (iTargetBlockID == Block.portal.blockID) {
            if (iBlockSubtype == 2) {
               world.setBlockMetadataWithNotify(i, j, k, 4);
            } else {
               world.setBlockMetadataWithNotify(i, j, k, 5);
            }
         }
      }
   }

   private boolean checkForBlightSurfaceConversions(World world, int i, int j, int k) {
      int iBlightSubtype = world.getBlockMetadata(i, j, k);
      int iBlockAboveID = world.getBlockId(i, j + 1, k);
      if (Block.lightOpacity[iBlockAboveID] > 2) {
         if (iBlightSubtype == 0) {
            world.setBlockWithNotify(i, j, k, Block.dirt.blockID);
            return true;
         }

         if (iBlightSubtype == 2) {
            world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 3);
            return true;
         }

         if (iBlightSubtype == 4) {
            world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 5);
            return true;
         }
      } else {
         if (iBlightSubtype == 3) {
            world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 2);
            return true;
         }

         if (iBlightSubtype == 5) {
            world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, 4);
            return true;
         }
      }

      return false;
   }

   private void blightKillLeaves(World world, int i, int j, int k, Random rand) {
      for (int iTempCount = 0; iTempCount < 4; iTempCount++) {
         int iRandI = i + rand.nextInt(3) - 1;
         int iRandJ = j + rand.nextInt(9);
         int iRandK = k + rand.nextInt(3) - 1;
         int iTargetBlockID = world.getBlockId(iRandI, iRandJ, iRandK);
         if (iTargetBlockID == Block.leaves.blockID) {
            world.setBlockWithNotify(iRandI, iRandJ, iRandK, 0);
         }
      }
   }

   private void blightKillVinesAndLeaves(World world, int i, int j, int k, Random rand) {
      for (int iTempCount = 0; iTempCount < 4; iTempCount++) {
         int iRandI = i + rand.nextInt(3) - 1;
         int iRandJ = j + rand.nextInt(9);
         int iRandK = k + rand.nextInt(3) - 1;
         int iTargetBlockID = world.getBlockId(iRandI, iRandJ, iRandK);
         if (iTargetBlockID == Block.leaves.blockID || iTargetBlockID == Block.vine.blockID) {
            world.setBlockWithNotify(iRandI, iRandJ, iRandK, 0);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("dirt");
      this.iconDung = register.registerIcon("fcBlockDung");
      this.iconPackedEarth = register.registerIcon("FCBlockPackedEarth");
      this.iconBlightLevel0SideArray[0] = register.registerIcon("FCBlockBlightL0_bottom");
      this.iconBlightLevel0SideArray[1] = register.registerIcon("FCBlockBlightL0_top");
      Icon sideIcon = register.registerIcon("FCBlockBlightL0_side");
      this.iconBlightLevel0SideArray[2] = sideIcon;
      this.iconBlightLevel0SideArray[3] = sideIcon;
      this.iconBlightLevel0SideArray[4] = sideIcon;
      this.iconBlightLevel0SideArray[5] = sideIcon;
      this.iconBlightLevel1SideArray[0] = register.registerIcon("FCBlockBlightL1_bottom");
      this.iconBlightLevel1SideArray[1] = register.registerIcon("FCBlockBlightL1_top");
      sideIcon = register.registerIcon("FCBlockBlightL1_side");
      this.iconBlightLevel1SideArray[2] = sideIcon;
      this.iconBlightLevel1SideArray[3] = sideIcon;
      this.iconBlightLevel1SideArray[4] = sideIcon;
      this.iconBlightLevel1SideArray[5] = sideIcon;
      this.iconBlightLevel2SideArray[0] = register.registerIcon("FCBlockBlightL2_bottom");
      this.iconBlightLevel2SideArray[1] = register.registerIcon("FCBlockBlightL2_top");
      sideIcon = register.registerIcon("FCBlockBlightL2_side");
      this.iconBlightLevel2SideArray[2] = sideIcon;
      this.iconBlightLevel2SideArray[3] = sideIcon;
      this.iconBlightLevel2SideArray[4] = sideIcon;
      this.iconBlightLevel2SideArray[5] = sideIcon;
      this.iconBlightLevel3SideArray[0] = register.registerIcon("FCBlockBlightL3_roots");
      this.iconBlightLevel3SideArray[1] = register.registerIcon("FCBlockBlightL3_top");
      sideIcon = register.registerIcon("FCBlockBlightL3_side");
      this.iconBlightLevel3SideArray[2] = sideIcon;
      this.iconBlightLevel3SideArray[3] = sideIcon;
      this.iconBlightLevel3SideArray[4] = sideIcon;
      this.iconBlightLevel3SideArray[5] = sideIcon;
      this.iconBlightRootsLevel2SideArray[0] = register.registerIcon("FCBlockBlightL2_bottom");
      sideIcon = register.registerIcon("FCBlockBlightL2_roots");
      this.iconBlightRootsLevel2SideArray[1] = sideIcon;
      this.iconBlightRootsLevel2SideArray[2] = sideIcon;
      this.iconBlightRootsLevel2SideArray[3] = sideIcon;
      this.iconBlightRootsLevel2SideArray[4] = sideIcon;
      this.iconBlightRootsLevel2SideArray[5] = sideIcon;
      this.iconBlightRootsLevel3 = register.registerIcon("FCBlockBlightL3_roots");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata == 0) {
         return this.iconBlightLevel0SideArray[iSide];
      } else if (iMetadata == 1) {
         return this.iconBlightLevel1SideArray[iSide];
      } else if (iMetadata == 2) {
         return this.iconBlightLevel2SideArray[iSide];
      } else if (iMetadata == 3) {
         return this.iconBlightRootsLevel2SideArray[iSide];
      } else if (iMetadata == 4) {
         return this.iconBlightLevel3SideArray[iSide];
      } else if (iMetadata == 5) {
         return this.iconBlightRootsLevel3;
      } else if (iMetadata == 6) {
         return this.iconPackedEarth;
      } else {
         return iMetadata == 7 ? this.iconDung : this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(blockID, 1, 0));
      list.add(new ItemStack(blockID, 1, 1));
      list.add(new ItemStack(blockID, 1, 2));
      list.add(new ItemStack(blockID, 1, 4));
      list.add(new ItemStack(blockID, 1, 3));
      list.add(new ItemStack(blockID, 1, 5));
      list.add(new ItemStack(blockID, 1, 6));
      list.add(new ItemStack(blockID, 1, 7));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getDamageValue(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      return metadata != 0 && metadata != 1 && metadata != 2 && metadata != 4 && metadata != 3 && metadata != 5
         ? super.getDamageValue(world, x, y, z)
         : metadata;
   }
}
