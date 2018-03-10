package vazkii.alquimia.client.lexicon.gui;

import java.util.Collection;

import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;

public class GuiLexiconIndex extends GuiLexiconEntryList {

	@Override
	protected String getName() {
		return I18n.translateToLocal("alquimia.gui.lexicon.index");
	}

	@Override
	protected String getDescriptionText() {
		return I18n.translateToLocal("alquimia.gui.lexicon.index.info");
	}

	@Override
	protected Collection<LexiconEntry> getEntries() {
		return LexiconRegistry.INSTANCE.entries.values();
	}

}
