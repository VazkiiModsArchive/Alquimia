package vazkii.alquimia.common.block.head;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.block.interf.IAutomaton;
import vazkii.alquimia.common.block.interf.IAutomatonHead;

public class HeadSticky implements IAutomatonHead {

	private static final String TAG_STATE = "state";
	private static final String TAG_TILE = "tile";
	
	IBlockState pickedUpState = null;
	TileEntity pickedUpTE = null;
	
	@Override
	public void onRotateStart(IAutomaton automaton) {
		if(automaton.isUp()) {
			World world = automaton.getWorld();
			BlockPos target = automaton.getPos().offset(automaton.getCurrentFacing());

			if(!world.isAirBlock(target)) {
				System.out.println("PICKING UP FROM " + target);
				pickedUpState = world.getBlockState(target);
				pickedUpTE = world.getTileEntity(target);
				world.removeTileEntity(target);
				world.setBlockToAir(target);
			}
		}
	}
	
	@Override
	public void onRotateEnd(IAutomaton automaton) {
		if(pickedUpState != null) {
			World world = automaton.getWorld();
			BlockPos target = automaton.getPos().offset(automaton.getCurrentFacing());
			
			if(world.isAirBlock(target)) {
				System.out.println("SETTING DOWN TO " + target);
				world.setBlockState(target, pickedUpState);
				if(pickedUpTE != null)
					world.setTileEntity(target, pickedUpTE);
				pickedUpState = null;
				pickedUpTE = null;
			}
		}
	}
	
	// TODO head causes inventory to not sync
	@Override
	public void writeToNBT(IAutomaton automaton, NBTTagCompound cmp) {
		NBTTagCompound innerCmp = new NBTTagCompound();
		if(pickedUpState != null)
			NBTUtil.writeBlockState(innerCmp, pickedUpState);
		cmp.setTag(TAG_STATE, innerCmp);
		
		innerCmp = new NBTTagCompound();
		if(pickedUpTE != null)
			pickedUpTE.writeToNBT(innerCmp);
		cmp.setTag(TAG_TILE, innerCmp);
	}
	
	@Override
	public void readFromNBT(IAutomaton automaton, NBTTagCompound cmp) {
		NBTTagCompound innerCmp = cmp.getCompoundTag(TAG_STATE);
		if(!innerCmp.getKeySet().isEmpty())
			pickedUpState = NBTUtil.readBlockState(innerCmp);
		else pickedUpState = null;
		
		innerCmp = cmp.getCompoundTag(TAG_TILE);
		if(!innerCmp.getKeySet().isEmpty())
			pickedUpTE = TileEntity.create(automaton.getWorld(), innerCmp);
		else pickedUpTE = null;
	}
	
}
