package vazkii.alquimia.common.base;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.lib.LibMisc;

public class AlquimiaCreativeTab extends CreativeTabs {

	public static AlquimiaCreativeTab INSTANCE = new AlquimiaCreativeTab();
	NonNullList list;	

	public AlquimiaCreativeTab() {
		super(LibMisc.MOD_ID);
//		setNoTitle();
//		setBackgroundImageName(LibResources.GUI_CREATIVE);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(ModItems.lexicon);
	}

	@Override
	public ItemStack getTabIconItem() {
		return getIconItemStack();
	}

	@Override
	public boolean hasSearchBar() {
		return false;//true;
	}

	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
		list = p_78018_1_;

		addItem(ModItems.lexicon);
	}

	private void addItem(Item item) {
		item.getSubItems(this, list);
	}

	private void addBlock(Block block) {
		addItem(Item.getItemFromBlock(block));
	}
	
}