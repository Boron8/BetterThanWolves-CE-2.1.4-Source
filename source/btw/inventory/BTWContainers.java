package btw.inventory;

import btw.block.tileentity.CauldronTileEntity;
import btw.block.tileentity.CrucibleTileEntity;
import btw.block.tileentity.HopperTileEntity;
import btw.block.tileentity.LoomTileEntity;
import btw.block.tileentity.OvenTileEntity;
import btw.block.tileentity.PulleyTileEntity;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.client.gui.BlockDispenserGui;
import btw.client.gui.CookingVesselGui;
import btw.client.gui.CraftingGuiAnvil;
import btw.client.gui.CraftingGuiSoulforge;
import btw.client.gui.HamperGui;
import btw.client.gui.HopperGui;
import btw.client.gui.InfernalEnchanterGui;
import btw.client.gui.LoomGui;
import btw.client.gui.PulleyGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.InventoryBasic;

public class BTWContainers {
   public static int cauldronContainerID = 223;
   public static int hopperContainerID = 224;
   public static int crucibleContainerID = 225;
   public static int soulforgeContainerID = 226;
   public static int blockDispenserContainerID = 227;
   public static int pulleyContainerID = 228;
   public static int infernalEnchanterContainerID = 229;
   public static int furnaceBrickContainerID = 230;
   public static int hamperContainerID = 231;
   public static int anvilContainerID = 232;
   public static int loomContainerID = 233;

   @Environment(EnvType.CLIENT)
   public static GuiContainer getAssociatedGui(EntityClientPlayerMP entityclientplayermp, int containerID) {
      if (containerID == cauldronContainerID) {
         CauldronTileEntity cauldronEntity = new CauldronTileEntity();
         return new CookingVesselGui(entityclientplayermp.inventory, cauldronEntity, containerID);
      } else if (containerID == hopperContainerID) {
         HopperTileEntity hopperEntity = new HopperTileEntity();
         return new HopperGui(entityclientplayermp.inventory, hopperEntity);
      } else if (containerID == crucibleContainerID) {
         CrucibleTileEntity crucibleEntity = new CrucibleTileEntity();
         return new CookingVesselGui(entityclientplayermp.inventory, crucibleEntity, containerID);
      } else if (containerID == soulforgeContainerID) {
         return new CraftingGuiSoulforge(entityclientplayermp.inventory, entityclientplayermp.worldObj, 0, 0, 0);
      } else if (containerID == blockDispenserContainerID) {
         BlockDispenserTileEntity dispenserEntity = new BlockDispenserTileEntity();
         return new BlockDispenserGui(entityclientplayermp.inventory, dispenserEntity);
      } else if (containerID == pulleyContainerID) {
         PulleyTileEntity pulleyEntity = new PulleyTileEntity();
         return new PulleyGui(entityclientplayermp.inventory, pulleyEntity);
      } else if (containerID == infernalEnchanterContainerID) {
         return new InfernalEnchanterGui(entityclientplayermp.inventory, entityclientplayermp.worldObj, 0, 0, 0);
      } else if (containerID == furnaceBrickContainerID) {
         OvenTileEntity brickFurnaceEntity = new OvenTileEntity();
         return new GuiFurnace(entityclientplayermp.inventory, brickFurnaceEntity);
      } else if (containerID == hamperContainerID) {
         InventoryBasic hamperInventory = new InventoryBasic("container.fcHamper", false, 4);
         return new HamperGui(entityclientplayermp.inventory, hamperInventory);
      } else if (containerID == anvilContainerID) {
         return new CraftingGuiAnvil(entityclientplayermp.inventory, entityclientplayermp.worldObj, 0, 0, 0);
      } else if (containerID == loomContainerID) {
         LoomTileEntity loomEntity = new LoomTileEntity();
         return new LoomGui(entityclientplayermp.inventory, loomEntity);
      } else {
         return null;
      }
   }
}
