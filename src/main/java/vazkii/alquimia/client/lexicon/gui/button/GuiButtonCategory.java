package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonCategory extends GuiButton {

	float hoverTime;
	
	GuiLexicon parent;
	LexiconCategory category;
	int u, v;
	
	public GuiButtonCategory(GuiLexicon parent, int x, int y, LexiconCategory category) {
		super(0, parent.bookLeft + x, parent.bookTop + y, 20, 20, "");
		this.parent = parent;
		this.u = x;
		this.v = y;
		this.category = category;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if(enabled && visible) {
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			
			RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemIntoGUI(category.getIconItem(), x + 2, y + 2);
			
			if(!hovered) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1F, 1F, 1F, 0.5F);
				GlStateManager.translate(0, 0, 200);
				GuiLexicon.drawFromTexture(x, y, u, v, width, height);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.popMatrix();
			} else parent.setTooltip(true, category.getName());
		}
	}

}
	