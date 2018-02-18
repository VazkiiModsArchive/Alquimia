package vazkii.alquimia.client.lexicon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.util.ItemStackUtils;

public class LexiconCategory implements Comparable<LexiconCategory> {

	String name, description, icon, parent;
	int sortnum;
	
	transient boolean checkedParent = false;
	transient LexiconCategory parentCategory;
	transient List<LexiconEntry> entries = new ArrayList<>();
	
	private transient ItemStack iconItem = null;
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = ItemStackUtils.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public void addEntry(LexiconEntry entry) {
		this.entries.add(entry);
	}
	
	public List<LexiconEntry> getEntries() {
		return entries;
	}
	
	public LexiconCategory getParentCategory() {
		if(!checkedParent && parent != null) {
			parentCategory = LexiconRegistry.INSTANCE.CATEGORIES.get(new ResourceLocation(parent));
			checkedParent = true;
		}
		
		return parentCategory;
	}
	
	@Override
	public int compareTo(LexiconCategory o) {
		return this.sortnum - o.sortnum;
	}
	
}
