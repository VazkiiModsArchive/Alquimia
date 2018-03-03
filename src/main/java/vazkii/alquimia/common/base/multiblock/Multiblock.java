package vazkii.alquimia.common.base.multiblock;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Multiblock {

	final String[][] pattern;
	
	Map<Character, IBlockState> stateMap;
	IBlockState[][][] stateTargets;
	int offX, offY, offZ;
	int sizeX, sizeY, sizeZ;
	
	public Multiblock(String[][] pattern, Object... targets) {
		this.pattern = pattern;
		build(targets, getPatternDimensions());
	}
	
	public void place(World world, BlockPos pos) {
		BlockPos start = pos.add(-offX, -offY, -offZ);
		for(int x = 0; x < sizeX; x++)
			for(int y = 0; y < sizeY; y++)
				for(int z = 0; z < sizeZ; z++)
					world.setBlockState(start.add(x, y, z), stateTargets[x][y][z]);
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
	
	void build(Object[] targets, int[] dimensions) {
		if(targets.length % 2 == 1)
			throw new IllegalArgumentException("Illegal argument length for targets array " + targets.length);
		
		stateMap = new HashMap();
		for(int i = 0; i < targets.length / 2; i++) {
			char c = (Character) targets[i * 2];
			Object o = targets[i * 2 + 1];
			IBlockState state = o instanceof Block ? ((Block) o).getDefaultState() : (IBlockState) o;
			stateMap.put(c, state);
		}
		
		stateMap.put(' ', Blocks.AIR.getDefaultState());
		if(!stateMap.containsKey('0'))
			stateMap.put('0', Blocks.AIR.getDefaultState());
			
		boolean foundCenter = false;
		
		sizeX = dimensions[1];
		sizeY = dimensions[0];
		sizeZ = dimensions[2];
		stateTargets = new IBlockState[dimensions[1]][dimensions[0]][dimensions[2]];
		for(int y = 0; y < dimensions[0]; y++)
			for(int x = 0; x < dimensions[1]; x++)
				for(int z = 0; z < dimensions[2]; z++) {
					char c = pattern[y][x].charAt(z);
					if(!stateMap.containsKey(c))
						throw new IllegalArgumentException("Character " + c + " isn't mapped");
					
					IBlockState state = stateMap.get(c);
					if(c == '0') {
						if(foundCenter)
							throw new IllegalArgumentException("A structure can't have two centers");
						foundCenter = true;
						offX = x;
						offY = sizeY - y - 1;
						offZ = z;
					}
					
					stateTargets[x][sizeY - y - 1][z] = state;
				}
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
	
}
