package vazkii.alquimia.client.base;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.alquimia.client.handler.LexiconRightClickHandler;
import vazkii.alquimia.client.handler.MultiblockVisualizationHandler;
import vazkii.alquimia.client.handler.ReagentPouchEventHandler;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.render.entity.RenderRitual;
import vazkii.alquimia.client.render.tile.RenderTileAutomaton;
import vazkii.alquimia.client.render.tile.RenderTilePedestal;
import vazkii.alquimia.common.base.CommonProxy;
import vazkii.alquimia.common.block.tile.TileAutomaton;
import vazkii.alquimia.common.block.tile.TilePedestal;
import vazkii.alquimia.common.entity.EntityRitualLogic;
import vazkii.alquimia.common.handler.MultiblockTrackingHandler.MultiblockSettings;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		LexiconRegistry.INSTANCE.init();
		PersistentData.setup(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
		
		MinecraftForge.EVENT_BUS.register(ClientAdvancements.class);
		MinecraftForge.EVENT_BUS.register(MultiblockVisualizationHandler.class);
		MinecraftForge.EVENT_BUS.register(LexiconRightClickHandler.class);
		MinecraftForge.EVENT_BUS.register(ReagentPouchEventHandler.class);

		initRenderers();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		LexiconRegistry.INSTANCE.reloadLexiconRegistry();
	}
	
	private void initRenderers() {	
		ClientRegistry.bindTileEntitySpecialRenderer(TilePedestal.class, new RenderTilePedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAutomaton.class, new RenderTileAutomaton());

		RenderingRegistry.registerEntityRenderingHandler(EntityRitualLogic.class, RenderRitual::new);
	}
	
	@Override
	public void onConfigChanged(boolean firstChange) {
		if(!firstChange)
			LexiconRegistry.INSTANCE.reloadLexiconRegistry();
	}
	
	@Override
	public MultiblockSettings getVisualizingMultiblock(EntityPlayer player) {
		if(!player.world.isRemote)
			return super.getVisualizingMultiblock(player);
		
		if(player == Minecraft.getMinecraft().player && MultiblockVisualizationHandler.isAnchored())
			return new MultiblockSettings();
		
		return null;
	}
	
}
