package vazkii.alquimia.common.ritual;

import java.util.function.Function;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.multiblock.Multiblock;

public enum RitualType {

	SMALL(ModMultiblocks.small_ritual_circle, (pos) -> pos.east(2), "alquimia.ritual.size.small"),
	MEDIUM(ModMultiblocks.medium_ritual_circle, (pos) -> pos.east(3), "alquimia.ritual.size.medium"),
	LARGE(ModMultiblocks.large_ritual_circle, (pos) -> pos.east(4), "alquimia.ritual.size.large");
	
	private RitualType(Multiblock[] multiblock, Function<BlockPos, BlockPos> centerApplier, String name) {
		this.mutliblock = multiblock;
		this.name = name;
		this.centerApplier = centerApplier;
	}
	
	public final Multiblock[] mutliblock;
	public final String name;
	public final Function<BlockPos, BlockPos> centerApplier;
	
	public boolean isRitual(World world, BlockPos pos, boolean lit) {
		return mutliblock[lit ? 1 : 0].validate(world, pos);
	}
	
	public BlockPos getCenter(BlockPos pos) {
		return centerApplier.apply(pos);
	}
	
}
