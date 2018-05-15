package vazkii.alquimia.common.handler;

import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.client.handler.MultiblockVisualizationHandler;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.multiblock.Multiblock;

public final class MultiblockTrackingHandler {

	private static final WeakHashMap<EntityPlayer, MultiblockSettings> TRACKED_MULTIBLOCKS = new WeakHashMap(); 
	
	public static void track(EntityPlayer player, String id, BlockPos pos, int rot) {
		if(!player.world.isRemote)
			TRACKED_MULTIBLOCKS.put(player, new MultiblockSettings(id, pos, rot));
	}
	
	public static void untrack(EntityPlayer player) {
		TRACKED_MULTIBLOCKS.remove(player);
	}
	
	public static MultiblockSettings get(EntityPlayer player) {
		if(TRACKED_MULTIBLOCKS.containsKey(player)) {
			MultiblockSettings mb = TRACKED_MULTIBLOCKS.get(player);
			if(!mb.isValid(player.world))
				untrack(player);
			else
				return mb;
		}
		
		return null;
	}
	
	public static class MultiblockSettings {
		
		public final Multiblock mb;
		public final BlockPos pos;
		public final Rotation rot;
		
		public MultiblockSettings(String id, BlockPos pos, int rot) {
			mb = ModMultiblocks.MULTIBLOCKS.get(new ResourceLocation(id));
			this.pos = pos;
			this.rot = Rotation.values()[rot];
			
			if(mb == null) // won't crash the server because it's called on the network thread
				throw new IllegalArgumentException();
		}
		
		@SideOnly(Side.CLIENT)
		public MultiblockSettings() {
			mb = MultiblockVisualizationHandler.getMultiblock();
			pos = MultiblockVisualizationHandler.getStartPos();
			rot = MultiblockVisualizationHandler.getFacingRotation();
		}
		
		public boolean isValid(World world) {
			return !mb.validate(world, pos, rot);
		}
		
	}
	
}
