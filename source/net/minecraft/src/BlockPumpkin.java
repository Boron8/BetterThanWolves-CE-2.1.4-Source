package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockPumpkin extends BlockDirectional {
   private boolean blockType;
   @Environment(EnvType.CLIENT)
   private Icon field_94474_b;
   @Environment(EnvType.CLIENT)
   private Icon field_94475_c;

   protected BlockPumpkin(int par1, boolean par2) {
      super(par1, Material.pumpkin);
      this.b(true);
      this.blockType = par2;
      this.a(CreativeTabs.tabBlock);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return par1 == 1
         ? this.field_94474_b
         : (
            par1 == 0
               ? this.field_94474_b
               : (
                  par2 == 2 && par1 == 2
                     ? this.field_94475_c
                     : (
                        par2 == 3 && par1 == 5
                           ? this.field_94475_c
                           : (par2 == 0 && par1 == 3 ? this.field_94475_c : (par2 == 1 && par1 == 4 ? this.field_94475_c : this.blockIcon))
                     )
               )
         );
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.a(par1World, par2, par3, par4);
      if (par1World.getBlockId(par2, par3 - 1, par4) == Block.blockSnow.blockID && par1World.getBlockId(par2, par3 - 2, par4) == Block.blockSnow.blockID) {
         if (!par1World.isRemote) {
            par1World.setBlock(par2, par3, par4, 0, 0, 2);
            par1World.setBlock(par2, par3 - 1, par4, 0, 0, 2);
            par1World.setBlock(par2, par3 - 2, par4, 0, 0, 2);
            EntitySnowman var9 = (EntitySnowman)EntityList.createEntityOfType(EntitySnowman.class, par1World);
            var9.b(par2 + 0.5, par3 - 1.95, par4 + 0.5, 0.0F, 0.0F);
            par1World.spawnEntityInWorld(var9);
            par1World.notifyBlockChange(par2, par3, par4, 0);
            par1World.notifyBlockChange(par2, par3 - 1, par4, 0);
            par1World.notifyBlockChange(par2, par3 - 2, par4, 0);
         }

         for (int var10 = 0; var10 < 120; var10++) {
            par1World.spawnParticle(
               "snowshovel",
               par2 + par1World.rand.nextDouble(),
               par3 - 2 + par1World.rand.nextDouble() * 2.5,
               par4 + par1World.rand.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }
      } else if (par1World.getBlockId(par2, par3 - 1, par4) == Block.blockIron.blockID && par1World.getBlockId(par2, par3 - 2, par4) == Block.blockIron.blockID
         )
       {
         boolean var5 = par1World.getBlockId(par2 - 1, par3 - 1, par4) == Block.blockIron.blockID
            && par1World.getBlockId(par2 + 1, par3 - 1, par4) == Block.blockIron.blockID;
         boolean var6 = par1World.getBlockId(par2, par3 - 1, par4 - 1) == Block.blockIron.blockID
            && par1World.getBlockId(par2, par3 - 1, par4 + 1) == Block.blockIron.blockID;
         if (var5 || var6) {
            par1World.setBlock(par2, par3, par4, 0, 0, 2);
            par1World.setBlock(par2, par3 - 1, par4, 0, 0, 2);
            par1World.setBlock(par2, par3 - 2, par4, 0, 0, 2);
            if (var5) {
               par1World.setBlock(par2 - 1, par3 - 1, par4, 0, 0, 2);
               par1World.setBlock(par2 + 1, par3 - 1, par4, 0, 0, 2);
            } else {
               par1World.setBlock(par2, par3 - 1, par4 - 1, 0, 0, 2);
               par1World.setBlock(par2, par3 - 1, par4 + 1, 0, 0, 2);
            }

            EntityIronGolem var7 = (EntityIronGolem)EntityList.createEntityOfType(EntityIronGolem.class, par1World);
            var7.setPlayerCreated(true);
            var7.b(par2 + 0.5, par3 - 1.95, par4 + 0.5, 0.0F, 0.0F);
            par1World.spawnEntityInWorld(var7);

            for (int var8 = 0; var8 < 120; var8++) {
               par1World.spawnParticle(
                  "snowballpoof",
                  par2 + par1World.rand.nextDouble(),
                  par3 - 2 + par1World.rand.nextDouble() * 3.9,
                  par4 + par1World.rand.nextDouble(),
                  0.0,
                  0.0,
                  0.0
               );
            }

            par1World.notifyBlockChange(par2, par3, par4, 0);
            par1World.notifyBlockChange(par2, par3 - 1, par4, 0);
            par1World.notifyBlockChange(par2, par3 - 2, par4, 0);
            if (var5) {
               par1World.notifyBlockChange(par2 - 1, par3 - 1, par4, 0);
               par1World.notifyBlockChange(par2 + 1, par3 - 1, par4, 0);
            } else {
               par1World.notifyBlockChange(par2, par3 - 1, par4 - 1, 0);
               par1World.notifyBlockChange(par2, par3 - 1, par4 + 1, 0);
            }
         }
      }
   }

   @Override
   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockId(par2, par3, par4);
      return (var5 == 0 || Block.blocksList[var5].blockMaterial.isReplaceable()) && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
   }

   @Override
   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
      int var7 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 2.5) & 3;
      par1World.setBlockMetadataWithNotify(par2, par3, par4, var7, 2);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.field_94475_c = par1IconRegister.registerIcon(this.blockType ? "pumpkin_jack" : "pumpkin_face");
      this.field_94474_b = par1IconRegister.registerIcon("pumpkin_top");
      this.blockIcon = par1IconRegister.registerIcon("pumpkin_side");
   }
}
