package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.gui.GuiButton;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;

public class PageEmpty extends LexiconPage {

	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		GuiLexicon.drawPageFiller(0, 0);
	}

}
