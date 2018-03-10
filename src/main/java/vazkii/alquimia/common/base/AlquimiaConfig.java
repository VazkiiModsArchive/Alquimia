package vazkii.alquimia.common.base;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.management.ReflectionException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.alquimia.common.Alquimia;
import vazkii.alquimia.common.lib.LibMisc;

public final class AlquimiaConfig {

	public static Configuration config;

	private static Map<String, Boolean> configFlags = new HashMap();
	
	@Setting(name="Disable Advancement Locking", 
			desc="Set this to true to allow all entries and categories to be visible from the get-go rather than locked by advancements.")
	public static boolean disableAdvancementLocking = false;
	
	@Setting(name="Test Flag 1", flag="Test1")
	public static boolean testFlag1 = true;
	
	@Setting(name="Test Flag 2", flag="Test2")
	public static boolean testFlag2 = true;
	
	private static boolean firstChange;

	public static void loadConfig(File file) {
		config = new Configuration(file);
		firstChange = true;
		
		config.load();
		loadConfig();

		MinecraftForge.EVENT_BUS.register(ChangeListener.class);
	}
	
	public static boolean getConfigFlag(String name) {
		boolean target = true;
		if(name.startsWith("!")) {
			name = name.substring(1);
			target = false;
		}
		
		boolean status = (configFlags.containsKey(name) ? configFlags.get(name) : false) == target;
		return status;
	}

	public static void loadConfig() {
		Field[] fields = AlquimiaConfig.class.getFields();
		for(Field f : fields)
			try {
				Setting s = f.getAnnotation(Setting.class);
				loadProp(f, s);
			} catch (IllegalArgumentException | ReflectiveOperationException e) {
				e.printStackTrace();
			}
		
		boolean changed = config.hasChanged();
		if(changed || firstChange) {
			Alquimia.proxy.onConfigChanged(firstChange);
			if(firstChange)
				firstChange = false;
		}
				
		if(changed)
			config.save();
	}

	public static void loadProp(Field f, Setting s) throws IllegalArgumentException, ReflectiveOperationException {
		if(s != null) {
			String name = s.name();
			String desc = s.desc();
			String flag = s.flag();
			Object def = f.get(null);

			switch(f.getType().getName()) {
			case "int":
				f.setInt(null, loadPropInt(name, desc, (Integer) def)); 
				break;
			case "double":
				f.setDouble(null, loadPropDouble(name, desc, (Double) def));
				break;
			case "boolean":
				boolean b = loadPropBoolean(name, desc, (Boolean) def);
				f.setBoolean(null, b);
				if(!flag.isEmpty())
					configFlags.put(flag, b);
				break;
			case "java.lang.String":
				f.set(null, loadPropString(name, desc, (String) def));
				break;
			}
		}
	}

	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getDouble(default_);
	}

	public static boolean loadPropBoolean(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getBoolean(default_);
	}
	
	public static String loadPropString(String propName, String desc, String default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.setComment(desc);

		return prop.getString();
	}

	public static class ChangeListener {

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if(eventArgs.getModID().equals(LibMisc.MOD_ID))
				loadConfig();
		}

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private static @interface Setting {
		String name();
		String desc() default "";
		String flag() default "";
	}

}
