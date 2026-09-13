package net.minecraft.src;

import btw.block.blocks.MyceliumBlock;
import btw.entity.mob.CowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityMooshroom extends CowEntity {
   public EntityMooshroom(World par1World) {
      super(par1World);
      this.texture = "/mob/redcow.png";
      this.a(0.9F, 1.3F);
   }

   public EntityMooshroom func_94900_c(EntityAgeable par1EntityAgeable) {
      return (EntityMooshroom)EntityList.createEntityOfType(EntityMooshroom.class, this.worldObj);
   }

   @Override
   public CowEntity spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      return this.func_94900_c(par1EntityAgeable);
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.func_94900_c(par1EntityAgeable);
   }

   @Override
   public void checkForGrazeSideEffects(int i, int j, int k) {
   }

   @Override
   public void convertToMooshroom() {
   }

   @Override
   public void onLivingUpdate() {
      if (!this.worldObj.isRemote) {
         this.checkForMyceliumSpread();
      }

      super.onLivingUpdate();
   }

   @Override
   public boolean interact(EntityPlayer player) {
      ItemStack heldStack = player.inventory.getCurrentItem();
      if (heldStack != null && heldStack.itemID == Item.bowlEmpty.itemID && this.gotMilk()) {
         this.a(DamageSource.generic, 0);
         if (!this.worldObj.isRemote) {
            this.setGotMilk(false);
            this.worldObj.playAuxSFX(2254, MathHelper.floor_double(this.posX), (int)this.posY, MathHelper.floor_double(this.posZ), 0);
         }

         if (heldStack.stackSize == 1) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Item.bowlSoup));
         } else if (player.inventory.addItemStackToInventory(new ItemStack(Item.bowlSoup))) {
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
         }

         return true;
      } else {
         return this.entityAnimalInteract(player);
      }
   }

   private void checkForMyceliumSpread() {
      if (this.worldObj.provider.dimensionId != 1 && this.rand.nextInt(1000) == 0) {
         MyceliumBlock.checkForMyceliumSpreadFromLocation(
            this.worldObj, MathHelper.floor_double(this.posX), (int)this.posY - 1, MathHelper.floor_double(this.posZ)
         );
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      if (this.getWearingBreedingHarness()) {
         return "/btwmodtex/fc_mr_redcow.png";
      } else {
         int iHungerLevel = this.getHungerLevel();
         if (iHungerLevel == 1) {
            return "/btwmodtex/fcMooshroomFamished.png";
         } else {
            return iHungerLevel == 2 ? "/btwmodtex/fcMooshroomStarving.png" : this.texture;
         }
      }
   }
}
