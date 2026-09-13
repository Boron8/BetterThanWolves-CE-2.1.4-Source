package net.minecraft.src;

public class MerchantRecipe {
   private ItemStack itemToBuy;
   private ItemStack secondItemToBuy;
   private ItemStack itemToSell;
   private int toolUses;
   private int maxTradeUses;
   private boolean isMandatory;
   public int tradeLevel;

   public MerchantRecipe(NBTTagCompound par1NBTTagCompound) {
      this.readFromTags(par1NBTTagCompound);
   }

   public MerchantRecipe(ItemStack par1ItemStack, ItemStack par2ItemStack, ItemStack par3ItemStack) {
      this.itemToBuy = par1ItemStack;
      this.secondItemToBuy = par2ItemStack;
      this.itemToSell = par3ItemStack;
      this.maxTradeUses = 1;
      this.tradeLevel = 1;
   }

   public MerchantRecipe(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      this(par1ItemStack, (ItemStack)null, par2ItemStack);
   }

   public MerchantRecipe(ItemStack par1ItemStack, Item par2Item) {
      this(par1ItemStack, new ItemStack(par2Item));
   }

   public ItemStack getItemToBuy() {
      return this.itemToBuy;
   }

   public ItemStack getSecondItemToBuy() {
      return this.secondItemToBuy;
   }

   public boolean hasSecondItemToBuy() {
      return this.secondItemToBuy != null;
   }

   public ItemStack getItemToSell() {
      return this.itemToSell;
   }

   public boolean hasSameIDsAs(MerchantRecipe par1MerchantRecipe) {
      return this.itemToBuy.itemID == par1MerchantRecipe.itemToBuy.itemID && this.itemToSell.itemID == par1MerchantRecipe.itemToSell.itemID
         ? this.secondItemToBuy == null && par1MerchantRecipe.secondItemToBuy == null
            || this.secondItemToBuy != null
               && par1MerchantRecipe.secondItemToBuy != null
               && this.secondItemToBuy.itemID == par1MerchantRecipe.secondItemToBuy.itemID
         : false;
   }

   public boolean hasSameItemsAs(MerchantRecipe par1MerchantRecipe) {
      return this.hasSameIDsAs(par1MerchantRecipe)
         && (
            this.itemToBuy.stackSize < par1MerchantRecipe.itemToBuy.stackSize
               || this.secondItemToBuy != null && this.secondItemToBuy.stackSize < par1MerchantRecipe.secondItemToBuy.stackSize
         );
   }

   public void incrementToolUses() {
      this.toolUses++;
   }

   public void func_82783_a(int par1) {
      this.maxTradeUses += par1;
   }

   public boolean func_82784_g() {
      return this.toolUses >= this.maxTradeUses;
   }

   public void func_82785_h() {
      this.toolUses = this.maxTradeUses;
   }

   public void readFromTags(NBTTagCompound par1NBTTagCompound) {
      NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("buy");
      this.itemToBuy = ItemStack.loadItemStackFromNBT(var2);
      NBTTagCompound var3 = par1NBTTagCompound.getCompoundTag("sell");
      this.itemToSell = ItemStack.loadItemStackFromNBT(var3);
      if (par1NBTTagCompound.hasKey("buyB")) {
         this.secondItemToBuy = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("buyB"));
      }

      if (par1NBTTagCompound.hasKey("uses")) {
         this.toolUses = par1NBTTagCompound.getInteger("uses");
      }

      if (par1NBTTagCompound.hasKey("maxUses")) {
         this.maxTradeUses = par1NBTTagCompound.getInteger("maxUses");
      } else {
         this.maxTradeUses = 1;
      }

      if (par1NBTTagCompound.hasKey("fcTradeLevel")) {
         this.tradeLevel = par1NBTTagCompound.getInteger("fcTradeLevel");
      } else {
         this.tradeLevel = 1;
      }

      if (par1NBTTagCompound.hasKey("mandatory")) {
         this.isMandatory = par1NBTTagCompound.getBoolean("mandatory");
      } else {
         this.isMandatory = false;
      }
   }

   public NBTTagCompound writeToTags() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.setCompoundTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound("buy")));
      var1.setCompoundTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound("sell")));
      if (this.secondItemToBuy != null) {
         var1.setCompoundTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound("buyB")));
      }

      var1.setInteger("uses", this.toolUses);
      var1.setInteger("maxUses", this.maxTradeUses);
      var1.setInteger("fcTradeLevel", this.tradeLevel);
      var1.setBoolean("mandatory", this.isMandatory);
      return var1;
   }

   public MerchantRecipe(ItemStack inputStack1, ItemStack inputStack2, ItemStack outputStack, int iTradeLevel) {
      this(inputStack1, inputStack2, outputStack);
      this.tradeLevel = iTradeLevel;
   }

   public MerchantRecipe(ItemStack inputStack, ItemStack outputStack, int iTradeLevel) {
      this(inputStack, (ItemStack)null, outputStack, iTradeLevel);
   }

   public MerchantRecipe(ItemStack inputStack, Item outputItem, int iTradeLevel) {
      this(inputStack, new ItemStack(outputItem), iTradeLevel);
   }

   public boolean isMandatory() {
      return this.isMandatory;
   }

   public void setMandatory() {
      this.isMandatory = true;
   }
}
