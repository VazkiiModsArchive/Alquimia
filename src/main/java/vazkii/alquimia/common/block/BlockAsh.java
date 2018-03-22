package vazkii.alquimia.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.arl.block.BlockModDust;

public class BlockAsh extends BlockModDust implements IAlquimiaBlock {

	public BlockAsh() {
		super("ash");
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

	@Override
	public int getColor(IBlockAccess world, IBlockState state, BlockPos pos, int tint) {
		return 0xDDDDDD;
	}

}
