package vazkii.alquimia.client.lexicon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.client.base.ClientAdvancements;
import vazkii.alquimia.client.base.PersistentData;
import vazkii.alquimia.common.base.AlquimiaConfig;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.util.ItemStackUtil;
import vazkii.alquimia.common.util.ItemStackUtil.StackWrapper;

public class LexiconEntry implements Comparable<LexiconEntry> {

	String name, icon, category, flag;
	boolean priority = false;
	boolean secret = false;
	LexiconPage[] pages;
	String advancement;
	
	transient ResourceLocation resource;
	transient LexiconCategory lcategory = null;
	transient ItemStack iconItem = null;
	transient List<LexiconPage> realPages = new ArrayList();
	transient List<StackWrapper> relevantStacks = new LinkedList();
	transient boolean locked;
	
	public String getName() {
		return name;
	}

	public List<LexiconPage> getPages() {
		return realPages;
	}
	
	public boolean isPriority() {
		return priority;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = ItemStackUtil.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public LexiconCategory getCategory() {
		if(lcategory == null) {
			if(category.contains(":"))
				lcategory = LexiconRegistry.INSTANCE.categories.get(new ResourceLocation(category));
			else lcategory = LexiconRegistry.INSTANCE.categories.get(new ResourceLocation(LibMisc.MOD_ID, category));
		}
		
		return lcategory;
	}
	
	public void updateLockStatus() {
		locked = advancement != null && !advancement.isEmpty() && !ClientAdvancements.hasDone(advancement);
	}
	
	public boolean isLocked() {
		if(isSecret())
			return locked;
		return !AlquimiaConfig.disableAdvancementLocking && locked;
	}
	
	public boolean isUnread() {
		return !isLocked() && !PersistentData.data.viewedEntries.contains(getResource().toString());
	}
	
	public boolean isSecret() {
		return secret;
	}
	
	public boolean shouldHide() {
		return isSecret() && isLocked();
	}
	
	public ResourceLocation getResource() {
		return resource;
	}
	
	public boolean canAdd() {
		return (flag == null || flag.isEmpty() || AlquimiaConfig.getConfigFlag(flag)) && getCategory() != null;
	}
	
	@Override
	public int compareTo(LexiconEntry o) {
		if(o.locked != this.locked)
			return this.locked ? 1 : -1;

		if(o.priority != this.priority)
			return this.priority ? -1 : 1;
		
		return this.name.compareTo(o.name);
	}
	
	public void build(ResourceLocation resource) {
		this.resource = resource;
		for(int i = 0; i < pages.length; i++)
			if(pages[i].canAdd()) {
				realPages.add(pages[i]);
				pages[i].build(this, i);
			}
	}
	
	public void addRelevantStack(ItemStack stack, int page) {
		StackWrapper wrapper = ItemStackUtil.wrapStack(stack);
		relevantStacks.add(wrapper);
		
		if(!LexiconRegistry.INSTANCE.recipeMappings.containsKey(wrapper))
			LexiconRegistry.INSTANCE.recipeMappings.put(wrapper, Pair.of(this, page / 2));
	}
	
	public boolean isStackRelevant(ItemStack stack) {
		return relevantStacks.contains(ItemStackUtil.wrapStack(stack));
	}
	
}
