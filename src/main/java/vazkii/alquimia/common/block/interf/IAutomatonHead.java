package vazkii.alquimia.common.block.interf;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAutomatonHead {

	public default void onAttached(IAutomaton automaton) {}
	public default void onTicked(IAutomaton automaton) {}
	public default void onRemoved(IAutomaton automaton) {}
	public default boolean onRotateStart(IAutomaton automaton) { return true; }
	public default void onRotateEnd(IAutomaton automaton) {}
	public default void onEngageStatusStart(IAutomaton automaton) {}
	public default void onEngageStatusEnd(IAutomaton automaton) {}
	public default void writeToNBT(IAutomaton automaton, NBTTagCompound cmp)  {}
	public default void readFromNBT(IAutomaton automaton, NBTTagCompound cmp)  {}
	
	@SideOnly(Side.CLIENT) 
	public default boolean render(IAutomaton automaton, float rotation, float translation, float partTicks) {
		return true;
	}
	
}
