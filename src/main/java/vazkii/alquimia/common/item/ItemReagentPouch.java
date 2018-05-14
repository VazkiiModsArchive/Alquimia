package vazkii.alquimia.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.alquimia.common.crafting.recipe.RecipeRemoveReagents;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;
import vazkii.arl.util.ItemNBTHelper;

public class ItemReagentPouch extends ItemAlquimia implements IReagentHolder {

	private static final int TYPE_LIMIT = 64;
	private static final int STACK_LIMIT = 64 * 100 * ReagentList.DEFAULT_MULTIPLICATION_FACTOR;
	
	private static final String TAG_ITEM_LIST = "itemList";
	private static final String TAG_IS_CREATIVE = "isCreative";

	public ItemReagentPouch() {
		super("reagent_pouch");
		setMaxStackSize(1);
		addBooleanPropertyOverride("has_items", ItemReagentPouch::hasItems);
		
		new RecipeRemoveReagents();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		
		if(isInCreativeTab(tab)) {
			ItemStack creative = new ItemStack(this);
			ItemNBTHelper.setBoolean(creative, TAG_IS_CREATIVE, true);
			subItems.add(creative);
		}
	}
	
	public static boolean hasItems(ItemStack stack) {
		if(ItemNBTHelper.getBoolean(stack, TAG_IS_CREATIVE, false))
				return true;
		
		ReagentList list = ReagentHandler.getReagents(stack);
		return !list.stacks.isEmpty();
	}
	
	public static ItemStack with(ItemStack... reagents) {
		ItemStack stack = new ItemStack(ModItems.reagent_pouch);
		ReagentList list = ReagentList.of(reagents);
		list.commit(stack);
		
		return stack;
	}

	@Override
	public int getReagentTypeLimit(ItemStack stack) {
		return TYPE_LIMIT;
	}

	@Override
	public int getReagentStackLimit(ItemStack stack) {
		return STACK_LIMIT;
	}
	
	@Override
	public boolean isCreativeReagentHolder(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_IS_CREATIVE, false);
	}

}
