package vazkii.alquimia.common.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.crafting.CrucibleRecipes;
import vazkii.alquimia.common.handler.AdvancementSyncHandler;
import vazkii.alquimia.common.handler.CrucibleHandler;
import vazkii.alquimia.common.handler.ItemTickHandler;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.network.GuiHandler;
import vazkii.alquimia.common.network.ModPackets;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		AlquimiaConfig.loadConfig(event.getSuggestedConfigurationFile());

		ModBlocks.preInit();
		ModItems.preInit();
		ModMultiblocks.preInit();
		
		CrucibleRecipes.init();
		
		AlquimiaSounds.preInit();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Alquimia.instance, new GuiHandler());
		
		MinecraftForge.EVENT_BUS.register(AdvancementSyncHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemTickHandler.class);
		MinecraftForge.EVENT_BUS.register(CrucibleHandler.class);
	}
	
	public void init(FMLInitializationEvent event) {
		ModPackets.registerPackets();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	public void onConfigChanged(boolean firstChange) {
		// NO-OP
	}
	
}
