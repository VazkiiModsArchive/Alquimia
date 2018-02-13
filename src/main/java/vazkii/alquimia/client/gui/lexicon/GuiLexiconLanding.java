package vazkii.alquimia.client.gui.lexicon;

import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconRegistry;

public class GuiLexiconLanding extends GuiLexicon {

	LexiconTextRenderer text;
	
	@Override
	public void initGui() {
		super.initGui();
		
		String contents = "This is a test for the word rendering system. The idea is that text will be wrapped around the page.\n\nLine breaks (\\n) are now also working!";
		
		text = new LexiconTextRenderer(fontRenderer, contents, RIGHT_PAGE_X, TOP_PADDING, PAGE_WIDTH, TEXT_LINE_HEIGHT);
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		mc.fontRenderer.drawString("Categories:", 15, 20, 0);
		
		int y = 40;
		for(LexiconCategory category : LexiconRegistry.INSTANCE.CATEGORIES.values()) { 
			mc.fontRenderer.drawString(category.getName(), 15, y, 0);
			y += 10;
		}
		
		text.render(mouseX, mouseY);
	}

}
