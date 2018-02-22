package vazkii.alquimia.common.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableSet;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.network.MessageSyncAdvancements;
import vazkii.arl.network.NetworkHandler;

public final class AdvancementSyncHandler {

	// TODO allow this to be hooked
	public static final ImmutableSet<String> TRACKED_NAMESPACES = ImmutableSet.of(LibMisc.MOD_ID);
	
	public static List<ResourceLocation> syncedAdvancements = null;

	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent event) {
		if(event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
			buildSyncSet(player);
			
			if(syncedAdvancements.contains(event.getAdvancement().getId()))
				syncPlayer(player);
		}
	}
	
	@SubscribeEvent
	public static void onLogin(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			buildSyncSet(player);
			syncPlayer(player);
		}
	}
	
	private static void buildSyncSet(EntityPlayerMP player) {
		if(syncedAdvancements == null) {
			AdvancementManager manager = player.getServer().getAdvancementManager();
			Iterable<Advancement> allAdvancements = manager.getAdvancements();
			
			syncedAdvancements = new ArrayList();
			for(Advancement a : allAdvancements)
				if(TRACKED_NAMESPACES.contains(a.getId().getResourceDomain()))
					syncedAdvancements.add(a.getId());
		}
	}
	
	public static void syncPlayer(EntityPlayerMP player) {
		PlayerAdvancements advancements = player.getAdvancements();
		AdvancementManager manager = player.getServer().getAdvancementManager();
		
		List<String> completed = new LinkedList();
		for(ResourceLocation res : syncedAdvancements) {
			Advancement adv = manager.getAdvancement(res);
			AdvancementProgress p = advancements.getProgress(adv);
			if(p.isDone())
				completed.add(res.toString());
		}
		
		String[] completedArr = completed.toArray(new String[completed.size()]);
		NetworkHandler.INSTANCE.sendTo(new MessageSyncAdvancements(completedArr), player);
	}
	
	
}
