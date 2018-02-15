package vazkii.alquimia.client.lexicon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.page.PageCrafting;
import vazkii.alquimia.client.lexicon.page.PageImage;
import vazkii.alquimia.client.lexicon.page.PageText;
import vazkii.alquimia.common.lib.LibMisc;

public class LexiconRegistry implements IResourceManagerReloadListener {

	private static final String DEFAULT_LANG = "en_US";
	private static final ResourceLocation FALLBACK_CATEGORY = new ResourceLocation(LibMisc.MOD_ID, String.format("docs/%s/categories/fallback.json", DEFAULT_LANG));
	private static final ResourceLocation FALLBACK_ENTRY = new ResourceLocation(LibMisc.MOD_ID, String.format("docs/%s/entries/fallback.json", DEFAULT_LANG));

	public final Map<ResourceLocation, LexiconCategory> CATEGORIES = new HashMap();
	public final Map<ResourceLocation, LexiconEntry> ENTRIES = new HashMap();
	public final Map<String, Class<? extends LexiconPage>> PAGE_TYPES = new HashMap();
	
	public final List<ResourceLocation> CATEGORY_KEYS = new LinkedList();
	public final List<ResourceLocation> ENTRY_KEYS = new LinkedList();
	
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
		PAGE_TYPES.put("text", PageText.class);
		PAGE_TYPES.put("crafting", PageCrafting.class);
		PAGE_TYPES.put("image", PageImage.class);
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
	}
	
	private void registerCategory(String category) {
		registerCategory(new ResourceLocation(LibMisc.MOD_ID, category));
	}
	
	private void registerEntry(String category, String entry) {
		registerEntry(new ResourceLocation(LibMisc.MOD_ID, category + "/" + entry));
	}
	
	public void registerCategory(ResourceLocation res) {
		CATEGORY_KEYS.add(res);
	}
	
	public void registerEntry(ResourceLocation res) {
		ENTRY_KEYS.add(res);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		GuiLexicon.onReload();
		CATEGORIES.clear();
		ENTRIES.clear();
		
		currentLang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
		
		CATEGORY_KEYS.forEach(res -> loadCategory(res, new ResourceLocation(res.getResourceDomain(), String.format("docs/%s/categories/%s.json", DEFAULT_LANG, res.getResourcePath()))));
		ENTRY_KEYS.forEach(res -> loadEntry(res, new ResourceLocation(res.getResourceDomain(), String.format("docs/%s/entries/%s.json", DEFAULT_LANG, res.getResourcePath()))));
	}
	
	private void loadCategory(ResourceLocation key, ResourceLocation res) {
		InputStream stream = loadLocalizedJson(res, FALLBACK_CATEGORY);
		if(stream == null)
			throw new IllegalArgumentException(res + " does not exist.");
		
		LexiconCategory category = gson.fromJson(new InputStreamReader(stream), LexiconCategory.class);
		if(category == null)
			throw new IllegalArgumentException(res + " does not exist.");
		
		CATEGORIES.put(key, category);
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
		
		ENTRIES.put(key, entry);	
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
