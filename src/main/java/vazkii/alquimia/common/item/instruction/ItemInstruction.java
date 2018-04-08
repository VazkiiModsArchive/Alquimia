package vazkii.alquimia.common.item.instruction;

import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaItem;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.item.interf.IAutomatonInstruction;
import vazkii.arl.item.ItemMod;

public abstract class ItemInstruction extends ItemMod implements IAlquimiaItem, IAutomatonInstruction {

	public ItemInstruction(String name) {
		super(name);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

}
