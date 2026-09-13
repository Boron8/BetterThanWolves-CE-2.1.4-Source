package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockDaylightDetector;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class DaylightDetectorBlock extends BlockDaylightDetector {
   public DaylightDetectorBlock(int par1) {
      super(par1);
      this.a(null);
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return 0;
   }

   @Override
   public void updateLightLevel(World par1World, int par2, int par3, int par4) {
   }

   @Override
   public boolean canProvidePower() {
      return false;
   }

   @Override
   public TileEntity createNewTileEntity(World par1World) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon("fcBlockStub");
   }
}
