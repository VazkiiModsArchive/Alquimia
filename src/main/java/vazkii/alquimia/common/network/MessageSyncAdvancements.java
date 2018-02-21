package vazkii.alquimia.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.client.base.ClientAdvancements;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;

public class MessageSyncAdvancements extends NetworkMessage<MessageSyncAdvancements> {

	public String[] done;
	
	public MessageSyncAdvancements() { }
	
	public MessageSyncAdvancements(String[] done) { 
		this.done = done;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> ClientAdvancements.setDoneAdvancements(done));
		return null;
	}
	
}
