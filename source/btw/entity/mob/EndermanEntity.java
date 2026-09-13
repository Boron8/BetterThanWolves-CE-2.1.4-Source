package btw.entity.mob;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityDamageSourceIndirect;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class EndermanEntity extends EntityEnderman {
   public static boolean[] portableBlocks = new boolean[4096];
   protected int teleportDelay = 0;
   protected int aggressionCounter = 0;
   protected boolean shouldBeScreaming;
   private static int maxEnstonePlaceWeight;
   private static final int ENDSTONE_PLACE_WEIGHT_POWER = 8;
   private static int[] endstonePlacementNeighborWeights = new int[7];

   public EndermanEntity(World world) {
      super(world);
   }

   @Override
   protected void entityInit() {
      this.entityCreatureEntityInit();
      this.dataWatcher.addObject(16, new Integer(0));
      this.dataWatcher.addObject(17, new Byte((byte)0));
      this.dataWatcher.addObject(18, new Byte((byte)0));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      if (this.entityToAttack != null && this.entityToAttack instanceof EntityPlayer) {
         tag.setString("playerToAttack", ((EntityPlayer)this.entityToAttack).username);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("playerToAttack")) {
         String playerName = tag.getString("playerToAttack");
         EntityPlayer player = this.worldObj.getPlayerEntityByName(playerName);
         if (player != null && this.d(player) <= 64.0F) {
            this.entityToAttack = player;
         }
      }
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      super.dropFewItems(bKilledByPlayer, iLootingModifier);
      this.dropCarriedBlock();
   }

   @Override
   public void setCarried(int iBlockID) {
      this.dataWatcher.updateObject(16, iBlockID);
   }

   @Override
   public int getCarried() {
      return this.dataWatcher.getWatchableObjectInt(16);
   }

   @Override
   protected Entity findPlayerToAttack() {
      EntityPlayer target = this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0);
      if (target != null && this.isPlayerStaringAtMe(target)) {
         this.shouldBeScreaming = true;
         if (this.aggressionCounter == 0) {
            this.worldObj.playSoundAtEntity(target, "mob.endermen.stare", 1.0F, 1.0F);
         }

         this.aggressionCounter++;
         if (this.aggressionCounter > 5) {
            this.aggressionCounter = 0;
            this.a(true);
            this.angerNearbyEndermen(target);
            return target;
         }
      } else {
         this.aggressionCounter = 0;
      }

      return null;
   }

   @Override
   public void onLivingUpdate() {
      this.moveSpeed = 0.3F;
      if (this.entityToAttack != null) {
         this.moveSpeed = 6.5F;
      }

      if (this.F()) {
         this.attackEntityFrom(DamageSource.drown, 1);
      }

      if (!this.worldObj.isRemote) {
         if (this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") && this.entityToAttack == null) {
            if (this.getCarried() == 0) {
               if (!this.updateWithoutCarriedBlock()) {
                  return;
               }
            } else if (!this.updateWithCarriedBlock()) {
               return;
            }
         }

         if (this.worldObj.isDaytime()) {
            float fBrightness = this.c(1.0F);
            if (fBrightness > 0.5F
               && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
               && this.rand.nextFloat() * 30.0F < (fBrightness - 0.4F) * 2.0F) {
               this.panic();
            }
         }
      } else {
         this.emitParticles();
      }

      if (this.F() || this.ae()) {
         this.panic();
      }

      if (this.q() && !this.shouldBeScreaming && this.rand.nextInt(100) == 0) {
         this.a(false);
      }

      this.isJumping = false;
      if (this.entityToAttack != null) {
         this.a(this.entityToAttack, 100.0F, 100.0F);
      }

      if (!this.worldObj.isRemote && this.R()) {
         if (this.entityToAttack != null) {
            if (this.entityToAttack instanceof EntityPlayer && this.isPlayerStaringAtMe((EntityPlayer)this.entityToAttack)) {
               this.moveStrafing = this.moveForward = 0.0F;
               this.moveSpeed = 0.0F;
               if (this.entityToAttack.getDistanceSqToEntity(this) < 16.0) {
                  this.m();
               }

               this.teleportDelay = 0;
            } else if (this.entityToAttack.getDistanceSqToEntity(this) > 256.0 && this.teleportDelay++ >= 30 && this.p(this.entityToAttack)) {
               this.teleportDelay = 0;
            }
         } else {
            this.a(false);
            this.teleportDelay = 0;
         }
      }

      this.entityMobOnLivingUpdate();
   }

   @Override
   public boolean attackEntityFrom(DamageSource source, int iDamage) {
      if (!this.aq()) {
         this.a(true);
         if (source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer) {
            this.shouldBeScreaming = true;
         }

         if (source instanceof EntityDamageSourceIndirect) {
            this.shouldBeScreaming = false;

            for (int iTempCount = 0; iTempCount < 64; iTempCount++) {
               if (this.m()) {
                  return true;
               }
            }

            return false;
         } else if (!(source.getEntity() instanceof EntityPlayer)) {
            return this.entityMobAttackEntityFrom(source, iDamage);
         } else {
            boolean bResult = this.entityMobAttackEntityFrom(source, iDamage);
            if (this.R()) {
               int iTempCountx = 0;

               while (iTempCountx < 64 && !this.m()) {
                  iTempCountx++;
               }
            }

            this.angerNearbyEndermen((EntityPlayer)source.getEntity());
            return bResult;
         }
      } else {
         return false;
      }
   }

   @Override
   public void initCreature() {
      if (this.worldObj.provider.dimensionId == 1 && this.worldObj.rand.nextInt(5) == 0) {
         this.setCarried(Block.whiteStone.blockID);
         this.s(0);
      }
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.silkTouch.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   protected boolean isPlayerStaringAtMe(EntityPlayer player) {
      ItemStack headStack = player.inventory.armorInventory[3];
      if (headStack == null || headStack.itemID != BTWItems.enderSpectacles.itemID) {
         Vec3 vLook = player.i(1.0F).normalize();
         Vec3 vDelta = this.worldObj
            .getWorldVec3Pool()
            .getVecFromPool(this.posX - player.posX, this.posY + this.e() - (player.posY + player.getEyeHeight()), this.posZ - player.posZ);
         double dDist = vDelta.lengthVector();
         vDelta = vDelta.normalize();
         double dotDelta = vLook.dotProduct(vDelta);
         if (dotDelta > 1.0 - 0.025 / dDist) {
            return player.n(this);
         }
      }

      return false;
   }

   public void dropCarriedBlock() {
      int iCarriedBlockID = this.getCarried();
      if (iCarriedBlockID != 0) {
         Block block = Block.blocksList[iCarriedBlockID];
         if (block != null) {
            int iDamageDropped = block.damageDropped(this.p());
            this.a(new ItemStack(iCarriedBlockID, 1, iDamageDropped), 0.0F);
            this.setCarried(0);
            this.s(0);
         }
      }
   }

   private boolean canPickUpBlock(int i, int j, int k) {
      int iBlockID = this.worldObj.getBlockId(i, j, k);
      if (portableBlocks[iBlockID]) {
         if (!this.worldObj.isBlockNormalCube(i, j, k) && iBlockID != Block.cactus.blockID) {
            return true;
         }

         int iNeighboringNonSolidBlocks = 0;
         if (!this.doesBlockBlockPickingUp(i, j - 1, k)) {
            iNeighboringNonSolidBlocks++;
         } else {
            for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
               for (int iTempK = k - 1; iTempK <= k + 1; iTempK++) {
                  if (this.doesBlockBlockPickingUp(iTempI, j + 1, iTempK)) {
                     return false;
                  }
               }
            }
         }

         for (int iFacing = 1; iFacing < 6; iFacing++) {
            BlockPos targetPos = new BlockPos(i, j, k);
            targetPos.addFacingAsOffset(iFacing);
            if (!this.doesBlockBlockPickingUp(targetPos.x, targetPos.y, targetPos.z)) {
               iNeighboringNonSolidBlocks++;
            }
         }

         if (iNeighboringNonSolidBlocks >= 3) {
            return true;
         }
      }

      return false;
   }

   private boolean doesBlockBlockPickingUp(int i, int j, int k) {
      if (this.worldObj.isAirBlock(i, j, k)) {
         return false;
      } else if (this.worldObj.isBlockNormalCube(i, j, k)) {
         return true;
      } else {
         int iBlockID = this.worldObj.getBlockId(i, j, k);
         Block block = Block.blocksList[iBlockID];
         return block != null
            && block != Block.waterMoving
            && block != Block.waterStill
            && block != Block.lavaMoving
            && block != Block.lavaStill
            && block != Block.fire
            && block != BTWBlocks.stokedFire
            && !block.blockMaterial.isReplaceable()
            && block.blockMaterial != Material.plants
            && block.blockMaterial != Material.leaves;
      }
   }

   private boolean updateWithCarriedBlock() {
      int iCarriedBlockID = this.getCarried();
      if (this.worldObj.provider.dimensionId == 1) {
         int i = MathHelper.floor_double(this.posX) + this.rand.nextInt(5) - 2;
         int j = MathHelper.floor_double(this.posY) + this.rand.nextInt(7) - 3;
         int k = MathHelper.floor_double(this.posZ) + this.rand.nextInt(5) - 2;
         int iWeight = this.getPlaceEndstoneWeight(i, j, k);
         if (this.rand.nextInt(maxEnstonePlaceWeight >> 9) < iWeight) {
            this.worldObj.playAuxSFX(2246, i, j, k, iCarriedBlockID + (this.p() << 12));
            this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.getCarried(), this.p());
            this.setCarried(0);
         }
      } else if (this.rand.nextInt(2400) == 0) {
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY) + 1;
         int k = MathHelper.floor_double(this.posZ);
         this.worldObj.playAuxSFX(2247, i, j, k, 0);
         this.w();
         return false;
      }

      return true;
   }

   private boolean updateWithoutCarriedBlock() {
      if (this.rand.nextInt(20) == 0) {
         int i = MathHelper.floor_double(this.posX - 3.0 + this.rand.nextDouble() * 6.0);
         int j = MathHelper.floor_double(this.posY - 1.0 + this.rand.nextDouble() * 7.0);
         int k = MathHelper.floor_double(this.posZ - 3.0 + this.rand.nextDouble() * 6.0);
         int l1 = this.worldObj.getBlockId(i, j, k);
         if (this.canPickUpBlock(i, j, k)) {
            this.worldObj.playAuxSFX(2244, i, j, k, l1 + (this.worldObj.getBlockMetadata(i, j, k) << 12));
            this.setCarried(this.worldObj.getBlockId(i, j, k));
            this.s(this.worldObj.getBlockMetadata(i, j, k));
            this.worldObj.setBlockToAir(i, j, k);
         }
      } else if (this.worldObj.provider.dimensionId == 1 && this.rand.nextInt(9600) == 0) {
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY) + 1;
         int k = MathHelper.floor_double(this.posZ);
         this.worldObj.playAuxSFX(2247, i, j, k, 0);
         this.w();
         return false;
      }

      return true;
   }

   private int getPlaceEndstoneWeight(int i, int j, int k) {
      int iNumValidNeighbors = 0;
      if (this.worldObj.isAirBlock(i, j, k)) {
         if (this.worldObj.doesBlockHaveSolidTopSurface(i, j - 1, k)) {
            iNumValidNeighbors++;
         }

         for (int iFacing = 1; iFacing < 6; iFacing++) {
            BlockPos targetPos = new BlockPos(i, j, k);
            targetPos.addFacingAsOffset(iFacing);
            int iTargetBlockID = this.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            if (iTargetBlockID == Block.whiteStone.blockID) {
               iNumValidNeighbors++;
            }
         }
      }

      return endstonePlacementNeighborWeights[iNumValidNeighbors];
   }

   protected void angerNearbyEndermen(EntityPlayer targetPlayer) {
      for (Entity tempEntity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0, 32.0, 32.0))) {
         if (tempEntity instanceof EndermanEntity) {
            EndermanEntity enderman = (EndermanEntity)tempEntity;
            if (enderman.entityToAttack == null) {
               enderman.entityToAttack = targetPlayer;
               enderman.a(true);
            }
         }
      }
   }

   protected void emitParticles() {
      for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
         this.worldObj
            .spawnParticle(
               "portal",
               this.posX + (this.rand.nextDouble() - 0.5) * this.width,
               this.posY + this.rand.nextDouble() * this.height - 0.25,
               this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
               (this.rand.nextDouble() - 0.5) * 2.0,
               -this.rand.nextDouble(),
               (this.rand.nextDouble() - 0.5) * 2.0
            );
      }
   }

   protected void panic() {
      this.entityToAttack = null;
      this.a(false);
      this.shouldBeScreaming = false;
      this.m();
   }

   static {
      portableBlocks[Block.grass.blockID] = true;
      portableBlocks[Block.dirt.blockID] = true;
      portableBlocks[Block.sand.blockID] = true;
      portableBlocks[Block.gravel.blockID] = true;
      portableBlocks[Block.tnt.blockID] = true;
      portableBlocks[Block.cactus.blockID] = true;
      portableBlocks[Block.pumpkin.blockID] = true;
      portableBlocks[Block.melon.blockID] = true;
      portableBlocks[Block.mycelium.blockID] = true;
      portableBlocks[Block.wood.blockID] = true;
      portableBlocks[Block.netherrack.blockID] = true;

      for (int iTemp = 0; iTemp < 7; iTemp++) {
         endstonePlacementNeighborWeights[iTemp] = iTemp;

         for (int iPower = 1; iPower < 8; iPower++) {
            endstonePlacementNeighborWeights[iTemp] = endstonePlacementNeighborWeights[iTemp] * iTemp;
         }
      }

      maxEnstonePlaceWeight = endstonePlacementNeighborWeights[6];
   }
}
