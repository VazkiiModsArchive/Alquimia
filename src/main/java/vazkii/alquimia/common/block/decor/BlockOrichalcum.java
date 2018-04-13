package vazkii.alquimia.common.block.decor;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import vazkii.alquimia.common.base.AlquimiaCreativeTab;
import vazkii.alquimia.common.base.IAlquimiaBlock;
import vazkii.arl.block.BlockMod;

public class BlockOrichalcum extends BlockMod implements IAlquimiaBlock {

	public BlockOrichalcum() {
		super("orichalcum_block", Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(AlquimiaCreativeTab.INSTANCE);
	}

}
