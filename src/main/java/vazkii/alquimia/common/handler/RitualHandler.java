package vazkii.alquimia.common.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.entity.EntityRitualLogic;
import vazkii.alquimia.common.ritual.ModRituals;
import vazkii.alquimia.common.ritual.Ritual;
import vazkii.alquimia.common.ritual.RitualType;
import vazkii.arl.util.VanillaPacketDispatcher;

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
		Collection<Ritual> possibleRituals = ModRituals.ritualsPerType.get(candidate.type);
		if(possibleRituals != null && !possibleRituals.isEmpty()) {
			List<ItemStack> stacks = new ArrayList();
			List<TileEntity> tiles = new ArrayList();
			candidate.type.mutliblock[0].forEach(candidate.world, candidate.pos, Rotation.NONE, 'P', 
					(pos) -> {
						TileEntity tile = candidate.world.getTileEntity(pos);
						if(tile instanceof IInventory) {
							IInventory inv = (IInventory) tile;
							ItemStack stack = inv.getStackInSlot(0);
							if(!stack.isEmpty()) {
								stacks.add(stack);
								tiles.add(tile);
							}
						}
					});

			BlockPos center = candidate.type.getCenter(candidate.pos);
			for(Ritual r : possibleRituals) {
				if(r.matches(stacks) && r.canRun(candidate.world, center)) {
					tiles.forEach((tile) -> {
						IInventory inv = (IInventory) tile;
						ItemStack stack = inv.getStackInSlot(0);
						r.consumeItem(tile, stack);

						VanillaPacketDispatcher.dispatchTEToNearbyPlayers((TileEntity) inv);

						if(candidate.world instanceof WorldServer) {
							BlockPos pos = ((TileEntity) inv).getPos();
							((WorldServer) candidate.world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 100, 0.4, 0, 0.4, 0.01F);
							((WorldServer) candidate.world).spawnParticle(EnumParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 20, 0.4, 0, 0.4, 0.05F);
						}
					});

					startRitual(candidate.world, center, r);
				}
			}
		}
	}

	public static void startRitual(World world, BlockPos pos, Ritual ritual) {
		EntityRitualLogic logic = new EntityRitualLogic(world, pos, ritual);
		NBTTagCompound cmp = logic.getEntityData();
		if(!ritual.run(world, pos, cmp))
			world.spawnEntity(logic);
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
