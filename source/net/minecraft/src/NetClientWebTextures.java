package net.minecraft.src;

import net.minecraft.client.Minecraft;

class NetClientWebTextures extends GuiScreen {
   NetClientWebTextures(NetClientHandler var1, String var2) {
      this.netClientHandlerWebTextures = var1;
      this.texturePackName = var2;
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      this.mc = Minecraft.getMinecraft();
      if (this.mc.getServerData() != null) {
         this.mc.getServerData().setAcceptsTextures(var1);
         ServerList.func_78852_b(this.mc.getServerData());
      }

      if (var1) {
         this.mc.texturePackList.requestDownloadOfTexture(this.texturePackName);
      }

      this.mc.displayGuiScreen(null);
   }
}
