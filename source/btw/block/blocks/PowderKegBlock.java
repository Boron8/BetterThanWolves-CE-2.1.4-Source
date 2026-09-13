package btw.block.blocks;

import btw.block.util.Flammability;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockTNT;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.Explosion;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class PowderKegBlock extends BlockTNT {
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];

   public PowderKegBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.0F);
      this.setBuoyant();
      this.setFireProperties(Flammability.EXPLOSIVES);
      this.a(i);
      this.c("tnt");
   }

   @Override
   public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {
      if (!par1World.isRemote) {
         EntityLiving explosionOwner = null;
         if (par5Explosion != null) {
            explosionOwner = par5Explosion.func_94613_c();
         }

         EntityTNTPrimed var6;
         if (explosionOwner == null) {
            var6 = (EntityTNTPrimed)EntityList.createEntityOfType(
               EntityTNTPrimed.class, par1World, (double)(par2 + 0.5F), (double)(par3 + 0.5F), (double)(par4 + 0.5F)
            );
         } else {
            var6 = (EntityTNTPrimed)EntityList.createEntityOfType(
               EntityTNTPrimed.class, par1World, (double)(par2 + 0.5F), (double)(par3 + 0.5F), (double)(par4 + 0.5F), explosionOwner
            );
         }

         var6.fuse = par1World.rand.nextInt(var6.fuse / 4) + var6.fuse / 8;
         par1World.spawnEntityInWorld(var6);
      }
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
      this.g(world, i, j, k, 1);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon bottomIcon = register.registerIcon("fcBlockPowderKeg_bottom");
      this.blockIcon = bottomIcon;
      this.iconBySideArray[0] = bottomIcon;
      this.iconBySideArray[1] = register.registerIcon("fcBlockPowderKeg_top");
      Icon sideIcon = register.registerIcon("fcBlockPowderKeg_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconBySideArray[iSide];
   }
}
