package vazkii.alquimia.common.block.head;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;

public class HeadSticky implements IAutomatonHead {

	IBlockState pickedUpState = null;
	TileEntity pickedUpTE = null;
	
	@Override
	public void onRotateStart(IAutomaton automaton) {
		if(automaton.isUp()) {
			World world = automaton.getWorld();
			BlockPos target = automaton.getPos().offset(automaton.getCurrentFacing());
			pickedUpState = world.getBlockState(target);
			pickedUpTE = world.getTileEntity(target);
			world.removeTileEntity(target);
			world.setBlockToAir(target);
		}
	}
	
	@Override
	public void onRotateEnd(IAutomaton automaton) {
		if(pickedUpState != null) {
			World world = automaton.getWorld();
			BlockPos target = automaton.getPos().offset(automaton.getCurrentFacing());
			world.setBlockState(target, pickedUpState);
			if(pickedUpTE != null)
				world.setTileEntity(target, pickedUpTE);
			pickedUpState = null;
			pickedUpTE = null;
		}
	}
	
}
