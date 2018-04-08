package vazkii.alquimia.common.item.instruction;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionCounterclockwise extends ItemInstruction {

	public ItemInstructionCounterclockwise() {
		super("instruction_counterclockwise");
	}

	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		automaton.rotate(Rotation.COUNTERCLOCKWISE_90);
	}

}
