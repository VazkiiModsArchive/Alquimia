package vazkii.alquimia.client.lexicon.page.abstr;

import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;

public abstract class PageWithText extends LexiconPage {

	String text;

	transient LexiconTextRenderer textRender;
	
	@Override
	public void onDisplayed(GuiLexiconEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		textRender = new LexiconTextRenderer(parent, text, 0, getTextHeight());
	}
	
	public abstract int getTextHeight();
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		if(shouldRenderText())
			textRender.render(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(shouldRenderText())
			textRender.click(mouseX, mouseY, mouseButton);
	}
	
	public boolean shouldRenderText() {
		return true;
	}

}
