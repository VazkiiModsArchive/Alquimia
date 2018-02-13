package vazkii.alquimia.client.lexicon.page;

import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class PageText extends LexiconPage {

	String text;
	
	@Override
	public void render(GuiLexicon lexicon, int mouseX, int mouseY) {
		lexicon.mc.fontRenderer.drawString(text, 0, 0, 0); // TODO
	}

}
