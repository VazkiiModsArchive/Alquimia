package vazkii.alquimia.client.gui.lexicon;

import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconRegistry;

public class GuiLexiconLanding extends GuiLexicon {

	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		int y = 20;
		for(LexiconCategory category : LexiconRegistry.INSTANCE.CATEGORIES.values()) { 
			mc.fontRenderer.drawString(category.getName(), 15, y, 0);
			y += 10;
		}
	}

}
