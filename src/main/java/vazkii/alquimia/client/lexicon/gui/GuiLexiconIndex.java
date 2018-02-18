package vazkii.alquimia.client.lexicon.gui;

import java.util.Collection;

import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;

public class GuiLexiconIndex extends GuiLexiconEntryList {

	@Override
	String getName() {
		return I18n.translateToLocal("alquimia.gui.lexicon.index");
	}

	@Override
	String getDescriptionText() {
		return I18n.translateToLocal("alquimia.gui.lexicon.index.info");
	}

	@Override
	Collection<LexiconEntry> getEntries() {
		return LexiconRegistry.INSTANCE.entries.values();
	}

}
