package vazkii.alquimia.common.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.util.ProxyRegistry;

public class AlquimiaSounds {
	
	public static SoundEvent book_open;
	public static SoundEvent book_flip;

	public static void preInit() {
		book_open = register("book_open");
		book_flip = register("book_flip");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation loc = new ResourceLocation(LibMisc.PREFIX_MOD + name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(loc);
		
		ProxyRegistry.register(e);
		
		return e;
	}

}
