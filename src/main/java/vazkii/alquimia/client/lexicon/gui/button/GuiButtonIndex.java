package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.common.item.ModItems;

public class GuiButtonIndex extends GuiButtonCategory {

	public GuiButtonIndex(GuiLexicon parent, int x, int y) {
		super(parent, x, y, new ItemStack(ModItems.lexicon), I18n.translateToLocal("alquimia.gui.lexicon.index"));
	}

}
