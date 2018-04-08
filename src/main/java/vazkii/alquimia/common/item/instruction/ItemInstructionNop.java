package vazkii.alquimia.common.item.instruction;

import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionNop extends ItemInstruction {

	public ItemInstructionNop() {
		super("instruction_nop");
	}

	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		// literally nothing
	}

}
