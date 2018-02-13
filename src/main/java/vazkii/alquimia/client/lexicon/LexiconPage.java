package vazkii.alquimia.client.lexicon;

import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public abstract class LexiconPage {

	String type;
	
	public abstract void render(GuiLexicon lexicon, int mouseX, int mouseY);
	
}
