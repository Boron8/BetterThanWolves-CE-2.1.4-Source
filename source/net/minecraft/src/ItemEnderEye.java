package net.minecraft.src;

public class ItemEnderEye extends Item {
   public ItemEnderEye(int par1) {
      super(par1);
      this.e(4);
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public boolean onItemUse(
      ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10
   ) {
      int var11 = par3World.getBlockId(par4, par5, par6);
      int var12 = par3World.getBlockMetadata(par4, par5, par6);
      if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)
         || var11 != Block.endPortalFrame.blockID
         || BlockEndPortalFrame.isEnderEyeInserted(var12)) {
         return false;
      } else if (par3World.isRemote) {
         return true;
      } else {
         par3World.setBlockMetadataWithNotify(par4, par5, par6, var12 + 4, 2);
         par1ItemStack.stackSize--;

         for (int var13 = 0; var13 < 16; var13++) {
            double var14 = par4 + (5.0F + e.nextFloat() * 6.0F) / 16.0F;
            double var16 = par5 + 0.8125F;
            double var18 = par6 + (5.0F + e.nextFloat() * 6.0F) / 16.0F;
            double var20 = 0.0;
            double var22 = 0.0;
            double var24 = 0.0;
            par3World.spawnParticle("smoke", var14, var16, var18, var20, var22, var24);
         }

         int var261 = var12 & 3;
         int var26x = 0;
         int var15 = 0;
         boolean var27 = false;
         boolean var17 = true;
         int var28 = Direction.rotateRight[var261];

         for (int var19 = -2; var19 <= 2; var19++) {
            int var29 = par4 + Direction.offsetX[var28] * var19;
            int var21 = par6 + Direction.offsetZ[var28] * var19;
            int var30 = par3World.getBlockId(var29, par5, var21);
            if (var30 == Block.endPortalFrame.blockID) {
               int var23 = par3World.getBlockMetadata(var29, par5, var21);
               if (!BlockEndPortalFrame.isEnderEyeInserted(var23)) {
                  var17 = false;
                  break;
               }

               var15 = var19;
               if (!var27) {
                  var26x = var19;
                  var27 = true;
               }
            }
         }

         if (var17 && var15 == var26x + 2) {
            for (int var301 = var26x; var301 <= var15; var301++) {
               int var29 = par4 + Direction.offsetX[var28] * var301;
               int var21 = par6 + Direction.offsetZ[var28] * var301;
               var29 += Direction.offsetX[var261] * 4;
               var21 += Direction.offsetZ[var261] * 4;
               int var30x = par3World.getBlockId(var29, par5, var21);
               int var23x = par3World.getBlockMetadata(var29, par5, var21);
               if (var30x != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(var23x)) {
                  var17 = false;
                  break;
               }
            }

            for (int var311 = var26x - 1; var311 <= var15 + 1; var311 += 4) {
               for (int var29 = 1; var29 <= 3; var29++) {
                  int var21 = par4 + Direction.offsetX[var28] * var311;
                  int var30x = par6 + Direction.offsetZ[var28] * var311;
                  var21 += Direction.offsetX[var261] * var29;
                  var30x += Direction.offsetZ[var261] * var29;
                  int var23x = par3World.getBlockId(var21, par5, var30x);
                  int var31x = par3World.getBlockMetadata(var21, par5, var30x);
                  if (var23x != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(var31x)) {
                     var17 = false;
                     break;
                  }
               }
            }

            if (var17) {
               for (int var32 = var26x; var32 <= var15; var32++) {
                  for (int var29x = 1; var29x <= 3; var29x++) {
                     int var21 = par4 + Direction.offsetX[var28] * var32;
                     int var30x = par6 + Direction.offsetZ[var28] * var32;
                     var21 += Direction.offsetX[var261] * var29x;
                     var30x += Direction.offsetZ[var261] * var29x;
                     par3World.setBlock(var21, par5, var30x, Block.endPortal.blockID, 0, 2);
                  }
               }
            }
         }

         return true;
      }
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      MovingObjectPosition var4 = this.a(par2World, par3EntityPlayer, false);
      if (var4 != null && var4.typeOfHit == EnumMovingObjectType.TILE) {
         int var5 = par2World.getBlockId(var4.blockX, var4.blockY, var4.blockZ);
         if (var5 == Block.endPortalFrame.blockID) {
            return par1ItemStack;
         }
      }

      if (!par2World.isRemote) {
         ChunkPosition var7 = par2World.findClosestStructure("Stronghold", (int)par3EntityPlayer.posX, (int)par3EntityPlayer.posY, (int)par3EntityPlayer.posZ);
         if (var7 != null) {
            EntityEnderEye var6 = (EntityEnderEye)EntityList.createEntityOfType(
               EntityEnderEye.class, par2World, par3EntityPlayer.posX, par3EntityPlayer.posY + 1.62 - par3EntityPlayer.yOffset, par3EntityPlayer.posZ
            );
            var6.setItemDamage(par1ItemStack.getItemDamage());
            var6.moveTowards(var7.x, var7.y, var7.z);
            par2World.spawnEntityInWorld(var6);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
            par2World.playAuxSFXAtEntity((EntityPlayer)null, 1002, (int)par3EntityPlayer.posX, (int)par3EntityPlayer.posY, (int)par3EntityPlayer.posZ, 0);
            if (!par3EntityPlayer.capabilities.isCreativeMode) {
               par1ItemStack.stackSize--;
            }
         }
      }

      return par1ItemStack;
   }
}
