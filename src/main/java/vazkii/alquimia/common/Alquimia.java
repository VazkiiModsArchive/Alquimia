package vazkii.alquimia.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.alquimia.common.lib.LibMisc;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class Alquimia {

	@Instance(LibMisc.MOD_ID)
	public static Alquimia instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println("Loaded!");
	}

}
