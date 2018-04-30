package vazkii.alquimia.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.item.ModItems;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;

public class ModCraftingRecipes {

	public static void init() {
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.lexicon), Items.BOOK, Items.CLAY_BALL);
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.lexicon), Items.BOOK, ModItems.lexicon);
		
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.orichalcum_ingot, 1), "ingotIron", ModItems.cinnabar, ModItems.cinnabar, ModItems.cinnabar);
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(ModItems.orichalcum_ingot, 2), "ingotGold", ModItems.cinnabar, ModItems.cinnabar);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.orichalcum_gear, 6), 
				" O ", "O O", " O ",
				'O', ModItems.orichalcum_ingot);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.divining_rod), 
				" AS", " S ", "S  ",
				'A', ModItems.alchemical_ash,
				'S', Items.STICK);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.pedestal), 
				"SAS", " S ", "SCS",
				'A', ModItems.alchemical_ash,
				'S', "stone",
				'C', ModItems.cinnabar);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModBlocks.automaton), 
				"OGO", "GPG",
				'O', ModItems.orichalcum_ingot,
				'G', ModItems.orichalcum_gear,
				'P', Blocks.PISTON);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.sticky_head), 
				" W ", "SGB", " W ",
				'W', "plankWood",
				'S', Items.STICK,
				'G', ModItems.orichalcum_gear,
				'B', Items.SLIME_BALL);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.blade_head), 
				"  I", " I ", "G  ",
				'G', ModItems.orichalcum_gear,
				'I', "ingotIron");
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_clockwise, 2), 
				"I ", " G",
				'G', ModItems.orichalcum_gear,
				'I', ModItems.orichalcum_ingot);
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_counterclockwise, 2), 
				"G ", " I",
				'G', ModItems.orichalcum_gear,
				'I', ModItems.orichalcum_ingot);
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_up, 2), 
				"G", "I",
				'G', ModItems.orichalcum_gear,
				'I', ModItems.orichalcum_ingot);
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_down, 2), 
				"I", "G",
				'G', ModItems.orichalcum_gear,
				'I', ModItems.orichalcum_ingot);
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_nop, 3), 
				"G", "I", "G",
				'G', ModItems.orichalcum_gear,
				'I', ModItems.orichalcum_ingot);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_random_turn), 
				"LGR",
				'G', ModItems.orichalcum_gear,
				'L', ModItems.instruction_counterclockwise,
				'R', ModItems.instruction_clockwise);
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.instruction_repeat), 
				"GAG",
				'G', ModItems.orichalcum_gear,
				'A', ModItems.instruction_nop);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(ModItems.drill_head), 
				" II", " II", "G  ",
				'G', ModItems.orichalcum_gear,
				'I', "ingotIron");
		
		addBlock(ModBlocks.ash_block, ModBlocks.ash);
		addBlock(ModBlocks.alchemical_ash_block, ModItems.alchemical_ash);
		addBlock(ModBlocks.cinnabar_block, ModItems.cinnabar);
		addBlock(ModBlocks.orichalcum_block, ModItems.orichalcum_ingot);
	}
	
	static void addBlock(Block block, Block item) {
		addBlock(block, ProxyRegistry.getItemMapping(item));
	}
	
	static void addBlock(Block block, Item item) {
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(block), item, item, item, item, item, item, item, item, item);
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(item, 9), block);
	}
	 
}
