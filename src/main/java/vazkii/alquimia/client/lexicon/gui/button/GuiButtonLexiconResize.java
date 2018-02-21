package vazkii.alquimia.client.lexicon.gui.button;

import java.util.List;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import scala.actors.threadpool.Arrays;
import vazkii.alquimia.client.base.PersistentData;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonLexiconResize extends GuiButtonLexicon {

	public GuiButtonLexiconResize(GuiLexicon parent, int x, int y) {
		super(parent, x, y, 330, 9, 11, 11,
				I18n.translateToLocal("alquimia.gui.lexicon.button.resize"));
	}
	
	@Override
	public List<String> getTooltip() {
		return Arrays.asList(new String[] { 
				tooltip.get(0),
				TextFormatting.GRAY + I18n.translateToLocalFormatted("alquimia.gui.lexicon.button.resize.size" + PersistentData.data.lexiconGuiScale)});
	}

}
