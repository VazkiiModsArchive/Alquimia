package vazkii.alquimia.common.block.interf;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAutomatonHead {

	public default void onAttached(IAutomaton automaton) {}
	public default void onTicked(IAutomaton automaton) {}
	public default void onRemoved(IAutomaton automaton) {}
	public default void onRotateStart(IAutomaton automaton) {}
	public default void onRotateEnd(IAutomaton automaton) {}
	public default void onUpStart(IAutomaton automaton) {}
	public default void onUpEnd(IAutomaton automaton) {}
	public default void onDownStart(IAutomaton automaton) {}
	public default void onDownEnd(IAutomaton automaton) {}
	public default void writeToNBT(IAutomaton automaton, NBTTagCompound cmp)  {}
	public default void readFromNBT(IAutomaton automaton, NBTTagCompound cmp)  {}
	
	@SideOnly(Side.CLIENT) 
	public default boolean render(IAutomaton automaton, float upStatus, float angle) {
		return true;
	}
	
}
