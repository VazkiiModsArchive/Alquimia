package vazkii.alquimia.common.ritual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.arl.recipe.RecipeHandler;

public class Ritual {

	public final List<Ingredient> ingredients;
	public final RitualType type;
	
	public Ritual(Collection<Object> ingredients, RitualType type) {
		this.ingredients = ingredients.stream().map(RecipeHandler::asIngredient).collect(Collectors.toCollection(ArrayList::new));
		this.type = type;
	}
	
	public boolean matches(List<ItemStack> stacks) {
		List<ItemStack> copyStacks = new ArrayList(stacks);
		List<Ingredient> copyIngredients = new ArrayList(ingredients);
		
		if(copyStacks.size() != copyIngredients.size())
			return false;
		
		while(!copyStacks.isEmpty()) fulfil: {
			ItemStack stack = copyStacks.get(0);
			for(Ingredient ingr : copyIngredients)
				if(ingr.apply(stack)) {
					copyIngredients.remove(ingr);
					copyStacks.remove(0);
					break fulfil;
				}
			
			return false;
		}
		
		return true;
	}
	
	
}
