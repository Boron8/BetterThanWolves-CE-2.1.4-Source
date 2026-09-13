package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Packet15Place extends Packet {
   private int xPosition;
   private int yPosition;
   private int zPosition;
   private int direction;
   private ItemStack itemStack;
   private float xOffset;
   private float yOffset;
   private float zOffset;

   public Packet15Place() {
   }

   @Environment(EnvType.CLIENT)
   public Packet15Place(int x, int y, int z, int par4, ItemStack par5ItemStack, float par6, float par7, float par8) {
      this.xPosition = x;
      this.yPosition = y;
      this.zPosition = z;
      this.direction = par4;
      this.itemStack = par5ItemStack != null ? par5ItemStack.copy() : null;
      this.xOffset = par6;
      this.yOffset = par7;
      this.zOffset = par8;
   }

   @Override
   public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
      this.xPosition = par1DataInputStream.readInt();
      this.yPosition = par1DataInputStream.read();
      this.zPosition = par1DataInputStream.readInt();
      this.direction = par1DataInputStream.read();
      this.itemStack = c(par1DataInputStream);
      this.xOffset = par1DataInputStream.readShort() / 32000.0F;
      this.yOffset = par1DataInputStream.readShort() / 32000.0F;
      this.zOffset = par1DataInputStream.readShort() / 32000.0F;
   }

   @Override
   public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
      par1DataOutputStream.writeInt(this.xPosition);
      par1DataOutputStream.write(this.yPosition);
      par1DataOutputStream.writeInt(this.zPosition);
      par1DataOutputStream.write(this.direction);
      a(this.itemStack, par1DataOutputStream);
      par1DataOutputStream.writeShort((int)(this.xOffset * 32000.0F + 0.5F));
      par1DataOutputStream.writeShort((int)(this.yOffset * 32000.0F + 0.5F));
      par1DataOutputStream.writeShort((int)(this.zOffset * 32000.0F + 0.5F));
   }

   @Override
   public void processPacket(NetHandler par1NetHandler) {
      par1NetHandler.handlePlace(this);
   }

   @Override
   public int getPacketSize() {
      return 22;
   }

   public int getXPosition() {
      return this.xPosition;
   }

   public int getYPosition() {
      return this.yPosition;
   }

   public int getZPosition() {
      return this.zPosition;
   }

   public int getDirection() {
      return this.direction;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public float getXOffset() {
      return this.xOffset;
   }

   public float getYOffset() {
      return this.yOffset;
   }

   public float getZOffset() {
      return this.zOffset;
   }
}
