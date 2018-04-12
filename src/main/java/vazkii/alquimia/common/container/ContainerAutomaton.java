package vazkii.alquimia.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.tile.TileAutomaton;
import vazkii.alquimia.common.item.interf.IAutomatonHeadItem;
import vazkii.alquimia.common.item.interf.IAutomatonInstruction;
import vazkii.arl.container.ContainerBasic;
import vazkii.arl.container.slot.SlotType;

public class ContainerAutomaton extends ContainerBasic<TileAutomaton> {

	public ContainerAutomaton(InventoryPlayer playerInv, TileAutomaton tile) {
		super(playerInv, tile);
	}

	@Override
	public int addSlots() {
		addSlotToContainer(new SlotAutomton(tile, 0, 16, 35, IAutomatonHeadItem.class));
		
		for(int i = 0; i < TileAutomaton.INSTRUCTION_SLOTS; i++) {
			int j = i >= 6 ? (11 - i) : i;
			addSlotToContainer(new SlotAutomton(tile, i + 1, 53 + j * 18, 25 + (i / 6) * 20, IAutomatonInstruction.class));
		}
		
		return tile.getSizeInventory();
	}
	
	private static class SlotAutomton extends SlotType {

		private final TileAutomaton automaton;
		
		public SlotAutomton(TileAutomaton automaton, int index, int xPosition, int yPosition, Class<?> clazz) {
			super(automaton, index, xPosition, yPosition, clazz);
			this.automaton = automaton;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return super.isItemValid(stack) && !automaton.isEnabled();
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			return !automaton.isEnabled();
		}
		
	}
	
	// TODO do a thing so modules only send one at a time

}
