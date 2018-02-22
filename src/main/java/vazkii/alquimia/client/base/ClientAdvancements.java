package vazkii.alquimia.client.base;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import scala.actors.threadpool.Arrays;
import vazkii.alquimia.client.lexicon.LexiconRegistry;

public class ClientAdvancements {

	static List<String> doneAdvancements;
	
	public static void setDoneAdvancements(String[] done) {
		doneAdvancements = Arrays.asList(done);
		updateLockStatus();
	}
	
	public static void updateLockStatus() {
		if(doneAdvancements != null) {
			LexiconRegistry.INSTANCE.entries.values().forEach((e) -> e.updateLockStatus());
			LexiconRegistry.INSTANCE.categories.values().forEach((c) -> c.updateLockStatus(true));
		}
	}
	
	public static void resetIfNeeded() {
		if(doneAdvancements != null && doneAdvancements.size() > 0)
			setDoneAdvancements(new String[0]);
	}
	
	public static boolean hasDone(String advancement) {
		return doneAdvancements != null && doneAdvancements.contains(advancement);
	}
	
	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if(event.phase == Phase.END && Minecraft.getMinecraft().player == null)
			resetIfNeeded();
	}
	
}
