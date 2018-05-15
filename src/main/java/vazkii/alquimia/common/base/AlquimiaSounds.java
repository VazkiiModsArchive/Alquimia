package vazkii.alquimia.common.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.util.ProxyRegistry;

public class AlquimiaSounds {
	
	public static SoundEvent book_open;
	public static SoundEvent book_flip;
	public static SoundEvent ash_infuse;
	public static SoundEvent divining_rod_enable;
	public static SoundEvent divining_rod_disable;
	public static SoundEvent ritual_storms;
	public static SoundEvent ritual_clear_skies;
	public static SoundEvent heartbeat;
	public static SoundEvent automaton;
	public static SoundEvent place_ash;

	public static void preInit() {
		book_open = register("book_open");
		book_flip = register("book_flip");
		ash_infuse = register("ash_infuse");
		divining_rod_enable = register("divining_rod_enable");
		divining_rod_disable = register("divining_rod_disable");
		ritual_storms = register("ritual_storms");
		ritual_clear_skies = register("ritual_clear_skies");
		heartbeat = register("heartbeat");
		automaton = register("automaton");
		place_ash = register("place_ash");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation loc = new ResourceLocation(LibMisc.PREFIX_MOD + name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(loc);
		
		ProxyRegistry.register(e);
		
		return e;
	}

}
