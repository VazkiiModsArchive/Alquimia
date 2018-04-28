package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonCategory;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonEntry;

public class GuiLexiconCategory extends GuiLexiconEntryList {

	LexiconCategory category;
	
	public GuiLexiconCategory(LexiconCategory category) {
		this.category = category;
	}

	@Override
	protected String getName() {
		return category.getName();
	}

	@Override
	protected String getDescriptionText() {
		return category.getDescription();
	}

	@Override
	protected Collection<LexiconEntry> getEntries() {
		return category.getEntries();
	}
	
	@Override
	protected void addSubcategoryButtons() {
		int i = 0;
		List<LexiconCategory> categories = new ArrayList(LexiconRegistry.INSTANCE.categories.values());
		Collections.sort(categories);
		
		for(LexiconCategory ocategory : categories) {
			if(ocategory.getParentCategory() != category)
				continue;
			
			int x = LEFT_PAGE_X + 10 + (i % 4) * 24;
			int y = TOP_PADDING + PAGE_HEIGHT - 68;
			
			GuiButton button = new GuiButtonCategory(this, x, y, ocategory);
			buttonList.add(button);
			dependentButtons.add(button);
			
			i++;
		}
	}
	
	@Override
	protected boolean doesEntryCountForProgress(LexiconEntry entry) {
		return entry.getCategory() == category;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof GuiLexiconCategory && ((GuiLexiconCategory) obj).category == category && ((GuiLexiconCategory) obj).page == page);
	}
	
	@Override
	boolean canBeOpened() {
		return !category.isLocked() && !equals(Minecraft.getMinecraft().currentScreen);
	}
	
}
