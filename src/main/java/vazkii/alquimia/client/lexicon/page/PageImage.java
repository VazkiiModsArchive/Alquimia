package vazkii.alquimia.client.lexicon.page;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.client.lexicon.LexiconPage;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;
import vazkii.alquimia.client.lexicon.gui.GuiLexiconEntry;
import vazkii.alquimia.client.lexicon.gui.LexiconTextRenderer;
import vazkii.alquimia.client.lexicon.page.abstr.PageWithText;
import vazkii.arl.util.RenderHelper;

public class PageImage extends PageWithText {

	String image;
	boolean border;

	transient ResourceLocation imageRes;
	
	@Override
	public void onDisplayed(GuiLexiconEntry parent, int left, int top) {
		super.onDisplayed(parent, left, top);
		
		imageRes = new ResourceLocation(image);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float pticks) {
		mc.renderEngine.bindTexture(imageRes);
		
		int x = GuiLexicon.PAGE_WIDTH / 2 - 53;
		int y = 6;
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		parent.drawTexturedModalRect(x * 2 + 6, y * 2 + 6, 0, 0, 200, 200);
		if(border) {
			GlStateManager.scale(2F, 2F, 2F);
			GuiLexicon.drawFromTexture(x, y, 405, 149, 106, 106);
		}
		GlStateManager.popMatrix();
		
		super.render(mouseX, mouseY, pticks);
	}

	@Override
	public int getTextHeight() {
		return 120;
	}
	
}
