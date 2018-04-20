package vazkii.alquimia.common.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.block.interf.IAutomaton;

public class AutomatonUtil {

	public static boolean hasObstruction(IAutomaton automaton, boolean allowNonSolid) {
		World world = automaton.getWorld();
		EnumFacing facing = automaton.getCurrentFacing();
		EnumFacing endFacing = automaton.getCurrentRotation().rotate(facing);

		BlockPos current = automaton.getPos();
		BlockPos target = current.offset(facing);

		BlockPos end = current.offset(endFacing);
		BlockPos diag = end.offset(facing);

		if(!allowNonSolid)
			return !world.isAirBlock(end) || !world.isAirBlock(diag);
		else {
			IBlockState endState = world.getBlockState(end);
			IBlockState diagState = world.getBlockState(diag);
			AxisAlignedBB endAABB = endState.getBlock().getCollisionBoundingBox(endState, world, end);
			AxisAlignedBB diagAABB = diagState.getBlock().getCollisionBoundingBox(diagState, world, diag);
			return endAABB != null || diagAABB != null;
		}
	}
	
	public static void moveEntitiesAt(World world, BlockPos pos, EnumFacing direction, float speed) {
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos));
		moveEntitiesAt(entities, direction, speed);
	}
	
	public static void moveEntitiesAt(List<Entity> entities, EnumFacing direction, float speed) {
		float x = direction.getFrontOffsetX() * speed;
		float z = direction.getFrontOffsetZ() * speed;
		entities.forEach((e) -> e.move(MoverType.PISTON, x, 0, z));
	}

}
