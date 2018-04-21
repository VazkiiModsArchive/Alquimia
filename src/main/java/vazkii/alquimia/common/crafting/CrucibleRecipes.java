package vazkii.alquimia.common.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.lib.LibMisc;

public class CrucibleRecipes {

	public static final HashMap<ResourceLocation, CrucibleRecipe> RECIPE_MAPPING = new HashMap();
	public static final List<CrucibleRecipe> CRUCIBLE_RECIPES = new ArrayList();

	public static void init() {
		
	}

	private static void registerRecipe(String name, Ingredient input, ItemStack output, int time) {
		ResourceLocation res = new ResourceLocation(LibMisc.MOD_ID, name);
		registerRecipe(res, input, output, time);
	}
	
	private static void registerRecipe(ResourceLocation res, Ingredient input, ItemStack output, int time) {
		CrucibleRecipe recipe = new CrucibleRecipe(input, output, time);
		RECIPE_MAPPING.put(res, recipe);
		CRUCIBLE_RECIPES.add(recipe);
	}


}
