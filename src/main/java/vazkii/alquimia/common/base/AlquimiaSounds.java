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
	public static SoundEvent storms;
	public static SoundEvent clear_skies_ritual;
	public static SoundEvent automaton;

	public static void preInit() {
		book_open = register("book_open");
		book_flip = register("book_flip");
		ash_infuse = register("ash_infuse");
		divining_rod_enable = register("divining_rod_enable");
		divining_rod_disable = register("divining_rod_disable");
		storms = register("storms");
		clear_skies_ritual = register("clear_skies_ritual");
		automaton = register("automaton");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation loc = new ResourceLocation(LibMisc.PREFIX_MOD + name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(loc);
		
		ProxyRegistry.register(e);
		
		return e;
	}

}
