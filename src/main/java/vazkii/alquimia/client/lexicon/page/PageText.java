package vazkii.alquimia.client.lexicon.page;

import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.client.lexicon.page.abstr.PageWithText;

public class PageText extends PageWithText {
	
	@Override
	public int getTextHeight() {
		return pageNum == 0 ? 22 : 0;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		super.render(mouseX, mouseY, pticks);
		
		if(pageNum == 0) {
			parent.drawCenteredStringNoShadow(parent.getEntry().getName(), GuiLexicon.PAGE_WIDTH / 2, 0, 0x333333);
			parent.drawSeparator(0, 12);
		}
	}

}
