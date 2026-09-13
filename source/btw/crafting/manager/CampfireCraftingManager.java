package btw.crafting.manager;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.ItemStack;

public class CampfireCraftingManager {
   public static CampfireCraftingManager instance = new CampfireCraftingManager();
   private Map recipeMap = new HashMap();

   private CampfireCraftingManager() {
   }

   public ItemStack getRecipeResult(int iInputItemID) {
      return (ItemStack)this.recipeMap.get(iInputItemID);
   }

   public void addRecipe(int iInputItemID, ItemStack outputStack) {
      this.recipeMap.put(iInputItemID, outputStack);
   }
}
