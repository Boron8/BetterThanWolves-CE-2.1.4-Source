package btw.crafting.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraft.src.World;

public class SoulforgeCraftingManager {
   private static final SoulforgeCraftingManager instance = new SoulforgeCraftingManager();
   private List recipes = new ArrayList();

   public static final SoulforgeCraftingManager getInstance() {
      return instance;
   }

   private SoulforgeCraftingManager() {
   }

   public void addRecipe(ItemStack itemstack, Object[] aobj) {
      String s = "";
      int i = 0;
      int j = 0;
      int k = 0;
      if (aobj[i] instanceof String[]) {
         String[] as = (String[])aobj[i++];

         for (int l = 0; l < as.length; l++) {
            String s2 = as[l];
            k++;
            j = s2.length();
            s = s + s2;
         }
      } else {
         while (aobj[i] instanceof String) {
            String s1 = (String)aobj[i++];
            k++;
            j = s1.length();
            s = s + s1;
         }
      }

      HashMap hashmap;
      for (hashmap = new HashMap(); i < aobj.length; i += 2) {
         Character character = (Character)aobj[i];
         ItemStack itemstack1 = null;
         if (aobj[i + 1] instanceof Item) {
            itemstack1 = new ItemStack((Item)aobj[i + 1]);
         } else if (aobj[i + 1] instanceof Block) {
            itemstack1 = new ItemStack((Block)aobj[i + 1], 1, 32767);
         } else if (aobj[i + 1] instanceof ItemStack) {
            itemstack1 = (ItemStack)aobj[i + 1];
         }

         hashmap.put(character, itemstack1);
      }

      ItemStack[] aitemstack = new ItemStack[j * k];

      for (int i1 = 0; i1 < j * k; i1++) {
         char c = s.charAt(i1);
         if (hashmap.containsKey(c)) {
            aitemstack[i1] = ((ItemStack)hashmap.get(c)).copy();
         } else {
            aitemstack[i1] = null;
         }
      }

      this.recipes.add(new ShapedRecipes(j, k, aitemstack, itemstack));
   }

   public void addShapelessRecipe(ItemStack itemstack, Object[] aobj) {
      ArrayList arraylist = new ArrayList();

      for (Object obj : aobj) {
         if (obj instanceof ItemStack) {
            arraylist.add(((ItemStack)obj).copy());
         } else if (obj instanceof Item) {
            arraylist.add(new ItemStack((Item)obj));
         } else {
            if (!(obj instanceof Block)) {
               throw new RuntimeException("Invalid shapeless recipe!");
            }

            arraylist.add(new ItemStack((Block)obj));
         }
      }

      this.recipes.add(new ShapelessRecipes(itemstack, arraylist));
   }

   public ItemStack findMatchingRecipeStack(InventoryCrafting inventorycrafting, World world) {
      for (int i = 0; i < this.recipes.size(); i++) {
         IRecipe irecipe = (IRecipe)this.recipes.get(i);
         if (irecipe.matches(inventorycrafting, world)) {
            return irecipe.getCraftingResult(inventorycrafting);
         }
      }

      return null;
   }

   public IRecipe findMatchingRecipe(InventoryCrafting inventory, World world) {
      for (int iTempIndex = 0; iTempIndex < this.recipes.size(); iTempIndex++) {
         IRecipe tempRecipe = (IRecipe)this.recipes.get(iTempIndex);
         if (tempRecipe.matches(inventory, world)) {
            return tempRecipe;
         }
      }

      return null;
   }

   public List getRecipeList() {
      return this.recipes;
   }

   public boolean removeRecipe(ItemStack itemStack, Object[] recipeArray) {
      ShapedRecipes recipe = this.createRecipe(itemStack, recipeArray);
      int iMatchingIndex = this.getMatchingRecipeIndex(recipe);
      if (iMatchingIndex >= 0) {
         this.recipes.remove(iMatchingIndex);
         return true;
      } else {
         return false;
      }
   }

   public boolean removeShapelessRecipe(ItemStack itemStack, Object[] recipeArray) {
      ShapelessRecipes recipe = this.createShapelessRecipe(itemStack, recipeArray);
      int iMatchingIndex = this.getMatchingRecipeIndex(recipe);
      if (iMatchingIndex >= 0) {
         this.recipes.remove(iMatchingIndex);
         return true;
      } else {
         return false;
      }
   }

   private int getMatchingRecipeIndex(IRecipe recipe) {
      int iMatchingRecipeIndex = -1;

      for (int iIndex = 0; iIndex < this.recipes.size(); iIndex++) {
         IRecipe tempRecipe = (IRecipe)this.recipes.get(iIndex);
         if (tempRecipe.matches(recipe)) {
            return iIndex;
         }
      }

      return -1;
   }

   private ShapedRecipes createRecipe(ItemStack par1ItemStack, Object[] par2ArrayOfObj) {
      String s = "";
      int i = 0;
      int j = 0;
      int k = 0;
      if (par2ArrayOfObj[i] instanceof String[]) {
         String[] as = (String[])par2ArrayOfObj[i++];

         for (String s2 : as) {
            k++;
            j = s2.length();
            s = s + s2;
         }
      } else {
         while (par2ArrayOfObj[i] instanceof String) {
            String s1 = (String)par2ArrayOfObj[i++];
            k++;
            j = s1.length();
            s = s + s1;
         }
      }

      HashMap hashmap;
      for (hashmap = new HashMap(); i < par2ArrayOfObj.length; i += 2) {
         Character character = (Character)par2ArrayOfObj[i];
         ItemStack itemstack = null;
         if (par2ArrayOfObj[i + 1] instanceof Item) {
            itemstack = new ItemStack((Item)par2ArrayOfObj[i + 1]);
         } else if (par2ArrayOfObj[i + 1] instanceof Block) {
            itemstack = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
         } else if (par2ArrayOfObj[i + 1] instanceof ItemStack) {
            itemstack = (ItemStack)par2ArrayOfObj[i + 1];
         }

         hashmap.put(character, itemstack);
      }

      ItemStack[] aitemstack = new ItemStack[j * k];

      for (int i1 = 0; i1 < j * k; i1++) {
         char c = s.charAt(i1);
         if (hashmap.containsKey(c)) {
            aitemstack[i1] = ((ItemStack)hashmap.get(c)).copy();
         } else {
            aitemstack[i1] = null;
         }
      }

      return new ShapedRecipes(j, k, aitemstack, par1ItemStack);
   }

   private ShapelessRecipes createShapelessRecipe(ItemStack par1ItemStack, Object[] par2ArrayOfObj) {
      ArrayList arraylist = new ArrayList();

      for (Object obj : par2ArrayOfObj) {
         if (obj instanceof ItemStack) {
            arraylist.add(((ItemStack)obj).copy());
         } else if (obj instanceof Item) {
            arraylist.add(new ItemStack((Item)obj));
         } else {
            if (!(obj instanceof Block)) {
               throw new RuntimeException("Invalid shapeless recipe!");
            }

            arraylist.add(new ItemStack((Block)obj));
         }
      }

      return new ShapelessRecipes(par1ItemStack, arraylist);
   }
}
