package vazkii.alquimia.common.base;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.block.ModBlocks;
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

		// Basic Items/Resources
		addItem(ModItems.lexicon);
		addBlock(ModBlocks.ash);
		addItem(ModItems.alchemical_ash);
		addItem(ModItems.cinnabar);
		addItem(ModItems.orichalcum_ingot);
		addItem(ModItems.orichalcum_gear);
		
		// Progression Tools
		addItem(ModItems.divining_rod);
		addBlock(ModBlocks.pedestal);
		
		// Automaton, Heads, and Instructions
		addBlock(ModBlocks.automaton);
		
		addItem(ModItems.sticky_head);
		
		addItem(ModItems.instruction_clockwise);
		addItem(ModItems.instruction_counterclockwise);
		addItem(ModItems.instruction_up);
		addItem(ModItems.instruction_down);
		addItem(ModItems.instruction_nop);
		
		// Decor Blocks
		addBlock(ModBlocks.ash_block);
		addBlock(ModBlocks.alchemical_ash_block);
		addBlock(ModBlocks.cinnabar_block);
		addBlock(ModBlocks.orichalcum_block);

		// Other Stuff
		addItem(ModItems.test_rod);
	}

	private void addItem(Item item) {
		item.getSubItems(this, list);
	}

	private void addBlock(Block block) {
		addItem(Item.getItemFromBlock(block));
	}
	
}
