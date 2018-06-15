package vazkii.alquimia.common.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.block.ModBlocks;
import vazkii.alquimia.common.crafting.CrucibleRecipes;
import vazkii.alquimia.common.crafting.ModCraftingRecipes;
import vazkii.alquimia.common.entity.ModEntities;
import vazkii.alquimia.common.handler.AdvancementSyncHandler;
import vazkii.alquimia.common.handler.CrucibleHandler;
import vazkii.alquimia.common.handler.MultiblockTrackingHandler;
import vazkii.alquimia.common.handler.MultiblockTrackingHandler.MultiblockSettings;
import vazkii.alquimia.common.handler.RitualHandler;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.multiblock.ModMultiblocks;
import vazkii.alquimia.common.network.GuiHandler;
import vazkii.alquimia.common.network.ModPackets;
import vazkii.alquimia.common.ritual.ModRituals;
import vazkii.arl.util.ItemTickHandler;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		AlquimiaConfig.loadConfig(event.getSuggestedConfigurationFile());

		ModBlocks.preInit();
		ModItems.preInit();
		ModEntities.preInit();
		ModMultiblocks.preInit();
		ModRituals.preInit();

		ModCraftingRecipes.init();
		CrucibleRecipes.init();
		
		AlquimiaSounds.preInit();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Alquimia.instance, new GuiHandler());
		
		MinecraftForge.EVENT_BUS.register(AdvancementSyncHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemTickHandler.class);
		MinecraftForge.EVENT_BUS.register(CrucibleHandler.class);
		MinecraftForge.EVENT_BUS.register(RitualHandler.class);
	}
	
	public void init(FMLInitializationEvent event) {
		ModPackets.registerPackets();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		ModItems.postInit();
	}
	
	public void onConfigChanged(boolean firstChange) {
		// NO-OP
	}
	
	public MultiblockSettings getVisualizingMultiblock(EntityPlayer player) {
		MultiblockSettings mbs = MultiblockTrackingHandler.get(player);
		return mbs;
	}
	
}
