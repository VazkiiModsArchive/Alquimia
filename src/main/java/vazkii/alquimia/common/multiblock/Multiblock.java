package vazkii.alquimia.common.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Multiblock {

	final String[][] pattern;
	
	StateMatcher[][][] stateTargets;
	int centerX, centerY, centerZ;
	int offX, offY, offZ;
	int sizeX, sizeY, sizeZ;
	boolean symmetrical;
	
	public Multiblock(String[][] pattern, Object... targets) {
		this.pattern = pattern;
		build(targets, getPatternDimensions());
	}
	
	public Multiblock offset(int x, int y, int z) {
		offX += x;
		offY += y;
		offZ += z;
		return this;
	}
	
	public Multiblock setOffset(int x, int y, int z) {
		offX = x;
		offY = y;
		offZ = z;
		return this;
	}
	
	public Multiblock setSymmetrical(boolean symmetrical) {
		this.symmetrical = symmetrical;
		return this;
	}
	
	public void place(World world, BlockPos pos) {
		BlockPos start = pos.add(-offX, -offY, -offZ);
		for(int x = 0; x < sizeX; x++)
			for(int y = 0; y < sizeY; y++)
				for(int z = 0; z < sizeZ; z++)
					world.setBlockState(start.add(x, y, z), stateTargets[x][y][z].displayState);
	}
	
	public boolean validate(World world, BlockPos pos) {
		// TODO validate when asymmetrical
		
		BlockPos start = pos.add(-offX, -offY, -offZ);
		if(!test(world, start, centerX, centerY, centerZ))
			return false;
		
		for(int x = 0; x < sizeX; x++)
			for(int y = 0; y < sizeY; y++)
				for(int z = 0; z < sizeZ; z++)
					if(!test(world, start, x, y, z))
						return false;
		
		return true;
	}
	
	private boolean test(World world, BlockPos start, int x, int y, int z) {
		BlockPos checkPos = start.add(x, y, z);
		Predicate<IBlockState> pred = stateTargets[x][y][z].statePredicate;
		return pred.test(world.getBlockState(checkPos));
	}
	
	void build(Object[] targets, int[] dimensions) {
		if(targets.length % 2 == 1)
			throw new IllegalArgumentException("Illegal argument length for targets array " + targets.length);
		
		Map<Character, StateMatcher> stateMap = new HashMap();
		for(int i = 0; i < targets.length / 2; i++) {
			char c = (Character) targets[i * 2];
			Object o = targets[i * 2 + 1];
			StateMatcher state = null;
			
			if(o instanceof Block) 
				state = new StateMatcher((Block) o);
			else if(o instanceof IBlockState)
				state = new StateMatcher((IBlockState) o);
			else if(o instanceof StateMatcher)
				state = (StateMatcher) o;
			else throw new IllegalArgumentException("Invalid target " + o);
				
			stateMap.put(c, state);
		}
		
		if(!stateMap.containsKey(' '))
			stateMap.put(' ', new StateMatcher(Blocks.AIR));
		if(!stateMap.containsKey('0'))
			stateMap.put('0', new StateMatcher(Blocks.AIR));
			
		boolean foundCenter = false;
		
		sizeX = dimensions[1];
		sizeY = dimensions[0];
		sizeZ = dimensions[2];
		stateTargets = new StateMatcher[dimensions[1]][dimensions[0]][dimensions[2]];
		for(int y = 0; y < dimensions[0]; y++)
			for(int x = 0; x < dimensions[1]; x++)
				for(int z = 0; z < dimensions[2]; z++) {
					char c = pattern[y][x].charAt(z);
					if(!stateMap.containsKey(c))
						throw new IllegalArgumentException("Character " + c + " isn't mapped");
					
					StateMatcher matcher = stateMap.get(c);
					if(c == '0') {
						if(foundCenter)
							throw new IllegalArgumentException("A structure can't have two centers");
						foundCenter = true;
						offX = centerX = x;
						offY = centerY = sizeY - y - 1;
						offZ = centerZ = z;
					}
					
					stateTargets[x][sizeY - y - 1][z] = matcher;
				}
		
		if(!foundCenter)
			throw new IllegalArgumentException("A structure can't have no center");
	}
	
	int[] getPatternDimensions() {
		int expectedLenX = -1;
		int expectedLenZ = -1;
		for(String[] arr : pattern) {
			if(expectedLenX == -1)
				expectedLenX = arr.length;
			if(arr.length != expectedLenX)
				throw new IllegalArgumentException("Inconsistent array length. Expected" + expectedLenX + ", got " + arr.length);
			
			for(String s : arr) {
				if(expectedLenZ == -1)
					expectedLenZ = s.length();
				if(s.length() != expectedLenZ)
					throw new IllegalArgumentException("Inconsistent array length. Expected" + expectedLenX + ", got " + arr.length);
			}
		}
		
		return new int[] { pattern.length, expectedLenX, expectedLenZ }; 
	}
	
	public static class StateMatcher {
		
		final IBlockState displayState;
		final Predicate<IBlockState> statePredicate;
		
		public StateMatcher(IBlockState displayState, Predicate<IBlockState> statePredicate) {
			this.displayState = displayState;
			this.statePredicate = statePredicate;
		}
		
		public StateMatcher(IBlockState displayState, boolean strict) {
			this(displayState, 
					strict ? ((state) -> state.getBlock() == displayState.getBlock() && state.getProperties().equals(displayState.getProperties()))
							: ((state) -> state.getBlock() == displayState.getBlock()));
		}
		
		public StateMatcher(IBlockState displayState) {
			this(displayState, true);
		}
		
		public StateMatcher(Block block) {
			this(block.getDefaultState(), false);
		}
		
		public StateMatcher() {
			this(Blocks.AIR.getDefaultState(), (state) -> true);
		}

	}
	
}
