package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiScreenBook extends GuiScreen {
   private final EntityPlayer editingPlayer;
   private final ItemStack itemstackBook;
   private final boolean bookIsUnsigned;
   private boolean bookModified;
   private boolean editingTitle;
   private int updateCount;
   private int bookImageWidth = 192;
   private int bookImageHeight = 192;
   private int bookTotalPages = 1;
   private int currPage;
   private NBTTagList bookPages;
   private String bookTitle = "";
   private GuiButtonNextPage buttonNextPage;
   private GuiButtonNextPage buttonPreviousPage;
   private GuiButton buttonDone;
   private GuiButton buttonSign;
   private GuiButton buttonFinalize;
   private GuiButton buttonCancel;

   public GuiScreenBook(EntityPlayer var1, ItemStack var2, boolean var3) {
      this.editingPlayer = var1;
      this.itemstackBook = var2;
      this.bookIsUnsigned = var3;
      if (var2.hasTagCompound()) {
         NBTTagCompound var4 = var2.getTagCompound();
         this.bookPages = var4.getTagList("pages");
         if (this.bookPages != null) {
            this.bookPages = (NBTTagList)this.bookPages.copy();
            this.bookTotalPages = this.bookPages.tagCount();
            if (this.bookTotalPages < 1) {
               this.bookTotalPages = 1;
            }
         }
      }

      if (this.bookPages == null && var3) {
         this.bookPages = new NBTTagList("pages");
         this.bookPages.appendTag(new NBTTagString("1", ""));
         this.bookTotalPages = 1;
      }
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.updateCount++;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      if (this.bookIsUnsigned) {
         this.buttonList
            .add(this.buttonSign = new GuiButton(3, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, StatCollector.translateToLocal("book.signButton")));
         this.buttonList
            .add(this.buttonDone = new GuiButton(0, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, StatCollector.translateToLocal("gui.done")));
         this.buttonList
            .add(
               this.buttonFinalize = new GuiButton(
                  5, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, StatCollector.translateToLocal("book.finalizeButton")
               )
            );
         this.buttonList
            .add(this.buttonCancel = new GuiButton(4, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, StatCollector.translateToLocal("gui.cancel")));
      } else {
         this.buttonList
            .add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, StatCollector.translateToLocal("gui.done")));
      }

      int var1 = (this.width - this.bookImageWidth) / 2;
      byte var2 = 2;
      this.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, var1 + 120, var2 + 154, true));
      this.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, var1 + 38, var2 + 154, false));
      this.updateButtons();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   private void updateButtons() {
      this.buttonNextPage.drawButton = !this.editingTitle && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
      this.buttonPreviousPage.drawButton = !this.editingTitle && this.currPage > 0;
      this.buttonDone.drawButton = !this.bookIsUnsigned || !this.editingTitle;
      if (this.bookIsUnsigned) {
         this.buttonSign.drawButton = !this.editingTitle;
         this.buttonCancel.drawButton = this.editingTitle;
         this.buttonFinalize.drawButton = this.editingTitle;
         this.buttonFinalize.enabled = this.bookTitle.trim().length() > 0;
      }
   }

   private void sendBookToServer(boolean var1) {
      if (this.bookIsUnsigned && this.bookModified) {
         if (this.bookPages != null) {
            while (this.bookPages.tagCount() > 1) {
               NBTTagString var2 = (NBTTagString)this.bookPages.tagAt(this.bookPages.tagCount() - 1);
               if (var2.data != null && var2.data.length() != 0) {
                  break;
               }

               this.bookPages.removeTag(this.bookPages.tagCount() - 1);
            }

            if (this.itemstackBook.hasTagCompound()) {
               NBTTagCompound var7 = this.itemstackBook.getTagCompound();
               var7.setTag("pages", this.bookPages);
            } else {
               this.itemstackBook.setTagInfo("pages", this.bookPages);
            }

            String var8 = "MC|BEdit";
            if (var1) {
               var8 = "MC|BSign";
               this.itemstackBook.setTagInfo("author", new NBTTagString("author", this.editingPlayer.username));
               this.itemstackBook.setTagInfo("title", new NBTTagString("title", this.bookTitle.trim()));
               this.itemstackBook.itemID = Item.writtenBook.itemID;
            }

            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            DataOutputStream var4 = new DataOutputStream(var3);

            try {
               Packet.writeItemStack(this.itemstackBook, var4);
               this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(var8, var3.toByteArray()));
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 0) {
            this.mc.displayGuiScreen(null);
            this.sendBookToServer(false);
         } else if (var1.id == 3 && this.bookIsUnsigned) {
            this.editingTitle = true;
         } else if (var1.id == 1) {
            if (this.currPage < this.bookTotalPages - 1) {
               this.currPage++;
            } else if (this.bookIsUnsigned) {
               this.addNewPage();
               if (this.currPage < this.bookTotalPages - 1) {
                  this.currPage++;
               }
            }
         } else if (var1.id == 2) {
            if (this.currPage > 0) {
               this.currPage--;
            }
         } else if (var1.id == 5 && this.editingTitle) {
            this.sendBookToServer(true);
            this.mc.displayGuiScreen(null);
         } else if (var1.id == 4 && this.editingTitle) {
            this.editingTitle = false;
         }

         this.updateButtons();
      }
   }

   private void addNewPage() {
      if (this.bookPages != null && this.bookPages.tagCount() < 50) {
         this.bookPages.appendTag(new NBTTagString("" + (this.bookTotalPages + 1), ""));
         this.bookTotalPages++;
         this.bookModified = true;
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      super.keyTyped(var1, var2);
      if (this.bookIsUnsigned) {
         if (this.editingTitle) {
            this.func_74162_c(var1, var2);
         } else {
            this.keyTypedInBook(var1, var2);
         }
      }
   }

   private void keyTypedInBook(char var1, int var2) {
      switch (var1) {
         case '\u0016':
            this.func_74160_b(GuiScreen.getClipboardString());
            return;
         default:
            switch (var2) {
               case 14:
                  String var3 = this.func_74158_i();
                  if (var3.length() > 0) {
                     this.func_74159_a(var3.substring(0, var3.length() - 1));
                  }

                  return;
               case 28:
                  this.func_74160_b("\n");
                  return;
               default:
                  if (ChatAllowedCharacters.isAllowedCharacter(var1)) {
                     this.func_74160_b(Character.toString(var1));
                  }
            }
      }
   }

   private void func_74162_c(char var1, int var2) {
      switch (var2) {
         case 14:
            if (this.bookTitle.length() > 0) {
               this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
               this.updateButtons();
            }

            return;
         case 28:
            if (this.bookTitle.length() > 0) {
               this.sendBookToServer(true);
               this.mc.displayGuiScreen(null);
            }

            return;
         default:
            if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(var1)) {
               this.bookTitle = this.bookTitle + Character.toString(var1);
               this.updateButtons();
               this.bookModified = true;
            }
      }
   }

   private String func_74158_i() {
      if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
         NBTTagString var1 = (NBTTagString)this.bookPages.tagAt(this.currPage);
         return var1.toString();
      } else {
         return "";
      }
   }

   private void func_74159_a(String var1) {
      if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
         NBTTagString var2 = (NBTTagString)this.bookPages.tagAt(this.currPage);
         var2.data = var1;
         this.bookModified = true;
      }
   }

   private void func_74160_b(String var1) {
      String var2 = this.func_74158_i();
      String var3 = var2 + var1;
      int var4 = this.fontRenderer.splitStringWidth(var3 + "" + EnumChatFormatting.BLACK + "_", 118);
      if (var4 <= 118 && var3.length() < 256) {
         this.func_74159_a(var3);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/book.png");
      int var4 = (this.width - this.bookImageWidth) / 2;
      byte var5 = 2;
      this.b(var4, var5, 0, 0, this.bookImageWidth, this.bookImageHeight);
      if (this.editingTitle) {
         String var6 = this.bookTitle;
         if (this.bookIsUnsigned) {
            if (this.updateCount / 6 % 2 == 0) {
               var6 = var6 + "" + EnumChatFormatting.BLACK + "_";
            } else {
               var6 = var6 + "" + EnumChatFormatting.GRAY + "_";
            }
         }

         String var7 = StatCollector.translateToLocal("book.editTitle");
         int var8 = this.fontRenderer.getStringWidth(var7);
         this.fontRenderer.drawString(var7, var4 + 36 + (116 - var8) / 2, var5 + 16 + 16, 0);
         int var9 = this.fontRenderer.getStringWidth(var6);
         this.fontRenderer.drawString(var6, var4 + 36 + (116 - var9) / 2, var5 + 48, 0);
         Object var10 = String.format(StatCollector.translateToLocal("book.byAuthor"), this.editingPlayer.username);
         int var11 = this.fontRenderer.getStringWidth((String)var10);
         this.fontRenderer.drawString(EnumChatFormatting.DARK_GRAY + var10, var4 + 36 + (116 - var11) / 2, var5 + 48 + 10, 0);
         String var12 = StatCollector.translateToLocal("book.finalizeWarning");
         this.fontRenderer.drawSplitString(var12, var4 + 36, var5 + 80, 116, 0);
      } else {
         String var13 = String.format(StatCollector.translateToLocal("book.pageIndicator"), this.currPage + 1, this.bookTotalPages);
         String var14 = "";
         if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            NBTTagString var15 = (NBTTagString)this.bookPages.tagAt(this.currPage);
            var14 = var15.toString();
         }

         if (this.bookIsUnsigned) {
            if (this.fontRenderer.getBidiFlag()) {
               var14 = var14 + "_";
            } else if (this.updateCount / 6 % 2 == 0) {
               var14 = var14 + "" + EnumChatFormatting.BLACK + "_";
            } else {
               var14 = var14 + "" + EnumChatFormatting.GRAY + "_";
            }
         }

         int var16 = this.fontRenderer.getStringWidth(var13);
         this.fontRenderer.drawString(var13, var4 - var16 + this.bookImageWidth - 44, var5 + 16, 0);
         this.fontRenderer.drawSplitString(var14, var4 + 36, var5 + 16 + 16, 116, 0);
      }

      super.drawScreen(var1, var2, var3);
   }
}
