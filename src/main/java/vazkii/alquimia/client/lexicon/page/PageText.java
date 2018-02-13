package vazkii.alquimia.client.lexicon.page;

import vazkii.alquimia.client.gui.lexicon.GuiLexicon;
import vazkii.alquimia.client.lexicon.LexiconPage;

public class PageText extends LexiconPage {

	String text;
	
	@Override
	public void render(GuiLexicon lexicon, int mouseX, int mouseY) {
		lexicon.mc.fontRenderer.drawString(text, 0, 0, 0); // TODO
	}

}
