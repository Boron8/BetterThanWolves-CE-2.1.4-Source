package btw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class AddonFinder {
   private static boolean debug = false;
   private static List<String> blacklist = Arrays.asList(
      "net.minecraft.*", "argo.*", "com.jcraft.*", "com.prupe.mcpatcher.*", "paulscode.*", "org.bouncycastle.*"
   );

   public static boolean isBlackListed(String name) {
      for (String pkg : blacklist) {
         pkg = pkg.replace("*", "");
         if (name.startsWith(pkg)) {
            return true;
         }
      }

      return false;
   }

   public static List<String> getClasses(Enumeration<URL> resources) throws IOException {
      assert resources != null;

      List<String> names = new ArrayList<>();

      while (resources.hasMoreElements()) {
         URL resource = resources.nextElement();
         String proto = resource.getProtocol();
         if (debug) {
            AddonHandler.logMessage("RESOURCE: " + resource.getPath() + "\nPROTO: " + proto);
         }

         if ("file".equals(proto)) {
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
               names.addAll(findFileClasses(file, ""));
            } else {
               names.addAll(findFileClasses(new File[]{file}, ""));
            }
         } else if ("jar".equals(proto)) {
            names.addAll(findJarClasses(resource));
         } else {
            AddonHandler.logWarning("Protocol " + proto + " not supported");
         }
      }

      return names;
   }

   private static List<String> findJarClasses(URL packageResource) {
      List<String> classes = new ArrayList<>();

      try {
         if (debug) {
            AddonHandler.logMessage("Jar URL Path is " + packageResource.getPath());
         }

         URL fileUrl = new URL(packageResource.getPath());
         String proto = fileUrl.getProtocol();
         if ("file".equals(proto)) {
            String filePath = fileUrl.getPath();

            try {
               filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException var13) {
               var13.printStackTrace();
            }

            int jarTagPos = filePath.indexOf(".jar!/");
            if (jarTagPos < 0) {
               jarTagPos = filePath.indexOf(".zip!/");
            }

            if (jarTagPos < 0) {
               AddonHandler.logWarning("Non-conformant jar file reference " + filePath + " !");
            } else {
               String packagePath = filePath.substring(jarTagPos + 6);
               String jarFilename = filePath.substring(0, jarTagPos + 4);
               if (debug) {
                  AddonHandler.logMessage("Package " + packagePath);
                  AddonHandler.logMessage("Jar file " + jarFilename);
               }

               String packagePrefix = packagePath.length() == 0 ? "" : packagePath + '/';

               try {
                  JarInputStream jarFile = new JarInputStream(new FileInputStream(jarFilename));

                  while (true) {
                     JarEntry jarEntry = jarFile.getNextJarEntry();
                     if (jarEntry == null) {
                        jarFile.close();
                        break;
                     }

                     String classPath = jarEntry.getName();
                     if (classPath.startsWith(packagePrefix) && classPath.endsWith(".class")) {
                        String className = classPath.substring(0, classPath.length() - 6).replace('/', '.');
                        if (className.indexOf(".") >= 0 && !isBlackListed(className)) {
                           if (debug) {
                              AddonHandler.logMessage("Found entry " + jarEntry.getName());
                           }

                           classes.add(className);
                        }
                     }
                  }
               } catch (Exception var14) {
                  var14.printStackTrace();
               }
            }
         } else {
            AddonHandler.logWarning("Nested protocol " + proto + " not supprted!");
         }
      } catch (MalformedURLException var15) {
         var15.printStackTrace();
      }

      return classes;
   }

   private static List<String> findFileClasses(File directory, String packageName) {
      if (!directory.exists()) {
         AddonHandler.logWarning("Directory " + directory.getAbsolutePath() + " does not exist.");
         return new ArrayList<>();
      } else {
         File[] files = directory.listFiles();
         if (debug) {
            AddonHandler.logMessage("Directory " + directory.getAbsolutePath() + " has " + files.length + " elements.");
         }

         return findFileClasses(files, packageName);
      }
   }

   private static List<String> findFileClasses(File[] files, String packageName) {
      List<String> classes = new ArrayList<>();
      if (packageName.length() > 0 && !packageName.endsWith(".")) {
         packageName = packageName + ".";
      }

      if (isBlackListed(packageName)) {
         return classes;
      } else {
         for (File file : files) {
            if (file.isDirectory()) {
               assert !file.getName().contains(".");

               classes.addAll(findFileClasses(file.listFiles(), packageName + file.getName()));
            } else if (file.getName().endsWith(".class") && packageName.length() != 0) {
               String className = packageName + file.getName().substring(0, file.getName().length() - 6);
               classes.add(className);
            }
         }

         return classes;
      }
   }
}
