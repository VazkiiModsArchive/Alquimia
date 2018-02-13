package vazkii.alquimia.client.lexicon;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.item.ModItems;

public class LexiconEntry implements Comparable<LexiconEntry> {

	String name, desc, icon;
	boolean priority = false;
	LexiconPage[] pages;
	
	private transient Item iconItem = null;
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}

	public LexiconPage[] getPages() {
		return pages;
	}
	
	public Item getIcon() {
		if(iconItem == null) {
			iconItem = Item.REGISTRY.getObject(new ResourceLocation(icon));
			if(iconItem == null)
				iconItem = ModItems.lexicon;
		}
		
		return iconItem;
	}
	
	@Override
	public int compareTo(LexiconEntry o) {
		if(o.priority != this.priority)
			return this.priority ? 1 : -1;
		
		return this.name.compareTo(o.name);
	}

}
