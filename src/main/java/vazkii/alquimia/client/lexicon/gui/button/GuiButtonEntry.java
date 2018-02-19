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
	
	private static final int ANIM_TIME = 5;

	GuiLexicon parent;
	LexiconEntry entry;
	int i;
	float timeHovered;

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
				timeHovered = Math.min(ANIM_TIME, timeHovered + parent.timeDelta);
			else timeHovered = Math.max(0, timeHovered - parent.timeDelta);
			
			float time = Math.max(0, Math.min(ANIM_TIME, timeHovered + (hovered ? partialTicks : -partialTicks)));
			float widthFract = (float) time / ANIM_TIME;
			
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			Gui.drawRect(x * 2, y * 2, (x + (int) ((float) width * widthFract)) * 2, (y + height) * 2, 0x22000000);
			
			RenderHelper.enableGUIStandardItemLighting();
			
			mc.getRenderItem().renderItemIntoGUI(entry.getIconItem(), x * 2 + 2, y * 2 + 2);
			GlStateManager.scale(2F, 2F, 2F);
			
			boolean unicode = mc.fontRenderer.getUnicodeFlag();
			mc.fontRenderer.setUnicodeFlag(true);
			mc.fontRenderer.drawString((entry.isPriority() ? TextFormatting.ITALIC : "") + entry.getName(), x + 12, y, 0);
			mc.fontRenderer.setUnicodeFlag(unicode);
		}
	}
	
	public LexiconEntry getEntry() {
		return entry;
	}

}
