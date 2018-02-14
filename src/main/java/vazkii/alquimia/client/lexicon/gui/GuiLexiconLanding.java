package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconEdit;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconResize;
import vazkii.alquimia.common.item.ModItems;

public class GuiLexiconLanding extends GuiLexicon {

	LexiconTextRenderer text;
	
	@Override
	public void initGui() {
		super.initGui();
		
		String contents = "This is a test for the word rendering system. The idea is that $(italic)text will be wrapped$()"
				+ "around the page.$(br2)$(thing)Line breaks$() are now also working.$(br2)Also $(#d3d)featuring colors$(0)!$(br2)"
				+ "Testing $(o)other control codes now$() such as $(n)underline$(). Also resetting.$(br2)Lastly, $(l:intro/test2)clickable links$(/l) and codes $(a)in$(8)si$(c)de$() words.";
		
		text = new LexiconTextRenderer(this, fontRenderer, contents, RIGHT_PAGE_X, TOP_PADDING, PAGE_WIDTH, TEXT_LINE_HEIGHT);
		
		buttonList.add(new GuiButtonLexiconResize(this, bookLeft + 24, bookTop + FULL_HEIGHT - 25));
		buttonList.add(new GuiButtonLexiconEdit(this, bookLeft + 38, bookTop + FULL_HEIGHT - 25));
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		mc.fontRenderer.drawString(new ItemStack(ModItems.lexicon).getDisplayName(), 15, 20, 0);

		text.render(mouseX, mouseY);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		text.click(mouseX, mouseY);
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if(button instanceof GuiButtonLexiconEdit)
			displayLexiconGui(new GuiLexiconWriter(), true);
	}

}
