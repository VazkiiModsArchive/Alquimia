package vazkii.alquimia.client.lexicon.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.text.TextFormatting;
import vazkii.alquimia.client.lexicon.LexiconEntry;
import vazkii.alquimia.client.lexicon.gui.GuiLexicon;

public class GuiButtonEntry extends GuiButton {
	
	GuiLexicon parent;
	LexiconEntry entry;
	int i;

	public GuiButtonEntry(GuiLexicon parent, int x, int y, LexiconEntry entry, int i) {
		super(0, x, y, GuiLexicon.PAGE_WIDTH, 10, "");
		this.parent = parent;
		this.entry = entry;
		this.i = i;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if(enabled && visible) {
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			
			if(hovered)
				Gui.drawRect(x, y, x + width, y + height, 0x33000000);
			
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			mc.getRenderItem().renderItemIntoGUI(entry.getIconItem(), x * 2, y * 2 + 2);
			GlStateManager.scale(2F, 2F, 2F);
			
			boolean unicode = mc.fontRenderer.getUnicodeFlag();
			mc.fontRenderer.setUnicodeFlag(true);
			mc.fontRenderer.drawString(entry.getName() + "(" + i + ")", x + 10, y, 0); // TODO handle priority display
			mc.fontRenderer.setUnicodeFlag(unicode);
		}
	}
	
	public LexiconEntry getEntry() {
		return entry;
	}

}
