package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import vazkii.alquimia.client.lexicon.LexiconCategory;

public class GuiButtonCategory extends GuiButton {

	LexiconCategory category;
	
	public GuiButtonCategory(int x, int y, LexiconCategory category) {
		super(0, x, y, 20, 20, "");
		this.category = category;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if(enabled && visible) {
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			
			RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemIntoGUI(category.getIconItem(), x + 2, y + (hovered ? 0 : 2));
		}
	}

}
	