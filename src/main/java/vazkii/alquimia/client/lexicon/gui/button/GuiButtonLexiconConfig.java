package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexiconConfig extends GuiButtonLexicon {

	public GuiButtonLexiconConfig(GuiLexicon parent, int x, int y) {
		super(parent, x, y, 308, 20, 11, 11,
				I18n.translateToLocal("alquimia.gui.lexicon.button.config"));
	}

}
