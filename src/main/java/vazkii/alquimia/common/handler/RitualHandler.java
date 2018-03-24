package vazkii.alquimia.common.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.multiblock.Multiblock;

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
		// TODO temporary
		BlockPos center = candidate.pos.east(2);
		candidate.world.addWeatherEffect(new EntityLightningBolt(candidate.world, center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5, true));
	}

	public static void addCandidate(World world, BlockPos pos) {
		if(!world.isRemote) {
			RitualCandidate candidate = new RitualCandidate(world, pos);
			if(!candidates.contains(candidate))
				candidates.add(candidate);
		}
	}
	
	public static boolean isCircle(World world, BlockPos pos, boolean lit) {
		// TODO handle bigger ones
		Multiblock mb = lit ? ModMultiblocks.small_ritual_circle_lit : ModMultiblocks.small_ritual_circle;
		return mb.validate(world, pos);
	}

	private static class RitualCandidate {
		
		final World world;
		final BlockPos pos;

		boolean remove;

		RitualCandidate(World world, BlockPos pos) {
			this.world = world;
			this.pos = pos;
		}

		boolean check() {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() != ModBlocks.ash) {
				remove = true;
				return false;
			}

			// TODO not always small
			remove = isCircle(world, pos, true);
			return remove;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this || (obj instanceof RitualCandidate && ((RitualCandidate) obj).world == world && ((RitualCandidate) obj).pos.equals(pos));
		}

	}

}
