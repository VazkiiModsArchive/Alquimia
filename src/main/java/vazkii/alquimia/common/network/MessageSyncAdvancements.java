package vazkii.alquimia.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.alquimia.client.base.ClientAdvancements;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;

public class MessageSyncAdvancements extends NetworkMessage<MessageSyncAdvancements> {

	public String[] done;
	public boolean showToast;
	
	public MessageSyncAdvancements() { }
	
	public MessageSyncAdvancements(String[] done, boolean showToast) { 
		this.done = done;
		this.showToast = showToast;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			ClientAdvancements.setDoneAdvancements(done, showToast);
		});
		return null;
	}
	
}
