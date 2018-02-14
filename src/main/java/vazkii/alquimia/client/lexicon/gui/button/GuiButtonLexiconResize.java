package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexiconResize extends GuiButtonLexicon {

	public GuiButtonLexiconResize(GuiLexicon parent, int x, int y) {
		super(parent, x, y, 330, 9, 11, 11,
				I18n.translateToLocal("alquimia.gui.lexicon.button.resize"));
	}

}
