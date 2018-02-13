package vazkii.alquimia.client.lexicon.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.lib.LibMisc;

public abstract class GuiLexicon extends GuiScreen {

	public static ResourceLocation LEXICON_TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/lexicon.png"); 
	
	public static final int FULL_WIDTH = 272;
	public static final int FULL_HEIGHT = 180;
	public static final int PAGE_WIDTH = 116;
	public static final int PAGE_HEIGHT = 156;
	public static final int TOP_PADDING = 18;
	public static final int LEFT_PAGE_X = 15;
	public static final int RIGHT_PAGE_X = 141;
	public static final int TEXT_LINE_HEIGHT = 9;

	public static GuiLexicon currentGui;
	public int bookLeft, bookTop;

	public static GuiLexicon getCurrentGui() {
		if(currentGui == null)
			currentGui = new GuiLexiconLanding();
		
		return currentGui;
	}
	
	@Override
	public void initGui() {
		bookLeft = width / 2 - FULL_WIDTH / 2;
		bookTop = height / 2 - FULL_HEIGHT / 2;
		
		currentGui = this;
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
	}
	
	final void drawBackgroundElements(int mouseX, int mouseY, float partialTicks) {
		drawFromTexture(0, 0, 0, 0, FULL_WIDTH, FULL_HEIGHT);
	}
	
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) { }
	
	public static void drawFromTexture(int x, int y, int u, int v, int w, int h) {
		Minecraft.getMinecraft().renderEngine.bindTexture(LEXICON_TEXTURE);
		drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, 512, 256);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
