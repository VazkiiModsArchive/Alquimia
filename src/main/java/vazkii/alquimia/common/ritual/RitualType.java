package vazkii.alquimia.common.ritual;

import java.util.function.Function;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.multiblock.Multiblock;

public enum RitualType {

	SMALL(ModMultiblocks.small_ritual_circle, (pos) -> pos.down(2)),
	MEDIUM(ModMultiblocks.medium_ritual_circle, (pos) -> pos.down(3)),
	LARGE(ModMultiblocks.large_ritual_circle, (pos) -> pos.down(4));
	
	private RitualType(Multiblock[] multiblock, Function<BlockPos, BlockPos> centerApplier) {
		this.mutliblock = multiblock;
		this.centerApplier = centerApplier;
	}
	
	public final Multiblock[] mutliblock;
	private final Function<BlockPos, BlockPos> centerApplier;
	
	public boolean isRitual(World world, BlockPos pos, boolean lit) {
		return mutliblock[lit ? 1 : 0].validate(world, pos);
	}
	
	public BlockPos getCenter(BlockPos pos) {
		return centerApplier.apply(pos);
	}
	
}
