package vazkii.alquimia.client.lexicon;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.item.ModItems;

public class LexiconCategory implements Comparable<LexiconCategory> {

	String name, desc, icon;
	int sortnum;
	List<LexiconEntry> entries;
	
	private transient Item iconItem = null;
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public Item getIcon() {
		if(iconItem == null) {
			iconItem = Item.REGISTRY.getObject(new ResourceLocation(icon));
			if(iconItem == null)
				iconItem = ModItems.lexicon;
		}
		
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
