package vazkii.alquimia.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.NetworkMessage;

public final class ModPackets {

	public static void registerPackets() {
		NetworkHandler.register(MessageSyncAdvancements.class, Side.CLIENT);
		NetworkHandler.register(MessageAddToReagentHolder.class, Side.SERVER);
		NetworkHandler.register(MessageTrackMultiblock.class, Side.SERVER);

		NetworkMessage.mapHandler(String[].class, ModPackets::readStringArray, ModPackets::writeStringArray);
	}
	
	public static String[] readStringArray(ByteBuf buf) {
		int len = buf.readInt();
		String[] strs = new String[len];
		for(int i = 0; i < len; i++)
			strs[i] = ByteBufUtils.readUTF8String(buf);
		
		return strs;
	}
	
	public static void writeStringArray(String[] arr, ByteBuf buf) {
		buf.writeInt(arr.length);
		for(int i = 0; i < arr.length; i++)
			ByteBufUtils.writeUTF8String(buf, arr[i]);
	}
	
}
