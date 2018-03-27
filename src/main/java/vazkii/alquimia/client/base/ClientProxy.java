package vazkii.alquimia.client.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.alquimia.client.handler.LexiconRightClickHandler;
import vazkii.alquimia.client.handler.MultiblockVisualizationHandler;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.render.tile.RenderTilePedestal;
import vazkii.alquimia.common.base.CommonProxy;
import vazkii.alquimia.common.block.tile.TilePedestal;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		LexiconRegistry.INSTANCE.init();
		PersistentData.setup(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
		
		MinecraftForge.EVENT_BUS.register(ClientAdvancements.class);
		MinecraftForge.EVENT_BUS.register(MultiblockVisualizationHandler.class);
		MinecraftForge.EVENT_BUS.register(LexiconRightClickHandler.class);
		
		initRenderers();
	}
	
	private void initRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TilePedestal.class, new RenderTilePedestal());
	}
	
	@Override
	public void onConfigChanged(boolean firstChange) {
		if(!firstChange)
			LexiconRegistry.INSTANCE.reloadLexiconRegistry();
	}
	
}
