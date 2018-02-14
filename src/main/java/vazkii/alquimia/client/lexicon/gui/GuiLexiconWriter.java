package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.translation.I18n;

public class GuiLexiconWriter extends GuiLexicon {

	LexiconTextRenderer text, editableText;
	GuiTextField textfield;
	
	private static String savedText = "";

	@Override
	public void initGui() {
		super.initGui();
		
		text = new LexiconTextRenderer(this, I18n.translateToLocal("alquimia.gui.lexicon.editor_info"), LEFT_PAGE_X, TOP_PADDING + 20);
		textfield = new GuiTextField(0, fontRenderer, 10, FULL_HEIGHT - 40, PAGE_WIDTH, 20);
		textfield.setMaxStringLength(Integer.MAX_VALUE);
		textfield.setText(savedText);
		
		Keyboard.enableRepeatEvents(true);
		refreshText();
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		super.drawForegroundElements(mouseX, mouseY, partialTicks);
		
		mc.fontRenderer.drawString(I18n.translateToLocal("alquimia.gui.lexicon.editor"), LEFT_PAGE_X, TOP_PADDING + 5, 0);
		
		textfield.drawTextBox();
		text.render(mouseX, mouseY);
		editableText.render(mouseX, mouseY);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		textfield.mouseClicked(mouseX - bookLeft, mouseY - bookTop, mouseButton);
		
		text.click(mouseX, mouseY, mouseButton);
		editableText.click(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
		textfield.textboxKeyTyped(typedChar, keyCode);
		refreshText();
	}
	
	public void refreshText() {
		savedText = textfield.getText();
		try {
			editableText = new LexiconTextRenderer(this, savedText, RIGHT_PAGE_X, TOP_PADDING);
		} catch(Throwable e) {
			editableText = new LexiconTextRenderer(this, "[ERROR]", RIGHT_PAGE_X, TOP_PADDING);
		}
	}
}
