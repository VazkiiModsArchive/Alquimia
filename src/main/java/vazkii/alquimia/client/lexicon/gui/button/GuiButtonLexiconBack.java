package vazkii.alquimia.client.lexicon.gui.button;

import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexiconBack extends GuiButtonLexicon {

	public GuiButtonLexiconBack(GuiLexicon parent, int x, int y) {
		super(parent, x, y, 308, 0, 18, 9, () -> parent.canSeeBackButton());
	}
	
	
}
