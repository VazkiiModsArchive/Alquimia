package vazkii.alquimia.client.gui.lexicon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.lib.LibMisc;

public abstract class GuiLexicon extends GuiScreen {

	public static ResourceLocation LEXICON_TEXTURE = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/lexicon.png"); 
	
	public static GuiLexicon currentGui;
	public int bookLeft, bookTop, bookWidth, bookHeight;

	public static GuiLexicon getCurrentGui() {
		if(currentGui == null)
			currentGui = new GuiLexiconLanding();
		
		return currentGui;
	}
	
	@Override
	public void initGui() {
		bookWidth = 272;
		bookHeight = 180;
		bookLeft = width / 2 - bookWidth / 2;
		bookTop = height / 2 - bookHeight / 2;
		
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
		drawFromTexture(0, 0, 0, 0, bookWidth, bookHeight);
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
