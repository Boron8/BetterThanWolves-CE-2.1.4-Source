package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCPatcherUtils;
import java.util.Comparator;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.ITexturePack;

@Environment(EnvType.CLIENT)
public class ResourceLocationWithSource extends FakeResourceLocation {
   private final ITexturePack source;
   private final int order;
   private final boolean isDirectory;

   public ResourceLocationWithSource(ITexturePack source, FakeResourceLocation resource) {
      super(resource.getNamespace(), resource.getPath().replaceFirst("/$", ""));
      this.source = source;
      this.order = ResourceList.getResourcePackOrder(source);
      this.isDirectory = resource.getPath().endsWith("/");
   }

   public ITexturePack getSource() {
      return this.source;
   }

   public int getOrder() {
      return this.order;
   }

   public boolean isDirectory() {
      return this.isDirectory;
   }

   @Environment(EnvType.CLIENT)
   static class Comparator1 implements Comparator<ResourceLocationWithSource> {
      private final boolean bySource;
      private final String suffixExpr;

      Comparator1() {
         this(false, null);
      }

      Comparator1(boolean bySource, String suffix) {
         this.bySource = bySource;
         this.suffixExpr = MCPatcherUtils.isNullOrEmpty(suffix) ? null : Pattern.quote(suffix) + "$";
      }

      public int compare(ResourceLocationWithSource o1, ResourceLocationWithSource o2) {
         if (this.bySource) {
            int result = o1.getOrder() - o2.getOrder();
            if (result != 0) {
               return result;
            }
         }

         String n1 = o1.getNamespace();
         String n2 = o2.getNamespace();
         int result = n1.compareTo(n2);
         if (result != 0) {
            return result;
         } else {
            String p1 = o1.getPath();
            String p2 = o2.getPath();
            if (this.suffixExpr != null) {
               String f1 = p1.replaceAll(".*/", "").replaceFirst(this.suffixExpr, "");
               String f2 = p2.replaceAll(".*/", "").replaceFirst(this.suffixExpr, "");
               result = f1.compareTo(f2);
               if (result != 0) {
                  return result;
               }
            }

            return p1.compareTo(p2);
         }
      }
   }
}
