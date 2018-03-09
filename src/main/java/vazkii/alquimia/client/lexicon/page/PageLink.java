package vazkii.alquimia.client.lexicon.page;

import java.net.URI;
import java.net.URISyntaxException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNoCallback;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.client.lexicon.page.abstr.PageWithText;

public class PageLink extends PageWithText {

	String url;
	String link_text;

	transient GuiButton linkButton;

	@Override
	public void onDisplayed(GuiLexiconEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);

		adddButton(linkButton = new GuiButton(0, GuiLexicon.PAGE_WIDTH / 2 - 50, GuiLexicon.PAGE_HEIGHT - 35, 100, 20, link_text));
	}
	
	@Override
	public int getTextHeight() {
		return 0;
	}

	@Override
	protected void onButtonClicked(GuiButton button) {
		super.onButtonClicked(button);

		if(button == linkButton)
			try {
				openWebLink(new URI(url));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
	}

	private void openWebLink(URI url) {
		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop").invoke(null);
			oclass.getMethod("browse", URI.class).invoke(object, url);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
