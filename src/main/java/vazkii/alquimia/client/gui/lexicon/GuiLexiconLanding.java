package vazkii.alquimia.client.gui.lexicon;

import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconRegistry;

public class GuiLexiconLanding extends GuiLexicon {

	LexiconTextRenderer text;
	
	@Override
	public void initGui() {
		super.initGui();
		
		String contents = "This is a test for the $(item)word rendering system$(). $(thing)The idea is that $(italic)text will be wrapped$(nocolor)"
				+ "around the page.$()$(br2)$(thing)Line breaks$() are now also working.$(br2)Also $(#d3d)featuring$(0) colors!$(br2)"
				+ "Testing $(o)other control codes now$() such as $(n)underline$(). Also resetting.$(br2)Lastly, $(l:intro/test2)clickable links$(/l) and codes $(a)in$(8)si$(c)de$() words.";
		
		text = new LexiconTextRenderer(this, fontRenderer, contents, RIGHT_PAGE_X, TOP_PADDING, PAGE_WIDTH, TEXT_LINE_HEIGHT);
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
