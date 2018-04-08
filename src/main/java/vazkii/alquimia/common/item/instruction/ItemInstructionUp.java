package vazkii.alquimia.common.item.instruction;

import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionUp extends ItemInstruction {

	public ItemInstructionUp() {
		super("instruction_up");
	}

	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		automaton.setUp(true);
	}

}
