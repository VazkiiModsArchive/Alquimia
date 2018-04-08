package vazkii.alquimia.common.item.instruction;

import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionDown extends ItemInstruction {

	public ItemInstructionDown() {
		super("instruction_down");
	}

	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		automaton.setUp(false);
	}

}
