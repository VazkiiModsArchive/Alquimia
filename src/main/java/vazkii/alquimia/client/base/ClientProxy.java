package vazkii.alquimia.client.base;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.common.base.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		LexiconRegistry.INSTANCE.init();
	}
	
}
