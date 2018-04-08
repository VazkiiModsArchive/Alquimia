package vazkii.alquimia.common.item.instruction;

import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionUp extends ItemInstruction {

	public ItemInstructionUp() {
		super("instruction_up");
	}

	@Override
	public void run(IAutomaton automaton) {
		automaton.setUp(true);
	}

}
