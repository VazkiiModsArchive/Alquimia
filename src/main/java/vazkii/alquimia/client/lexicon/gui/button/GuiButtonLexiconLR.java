package vazkii.alquimia.client.lexicon.gui.button;

import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexiconLR extends GuiButtonLexicon {

	public final boolean left;
	
	public GuiButtonLexiconLR(GuiLexicon parent, int x, int y, boolean left) {
		super(parent, x, y, 272, left ? 10 : 0, 18, 10, () -> parent.canSeePageButton(left));
		this.left = left;
	}

}
