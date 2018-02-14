package vazkii.alquimia.client.lexicon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.item.ModItems;

public class LexiconEntry implements Comparable<LexiconEntry> {

	String name, desc, icon;
	boolean priority = false;
	LexiconPage[] pages;
	
	private transient ItemStack iconItem = null;
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}

	public LexiconPage[] getPages() {
		return pages;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = LexiconUtils.loadStackFromString(icon);
		
		return iconItem;
	}
	
	@Override
	public int compareTo(LexiconEntry o) {
		if(o.priority != this.priority)
			return this.priority ? 1 : -1;
		
		return this.name.compareTo(o.name);
	}

}
