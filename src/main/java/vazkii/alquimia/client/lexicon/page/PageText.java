package vazkii.alquimia.client.lexicon.page;

import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;

public class PageText extends LexiconPage {

	String text;

	transient LexiconTextRenderer textRender;
	
	@Override
	public void onDisplayed(GuiLexiconEntry parent) {
		super.onDisplayed(parent);
		
		textRender = new LexiconTextRenderer(parent, text, 0, pageNum == 0 ? 22 : 0);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		if(pageNum == 0) {
			parent.drawCenteredStringNoShadow(parent.getEntry().getName(), GuiLexicon.PAGE_WIDTH / 2, 0, 0x333333);
			parent.drawSeparator(0, 12);
		}
		
		textRender.render(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		textRender.click(mouseX, mouseY, mouseButton);
	}

}
