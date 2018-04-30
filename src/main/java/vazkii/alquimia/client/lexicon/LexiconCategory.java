package vazkii.alquimia.client.lexicon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.client.base.ClientAdvancements;
import vazkii.alquimia.common.base.AlquimiaConfig;
import vazkii.alquimia.common.util.ItemStackUtil;

public class LexiconCategory implements Comparable<LexiconCategory> {

	String name, description, icon, parent, flag;
	int sortnum;
	
	transient boolean checkedParent = false;
	transient LexiconCategory parentCategory;
	transient List<LexiconCategory> children = new ArrayList<>();
	transient List<LexiconEntry> entries = new ArrayList<>();
	transient boolean locked;
	transient ItemStack iconItem = null;
	transient ResourceLocation resource;
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = ItemStackUtil.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public void addEntry(LexiconEntry entry) {
		this.entries.add(entry);
	}
	
	public void addChildCategory(LexiconCategory category) {
		children.add(category);
	}
	
	public List<LexiconEntry> getEntries() {
		return entries;
	}
	
	public LexiconCategory getParentCategory() {
		if(!checkedParent && !isRootCategory()) {
			parentCategory = LexiconRegistry.INSTANCE.categories.get(new ResourceLocation(parent));
			checkedParent = true;
		}
		
		return parentCategory;
	}
	
	public void updateLockStatus(boolean rootOnly) {
		if(rootOnly && !isRootCategory())
			return;

		children.forEach((c) ->  c.updateLockStatus(false));
		
		locked = true;
		for(LexiconCategory c : children)
			if(!c.isLocked()) {
				locked = false;
				return;
			}
		
		for(LexiconEntry e : entries) {
			if(!e.isLocked()) {
				locked = false;
				return;
			}
		}
	}
	
	public boolean isLocked() {
		return !AlquimiaConfig.disableAdvancementLocking && locked;
	}
	
	public boolean isUnread() {
		for(LexiconEntry e : entries)
			if(e.isUnread())
				return true;
		
		for(LexiconCategory c : children)
			if(c.isUnread())
				return true;
		
		return false;
	}
	
	public boolean isRootCategory() {
		return parent == null || parent.isEmpty();
	}
	
	public ResourceLocation getResource() {
		return resource;
	}
	
	public boolean canAdd() {
		return flag == null || flag.isEmpty() || AlquimiaConfig.getConfigFlag(flag);
	}
	
	@Override
	public int compareTo(LexiconCategory o) {
		if(o.locked != this.locked)
			return this.locked ? 1 : -1;
		
		return this.sortnum - o.sortnum;
	}
	
	public void build(ResourceLocation resource) {
		this.resource = resource;
		LexiconCategory parent = getParentCategory();
		if(parent != null)
			parent.addChildCategory(this);
	}
	
}
