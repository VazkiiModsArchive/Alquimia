package vazkii.alquimia.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.alquimia.common.handler.MultiblockTrackingHandler;
import vazkii.alquimia.common.multiblock.Multiblock;
import vazkii.arl.network.NetworkMessage;

public class MessageTrackMultiblock extends NetworkMessage<MessageTrackMultiblock> {

	public String id;
	public BlockPos pos;
	public int rot;
	
	public MessageTrackMultiblock() { }
	
	public MessageTrackMultiblock(Multiblock mb, BlockPos pos, Rotation rot) {
		id = mb == null ? "" : mb.res.toString();
		this.pos = pos;
		this.rot = rot.ordinal();
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer player = context.getServerHandler().player;
		
		if(id.isEmpty())
			MultiblockTrackingHandler.untrack(player);
		else MultiblockTrackingHandler.track(player, id, pos, rot);
		
		return null;
	}
	
}
