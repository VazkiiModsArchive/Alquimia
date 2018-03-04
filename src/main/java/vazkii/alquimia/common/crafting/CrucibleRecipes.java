package vazkii.alquimia.common.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.multiblock.ModMultiblocks;

public class CrucibleRecipes {

	private static final String TAG_BURNING_TIME = "alquimia:burning_time";

	public static final HashMap<ResourceLocation, CrucibleRecipe> RECIPE_MAPPING = new HashMap();
	public static final List<CrucibleRecipe> CRUCIBLE_RECIPES = new ArrayList();

	public static void init() {
		registerRecipe("ash", Ingredient.fromItem(Item.getItemFromBlock(Blocks.COBBLESTONE)), new ItemStack(Items.GUNPOWDER), 20, 10); // TODO

		MinecraftForge.EVENT_BUS.register(CrucibleRecipes.class);
	}

	private static void registerRecipe(String name, Ingredient input, ItemStack output, int time, int xp) {
		ResourceLocation res = new ResourceLocation(LibMisc.MOD_ID, name);
		CrucibleRecipe recipe = new CrucibleRecipe(input, output, time, xp);
		RECIPE_MAPPING.put(res, recipe);
		CRUCIBLE_RECIPES.add(recipe);
	}

	@SubscribeEvent
	public static void update(WorldTickEvent event) {
		if(event.phase == Phase.END) {
			List<Entity> entities = new ArrayList(event.world.loadedEntityList);
			entities.forEach((entity) -> {
				if(entity instanceof EntityItem)
					tickEntity((EntityItem) entity);
			});
		}
	}

	private static void tickEntity(EntityItem entity) {
		if(!ModMultiblocks.crucible.validate(entity.world, entity.getPosition()))
			return;

		int time = entity.getEntityData().getInteger(TAG_BURNING_TIME);
		if(!entity.getEntityData().hasKey(TAG_BURNING_TIME))
			time = -160;
		
		ItemStack stack = entity.getItem().copy();
		boolean matched = false;
		for(CrucibleRecipe recipe : CRUCIBLE_RECIPES) {
			matched = recipe.tick(entity, time);
			if(matched) {
				ItemStack stack2 = entity.getItem();
				if(!ItemStack.areItemStacksEqual(stack, stack2))
					time = 0;

				break;
			}
		}

		entity.getEntityData().setInteger(TAG_BURNING_TIME, time + 1);
		if(!matched && time > 200) {
			stack.shrink(1);
			entity.setItem(stack);
		}
	}

}
