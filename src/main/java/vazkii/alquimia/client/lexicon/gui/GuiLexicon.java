package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import scala.actors.threadpool.Arrays;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconBack;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconLR;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.util.RenderHelper;

public abstract class GuiLexicon extends GuiScreen {

	public static final ResourceLocation LEXICON_TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/lexicon/lexicon.png"); 

	public static final int FULL_WIDTH = 272;
	public static final int FULL_HEIGHT = 180;
	public static final int PAGE_WIDTH = 116;
	public static final int PAGE_HEIGHT = 156;
	public static final int TOP_PADDING = 18;
	public static final int LEFT_PAGE_X = 15;
	public static final int RIGHT_PAGE_X = 141;
	public static final int TEXT_LINE_HEIGHT = 9;

	public static Stack<GuiLexicon> guiStack = new Stack();
	public static GuiLexicon currentGui;
	public int bookLeft, bookTop;

	private List<String> tooltip;
	private boolean tooltipLocked = false;
	protected int page, maxpages;

	public static GuiLexicon getCurrentGui() {
		if(currentGui == null)
			currentGui = new GuiLexiconLanding();

		return currentGui;
	}

	public static void onReload() {
		currentGui = null;
		guiStack.clear();
	}

	public static void displayLexiconGui(GuiLexicon gui, boolean push) {
		Minecraft mc = Minecraft.getMinecraft();
		if(push && mc.currentScreen instanceof GuiLexicon && gui != mc.currentScreen)
			guiStack.push((GuiLexicon) mc.currentScreen);

		mc.displayGuiScreen(gui);
	}

	@Override
	public void initGui() {
		page = maxpages = 0;
		
		bookLeft = width / 2 - FULL_WIDTH / 2;
		bookTop = height / 2 - FULL_HEIGHT / 2;

		currentGui = this;

		buttonList.clear();

		buttonList.add(new GuiButtonLexiconBack(this, width / 2 - 9, bookTop + FULL_HEIGHT - 5));
		buttonList.add(new GuiButtonLexiconLR(this, bookLeft - 4, bookTop + FULL_HEIGHT - 6, true));
		buttonList.add(new GuiButtonLexiconLR(this, bookLeft + FULL_WIDTH - 14, bookTop + FULL_HEIGHT - 6, false));
	}

	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		GlStateManager.pushMatrix();
		GlStateManager.translate(bookLeft, bookTop, 0);
		drawBackgroundElements(mouseX, mouseY, partialTicks);
		drawForegroundElements(mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);

		drawTooltip(mouseX, mouseY);
	}

	final void drawBackgroundElements(int mouseX, int mouseY, float partialTicks) {
		drawFromTexture(0, 0, 0, 0, FULL_WIDTH, FULL_HEIGHT);
	}

	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) { }

	final void drawTooltip(int mouseX, int mouseY) {
		if(tooltip != null && !tooltip.isEmpty())
			RenderHelper.renderTooltip(mouseX, mouseY, tooltip);

		tooltip = null;
		tooltipLocked = false;
	}

	public static void drawFromTexture(int x, int y, int u, int v, int w, int h) {
		Minecraft.getMinecraft().renderEngine.bindTexture(LEXICON_TEXTURE);
		drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, 512, 256);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if(button instanceof GuiButtonLexiconBack)
			back();
		else if(button instanceof GuiButtonLexiconLR)
			changePage(((GuiButtonLexiconLR) button).left);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		switch(mouseButton) {
		case 1: 
			back();
			break;
		case 3:  
			changePage(true);
			break;
		case 4:
			changePage(false);
			break;
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		int w = Mouse.getEventDWheel();
		if(w < 0)
			changePage(false);
		else if(w > 0)
			changePage(true);
	}

	void back() {
		if(!guiStack.isEmpty()) {
			if(isShiftKeyDown()) {
				displayLexiconGui(new GuiLexiconLanding(), false);
				guiStack.clear();
			} else displayLexiconGui(guiStack.pop(), false);
		}
	}

	void changePage(boolean left) {
		if(canSeePageButton(left)) {
			int oldpage = page;
			if(left)
				page--;
			else page++;
			
			onPageChanged();
		}
	}
	
	void onPageChanged() {
		// NO-OP
	}

	public boolean canSeePageButton(boolean left) {
		return left ? page > 0 : (page + 1) < maxpages; 
	}
	
	public boolean canSeeBackButton() {
		return !guiStack.isEmpty();
	}

	public void setTooltip(boolean locked, String... strings) {
		setTooltip(locked, Arrays.asList(strings));
	}

	public void setTooltip(boolean locked, List<String> strings) {
		if(!tooltipLocked) {
			tooltip = strings;
			this.tooltipLocked = locked;
		}
	}

	public boolean isMouseInRelativeRange(int absMx, int absMy, int x, int y, int w, int h) {
		int mx = absMx - bookLeft;
		int my = absMy - bookTop;
		return mx > x && my > y && mx <= (x + w) && my <= (y + h);
	}

	public void drawProgressBar(int mouseX, int mouseY, Predicate<LexiconEntry> filter) {
		int barLeft = 19;
		int barTop = FULL_HEIGHT - 36;
		int barWidth = PAGE_WIDTH - 10;
		int barHeight = 12;

		int totalEntries = 100;
		int unlockedEntries = 20;
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
	
	public void drawSeparator(int x, int y) {
		int w = 110;
		int h = 3;
		int rx = x + PAGE_WIDTH / 2 - w / 2;
		
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 0.8F);
		drawFromTexture(rx, y, 140, 180, w, h);
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
	
	public void drawCenteredStringNoShadow(String s, int x, int y, int color) {
		fontRenderer.drawString(s, x - fontRenderer.getStringWidth(s) / 2, y, color);
	}
	
}

