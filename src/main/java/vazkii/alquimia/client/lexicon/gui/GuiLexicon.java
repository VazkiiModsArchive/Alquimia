package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconBack;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconLR;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.arl.util.RenderHelper;

public abstract class GuiLexicon extends GuiScreen {

	public static final ResourceLocation LEXICON_TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/lexicon.png"); 
	
	public static final int FULL_WIDTH = 272;
	public static final int FULL_HEIGHT = 180;
	public static final int PAGE_WIDTH = 116;
	public static final int PAGE_HEIGHT = 156;
	public static final int TOP_PADDING = 18;
	public static final int LEFT_PAGE_X = 15;
	public static final int RIGHT_PAGE_X = 141;
	public static final int TEXT_LINE_HEIGHT = 9;
	
	public static final int BACK = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;

	public static Stack<GuiLexicon> guiStack = new Stack();
	public static GuiLexicon currentGui;
	public int bookLeft, bookTop;
	
	private List<String> tooltip;
	private boolean tooltipLocked = false;

	public static GuiLexicon getCurrentGui() {
		if(currentGui == null)
			currentGui = new GuiLexiconLanding();
		
		return currentGui;
	}
	
	public static void displayLexiconGui(GuiLexicon gui, boolean push) {
		Minecraft mc = Minecraft.getMinecraft();
		if(push && mc.currentScreen instanceof GuiLexicon && gui != mc.currentScreen)
			guiStack.push((GuiLexicon) mc.currentScreen);
		
		mc.displayGuiScreen(gui);
	}
	
	@Override
	public void initGui() {
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
	
	void back() {
		if(!guiStack.isEmpty()) {
			if(isShiftKeyDown()) {
				displayLexiconGui(new GuiLexiconLanding(), false);
				guiStack.clear();
			} else displayLexiconGui(guiStack.pop(), false);
		}
	}
	
	void changePage(boolean left) { 
		// NO-OP
	}
	
	public boolean canSeePageButton(boolean left) {
		return false;
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
	
}

