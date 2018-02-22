package vazkii.alquimia.client.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
		SerializationUtil.saveToFile(SerializationUtil.PRETTY_GSON, saveFile, DataHolder.class, data);
	}
	
	public static final class DataHolder {
		
		public int lexiconGuiScale = 0;
		public List<String> viewedEntries = new ArrayList();
		
	}
	
}
