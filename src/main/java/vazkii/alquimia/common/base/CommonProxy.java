package vazkii.alquimia.common.base;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.network.GuiHandler;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModItems.preInit();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Alquimia.instance, new GuiHandler());
	}
	
	public void init(FMLInitializationEvent event) {
	
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
