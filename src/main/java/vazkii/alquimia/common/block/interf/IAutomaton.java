package vazkii.alquimia.common.block.interf;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAutomaton {
	
	public World getWorld();
	public BlockPos getPos();
	public IAutomatonHead getHead();
	public EnumFacing getCurrentFacing();
	public EnumFacing getPreviousFacing();
	public Rotation getCurrentRotation();
	public void rotate(Rotation rotation);
	public boolean isUp();
	public boolean wasUp();
	public void setUp(boolean up);
	public boolean isEnabled();
	public boolean isExecuting();
	public int getInstructionTime();
	
}
