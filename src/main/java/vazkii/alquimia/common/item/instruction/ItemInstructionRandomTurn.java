package vazkii.alquimia.common.item.instruction;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class ItemInstructionRandomTurn extends ItemInstruction {

	public ItemInstructionRandomTurn() {
		super("instruction_random_turn");
	}

	@Override
	public void run(ItemStack stack, IAutomaton automaton) {
		automaton.rotateHead(automaton.getRNG().nextBoolean() ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90);
	}

}
