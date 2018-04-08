package vazkii.alquimia.common.item.instruction;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionClockwise extends ItemInstruction {

	public ItemInstructionClockwise() {
		super("instruction_clockwise");
	}

	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		automaton.rotate(Rotation.CLOCKWISE_90);
	}

}
