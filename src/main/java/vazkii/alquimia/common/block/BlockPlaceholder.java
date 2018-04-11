package vazkii.alquimia.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.arl.block.BlockMod;

public class BlockPlaceholder extends BlockMod implements IAlquimiaBlock {

	public BlockPlaceholder() {
		super("placeholder", Material.AIR);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		for(EnumFacing face : EnumFacing.HORIZONTALS)
			if(worldIn.getBlockState(pos.offset(face)).getBlock() == ModBlocks.automaton)
				return;
		
		worldIn.setBlockToAir(pos);
	}

	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

}
