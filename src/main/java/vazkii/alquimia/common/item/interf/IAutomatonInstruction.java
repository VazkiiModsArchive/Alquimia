package vazkii.alquimia.common.item.interf;

import net.minecraft.item.ItemStack;
import vazkii.alquimia.common.block.interf.IAutomaton;

public interface IAutomatonInstruction {

	public void run(ItemStack stack, IAutomaton automaton);
	
	public default boolean scan(ItemStack stack, IAutomaton automaton) { 
		return true; 
	}
	
}
