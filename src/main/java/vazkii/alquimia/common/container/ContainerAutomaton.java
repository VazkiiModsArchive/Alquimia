package vazkii.alquimia.common.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
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
		addSlotToContainer(new SlotType(tile, 0, 16, 35, IAutomatonHeadItem.class));
		
		for(int i = 0; i < TileAutomaton.INSTRUCTION_SLOTS; i++)
			addSlotToContainer(new SlotType(tile, i + 1, 53 + i * 18, 35, IAutomatonInstruction.class));
		
		return tile.getSizeInventory();
	}
	
	// TODO do a thing so modules only send one at a time

}
