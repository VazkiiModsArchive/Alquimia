package vazkii.alquimia.common.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.multiblock.Multiblock;
import vazkii.alquimia.common.ritual.RitualType;

public class RitualHandler {

	private static final List<RitualCandidate> candidates = new ArrayList();

	@SubscribeEvent 
	public static void worldTick(WorldTickEvent event) {
		if(event.phase == Phase.END) {
			candidates.stream().filter((c) -> c.world == event.world && c.check()).forEach(RitualHandler::triggerRitual);
			candidates.removeIf(c -> c.remove);
		}
	}

	public static void triggerRitual(RitualCandidate candidate) {
		candidate.type.mutliblock[0].forEach(candidate.world, candidate.pos, Rotation.NONE, 'P', 
				(pos) -> candidate.world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState()));
	}

	public static void addCandidate(World world, BlockPos pos, RitualType type) {
		if(!world.isRemote) {
			RitualCandidate candidate = new RitualCandidate(world, pos, type);
			if(!candidates.contains(candidate))
				candidates.add(candidate);
		}
	}
	
	public static RitualType getCircleType(World world, BlockPos pos) {
		for(RitualType type : RitualType.class.getEnumConstants())
			if(type.isRitual(world, pos, false))
				return type;
		
		return null;
	}

	private static class RitualCandidate {
		
		final World world;
		final BlockPos pos;
		final RitualType type;

		boolean remove;

		RitualCandidate(World world, BlockPos pos, RitualType type) {
			this.world = world;
			this.pos = pos;
			this.type = type;
		}

		boolean check() {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() != ModBlocks.ash) {
				remove = true;
				return false;
			}

			remove = type.isRitual(world, pos, true);
			return remove;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this || (obj instanceof RitualCandidate && ((RitualCandidate) obj).world == world && ((RitualCandidate) obj).pos.equals(pos));
		}

	}

}
