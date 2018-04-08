package vazkii.alquimia.common.item.instruction;

import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionNop extends ItemInstruction {

	public ItemInstructionNop() {
		super("instruction_nop");
	}

	@Override
	public void run(IAutomaton automaton) {
		// literally nothing
	}

}
