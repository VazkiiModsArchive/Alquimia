package vazkii.alquimia.common.ritual;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.ritual.small.RitualPurification;

public class ModRituals {

	public static Map<ResourceLocation, Ritual> rituals = new HashMap();
	public static Multimap<RitualType, Ritual> ritualsPerType = HashMultimap.create();
	
	public static Ritual purification;
	
	public static void preInit() {
		purification = registerRitual(new RitualPurification());
	}
	
	public static Ritual registerRitual(Ritual ritual) {
		rituals.put(ritual.name, ritual);
		ritualsPerType.put(ritual.type, ritual);
		return ritual;
	}
	
}
