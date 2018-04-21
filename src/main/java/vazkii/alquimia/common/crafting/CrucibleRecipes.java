package vazkii.alquimia.common.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;

public class CrucibleRecipes {

	public static final HashMap<ResourceLocation, CrucibleRecipe> RECIPE_MAPPING = new HashMap();
	public static final List<CrucibleRecipe> CRUCIBLE_RECIPES = new ArrayList();

	public static void init() {
		registerRecipe(RecipeHandler.compound("cobblestone", "gravel", "sand"), ProxyRegistry.newStack(ModBlocks.ash), 20);
		registerRecipe(ModItems.cinnabar, ProxyRegistry.newStack(ModItems.alchemical_ash), 60);
	}

	private static boolean registerRecipe(Object input, ItemStack output, int time) {
		ResourceLocation res = output.getItem().getRegistryName();
		String pathBase = res.getResourcePath() + "_";
		int i = 1;
		while(!registerRecipe(res, input, output, time))
			res = new ResourceLocation(res.getResourceDomain(), pathBase + (i++));
		
		return true;
	}
	
	private static boolean registerRecipe(String name, Object input, ItemStack output, int time) {
		ResourceLocation res = new ResourceLocation(LibMisc.MOD_ID, name);
		return registerRecipe(res, input, output, time);
	}
	
	private static boolean registerRecipe(ResourceLocation res, Object input, ItemStack output, int time) {
		if(RECIPE_MAPPING.containsKey(res))
			return false;
	
		Ingredient ingredient = RecipeHandler.asIngredient(input);
		CrucibleRecipe recipe = new CrucibleRecipe(ingredient, output, time);
		RECIPE_MAPPING.put(res, recipe);
		CRUCIBLE_RECIPES.add(recipe);
		return true;
	}


}
