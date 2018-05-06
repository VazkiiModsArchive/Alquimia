package vazkii.alquimia.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.alquimia.common.handler.reagent.IReagentHolder;
import vazkii.alquimia.common.handler.reagent.ReagentHandler;
import vazkii.alquimia.common.handler.reagent.ReagentList;

public class ItemReagentPouch extends ItemAlquimia implements IReagentHolder {

	private static final String TAG_ITEM_LIST = "itemList";
	
	public ItemReagentPouch() {
		super("reagent_pouch");
		setMaxStackSize(1);
		addBooleanPropertyOverride("has_items", ItemReagentPouch::hasItems);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		
		if(isInCreativeTab(tab)) {
			subItems.add(with(new ItemStack(Items.IRON_INGOT, 120), new ItemStack(Items.GOLD_INGOT, 1892), new ItemStack(Items.CARROT, 1212)));
			subItems.add(with(new ItemStack(Items.ARROW, 10), new ItemStack(Items.APPLE, 20), new ItemStack(Items.BLAZE_ROD, 7753)));
		}
	}
	
	public static boolean hasItems(ItemStack stack) {
		ReagentList list = ReagentHandler.getReagents(stack);
		return !list.stacks.isEmpty();
	}
	
	public static ItemStack with(ItemStack... reagents) {
		ItemStack stack = new ItemStack(ModItems.reagent_pouch);
		ReagentList list = ReagentList.of(reagents);
		list.commit(stack);
		
		return stack;
	}

}
