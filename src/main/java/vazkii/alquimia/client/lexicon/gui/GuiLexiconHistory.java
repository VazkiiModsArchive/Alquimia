package vazkii.alquimia.client.lexicon.gui;

import java.util.Collection;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.base.PersistentData;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.common.lib.LibMisc;

public class GuiLexiconHistory extends GuiLexiconEntryList {

	@Override
	protected String getName() {
		return I18n.translateToLocal("alquimia.gui.lexicon.history");
	}

	@Override
	protected String getDescriptionText() {
		return I18n.translateToLocal("alquimia.gui.lexicon.history.info");
	}
	
	@Override
	protected boolean shouldDrawProgressBar() {
		return false;
	}
	
	@Override
	protected boolean shouldSortEntryList() {
		return false;
	}

	@Override
	protected Collection<LexiconEntry> getEntries() {
		return PersistentData.data.history.stream()
				.map((s) -> new ResourceLocation(s))
				.map((res) -> LexiconRegistry.INSTANCE.entries.get(res))
				.filter((e) -> e != null)
				.collect(Collectors.toList());
	}

}
