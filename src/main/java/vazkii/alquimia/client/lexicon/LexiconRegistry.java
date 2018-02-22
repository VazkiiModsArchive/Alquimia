package vazkii.alquimia.client.lexicon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.client.base.ClientAdvancements;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.page.PageCrafting;
import vazkii.alquimia.client.lexicon.page.PageImage;
import vazkii.alquimia.client.lexicon.page.PageText;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.util.ItemStackUtil;
import vazkii.alquimia.common.util.ItemStackUtil.StackWrapper;

public class LexiconRegistry implements IResourceManagerReloadListener {

	private static final String DEFAULT_LANG = "en_US";
	private static final ResourceLocation FALLBACK_CATEGORY = new ResourceLocation(LibMisc.MOD_ID, String.format("docs/%s/categories/fallback.json", DEFAULT_LANG));
	private static final ResourceLocation FALLBACK_ENTRY = new ResourceLocation(LibMisc.MOD_ID, String.format("docs/%s/entries/fallback.json", DEFAULT_LANG));

	public final Map<ResourceLocation, LexiconCategory> categories = new HashMap();
	public final Map<ResourceLocation, LexiconEntry> entries = new HashMap();
	public final Map<String, Class<? extends LexiconPage>> pageTypes = new HashMap();
	
	public final List<ResourceLocation> categoryKeys = new LinkedList();
	public final List<ResourceLocation> entryKeys = new LinkedList();
	
	public final Map<StackWrapper, Pair<LexiconEntry, Integer>> recipeMappings = new HashMap();
	
	private Gson gson;
	private String currentLang;
	
	public static final LexiconRegistry INSTANCE = new LexiconRegistry();
	
	private LexiconRegistry() {
		gson = new GsonBuilder()
				.registerTypeHierarchyAdapter(LexiconPage.class, new LexiconPage.LexiconPageAdapter())
				.create();
	}
	
	public void init() {
		addPageTypes();
		addCategories();
		addEntries();
		
		IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		if(manager instanceof IReloadableResourceManager)
			((IReloadableResourceManager) manager).registerReloadListener(this);
		else throw new RuntimeException("Minecraft's resource manager is not reloadable. Something went way wrong.");
	}
	
	private void addPageTypes() {
		pageTypes.put("text", PageText.class);
		pageTypes.put("crafting", PageCrafting.class);
		pageTypes.put("image", PageImage.class);
	}
	
	private void addCategories() {
		registerCategory("intro");
		registerCategory("test1");
		registerCategory("test2");
		registerCategory("test3");
		registerCategory("test4");
		registerCategory("test5");
	}
	
	private void addEntries() {
		registerEntry("intro", "test1");
		registerEntry("intro", "test2");
		registerEntry("intro", "test3");
		registerEntry("intro", "test_prio");
		
		registerEntry("test1", "test_recipes");
		registerEntry("test1", "test_paper");
		
		registerEntry("test2", "test_clock");
	}
	
	private ResourceLocation registerCategory(String category) {
		ResourceLocation res = new ResourceLocation(LibMisc.MOD_ID, category); 
		return registerCategory(res);
	}
	
	private ResourceLocation registerEntry(String category, String entry) {
		ResourceLocation res = new ResourceLocation(LibMisc.MOD_ID, category + "/" + entry);
		return registerEntry(res);
	}
	
	public ResourceLocation registerCategory(ResourceLocation res) {
		categoryKeys.add(res);
		return res;
	}
	
	public ResourceLocation registerEntry(ResourceLocation res) {
		entryKeys.add(res);
		return res;
	}
	
	public Pair<LexiconEntry, Integer> getEntryForStack(ItemStack stack) {
		return recipeMappings.get(ItemStackUtil.wrapStack(stack));
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		GuiLexicon.onReload();
		categories.clear();
		entries.clear();
		recipeMappings.clear();
		
		currentLang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
		
		categoryKeys.forEach(res -> loadCategory(res, new ResourceLocation(res.getResourceDomain(), String.format("docs/%s/categories/%s.json", DEFAULT_LANG, res.getResourcePath()))));
		entryKeys.forEach(res -> loadEntry(res, new ResourceLocation(res.getResourceDomain(), String.format("docs/%s/entries/%s.json", DEFAULT_LANG, res.getResourcePath()))));
		
		entries.forEach((res, entry) -> entry.build(res));
		categories.forEach((res, category) -> category.build(res));
		ClientAdvancements.updateLockStatus();
	}
	
	private void loadCategory(ResourceLocation key, ResourceLocation res) {
		InputStream stream = loadLocalizedJson(res, FALLBACK_CATEGORY);
		if(stream == null)
			throw new IllegalArgumentException(res + " does not exist.");
		
		LexiconCategory category = gson.fromJson(new InputStreamReader(stream), LexiconCategory.class);
		if(category == null)
			throw new IllegalArgumentException(res + " does not exist.");
		
		categories.put(key, category);
	}
	
	private void loadEntry(ResourceLocation key, ResourceLocation res) {
		InputStream stream = loadLocalizedJson(res, FALLBACK_ENTRY);
		if(stream == null)
			throw new IllegalArgumentException(res + " does not exist.");
		
		LexiconEntry entry = gson.fromJson(new InputStreamReader(stream), LexiconEntry.class);
		if(entry == null)
			throw new IllegalArgumentException(res + " does not exist.");
		
		LexiconCategory category = entry.getCategory();
		if(category != null)
			category.addEntry(entry);
		else new RuntimeException("Entry " + key + " does not have a valid category.").printStackTrace();
		
		entries.put(key, entry);	
	}
	
	private InputStream loadLocalizedJson(ResourceLocation res, ResourceLocation fallback) {
		ResourceLocation localized = new ResourceLocation(res.getResourceDomain(), res.getResourcePath().replaceAll(DEFAULT_LANG, currentLang));
		
		InputStream stream = loadJson(localized, null);
		return stream == null ? loadJson(res, fallback) : stream;
	}
	
	private InputStream loadJson(ResourceLocation resloc, ResourceLocation fallback) {
		IResource res = null;
		try {
			res = Minecraft.getMinecraft().getResourceManager().getResource(resloc);
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		if(res == null && fallback != null) {
			new RuntimeException("Alquimia failed to load " + resloc + ". Switching to fallback.").printStackTrace();
			return loadJson(fallback, null);
		}
		
		return res.getInputStream();
	}
	
}
