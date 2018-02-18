package vazkii.alquimia.client.lexicon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.item.ModItems;
import vazkii.alquimia.common.lib.LibMisc;

public class LexiconEntry implements Comparable<LexiconEntry> {

	String name, icon, category;
	boolean priority = false;
	LexiconPage[] pages;
	String[] relations;
	
	transient LexiconCategory lcategory = null;
	transient ItemStack iconItem = null;
	
	public String getName() {
		return name;
	}

	public LexiconPage[] getPages() {
		return pages;
	}
	
	public String[] getRelations() {
		return relations;
	}
	
	public boolean isPriority() {
		return priority;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = LexiconUtils.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public LexiconCategory getCategory() {
		if(lcategory == null) {
			if(category.contains(":"))
				lcategory = LexiconRegistry.INSTANCE.CATEGORIES.get(new ResourceLocation(category));
			else lcategory = LexiconRegistry.INSTANCE.CATEGORIES.get(new ResourceLocation(LibMisc.MOD_ID, category));
		}
		
		return lcategory;
	}
	
	@Override
	public int compareTo(LexiconEntry o) {
		if(o.priority != this.priority)
			return this.priority ? -1 : 1;
		
		return this.name.compareTo(o.name);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LexiconEntry[");
		builder.append(name);
		builder.append(" / Pages:");
		builder.append(pages.length);
		for(LexiconPage page : pages) {
			builder.append(" ");
			if(page == null)
				builder.append("NULL");
			else builder.append(page.type);
		}
		builder.append("]");
		
		return builder.toString();
	}

}
