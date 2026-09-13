package btw.block.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class PillarBlock extends Block {
   public String[] topTextures;
   public String[] sideTextures;
   public Icon[] topIcons;
   public Icon[] sideIcons;

   protected PillarBlock(int id, Material material, String[] topTextures, String[] sideTextures) {
      super(id, material);
      this.topTextures = topTextures;
      this.sideTextures = sideTextures;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess access, int x, int y, int z) {
      return (access.getBlockMetadata(x, y, z) & 12) != 0;
   }

   @Override
   public int rotateMetadataAroundJAxis(int meta, boolean var2) {
      int directionMeta = meta & 12;
      if (directionMeta != 0) {
         if (directionMeta == 4) {
            directionMeta = 8;
         } else if (directionMeta == 8) {
            directionMeta = 4;
         }

         meta = meta & -13 | directionMeta;
      }

      return meta;
   }

   @Override
   public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
      int type = meta & 3;
      byte directionMeta = 0;
      switch (side) {
         case 0:
         case 1:
            directionMeta = 0;
            break;
         case 2:
         case 3:
            directionMeta = 8;
            break;
         case 4:
         case 5:
            directionMeta = 4;
      }

      return type | directionMeta;
   }

   @Override
   public int damageDropped(int meta) {
      return meta & 3;
   }

   public static int limitToValidMetadata(int meta) {
      return meta & 3;
   }

   @Override
   public Icon getIcon(int side, int meta) {
      int directionMeta = meta & 12;
      int type = meta & 3;
      return (directionMeta != 0 || side != 0 && side != 1) && (directionMeta != 4 || side != 4 && side != 5) && (directionMeta != 8 || side != 2 && side != 3)
         ? this.sideIcons[type]
         : this.topIcons[type];
   }

   @Override
   public void registerIcons(IconRegister register) {
      this.topIcons = new Icon[this.topTextures.length];
      this.sideIcons = new Icon[this.topTextures.length];

      for (int i = 0; i < this.topTextures.length; i++) {
         this.topIcons[i] = register.registerIcon(this.topTextures[i]);
         this.sideIcons[i] = register.registerIcon(this.sideTextures[i]);
      }
   }

   @Override
   public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
      int meta = render.blockAccess.getBlockMetadata(x, y, z);
      int directionMeta = meta & 12;
      if (directionMeta == 4) {
         render.setUVRotateTop(1);
         render.setUVRotateBottom(1);
         render.setUVRotateWest(1);
         render.setUVRotateEast(1);
      } else if (directionMeta == 8) {
         render.setUVRotateNorth(1);
         render.setUVRotateSouth(1);
      }

      render.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      render.renderStandardBlock(this, x, y, z);
      render.clearUVRotation();
      return true;
   }
}
