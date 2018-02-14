package vazkii.alquimia.client.lexicon;

import java.util.List;

import net.minecraft.item.ItemStack;

public class LexiconCategory implements Comparable<LexiconCategory> {

	String name, desc, icon;
	int sortnum;
	List<LexiconEntry> entries;
	
	private transient ItemStack iconItem = null;
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = LexiconUtils.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public void addEntry(LexiconEntry entry) {
		this.entries.add(entry);
	}
	
	@Override
	public int compareTo(LexiconCategory o) {
		return this.sortnum - o.sortnum;
	}
	
}
