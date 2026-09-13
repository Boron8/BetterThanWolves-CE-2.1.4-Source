package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

public class GuiTexturePacks extends GuiScreen {
   protected GuiScreen guiScreen;
   private int refreshTimer = -1;
   private String fileLocation = "";
   private GuiTexturePackSlot guiTexturePackSlot;
   private GameSettings field_96146_n;

   public GuiTexturePacks(GuiScreen var1, GameSettings var2) {
      this.guiScreen = var1;
      this.field_96146_n = var2;
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.add(new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, var1.translateKey("texturePack.openFolder")));
      this.buttonList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, var1.translateKey("gui.done")));
      this.mc.texturePackList.updateAvaliableTexturePacks();
      this.fileLocation = new File(Minecraft.getMinecraftDir(), "texturepacks").getAbsolutePath();
      this.guiTexturePackSlot = new GuiTexturePackSlot(this);
      this.guiTexturePackSlot.a(this.buttonList, 7, 8);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 5) {
            if (Minecraft.getOs() == EnumOS.MACOS) {
               try {
                  this.mc.getLogAgent().logInfo(this.fileLocation);
                  Runtime.getRuntime().exec(new String[]{"/usr/bin/open", this.fileLocation});
                  return;
               } catch (IOException var7) {
                  var7.printStackTrace();
               }
            } else if (Minecraft.getOs() == EnumOS.WINDOWS) {
               String var2 = String.format("cmd.exe /C start \"Open file\" \"%s\"", this.fileLocation);

               try {
                  Runtime.getRuntime().exec(var2);
                  return;
               } catch (IOException var6) {
                  var6.printStackTrace();
               }
            }

            boolean var8 = false;

            try {
               Class var3 = Class.forName("java.awt.Desktop");
               Object var4 = var3.getMethod("getDesktop").invoke(null);
               var3.getMethod("browse", URI.class).invoke(var4, new File(Minecraft.getMinecraftDir(), "texturepacks").toURI());
            } catch (Throwable var5) {
               var5.printStackTrace();
               var8 = true;
            }

            if (var8) {
               this.mc.getLogAgent().logInfo("Opening via system class!");
               Sys.openURL("file://" + this.fileLocation);
            }
         } else if (var1.id == 6) {
            this.mc.displayGuiScreen(this.guiScreen);
         } else {
            this.guiTexturePackSlot.a(var1);
         }
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
   }

   @Override
   protected void mouseMovedOrUp(int var1, int var2, int var3) {
      super.mouseMovedOrUp(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.guiTexturePackSlot.a(var1, var2, var3);
      if (this.refreshTimer <= 0) {
         this.mc.texturePackList.updateAvaliableTexturePacks();
         this.refreshTimer += 20;
      }

      StringTranslate var4 = StringTranslate.getInstance();
      this.a(this.fontRenderer, var4.translateKey("texturePack.title"), this.width / 2, 16, 16777215);
      this.a(this.fontRenderer, var4.translateKey("texturePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
      super.drawScreen(var1, var2, var3);
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.refreshTimer--;
   }
}
