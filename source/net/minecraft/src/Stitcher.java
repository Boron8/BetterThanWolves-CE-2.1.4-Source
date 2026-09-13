package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Stitcher {
   private final Set setStitchHolders = new HashSet(256);
   private final List stitchSlots = new ArrayList(256);
   private int currentWidth = 0;
   private int currentHeight = 0;
   private final int maxWidth;
   private final int maxHeight;
   private final boolean forcePowerOf2;
   private final int maxTileDimension;
   private Texture atlasTexture;
   private final String textureName;

   public Stitcher(String var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, 0);
   }

   public Stitcher(String var1, int var2, int var3, boolean var4, int var5) {
      this.textureName = var1;
      this.maxWidth = var2;
      this.maxHeight = var3;
      this.forcePowerOf2 = var4;
      this.maxTileDimension = var5;
   }

   public void addStitchHolder(StitchHolder var1) {
      if (this.maxTileDimension > 0) {
         var1.setNewDimension(this.maxTileDimension);
      }

      this.setStitchHolders.add(var1);
   }

   public Texture getTexture() {
      if (this.forcePowerOf2) {
         this.currentWidth = this.getCeilPowerOf2(this.currentWidth);
         this.currentHeight = this.getCeilPowerOf2(this.currentHeight);
      }

      this.atlasTexture = TextureManager.instance().createEmptyTexture(this.textureName, 1, this.currentWidth, this.currentHeight, 6408);
      this.atlasTexture.fillRect(this.atlasTexture.getTextureRect(), -65536);
      List var1 = this.getStichSlots();

      for (int var2 = 0; var2 < var1.size(); var2++) {
         StitchSlot var3 = (StitchSlot)var1.get(var2);
         StitchHolder var4 = var3.getStitchHolder();
         this.atlasTexture.copyFrom(var3.getOriginX(), var3.getOriginY(), var4.func_98150_a(), var4.isRotated());
      }

      TextureManager.instance().registerTexture(this.textureName, this.atlasTexture);
      return this.atlasTexture;
   }

   public void doStitch() {
      StitchHolder[] var1 = this.setStitchHolders.toArray(new StitchHolder[this.setStitchHolders.size()]);
      Arrays.sort((Object[])var1);
      this.atlasTexture = null;

      for (int var2 = 0; var2 < var1.length; var2++) {
         StitchHolder var3 = var1[var2];
         if (!this.allocateSlot(var3)) {
            throw new StitcherException(var3);
         }
      }
   }

   public List getStichSlots() {
      ArrayList var1 = new ArrayList();

      for (StitchSlot var3 : this.stitchSlots) {
         var3.getAllStitchSlots(var1);
      }

      return var1;
   }

   private int getCeilPowerOf2(int var1) {
      int var2 = var1 - 1;
      var2 |= var2 >> 1;
      var2 |= var2 >> 2;
      var2 |= var2 >> 4;
      var2 |= var2 >> 8;
      var2 |= var2 >> 16;
      return var2 + 1;
   }

   private boolean allocateSlot(StitchHolder var1) {
      for (int var2 = 0; var2 < this.stitchSlots.size(); var2++) {
         if (((StitchSlot)this.stitchSlots.get(var2)).func_94182_a(var1)) {
            return true;
         }

         var1.rotate();
         if (((StitchSlot)this.stitchSlots.get(var2)).func_94182_a(var1)) {
            return true;
         }

         var1.rotate();
      }

      return this.expandAndAllocateSlot(var1);
   }

   private boolean expandAndAllocateSlot(StitchHolder var1) {
      int var2 = Math.min(var1.getHeight(), var1.getWidth());
      boolean var3 = this.currentWidth == 0 && this.currentHeight == 0;
      boolean var4;
      if (this.forcePowerOf2) {
         int var5 = this.getCeilPowerOf2(this.currentWidth);
         int var6 = this.getCeilPowerOf2(this.currentHeight);
         int var7 = this.getCeilPowerOf2(this.currentWidth + var2);
         int var8 = this.getCeilPowerOf2(this.currentHeight + var2);
         boolean var9 = var7 <= this.maxWidth;
         boolean var10 = var8 <= this.maxHeight;
         if (!var9 && !var10) {
            return false;
         }

         int var11 = Math.max(var1.getHeight(), var1.getWidth());
         if (var3 && !var9 && this.getCeilPowerOf2(this.currentHeight + var11) > this.maxHeight) {
            return false;
         }

         boolean var12 = var5 != var7;
         boolean var13 = var6 != var8;
         if (var12 ^ var13) {
            var4 = var12 && var9;
         } else {
            var4 = var9 && var5 <= var6;
         }
      } else {
         boolean var14 = this.currentWidth + var2 <= this.maxWidth;
         boolean var16 = this.currentHeight + var2 <= this.maxHeight;
         if (!var14 && !var16) {
            return false;
         }

         var4 = (var3 || this.currentWidth <= this.currentHeight) && var14;
      }

      StitchSlot var15;
      if (var4) {
         if (var1.getWidth() > var1.getHeight()) {
            var1.rotate();
         }

         if (this.currentHeight == 0) {
            this.currentHeight = var1.getHeight();
         }

         var15 = new StitchSlot(this.currentWidth, 0, var1.getWidth(), this.currentHeight);
         this.currentWidth = this.currentWidth + var1.getWidth();
      } else {
         var15 = new StitchSlot(0, this.currentHeight, this.currentWidth, var1.getHeight());
         this.currentHeight = this.currentHeight + var1.getHeight();
      }

      var15.func_94182_a(var1);
      this.stitchSlots.add(var15);
      return true;
   }
}
