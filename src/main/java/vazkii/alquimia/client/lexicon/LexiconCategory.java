package vazkii.alquimia.client.lexicon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class LexiconCategory implements Comparable<LexiconCategory> {

	String name, description, icon;
	int sortnum;
	List<LexiconEntry> entries = new ArrayList<>();
	
	private transient ItemStack iconItem = null;
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = LexiconUtils.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public void addEntry(LexiconEntry entry) {
		this.entries.add(entry);
	}
	
	public List<LexiconEntry> getEntries() {
		return entries;
	}
	
	@Override
	public int compareTo(LexiconCategory o) {
		return this.sortnum - o.sortnum;
	}
	
}
