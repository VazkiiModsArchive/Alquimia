package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;

import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconEntry;

public class GuiLexiconCategory extends GuiLexicon {

	LexiconCategory category;
	LexiconTextRenderer text;
	
	public GuiLexiconCategory(LexiconCategory category) {
		this.category = category;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		text = new LexiconTextRenderer(this, fontRenderer, category.getDescription(), LEFT_PAGE_X, TOP_PADDING + 16, PAGE_WIDTH, TEXT_LINE_HEIGHT);
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		super.drawForegroundElements(mouseX, mouseY, partialTicks);
		
		fontRenderer.drawString(category.getName(), LEFT_PAGE_X, TOP_PADDING + 4, 0x333333);
		text.render(mouseX, mouseY);
		
		fontRenderer.drawString(I18n.translateToLocal("alqmisc.wip"), RIGHT_PAGE_X + 10, TOP_PADDING + 12, 0x333333);
		
		int i = 0;
		for(LexiconEntry entry : category.getEntries()) {
			fontRenderer.drawString(entry.getName(), RIGHT_PAGE_X + 12, TOP_PADDING + 24 + i * 10, 0x333333);
			i++;
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		text.click(mouseX, mouseY);
	}
	
}
