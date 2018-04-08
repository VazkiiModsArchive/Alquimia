package vazkii.alquimia.common.block.decor;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.arl.block.BlockModFalling;

public class BlockAlchemicalAshBlock extends BlockModFalling implements IAlquimiaBlock {

	public BlockAlchemicalAshBlock() {
		super("alchemical_ash_block");
		setHardness(0.5F);
		setLightLevel(0.5F);
		setSoundType(SoundType.SAND);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

}
