package vazkii.alquimia.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.alquimia.common.Alquimia;

public class ModEntities {

	public static void preInit() {
		int id = 0;

		registerModEntity(EntityRitualLogic.class, "ritual_logic", id++, Alquimia.instance, 256, 64, false);
	}
	
	private static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(new ResourceLocation(entityName), entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
		
	}
	
}
