package vazkii.alquimia.client.lexicon;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.io.FilenameUtils;
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
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import vazkii.alquimia.client.base.ClientAdvancements;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.page.PageCrafting;
import vazkii.alquimia.client.lexicon.page.PageCrucible;
import vazkii.alquimia.client.lexicon.page.PageEmpty;
import vazkii.alquimia.client.lexicon.page.PageImage;
import vazkii.alquimia.client.lexicon.page.PageLink;
import vazkii.alquimia.client.lexicon.page.PageMultiblock;
import vazkii.alquimia.client.lexicon.page.PageRelations;
import vazkii.alquimia.client.lexicon.page.PageRitual;
import vazkii.alquimia.client.lexicon.page.PageSpotlight;
import vazkii.alquimia.client.lexicon.page.PageText;
import vazkii.alquimia.common.handler.AdvancementSyncHandler;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.util.ItemStackUtil;
import vazkii.alquimia.common.util.ItemStackUtil.StackWrapper;

public class LexiconRegistry implements IResourceManagerReloadListener {

	private static final String DEFAULT_LANG = "en_us";

	public final List<String> modsWithDocs = new ArrayList();

	public final Map<ResourceLocation, LexiconCategory> categories = new HashMap();
	public final Map<ResourceLocation, LexiconEntry> entries = new HashMap();
	public final Map<String, Class<? extends LexiconPage>> pageTypes = new HashMap();
	public final Map<StackWrapper, Pair<LexiconEntry, Integer>> recipeMappings = new HashMap();
	private boolean errored = false;
	private boolean firstLoad = true;
	
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
		registerMod(LibMisc.MOD_ID);

		IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		if(manager instanceof IReloadableResourceManager)
			((IReloadableResourceManager) manager).registerReloadListener(this);
		else throw new RuntimeException("Minecraft's resource manager is not reloadable. Something went way wrong.");
	}
	
	public boolean isErrored() {
		return errored;
	}
	
	public void registerMod(String id) {
		modsWithDocs.add(id);
	}

	private void addPageTypes() {
		pageTypes.put("text", PageText.class);
		pageTypes.put("crafting", PageCrafting.class);
		pageTypes.put("image", PageImage.class);
		pageTypes.put("spotlight", PageSpotlight.class);
		pageTypes.put("empty", PageEmpty.class);
		pageTypes.put("multiblock", PageMultiblock.class);
		pageTypes.put("link", PageLink.class);
		pageTypes.put("relations", PageRelations.class);
		pageTypes.put("crucible", PageCrucible.class);
		pageTypes.put("ritual", PageRitual.class);
	}
	
	public Pair<LexiconEntry, Integer> getEntryForStack(ItemStack stack) {
		return recipeMappings.get(ItemStackUtil.wrapStack(stack));
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		if(!firstLoad)
			reloadLexiconRegistry();
	}

	public void reloadLexiconRegistry() {
		firstLoad = false;
		errored = false;
		GuiLexicon.onReload();
		AdvancementSyncHandler.trackedNamespaces.clear();
		categories.clear();
		entries.clear();
		recipeMappings.clear();

		currentLang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();

		List<ModContainer> foundMods = new ArrayList();
		List<ResourceLocation> foundCategories = new ArrayList();
		List<ResourceLocation> foundEntries = new ArrayList();
		List<ModContainer> mods = Loader.instance().getActiveModList();
		
		mods.forEach((mod) -> {
			String id = mod.getModId();
			CraftingHelper.findFiles(mod, String.format("assets/%s/alq_docs", id), (path) -> {
				if(Files.exists(path)) {
					AdvancementSyncHandler.trackedNamespaces.add(id);
					foundMods.add(mod);
					return true;
				}
				
				return false;
			}, null);
		});
		
		try {
			foundMods.forEach((mod) -> {
				String id = mod.getModId();
				CraftingHelper.findFiles(mod, String.format("assets/%s/alq_docs/%s/categories", id, DEFAULT_LANG), null, pred(id, foundCategories));
				CraftingHelper.findFiles(mod, String.format("assets/%s/alq_docs/%s/entries", id, DEFAULT_LANG), null, pred(id, foundEntries));
			});
			
			foundCategories.forEach(c -> loadCategory(c, new ResourceLocation(c.getResourceDomain(), String.format("alq_docs/%s/categories/%s.json", DEFAULT_LANG, c.getResourcePath()))));
			foundEntries.forEach(e -> loadEntry(e, new ResourceLocation(e.getResourceDomain(), String.format("alq_docs/%s/entries/%s.json", DEFAULT_LANG, e.getResourcePath()))));

			entries.forEach((res, entry) -> entry.build(res));
			categories.forEach((res, category) -> category.build(res));
		} catch(Exception e) {
			errored = true;
			e.printStackTrace();
		}

		ClientAdvancements.updateLockStatus();
	}
	
	private BiFunction<Path, Path, Boolean> pred(String modId, List<ResourceLocation> list) {
		return (root, file) -> {
			Path rel = root.relativize(file);
			String relName = rel.toString();
			if(relName.toString().endsWith(".json")) {
				relName = FilenameUtils.removeExtension(FilenameUtils.separatorsToUnix(relName));
				ResourceLocation res = new ResourceLocation(modId, relName); 
				list.add(res);
			}

			return true;
		};
	}

	private void loadCategory(ResourceLocation key, ResourceLocation res) {
		InputStream stream = loadLocalizedJson(res);
		if(stream == null)
			throw new IllegalArgumentException(res + " does not exist.");

		LexiconCategory category = gson.fromJson(new InputStreamReader(stream), LexiconCategory.class);
		if(category == null)	
			throw new IllegalArgumentException(res + " does not exist.");

		if(category.canAdd())
			categories.put(key, category);
	}

	private void loadEntry(ResourceLocation key, ResourceLocation res) {
		InputStream stream = loadLocalizedJson(res);
		if(stream == null)
			throw new IllegalArgumentException(res + " does not exist.");

		LexiconEntry entry = gson.fromJson(new InputStreamReader(stream), LexiconEntry.class);
		if(entry == null)
			throw new IllegalArgumentException(res + " does not exist.");

		if(entry.canAdd()) {
			LexiconCategory category = entry.getCategory();
			if(category != null)
				category.addEntry(entry);
			else new RuntimeException("Entry " + key + " does not have a valid category.").printStackTrace();

			entries.put(key, entry);
		}
	}

	private InputStream loadLocalizedJson(ResourceLocation res) {
		ResourceLocation localized = new ResourceLocation(res.getResourceDomain(), res.getResourcePath().replaceAll(DEFAULT_LANG, currentLang));

		return loadJson(localized, null);
	}

	private InputStream loadJson(ResourceLocation resloc, ResourceLocation fallback) {
		IResource res = null;
		try {
			res = Minecraft.getMinecraft().getResourceManager().getResource(resloc);
		} catch (FileNotFoundException e) {
			return null;
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
