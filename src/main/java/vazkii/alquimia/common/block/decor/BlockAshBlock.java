package vazkii.alquimia.common.block.decor;

import net.minecraft.block.SoundType;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.arl.block.BlockModFalling;

public class BlockAshBlock extends BlockModFalling implements IAlquimiaBlock {

	public BlockAshBlock() {
		super("ash_block");
		setHardness(0.5F);
		setSoundType(SoundType.SAND);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}
	
}
