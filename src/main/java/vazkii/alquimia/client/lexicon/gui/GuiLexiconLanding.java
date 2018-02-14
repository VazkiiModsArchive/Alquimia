package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonCategory;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconEdit;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconResize;
import vazkii.alquimia.common.item.ModItems;

public class GuiLexiconLanding extends GuiLexicon {

	LexiconTextRenderer text;
	
	@Override
	public void initGui() {
		super.initGui();
		
		text = new LexiconTextRenderer(this, fontRenderer, I18n.translateToLocal("alquimia.gui.lexicon.landing_info"), LEFT_PAGE_X, TOP_PADDING + 25, PAGE_WIDTH, TEXT_LINE_HEIGHT);

		buttonList.add(new GuiButtonLexiconResize(this, bookLeft + 24, bookTop + FULL_HEIGHT - 72));
		buttonList.add(new GuiButtonLexiconEdit(this, bookLeft + 44, bookTop + FULL_HEIGHT - 72));
		
		int i = 0;
		for(ResourceLocation res : LexiconRegistry.INSTANCE.CATEGORY_KEYS) {
			LexiconCategory category = LexiconRegistry.INSTANCE.CATEGORIES.get(res);
			
			int x = RIGHT_PAGE_X + 10 + (i % 4) * 24;
			int y = TOP_PADDING + 15 + (i /4) * 24;
			
			buttonList.add(new GuiButtonCategory(this, x, y, category));
			
			i++;
		}
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		text.render(mouseX, mouseY);
		
		String s = I18n.translateToLocal("alquimia.gui.lexicon.categories");
		fontRenderer.drawString(s, RIGHT_PAGE_X + PAGE_WIDTH / 2 - fontRenderer.getStringWidth(s) / 2, TOP_PADDING, 0x444444);
		
		drawHeader();
		drawProgressBar(mouseX, mouseY);
	}
	
	void drawHeader() {
		GlStateManager.color(1F, 1F, 1F, 1F);
		drawFromTexture(-8, 12, 0, 180, 140, 31);

		int color = 0xFFDD00;
		boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.drawString(new ItemStack(ModItems.lexicon).getDisplayName(), 13, 16, color);
		fontRenderer.setUnicodeFlag(true);
		fontRenderer.drawString(I18n.translateToLocal("alquimia.gui.lexicon.default_version"), 24, 24, color); // TODO hook up version to this
		fontRenderer.setUnicodeFlag(unicode);
	}
	
	void drawProgressBar(int mouseX, int mouseY) {
		int barLeft = 19;
		int barTop = FULL_HEIGHT - 36;
		int barWidth = PAGE_WIDTH - 10;
		int barHeight = 12;
		
		int totalEntries = 100;
		int unlockedEntries = 42;
		float unlockFract = (float) unlockedEntries / (float) totalEntries;
		int progressWidth = (int) (((float) barWidth - 2) * unlockFract);
		
		drawRect(barLeft, barTop, barLeft + barWidth, barTop + barHeight, 0xFF333333);
		drawGradientRect(barLeft + 1, barTop + 1, barLeft + barWidth - 1, barTop + barHeight - 1, 0xFFDDDDDD, 0xFFBBBBBB);
		drawGradientRect(barLeft + 1, barTop + 1, barLeft + progressWidth, barTop + barHeight - 1, 0xFFFFFF55, 0xFFBBBB00);

		fontRenderer.drawString(I18n.translateToLocal("alquimia.gui.lexicon.progress_meter"), barLeft, barTop - 9, 0x444444);
		
		String progressStr = unlockedEntries + "/" + totalEntries;
		if(isMouseInRelativeRange(mouseX, mouseY, barLeft, barTop, barWidth, barHeight))
			setTooltip(true, progressStr);
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
