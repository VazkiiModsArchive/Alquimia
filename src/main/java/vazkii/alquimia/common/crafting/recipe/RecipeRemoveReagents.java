package vazkii.alquimia.common.crafting.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.recipe.ModRecipe;

public class RecipeRemoveReagents extends ModRecipe {

	ItemStack output = ItemStack.EMPTY;

	public RecipeRemoveReagents() {
		super(new ResourceLocation(LibMisc.MOD_ID, "remove_reagents"));
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack container = null;
		ItemStack reagent = null;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if(!stackAt.isEmpty()) {
				if(stackAt.getItem() instanceof IReagentHolder) {
					if(container == null)
						container = stackAt;
					else return false;
				} else {
					if(reagent == null)
						reagent = stackAt;
					else return false;
				}
			}
		}

		if(container != null && reagent != null) {
			ReagentList list = ReagentHandler.getReagents(container);
			return list.getCount(reagent) > 0;
		}

		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		output = ItemStack.EMPTY;
		ItemStack container = null;
		ItemStack reagent = null;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stackAt = inv.getStackInSlot(i);
			if(!stackAt.isEmpty()) {
				if(stackAt.getItem() instanceof IReagentHolder) {
					if(container == null)
						container = stackAt;
					else return ItemStack.EMPTY;
				} else {
					if(reagent == null)
						reagent = stackAt;
					else return ItemStack.EMPTY;
				}
			}
		}

		if(container != null && reagent != null) {
			output = container.copy();
			ReagentList list = ReagentHandler.getReagents(output);
			list.except(reagent);
			list.commit(output);
		}

		return output;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < ret.size(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && !(stack.getItem() instanceof IReagentHolder))
				stack.setCount(stack.getCount() + 1);
		}
		
		return ret;
	}
}
