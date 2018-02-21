package vazkii.alquimia.client.base;

import java.io.File;

import vazkii.alquimia.common.util.SerializationUtil;

public final class PersistentData {

	private static File saveFile;
	
	public static DataHolder data;
	
	public static void setup(File dir) {
		saveFile = new File(dir, "AlquimiaData.json");
		load();
	}
	
	public static void load() {
		data = SerializationUtil.loadFromFile(saveFile, DataHolder.class, DataHolder::new);
	}
	
	public static void save() {
		SerializationUtil.saveToFile(saveFile, DataHolder.class, data);
	}
	
	public static final class DataHolder {
		
		public int lexiconGuiScale = 0;
		
	}
	
}
